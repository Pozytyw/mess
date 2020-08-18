package com.mess.DTO.ws;
import org.springframework.web.util.HtmlUtils;

public class MessageWS {
    private String sender;
    private String conversationId;
    private String message;
    private Long mess_id;

    public MessageWS() {
    }

    public MessageWS(String sender, String conversationId, String message) {
        this.sender = sender;
        this.conversationId = conversationId;
        this.message = message;
        this.mess_id = null;
    }

    public MessageWS(String sender, String conversationId, String message, Long mess_id) {
        this.sender = sender;
        this.conversationId = conversationId;
        this.message = message;
        this.mess_id = mess_id;
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

    public Long getMess_id() {
        return mess_id;
    }

    public void setMess_id(Long mess_id) {
        this.mess_id = mess_id;
    }
}
