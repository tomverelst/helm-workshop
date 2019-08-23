package be.ordina.jworks.helm.speaker.speech;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import reactor.test.StepVerifier;

import java.time.Duration;

class SpeechTypeDetectorTest {

    private SpeechTypeDetector detector;

    @BeforeEach
    public void setUp(){
        this.detector = new SpeechTypeDetector();
    }

    @ParameterizedTest
    @ValueSource(strings ={"order", "Order", "  order", "order, order!"})
    public void order(String text){
        is(text, SpeechType.ORDER);
    }

    @ParameterizedTest
    @ValueSource(strings ={"louder", "older", "order the older", "old"})
    public void orderLike(String text){
        is(text, SpeechType.ORDER);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Why are you such a crybaby?",
            "Where did that window licker go?",
            "Whose badjas is this?"
    })
    public void question(String text){
        is(text, SpeechType.QUESTION);
    }

    private void is(String text, SpeechType type){
        var mono = detector.detect(new Speech(text));

        StepVerifier.create(mono)
                .expectNext(type)
                .expectComplete()
                .verify(Duration.ofSeconds(5L));
    }

}