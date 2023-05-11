package flight.rest.agentii_turism.exceptions;

/**
 * All self-made exception will extend this class.
 */
public abstract class BaseException extends Exception{
    BaseException(String msg){
        super(msg);
    }
}

