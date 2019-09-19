package be.ordina.jworks.helm.member;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MessageChannels {

    @Output("seat")
    MessageChannel seat();

    @Output("vote")
    MessageChannel vote();

}
