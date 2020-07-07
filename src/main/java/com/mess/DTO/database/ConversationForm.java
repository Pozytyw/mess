package com.mess.DTO.database;

import java.util.*;

public class ConversationForm{

    private long id;
    private String name;
    private boolean isGroup;

    public ConversationForm() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }
}
