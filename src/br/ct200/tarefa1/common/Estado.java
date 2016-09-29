package br.ct200.tarefa1.common;

public class Estado {
	private static Integer proximoId = 0;
	public Integer id;
	public TipoEstadoEnum tipo;

	public Estado(TipoEstadoEnum tipo) {
		super();
		this.id = proximoId++;
		this.tipo = tipo;
	}
	public Integer getId() {
		return id;
	}
	public TipoEstadoEnum getTipo() {
		return tipo;
	}
	public void setTipo(TipoEstadoEnum tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public String toString(){
		StringBuffer retorno = new StringBuffer();
		if (TipoEstadoEnum.INICIAL.equals(this.getTipo())){
			retorno.append(">");
		}
		
		if (TipoEstadoEnum.FINAL.equals(this.getTipo())){
			retorno.append("(");
		}
		retorno.append("(").append(this.getId()).append(")");
		if (TipoEstadoEnum.FINAL.equals(this.getTipo())){
			retorno.append(")");
		}
		return retorno.toString();
	}
	
	public static void zeraId(){
		proximoId = 0;
	}
}
