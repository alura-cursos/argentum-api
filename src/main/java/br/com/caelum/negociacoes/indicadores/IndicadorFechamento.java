package br.com.caelum.negociacoes.indicadores;

import br.com.caelum.negociacoes.modelos.SerieTemporal;

public class IndicadorFechamento implements Indicador{

	public double calcula(int posicao, SerieTemporal serie) {
		return serie.getCandle(posicao).getFechamento();
	}

	@Override
	public String toString() {
		return "Fechamento";
	}
}
