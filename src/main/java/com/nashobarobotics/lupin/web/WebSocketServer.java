package com.nashobarobotics.lupin.web;

import com.nashobarobotics.lupin.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class WebSocketServer extends Thread {
    private int port;
    private Server server;

    public WebSocketServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        server = new Server(port);
        WebSocketHandler wshandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.register(WebSocketWorker.class);                
            }
        };
        server.setHandler(wshandler);
        try {
            server.start();
            Logger.debug("Started websocket server");
            while(!interrupted() && server.isRunning()) {
                Thread.onSpinWait();
            }
        } catch (Exception e) {
            Logger.error("Error running websocket server: " + e.getMessage());
        } finally {
            if(server.isRunning()) {
                try {
                    server.stop();
                    Logger.debug("Stopped websocket server");
                } catch (Exception e) {
                    Logger.error("Failed to stop server: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void interrupt() {
        if(server != null) {
            try {
                server.stop();
                Logger.debug("Stopped websocket server");
            } catch (Exception e) {
                Logger.error("Failed to stop server: " + e.getMessage());
            }
        }
    }
}