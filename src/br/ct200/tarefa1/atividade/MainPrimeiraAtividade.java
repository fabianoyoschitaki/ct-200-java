package br.ct200.tarefa1.atividade;

import br.ct200.tarefa1.common.Automato;
import br.ct200.tarefa1.common.GraphvizParser;

public class MainPrimeiraAtividade {

	/**
	 * Primeira Atividade contendo 4 expressões regulares do roteiro para teste.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//		String expressaoRegular = "(a+b)*bb(b+a)*";
//		String expressaoRegular = "(a(b+c))*";
//		String expressaoRegular = "a*b+b*a";
		String expressaoRegular = "a*b*c*";
		
		System.out.println("Regex: " + expressaoRegular);
		Automato automato = new Automato(expressaoRegular);
		System.out.println(GraphvizParser.traduzAutomatoParaGraphviz(automato));
	}

}
