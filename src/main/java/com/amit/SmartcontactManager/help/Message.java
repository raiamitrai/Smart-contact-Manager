package com.amit.SmartcontactManager.help;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Message {

    private String content;
    private String type;

    public Message(String content, String type) {
        this.content = content;
        this.type = type;
    }

}



