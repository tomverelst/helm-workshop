package be.ordina.jworks.helm.speaker;

import be.ordina.jworks.helm.speaker.speech.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class Speaker {

    private static final Logger LOGGER = LoggerFactory.getLogger(Speaker.class);

    private final MessageChannels channels;

    private SpeechTypeDetector detector;

    public Speaker(
            final MessageChannels channels,
            final SpeechTypeDetector detector) {
        this.channels = Objects.requireNonNull(channels, "channels is required");
        this.detector = Objects.requireNonNull(detector, "detector is required");
    }

    public Mono<Speech> say(final Speech speech){
        return detector.detect(speech)
                .flatMap(type -> handle(speech.withType(type)));
    }

    private Mono<Speech> handle(
            final Speech speech) {
        if (speech.getType() == SpeechType.ORDER) {
            return order();
        } else {
            return askQuestion(new Question(speech.getText()));
        }
    }

    public Mono<Speech> order(){
        return publish(new ShoutOrder());
    }

    public Mono<Speech> askQuestion(Question question){
        return Mono.defer(() -> {
            if(question == null){
                return Mono.error(new InvalidQuestionException());
            } else {
                return publish(question);
            }
        });
    }

    private Mono<Speech> publish(Speech speech){
        return Mono.just(speech)
                .map(s -> {
                    LOGGER.info("Publishing speech {}", speech);
                    channels.speak().send(MessageBuilder.withPayload(speech).build());
                    return speech;
                });
    }

}
