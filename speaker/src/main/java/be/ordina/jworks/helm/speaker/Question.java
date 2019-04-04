package be.ordina.jworks.helm.speaker;

import java.util.Objects;

public class Question {

    private final String question;

    public Question(String question) {
        this.question = Objects.requireNonNull(question);
    }

    public String getQuestion() {
        return question;
    }
}
