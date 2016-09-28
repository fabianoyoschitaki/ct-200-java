package br.ct200.tarefa1.atividade;

import br.ct200.tarefa1.common.Automato;
import br.ct200.tarefa1.common.AutomatoParser;

public class MainSegundaAtividade {

	/**
	 * Segunda Atividade, recebe uma expressão regular e uma cadeia de entrada e 
	 * retorna os possíveis estados após a computação da cadeia e se o estado final 1 
	 * pertence a esse conjunto de estados (cadeia aceita ou não pelo autômato)0 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String expressaoRegular = "(a+b)*bb(b+a)*";
		String cadeiaParaVerificar = "abb";
		Automato automato = new Automato(expressaoRegular);
		automato.processaAutomato();
		System.out.println(AutomatoParser.traduzAutomatoParaGraphviz(automato));
		automato.processaCadeia(cadeiaParaVerificar);
	}

}
