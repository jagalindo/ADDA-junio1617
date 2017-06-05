package practica.SpinnerPD;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import us.lsi.stream.Stream2;

/**
 * <p> Esta clase implementa el tipo ProblemaMochila. Los objetos correspondientes son problemas generalizados de la mochila. </p>
 * <p> Las propiedades de estos problemas son: </p>
 * <ul>
 * <li> Capacidad
 * <li> Index 
 * <li> Objetos Disponibles (propiedad compartida)
 * </ul> 
 * 
 * 
 * 
 * @author Miguel Toro
 *
 */
public class ProblemaSpinner {
	
	private static List<PiezaSpinner> piezasDisponibles;
	private static Comparator<PiezaSpinner> ordenPiezas;
	

	/**
	 * @param fichero Fichero que contiene las propiedades de las piezas disponibles. Un objeto por línea
	 */
	public static void leePiezasDisponibles(String fichero) {
		ordenPiezas = Comparator.reverseOrder();
		piezasDisponibles = Stream2.fromFile(fichero)
				.<PiezaSpinner> map((String s) -> PiezaSpinner.create(s))
				.sorted(ordenPiezas)
				.collect(Collectors.<PiezaSpinner> toList());
	}
	
	public static List<PiezaSpinner> getPiezasDisponibles() {
		return piezasDisponibles;
	}
	
	protected ProblemaSpinner() {
		super();
	}	
	
}
