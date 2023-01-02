package br.com.gastospessoais.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gastospessoais.domain.model.CentroDeCusto;
import br.com.gastospessoais.domain.model.Usuario;

public interface CentroDeCustoRepository extends JpaRepository<CentroDeCusto, Long> {

	List<CentroDeCusto> findByUsuario(Usuario usuario);
}
