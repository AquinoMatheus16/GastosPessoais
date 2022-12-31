package br.com.gastospessoais.domain.model;

public class ErrorResposta {

	private String dataHora;

	private Integer status;

	private String titulo;

	private String mensagem;

	public ErrorResposta(String titulo, Integer status, String mensagem, String dataHora) {
		super();
		this.titulo = titulo;
		this.status = status;
		this.mensagem = mensagem;
		this.dataHora = dataHora;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getDataHora() {
		return dataHora;
	}

	public void setDataHora(String dataHora) {
		this.dataHora = dataHora;
	}

}
