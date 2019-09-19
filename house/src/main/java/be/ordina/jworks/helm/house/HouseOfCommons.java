package be.ordina.jworks.helm.house;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class HouseOfCommons {

    private static final Logger LOGGER = LoggerFactory.getLogger(HouseOfCommons.class);

    private final Seats seats = new Seats();
    private final Motions motions = new Motions();

    private final ApplicationEventPublisher publisher;

    public HouseOfCommons(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public Flux<Member> getMembers() {
        return seats.getMembers();
    }

    public Mono<MotionStatus> getStatus(){
        return motions.getCurrent()
                .map(MotionStatus::new)
                .switchIfEmpty(Mono.just(MotionStatus.empty()));
    }

    public Mono<Void> order(){
        return this.motions.stopCurrent()
            .doOnSuccess(m -> publisher.publishEvent(new MotionStoppedEvent()));
    }

    public Mono<Motion> startMotion(String question) {
        return motions.startNew(question)
                .doOnSuccess(m -> publisher.publishEvent(new MotionStartedEvent(m)));
    }

    public Mono<Member> seat(String memberId, String name) {
        return seat(new Member(memberId, name));
    }

    public Mono<Member> seat(Member member) {
        return this.seats.add(member)
                .doOnSuccess(v -> publisher.publishEvent(new MemberSeatedEvent(member)));
    }

    public Mono<Void> vote(String memberId, Vote vote) {
        return Mono.just(memberId)
                .flatMap(this.seats::getMember)
                .flatMap(m -> vote(m, vote));
    }

    public Mono<Void> vote(Member member, Vote vote) {
        return this.motions.getCurrent()
                .map(m -> m.vote(member, vote))
                .doOnSuccess(m -> {
                    if(m != null) {
                        publisher.publishEvent(new MemberVotedEvent(member, vote));
                    }})
                .doOnSuccess(m -> {
                    if (m != null) {
                        publisher.publishEvent(new MotionUpdatedEvent(m));
                    }
                })
                .then();
    }

}
