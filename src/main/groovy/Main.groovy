import akka.actor.ActorSystem
import akka.actor.Props

class Main {
    static void main(args) {
        ActorSystem system = ActorSystem.create("Hello")
        def actor = system.actorOf(Props.create(HelloWorld), "helloWorld")
        system.actorOf(Props.create(Terminator, actor), "terminator");
    }
}

