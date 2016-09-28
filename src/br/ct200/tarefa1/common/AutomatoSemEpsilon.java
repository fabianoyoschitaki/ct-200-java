package br.ct200.tarefa1.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutomatoSemEpsilon {
	private String expressaoRegular;

	private Automato automatoOriginal;
	private Automato automatoResultado;
	
	private HashMap<Integer, Estado> mapEstadosPorId;
	private Map<Integer, List<Arco>> mapArcosPorIdEstado;
	
	public AutomatoSemEpsilon(String expressaoRegular) {
		this.expressaoRegular = expressaoRegular;
		this.automatoOriginal = new Automato(expressaoRegular);
		this.automatoResultado = new Automato();
		this.geraAutomatoSemTransicaoEpsilon();
	}

	public Automato getAutomato() {
		return automatoResultado;
	}

	/**
	 * Método principal da Atividade 3, remove as &-transições de um autômato,
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
	private void geraAutomatoSemTransicaoEpsilon() {
		// TODO Auto-generated method stub
	}

	public String getExpressaoRegular() {
		return expressaoRegular;
	}

	/** 
	 * Método que cria novo arco para o autômato 
	 */
	private void criaNovoArco(Estado estadoInicial, Estado estadoFinal,
			String expressao) {
		Arco novoArco = new Arco(estadoInicial, estadoFinal, expressao);
		List<Arco> arcosDoEstadoInicial = mapArcosPorIdEstado.get(estadoInicial.getId());
		if (arcosDoEstadoInicial == null){
			arcosDoEstadoInicial = new ArrayList<Arco>();
		}
		arcosDoEstadoInicial.add(novoArco);
		mapArcosPorIdEstado.put(novoArco.getIdEstadoInicial(), arcosDoEstadoInicial);
	}
	
	/**
	 * Cria novo estado e guarda no mapa
	 * 
	 * @param tipo
	 * @return
	 */
	private Estado criaNovoEstado(TipoEstadoEnum tipo) {
		Estado retorno = new Estado(tipo);
		mapEstadosPorId.put(retorno.getId(), retorno);
		return retorno;
	}
	
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
