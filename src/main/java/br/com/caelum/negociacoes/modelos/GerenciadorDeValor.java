package br.com.caelum.negociacoes.modelos;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class GerenciadorDeValor {

	@Autowired
	@Qualifier(value = "valores")
	private ArrayList<Long> valores;

	private int quantidade;

	public ArrayList<Long> getValores() {
		return valores;
	}

	public void setValores(ArrayList<Long> valores) {
		this.valores = valores;
	}

	public void inicia() {
		this.valores.clear();

	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	
}
