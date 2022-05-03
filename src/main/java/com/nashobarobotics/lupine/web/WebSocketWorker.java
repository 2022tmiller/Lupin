package com.nashobarobotics.lupine.web;

import java.io.IOException;

import com.nashobarobotics.lupine.Logger;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class WebSocketWorker {
    private Session session;

    public WebSocketWorker() {
        session = null;
        Logger.debug("Created instance");
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        Logger.debug("Connected to " + session.getRemoteAddress());
        try {
            session.getRemote().sendString("Hello, world!");
        } catch (IOException e) {
            Logger.error(e.toString());
        }
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        try {
            Logger.debug("Message: " + message);
            session.getRemote().sendString("ECHO " + message);
        } catch (IOException e) {
            Logger.error("Error while recieving message: " + e.getMessage());
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        Logger.debug("Lost connection: " + reason);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        Logger.error(t.getMessage());
    }
}