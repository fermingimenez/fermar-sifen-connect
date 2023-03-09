package py.com.fermar.dto;

import java.math.BigDecimal;

public class ProductoDTO {

	private Long id;
	private String codigoInterno;
	private String descripcion;
	private BigDecimal impuestoAfectado;
	private String descripcionImpuestoAfectado;
	private String descripcionTasaImpuesto;
	private BigDecimal precioUnitario;
	private BigDecimal tasaImpuesto;
	private String activo;

	public ProductoDTO() {
	}

	public ProductoDTO(Long id, String codigoInterno, String descripcion, BigDecimal tasaImpuesto,
			BigDecimal impuestoAfectado, BigDecimal precioUnitario, String activo, String descripcionTasaImpuesto,
			String descripcionImpuestoAfectado) {
		super();
		this.id = id;
		this.codigoInterno = codigoInterno;
		this.descripcion = descripcion;
		this.tasaImpuesto = tasaImpuesto;
		this.impuestoAfectado = impuestoAfectado;
		this.precioUnitario = precioUnitario;
		this.activo = activo;
		this.descripcionTasaImpuesto = descripcionTasaImpuesto;
		this.descripcionImpuestoAfectado = descripcionImpuestoAfectado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoInterno() {
		return codigoInterno;
	}

	public void setCodigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getImpuestoAfectado() {
		return impuestoAfectado;
	}

	public void setImpuestoAfectado(BigDecimal impuestoAfectado) {
		this.impuestoAfectado = impuestoAfectado;
	}

	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public BigDecimal getTasaImpuesto() {
		return tasaImpuesto;
	}

	public void setTasaImpuesto(BigDecimal tasaImpuesto) {
		this.tasaImpuesto = tasaImpuesto;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getDescripcionTasaImpuesto() {
		return descripcionTasaImpuesto;
	}

	public void setDescripcionTasaImpuesto(String descripcionTasaImpuesto) {
		this.descripcionTasaImpuesto = descripcionTasaImpuesto;
	}

	public String getDescripcionImpuestoAfectado() {
		return descripcionImpuestoAfectado;
	}

	public void setDescripcionImpuestoAfectado(String descripcionImpuestoAfectado) {
		this.descripcionImpuestoAfectado = descripcionImpuestoAfectado;
	}

}
