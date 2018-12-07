package br.com.caelum.negociacoes.indicadores;

import br.com.caelum.negociacoes.modelos.SerieTemporal;

public interface Indicador {

	public abstract double calcula(int posicao, SerieTemporal serie);

}