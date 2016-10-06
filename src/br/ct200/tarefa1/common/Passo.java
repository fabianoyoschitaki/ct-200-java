package br.ct200.tarefa1.common;

import java.io.Serializable;

/**
 * Classe utilizada para guardar o histórico de passos
 * 
 * @author Fabiano
 *
 */
public class Passo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer numero;
	private String descricao;
	
	public Passo(Integer numero, String descricao) {
		super();
		this.numero = numero;
		this.descricao = descricao;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String toString(){
		return this.numero + "\t" + this.descricao;
	}
}
