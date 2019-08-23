package be.ordina.jworks.helm.speaker;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MessageChannels {

    @Output("speak")
    MessageChannel speak();

}
