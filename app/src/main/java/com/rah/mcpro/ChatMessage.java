package com.rah.mcpro;

import java.util.Date;

public class ChatMessage {

    public String messageText;
    public String messageUser;
    public long messageTime;
    public String roomId;
//    public int roomNumber;

    public ChatMessage(String text, String user, String roomId) {
        this.messageText = text;
        this.messageUser = user;
        this.roomId = roomId;

        messageTime = new Date().getTime();
    }

    public ChatMessage() {}

    public String getRoomId() {
        return roomId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }




}
