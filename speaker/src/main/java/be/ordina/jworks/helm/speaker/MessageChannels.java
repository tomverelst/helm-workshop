package be.ordina.jworks.helm.speaker;

import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface MessageChannels {

    MessageChannel speaker();

}
