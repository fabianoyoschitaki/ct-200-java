package br.ct200.tarefa1.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.ct200.tarefa1.processo.ProcessamentoCadeia;
import br.ct200.tarefa1.processo.ProcessamentoLinguagem;
import br.ct200.tarefa1.processo.ProcessamentoLinguagemConcatenacao;
import br.ct200.tarefa1.processo.ProcessamentoLinguagemKleene;
import br.ct200.tarefa1.processo.ProcessamentoLinguagemParentese;
import br.ct200.tarefa1.processo.ProcessamentoLinguagemUniao;
import br.ct200.tarefa1.util.ProcessamentoCadeiaUtil;
import br.ct200.tarefa1.util.ProcessamentoLinguagemUtil;

public class Automato {
	private HashMap<Integer, Estado> mapEstadosPorId;
	private String expressaoRegular;
	private Map<Integer, List<Arco>> mapArcosPorIdEstado;
	
	/**
	 * Construtor que recebe expressão regular
	 * 
	 * @param expressaoRegular
	 */
	public Automato(String expressaoRegular){
		super();
		Estado.zeraId();
		this.expressaoRegular = expressaoRegular;
		this.mapArcosPorIdEstado = new LinkedHashMap<Integer, List<Arco>>();
		this.mapEstadosPorId = new LinkedHashMap<Integer, Estado>();
		criaNovoArco(criaNovoEstado(TipoEstadoEnum.INICIAL), criaNovoEstado(TipoEstadoEnum.FINAL), expressaoRegular);
		this.processaAutomato();
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

	/** 
	 * Método que cria novo arco para o autômato 
	 */
	public void criaNovoArco(Estado estadoInicial, Estado estadoFinal,
			String expressao) {
		criaNovoArco(new Arco(estadoInicial, estadoFinal, expressao));
	}

	public void imprimeAutomatoTeste(){
		System.out.println("\n---- Resultado ----");
		for (Estado estado : mapEstadosPorId.values()){
			List<Arco> arcosDoEstado = mapArcosPorIdEstado.get(estado.getId());
			if (arcosDoEstado != null){
				System.out.println("Arcos do estado (" + estado.getId() + "): ");
				for (Arco arco : arcosDoEstado){
					System.out.println("\t" + arco);
				}
			}
		}
	}
	
	/** 
	 * Método que processa autômato até que todos
	 * os arcos tenham apenas um símbolo
	 */
	private void processaAutomato() {
		Arco arcoParaProcessar = proximoArcoParaProcessar();
		while (arcoParaProcessar != null){
			processaArco(arcoParaProcessar);
			arcoParaProcessar = proximoArcoParaProcessar();
		}
	}

	/**
	 * Método que processa arco seguindo os passos:
	 * 
	 * I 	- União de linguagens a+b
	 * II 	- Concatenação de linguagens ab, aa, a*b, a(a+b)*
	 * III 	- Fecho de Kleene a*, (ab)*, (a+b)*
	 * IV 	- Expressão entre parênteses (a+b), ((a+b)*a)
	 * 
	 * @param arcoParaProcessar
	 */
	private void processaArco(Arco arcoParaProcessar) {
		while (arcoParaProcessar.getExpressao().length() > 1){
			ProcessamentoLinguagem processamentoArco = ProcessamentoLinguagemUtil.getTipoProcessamentoLinguagem(arcoParaProcessar);
			if (processamentoArco instanceof ProcessamentoLinguagemUniao){
				ProcessamentoLinguagemUniao uniao = (ProcessamentoLinguagemUniao) processamentoArco;
				arcoParaProcessar.setExpressao(uniao.getLinguagemInicial());
				criaNovoArco(arcoParaProcessar.getEstadoInicial(), arcoParaProcessar.getEstadoFinal(), uniao.getLinguagemFinal());
			} else if (processamentoArco instanceof ProcessamentoLinguagemConcatenacao){
				ProcessamentoLinguagemConcatenacao concatenacao = (ProcessamentoLinguagemConcatenacao) processamentoArco;
				arcoParaProcessar.setExpressao(concatenacao.getLinguagemInicial());
				Estado estadoFinal = arcoParaProcessar.getEstadoFinal();
				Estado estadoIntermediario = criaNovoEstado(TipoEstadoEnum.COMUM);
				arcoParaProcessar.setEstadoFinal(estadoIntermediario);
				criaNovoArco(estadoIntermediario, estadoFinal, concatenacao.getLinguagemFinal());
			} else if (processamentoArco instanceof ProcessamentoLinguagemKleene){
				ProcessamentoLinguagemKleene kleene = (ProcessamentoLinguagemKleene) processamentoArco;
				Estado estadoFinal = arcoParaProcessar.getEstadoFinal();
				Estado estadoKleene = criaNovoEstado(TipoEstadoEnum.COMUM);
				arcoParaProcessar.setExpressao("&");
				arcoParaProcessar.setEstadoFinal(estadoKleene);
				criaNovoArco(estadoKleene, estadoFinal, "&");
				criaNovoArco(estadoKleene, estadoKleene, kleene.getLinguagem());
			} else if (processamentoArco instanceof ProcessamentoLinguagemParentese){
				ProcessamentoLinguagemParentese parentese = (ProcessamentoLinguagemParentese) processamentoArco;
				arcoParaProcessar.setExpressao(parentese.getLinguagem());
			}
		}
	}

	/**
	 * Retorna próximo arco para ser processado, 
	 * i.e. expressao do arco tem mais de um char 
	 * em sua expressão
	 * 
	 * @return
	 */
	private Arco proximoArcoParaProcessar() {
		for (Estado estado : mapEstadosPorId.values()){
			List<Arco> arcosDoEstado = mapArcosPorIdEstado.get(estado.getId());
			if (arcosDoEstado != null){
				for (Arco arco : arcosDoEstado){
					if (arco.getExpressao().length() > 1){
						return arco;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Verifica se determinada cadeia é aceita pelo autômato
	 * além de retornar os possíveis estados após computação da cadeia
	 * 
	 * @param cadeiaParaVerificar
	 * @return
	 */
	public ProcessamentoCadeia processaCadeia(String cadeiaParaVerificar) {
		return ProcessamentoCadeiaUtil.processaCadeia(this, cadeiaParaVerificar);
	}

	/**
	 * Método que cria novo arco para o autômato
	 * 
	 * @param novoArco
	 */
	public void criaNovoArco(Arco novoArco) {
		List<Arco> arcosDoEstadoInicial = mapArcosPorIdEstado.get(novoArco.getEstadoInicial().getId());
		if (arcosDoEstadoInicial == null){
			arcosDoEstadoInicial = new ArrayList<Arco>();
		}
		arcosDoEstadoInicial.add(novoArco);
		mapArcosPorIdEstado.put(novoArco.getIdEstadoInicial(), arcosDoEstadoInicial);
	}
	
	/**
	 * Retorna lista de arcos em que o idEstado seja o estado do início do arco
	 * 
	 * @param idEstado
	 * @return
	 */
	public List<Arco> getArcosPorIdEstado(Integer idEstado) {
		return mapArcosPorIdEstado.get(idEstado);
	}
	
	/**
	 * Retorna objeto Estado pelo id
	 * 
	 * @param idEstado
	 * @return
	 */
	public Estado getEstadoPorId(Integer idEstado){
		return mapEstadosPorId.get(idEstado);
	}
	
	/**
	 * Retorna expressão regular inicial de entrada
	 * 
	 * @return
	 */
	public String getExpressaoRegular() {
		return expressaoRegular;
	}
	
	/**
	 * Retorna todos os estados criados para o autômato
	 * 
	 * @return
	 */
	public Collection<Estado> getTodosEstados() {
		return mapEstadosPorId.values();
	}

	/**
	 * Método que encontra expressão regular a partir do autômato &-AFN
	 * removendo estado por estado.
	 * 
	 * @return
	 */
	public String encontraExpressaoRegular() {
		StringBuffer retorno = new StringBuffer();
		Estado estadoInicial = getEstadoInicial();
		removeFechoKleeneRecursivo(estadoInicial);
		removeConcatenacaoRecursivo(estadoInicial);
		removeUniaoRecursivo(estadoInicial);
		return retorno.toString();
	}
	
	private void removeUniaoRecursivo(Estado estadoInicial) {
		List<Arco> arcosDoEstadoOrigem = getArcosPorIdEstado(estadoInicial.getId());
		List<Arco> arcosUniaoTemp = new ArrayList<Arco>();
		for (Arco arcoEstadoOrigem : arcosDoEstadoOrigem) {
			if (!arcoEstadoOrigem.getExpressao().equals("&")){
				arcosUniaoTemp.add(arcoEstadoOrigem);
			}
		}
		if (arcosUniaoTemp != null && arcosUniaoTemp.size() > 1){
			Arco primeiroArco = arcosUniaoTemp.get(0);
			System.out.println("Primeiro arco " + primeiroArco);
			for (int contArco = 1; contArco < arcosUniaoTemp.size(); contArco++) {
				Arco proximoArco = arcosUniaoTemp.get(contArco);
				System.out.println("Próximo arco " + proximoArco);
				if (proximoArco.getEstadoFinal().equals(primeiroArco.getEstadoFinal())){
					primeiroArco.setExpressao(primeiroArco.getExpressao() + "+" + proximoArco.getExpressao());
					System.out.println("Arco resultante " + primeiroArco);
					removeArcoDoEstado(estadoInicial.getId(), proximoArco);
					removeUniaoRecursivo(estadoInicial);
				}
			}
		}
	}

	/**
	 * Remove todas as concatenações recursivametne
	 * 
	 * @param estadoInicial
	 */
	private void removeConcatenacaoRecursivo(Estado estadoInicial) {
		List<Arco> arcosDoEstadoOrigem = getArcosPorIdEstado(estadoInicial.getId());
		if (arcosDoEstadoOrigem != null && !arcosDoEstadoOrigem.isEmpty()){
			for (int contArcoEstadoOrigem = 0; contArcoEstadoOrigem < arcosDoEstadoOrigem.size(); contArcoEstadoOrigem++) {
				Arco arcoEstadoOrigem = arcosDoEstadoOrigem.get(contArcoEstadoOrigem);
				List<Arco> arcosDoEstadoDestino = getArcosPorIdEstado(arcoEstadoOrigem.getIdEstadoFinal());
				if (arcosDoEstadoDestino != null && !arcosDoEstadoDestino.isEmpty()){
					for (Arco arcoEstadoDestino : arcosDoEstadoDestino) {
						if (!arcoEstadoOrigem.getExpressao().equals("&")
						 && !arcoEstadoDestino.getExpressao().equals("&")
						 && !arcoEstadoOrigem.getIdEstadoFinal().equals(arcoEstadoOrigem.getIdEstadoInicial())
						 && !arcoEstadoDestino.getIdEstadoFinal().equals(arcoEstadoDestino.getIdEstadoInicial())){
							if (getArcosPorIdEstado(arcoEstadoDestino.getIdEstadoInicial()).size() > 1){
								removeUniaoRecursivo(arcoEstadoDestino.getEstadoInicial());
							}
							if (getArcosPorIdEstado(arcoEstadoOrigem.getIdEstadoInicial()).size() > 1){
								removeUniaoRecursivo(arcoEstadoOrigem.getEstadoInicial());
							}
							System.out.println("Concatenacao " + arcoEstadoOrigem + " com " + arcoEstadoDestino);
							arcoEstadoOrigem.setEstadoFinal(arcoEstadoDestino.getEstadoFinal());
							String primeiraParte = arcoEstadoOrigem.getExpressao();
							if (primeiraParte.contains("+") && !primeiraParte.startsWith("(")){
								primeiraParte = "(" + primeiraParte + ")";
							}
							String segundaParte = arcoEstadoDestino.getExpressao();
							if (segundaParte.contains("+") && !segundaParte.startsWith("(")){
								segundaParte = "(" + segundaParte + ")";
							}
							arcoEstadoOrigem.setExpressao(primeiraParte + segundaParte);
							removeEstadoEArcos(arcoEstadoDestino.getIdEstadoInicial());
							removeConcatenacaoRecursivo(arcoEstadoOrigem.getEstadoInicial());
						}
					} 
				}
			}
		}
	}

	/**
	 * Remove todos os fechos de kleene recursivametne
	 * 
	 * @param estadoInicial
	 */
	private void removeFechoKleeneRecursivo(Estado estadoInicial) {
		List<Arco> arcosDoEstadoOrigem = getArcosPorIdEstado(estadoInicial.getId());
		if (arcosDoEstadoOrigem != null && !arcosDoEstadoOrigem.isEmpty()){
			for (Arco arcoEstadoOrigem : arcosDoEstadoOrigem) {
				List<Arco> arcosDoEstadoDestino = getArcosPorIdEstado(arcoEstadoOrigem.getIdEstadoFinal());
				if (arcosDoEstadoDestino != null && !arcosDoEstadoDestino.isEmpty()){
					for (int contArcoDestino = 0; contArcoDestino < arcosDoEstadoDestino.size(); contArcoDestino++) {
						Arco arcoEstadoDestino = arcosDoEstadoDestino.get(contArcoDestino);
						// kleene
						if (arcoEstadoOrigem.getExpressao().equals("&")
						 && arcoEstadoDestino.getExpressao().equals("&")){
							removeConcatenacaoRecursivo(arcoEstadoOrigem.getEstadoFinal());
							removeUniaoRecursivo(arcoEstadoOrigem.getEstadoFinal());
							List<Arco> arcosDoEstadoKleeneSemEpsilon = getArcosSemEpsilonPorIdEstado(arcoEstadoOrigem.getIdEstadoFinal());
							System.out.println("arcosKleeneTemp.size:" + arcosDoEstadoKleeneSemEpsilon.size());
							System.out.println("Kleene:" + arcoEstadoOrigem + " com " + arcoEstadoDestino);
							arcoEstadoOrigem.setEstadoFinal(arcoEstadoDestino.getEstadoFinal());
							if (arcosDoEstadoKleeneSemEpsilon.get(0).getExpressao().length() > 1){
								arcoEstadoOrigem.setExpressao("(" + arcosDoEstadoKleeneSemEpsilon.get(0).getExpressao() + ")*");
							} else {
								arcoEstadoOrigem.setExpressao(arcosDoEstadoKleeneSemEpsilon.get(0).getExpressao() + "*");
							}
							removeEstadoEArcos(arcoEstadoDestino.getIdEstadoInicial());
						}
						removeFechoKleeneRecursivo(arcoEstadoOrigem.getEstadoFinal());
					}
				}
			}
		}
	}

	/**
	 * Retorna arcos desconsiderando transição epsilon de determinado estado
	 * @param idEstado
	 * @return
	 */
	private List<Arco> getArcosSemEpsilonPorIdEstado(Integer idEstado) {
		List<Arco> retorno = new ArrayList<Arco>();
		for (Arco arcoEstadoKleene : mapArcosPorIdEstado.get(idEstado)) {
			if (!arcoEstadoKleene.getExpressao().equals("&")){
				retorno.add(arcoEstadoKleene);
			}
		}
		return retorno;
	}

	/**
	 * Método que remove o estado e seus arcos
	 * 
	 * @param idEstadoInicial
	 */
	private void removeEstadoEArcos(Integer idEstado) {
		System.out.println("Remover estado:" + idEstado);
		Estado estadoRemovido = mapEstadosPorId.remove(idEstado);
		System.out.println("Removido estado:" + estadoRemovido);
		List<Arco> arcosRemovidos = mapArcosPorIdEstado.remove(idEstado);
		for (Arco arcoRemovido : arcosRemovidos) {
			System.out.println("Removido arco:" + arcoRemovido);
		}
	}
	
	/**
	 * Método que remove o estado e seus arcos
	 * 
	 * @param idEstadoInicial
	 */
	private void removeArcoDoEstado(Integer idEstado, Arco arco) {
		System.out.println("Removendo arco:" + arco);
		mapArcosPorIdEstado.get(idEstado).remove(arco);
	}

	/**
	 * Retorna estado inicial do autômato
	 * 
	 * @return
	 */
	private Estado getEstadoInicial() {
		Estado retorno = null;
		for (Estado estado : getTodosEstados()){
			if (TipoEstadoEnum.INICIAL.equals(estado.getTipo())){
				retorno = estado;
			}
		}
		return retorno;
	}
}
