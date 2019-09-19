package be.ordina.jworks.helm.house.stream;

import be.ordina.jworks.helm.house.HouseOfCommons;
import be.ordina.jworks.helm.house.stream.MemberSeatedStreamEvent;
import be.ordina.jworks.helm.house.stream.MemberVotedStreamEvent;
import be.ordina.jworks.helm.house.stream.MessageChannels;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.SECONDS;

@Component
public class HouseOfCommonsStreamListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(HouseOfCommonsStreamListener.class);

    private final Duration TIMEOUT = Duration.of(5L, SECONDS);

    private final HouseOfCommons houseOfCommons;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public HouseOfCommonsStreamListener(
            final HouseOfCommons houseOfCommons,
            final MessageChannels messageChannels) {
        this.houseOfCommons = houseOfCommons;
        messageChannels.seat().subscribe(this::onSeat);
        messageChannels.vote().subscribe(this::onVote);
        messageChannels.speak().subscribe(this::onSpeech);
    }

    private void onSeat(Message<?> msg) {
        read(msg, MemberSeatedStreamEvent.class).ifPresent(
                e -> houseOfCommons.seat(e.getMemberId(), e.getName()).block(TIMEOUT));
    }

    private void onVote(Message<?> msg) {
        read(msg, MemberVotedStreamEvent.class).ifPresent(
                e -> houseOfCommons.vote(e.getMemberId(), e.getVote()).block(TIMEOUT));
    }

    private <T> Optional<T> read(Message<?> msg, Class<T> clazz) {
        try {
            return Optional.of(objectMapper.readValue((byte[]) msg.getPayload(), clazz));
        } catch (IOException e) {
            LOGGER.error("Could not read message", e);
            return Optional.empty();
        }
    }

    private void onSpeech(Message<?> msg) {
        read(msg, SpeechStreamEvent.class).ifPresent(e -> {
            switch (e.getType().toLowerCase()) {
                case "order":
                    houseOfCommons.order().block(TIMEOUT);
                    break;
                case "question":
                    houseOfCommons.startMotion(e.getText()).block(TIMEOUT);
                    break;
                case "unknown":
                    break;
            }
        });
    }
}
