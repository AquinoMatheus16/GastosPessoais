package br.com.gastospessoais.security;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import br.com.gastospessoais.common.ConversorData;
import br.com.gastospessoais.domain.model.ErrorResposta;
import br.com.gastospessoais.domain.model.Usuario;
import br.com.gastospessoais.dto.usuario.LoginRequestDto;
import br.com.gastospessoais.dto.usuario.LoginResponseDto;
import br.com.gastospessoais.dto.usuario.UsuarioResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private JwtUtil jwtUtil;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;

		setFilterProcessesUrl("/api/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse sesponse) {

		try {

			LoginRequestDto login = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(login.getEmail(),
					login.getSenha());
			Authentication auth = authenticationManager.authenticate(authToken);
			return auth;

		} catch (BadCredentialsException e) {

			throw new BadCredentialsException("Usu??rio ou senha inv??lidos");
		} catch (Exception e) {

			throw new InternalAuthenticationServiceException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException {

		Usuario usuario = (Usuario) authResult.getPrincipal();
		String token = jwtUtil.gerarToken(authResult);

//		UsuarioResponseDto usuarioResponse = new UsuarioResponseDto();
//		usuarioResponse.setEmail(usuario.getEmail());
//		usuarioResponse.setId(usuario.getId());
//		usuarioResponse.setFoto(usuario.getFoto());

//		UsuarioResponseDto usuarioResponse = mapper.map(usuario, UsuarioResponseDto.class);

		UsuarioResponseDto usuarioResponse = new UsuarioResponseDto();
		usuarioResponse.setId(usuario.getId());
		usuarioResponse.setEmail(usuario.getEmail());
		usuarioResponse.setNome(usuario.getNome());
		usuarioResponse.setFoto(usuario.getFoto());
		usuarioResponse.setDataInativacao(usuario.getDataInativacao());
		usuarioResponse.setDataCadastro(usuario.getDataCadastro());

		LoginResponseDto loginResponseDto = new LoginResponseDto();
		loginResponseDto.setToken("Bearer " + token);
		loginResponseDto.setUsuario(usuarioResponse);

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.getWriter().write(new Gson().toJson(loginResponseDto));
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {

		String dataHora = ConversorData.converterDateParaDataEHora(new Date());
		ErrorResposta erro = new ErrorResposta(dataHora, 401, "Unauthorized", failed.getMessage());

		response.setStatus(401);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.getWriter().write(new Gson().toJson(erro));
	}
}
