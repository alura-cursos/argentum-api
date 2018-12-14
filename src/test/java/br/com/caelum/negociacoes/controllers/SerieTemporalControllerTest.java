package br.com.caelum.negociacoes.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import br.com.caelum.negociacoes.indicadores.Indicador;
import br.com.caelum.negociacoes.modelos.Candle;
import br.com.caelum.negociacoes.modelos.CandlestickFactory;
import br.com.caelum.negociacoes.modelos.GerenciadorDeData;
import br.com.caelum.negociacoes.modelos.GerenciadorDeNegociacao;
import br.com.caelum.negociacoes.modelos.GerenciadorDeValor;
import br.com.caelum.negociacoes.modelos.Negociacao;
import br.com.caelum.negociacoes.modelos.SerieTemporal;
import br.com.caelum.negociacoes.ws.ClienteWebService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SerieTemporalControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@Autowired
	private ClienteWebService clienteWebService;

	@Autowired
	private GerenciadorDeNegociacao gerenciadorDeNegociacao;

	@Autowired
	private GerenciadorDeData gerenciadorDeData;

	@Autowired
	GerenciadorDeValor gerenciadorDeValor;

	private Optional<Calendar> dataInicialParametro;
	private Optional<Calendar> dataFinalParametro;

	@Test
	public void deveRetornarTodasAsSeriesTemporaisCasoSejaChamadoApenasAUrlSemParametros() throws Exception {
		dataInicialParametro = Optional.empty();
		dataFinalParametro = Optional.empty();
		List<Negociacao> negociacoes = this.clienteWebService.getNegociacoes();

		gerenciadorDeData.defineData(dataInicialParametro, dataFinalParametro);

		gerenciadorDeNegociacao.filtraNegociacoes(negociacoes, gerenciadorDeData);

		gerenciadorDeNegociacao.setQuantidade(gerenciadorDeNegociacao.getNegociacoes().size());

		List<Candle> candles = new CandlestickFactory().constroiCandles(gerenciadorDeNegociacao.getNegociacoes());
		SerieTemporal serie = new SerieTemporal(candles);


		gerenciadorDeValor.setValores(plotaMediaMovelSimples(
				defineIndicador("IndicadorFechamento", "MediaMovelSimples"), serie, 2, serie.getUltimaPosicao()));

		gerenciadorDeValor.setQuantidade(gerenciadorDeValor.getValores().size());

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

		String json = ow.writeValueAsString(gerenciadorDeValor);

		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/serie-temporal", String.class))
				.contains(json);
	}

	@Test
	public void deveRetornarSerieTemporalDeIndicadorFechamentoEMediaMovelPonderada() throws Exception {
		
		gerenciadorDeNegociacao.inicia();
		gerenciadorDeValor.inicia();
		
		dataInicialParametro = Optional.empty();
		dataFinalParametro = Optional.empty();
		List<Negociacao> negociacoes = this.clienteWebService.getNegociacoes();

		gerenciadorDeData.defineData(dataInicialParametro, dataFinalParametro);

		gerenciadorDeNegociacao.filtraNegociacoes(negociacoes, gerenciadorDeData);

		gerenciadorDeNegociacao.setQuantidade(gerenciadorDeNegociacao.getNegociacoes().size());

		List<Candle> candles = new CandlestickFactory().constroiCandles(gerenciadorDeNegociacao.getNegociacoes());
		SerieTemporal serie = new SerieTemporal(candles);


		gerenciadorDeValor.setValores(plotaMediaMovelSimples(
				defineIndicador("IndicadorFechamento", "MediaMovelPonderada"), serie, 2, serie.getUltimaPosicao()));

		gerenciadorDeValor.setQuantidade(gerenciadorDeValor.getValores().size());
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

		String json = ow.writeValueAsString(gerenciadorDeValor);
		
		
		assertThat(this.restTemplate.getForObject(
				"http://localhost:" + port + "/serie-temporal?indicador=IndicadorFechamento&media=MediaMovelPonderada",
				String.class)).isEqualTo(json);
	}

	@Test
	public void deveRetornarWhitelabelCasoPaginaNaoExista() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/serie-tmporall", String.class))
				.contains("No message available");
	}

	@Test
	public void deveRetornarSerieTemporalDeIndicadorAberturaEMediaMovelPonderada() throws Exception {
		
		gerenciadorDeNegociacao.inicia();
		gerenciadorDeValor.inicia();
		
		dataInicialParametro = Optional.empty();
		dataFinalParametro = Optional.empty();
		List<Negociacao> negociacoes = this.clienteWebService.getNegociacoes();

		gerenciadorDeData.defineData(dataInicialParametro, dataFinalParametro);

		gerenciadorDeNegociacao.filtraNegociacoes(negociacoes, gerenciadorDeData);

		gerenciadorDeNegociacao.setQuantidade(gerenciadorDeNegociacao.getNegociacoes().size());

		List<Candle> candles = new CandlestickFactory().constroiCandles(gerenciadorDeNegociacao.getNegociacoes());
		SerieTemporal serie = new SerieTemporal(candles);


		gerenciadorDeValor.setValores(plotaMediaMovelSimples(
				defineIndicador("IndicadorAbertura", "MediaMovelPonderada"), serie, 2, serie.getUltimaPosicao()));

		gerenciadorDeValor.setQuantidade(gerenciadorDeValor.getValores().size());
		
	
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

		String json = ow.writeValueAsString(gerenciadorDeValor);
		
		
		assertThat(this.restTemplate.getForObject(
				"http://localhost:" + port + "/serie-temporal?indicador=IndicadorAbertura&media=MediaMovelPonderada",
				String.class)).isEqualTo(json);
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
