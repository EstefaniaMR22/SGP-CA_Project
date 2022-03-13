package controller.exceptions;

public class LimitReachedException extends Exception{
    public LimitReachedException(String message) {
        super(message);
    }
}
