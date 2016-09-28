package br.ct200.tarefa1.atividade;

import br.ct200.tarefa1.common.AutomatoSemEpsilon;
import br.ct200.tarefa1.common.GraphvizParser;

public class MainTerceiraAtividade {

	/**
	 * Terceira Atividade, remove as &-transições de um autômato,
	 * (podendo resultar em múltiplos estados finais)
	 * 
	 * Algoritmo:
	 * I 	computa o &-fecho de cada estado
	 * II 	todo arco de A em X gera um arco de A em Y para cada Y no &-fecho(X)
	 * III 	todo arco de Y em A para qualquer Y no &-fecho(X) gera um arco de X para A
	 * IV 	X é estado final se algum Y no &-fecho(X) for final
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String expressaoRegular = "(a+b)*bb(b+a)*";
//		String expressaoRegular = "(a(b+c))*";
//		String expressaoRegular = "a*b+b*a";
//		String expressaoRegular = "a*b*c*";
		
		System.out.println("Regex: " + expressaoRegular);
		AutomatoSemEpsilon automatoSemEpsilon = new AutomatoSemEpsilon(expressaoRegular);
		System.out.println(GraphvizParser.traduzAutomatoParaGraphviz(automatoSemEpsilon.getAutomato()));
	}
}
