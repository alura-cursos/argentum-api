package br.com.caelum.negociacoes;

import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.caelum.negociacoes.modelos.Negociacao;

@Configuration
public class AppConfig {
	
	@Bean
    public Calendar dataInicial() {
        return Calendar.getInstance();
    }
	
	@Bean
    public Calendar dataFinal() {
        return Calendar.getInstance();
    }
	
	@Bean
    public ArrayList<Negociacao> negociacoes() {
        return new ArrayList<Negociacao>();
    }
	
	@Bean
    public ArrayList<Long> valores() {
        return new ArrayList<Long>();
    }

}
