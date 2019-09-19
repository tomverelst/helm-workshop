package be.ordina.jworks.helm.house;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class HouseOfCommonsTest {

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private HouseOfCommons house;

    private HouseScenario scenario;

    @BeforeEach
    public void setUp(){
        this.scenario = new HouseScenario(house);
    }

    @Nested
    class Voting {

        @BeforeEach
        public void memberSeated(){
            scenario.seatMember();
        }

        @BeforeEach
        public void motionStarted(){
            scenario.startMotion();
        }

        @Test
        public void voteAye(){
            scenario.voteAye();

            StepVerifier.create(house.getStatus())
                    .assertNext(status -> {
                        assertThat(status.getQuestion()).isEqualTo("Question?");
                        assertThat(status.getAyes()).isEqualTo(1L);
                        assertThat(status.getNoes()).isEqualTo(0L);
                    })
                    .expectComplete()
                    .verify(Duration.ofSeconds(5L));
        }

        @Test
        public void voteNo(){
            scenario.voteNo();

            StepVerifier.create(house.getStatus())
                    .assertNext(status -> {
                        assertThat(status.getQuestion()).isEqualTo("Question?");
                        assertThat(status.getAyes()).isEqualTo(0L);
                        assertThat(status.getNoes()).isEqualTo(1L);
                    })
                    .expectComplete()
                    .verify(Duration.ofSeconds(5L));
        }
    }

    @Test
    void order() {
        this.scenario.seatMember().startMotion().voteNo().order();

        StepVerifier.create(house.getStatus())
                .assertNext(status -> {
                    assertThat(status.getQuestion()).isEqualTo("");
                    assertThat(status.getAyes()).isEqualTo(0L);
                    assertThat(status.getNoes()).isEqualTo(0L);
                })
                .expectComplete()
                .verify(Duration.ofSeconds(5L));
    }


}