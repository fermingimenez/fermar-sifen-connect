package py.com.fermar.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import py.com.fermar.constants.AppConstants;

public class DocumentoElectronicoDTO {
	private String cdc;

	private String estado;

	private Timestamp fechaAprobacion;

	private Timestamp fechaEmision;

	private String identificacionReceptor;

	private BigDecimal monto;

	private String numeroDocumento;
	
	private String establecimiento;
	
	private String puntoExpedicion;
	
	private String monedaOperacion;
	
	private BigDecimal montoOperacion;
	
	private Integer tipoDocumentoElectronico;
	
	private String rucEmisor;

	public String getCdc() {
		return cdc;
	}

	public void setCdc(String cdc) {
		this.cdc = cdc;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Timestamp getFechaAprobacion() {
		return fechaAprobacion;
	}

	public void setFechaAprobacion(Timestamp fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}

	public Timestamp getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Timestamp fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getIdentificacionReceptor() {
		return identificacionReceptor;
	}

	public void setIdentificacionReceptor(String identificacionReceptor) {
		this.identificacionReceptor = identificacionReceptor;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getEstadoDescripcion() {		
		return AppConstants.EstadosDTEEnum.getDescripcion(estado);
	}

	public String getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
	}

	public String getPuntoExpedicion() {
		return puntoExpedicion;
	}

	public void setPuntoExpedicion(String puntoExpedicion) {
		this.puntoExpedicion = puntoExpedicion;
	}

	public String getMonedaOperacion() {
		return monedaOperacion;
	}

	public void setMonedaOperacion(String monedaOperacion) {
		this.monedaOperacion = monedaOperacion;
	}

	public BigDecimal getMontoOperacion() {
		return montoOperacion;
	}

	public void setMontoOperacion(BigDecimal montoOperacion) {
		this.montoOperacion = montoOperacion;
	}

	public Integer getTipoDocumentoElectronico() {
		return tipoDocumentoElectronico;
	}

	public void setTipoDocumentoElectronico(Integer tipoDocumentoElectronico) {
		this.tipoDocumentoElectronico = tipoDocumentoElectronico;
	}

	public String getRucEmisor() {
		return rucEmisor;
	}

	public void setRucEmisor(String rucEmisor) {
		this.rucEmisor = rucEmisor;
	}
}
