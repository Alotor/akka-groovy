package swarm

import static akka.japi.pf.ReceiveBuilder.matchEquals

import akka.actor.AbstractActor
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

public class Processor extends AbstractActor {
    Integer id

    public static enum Msg {
        START,
        DONE
    }

    public Processor(Integer id) {
        this.id = id

        receive(
            matchEquals(Msg.START, this.&doStart).
            matchEquals(Msg.DONE, { m -> println " - stop ${id}" ; context.stop(self()) }).
            build()
        )
    }

    @Override
    void postRestart(Throwable reason) {
        println ">> Restart ${id}"
        self().tell(Msg.START, self())
    }

    void doStart(Msg message) {
        def t = new Random().nextInt(10000)
        // def timeout = new FiniteDuration(t, TimeUnit.MILLISECONDS)

        println " + Processor ${id} start (${t})"

        if (t < 1000) {
            // Some processes caused random exceptions
            println " ! Failing ${id}"
            throw new Exception(">> Error in ${id}")
        }

        def system = context.system()

        // system.scheduler()
        //       .scheduleOnce(timeout, self(), Msg.DONE, system.dispatcher(), self())
        println  " > sleeping ${id} - ${t}"

        Thread.sleep(t)
        self().tell(Msg.DONE, self())
    }

}
