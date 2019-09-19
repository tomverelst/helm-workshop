package be.ordina.jworks.helm.member.http;

import be.ordina.jworks.helm.member.Member;
import be.ordina.jworks.helm.member.speech.Speech;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class MemberEndpointConfiguration {

    private Member member;

    public MemberEndpointConfiguration(Member member) {
        this.member = member;
    }

    @Bean
    RouterFunction<ServerResponse> routes() {
        return route(i(POST("/aye")), this::aye)
                .andRoute(i(POST("/no")), this::no)
                .andRoute(i(POST("/say")), this::say)
                .andRoute(i(GET("/name")), this::name)
                .andRoute(i(GET("/")), this::index);
    }

    private static RequestPredicate i(final RequestPredicate target) {
        return new CaseInsensitiveRequestPredicate(target);
    }

    private Mono<ServerResponse> index(final ServerRequest request){
        return ServerResponse.ok().build();
    }

    private Mono<ServerResponse> say(final ServerRequest request) {
        return request
                .bodyToMono(Speech.class)
                .flatMap(s -> member.say(s))
                .flatMap(v -> ServerResponse.ok().build());
    }

    private Mono<ServerResponse> aye(final ServerRequest request) {
        return member.aye()
                .flatMap(v -> ServerResponse.ok().build());
    }

    private Mono<ServerResponse> no(final ServerRequest request) {
        return member.no()
                .flatMap(v -> ServerResponse.ok().build());
    }

    private Mono<ServerResponse> name(final ServerRequest request) {
        return member.getName()
                .flatMap(n -> ServerResponse.ok().body(Mono.just(Map.of("name", n)), Map.class));
    }

}
