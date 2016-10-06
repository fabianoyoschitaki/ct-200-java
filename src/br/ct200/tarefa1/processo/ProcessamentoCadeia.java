package br.ct200.tarefa1.processo;

import java.util.ArrayList;
import java.util.List;

import br.ct200.tarefa1.common.Estado;
import br.ct200.tarefa1.common.Passo;
import br.ct200.tarefa1.common.TipoEstadoEnum;

public class ProcessamentoCadeia {
	private List<Estado> estadosPossiveis;
	private String cadeia;
	
	/** passos para a validação da cadeia em um autômato **/
	private List<Passo> passosProcessamento = new ArrayList<Passo>();
	private Integer numeroPassoAutomato = 0;
	
	
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
	
	/**
	 * Retorna o histórico de passos para o processamento da cadeia
	 * 
	 * @return
	 */
	public List<Passo> getPassosProcessamento() {
		return passosProcessamento;
	}
	
	/**
	 * Cria novo passo para guardar no histórico
	 * 
	 * @param descricao
	 */
	public void adicionaPasso(String descricao){
		this.passosProcessamento.add(new Passo(numeroPassoAutomato++, descricao));
	}
}
