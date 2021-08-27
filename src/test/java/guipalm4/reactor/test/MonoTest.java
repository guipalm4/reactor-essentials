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
        Mono<String> nameMono = Mono.just(name)
                .log();
        //When
        nameMono.subscribe();

        //Then
        log.info("-------Expected------------");
        StepVerifier.create(nameMono)
                .expectNext(name)
                .verifyComplete();

    }
}
