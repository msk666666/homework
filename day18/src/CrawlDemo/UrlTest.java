package CrawlDemo;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UrlTest {

    //获取整个html页面
    public static InputStream getHTML (String link) throws IOException {
        //链接url返回整个html网页
        HttpURLConnection url = (HttpURLConnection)
                new URL(link).openConnection();
        System.out.println("网络已连接");
        InputStream in = url.getInputStream();
        return in;
    }

    public static void main(String[] args) throws Exception {
        String webUrl = "https://tieba.baidu.com/p/2256306796?red_tag=1781367364";
        String dirPath = "C:\\Users\\WIN10\\Desktop\\java\\homework\\day18\\imgDir";

        //创建阻塞队列
        ArrayBlockingQueue<String> urlArray = new ArrayBlockingQueue<>(3);
        InputStream in = getHTML(webUrl);
        new Geturl("Pro1",urlArray,webUrl,in).start();
        new DownloadImg("Con1",urlArray,dirPath).start();
        new DownloadImg("Con2",urlArray,dirPath).start();
    }

}

//匹配url
class Geturl extends Thread{
    private BlockingQueue<String> abq;
    private String link;
    private InputStream in;

    public Geturl(String getThName,BlockingQueue<String> abq,String link,InputStream in) {
        super(getThName);
        this.in=in;
        this.abq=abq;
        this.link=link;
    }
    @Override
    public void run() {
        try {
            getUrl(in);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //爬取的图片url
    public synchronized void getUrl(InputStream in) throws IOException, InterruptedException {
        //抓取网页中的图片链接
        Pattern p = Pattern.compile("<img class=\\\"BDE_Image\\\" src=\\\"(.*?)\\\"");
        BufferedReader buffReader = new BufferedReader(new InputStreamReader(in));
        while (buffReader.readLine()!=null){
            String line= buffReader.readLine();
            if(line!=null){
                Matcher m = p.matcher(line);
                while (m.find()){
                    //将匹配到的url加入阻塞队列，如果队列已满则阻塞
                    abq.put(m.group(1));
                    System.out.println(Thread.currentThread().getName()+"爬取图片url完成");
                }
            }
        }
        buffReader.close();
    }
}

//下载图片
class DownloadImg extends Thread{
    private BlockingQueue<String> abq;
    private String path;

    public DownloadImg(String getThName,BlockingQueue<String> abq, String path) {
        super(getThName);
        this.abq = abq;
        this.path = path;
    }
    @Override
    public void run( ) {
        while (true){
            try {
                downloadimg(path);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void downloadimg(String path) throws IOException, InterruptedException {
        //从阻塞队列中拿出url，如果为空则阻塞
        String url=abq.take();
        System.out.println(Thread.currentThread().getName()+"下载图片完成");
        HttpURLConnection imgurl =(HttpURLConnection)new URL(url).openConnection();
        InputStream imgin = imgurl.getInputStream();
        String imgPath=path+"\\"+url.substring(url.lastIndexOf("/") + 1);
        FileOutputStream imgout = new FileOutputStream(imgPath);
        byte[] imgbuff = new byte[1024 * 8];
        int hasRead=0;
        while ((hasRead=imgin.read(imgbuff))>0){
            imgout.write(imgbuff,0,hasRead);
        }
        imgin.close();
        imgout.close();
        imgurl.disconnect();

    }
}

