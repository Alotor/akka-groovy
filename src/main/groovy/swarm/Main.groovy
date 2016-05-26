package swarm

import akka.actor.ActorSystem
import akka.actor.Props

class Main {
    static void main(args) {
        println ">>> Starting..."
        def system = ActorSystem.create("SwarmTest")
        def reaper = system.actorOf(Props.create(Reaper), "reaper")

        10.times { Integer id ->
            def actor = system.actorOf(Props.create(Processor, id))
            reaper.tell(Reaper.request(actor), null)
            actor.tell(Processor.Msg.START, null)
        }

        system.awaitTermination()
        println ">>> DONE..."
    }
}

