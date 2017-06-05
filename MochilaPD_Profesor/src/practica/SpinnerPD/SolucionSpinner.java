package practica.SpinnerPD;

import java.util.Set;

import com.google.common.collect.*;

/**
 * <p> Esta clase implementa el tipo SolucionMochila. </p>
 * <p> Las propiedades de estos objetos son: </p>
 * <ul>
 * <li> Objetos en la mochila (básica, de tipo Multiset&lt;ObjetoMochila&gt;)
 * <li> Valor (derivada)
 * <li> Peso (derivada)
 * </ul> 
 * 
 * 
 * 
 * @author Miguel Toro
 *
 */
public class SolucionSpinner {
	
	public static SolucionSpinner create() {
		return new SolucionSpinner();
	}
	
	public static SolucionSpinner create(Multiset<PiezaSpinner> m) {
		return new SolucionSpinner(m);
	}

	private Multiset<PiezaSpinner> m;	

	private SolucionSpinner() {
		super();
		this.m = HashMultiset.create();
	}
	
	private SolucionSpinner(Multiset<PiezaSpinner> m) {
		super();
		this.m = m;
	}
		
	public SolucionSpinner add(PiezaSpinner ob, int nu) {
		Multiset<PiezaSpinner> m = getM();
		m.add(ob, nu);	
		return create(m);
	}
	
	public Multiset<PiezaSpinner> getM() {
		return HashMultiset.create(m);
	}
	
	public int count(PiezaSpinner ob){
		return m.count(ob);
	}
	
	public Set<PiezaSpinner> elements(){
		return m.elementSet();
	}
	
	public Integer getValor() {	
		Integer r = 0;
		for(PiezaSpinner e : m){
				r = r+e.getSpin();
		}
		return r;
	}
	
	public Integer getPeso() {
		Integer r = 0;
		for(PiezaSpinner e : m){
			r = r+e.getPrecio();
		}
		return r;
	}
	
	public int compareTo(SolucionSpinner s) {
		int r = getValor().compareTo(s.getValor());
		if(r == 0){
			r = this.toString().compareTo(s.toString());
		}
		return r;
	}
	
	
	public boolean equals(Object arg0) {
		return m.equals(arg0);
	}

	public int hashCode() {
		return m.hashCode();
	}
	
	public String toString() {
		return m.toString()+",spin:"+getValor()+",price:"+getPeso();
	}
	
}
