package br.com.gastospessoais.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.gastospessoais.common.ConversorData;
import br.com.gastospessoais.domain.excption.ResourceBadRequestException;
import br.com.gastospessoais.domain.excption.ResourceNotFoundtException;
import br.com.gastospessoais.domain.model.ErrorResposta;

@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(ResourceNotFoundtException.class)
	public ResponseEntity<ErrorResposta> handlerResourceNotFoundExeption(ResourceNotFoundtException ex) {

		String dataHora = ConversorData.converterDateParaDataEHora(new Date());

		ErrorResposta erro = new ErrorResposta(dataHora, HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage());

		return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ResourceBadRequestException.class)
	public ResponseEntity<ErrorResposta> handlerResourceBadRequestExeption(ResourceBadRequestException ex) {

		String dataHora = ConversorData.converterDateParaDataEHora(new Date());

		ErrorResposta erro = new ErrorResposta(dataHora, HttpStatus.BAD_REQUEST.value(), "Bad Request",
				ex.getMessage());

		return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResposta> handlerRequestExeption(Exception ex) {

		String dataHora = ConversorData.converterDateParaDataEHora(new Date());

		ErrorResposta erro = new ErrorResposta(dataHora, HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", ex.getMessage());

		return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
