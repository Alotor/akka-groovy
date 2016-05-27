package swarm

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.UntypedActor

import scala.concurrent.duration.Duration
import akka.actor.SupervisorStrategy
import akka.actor.SupervisorStrategy.Directive
import akka.actor.OneForOneStrategy
import akka.actor.Status
import akka.japi.Function

class Main extends UntypedActor {
    private static SupervisorStrategy strategy = new OneForOneStrategy(
        10,
        Duration.create("1 minute"),
        new Function<Throwable, Directive>() {
            @Override
            public Directive apply(Throwable t) {
                println ">> Apply $t"
                return SupervisorStrategy.restart();
            }
        })

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy
    }

    @Override
    void preStart() {
        def reaper = context.actorOf(Props.create(Reaper), "reaper")

        30.times { Integer id ->
            def actor = context.actorOf(Props
                                        .create(Processor, id)
                                        // Can change 10 to 1 or 20 to use different thread pool policies
                                        // Check the file /src/main/resources/application.conf
                                        .withDispatcher("thread-pool-dispatcher-10"))
            reaper.tell(Reaper.request(actor), self())
            actor.tell(Processor.Msg.START, self())
        }
    }

    @Override
    void onReceive(message) {
        unhandled(message)
    }

    static void main(args) {
        println ">>> Starting..."
        def system = ActorSystem.create("SwarmTest")

        system.actorOf(Props.create(Main), "main")
        system.awaitTermination()

        println ">>> DONE..."
    }
}

