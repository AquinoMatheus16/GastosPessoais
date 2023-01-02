package br.com.gastospessoais.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gastospessoais.domain.enums.ETipoTitulo;
import br.com.gastospessoais.dto.dashboard.DashboardResponseDto;
import br.com.gastospessoais.dto.titulo.TituloResponseDto;

@Service
public class DashboardService {

	@Autowired
	private TituloService tituloService;

	public DashboardResponseDto obterFluxoDeCaixa(String periodoInicial, String periodoFinal) {

		List<TituloResponseDto> titulos = tituloService.obterPorDataDeVencimento(periodoInicial, periodoFinal);

		Double totalApagar = 0.0;
		Double totalAreceber = 0.0;
		Double saldo = 0.0;
		List<TituloResponseDto> titulosApagar = new ArrayList<>();
		List<TituloResponseDto> titulosAreceber = new ArrayList<>();

		for (TituloResponseDto titulo : titulos) {

			if (titulo.getTipo() == ETipoTitulo.APAGAR) {
				totalApagar += titulo.getValor();
				titulosApagar.add(titulo);
			} else {
				totalAreceber += titulo.getValor();
				titulosAreceber.add(titulo);
			}
		}

		saldo = totalAreceber - totalApagar;

		return new DashboardResponseDto(totalApagar, totalAreceber, saldo, titulosApagar, titulosAreceber);
	}
}
