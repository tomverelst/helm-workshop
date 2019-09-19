package be.ordina.jworks.helm.house.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class HouseEndpointConfiguration {

    @Bean
    RouterFunction<ServerResponse> routes() {
        return route(i(GET("/")), this::index);
    }

    private static RequestPredicate i(final RequestPredicate target) {
        return new CaseInsensitiveRequestPredicate(target);
    }

    private Mono<ServerResponse> index(final ServerRequest request) {
        return ServerResponse.ok().build();
    }

}
