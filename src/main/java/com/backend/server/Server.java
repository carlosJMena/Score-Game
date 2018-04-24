package com.backend.server;

import com.backend.core.singleton.GameSingleton;
import com.sun.net.httpserver.HttpServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static int PORT = 8081;

    /**
     * Main Method where the HttpServer is deployed
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        try {
            String hostName = "localhost";
            try {
                hostName = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException ex) {
                System.err.println("Unknown Host: " + ex);
            }
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/", new KingHttpHandler(GameSingleton.getInstance()));
            ExecutorService executorService = Executors.newCachedThreadPool();
            httpServer.setExecutor(executorService);
            httpServer.start();
            System.out.println("   HTTPServer started in http://" + hostName + ":" + PORT + "/");
        } catch (Exception e) {
            System.err.println("Error starting HTTPServer.");
            System.err.println(e.getMessage());

        }
    }

}
