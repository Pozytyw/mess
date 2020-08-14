package com.mess.DTO.ws;
import java.util.Calendar;

public class ReadMessageWS {
    private long conv_id;
    private long mess_id;
    private Calendar readDate;

    public long getConv_id() {
        return conv_id;
    }

    public void setConv_id(long conv_id) {
        this.conv_id = conv_id;
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
}
