package br.ct200.tarefa1.atividade;

import br.ct200.tarefa1.common.Automato;
import br.ct200.tarefa1.common.AutomatoParser;

public class MainPrimeiraAtividade {

	public static void main(String[] args) {
		String expressaoRegular = "(a+b)*bb(b+a)*";
		Automato automato = new Automato(expressaoRegular);
		automato.processaAutomato();
		automato.imprimeAutomatoTeste();
		AutomatoParser.traduzAutomatoParaGraphviz(automato);
	}

}
