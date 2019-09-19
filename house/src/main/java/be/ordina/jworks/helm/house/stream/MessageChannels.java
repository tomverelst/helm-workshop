package be.ordina.jworks.helm.house.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface MessageChannels {

    @Input("speak")
    SubscribableChannel speak();

    @Input("seat")
    SubscribableChannel seat();

    @Input("vote")
    SubscribableChannel vote();

}
