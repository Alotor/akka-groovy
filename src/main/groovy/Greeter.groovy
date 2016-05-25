import static akka.japi.pf.ReceiveBuilder.matchEquals
import akka.actor.AbstractActor

public class Greeter extends AbstractActor {

    public static enum Msg {
        GREET,
        DONE
    }

    public Greeter() {
        receive(
            matchEquals(Msg.GREET, this.&doGreet).
            build()
        )
    }

    void doGreet(Msg message) {
        println ">>> Hello World!! ${message}"
        sender().tell(Msg.DONE, self());
    }



}
