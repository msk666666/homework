package ChatRoom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public abstract class AbstractMethod {
    public void receive(InputStream in, OutputStream out,Socket socket) throws IOException {
        while (true) {
            //接受命令
            int cmd = in.read();
            //读取发送内容长度高八位
            int lengthHigh = in.read();
            //读取发送内容长度低八位
            int lengthLower = in.read();
            //计算发送内容长度
            int length=(lengthHigh<<8)+lengthLower;
            //根据发送内容长度创建接收数组
            byte[] bytes = new byte[length];
            //读取接收内容
            in.read(bytes);
            String content = new String(bytes, 0, length);
            handle(cmd,content,in,out,socket);
        }
    }

    protected abstract void handle(int cmd, String content, InputStream in, OutputStream out, Socket socket) throws IOException;


    public  void send(int cmd,String content,OutputStream out) throws IOException {
        //发送命令
        out.write(cmd);
        byte[] bytes = content.getBytes("utf-8");
        int length =bytes.length;
        //发送内容长度的高八位
        out.write(length>>8);
        //发送内容长度的低八位
        out.write(0XFF&length);
        //发送内容
        out.write(bytes);

    }
}
