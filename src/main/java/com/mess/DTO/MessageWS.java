package com.mess.DTO;

import org.springframework.web.util.HtmlUtils;
import org.unbescape.html.HtmlEscape;

public class MessageWS {
    private String sender;
    private String conversationId;
    private String message;

    public MessageWS(String sender, String conversationId, String message) {
        this.sender = sender;
        this.conversationId = conversationId;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getMessage() {
        return HtmlUtils.htmlEscape(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
