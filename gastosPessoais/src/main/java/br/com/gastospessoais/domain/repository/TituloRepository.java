package br.com.gastospessoais.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gastospessoais.domain.model.Titulo;

public interface TituloRepository extends JpaRepository<Titulo, Long> {

}
