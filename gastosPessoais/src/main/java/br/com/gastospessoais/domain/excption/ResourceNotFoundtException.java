package br.com.gastospessoais.domain.excption;

public class ResourceNotFoundtException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundtException(String mensagem) {
		super(mensagem);
	}
}
