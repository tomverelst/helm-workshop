package be.ordina.jworks.helm.speaker;

import be.ordina.jworks.helm.speaker.test.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.test.binder.MessageCollector;

@IntegrationTest
public class SpeakerIntegrationTest {

    @Autowired
    private MessageChannels channels;

    @Autowired
    private MessageCollector collector;

    @Test
    public void testMessages() {
//        BlockingQueue<Message<ShoutOrder>> messages = collector.forChannel(channels.speaker());
//
//        assertThat(messages, receivesPayloadThat(is("foo")));
//        assertThat(messages, receivesPayloadThat(is("bar")));
//        assertThat(messages, receivesPayloadThat(is("foo")));
//        assertThat(messages, receivesPayloadThat(is("bar")));
    }
}
