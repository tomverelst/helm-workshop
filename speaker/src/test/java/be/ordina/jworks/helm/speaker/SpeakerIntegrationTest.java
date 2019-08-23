package be.ordina.jworks.helm.speaker;

import be.ordina.jworks.helm.speaker.speech.ShoutOrder;
import be.ordina.jworks.helm.speaker.test.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.test.binder.MessageCollector;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;

@IntegrationTest
public class SpeakerIntegrationTest {

    @Autowired
    private MessageChannels channels;

    @Autowired
    private MessageCollector collector;

    @Autowired
    private Speaker speaker;

    @Test
    public void shoutOrder() {
        var messages = collector.forChannel(channels.speak());

        speaker.say(new ShoutOrder()).block();

        assertThat(messages, receivesPayloadThat(is("{\"text\":\"Order!\",\"type\":\"ORDER\"}")));
    }
}
