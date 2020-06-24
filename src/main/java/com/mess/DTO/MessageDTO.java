package com.mess.DTO;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name="messages")
public class MessageDTO {

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    private long id;

    @Column(name = "message")
    private String message;

    @Column(name = "user_id")
    private long user_id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "send_date")
    private Calendar send_date;

    public MessageDTO() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public Calendar getSend_date() {
        return send_date;
    }

    public void setSend_date(Calendar send_date) {
        this.send_date = send_date;
    }

}
