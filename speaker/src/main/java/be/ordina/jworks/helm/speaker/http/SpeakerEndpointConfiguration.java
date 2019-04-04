package be.ordina.jworks.helm.speaker.http;

import be.ordina.jworks.helm.speaker.Question;
import be.ordina.jworks.helm.speaker.Speaker;
import be.ordina.jworks.helm.speaker.speech.Speech;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SpeakerEndpointConfiguration {

    private Speaker speaker;

    public SpeakerEndpointConfiguration(Speaker speaker) {
        this.speaker = speaker;
    }

    @Bean
    RouterFunction<ServerResponse> routes() {
        return route(i(POST("/order")), this::order)
                .andRoute(i(POST("/vote")), this::askQuestion)
                .andRoute(i(POST("/say")), this::say);
    }

    private static RequestPredicate i(final RequestPredicate target) {
        return new CaseInsensitiveRequestPredicate(target);
    }

    private Mono<ServerResponse> say(final ServerRequest request) {
        return request
                .bodyToMono(Speech.class)
                .flatMap(s -> speaker.say(s))
                .flatMap(v -> ServerResponse.ok().build());
    }

    private Mono<ServerResponse> order(final ServerRequest request) {
        return speaker.order()
                .flatMap(v -> ServerResponse.ok().build());
    }

    private Mono<ServerResponse> askQuestion(final ServerRequest request) {
        return request
                .bodyToMono(Question.class)
                .flatMap(question -> speaker.askQuestion(question))
                .flatMap(v -> ServerResponse.ok().build());
    }

}
