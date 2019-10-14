package com.company;

import java.net.ServerSocket;
import java.net.Socket;

public class HttpServerFactory extends ServerFactory {
    @Override
    public Server createServer(Socket socket) {
        try {
            return new HttpServer(socket);
        }
        catch (Throwable ex){
            ex.printStackTrace();
        }
        return null;
    }
}
