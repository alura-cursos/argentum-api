package br.com.caelum.negociacoes.modelos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public class GerenciadorDeNegociacao{

	
	@Autowired
	@Qualifier(value = "negociacoes")
	private ArrayList<Negociacao> negociacoes;
	
	private Integer quantidade;

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	
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
	
	@Override
    public String toString() {
    	ObjectMapper mapper = new ObjectMapper();
    	
    	String jsonString = "";
		try {
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			jsonString = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
    	return jsonString;
    }

	
	
}
