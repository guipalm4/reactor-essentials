package guipalm4.reactor.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
public class MonoTest {

    @Test
    void monoSubscriber() {
        //Given
        log.info("-------Actual------------");

        var name = "guipalm4";
        Mono<String> nameMono = Mono.just(name).log();

        //When
        nameMono.subscribe();

        //Then
        log.info("-------Expected------------");

        StepVerifier.create(nameMono)
                .expectNext(name)
                .verifyComplete();
    }

    @Test
    void monoSubscriberConsumer() {
        //Given
        log.info("-------Actual------------");

        var name = "guipalm4";
        Mono<String> nameMono = Mono.just(name).log();

        //When
        nameMono.subscribe(s-> log.info("The dev is {}", s));

        //Then
        log.info("-------Expected------------");

        StepVerifier.create(nameMono)
                .expectNext(name)
                .verifyComplete();
    }

    @Test
    void monoSubscriberConsumerError() {
        //Given
        log.info("-------Actual------------");

        var name = "guipalm4";
        Mono<String> nameMono = Mono.just(name)
                .map(s-> {throw new RuntimeException("Testing mono with error");});

        //When
        nameMono.subscribe(s-> log.info("User {}", s), s-> log.error("Something bad happened"));
        nameMono.subscribe(s-> log.info("User {}", s), Throwable::printStackTrace);

        //Then
        StepVerifier.create(nameMono)
                .expectError(RuntimeException.class)
                .verify();
    }
}
