package com.mess.DTO.database;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Conv_userId implements Serializable {
    private long conv_id;
    private long user_id;

    public Conv_userId() {
    }

    public Conv_userId(long conv_id, long user_id) {
        this.conv_id = conv_id;
        this.user_id = user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conv_userId that = (Conv_userId) o;
        return conv_id == that.conv_id &&
                user_id == that.user_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conv_id, user_id);
    }

    public long getConv_id() {
        return conv_id;
    }

    public void setConv_id(long conv_id) {
        this.conv_id = conv_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
