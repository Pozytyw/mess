package com.mess.DTO.database;

import com.mess.DTO.ws.ReadMessageWS;

import javax.persistence.*;
import java.util.Calendar;

@Entity
public class ReadMessageDTO {
    @EmbeddedId
    private Conv_userId id;

    @Column(name = "mess_id")
    private long mess_id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "read_date")
    private Calendar readDate;

    public ReadMessageDTO() {
    }

    public ReadMessageDTO(ReadMessageWS readMessageWS, Long user_id) {
        this.id = new Conv_userId(readMessageWS.getConv_id(), user_id);
        this.mess_id = readMessageWS.getMess_id();
        this.readDate = readMessageWS.getReadDate();
    }

    public long getMess_id() {
        return mess_id;
    }

    public void setMess_id(long mess_id) {
        this.mess_id = mess_id;
    }

    public Calendar getReadDate() {
        return readDate;
    }

    public void setReadDate(Calendar readDate) {
        this.readDate = readDate;
    }

    public Conv_userId getId() {
        return id;
    }
}
