package py.com.fermar.dto;

public class ActividadEconomicaDTO {
	private String ruc;
	private String actividadEconomica;
	private String nombre;
	private String sigla;
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	public String getActividadEconomica() {
		return actividadEconomica;
	}
	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
}
