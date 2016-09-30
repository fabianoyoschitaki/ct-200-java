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
	 * M�todo que cria novo arco para o aut�mato 
	 */
	public void criaNovoArco(Estado estadoInicial, Estado estadoFinal,
			String expressao) {
		criaNovoArco(new Arco(estadoInicial, estadoFinal, expressao));
	}

	public String getExpressaoRegular() {
		return expressaoRegular;
	}
	
	public Collection<Estado> getTodosEstados() {
		return mapEstadosPorId.values();
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
	 * M�todo que processa aut�mato at� que todos
	 * os arcos tenham apenas um s�mbolo
	 */
	private void processaAutomato() {
		Arco arcoParaProcessar = proximoArcoParaProcessar();
		while (arcoParaProcessar != null){
			processaArco(arcoParaProcessar);
			arcoParaProcessar = proximoArcoParaProcessar();
		}
	}

	/**
	 * M�todo que processa arco seguindo os passos:
	 * 
	 * I 	- Uni�o de linguagens a+b
	 * II 	- Concatena��o de linguagens ab, aa, a*b, a(a+b)*
	 * III 	- Fecho de Kleene a*, (ab)*, (a+b)*
	 * IV 	- Express�o entre par�nteses (a+b), ((a+b)*a)
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
	 * M�todo que verifica se existe arco para ser processado, 
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

	public void criaNovoArco(Arco novoArco) {
		List<Arco> arcosDoEstadoInicial = mapArcosPorIdEstado.get(novoArco.getEstadoInicial().getId());
		if (arcosDoEstadoInicial == null){
			arcosDoEstadoInicial = new ArrayList<Arco>();
		}
		arcosDoEstadoInicial.add(novoArco);
		mapArcosPorIdEstado.put(novoArco.getIdEstadoInicial(), arcosDoEstadoInicial);
	}
}
