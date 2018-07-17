package it.unipd.math.pcd.actors;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A message container used by an actor to store messages
 *
 * @author Alessandro Pol
 * @version 1.0
 * @since 1.0
 */

public class mailBox<T extends Message> {

    /**
     * A nested class named Mex that contain the message T and a destination represented by an ActorRef
     *
     * @author Alessandro Pol
     * @version 1.0
     * @since 1.0
     */

    public class mex<T extends Message> {
        private T message;
        private ActorRef<T> sender;

        public mex(T _message, ActorRef<T> _sender){
            message=_message;
            sender=_sender;
        }

        public T get_message(){
            return message;
        }

        public ActorRef<T> get_sender(){
            return sender;
        }
    }

    // list than contains the message
    private ConcurrentLinkedQueue<mex<T>> mail= new ConcurrentLinkedQueue<>();

    /**
     * Insert a new Mex in the tail of the list
     * @param mes the message to store
     * @param ref the destination of the message
     *
     */
    public void push(T mes, ActorRef<T> ref) {

        mail.add(new mex<>(mes,ref));
    }

    /**
     * Return the first message stored in the list
     * @return the message stored
     *
     */
    public mex<T> pop(){
        return mail.poll();
    } 

    /**
     * Control if the list is full or empty
     * @return true if list is empty
     *
     */
    public boolean empty()
    {
        return mail.isEmpty();
    }

    public int sizeBox() { return mail.size();}
}
