/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2015 Riccardo Cardin
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p/>
 * Please, insert description here.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */

/**
 * Please, insert description here.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */
package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;

/**
 * Defines common properties of all actors.
 *
 * @author Alessandro Pol
 * @version 2.0
 * @since 1.0
 */


public abstract class AbsActor<T extends Message> extends Thread implements Actor<T> {

    private mailBox<T> Mbox=new mailBox<>();
    private boolean die=false;

    /**
     * Self-reference of the actor
     */
    protected ActorRef<T> self;

    /**
     * Sender of the current message
     */
    protected ActorRef<T> sender;

    /**
     * Sets the self-referece.
     *
     * @param self The reference to itself
     * @return The actor.
     */
    protected final Actor<T> setSelf(ActorRef<T> self) {
        this.self = self;
        return this;
    }

    public AbsActor(){
        start();
    }

    /**
     * Implementation of run method
     *
     */
    @Override
    public void run() {
        try{
            while(!this.die)//until the actor is active
            {
                synchronized (Mbox)//take control of the mailbox
                {
                    while (Mbox.empty())//if it's empty
                    {
                        Mbox.wait();  //the actor stops
                    }
                    mailBox<T>.mex<T>  mex_mailBox=Mbox.pop(); //extract the message

                    sender=mex_mailBox.get_sender(); //get destination
                    T mess=mex_mailBox.get_message(); //get message

                    receive(mess); //execute the message
                }
            }
        }
        catch (InterruptedException e) {
                this.killActor();
                e.printStackTrace();
        }
    }

    /**
     * empty the mail box
     *
     */
    public synchronized void emptyMB(){
        while(!Mbox.empty()){
            mailBox<T>.mex<T>  mex_mailBox=Mbox.pop();
            sender=mex_mailBox.get_sender();
            T mess=mex_mailBox.get_message();
            receive(mess);
        }
    }

    /**
     * stop the actor
     *
     */
    public synchronized void killActor(){
        if(die==true)
            throw new NoSuchActorException();
        else
        {
            this.die=true;
            emptyMB();
        }
    }

    /**
     * Implementation of run method
     * @param mes the message to store
     * @param ref the destination
     */
    public void incomingMessage(T mes,ActorRef<T> ref){
        if(!this.die){
            synchronized (Mbox)
            {
                Mbox.push(mes,ref);//push the message inside the mailbox
                Mbox.notifyAll();//wake up actor
            }
        }
    }

    public boolean isDie(){
        return die;
    }

    public int messageSize(){
        return Mbox.sizeBox();
    }
}
