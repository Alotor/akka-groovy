import akka.actor.AbstractActor
import akka.actor.Props
import akka.actor.ActorRef
import static akka.japi.pf.ReceiveBuilder.matchEquals

import static Greeter.Msg;

public class HelloWorld extends AbstractActor {
    public HelloWorld() {
        receive(
            matchEquals(Msg.DONE, this.&doDone).
            build()
        )
    }

    void doDone(Msg message) {
        context().stop(self())
    }

    @Override
    public void preStart() {
        def greeter = getContext().actorOf(Props.create(Greeter), "greeter")

        // tell it to perform the greeting
        greeter.tell(Msg.GREET, self())
    }
}
