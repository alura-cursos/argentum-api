package br.com.caelum.negociacoes.indicadores;

import br.com.caelum.negociacoes.modelos.SerieTemporal;

public class MediaMovelSimples implements Indicador {

	private Indicador outroIndicador;

	public MediaMovelSimples(Indicador outroIndicador) {
		this.outroIndicador = outroIndicador;
	}
	
	public double calcula(int posicao, SerieTemporal serie){
		double soma = 0.0;
		for(int i = posicao; i > (posicao-3); i--){
			soma += outroIndicador.calcula(i, serie);
		}
		return soma/3;
	}
	
	@Override
	public String toString() {
		return "MMS "+outroIndicador;
	}
}
