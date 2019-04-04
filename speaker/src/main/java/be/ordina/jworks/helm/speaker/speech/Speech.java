package be.ordina.jworks.helm.speaker.speech;

import java.util.Objects;

public class Speech {

    private String text;

    public Speech(String text) {
        this.text = Objects.requireNonNull(text).trim();
    }

    public String getText() {
        return text;
    }

}
