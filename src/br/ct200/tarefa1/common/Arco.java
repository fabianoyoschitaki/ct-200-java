package br.ct200.tarefa1.common;

public class Arco {
	private Estado estadoInicial;
	private Estado estadoFinal;
	private String expressao;
	private boolean jaProcessado;
	
	public Arco(Estado estadoInicial, Estado estadoFinal, String expressao) {
		super();
		this.estadoInicial = estadoInicial;
		this.estadoFinal = estadoFinal;
		this.expressao = expressao;
	}
	
	public Integer getIdEstadoInicial(){
		return this.estadoInicial.getId();
	}
	
	public Integer getIdEstadoFinal(){
		return this.estadoFinal.getId();
	}
	
	public Estado getEstadoInicial() {
		return estadoInicial;
	}
	public void setEstadoInicial(Estado estadoInicial) {
		this.estadoInicial = estadoInicial;
	}
	public Estado getEstadoFinal() {
		return estadoFinal;
	}
	public void setEstadoFinal(Estado estadoFinal) {
		this.estadoFinal = estadoFinal;
	}
	public String getExpressao() {
		return expressao;
	}
	public void setExpressao(String expressao) {
		this.expressao = expressao;
	}
	public boolean isJaProcessado() {
		return jaProcessado;
	}
	public void setJaProcessado(boolean jaProcessado) {
		this.jaProcessado = jaProcessado;
	}
	
	@Override
	public String toString(){
		return new StringBuffer(estadoInicial.toString()).append(" -- ").append(expressao).append(" --> ").append(estadoFinal).toString();
	}
}
