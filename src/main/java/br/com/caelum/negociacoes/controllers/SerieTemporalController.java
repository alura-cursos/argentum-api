package br.com.caelum.negociacoes.controllers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.caelum.negociacoes.indicadores.Indicador;
import br.com.caelum.negociacoes.modelos.Candle;
import br.com.caelum.negociacoes.modelos.CandlestickFactory;
import br.com.caelum.negociacoes.modelos.GerenciadorDeData;
import br.com.caelum.negociacoes.modelos.GerenciadorDeNegociacao;
import br.com.caelum.negociacoes.modelos.GerenciadorDeValor;
import br.com.caelum.negociacoes.modelos.Negociacao;
import br.com.caelum.negociacoes.modelos.SerieTemporal;
import br.com.caelum.negociacoes.ws.ClienteWebService;

@Controller
public class SerieTemporalController {

	@Autowired
	private ClienteWebService clienteWebService;

	@Autowired
	private GerenciadorDeNegociacao gerenciadorDeNegociacao;

	@Autowired
	private GerenciadorDeData gerenciadorDeData;

	@Autowired
	GerenciadorDeValor gerenciadorDeValor;

	@GetMapping("/serie-temporal")
	@ResponseBody
	public GerenciadorDeValor listaSeries(
			@RequestParam(value = "indicador", required = true, defaultValue = "IndicadorFechamento") Optional<String> indicadorParametro,
			@RequestParam(value = "media", required = true, defaultValue = "MediaMovelSimples") Optional<String> mediaParametro,
			@RequestParam(value = "de") @DateTimeFormat(pattern = "yyyy/MM/dd") Optional<Calendar> dataInicialParametro,
			@RequestParam(value = "ate") @DateTimeFormat(pattern = "yyyy/MM/dd") Optional<Calendar> dataFinalParametro) {
		
		gerenciadorDeNegociacao.inicia();
		gerenciadorDeValor.inicia();
		
		List<Negociacao> negociacoes = this.clienteWebService.getNegociacoes();
		

		gerenciadorDeData.defineData(dataInicialParametro, dataFinalParametro);
		
		gerenciadorDeNegociacao.filtraNegociacoes(negociacoes, gerenciadorDeData);

		gerenciadorDeNegociacao.setQuantidade(gerenciadorDeNegociacao.getNegociacoes().size());

		
		List<Candle> candles = new CandlestickFactory().constroiCandles(gerenciadorDeNegociacao.getNegociacoes());
		SerieTemporal serie = new SerieTemporal(candles);
		
		gerenciadorDeValor.setValores(plotaMediaMovelSimples(defineIndicador(indicadorParametro.get(), 
				mediaParametro.get()),serie , 2, serie.getUltimaPosicao()));
		
		gerenciadorDeValor.setQuantidade(gerenciadorDeValor.getValores().size());
		
		return gerenciadorDeValor;
	}

	private Indicador defineIndicador(String nomeIndicadorBase, String nomeMedia) {
		String pacote = "br.com.caelum.negociacoes.indicadores.";
		try {
			Class<?> classeIndicadorBase = Class.forName(pacote + nomeIndicadorBase);
			Indicador indicadorBase = (Indicador) classeIndicadorBase.newInstance();

			Class<?> classeMedia = Class.forName(pacote + nomeMedia);
			Constructor<?> construtorMedia = classeMedia.getConstructor(Indicador.class);
			Indicador indicador = (Indicador) construtorMedia.newInstance(indicadorBase);
			return indicador;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public ArrayList<Long> plotaMediaMovelSimples(Indicador indicador, SerieTemporal serie, int comeco, int fim) {
		ArrayList<Long> valores = new ArrayList<Long>();
		for (int i = comeco; i <= fim; i++) {
			Double valor = indicador.calcula(i, serie);
			valores.add(Math.round(valor));
		}

		return valores;
	}

}
