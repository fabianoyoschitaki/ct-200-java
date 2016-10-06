package br.ct200.tarefa1.util;

import java.util.List;

import br.ct200.tarefa1.common.Arco;
import br.ct200.tarefa1.common.Automato;
import br.ct200.tarefa1.common.Estado;
import br.ct200.tarefa1.processo.ProcessamentoCadeia;

public class ProcessamentoCadeiaUtil {

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
		retorno.adicionaPasso("Iniciando processamento da cadeia " + cadeiaParaVerificar);
		retorno.adicionaPasso("Regex do autômato: " + automato.getExpressaoRegular());
		Estado estadoAtual = automato.getEstadoPorId(0);
		processaCadeiaRecursivo(
			retorno, 
			automato,
			cadeiaParaVerificar, 
			estadoAtual);
		retorno.adicionaPasso("Processamentos concluídos : " + cadeiaParaVerificar + (retorno.isCadeiaAceita() ? " é aceita por " + automato.getExpressaoRegular() : " não é aceita por " + automato.getExpressaoRegular()));
		return retorno;
	}
	
	/**
	 * Método recursivo para processamento da cadeia de caracteres 
	 * 
	 * @param retorno
	 * @param automato
	 * @param cadeia
	 * @param estado
	 */
	private static void processaCadeiaRecursivo(
		ProcessamentoCadeia retorno,
		Automato automato, 
		String cadeia,
		Estado estado) {
		if (cadeia.length() == 0){ // cadeia terminou
			retorno.getEstadosPossiveis().add(estado);
			retorno.adicionaPasso("Cadeia terminou. Estado possível para computação da cadeia: " + estado);
			List<Arco> arcosDoEstado = automato.getArcosPorIdEstado(estado.getId());
			if (arcosDoEstado != null && !arcosDoEstado.isEmpty()){
				for (Arco arco : arcosDoEstado) {
					if (arco.getExpressao().equals("&")){
						retorno.adicionaPasso("Cadeia vazia, porém há transição epsilon para continuar : " + arco);
						processaCadeiaRecursivo(retorno, automato, cadeia, arco.getEstadoFinal());
					}
				}
			}
		} else { // ainda tem cadeia para processar
			retorno.adicionaPasso("Cadeia restante : '" + cadeia + "'. Estado atual : " + estado);
			List<Arco> arcosDoEstado = automato.getArcosPorIdEstado(estado.getId());
			if (arcosDoEstado != null && !arcosDoEstado.isEmpty()){
				for (Arco arco : arcosDoEstado){
					if (arco.getExpressao().equals(cadeia.substring(0,1))){
						retorno.adicionaPasso("Arco : " + arco + " consegue processar : '" + cadeia.substring(0,1) + "'");
						processaCadeiaRecursivo(retorno, automato, cadeia.substring(1), arco.getEstadoFinal());
					} else if (arco.getExpressao().equals("&")){
						retorno.adicionaPasso("Arco epsilon para continuar : " + arco);
						processaCadeiaRecursivo(retorno, automato, cadeia, arco.getEstadoFinal());
					} else {
						retorno.adicionaPasso("Não há arco em " + estado + " que processe : '" + cadeia.substring(0,1) + "'");
					}
				}
			} else {
				retorno.adicionaPasso("Estado " + estado + " não tem arcos para processar '" + cadeia.substring(0,1) + "', fim dessa thread.");
			}
		}
	}
}
