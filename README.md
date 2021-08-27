# Study for reactive-programming with Reactor

# Reactive streams Recap
 1. Asynchronous
 2. Non-Blocking
 3. Backpressure
    <br />
      
        Publisher <- (subscribe) Subscriber
        Subscription is created
        Publisher (onSubscribe() with the subscription) -> Subscriber
        Subscription <- (request N) Subscriber
        Publisher -> (onNext()) Subscriber
    
     <i> until:</i>

      1. Publisher sends all the objects requested.
      2. Publisher sends all the objects it has. (onComplete()) subscriber and subscription will be canceled
      3. There is an error. (onError()) -> subscriber and subscription will be canceled
/
