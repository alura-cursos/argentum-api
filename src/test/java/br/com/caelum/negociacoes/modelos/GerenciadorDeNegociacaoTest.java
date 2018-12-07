package br.com.caelum.negociacoes.modelos;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.caelum.negociacoes.ws.ClienteWebService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)

public class GerenciadorDeNegociacaoTest {

	@Autowired
	private GerenciadorDeNegociacao gerenciadorDeNegociacao;
	
	@Autowired
	private GerenciadorDeData gerenciadorDeData;
	
	@Autowired
	private ClienteWebService clienteWebService;


	@Test
	public void deveRetornarListaCompletaDeNegociacoes() {
		gerenciadorDeNegociacao.filtraNegociacoes(clienteWebService.getNegociacoes(), gerenciadorDeData);
		gerenciadorDeNegociacao.setQuantidade(gerenciadorDeNegociacao.getNegociacoes().size());
		Assert.assertEquals(Integer.valueOf(244), Integer.valueOf(gerenciadorDeNegociacao.getQuantidade()));
		
	}
	
	@Test
	public void deveRetornarListaVaziaAposChamarOMetodoIniciaEQuantidadeZerada() {
		gerenciadorDeNegociacao.filtraNegociacoes(clienteWebService.getNegociacoes(), gerenciadorDeData);
		gerenciadorDeNegociacao.setQuantidade(gerenciadorDeNegociacao.getNegociacoes().size());
		gerenciadorDeNegociacao.inicia();
		Assert.assertEquals(Integer.valueOf(0), Integer.valueOf(gerenciadorDeNegociacao.getQuantidade()));
		Assert.assertEquals(Integer.valueOf(0), gerenciadorDeNegociacao.getQuantidade());
	}
	
}
