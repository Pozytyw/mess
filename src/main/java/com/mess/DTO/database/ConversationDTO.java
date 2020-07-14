package com.mess.DTO.database;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "conversation")
public class ConversationDTO {

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    private long id;

    @Column(name="name")
    private String name;

    @Column(name = "is_group")
    private boolean isGroup;

    @ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="conv_messages", joinColumns=@JoinColumn(name="conv_id"), inverseJoinColumns=@JoinColumn(name="mess_id"))
    @OrderBy(value="send_date")
    private Set<MessageDTO> messages;

    @ManyToMany(cascade=CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name="conv_user", joinColumns=@JoinColumn(name="conv_id"), inverseJoinColumns=@JoinColumn(name="user_id"))
    private Set<UserDTO> users;

    public ConversationDTO() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public String getName(UserDTO user) {
        if(this.name.equals("self")){
            return user.getUsername();
        }
        List<Object> users = new LinkedList<>(Arrays.asList(this.users.toArray()));
        users.removeIf(userDTO -> ((UserDTO)userDTO).getId() == user.getId());
        return ((UserDTO)users.get(0)).getUsername();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName(Set<UserDTO> users) {
        this.name = "";
        users.forEach(user -> {
            this.name += user.getUsername() + ", ";
        });
        this.name = this.name.substring(0 , this.name.length() - 2);//remove last ", "
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public Set<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(Set<MessageDTO> messages) {
        this.messages = messages;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }
}
