package be.ordina.jworks.helm.house;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

class Seats {

    private static final Logger LOGGER = LoggerFactory.getLogger(Seats.class);

    private final Map<String, Member> members = Maps.newConcurrentMap();

    Mono<Member> add(
            final Member member) {
        Objects.requireNonNull(member);
        return Mono.just(member)
                .map(m -> {
                    final var existing = this.members.put(m.getId(), m);
                    if (existing == null) {
                        LOGGER.info("New member seated: {}", m.getName());
                    }
                    return m;
                });
    }

    Mono<Member> getMember(String memberId) {
        return Mono.just(memberId)
                .flatMap(mid -> {
                    var member = members.get(mid);
                    if (member == null) {
                        return Mono.empty();
                    } else {
                        return Mono.just(member);
                    }
                });
    }

    Flux<Member> getMembers() {
        return Flux.fromStream(members.values().stream());
    }

}
