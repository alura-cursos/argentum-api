package br.com.caelum.negociacoes.modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Component
public class GerenciadorDeNegociacao implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier(value = "negociacoes")
	@JsonDeserialize
	private ArrayList<Negociacao> negociacoes;
	
	private Integer quantidade;
	
	@JsonDeserialize
	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	@JsonSerialize
	public ArrayList<Negociacao> getNegociacoes() {
		return negociacoes;
	}
	

	public void setNegociacoes(ArrayList<Negociacao> negociacoes) {
		this.negociacoes = negociacoes;
	}

	public void filtraNegociacoes(List<Negociacao> negociacoes, GerenciadorDeData gerenciadorDeData) {
		for (Negociacao negociacao : negociacoes) {
			if ((negociacao.getData().getTime().before(gerenciadorDeData.getDataFinal().getTime())
					&& negociacao.getData().getTime().after(gerenciadorDeData.getDataInicial().getTime()))
					|| ((negociacao.getData().getTime().equals((gerenciadorDeData.getDataInicial().getTime()))
							|| negociacao.getData().getTime().equals(gerenciadorDeData.getDataFinal().getTime())))) {
				this.negociacoes.add(negociacao);
			}
		}
	}

	public void inicia() {
		this.negociacoes.clear();
		this.quantidade = 0;
	}

	
	
}
