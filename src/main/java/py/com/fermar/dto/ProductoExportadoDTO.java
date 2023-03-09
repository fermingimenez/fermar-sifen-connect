package py.com.fermar.dto;

import java.math.BigDecimal;

public class ProductoExportadoDTO {

	private String codigoInterno;
	private String descripcion;
	private String impuestoAfectado;
	private BigDecimal precioUnitario;
	private String tasaImpuesto;
	private String activo;

	public ProductoExportadoDTO() {
	}

	public ProductoExportadoDTO(String codigoInterno, String descripcion, String impuestoAfectado,
			BigDecimal precioUnitario, String tasaImpuesto, String activo) {
		super();
		this.codigoInterno = codigoInterno;
		this.descripcion = descripcion;
		this.impuestoAfectado = impuestoAfectado;
		this.precioUnitario = precioUnitario;
		this.tasaImpuesto = tasaImpuesto;
		this.activo = activo;
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

	public String getImpuestoAfectado() {
		return impuestoAfectado;
	}

	public void setImpuestoAfectado(String impuestoAfectado) {
		this.impuestoAfectado = impuestoAfectado;
	}

	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public String getTasaImpuesto() {
		return tasaImpuesto;
	}

	public void setTasaImpuesto(String tasaImpuesto) {
		this.tasaImpuesto = tasaImpuesto;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	@Override
	public String toString() {
		return "ProductoExportadoDTO [codigoInterno=" + codigoInterno + ", descripcion=" + descripcion
				+ ", impuestoAfectado=" + impuestoAfectado + ", precioUnitario=" + precioUnitario + ", tasaImpuesto="
				+ tasaImpuesto + ", activo=" + activo + "]";
	}

}
