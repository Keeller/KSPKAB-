package com.company;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    public static void main(String[] args) throws Throwable {
        ServerSocket ss = new ServerSocket(8081);
        while (true) {
            Socket s = ss.accept();
            System.err.println("Client accepted");
            (new Thread((HttpServer)(new HttpServerFactory().createServer(s)))).start();
        }
    }
}
