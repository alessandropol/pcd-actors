package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.impl.ActorSystemImpl;

/**
 * A concrete implementation of ActorRef
 *
 * @author Alessandro Pol
 * @version 1.0
 * @since 1.0
 */

public class CntActorRef<T extends Message> implements ActorRef<T> {

    private ActorSystemImpl actor_system;

    public CntActorRef(ActorSystemImpl AS){
        actor_system=AS;
    }

    /**
     * Compare two ActorRef and control if the are equals
     * @param AR contain the second ActorRef to compare
     * @return a value less 0 if the message are not the same
     *
     */
    @Override
    public int compareTo(ActorRef AR) {
        return ( this == AR ) ? 0 : -1;

    }

    /**
     * Concrete implementation of send function
     * @param message contain to send
     * @param to contain the destination
     *
     */

    @Override
    public void send(T message, ActorRef to) {

        AbsActor act=(AbsActor) actor_system.getActor(to);
        act.incomingMessage(message,this);
    }
}
