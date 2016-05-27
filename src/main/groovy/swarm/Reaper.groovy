package swarm

import akka.actor.UntypedActor
import akka.actor.ActorRef
import akka.actor.Terminated

class Reaper extends UntypedActor {
    static request(ActorRef actor) {
        new Request(actor: actor)
    }

    static class Request implements Serializable {
        ActorRef actor
    }

    final Set<ActorRef> watched = new TreeSet<ActorRef>()

    @Override
    public void onReceive(message) {
        switch(message) {
            case { it instanceof Request }:
                context.watch(message.actor)
                watched.add(message.actor)
                break;

            case { it instanceof Terminated }:
                watched.remove(message.actor)
                if (watched.isEmpty()) {
                    context.system.shutdown()
                }
                break

            default:
                println ">> Ein? ${message}"
                unhandled(message);
        }
    }
}