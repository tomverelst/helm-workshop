package be.ordina.jworks.helm.speaker;

import be.ordina.jworks.helm.speaker.speech.Speech;
import be.ordina.jworks.helm.speaker.speech.SpeechType;
import be.ordina.jworks.helm.speaker.speech.SpeechTypeDetector;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Speaker {

    private final MessageChannels channels;

    private SpeechTypeDetector detector;

    public Speaker(
            final MessageChannels channels,
            final SpeechTypeDetector detector) {
        this.channels = channels;
        this.detector = detector;
    }

    public Mono<SpeechType> say(Speech speech){
        return detector.detect(speech)
                .map(type -> {
                    if (type == SpeechType.ORDER) {
                        order();
                    } else {
                        askQuestion(new Question(speech.getText()));
                    }
                    return type;
                });
    }

    public Mono<Void> order(){
        return Mono.defer(() ->
                Mono.just(channels.speaker().send(MessageBuilder.withPayload(new ShoutOrder()).build())))
                .then();
    }

    public Mono<Void> askQuestion(Question question){
        return Mono.defer(() -> {
            if(question == null){
                return Mono.error(new InvalidQuestionException());
            } else {
                return Mono.just(channels.speaker().send(MessageBuilder.withPayload(question).build()));
            }
        }).then();
    }

}
