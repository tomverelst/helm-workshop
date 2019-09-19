package be.ordina.jworks.helm.house;

import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Motions {

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Motion current;
    private List<Motion> previous = new ArrayList<>();

    Mono<Void> stopCurrent(){
        return Mono.defer(() -> {
            final var w = lock.writeLock();
            w.lock();

            try {
                if(this.current != null) {
                    previous.add(current);
                }
                this.current = null;
                return Mono.empty();
            } finally {
                w.unlock();
            }
        });
    }

    Mono<Motion> startNew(String question){
        return Mono.defer(() -> {
            final var w = lock.writeLock();
            w.lock();

            try {
                final Motion motion = new Motion(question);
                if(this.current != null) {
                    previous.add(current);
                }
                this.current = motion;
                return Mono.just(motion);
            } finally {
                w.unlock();
            }
        });
    }

    Mono<Motion> getCurrent(){
        return Mono.defer(() -> {
            var r = lock.readLock();
            r.lock();
            try {
                return Mono.justOrEmpty(current);
            } finally {
                r.unlock();
            }
        });
    }

}
