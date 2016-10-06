package br.ct200.tarefa1.atividade;

import java.util.List;

import br.ct200.tarefa1.common.Automato;
import br.ct200.tarefa1.common.GraphvizParser;
import br.ct200.tarefa1.common.Passo;

public class MainPrimeiraAtividade {

	/**
	 * Primeira Atividade contendo 4 expressões regulares do roteiro para teste.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String expressaoRegular = "(a+b)*bb(b+a)*";
//		String expressaoRegular = "(a(b+c))*";
//		String expressaoRegular = "a*b+b*a";
//		String expressaoRegular = "a*b*c*";
		
		System.out.println("Regex: " + expressaoRegular);
		Automato automato = new Automato(expressaoRegular);
		List<Passo> passos = automato.getPassosAutomato();
		for (Passo passo : passos) {
			System.out.println(passo);
		}
		System.out.println(GraphvizParser.traduzAutomatoParaGraphviz(automato));
	}

}
