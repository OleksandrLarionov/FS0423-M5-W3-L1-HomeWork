package larionovoleksanr.exam.exceptions;
import lombok.Getter;
import org.springframework.validation.ObjectError;
import java.util.List;

@Getter
public class BadRequestException extends RuntimeException {
    private List<ObjectError> errorList;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(List<ObjectError> errorList) {
        super("Errore nel payload");
        this.errorList = errorList;
    }
}
