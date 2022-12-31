package br.com.gastospessoais.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.gastospessoais.domain.model.Usuario;
import br.com.gastospessoais.domain.repository.UsuarioRepository;
import br.com.gastospessoais.dto.usuario.UsuarioRequestDto;
import br.com.gastospessoais.dto.usuario.UsuarioResponseDto;

public class UsuarioService implements ICRUDService<UsuarioRequestDto, UsuarioResponseDto> {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public List<UsuarioResponseDto> obterTodos() {

		List<Usuario> usuarios = usuarioRepository.findAll();

		// == PRIMEIRA FORMA ==
//		List<UsuarioResponseDto> usuariosDto = new ArrayList<>();
//		for (Usuario usuario : usuarios) {
//			UsuarioResponseDto dto = mapper.map(usuario, UsuarioResponseDto.class);
//			usuariosDto.add(dto);
//		}
//		return usuariosDto;

		// == SEGUNDA FORMA ==
		return usuarios.stream().map(usuario -> mapper.map(usuario, UsuarioResponseDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public UsuarioResponseDto obterPorID(Long id) {
		Optional<Usuario> optUsuario = usuarioRepository.findById(id);

		if (optUsuario.isEmpty()) {
			//
		}

		return mapper.map(optUsuario.get(), UsuarioResponseDto.class);
	}

	@Override
	public UsuarioResponseDto cadastrar(UsuarioRequestDto dto) {
		return null;
	}

	@Override
	public UsuarioResponseDto atualizar(Long id, UsuarioRequestDto dto) {
		return null;
	}

	@Override
	public void deletar(Long id) {

	}

}
