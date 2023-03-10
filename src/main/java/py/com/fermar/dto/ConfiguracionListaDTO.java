package py.com.fermar.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ConfiguracionListaDTO {
	private String modalidad;
	private BigDecimal timbrado;
	private Date fechaInicioVigencia;
	private String tipoContribuyente;
	private String establecimiento;
	private String listaCSC;
	private List<ActividadEconomicaDTO> listaActividad;
	
	public String getModalidad() {
		return modalidad;
	}
	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}
	public BigDecimal getTimbrado() {
		return timbrado;
	}
	public void setTimbrado(BigDecimal timbrado) {
		this.timbrado = timbrado;
	}
	public Date getFechaInicioVigencia() {
		return fechaInicioVigencia;
	}
	public void setFechaInicioVigencia(Date fechaInicioVigencia) {
		this.fechaInicioVigencia = fechaInicioVigencia;
	}
	public String getEstablecimiento() {
		return establecimiento;
	}
	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
	}
	public String getListaCSC() {
		return listaCSC;
	}
	public void setListaCSC(String listaCSC) {
		this.listaCSC = listaCSC;
	}
	public List<ActividadEconomicaDTO> getListaActividad() {
		return listaActividad;
	}
	public void setListaActividad(List<ActividadEconomicaDTO> listaActividad) {
		this.listaActividad = listaActividad;
	}
	public String getTipoContribuyente() {
		return tipoContribuyente;
	}
	public void setTipoContribuyente(String tipoContribuyente) {
		this.tipoContribuyente = tipoContribuyente;
	}
}
