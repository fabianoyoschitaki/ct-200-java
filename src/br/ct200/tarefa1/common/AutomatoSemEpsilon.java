package br.ct200.tarefa1.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AutomatoSemEpsilon {
	/** autômato com transições epsilon de entrada **/
	private Automato automato;
	
	/** map de id estado x fechos epsilon do estado **/
	private Map<Integer, List<Integer>> mapFechosEpsilonPorEstado;
	
	/** passos para a criação do autômato sem transições epsilon **/
	private List<Passo> passosAutomato = new ArrayList<Passo>();
	private Integer numeroPassoAutomato = 0;
	
	public AutomatoSemEpsilon(String expressaoRegular) {
		this.automato = new Automato(expressaoRegular);
		this.mapFechosEpsilonPorEstado = new HashMap<Integer, List<Integer>>();
		adicionaPasso("Iniciando remoção de transições epsilon a partir da regex : " + expressaoRegular);
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
		adicionaPasso("Início remoção transições epsilon");
		for (Estado estado : automato.getTodosEstados()){
			List<Arco> arcosDoEstado = automato.getArcosPorIdEstado(estado.getId());
			if (arcosDoEstado != null){
				for (Iterator<Arco> iter = arcosDoEstado.iterator(); iter.hasNext();) {
					Arco arco = iter.next();
					if ("&".equals(arco.getExpressao())){
						adicionaPasso("Removendo arco : " + arco);
						iter.remove();
					}
				}
			}
		}
		adicionaPasso("Fim remoção transições epsilon");
	}

	/**
	 * I e IV Calcula os fechos epsilon de todos os estados e 
	 * se X é estado final se algum Y no &-fecho(X) for final
	 */
	private void calculaFechosEpsilon() {
		adicionaPasso("Início computação fechos epsilon");
		for (Estado estado : automato.getTodosEstados()){
			List<Integer> fechosEpsilon = new ArrayList<Integer>();
			adicionaPasso("Computando fechos epsilon para estado : " + estado);
			setFechosEpsilonRecursivo(fechosEpsilon, automato.getArcosPorIdEstado(estado.getId()));
			if (fechosEpsilon != null && !fechosEpsilon.isEmpty()){
				mapFechosEpsilonPorEstado.put(estado.getId(), fechosEpsilon);
				for (Integer fecho : fechosEpsilon) {
					adicionaPasso("Estado " + automato.getEstadoPorId(fecho) + " é fecho epsilon de " + estado);
					if (TipoEstadoEnum.FINAL.equals(automato.getEstadoPorId(fecho).getTipo())){
						estado.setTipo(TipoEstadoEnum.FINAL);
					}
				}
			} else {
				adicionaPasso("Estado " + estado + " não tem fechos epsilon");
			}
		}
		adicionaPasso("Fim computação fechos epsilon");
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
		adicionaPasso("Início passo II - todo arco de A em X gera um arco de A em Y para cada Y no &-fecho(X)");
		List<Arco> retorno = new ArrayList<Arco>();
		Set<Integer> estadosComFechoEpsilon = mapFechosEpsilonPorEstado.keySet();
		for (Integer estadoComFechoEpsilon : estadosComFechoEpsilon){
			for (Estado estado : automato.getTodosEstados()){
				List<Arco> arcosDoEstado = automato.getArcosPorIdEstado(estado.getId());
				if (arcosDoEstado != null && !arcosDoEstado.isEmpty()){
					for (int contArco = 0; contArco < arcosDoEstado.size(); contArco ++) {
						Arco arco = arcosDoEstado.get(contArco);
						if (arco.getEstadoFinal().getId().equals(estadoComFechoEpsilon)){
							adicionaPasso("Estado destino do arco : " + arco + " é fecho epsilon");
							List<Integer> estadosFechoEpsilon = mapFechosEpsilonPorEstado.get(estadoComFechoEpsilon);
							for (Integer estadoFechoEpsilon : estadosFechoEpsilon) {
								Arco novoArco = new Arco(arco.getEstadoInicial(), automato.getEstadoPorId(estadoFechoEpsilon), arco.getExpressao());
								adicionaPasso("Cria novo arco : " + novoArco);
								retorno.add(novoArco);
							}
						}
					}
				}
			}
		}
		adicionaPasso("Fim passo II - todo arco de A em X gera um arco de A em Y para cada Y no &-fecho(X)");
		return retorno;
	}
	
	/**
	 * III 	todo arco de Y em A para qualquer Y no &-fecho(X) gera um arco de X para A
	 */
	private List<Arco> getNovosArcosPassoIII() {
		adicionaPasso("Início passo III - todo arco de Y em A para qualquer Y no &-fecho(X) gera um arco de X para A");
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
		adicionaPasso("Início passo III - todo arco de Y em A para qualquer Y no &-fecho(X) gera um arco de X para A");
		return retorno;
	}
	
	/**
	 * Retorna o histórico de passos para a criação do autômato
	 * 
	 * @return
	 */
	public List<Passo> getPassosAutomato() {
		return passosAutomato;
	}
	
	/**
	 * Cria novo passo para guardar no histórico
	 * 
	 * @param descricao
	 */
	public void adicionaPasso(String descricao){
		this.passosAutomato.add(new Passo(numeroPassoAutomato++, descricao));
	}
}
