package py.com.fermar.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import py.gov.sifen.commons.domain.CodigoSeguridadContribuyente;
import py.gov.sifen.commons.ek.domain.Establecimiento;
import py.gov.sifen.commons.ek.domain.EkParametrosDetalle;

public class ConfiguracionListaDTO {
	private List<EkParametrosDetalle> modalidad;
	private BigDecimal timbrado;
	private Date fechaInicioVigencia;
	private String tipoContribuyente;
	private List<Establecimiento> establecimiento;
	private List<CodigoSeguridadContribuyente> listaCSC;
	private List<ActividadEconomicaDTO> listaActividad;
	
	public List<EkParametrosDetalle> getModalidad() {
		return modalidad;
	}
	public void setModalidad(List<EkParametrosDetalle> modalidad) {
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
	public List<Establecimiento> getEstablecimiento() {
		return establecimiento;
	}
	public void setEstablecimiento(List<Establecimiento> establecimiento) {
		this.establecimiento = establecimiento;
	}
	public List<CodigoSeguridadContribuyente> getListaCSC() {
		return listaCSC;
	}
	public void setListaCSC(List<CodigoSeguridadContribuyente> listaCSC) {
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
