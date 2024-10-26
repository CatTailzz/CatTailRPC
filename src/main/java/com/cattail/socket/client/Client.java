package com.cattail.socket.client;

import com.cattail.socket.MyObject;
import sun.misc.resources.Messages;

import java.io.*;
import java.net.Socket;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/26
 * @Copyright: https://github.com/CatTailzz
 */
public class Client {

    static int port = 12345;

    static String hostName = "localhost";

    public static void main(String[] args) throws IOException {
        //here
        runWithObject();
    }

    //普通
    public static void run() throws IOException {
        Socket socket = new Socket(hostName, port);
        System.out.println("Connected to server at " + hostName + ":" + port);

        OutputStream outputStream = socket.getOutputStream();
        PrintWriter out = new PrintWriter(outputStream, true);
        String message = "hello, I am client";
        out.println(message);

        socket.close();
        System.out.println("message has send");
    }

    //模拟粘包问题
    public static void runWithStickyBag() throws IOException {
        Socket socket = new Socket(hostName, port);
        System.out.println("Connected to server at " + hostName + ":" + port);

        OutputStream outputStream = socket.getOutputStream();

        String[] Messages = {"a", "b", "c", "d"};
        for (String msg : Messages) {
            outputStream.write(msg.getBytes());
            outputStream.flush();
        }
        socket.close();
    }

    //解决粘包问题，需要client配合
    public static void resolveRunWithStickyBag() throws IOException {
        Socket socket = new Socket(hostName, port);
        System.out.println("Connected to server at " + hostName + ":" + port);

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        String[] Messages = {"a", "b", "c", "d"};
        for (String msg : Messages) {
            dataOutputStream.writeInt(msg.length());
            dataOutputStream.writeBytes(msg);
            dataOutputStream.flush();
        }
        socket.close();
    }

    //半包问题
    public static void runWithHalfPackage() throws IOException {
        Socket socket = new Socket(hostName, port);
        System.out.println("Connected to server at " + hostName + ":" + port);

        OutputStream outputStream = socket.getOutputStream();
        PrintWriter out = new PrintWriter(outputStream, true);

        String message = "I am Longggggggggggggggggggggggggggggggggggggggggggggggggggggggggg";
        out.println(message);

        socket.close();
    }

    //传输obj
    public static void runWithObject() throws IOException {
        Socket socket = new Socket(hostName, port);
        System.out.println("Connected to server at " + hostName + ":" + port);

        MyObject myObject = new MyObject("a", "b");
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(myObject);

        out.close();
        socket.close();
    }

}

