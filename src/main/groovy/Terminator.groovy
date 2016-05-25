import static akka.japi.pf.ReceiveBuilder.match

import akka.actor.AbstractActor
import akka.actor.Terminated
import akka.actor.ActorRef

public class Terminator extends AbstractActor {
    private final ActorRef ref

    public Terminator(ActorRef ref) {
        this.ref = ref
        getContext().watch(ref);

        receive(
            match(Terminated, this.&doTerminate).
            build()
        )
    }

    void doTerminate(t) {
        println("${ref.path()} has terminated, shutting down system");
        context().system().terminate()
    }
}
