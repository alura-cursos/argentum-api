package br.com.caelum.negociacoes.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.caelum.negociacoes.modelos.GerenciadorDeData;
import br.com.caelum.negociacoes.modelos.GerenciadorDeNegociacao;
import br.com.caelum.negociacoes.modelos.Negociacao;
import br.com.caelum.negociacoes.ws.ClienteWebService;

@RestController
public class NegociacoesController {

	@Autowired
	ClienteWebService clienteWebService;

	@Autowired
	GerenciadorDeData gerenciadorDeData;
	
	@Autowired
	GerenciadorDeNegociacao gerenciadorDeNegociacao;

	@GetMapping(value = "/negociacoes", produces="application/json")
	public GerenciadorDeNegociacao listaNegociacoes(
			@RequestParam(value = "de") @DateTimeFormat(pattern = "yyyy/MM/dd") Optional<Calendar> dataInicialParametro,
			@RequestParam(value = "ate") @DateTimeFormat(pattern = "yyyy/MM/dd") Optional<Calendar> dataFinalParametro) {

		gerenciadorDeNegociacao.inicia();
		
		List<Negociacao> negociacoes = this.clienteWebService.getNegociacoes();

		gerenciadorDeData.defineData(dataInicialParametro, dataFinalParametro);
		
		gerenciadorDeNegociacao.filtraNegociacoes(negociacoes, gerenciadorDeData);

		gerenciadorDeNegociacao.setQuantidade(gerenciadorDeNegociacao.getNegociacoes().size());
		
		System.out.println(negociacoes.size());

		return gerenciadorDeNegociacao;
	}

	@GetMapping(value = "/negociacoes/{id}", produces="application/json")
	public GerenciadorDeNegociacao listaNegociacao(@PathVariable("id") Integer id) {

		List<Negociacao> negociacoes = this.clienteWebService.getNegociacoes();
		gerenciadorDeNegociacao.inicia();
		
		if (ehValido(id, negociacoes)) {
			List<Negociacao> listaRetorno = new ArrayList<Negociacao>();
			listaRetorno.add(negociacoes.get(id - 1));
			gerenciadorDeNegociacao.filtraNegociacoes(listaRetorno, gerenciadorDeData);
			gerenciadorDeNegociacao.setQuantidade(1);
			return gerenciadorDeNegociacao;
		}
		
		return null;
	}

	private boolean ehValido(Integer id, List<Negociacao> listaNegociacoes) {
		return (id > listaNegociacoes.size() || id < 1) ? false : true;
	}

}
