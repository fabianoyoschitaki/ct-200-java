package br.ct200.tarefa1.atividade;

import br.ct200.tarefa1.common.Automato;
import br.ct200.tarefa1.common.AutomatoParser;
import br.ct200.tarefa1.common.Estado;
import br.ct200.tarefa1.processo.ProcessamentoCadeia;
import br.ct200.tarefa1.util.ProcessamentoCadeiaUtil;

public class MainSegundaAtividade {

	/**
	 * Segunda Atividade, recebe uma express�o regular e uma cadeia de entrada e 
	 * retorna os poss�veis estados ap�s a computa��o da cadeia e se o estado final 1 
	 * pertence a esse conjunto de estados (cadeia aceita ou n�o pelo aut�mato)0 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String expressaoRegular = "(a+b)*bb(b+a)*";
		String cadeiaParaVerificar = "abb";
		
		Automato automato = new Automato(expressaoRegular);
		System.out.println(AutomatoParser.traduzAutomatoParaGraphviz(automato));
		ProcessamentoCadeia resultado = automato.processaCadeia(cadeiaParaVerificar);
		System.out.println("Cadeia:" + resultado.getCadeia());
		for (Estado estado : resultado.getEstadosPossiveis()) {
			System.out.println("Estado poss�vel: " + estado.getId());
		}
		if (resultado.isCadeiaAceita()){
			System.out.println("Cadeia aceita.");
		} else {
			System.out.println("Cadeia n�o aceita.");
		}
	}

}
