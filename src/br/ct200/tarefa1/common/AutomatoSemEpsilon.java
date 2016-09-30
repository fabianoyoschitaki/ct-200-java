package br.ct200.tarefa1.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AutomatoSemEpsilon {

	private Automato automato;
	private Map<Integer, List<Integer>> mapFechosEpsilonPorEstado;
	
	public AutomatoSemEpsilon(String expressaoRegular) {
		this.automato = new Automato(expressaoRegular);
		this.mapFechosEpsilonPorEstado = new HashMap<Integer, List<Integer>>();
		this.geraAutomatoSemTransicaoEpsilon();
	}

	public Automato getAutomato() {
		return automato;
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
		calculaFechosEpsilon();
		removeTransicoesEpsilon();
		List<Arco> novosArcos = new ArrayList<Arco>();
		novosArcos.addAll(getNovosArcosPassoII());
		novosArcos.addAll(getNovosArcosPassoIII());
		for (Arco novoArco : novosArcos) {
			automato.criaNovoArco(novoArco);
		}
	}

	/**
	 * Remove as transições epsilon do autômato
	 */
	private void removeTransicoesEpsilon() {
		for (Estado estado : automato.getTodosEstados()){
			List<Arco> arcosDoEstado = automato.getArcosPorIdEstado(estado.getId());
			if (arcosDoEstado != null){
				for (Iterator<Arco> iter = arcosDoEstado.iterator(); iter.hasNext();) {
					Arco arco = iter.next();
					if ("&".equals(arco.getExpressao())){
						iter.remove();
					}
				}
			}
		}
	}

	/**
	 * I e IV Calcula os fechos epsilon de todos os estados e 
	 * se X é estado final se algum Y no &-fecho(X) for final
	 */
	private void calculaFechosEpsilon() {
		for (Estado estado : automato.getTodosEstados()){
			List<Integer> fechosEpsilon = new ArrayList<Integer>();
			setFechosEpsilonRecursivo(fechosEpsilon, automato.getArcosPorIdEstado(estado.getId()));
			if (fechosEpsilon != null && !fechosEpsilon.isEmpty()){
				mapFechosEpsilonPorEstado.put(estado.getId(), fechosEpsilon);
//				System.out.println("Fechos-e do estado " + estado.getId());
				for (Integer fecho : fechosEpsilon) {
					if (TipoEstadoEnum.FINAL.equals(automato.getEstadoPorId(fecho).getTipo())){
						estado.setTipo(TipoEstadoEnum.FINAL);
					}
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
					setFechosEpsilonRecursivo(retorno, automato.getArcosPorIdEstado(arco.getIdEstadoFinal()));
				}
			}
		}
	}

	/**
	 * II 	todo arco de A em X gera um arco de A em Y para cada Y no &-fecho(X)
	 */
	private List<Arco> getNovosArcosPassoII() {
		List<Arco> retorno = new ArrayList<Arco>();
		Set<Integer> estadosComFechoEpsilon = mapFechosEpsilonPorEstado.keySet();
		for (Integer estadoComFechoEpsilon : estadosComFechoEpsilon){
			for (Estado estado : automato.getTodosEstados()){
				List<Arco> arcosDoEstado = automato.getArcosPorIdEstado(estado.getId());
				if (arcosDoEstado != null && !arcosDoEstado.isEmpty()){
					for (int contArco = 0; contArco < arcosDoEstado.size(); contArco ++) {
						Arco arco = arcosDoEstado.get(contArco);
						if (arco.getEstadoFinal().getId().equals(estadoComFechoEpsilon)){
//							System.out.println("Arco :" + arco + " tem estado final = " + estadoComFechoEpsilon);
							List<Integer> estadosFechoEpsilon = mapFechosEpsilonPorEstado.get(estadoComFechoEpsilon);
							for (Integer estadoFechoEpsilon : estadosFechoEpsilon) {
//								System.out.println("Estado " + estadoFechoEpsilon + " é fecho-e de " + estadoComFechoEpsilon);
								retorno.add(new Arco(arco.getEstadoInicial(), automato.getEstadoPorId(estadoFechoEpsilon), arco.getExpressao()));
							}
						}
					}
				}
			}
		}
		return retorno;
	}
	
	/**
	 * III 	todo arco de Y em A para qualquer Y no &-fecho(X) gera um arco de X para A
	 */
	private List<Arco> getNovosArcosPassoIII() {
		List<Arco> retorno = new ArrayList<Arco>();
		Set<Integer> estadosComFechoEpsilon = mapFechosEpsilonPorEstado.keySet();
		for (Integer estadoComFechoEpsilon : estadosComFechoEpsilon){
//			System.out.println("\nEstado com fecho epsilon: " + estadoComFechoEpsilon);
			List<Integer> estadosFechoEpsilon = mapFechosEpsilonPorEstado.get(estadoComFechoEpsilon);
			for (Integer estadoFechoEpsilon : estadosFechoEpsilon) {
				List<Arco> arcosDoEstadoFechoEpsilon = automato.getArcosPorIdEstado(estadoFechoEpsilon);
				if (arcosDoEstadoFechoEpsilon != null && !arcosDoEstadoFechoEpsilon.isEmpty()){
//					System.out.println("\nFecho epsilon: " + estadoFechoEpsilon);
					for (Arco arco : arcosDoEstadoFechoEpsilon) {
//						System.out.println(arco);
						retorno.add(new Arco(automato.getEstadoPorId(estadoComFechoEpsilon), arco.getEstadoFinal(), arco.getExpressao()));
					}
				}
			}
		}
		return retorno;
	}
	
	public static void main(String[] args) {
//		String expressaoRegular = "(a+b)*bb(b+a)*";
//		String expressaoRegular = "(a(b+c))*";
//		String expressaoRegular = "a*b+b*a";
		String expressaoRegular = "a*b*c*";
		
		System.out.println("Regex: " + expressaoRegular);
		AutomatoSemEpsilon automatoSemEpsilon = new AutomatoSemEpsilon(expressaoRegular);
		System.out.println(GraphvizParser.traduzAutomatoParaGraphviz(automatoSemEpsilon.getAutomato()));
	}
}
