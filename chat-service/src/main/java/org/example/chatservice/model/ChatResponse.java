package org.example.chatservice.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private int index;
        private Message message;
        private LocalDateTime dateTime;

        public Choice(int index, Message message) {
            this.index = index;
            this.message = message;
            this.dateTime = LocalDateTime.now();
        }
    }
}
