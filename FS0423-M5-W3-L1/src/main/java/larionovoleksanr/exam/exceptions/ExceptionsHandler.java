package larionovoleksanr.exam.exceptions;

import larionovoleksanr.exam.payloads.ErrorsDTO;
import larionovoleksanr.exam.payloads.ErrorsPayloadWhitList;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestControllerAdvice
public class ExceptionsHandler {
	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorsPayloadWhitList handleBadRequest(BadRequestException ex) {



		List<String> errorsMessages = new ArrayList<>();
		if(ex.getErrorList() != null)
			errorsMessages = ex.getErrorList().stream().map(errore -> errore.getDefaultMessage()).toList();
		return new ErrorsPayloadWhitList(ex.getMessage(), newDateAndHour(), errorsMessages);
	}

	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
	public ErrorsDTO handleUnauthorized(UnauthorizedException e) {
		return new ErrorsDTO(e.getMessage(), LocalDateTime.now());
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorsPayload handleNotFound(NotFoundException ex) {
		return new ErrorsPayload(ex.getMessage(), LocalDateTime.now());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorsPayload handleGenericError(Exception ex) {
		ex.printStackTrace();
		return new ErrorsPayload("Un po di pazienza ci stiamo lavorando", LocalDateTime.now());
	}

	public static String newDateAndHour(){
		String pattern = "E, dd MMM yyyy HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		return date;
	}
}
