package br.com.caelum.negociacoes.controllers;

import static org.assertj.core.api.Assertions.assertThat;

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

import br.com.caelum.negociacoes.modelos.GerenciadorDeData;
import br.com.caelum.negociacoes.modelos.GerenciadorDeNegociacao;
import br.com.caelum.negociacoes.modelos.Negociacao;
import br.com.caelum.negociacoes.ws.ClienteWebService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class NegociacaoControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@Autowired
	ClienteWebService clienteWebService;

	@Autowired
	GerenciadorDeData gerenciadorDeData;

	@Autowired
	GerenciadorDeNegociacao gerenciadorDeNegociacao;

	private Optional<Calendar> dataInicialParametro;
	private Optional<Calendar> dataFinalParametro;

	@Test
	public void deveRetornartodasAsNegociacoesCasoSejaChamadoSemId() throws Exception {
		gerenciadorDeNegociacao.inicia();

		List<Negociacao> negociacoes = this.clienteWebService.getNegociacoes();
		
		dataInicialParametro = Optional.empty();
		dataFinalParametro = Optional.empty();
		gerenciadorDeData.defineData(dataInicialParametro, dataFinalParametro);

		gerenciadorDeNegociacao.filtraNegociacoes(negociacoes, gerenciadorDeData);

		gerenciadorDeNegociacao.setQuantidade(gerenciadorDeNegociacao.getNegociacoes().size());
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		
		String json = ow.writeValueAsString(gerenciadorDeNegociacao);
		
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/negociacoes", String.class)).
		isEqualTo(json);
	}

	@Test
	public void deveRetornarNegociacaoEspecifica() throws Exception {
		gerenciadorDeNegociacao.inicia();

		List<Negociacao> negociacoes = this.clienteWebService.getNegociacoes();
		
		dataInicialParametro = Optional.empty();
		dataFinalParametro = Optional.empty();
		gerenciadorDeData.defineData(dataInicialParametro, dataFinalParametro);

		gerenciadorDeNegociacao.filtraNegociacoes(negociacoes, gerenciadorDeData);

		gerenciadorDeNegociacao.setQuantidade(gerenciadorDeNegociacao.getNegociacoes().size());
		
		GerenciadorDeNegociacao unicaNegociacao = new GerenciadorDeNegociacao();
		ArrayList<Negociacao> unicaNegociacaoList = new ArrayList<Negociacao>();
		unicaNegociacaoList.add(gerenciadorDeNegociacao.getNegociacoes().get(0));
		unicaNegociacao.setNegociacoes(unicaNegociacaoList);
		unicaNegociacao.setQuantidade(1);
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		
		String json = ow.writeValueAsString(unicaNegociacao);
		
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/negociacoes/1", String.class))
				.isEqualTo(json	);
	}

	@Test
	public void deveRetornarWhitelabelCasoPaginaNaoExista() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/negociacoess", String.class))
				.contains("No message available");
	}

}
