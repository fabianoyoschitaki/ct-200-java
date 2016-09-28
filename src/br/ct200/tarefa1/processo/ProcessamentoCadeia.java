package br.ct200.tarefa1.processo;

import java.util.ArrayList;
import java.util.List;

import br.ct200.tarefa1.common.Estado;
import br.ct200.tarefa1.common.TipoEstadoEnum;

public class ProcessamentoCadeia {
	private List<Estado> estadosPossiveis;
	private String cadeia;
	
	public ProcessamentoCadeia(String cadeia) {
		super();
		this.cadeia = cadeia;
		this.estadosPossiveis = new ArrayList<Estado>();
	}
	public List<Estado> getEstadosPossiveis() {
		return estadosPossiveis;
	}
	public void setEstadosPossiveis(List<Estado> estadosPossiveis) {
		this.estadosPossiveis = estadosPossiveis;
	}
	public String getCadeia() {
		return cadeia;
	}
	public void setCadeia(String cadeia) {
		this.cadeia = cadeia;
	}
	public boolean isCadeiaAceita(){
		for (Estado estado : estadosPossiveis) {
			if (TipoEstadoEnum.FINAL.equals(estado.getTipo())){
				return true;
			}
		}
		return false;
	}
}
