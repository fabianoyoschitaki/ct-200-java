package br.ct200.tarefa1.common;

import java.util.List;

public class GraphvizParser {
	public static String traduzAutomatoParaGraphviz(Automato automato){
		StringBuffer retorno = new StringBuffer("digraph finite_state_machine {");
		retorno.append("\n	rankdir=LR;");
		retorno.append("\n	size=\"8,5\"");
		retorno.append("\n	node [shape = doublecircle]; ");
		for (Estado estado : automato.getTodosEstados()){
			if (estado.getTipo().equals(TipoEstadoEnum.FINAL)){
				retorno.append(estado.getId()).append(" ");
			}
		}
		retorno.append(";");
		retorno.append("\n	node [shape = circle];");
		for (Estado estado : automato.getTodosEstados()){
			List<Arco> arcosDoEstado = automato.getArcosPorIdEstado(estado.getId());
			if (arcosDoEstado != null){
				for (Arco arco : arcosDoEstado){
					retorno.append("\n	")
					.append(arco.getEstadoInicial().getId())
					.append(" -> ")
					.append(arco.getEstadoFinal().getId())
					.append(" [ label = \"")
					.append(arco.getExpressao())
					.append("\" ];");
				}
			}
		}
		retorno.append("\n}");
		return retorno.toString();
	}
}
