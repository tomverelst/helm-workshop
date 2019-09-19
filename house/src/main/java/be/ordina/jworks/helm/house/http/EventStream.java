package be.ordina.jworks.helm.house.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@RestController
public class EventStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventStream.class);

    private final UnicastProcessor<Event> source;
    private final Flux<Event> hot;

    EventStream(){
        this.source = UnicastProcessor.create();
        this.hot = this.source.publish().autoConnect();
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<ServerSentEvent<Object>> sse() {
        return Flux.create(sink -> {
            var d = hot.subscribe(event -> sink.next(ServerSentEvent.builder()
                    .event(event.getType())
                    .data(event.getData())
                    .build()));

            sink.onDispose(d);
        });
    }

    public void publish(Event event) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Publishing {} event: {}", event.getType(), event.getData());
        }
        this.source.onNext(event);
    }

    @EventListener
    void onEvent(Object event){
        if(event instanceof Event){
            publish((Event) event);
        }
    }

}
