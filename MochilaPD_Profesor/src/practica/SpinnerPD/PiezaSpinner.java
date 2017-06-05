package practica.SpinnerPD;

/**
 * <p> Esta clase implementa el tipo ObjetoMochila.</p>
 * <p> Las propiedades de estos problemas son: </p>
 * <ul>
 * <li> Código
 * <li> Valor
 * <li> Peso
 * <li> Número máximo de unidades
 * </ul> 
 * 
 * 
 * 
 * @author Miguel Toro
 *
 */
public class PiezaSpinner implements Comparable<PiezaSpinner>{
	
	public static PiezaSpinner create(Integer spin,Integer precio, Integer countMax) {
		return new PiezaSpinner(spin, precio, countMax);
	}

	/**
	 * @param s Una línea de un fichero de texto
	 * @return Construye un objeto mochila a partir de una línea de un fichero
	 */
	public static PiezaSpinner create(String s) {
		return new PiezaSpinner(s);
	}
	
	
	private Integer tipo;
	private Integer spin;
	private Integer precio;
	
	
	PiezaSpinner(Integer tipo,Integer valor, Integer peso){
		this.tipo = tipo;
		this.spin = valor;
		this.precio = peso;
	
	}	
	PiezaSpinner(String s){		
		String[] v = s.split("[ ,]");
		Integer ne = v.length;
		if(ne != 3) throw new IllegalArgumentException("Formato no adecuado en línea  "+s);	
		spin = new Integer(v[1]);
		precio = new Integer(v[2]);
		tipo = new Integer(v[0]);
		
	}	
	public Integer getPrecio() {
		return precio;
	}
	public Integer getSpin() {
		return spin;
	}
	public Integer getTipo() {
		return tipo;
	}
	public Integer getNumMaxDeUnidades() {
		Integer res=null;
		if(tipo==0){
			res= 1;
		}else if(tipo==1){
			res=  3;
		}else if(tipo==2){
			res=  1;
		}
		return res;
	}
	
	@Override
	public int compareTo(PiezaSpinner o) {
		int r = getRatioValorPeso().compareTo(o.getRatioValorPeso());
		if(r == 0){
			r = getTipo().compareTo(o.getTipo());
		}
		return r;
	}	
	public Double getRatioValorPeso() {
		return ((double)spin)/precio;
	}
	
	@Override
	public String toString() {
		return "<tipo:"+tipo+",spin:"+spin+",pvp:"+precio+",maxNumUnits"+this.getNumMaxDeUnidades()+">";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PiezaSpinner))
			return false;
		PiezaSpinner other = (PiezaSpinner) obj;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		return true;
	}
	
	
	
}
