package br.ct200.tarefa1.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutomatoSemEpsilon {
	private String expressaoRegular;

	private Automato automatoOriginal;
	private Automato automatoResultado;
	
	private Map<Integer, List<Integer>> mapFechosEpsilonPorEstado;
	
	private HashMap<Integer, Estado> mapEstadosPorId;
	private Map<Integer, List<Arco>> mapArcosPorIdEstado;
	
	public AutomatoSemEpsilon(String expressaoRegular) {
		this.expressaoRegular = expressaoRegular;
		this.automatoOriginal = new Automato(expressaoRegular);
		this.automatoResultado = new Automato();
		this.mapFechosEpsilonPorEstado = new HashMap<Integer, List<Integer>>();
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
		calculaFechosEpsilon(automatoOriginal.getTodosEstados());
	}

	private void calculaFechosEpsilon(Collection<Estado> todosEstados) {
		for (Estado estado : todosEstados){
			List<Integer> fechosEpsilon = new ArrayList<Integer>();
			setFechosEpsilonRecursivo(fechosEpsilon, automatoOriginal.getArcosPorIdEstado(estado.getId()));
			mapFechosEpsilonPorEstado.put(estado.getId(), fechosEpsilon);
			if (!mapFechosEpsilonPorEstado.get(estado.getId()).isEmpty()){
				System.out.println("Fechos-e do estado " + estado.getId());
				for (Integer fecho : fechosEpsilon) {
					System.out.println(fecho);
				}
			}
		}
	}

	/**
	 * Descobre recursivamente os fechos-epsilon de cada estado
	 * 
	 * @param retorno
	 * @param arcos
	 */
	private void setFechosEpsilonRecursivo(List<Integer> retorno, List<Arco> arcos) {
		if (arcos != null && !arcos.isEmpty()){
			for (Arco arco : arcos) {
				if ("&".equals(arco.getExpressao())){
					retorno.add(arco.getIdEstadoFinal());
					setFechosEpsilonRecursivo(retorno, automatoOriginal.getArcosPorIdEstado(arco.getIdEstadoFinal()));
				}
			}
		}
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
		String expressaoRegular = "a*b+b*a";
//		String expressaoRegular = "(a(b+c))*";
//		String expressaoRegular = "a*b+b*a";
//		String expressaoRegular = "a*b*c*";
		
		System.out.println("Regex: " + expressaoRegular);
		AutomatoSemEpsilon automatoSemEpsilon = new AutomatoSemEpsilon(expressaoRegular);
		System.out.println(GraphvizParser.traduzAutomatoParaGraphviz(automatoSemEpsilon.getAutomato()));
		System.out.println(GraphvizParser.traduzAutomatoParaGraphviz(new Automato(expressaoRegular)));
	}
}
