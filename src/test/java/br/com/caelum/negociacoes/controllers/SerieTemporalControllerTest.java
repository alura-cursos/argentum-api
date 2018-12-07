package br.com.caelum.negociacoes.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SerieTemporalControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@Test
	public void deveRetornarTodasAsSeriesTemporaisCasoSejaChamadoApenasAUrlSemParametros() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/serie-temporal", String.class))
		.isEqualTo("{\"valores\":[329,355,337,330,286,309,308,347,345,375,337,319,276,281,327,308,303,263,266,258,235,282,349,393,376,383,394,430,391,391,371,394,342,304,273,301,298,318,301,341,303,305,299,323,336,320,342,318,340,306,363,356,403,389,417,422,390,342,287],\"quantidade\":59}");
	}

	@Test
	public void deveRetornarSerieTemporalDeIndicadorFechamentoEMediaMovelPonderada() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/serie-temporal?indicador=IndicadorFechamento&media=MediaMovelPonderada", String.class))
				.isEqualTo("{\"valores\":[349,348,385,320,298],\"quantidade\":5}");
	}
	
	@Test
	public void deveRetornarWhitelabelCasoPaginaNaoExista() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/serie-tmporall", String.class))
		.contains("No message available");
	}
	
	@Test
	public void deveRetornarSerieTemporalDeIndicadorAberturaEMediaMovelPonderada() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/serie-temporal?indicador=IndicadorAbertura&media=MediaMovelPonderada", String.class))
		.isEqualTo("{\"valores\":[344,305,309,269,291,345,349,375,375,396,378,389,339,316,321,299,285,260,292,316,376,401,336,333,326,300,354,402,332,369,363,410,389,350,362,298,255,305,303,373,302,348,391,330,304,296,362,365,368,380,336,309,266,308,356,322,347,302,308],\"quantidade\":59}");
	}
	
	@Test
	public void deveRetornarSerieTemporalDeIndicadorFechamentoEMediaMovelPonderadaDeDeterminadaData() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/serie-temporal?indicador=IndicadorFechamento&de=2018/10/13&ate=2018/10/20&media=MediaMovelPonderada", String.class))
		.isEqualTo("{\"valores\":[349,348,385,320,298],\"quantidade\":5}");
	}
}
