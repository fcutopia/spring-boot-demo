package com.fc.springboot.utils;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.NotYetConnectedException;
import java.util.Set;

public class WebSocketUtils /*extends WebSocketServer*/ {

    /*public UploadServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(InetAddress.getByName("192.168.137.178"), port));
    }

    public UploadServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        try {
            this.sendToAll("new");
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println(conn + " entered the room!");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        try {
            this.sendToAll(conn + " has left the room!");
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println(conn + " has left the room!");
    }

    public String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        // try {
        // this.sendToAll( message );
        // } catch ( InterruptedException ex ) {
        // ex.printStackTrace();
        // }
        // System.out.println( conn + ": " + message );
        System.out.println("文件名" + message);
        // 将文件名写入连接对象中,(需要手动修改webSocket类)
        this.setFileName(message);
        try {
            conn.send("ok");
        } catch (NotYetConnectedException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onMessage(WebSocket conn, byte[] message) {
        System.out.println("收到二进制流:");
        // 将二进制流保存为文件, 文件名从连接对象中取出
        saveFileFromBytes(message, "D:/c/apache-tomcat-7.0.62/webapps/websocketchat/src/" + this.getFileName());
        // 告诉前台可以继续发送了.
        try {
            conn.send("ok");
        } catch (NotYetConnectedException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean saveFileFromBytes(byte[] b, String outputFile) {
        FileOutputStream fstream = null;
        File file = null;
        try {
            file = new File(outputFile);
            fstream = new FileOutputStream(file, true);
            fstream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fstream != null) {
                try {
                    fstream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    public void sendToAll(String text) throws InterruptedException {
        Set<WebSocket> con = connections();
        synchronized (con) {
            for (WebSocket c : con) {
                c.send(text);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        int port = 8887;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception ex) {
        }
        WebSocketUtils s = new WebSocketUtils(port);
        s.start();
        System.out.println("UploadServer started on port: " + s.getPort());
        // 服务端 发送消息处理部份
        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String in = sysin.readLine();
            s.sendToAll(in);
        }
    }*/
}
