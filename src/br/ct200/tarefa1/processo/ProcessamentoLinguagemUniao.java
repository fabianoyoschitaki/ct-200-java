package br.ct200.tarefa1.processo;

public class ProcessamentoLinguagemUniao implements ProcessamentoLinguagem {
	public String linguagemInicial;
	public String linguagemFinal;
	public String getLinguagemInicial() {
		return linguagemInicial;
	}
	public void setLinguagemInicial(String linguagemInicial) {
		this.linguagemInicial = linguagemInicial;
	}
	public String getLinguagemFinal() {
		return linguagemFinal;
	}
	public void setLinguagemFinal(String linguagemFinal) {
		this.linguagemFinal = linguagemFinal;
	}
}
