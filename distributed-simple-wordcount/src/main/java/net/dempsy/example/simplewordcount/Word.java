package net.dempsy.example.simplewordcount;

import java.io.Serializable;

import net.dempsy.lifecycle.annotation.MessageKey;
import net.dempsy.lifecycle.annotation.MessageType;

@MessageType
public class Word implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String wordText;

    public Word(final String data) {
        this.wordText = data;
    }

    @MessageKey
    public String getWordText() {
        return this.wordText;
    }
}
