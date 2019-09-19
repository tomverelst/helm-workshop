package be.ordina.jworks.helm.house.http;

import be.ordina.jworks.helm.house.HouseOfCommons;
import be.ordina.jworks.helm.house.HouseScenario;
import be.ordina.jworks.helm.house.IntegrationTest;
import be.ordina.jworks.helm.house.MemberSeatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class EventStreamIntegrationTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10L);

    private WebTestClient client;

    private HouseScenario scenario;

    @BeforeEach
    void setUp(ApplicationContext context){
        this.scenario = new HouseScenario(context.getBean(HouseOfCommons.class));
        this.client = WebTestClient
                .bindToApplicationContext(context)
                .configureClient()
                .responseTimeout(TIMEOUT)
                .build();
    }

    @Test
    void memberSeatedEvent(){
        schedule(() -> scenario.seatMember());

        final var flux = subscribeForOneEvent();

        StepVerifier.create(flux)
                .assertNext(sse -> {
                    assertThat(sse.event()).isEqualTo("member-seated");
                    assertThat(sse.data()).isEqualTo(Map.of("id","memberId", "name", "memberName"));
                }).expectComplete()
                .verify(TIMEOUT);
    }

    /**
     * Subscribing to the event stream blocks until the first event,
     * so the first event must be triggered asynchronously
     */
    private void schedule(Runnable runnable){
        CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS).execute(runnable);
    }

    private Flux<ServerSentEvent> subscribeForOneEvent() {
        return client.get().uri("/sse")
                    .accept(MediaType.TEXT_EVENT_STREAM)
                    .exchange()
                    .expectStatus().isOk()
                    .returnResult(ServerSentEvent.class)
                    .getResponseBody()
                    .take(1);
    }

}