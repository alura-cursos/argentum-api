package br.com.caelum.negociacoes.modelos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)

public class GerenciadorDeDataTest {

	@Autowired
	private GerenciadorDeData gerenciadorDeData;


	@Test
	public void deveRetornarDataMuitoAntigaEMuitoFuturistaAoInicializarGerenciador() {
		final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		final String dataInicialFormatada = df.format(gerenciadorDeData.getDataInicial().getTime());
		final String dataFinalFormatada = df.format(gerenciadorDeData.getDataFinal().getTime());
		Assert.assertEquals("01/01/1900", dataInicialFormatada);
		Assert.assertEquals("01/01/3000", dataFinalFormatada);

	}
	
	@Test
	public void deveSobrescreverDataPadraoCasoAlgumaDataSejaInformada() {
		final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = Calendar.getInstance();
		dataInicial.set(1998, 3, 2);
		dataFinal.set(2018, 5, 12);
		gerenciadorDeData.setDataInicial(dataInicial);
		gerenciadorDeData.setDataFinal(dataFinal);
		final String dataInicialFormatada = df.format(gerenciadorDeData.getDataInicial().getTime());
		final String dataFinalFormatada = df.format(gerenciadorDeData.getDataFinal().getTime());
		Assert.assertEquals("02/04/1998", dataInicialFormatada);
		Assert.assertEquals("12/06/2018", dataFinalFormatada);

	}
}
