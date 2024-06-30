package com.prm392.estoreprm392.service.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class Chat extends BaseModel{
        private List<String> userIds;
        private String lastMessage;
        private String lastMessageSenderId;
        private Timestamp lastMessageTimeStamp;

        public Chat() {}

        public Chat(String uid, List<String> userIds, Timestamp lastMessageTimestamp, String lastMessageSenderId) {
            super(uid);
            this.userIds = userIds;
            this.lastMessageTimeStamp = lastMessageTimestamp;
            this.lastMessageSenderId = lastMessageSenderId;
        }

        public List<String> getUserIds() {
            return userIds;
        }

        public void setUserIds(List<String> userIds) {
            this.userIds = userIds;
        }

        public String getLastMessage() {
            return lastMessage;
        }

        public void setLastMessage(String lastMessage) {
            this.lastMessage = lastMessage;
        }

        public String getLastMessageSenderId() {
            return lastMessageSenderId;
        }

        public void setLastMessageSenderId(String lastMessageSenderId) {
            this.lastMessageSenderId = lastMessageSenderId;
        }

        public Timestamp getLastMessageTimeStamp() {
            return lastMessageTimeStamp;
        }

        public void setLastMessageTimeStamp(Timestamp lastMessageTimeStamp) {
            this.lastMessageTimeStamp = lastMessageTimeStamp;
        }
}

