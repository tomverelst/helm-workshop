package be.ordina.jworks.helm.member;

import be.ordina.jworks.helm.member.speech.Aye;
import be.ordina.jworks.helm.member.speech.No;
import be.ordina.jworks.helm.member.speech.Speech;
import be.ordina.jworks.helm.member.speech.SpeechTypeDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
public class Member {

    private static final Logger LOGGER = LoggerFactory.getLogger(Member.class);

    private final MessageChannels channels;

    private SpeechTypeDetector detector;

    private String id;
    private String name;

    public Member(
            @Value("${MEMBER_NAME:Unknown}") final String name,
            final MessageChannels channels,
            final SpeechTypeDetector detector) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.channels = Objects.requireNonNull(channels, "channels is required");
        this.detector = Objects.requireNonNull(detector, "detector is required");
    }

    @Scheduled(initialDelay = 5000L, fixedRate = 5000L)
    public void ping(){
        this.channels.seat().send(MessageBuilder.withPayload(new MemberSeatedEvent(id, this.name)).build());
    }

    public Mono<Speech> say(final Speech speech) {
        return detector.detect(speech)
                .flatMap(type -> handle(speech.withType(type)));
    }

    private Mono<Speech> handle(final Speech speech) {
        switch (speech.getType()) {
            case AYE:
                return aye();
            case NO:
                return no();
            default:
                return Mono.empty();
        }
    }

    public Mono<String> getName(){
        return Mono.just(name);
    }

    public Mono<Speech> aye() {
        return publish(new Aye());
    }

    public Mono<Speech> no() {
        return publish(new No());
    }

    private Mono<Speech> publish(Speech speech) {
        return Mono.just(speech)
                .map(s -> {
                    LOGGER.info("Publishing vote {}", speech);
                    channels.vote().send(MessageBuilder.withPayload(
                            Map.of("memberId", this.id, "vote", speech.getType()))
                            .build());
                    return speech;
                });
    }

}
