package practica.SpinnerPD;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import us.lsi.pd.AlgoritmoPD.Sp;
import us.lsi.pd.ProblemaPD;

public class ProblemaSpinnerPD implements ProblemaPD<Multiset<PiezaSpinner>, Integer> {
	
	public static List<PiezaSpinner> piezas;

	//Primera pieza a colocar
	private int index;
	private Integer dineroRestante;
	
	private Double spinAcumulado;
	private Double spinSolucion = Double.MIN_VALUE;
	private static int MaxNumCC,MaxNumRS,MaxNumRC;
	private int contadorCC,contadorRS,contadorRC;
	
	public static ProblemaSpinnerPD create(String fichero, Integer c) {	
		ProblemaSpinner.leePiezasDisponibles(fichero);
		ProblemaSpinnerPD.piezas = ProblemaSpinner.getPiezasDisponibles();
		
		for(PiezaSpinner p: ProblemaSpinnerPD.piezas){
			if(p.getTipo()==0){
				ProblemaSpinnerPD.MaxNumCC=p.getNumMaxDeUnidades();
			}else if(p.getTipo()==1){
				ProblemaSpinnerPD.MaxNumRS=p.getNumMaxDeUnidades();
			}else if(p.getTipo()==2){
				ProblemaSpinnerPD.MaxNumRC=p.getNumMaxDeUnidades();
			}
		}
		
		return new ProblemaSpinnerPD(0, c, 0.,0,0,0);
	}
	
	public static ProblemaSpinnerPD create(int index,int dineroRestante,double spinAcumulado,int contadorCC,int contadorRS,int contadorRC) {
		ProblemaSpinnerPD p = new ProblemaSpinnerPD(index, dineroRestante,spinAcumulado,  contadorCC, contadorRS, contadorRC);
		//System.out.println(p);
		
		return p;
	}
	
	private ProblemaSpinnerPD(int index, int dineroRestante, double spinAcumulado,int contadorCC,int contadorRS,int contadorRC) {
		//inicializamos el número de piezas
		this.contadorCC=contadorCC;
		this.contadorRC=contadorRC;
		this.contadorRS=contadorRS;
		this.index = index;
		this.dineroRestante = dineroRestante;
		this.spinAcumulado = spinAcumulado;
	}	
	
	private Boolean constraints(Integer x) {
		boolean res=x*piezas.get(index).getPrecio() <= dineroRestante;
		res=res&& verificaCompra(piezas.get(index).getTipo(),x);
		return res;
	}	
	
	private boolean verificaCompra(Integer tipo, Integer a2) {
		boolean res=false;
		if(tipo==0){
			res=contadorCC+a2<=MaxNumCC;
		}else if(tipo==1){
			res=contadorRS+a2<=MaxNumRS;
		}else if(tipo==2){
			res=contadorRC+a2<=MaxNumRC;
		}
		return res;
	}

	@Override
	public Tipo getTipo(){
		return Tipo.Max;
	}
	
	@Override
	public int size() {
		return ProblemaSpinnerPD.piezas.size()-index+1;
	}
	
	@Override
	public List<Integer> getAlternativas() {
		List<Integer> ls = IntStream.rangeClosed(0, piezas.get(this.index).getNumMaxDeUnidades() )
				.filter(x->this.constraints(x))
				.boxed()
				.collect(Collectors.toList());
		Collections.reverse(ls);
		return ls;
	}
	
	@Override
	public boolean esCasoBase() {
		
		return this.dineroRestante == 0 ||  index == ProblemaSpinnerPD.piezas.size()-1 ;
	}
	
	@Override
	public Sp<Integer> getSolucionCasoBase() {
		
		//si no he comprado todas las piezas
		if (ProblemaSpinnerPD.MaxNumCC-contadorCC !=0 || ProblemaSpinnerPD.MaxNumRC-contadorRC!=0 ||ProblemaSpinnerPD.MaxNumRS-contadorRS !=0){
			return null;
		}
		Integer precio = piezas.get(index).getPrecio();//precio actual

		int num = dineroRestante/precio ;// cuantas puedo comprar
		switch(piezas.get(index).getTipo()){
			case 0: num= Math.min(num, ProblemaSpinnerPD.MaxNumCC-this.contadorCC);break;
			case 1: num= Math.min(num, ProblemaSpinnerPD.MaxNumRS-this.contadorRS);break;
			case 2: num= Math.min(num, ProblemaSpinnerPD.MaxNumRC-this.contadorRC);break;
		}
				
		Double val = (double) num*piezas.get(index).getSpin();
		spinSolucion = val;
		return Sp.create(num, val);
	}

	@Override
	public ProblemaPD<Multiset<PiezaSpinner>, Integer> getSubProblema(Integer a, int np) {
		Preconditions.checkArgument(np==0);
		int coste=a*piezas.get(index).getPrecio();
		Integer dineroRestante = this.dineroRestante-coste;
		Double spinAcumulado = this.spinAcumulado+a*piezas.get(index).getSpin();
		
		int contadorCCtmp=contadorCC;
		int contadorRCtmp=contadorRC;
		int contadorRStmp=contadorRS;		
		
		if(piezas.get(index).getTipo() ==0){
			contadorCCtmp+=a;
		}else if(piezas.get(index).getTipo() ==1){
			contadorRStmp+=a;
		}else if(piezas.get(index).getTipo() ==2){
			contadorRCtmp+=a;
		}
			
		
		return ProblemaSpinnerPD.create(index+1,dineroRestante,spinAcumulado,contadorCCtmp,contadorRStmp,contadorRCtmp);
	}

	@Override
	public Sp<Integer> combinaSolucionesParciales(Integer a, List<Sp<Integer>> ls) {
		Sp<Integer> r = ls.get(0);
		Double valor = a*piezas.get(index).getSpin()+r.propiedad;
		return Sp.create(a, valor);
	}
	
	@Override
	public Sp<Integer> seleccionaAlternativa(List<Sp<Integer>> ls) {
		Sp<Integer> r =ls.stream().filter(x -> x.propiedad != null).max(Comparator.naturalOrder()).orElse(null);
		spinSolucion = r!=null ? r.propiedad : Double.MIN_VALUE;
		return r;
	}
	
	@Override
	public int getNumeroSubProblemas(Integer a) {
		return 1;
	}

	@Override
	public Multiset<PiezaSpinner> getSolucionReconstruida(Sp<Integer> sp) {
		Multiset<PiezaSpinner> m = HashMultiset.create();
		m.add(ProblemaSpinnerPD.piezas.get(this.index), sp.alternativa);
		return m;
	}

	@Override
	public Multiset<PiezaSpinner> getSolucionReconstruida(Sp<Integer> sp, List<Multiset<PiezaSpinner>> ls) {
		Multiset<PiezaSpinner> m = ls.get(0);
		m.add(ProblemaSpinnerPD.piezas.get(this.index), sp.alternativa);
	//	System.out.println(m);
		return m;
	}

	
	@Override
	public Double getObjetivoEstimado(Integer a) {
		// Con filtro
	//	return this.spinAcumulado+this.getCotaSuperiorValorEstimado(a);
		// Sin filtro
		 return Double.MAX_VALUE;
		
	}

	@Override
	public Double getObjetivo() {
		return this.spinAcumulado+this.spinSolucion;
	}

	
	/**
	 * @pre a está contenida en getAlternativas()
	 * @param a Una alternativa de this
	 * @return Una cota superior del valor de la solución del problema this si se escoge la alternativa a
	 */
	public Integer getCotaSuperiorValorEstimado(Integer a){
		Double r = 0.;
		Double capacidadRestante = (double) (this.dineroRestante);
		Double nu =(double) a;
		int ind = this.index;
		while(true) {
			r = r+nu*piezas.get(ind).getSpin();
			capacidadRestante = capacidadRestante-nu*piezas.get(ind).getPrecio();
			ind++;
			if(ind >= piezas.size() || capacidadRestante <= 0.) break;
			nu = Math.min(piezas.get(ind).getNumMaxDeUnidades(),
					capacidadRestante/piezas.get(ind).getPrecio());			
		} 
		return (int)Math.ceil(r);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((dineroRestante == null) ? 0 : dineroRestante
						.hashCode());
		result = prime * result + index;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ProblemaSpinnerPD))
			return false;
		ProblemaSpinnerPD other = (ProblemaSpinnerPD) obj;
		if (dineroRestante == null) {
			if (other.dineroRestante != null)
				return false;
		} else if (!dineroRestante.equals(other.dineroRestante))
			return false;
		if (index != other.index)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String str= "["+contadorCC+","+contadorRS+","+contadorRC+"]";
		
		str+="]";
		return "( i:" + index + ", um:"
				+ dineroRestante + " "+str+" "+piezas.get(index)+ ")";
	}

}
