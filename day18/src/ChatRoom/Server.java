package ChatRoom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends AbstractMethod {
    private  ConcurrentHashMap<Socket,String> map=new ConcurrentHashMap();
    private String userLeave;
    public static void main(String[] args) throws IOException {
        Server server1 = new Server();
        ServerSocket server = new ServerSocket(5000);
        System.out.println("服务器已启动");

        while (true){
            Socket accept = server.accept();
            ExecutorService pool = Executors.newFixedThreadPool(8);
            pool.submit(()->{
                try (
                        InputStream in = accept.getInputStream();
                        OutputStream out = accept.getOutputStream();
                       )
               {
                   server1.receive(in,out,accept);
               }catch (Exception e){
                    //如果用户离线
                    userLeave(server1,accept);
                }
            });
        }
    }

    public static void userLeave(Server server1,Socket accept) {
        Set<Map.Entry<Socket, String>> deleteEntries = server1.map.entrySet();
        for (Map.Entry<Socket, String> entry : deleteEntries) {
            //用户离线，该用户的socket对象就会关闭
            if(entry.getKey().isClosed()){
                try {
                    String username=entry.getValue();
                    server1.map.remove(entry.getKey(),entry.getValue());
                    for (Socket socket : server1.map.keySet()) {
                        server1.send(7,"用户"+username+"已经离线",socket.getOutputStream());
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    @Override
    protected void handle(int cmd, String content, InputStream in, OutputStream out, Socket socket) throws IOException {
            switch (cmd){
                //接收用户名并发送响应信息
                case 1:
                    if(map.values().contains(content)){
                        send(5,"用户已存在",out);
                    }else {
                        map.put(socket,content);
                        send(5,"欢迎["+content+"]来到聊天室",out);
                    }
                    break;
                //响应发送所有在线用户
                case 2:
                    Collection<String> nameMap = map.values();
                    send(6,nameMap.toString(),out);
                    break;
                case 3:
                    String sendUser = map.get(socket);
                    String info = sendUser + "说：" + content;
                    for (Socket users : map.keySet()) {
                        send(7,info,users.getOutputStream());
                    }
                    break;
                case 4:
                    String sendName = content.split(" ")[0];
                    String sendContent = content.split(" ")[1];
                    if(map.values().contains(sendName)){
                        for (Map.Entry<Socket, String> entry : map.entrySet()) {
                            if(entry.getValue().equals(sendName)){
                                String sendUser1 = map.get(socket);
                                String info1 = sendUser1 + "私聊：" + sendContent;
                                send(8,info1,entry.getKey().getOutputStream());
                            }
                        }
                    }else {
                        send(8,"该用户不存在",out);
                    }
            }
        }
    }

