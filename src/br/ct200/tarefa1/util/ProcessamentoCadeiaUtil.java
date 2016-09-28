package br.ct200.tarefa1.util;

import java.util.List;

import br.ct200.tarefa1.common.Arco;
import br.ct200.tarefa1.common.Automato;
import br.ct200.tarefa1.common.Estado;
import br.ct200.tarefa1.processo.ProcessamentoCadeia;

public class ProcessamentoCadeiaUtil {

	public static StringBuffer passosProcessamento = new StringBuffer();
	
	/**
	 * Método que verifica se determinada cadeia é aceita pelo autômato.
	 * 
	 * @param cadeiaParaVerificar
	 * @return
	 */
	public static ProcessamentoCadeia processaCadeia(
		Automato automato, 
		String cadeiaParaVerificar) {
		ProcessamentoCadeia retorno = new ProcessamentoCadeia(cadeiaParaVerificar);
		Estado estadoAtual = automato.getEstadoPorId(0);
		processaCadeiaRecursivo(
			retorno, 
			automato,
			cadeiaParaVerificar, 
			estadoAtual);
		return retorno;
	}
	
	private static void processaCadeiaRecursivo(
		ProcessamentoCadeia retorno,
		Automato automato, 
		String cadeia,
		Estado estado) {
		if (cadeia.length() == 0){ // cadeia terminou
			retorno.getEstadosPossiveis().add(estado);
			passosProcessamento.append("Cadeia terminou em estado " + estado).append("\n");
			List<Arco> arcosDoEstado = automato.getArcosPorIdEstado(estado.getId());
			if (arcosDoEstado != null && arcosDoEstado.size() > 0){
				for (Arco arco : arcosDoEstado) {
					if (arco.getExpressao().equals("&")){
						passosProcessamento.append("Mesmo vazia, há arco com &. Verificando próximo estado " + arco).append("\n");
						processaCadeiaRecursivo(retorno, automato, cadeia, arco.getEstadoFinal());
					}
				}
			}
		} else { // ainda tem cadeia para processar
			passosProcessamento.append("Cadeia: " + cadeia).append(" Estado atual: " + estado).append("\n");
			List<Arco> arcosDoEstado = automato.getArcosPorIdEstado(estado.getId());
			if (arcosDoEstado != null && arcosDoEstado.size() > 0){
				for (Arco arco : arcosDoEstado){
					if (arco.getExpressao().equals(cadeia.substring(0,1))){
						passosProcessamento.append("Arco: " + arco).append(" consegue processar: " + cadeia.substring(0,1)).append("\n");
						processaCadeiaRecursivo(retorno, automato, cadeia.substring(1), arco.getEstadoFinal());
					} else if (arco.getExpressao().equals("&")){
						passosProcessamento.append("Arco com &. Verificando próximo estado " + arco).append("\n");
						processaCadeiaRecursivo(retorno, automato, cadeia, arco.getEstadoFinal());
					}
				}
			}
		}
	}
}
