package com.mess.websocket;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class Listener implements ApplicationListener<SessionDisconnectEvent> {
    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        String name = sessionDisconnectEvent.getUser().getName();
        UsersToken.removeToken(name);
    }
}
