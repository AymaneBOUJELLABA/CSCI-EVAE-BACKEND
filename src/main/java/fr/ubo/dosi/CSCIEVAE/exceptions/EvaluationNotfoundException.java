package fr.ubo.dosi.CSCIEVAE.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
public class EvaluationNotfoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private HttpStatus status = null;

    private Object data = null;

    public EvaluationNotfoundException() {
        super();
    }

    public EvaluationNotfoundException(
            String message
    ) {
        super(message);
    }

    public EvaluationNotfoundException(
            HttpStatus status,
            String message
    ) {
        this(message);
        this.status = status;
    }

    public EvaluationNotfoundException(
            HttpStatus status,
            String message,
            Object data
    ) {
        this(
                status,
                message
        );
        this.data = data;
    }
}
