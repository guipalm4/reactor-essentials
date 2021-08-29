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
        nameMono.subscribe(s -> log.info("The dev is {}", s));

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
                .map(s -> {
                    throw new RuntimeException("Testing mono with error");
                });

        //When
        nameMono.subscribe(s -> log.info("User {}", s), s -> log.error("Something bad happened"));
        nameMono.subscribe(s -> log.info("User {}", s), Throwable::printStackTrace);

        //Then
        StepVerifier.create(nameMono)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void monoSubscriberConsumerComplete() {
        //Given
        log.info("-------Actual------------");

        var name = "guipalm4";
        Mono<String> nameMono = Mono.just(name)
                .log()
                .map(String::toUpperCase);

        //When
        nameMono.subscribe(s -> log.info("The dev is {} in UpperCase", s),
                Throwable::printStackTrace,
                () -> log.info("PROCESS FINISHED!"));

        //Then
        log.info("-------Expected------------");

        StepVerifier.create(nameMono)
                .expectNext(name.toUpperCase())
                .verifyComplete();
    }

    @Test
    void monoSubscriberConsumerSubscription() {
        //Given
        log.info("-------Actual------------");

        var name = "guipalm4";
        Mono<String> nameMono = Mono.just(name)
                .log()
                .map(String::toUpperCase);

        //When
        nameMono.subscribe(s -> log.info("The dev is {} in UpperCase", s),
                Throwable::printStackTrace,
                () -> log.info("PROCESS FINISHED!")
                , subscription -> subscription.request(5));

        //Then
        log.info("-------Expected------------");

        StepVerifier.create(nameMono)
                .expectNext(name.toUpperCase())
                .verifyComplete();
    }

    @Test
    void monoDoOnMethods() {
        //Given
        log.info("-------Actual------------");

        var name = "guipalm4";
        Mono<Object> mono = Mono.just(name)
                .log()
                .map(String::toUpperCase)
                .doOnSubscribe(subscription -> log.info("Subscribed"))
                .doOnRequest(longNumber -> log.info("Request received, starting doing something..."))
                .doOnNext(s -> log.info("Value is here. Executing doOnNext with {}", s))
                .flatMap(s -> Mono.empty())
                .doOnNext(s -> log.info("Value is here. Executing doOnNext with {}", s))//will not be executed
                .doOnSuccess(s -> log.info("doOnSuccess executed {}", s));

        //When
        mono.subscribe(s -> log.info("The dev is {} in UpperCase", s),
                Throwable::printStackTrace,
                () -> log.info("PROCESS FINISHED!"));
    }

    @Test
    void monoDoOnError() {

        //When
        Mono<Object> error = Mono.error(new IllegalArgumentException("Error illegal argument exception"))
                .doOnError(e -> MonoTest.log.error("Message: {}", e.getMessage()))
                .doOnNext(s -> log.info("This never execute when throw an exception"))
                .log();

        //Then
        StepVerifier.create(error)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void monoDoOnErrorResume() {

        //Given
        var fallbackMessage = "Be careful, something bah happened";

        //When
        Mono<Object> error = Mono.error(new IllegalArgumentException("Error illegal argument exception"))
                .doOnError(e -> MonoTest.log.error("Message: {}", e.getMessage()))
                .onErrorResume(s -> {
                    log.info("Something bad happened, but i will continue");
                    return Mono.just(fallbackMessage);
                })
                .log();

        //Then
        StepVerifier.create(error)
                .expectNext(fallbackMessage)
                .verifyComplete();
    }

    @Test
    void monoDoOnErrorReturn() {

        //Given
        var fallbackMessage = "Be careful, something bah happened";
        var blocking = "Block Error Happened";

        //When
        Mono<Object> error = Mono.error(new IllegalArgumentException("Error illegal argument exception"))
                .onErrorReturn(blocking)
                //Ignored because RETURN...
                .onErrorResume(s -> {
                    log.info("Something bad happened, but i will continue");
                    return Mono.just(fallbackMessage);
                })
                .doOnError(e -> MonoTest.log.error("Message: {}", e.getMessage()))
                .log();

        //Then
        StepVerifier.create(error)
                .expectNext(blocking)
                .verifyComplete();
    }

}
