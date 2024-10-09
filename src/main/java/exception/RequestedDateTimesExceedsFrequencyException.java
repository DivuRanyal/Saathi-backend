package exception;

public class RequestedDateTimesExceedsFrequencyException extends Exception {
    
    public RequestedDateTimesExceedsFrequencyException(String message) {
        super(message);
    }
    
    public RequestedDateTimesExceedsFrequencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
