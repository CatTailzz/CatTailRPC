package com.cattail.socket.server;

import com.cattail.socket.MyObject;
import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/26
 * @Copyright: https://github.com/CatTailzz
 */
public class Server {
    static int port = 12345;

    public static void main(String[] args) throws IOException {
        runWithObject();
    }

    // 普通
    public static void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is listening on port" + port);

        try {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accept connection from" + clientSocket.getInetAddress().getHostAddress());

            InputStream in = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            while (true) {
                String message = reader.readLine();
                if (message == null) {
                    break;
                }
                System.out.println("Received message from client:" + message);
            }
        } finally {
            serverSocket.close();
        }
    }

    // 针对粘包问题
    public static void runWithSockyBag() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is listening on port" + port);

        try {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accept connection from" + clientSocket.getInetAddress().getHostAddress());

            InputStream in = clientSocket.getInputStream();
            DataInputStream dis = new DataInputStream(in);

            while (true) {
                int length = dis.readInt();
                byte[] buffer = new byte[length];
                dis.readFully(buffer);
                String message = new String(buffer);

                System.out.println("Received message from client:" + message);
            }
        } catch (EOFException e) {
            System.out.println("End of stream reached. Client closed the connection");
        } finally {
            serverSocket.close();
        }
    }

    //模拟半包问题
    public static void runWithHalfPackage() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is listening on port" + port);

        try {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accept connection from" + clientSocket.getInetAddress().getHostAddress());

            InputStream in = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            char[] buffer = new char[10];//用小的缓冲区来模拟半包
            int charsRead = reader.read(buffer, 0, buffer.length);
            while (charsRead != -1) {
                String msg = new String(buffer, 0, charsRead);
                System.out.println("Received msg:" + msg);
                charsRead = reader.read(buffer, 0, buffer.length);
            }

            clientSocket.close();
        } catch (EOFException e) {
            System.out.println("End of stream reached. Client closed the connection");
        } finally {
            serverSocket.close();
        }
    }

    //传输object问题
    public static void runWithObject() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is listening on port" + port);

        try {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accept connection from" + clientSocket.getInetAddress().getHostAddress());

            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            MyObject myObject = (MyObject) in.readObject();
            System.out.println("Received obj from client:" + myObject);

            in.close();
            clientSocket.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            serverSocket.close();
        }
    }
}
