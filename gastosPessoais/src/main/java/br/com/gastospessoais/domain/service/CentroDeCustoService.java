package br.com.gastospessoais.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.gastospessoais.domain.excption.ResourceNotFoundtException;
import br.com.gastospessoais.domain.model.CentroDeCusto;
import br.com.gastospessoais.domain.model.Usuario;
import br.com.gastospessoais.domain.repository.CentroDeCustoRepository;
import br.com.gastospessoais.dto.centrodecusto.CentroDeCustoRequestDto;
import br.com.gastospessoais.dto.centrodecusto.CentroDeCustoResponseDto;

@Service
public class CentroDeCustoService implements ICRUDService<CentroDeCustoRequestDto, CentroDeCustoResponseDto> {

	@Autowired
	private CentroDeCustoRepository centroDeCustoRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public List<CentroDeCustoResponseDto> obterTodos() {
		List<CentroDeCusto> lista = centroDeCustoRepository.findAll();

		return lista.stream().map(centroDeCusto -> mapper.map(centroDeCusto, CentroDeCustoResponseDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public CentroDeCustoResponseDto obterPorId(Long id) {
		Optional<CentroDeCusto> optCentroDeCusto = centroDeCustoRepository.findById(id);

		if (optCentroDeCusto.isEmpty()) {
			throw new ResourceNotFoundtException("Não foi possível encontrar o centro de custo com esse id: " + id);
		}

		return mapper.map(optCentroDeCusto.get(), CentroDeCustoResponseDto.class);
	}

	@Override
	public CentroDeCustoResponseDto cadastrar(CentroDeCustoRequestDto dto) {

		CentroDeCusto centroDeCusto = mapper.map(dto, CentroDeCusto.class);

		Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		centroDeCusto.setUsuario(usuario);
		centroDeCusto.setId(null);
		centroDeCusto = centroDeCustoRepository.save(centroDeCusto);

		return mapper.map(centroDeCusto, CentroDeCustoResponseDto.class);
	}

	@Override
	public CentroDeCustoResponseDto atualizar(Long id, CentroDeCustoRequestDto dto) {

		obterPorId(id);
		CentroDeCusto centroDeCusto = mapper.map(dto, CentroDeCusto.class);

		Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		centroDeCusto.setUsuario(usuario);
		centroDeCusto.setId(id);
		centroDeCusto = centroDeCustoRepository.save(centroDeCusto);

		return mapper.map(centroDeCusto, CentroDeCustoResponseDto.class);
	}

	@Override
	public void deletar(Long id) {

		obterPorId(id);
		centroDeCustoRepository.deleteById(id);
	}

}
