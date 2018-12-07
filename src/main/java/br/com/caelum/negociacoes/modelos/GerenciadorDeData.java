package br.com.caelum.negociacoes.modelos;

import java.util.Calendar;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class GerenciadorDeData {

	@Autowired
	@Qualifier(value = "dataInicial")
	private Calendar dataInicial;

	@Autowired
	@Qualifier(value = "dataFinal")
	private Calendar dataFinal;

	@PostConstruct
	private void init() {
		System.out.println("Passei");
		this.dataInicial.set(1900, 0, 1);
		this.dataFinal.set(3000, 0, 1);
	}

	public Calendar getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Calendar dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Calendar getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Calendar dataFinal) {
		this.dataFinal = dataFinal;
	}

	public void defineData(Optional<Calendar> dataInicialParametro, Optional<Calendar> dataFinalParametro) {

		if (dataInicialParametro.isPresent())
			this.dataInicial.setTime(dataInicialParametro.get().getTime());
		if (dataFinalParametro.isPresent())
			this.dataFinal.setTime(dataFinalParametro.get().getTime());

	}

}
