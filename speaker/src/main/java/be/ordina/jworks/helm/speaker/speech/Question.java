package be.ordina.jworks.helm.speaker.speech;

import be.ordina.jworks.helm.speaker.speech.Speech;
import be.ordina.jworks.helm.speaker.speech.SpeechType;

public class Question extends Speech {

    public Question(String question) {
        super(question, SpeechType.QUESTION);
    }

}
