// Escriba una versi�n del programa del Paseo del caballo que, 
//al encontrarse con un empate entre dos o m�s posiciones, 
//decida qu� posici�n elegir, m�s adelante busque aquellas 
//posiciones que se puedan alcanzar desde las posiciones �empatadas�. 
//Su aplicaci�n debe mover el caballo a la posici�n empatada para la 
//cual el siguiente movimiento lo lleve a una posici�n con el n�mero 
//de accesibilidad m�s bajo.
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PruebaPaseoDelCaballoD {

	public static void main ( String[] args ){
		
		JFrame marco = new JFrame("Paseo del caballo");
		marco.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
			
		PaseoDelCaballoD paseoDelCaballoD = new PaseoDelCaballoD ();
						
		PanelSur panelSur = new PanelSur();
		//se necesita el metodo setPreferredSize para el tama�ao
		//por que se estan manejando layouts managers en el proyecto
		panelSur.setPreferredSize( new Dimension( 230 , 100 ) );
			
		//La clase Dimension encapsula las medidas
		
		marco.add( paseoDelCaballoD, BorderLayout.CENTER );
		marco.add( panelSur, BorderLayout.EAST );
		
		marco.setSize(648, 440);
		marco.setVisible(true);
		
				
		
	}//fin de main
	
}//fin de clase
