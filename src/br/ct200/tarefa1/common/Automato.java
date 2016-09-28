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
	private List<String> alfabeto;
	
	public Automato(String expressaoRegular){
		super();
		this.expressaoRegular = expressaoRegular;
		this.mapArcosPorIdEstado = new LinkedHashMap<Integer, List<Arco>>();
		this.mapEstadosPorId = new LinkedHashMap<Integer, Estado>();
		this.alfabeto = new ArrayList<String>();
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

	public String getExpressaoRegular() {
		return expressaoRegular;
	}
	
	public Collection<Estado> getTodosEstados() {
		return mapEstadosPorId.values();
	}

	public void setExpressaoRegular(String expressaoRegular) {
		this.expressaoRegular = expressaoRegular;
	}

	public List<String> getAlfabeto() {
		return alfabeto;
	}

	public void setAlfabeto(List<String> alfabeto) {
		this.alfabeto = alfabeto;
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
	 * Método que verifica se existe arco para ser processado, 
	 * i.e. expressao do arco tem mais de um char, caso tenha,
	 * retorna-o
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
	
	public List<Arco> getArcosPorIdEstado(Integer idEstado) {
		return mapArcosPorIdEstado.get(idEstado);
	}
	
	public Estado getEstadoPorId(Integer idEstado){
		return mapEstadosPorId.get(idEstado);
	}

	public ProcessamentoCadeia processaCadeia(String cadeiaParaVerificar) {
		return ProcessamentoCadeiaUtil.processaCadeia(this, cadeiaParaVerificar);
	}
	
	public static void main(String[] args) {
//		String expressaoRegular = "(a+b)*bb(b+a)*";
//		String expressaoRegular = "(a(b+c))*";
//		String expressaoRegular = "a*b+b*a";
		String expressaoRegular = "a*b*c*";
		
//		String cadeiaParaVerificar = "ab";
//		String cadeiaParaVerificar = "abb";
//		String cadeiaParaVerificar = "bba";
		String cadeiaParaVerificar = "abba";
		
		Automato automato = new Automato(expressaoRegular);
		ProcessamentoCadeia resultado = automato.processaCadeia(cadeiaParaVerificar);
		System.out.println(AutomatoParser.traduzAutomatoParaGraphviz(automato));
		System.out.println(ProcessamentoCadeiaUtil.passosProcessamento.toString());
		System.out.println("Cadeia:" + resultado.getCadeia());
		for (Estado estado : resultado.getEstadosPossiveis()) {
			System.out.println("Estado possível: " + estado);
		}
		if (resultado.isCadeiaAceita()){
			System.out.println("Cadeia aceita.");
		} else {
			System.out.println("Cadeia não aceita.");
		}
	}
}
