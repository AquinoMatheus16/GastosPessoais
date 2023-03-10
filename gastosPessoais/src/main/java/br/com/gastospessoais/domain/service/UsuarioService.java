package br.com.gastospessoais.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.gastospessoais.domain.excption.ResourceBadRequestException;
import br.com.gastospessoais.domain.excption.ResourceNotFoundtException;
import br.com.gastospessoais.domain.model.Usuario;
import br.com.gastospessoais.domain.repository.UsuarioRepository;
import br.com.gastospessoais.dto.usuario.UsuarioRequestDto;
import br.com.gastospessoais.dto.usuario.UsuarioResponseDto;

@Service
public class UsuarioService implements ICRUDService<UsuarioRequestDto, UsuarioResponseDto> {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

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

	public UsuarioResponseDto obterPorEmail(String email) {
		Optional<Usuario> optUsuario = usuarioRepository.findByEmail(email);

		if (optUsuario.isEmpty()) {
			throw new ResourceNotFoundtException("Não foi possível encontrar o usuário com o e-mail: " + email);
		}

		return mapper.map(optUsuario.get(), UsuarioResponseDto.class);
	}

	@Override
	public UsuarioResponseDto cadastrar(UsuarioRequestDto dto) {

		validarUsuario(dto);

		Optional<Usuario> optUsuario = usuarioRepository.findByEmail(dto.getEmail());

		if (optUsuario.isPresent()) {
			throw new ResourceBadRequestException("Já existe um usuário cadastrado com o e-mail: " + dto.getEmail());
		}

		Usuario usuario = mapper.map(dto, Usuario.class);

		String senha = passwordEncoder.encode(usuario.getSenha());
		usuario.setSenha(senha);

		usuario.setId(null);
		usuario.setDataCadastro(new Date());
		usuario = usuarioRepository.save(usuario);

		return mapper.map(usuario, UsuarioResponseDto.class);
	}

	@Override
	public UsuarioResponseDto atualizar(Long id, UsuarioRequestDto dto) {

		UsuarioResponseDto usuarioBanco = obterPorId(id);
		validarUsuario(dto);

		Usuario usuario = mapper.map(dto, Usuario.class);

		String senha = passwordEncoder.encode(dto.getSenha());
		usuario.setSenha(senha);

		usuario.setId(id);
		usuario.setDataInativacao(usuarioBanco.getDataInativacao());
		usuario.setDataCadastro(usuarioBanco.getDataCadastro());
		usuario = usuarioRepository.save(usuario);

		return mapper.map(usuario, UsuarioResponseDto.class);
	}

	@Override
	public void deletar(Long id) {

		Optional<Usuario> optUsuario = usuarioRepository.findById(id);

		if (optUsuario.isEmpty()) {
			throw new ResourceNotFoundtException("Não foi possível encontrar o usuário com o id: " + id);
		}

		Usuario usuario = optUsuario.get();
		usuario.setDataInativacao(new Date());

		usuarioRepository.save(usuario);
	}

	private void validarUsuario(UsuarioRequestDto dto) {

		if (dto.getEmail() == null || dto.getSenha() == null) {
			throw new ResourceBadRequestException("E-mail e senha são obrigatórios");
		}
	}
}
