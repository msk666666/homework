package ChatRoom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client extends AbstractMethod {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        Socket socket = new Socket("127.0.0.1",5000);
        System.out.print("请输入用户名：");
        Scanner nameSc = new Scanner(System.in);
        String name = nameSc.nextLine();
        OutputStream nameOut = socket.getOutputStream();
        client.send(1,name,nameOut);
        //从键盘获取命令并发送
        new Thread(()->{
            try (
                    OutputStream out=socket.getOutputStream();
                    Scanner sc = new Scanner(System.in);
                    )
            {
                while (sc.hasNextLine()){
                    String line = sc.nextLine();
                    char cmd = line.charAt(0);
                    switch (cmd){
                        case '1':
                            String content1=line.substring(2);
                            client.send(1,content1,out);
                            break;
                        case '2':
                            client.send(2,"",out);
                            break;
                        case '3':
                            String content3=line.substring(2);
                            client.send(3,content3,out);
                            break;
                        case '4':
                            String content4=line.substring(2);
                            client.send(4,content4,out);
                            break;
                        case '0':
                            System.exit(0);
                            break;
                        default:
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }).start();

        //接受服务端响应过来的消息
        new Thread(()->{

            try (
                    InputStream in = socket.getInputStream();
                    OutputStream out = socket.getOutputStream();
            )
            {
                client.receive(in,out,socket);
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();

    }


    @Override
    protected void handle(int cmd, String content, InputStream in, OutputStream out, Socket socket) {
            switch (cmd){
                case 5:
                    System.out.println(content);
                    break;
                case 6:
                    System.out.println(content);
                    break;
                case 7:
                    System.out.println(content);
                    break;
                case 8:
                    System.out.println(content);
                    break;
                case 9:
                    System.out.println(content);
                    break;
            }
        }

    }

