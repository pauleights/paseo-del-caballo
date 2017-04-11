// Escriba una versión del programa del Paseo del caballo que, 
//al encontrarse con un empate entre dos o más posiciones, 
//decida qué posición elegir, más adelante busque aquellas 
//posiciones que se puedan alcanzar desde las posiciones “empatadas”. 
//Su aplicación debe mover el caballo a la posición empatada para la 
//cual el siguiente movimiento lo lleve a una posición con el número 
//de accesibilidad más bajo.
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
		//se necesita el metodo setPreferredSize para el tamañao
		//por que se estan manejando layouts managers en el proyecto
		panelSur.setPreferredSize( new Dimension( 230 , 100 ) );
			
		//La clase Dimension encapsula las medidas
		
		marco.add( paseoDelCaballoD, BorderLayout.CENTER );
		marco.add( panelSur, BorderLayout.EAST );
		
		marco.setSize(648, 440);
		marco.setVisible(true);
		
				
		
	}//fin de main
	
}//fin de clase
