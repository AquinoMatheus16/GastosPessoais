package br.com.gastospessoais.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.gastospessoais.domain.excption.ResourceBadRequestException;
import br.com.gastospessoais.domain.excption.ResourceNotFoundtException;
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
	public UsuarioResponseDto obterPorId(Long id) {
		Optional<Usuario> optUsuario = usuarioRepository.findById(id);

		if (optUsuario.isEmpty()) {
			throw new ResourceNotFoundtException("Não foi possível encontrar o usuário com o id: " + id);
		}

		return mapper.map(optUsuario.get(), UsuarioResponseDto.class);
	}

	@Override
	public UsuarioResponseDto cadastrar(UsuarioRequestDto dto) {

		validarUsuario(dto);

		Usuario usuario = mapper.map(dto, Usuario.class);

		usuario.setId(null);
		usuario = usuarioRepository.save(usuario);

		return mapper.map(usuario, UsuarioResponseDto.class);
	}

	@Override
	public UsuarioResponseDto atualizar(Long id, UsuarioRequestDto dto) {

		UsuarioResponseDto usuarioBanco = obterPorId(id);
		validarUsuario(dto);

		Usuario usuario = mapper.map(dto, Usuario.class);

		usuario.setId(id);
		usuario.setDataInativacao(usuarioBanco.getDataInativacao());
		usuario = usuarioRepository.save(usuario);

		return mapper.map(usuario, UsuarioResponseDto.class);
	}

	@Override
	public void deletar(Long id) {

		UsuarioResponseDto usuarioEncontrado = obterPorId(id);

		Usuario usuario = mapper.map(usuarioEncontrado, Usuario.class);

		usuario.setDataInativacao(new Date());
		usuarioRepository.save(usuario);
	}

	private void validarUsuario(UsuarioRequestDto dto) {

		if (dto.getEmail() == null || dto.getSenha() == null) {
			throw new ResourceBadRequestException("E-mail e senha são obrigatórios");
		}
	}
}
