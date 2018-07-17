package it.unipd.math.pcd.actors.impl;


import it.unipd.math.pcd.actors.*;
import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;

/**
 * A concrete implementation of AbsActorSystem
 *
 * @author Alessandro Pol
 * @version 1.0
 * @since 1.0
 */

public class ActorSystemImpl extends AbsActorSystem {


    public int countACT(){
        return actors.size();
    }
    public AbsActor getACT(ActorRef<?> actor)
    {
        return (AbsActor) getActor(actor);
    }
    /**
     * A concrete implementation of stop function
     * @param actor the actor to stop
     *
     */
    @Override
    public void stop(ActorRef<?> actor) throws NoSuchActorException {
        if (actors.containsKey(actor))//controll if the actors is present in the list
        {
            AbsActor _actor= (AbsActor) getActor(actor); //get the istance
            _actor.killActor(); //stop the actor
            actors.remove(actor);//remove the actor from the list
        }
        else{
            throw  new NoSuchActorException();
        }
    }

    /**
     * Stop all actors present in the list
     *
     */

    @Override
    public void stop() {
        for (ActorRef<?> actor : actors.keySet()) {
            this.stop(actor);
        }
    }

    /**
     * Create new ActorReference
     * @param mode it specifies if the actor is LOCAL or REMOTE
     * @return a new reference of a ActorRef
     */
    @Override
    protected ActorRef createActorReference(ActorMode mode) {
        if( mode == ActorMode.LOCAL ){
            return new CntActorRef<>(this);
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }
}
