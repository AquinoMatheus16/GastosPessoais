package br.com.gastospessoais.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.gastospessoais.domain.excption.ResourceBadRequestException;
import br.com.gastospessoais.domain.excption.ResourceNotFoundtException;
import br.com.gastospessoais.domain.model.Titulo;
import br.com.gastospessoais.domain.model.Usuario;
import br.com.gastospessoais.domain.repository.TituloRepository;
import br.com.gastospessoais.dto.titulo.TituloRequestDto;
import br.com.gastospessoais.dto.titulo.TituloResponseDto;

@Service
public class TituloService implements ICRUDService<TituloRequestDto, TituloResponseDto> {

	@Autowired
	private TituloRepository tituloRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public List<TituloResponseDto> obterTodos() {
		Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Titulo> titulos = tituloRepository.findByUsuario(usuario);

		return titulos.stream().map(titulo -> mapper.map(titulo, TituloResponseDto.class)).collect(Collectors.toList());
	}

	@Override
	public TituloResponseDto obterPorId(Long id) {
		Optional<Titulo> optTitulo = tituloRepository.findById(id);

		if (optTitulo.isEmpty()) {
			throw new ResourceNotFoundtException("Não foi possível encontrar o título com esse id: " + id);
		}

		return mapper.map(optTitulo.get(), TituloResponseDto.class);
	}

	@Override
	public TituloResponseDto cadastrar(TituloRequestDto dto) {

		validarTitulo(dto);

		Titulo titulo = mapper.map(dto, Titulo.class);

		Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		titulo.setUsuario(usuario);
		titulo.setId(null);
		titulo.setDataCadastro(new Date());
		titulo = tituloRepository.save(titulo);

		return mapper.map(titulo, TituloResponseDto.class);
	}

	@Override
	public TituloResponseDto atualizar(Long id, TituloRequestDto dto) {

		obterPorId(id);
		validarTitulo(dto);

		Titulo titulo = mapper.map(dto, Titulo.class);

		Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		titulo.setUsuario(usuario);
		titulo.setId(id);
		titulo = tituloRepository.save(titulo);

		return mapper.map(titulo, TituloResponseDto.class);
	}

	@Override
	public void deletar(Long id) {

		obterPorId(id);
		tituloRepository.deleteById(id);
	}

	private void validarTitulo(TituloRequestDto dto) {

		if (dto.getTipo() == null || dto.getDataVencimento() == null || dto.getValor() == null
				|| dto.getDescricao() == null) {

			throw new ResourceBadRequestException(
					"Os campos Tipo, Data de vencimento, Valor e Descrição são obrigatótios.");
		}
	}
}
