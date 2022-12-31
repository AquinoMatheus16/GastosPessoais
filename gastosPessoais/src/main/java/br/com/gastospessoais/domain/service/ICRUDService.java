package br.com.gastospessoais.domain.service;

import java.util.List;

// Req = REQUISICAO || Req = RESPONSE
public interface ICRUDService<Req, Res> {

	List<Res> obterTodos();

	Res obterPorID(Long id);

	Res cadastrar(Req dto);

	Res atualizar(Long id, Req dto);

	void deletar(Long id);
}
