package swarm

import akka.actor.ActorSystem
import akka.actor.Props

class Main {
    static void main(args) {
        println ">>> Starting..."
        def system = ActorSystem.create("SwarmTest")
        def reaper = system.actorOf(Props.create(Reaper), "reaper")

        100.times { Integer id ->
            def actor = system.actorOf(Props
                                       .create(Processor, id)
                                       // Can change 10 to 1 or 20 to use different thread pool policies
                                       // Check the file /src/main/resources/application.conf
                                       .withDispatcher("thread-pool-dispatcher-10"))
            reaper.tell(Reaper.request(actor), null)
            actor.tell(Processor.Msg.START, null)
        }

        system.awaitTermination()
        println ">>> DONE..."
    }
}

