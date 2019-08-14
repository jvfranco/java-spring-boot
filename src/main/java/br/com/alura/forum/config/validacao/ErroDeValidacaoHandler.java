package br.com.alura.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Classe que realiza a interceptação de erros e os trata de acordo com o objeto criado, neste caso ErroDeFormularioDto
 * 
 * @author Joao Victor
 *
 */

@RestControllerAdvice
public class ErroDeValidacaoHandler {
	
	@Autowired
	private MessageSource messageSource; //classe do Spring que auxilia recuperar a mensagem de erro
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST) //Anotação que informa qual o status que deve ser devolvido mesmo com o tratamento da exceção
	@ExceptionHandler(MethodArgumentNotValidException.class) // Anotação que intercepta toda exceção MethodArgumentNotValidException
	public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception) { //dentro deste objeto estão todos os erros encontrados
		List<ErroDeFormularioDto> dto = new ArrayList<>();
		
		//getBindingResults retorna os erros e getFieldErrors retorna todos os erros de formulario que ocorreram
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		fieldErrors.forEach(e -> {
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(), mensagem);
			dto.add(erro);
		});
		
		return dto;
	}
	
}
