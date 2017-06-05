package practica.SpinnerPD;

import com.google.common.collect.Multiset;

import us.lsi.algoritmos.Algoritmos;
import us.lsi.pd.AlgoritmoPD;

public class Test {

	public static void main(String[] args) {
		ProblemaSpinnerPD  p = ProblemaSpinnerPD.create("ficheros/piezas.txt", 15);		
		AlgoritmoPD.isRandomize = false;
		System.out.println("Piezas = " + ProblemaSpinnerPD.piezas);
		System.out.println("Problema Inicial = "+p);
		AlgoritmoPD<Multiset<PiezaSpinner>, Integer> a = Algoritmos.createPD(p);
		a.ejecuta();
		a.showAllGraph("ficheros/pruebaMochilaConFiltro.gv","Mochila",p);
		System.out.println("Solucion = "+SolucionSpinner.create(a.getSolucion(p)));
	}

}
