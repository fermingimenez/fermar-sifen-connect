package py.com.fermar.dto;

public class ContribuyenteDTO {
	private String numeroDocumento;
	private String razonSocial;
	private String dv;
	private String correoElectronico;
	private String nombreUbicacion;
	private String telefono;
	private String departamento;
	private String localidad;
	private String distrito;
	private String estado;
	private String tipoContribuyente;
	
	public ContribuyenteDTO() {
		super();
	}
	
	public ContribuyenteDTO(Object[] obj) {
		if (obj[0] != null) {
			this.setNumeroDocumento(obj[0].toString());
		}
		if (obj[1] != null) {
			this.setRazonSocial(obj[1].toString());
		}
		if (obj[2] != null) {
			this.setDv(obj[2].toString());
		}
		if (obj[3] != null) {
			this.setCorreoElectronico(obj[3].toString());
		}
		if (obj[4] != null) {
			this.setNombreUbicacion(obj[4].toString());
		}
		if (obj[5] != null) {
			this.setTelefono(obj[5].toString());
		}
		if (obj[6] != null) {
			this.setDepartamento(obj[6].toString());
		}
		if (obj[7] != null) {
			this.setLocalidad(obj[7].toString());
		}
		if (obj[8] != null) {
			this.setDistrito(obj[8].toString());
		}
		if (obj[9] != null) {
			this.setEstado(obj[9].toString());
		}
		if (obj[10] != null) {
			this.setTipoContribuyente(obj[10].toString());
		}
	}
	
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getDv() {
		return dv;
	}
	public void setDv(String dv) {
		this.dv = dv;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public String getNombreUbicacion() {
		return nombreUbicacion;
	}
	public void setNombreUbicacion(String nombreUbicacion) {
		this.nombreUbicacion = nombreUbicacion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public String getDistrito() {
		return distrito;
	}
	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getTipoContribuyente() {
		return tipoContribuyente;
	}
	public void setTipoContribuyente(String tipoContribuyente) {
		this.tipoContribuyente = tipoContribuyente;
	}
	
}
