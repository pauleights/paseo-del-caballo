import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;


import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PaseoDelCaballoD extends JPanel implements ActionListener, MouseListener, MouseMotionListener{

	private enum Estado { VERDADERO, FALSO, ENCERRADO, ENCONTROMEJORTIRO };
	private Estado ejecutoTiro;
	
	private enum Juego { NUEVOJUEGO, JUEGOTERMINO };
	private Juego ejecuta;
	
	
	private static int numeroMovimientoJP;
	private int tamanio;
	private int[][] tableroJP;
	private int[][] cordenadasTableroX, cordenadasTableroY;
	private int[] guardaCordenadasX, guardaCordenadasY;
	private int filaJPanel, columnaJPanel;
	private int filaActualJP = 0;
	private int columnaActualJP = 0;
	
	
	private Timer temporizador;
	
	private GeneralPath caballo;
	private Color colorCaballo;
	
	//variables que almacenana los valores RGB
	private int red, green, blue; 
		
	private static int mouseX, mouseY;
	
	//variables para metodo mouseCLick
	private int mouseClickX, mouseClickY;
	private int pintaCuadroFila, pintaCuadroColumna;
	private boolean seDioClik;
	
	//constructor
	public PaseoDelCaballoD (){
		
		red  = 95;
		green = 158;
		blue = 160;
		
		ejecuta = Juego.JUEGOTERMINO;
		
		tableroJP = new int [8][8];
		tamanio = 50;
		cordenadasTableroX = new int[8][8];
		cordenadasTableroY = new int[8][8];
		temporizador = new Timer(150, this);
		//temporizador.start();
		numeroMovimientoJP = 0;
		
		guardaCordenadasX = new int [64];
		guardaCordenadasY = new int [64];
		
		//metodo inicializa tablero con numeros de accesibilidad
		tableroDeAccesibilidadJP();
		
		//asigna un color al caballo
		colorCaballo = new Color(184, 134, 11);
		
		//CREACION DEL CABALLO CON GeneralPath
		//cordenadas del caballo
		int[] puntosX = { 10, 30, 30, 25, 10, 10, 20, };
		int[] puntosY = { 40, 40, 10, 10, 15, 20, 20, };
		//crea objeto GeneralPath		
		caballo = new GeneralPath();
		//establece la cordenada incial de la ruta general
		caballo.moveTo(puntosX[0], puntosY[0]);
		//crea el caballlo esto no lo dibuja
		for ( int cuenta = 1; cuenta < puntosX.length; cuenta++ )
			caballo.lineTo(puntosX[cuenta],puntosY[cuenta]);
		caballo.closePath();//cierra la figura
		
		
		this.addMouseListener( this );
		this.addMouseMotionListener(this);
	}//fin del constructor
	
	
	//NUEVA IMPLEMENTACION
	@Override
	public void paintComponent( Graphics g ){
		
		super.paintComponent(g);
		
		this.setBackground( Color.WHITE );
		
		Graphics2D g2d = ( Graphics2D ) g;
		
			
		g2d.setColor( Color.BLACK );
		
		//CREACION DEL TABLERO
		for ( int f = 0; f < 8; f++ ){
			for ( int c = 0; c < 8; c++ ){
				
				cordenadasTableroX[f][c] = tamanio * c;//almacena las cordenadas X
				cordenadasTableroY[f][c] = tamanio * f;//almacena las cordenadas Y
				
				if ( f % 2 == 0 ){
					if ( c % 2 == 0 ){
						g2d.setColor( new Color(255, 222, 173) );
						g2d.fillRect(tamanio * c, f * tamanio, tamanio, tamanio);
					}else{
						g2d.setColor( new Color(139, 69, 19));
						g2d.fillRect(tamanio * c, f * tamanio, tamanio, tamanio);
					}
						
				}else{
					if ( c % 2 == 0 ){
						g2d.setColor( new Color(139, 69, 19));
						g2d.fillRect(tamanio * c, f * tamanio, tamanio, tamanio);
					}else{
						g2d.setColor( new Color(255, 222, 173) );
						g2d.fillRect(tamanio * c, f * tamanio, tamanio, tamanio);
					}
						
				}
				
								
				
			}//fin de for c
		}//fin de for f
		
		//dibuja los numeros de tiros realizados
		for ( int numeros = 0; numeros < numeroMovimientoJP ; numeros++ ){
			
			//letra para pintar los numeros en el tablero
			g2d.setFont(  new Font("SansSerif", Font.BOLD, 35) );
			g2d.setColor( new Color (red +20, green+20,  blue+20) );
						
			//pinta los numeros con drawString
			if ( numeros  < 9 )
				g2d.drawString(String.format("%d", numeros+1), guardaCordenadasX[numeros] + 15, guardaCordenadasY[numeros] + 35);
			else
				g2d.drawString(String.format("%d", numeros+1), guardaCordenadasX[numeros] + 7, guardaCordenadasY[numeros] + 35);
			
								
			
		}//fin de for dibuja los tiros realizados
		
		
		if ( ejecuta == Juego.NUEVOJUEGO ){
						
			if ( ejecutoTiro != Estado.ENCERRADO ){
								
				moverCaballoJP();//obtiene el siguiente tiro
				
				//obtiene las coredandas en las que sera dibujado el caballo
				guardaCordenadasX[numeroMovimientoJP] = cordenadasTableroX[filaActualJP][columnaActualJP];//guarda las cordenadas X cada movimiento
				guardaCordenadasY[numeroMovimientoJP] = cordenadasTableroY[filaActualJP][columnaActualJP];//guarda las cordenadas Y cada movimiento
				
				//solo para depurar
				//System.out.printf("numeroMovimientoJP = %d\n", numeroMovimientoJP);
				//System.out.printf("guardaCordenadasX = %d\n", guardaCordenadasX[numeroMovimientoJP]);
				//System.out.printf("guardaCordenadasY = %d\n", guardaCordenadasY[numeroMovimientoJP]);
				
				g2d.setColor( colorCaballo );//establece el color del caballo
				
				//mueve la figura del caballo a las nuevas cordenadas
				g2d.translate(cordenadasTableroX[filaActualJP][columnaActualJP], cordenadasTableroY[filaActualJP][columnaActualJP]);
				//g2d.drawString(String.format("%d", numeroMovimientoJP+1), filaActualJP, columnaActualJP);
				g2d.fill( caballo );//dibuja el caballo  con color de relleno
				
			}
			
			if ( ejecutoTiro != Estado.ENCERRADO )
				numeroMovimientoJP++;
			
			/*NO SE SI USARLO O NO
			if ( ejecutoTiro == Estado.ENCERRADO )
				JOptionPane.showMessageDialog(this, "PASEO INCOMPLETO");
			if( numeroMovimientoJP+1 == 64 ){
				JOptionPane.showMessageDialog(this, "!!PASEO COMPLETO!!");
			}
			*/
			
		}//fin de if ejecuta = Juego.NUEVOJUEGO
		
			
		if ( ejecuta == Juego.JUEGOTERMINO ){
						
			
			g2d.setColor( Color.RED );
			//pinta cuadro en donde se posiciona el mouse 
			g2d.setStroke( new BasicStroke( 3.0f ) );
			g2d.draw( new RoundRectangle2D.Double(tamanio * pintaCuadroColumna + 5, pintaCuadroFila * tamanio + 5, tamanio - 12, tamanio - 12, 20, 20));
			//System.out.printf("pintaCuadroFila = %d\n", pintaCuadroFila);
			//System.out.printf("pintaCuadroColumna = %d\n", pintaCuadroColumna);
			
			
			//caballo que sigue las cordenadas del mouse
			g2d.setColor( colorCaballo );//color del caballo
			g2d.translate(mouseX - 20, mouseY - 20);
			g2d.fill( caballo );//dibuja el caballo  con color de relleno
			
			
		}
		
		
		//TAMANIO DE LOS CUADROS DEL TABLERO = 50;
		
				
	}//fin de metodo paintCompoenent
	
	
	//SOLO PARA DEPURAR
	private void mostrarTableroJP (){
		
		System.out.println("Tablero de ajedrez");
		int contadorF = 0;
		
		System.out.println("      0   1   2   3   4   5   6   7");
		System.out.println("      |   |   |   |   |   |   |   |");
		for(int[] fila : tableroJP ){
			System.out.printf("%d - ", contadorF);
			for(int columna : fila ){
				System.out.printf("%3d ", columna);
			}
			System.out.println();
			contadorF++;
		}//fin de for
		
		System.out.printf("Posicion final [%d][%d]\n", filaActualJP, columnaActualJP);
		
	}//fin de mostrarTableroJP
	
	
	
	private void moverCaballoJP (){
		
		int numeroMovimiento = 0;
				
		int tiroFinal = 0;
		
		int [] horizontal = new int [8];
		int [] vertical = new int [8];
		
		/*
		 * IMPLENTAR ESTOS MOVIMIENTOS POR QUE SI SIRVEN
		 */
		
		horizontal [ 0 ] = 2;
		horizontal [ 1 ] = 1;
		horizontal [ 2 ] = -1;
		horizontal [ 3 ] = -2;
		horizontal [ 4 ] = -2;
		horizontal [ 5 ] = -1;
		horizontal [ 6 ] = 1;
		horizontal [ 7 ] = 2;
		
		vertical [ 0 ] = -1;
		vertical [ 1 ] = -2;
		vertical [ 2 ] = -2;
		vertical [ 3 ] = -1;
		vertical [ 4 ] = 1;
		vertical [ 5 ] = 2;
		vertical [ 6 ] = 2;
		vertical [ 7 ] = 1;
		
		int juegosTerminados = 0;
	
		int temporalC = 0;
		int temporalF = 0;
		
			
		int mejorTiroC1 = 0;
		int mejorTiroF1 = 0;
		int mejorTiroC2 = 0;
		int mejorTiroF2 = 0;
		
		int valor = 0;
				
		int juego = 0;
	
		int numeroDeA1 = 0;
		int numeroDeA2 = 0;
		
		int contadorMejorTiro = 0;
		
		
		ejecutoTiro = Estado.FALSO;
			
		int inicia = 0;
				
		boolean salir = false;
				
		
				
				
		//System.out.printf("\nPosicion Inicial [ %d ][ %d ]\n",filaActualJP, columnaActualJP);
				
				
		ejecutoTiro = Estado.FALSO;
				
				
					
		salir = false;
					
		numeroDeA1 = 0;
		numeroDeA2 = 0;
					
					
		//eliminar numeros de accesibilidad
		/////////////busca numero 2
		for (int f = 0; f < 1 && salir != true; f++ ){
			for (int c = 0; c < tableroJP [f].length && salir != true; c++){
				if ( tableroJP[f][c] == 2 ){
					inicia = 2;
					salir = true;
				}
			}
		}
		for (int f = 7; f < 8 && salir != true; f++ ){
			for (int c = 0; c < tableroJP [f].length && salir != true; c++){
				if ( tableroJP[f][c] == 2 ){
					inicia = 2;
					salir = true;
				}
			}
		}
								
		////////////busca numero 3
		for (int f = 0; f < 2 && salir != true; f++ ){
			for (int c = 0; c < tableroJP [f].length && salir != true; c++){
				if ( tableroJP[f][c] == 3 ){
					inicia = 3;
					salir = true;
				}
			}
		}
		for (int f = 6; f < 8 && salir != true; f++ ){
			for (int c = 0; c < tableroJP [f].length && salir != true; c++){
				if ( tableroJP[f][c] == 3 ){
					inicia = 3;
					salir = true;
				}
			}
		}
					
		//////////busca numero 4
		for (int f = 0; f < tableroJP.length && salir != true; f++ ){
			for (int c = 0; c < tableroJP [f].length && salir != true; c++){
				if ( tableroJP[f][c] == 4 ){
					inicia = 4;
					salir = true;
				}
			}
		}
					
		/////////busca numero 6
		for (int f = 1; f < 7 && salir != true; f++ ){
			for (int c = 0; c < tableroJP [f].length && salir != true; c++){
				if ( tableroJP[f][c] == 6 ){
					inicia = 6;
					salir = true;
				}
			}
		}
					
		////////busca numero 8
		for (int f = 2; f < 6 && salir != true; f++){
			for (int c = 2; c < 6 && salir != true; c++){
				if ( tableroJP[f][c] == 8 ){
					inicia = 8;
					salir = true;
				}
			}
		}
				
						
		ejecutoTiro = Estado.FALSO;
		contadorMejorTiro = 0;
		
		for(int tirosDeProbabilidad = inicia; tirosDeProbabilidad <= 8 && ejecutoTiro != Estado.ENCONTROMEJORTIRO; tirosDeProbabilidad++ ){
			
			if ( numeroDeA1 <= tirosDeProbabilidad ){
				//omitir tiros 5 y 7 
				if ( tirosDeProbabilidad != 5 && tirosDeProbabilidad != 7 ){
					
					numeroMovimiento = 0;
					ejecutoTiro = Estado.FALSO;
					while ( ejecutoTiro == Estado.FALSO  ){
									
																	
						while ( contadorMejorTiro != 2 && ejecutoTiro != Estado.ENCERRADO ){
										
							temporalF = filaActualJP;
							temporalC = columnaActualJP;
							
							temporalF += vertical [ numeroMovimiento ];
							temporalC += horizontal [ numeroMovimiento ];
							
							if ( ( (temporalF >= 0 && temporalF < 8 ) && (temporalC >= 0 && temporalC < 8) ) ){
											
											
								if ( tableroJP [ temporalF ][ temporalC ] == tirosDeProbabilidad ){
									contadorMejorTiro++;
												
									//almacena el PRIMER TIRO
									if ( contadorMejorTiro == 1 ){
													
										numeroDeA1 = tirosDeProbabilidad;
													
										mejorTiroF1 = temporalF;
										mejorTiroC1 = temporalC;
													
													
									}
									//almacena el SEGUNDO TIRO
												
												
									if ( contadorMejorTiro == 2 ){
													
										numeroDeA2 = tirosDeProbabilidad;
													
										mejorTiroF2 = temporalF;
										mejorTiroC2 = temporalC;
													
										ejecutoTiro = Estado.ENCONTROMEJORTIRO;
													
									}
												
								}//fin de IF ultimo filtro
											
							}//fin de IF primer filtro
										
							numeroMovimiento++;
							//cambia a Estado.ENCERRADO por que se agotaron los tiros
							if ( numeroMovimiento > 7 && ejecutoTiro != Estado.ENCONTROMEJORTIRO ){
								ejecutoTiro = Estado.ENCERRADO;
								//tiroFinal = tiro;
							}									
																																						
						}//fin de while ( contadorMejorTiro != 2 )
																											
					}//fin de primer while
							
				}//fin de omitir tiros 
							
				valor =	numeroDeA1; 
						
			}
						
												
			if ( tirosDeProbabilidad > 7 && contadorMejorTiro == 1 ){
				ejecutoTiro = Estado.ENCONTROMEJORTIRO;
				numeroDeA2 = tirosDeProbabilidad ;
			}
													
		}//fin de FOR tiros de probabilidad 	
										
					
		//Tiros de busqueda en caso de EMPATE
		if ( numeroDeA1 == numeroDeA2 && (numeroDeA1 != 0 && numeroDeA2 != 0) ){
						
			ejecutoTiro = Estado.FALSO;
						
			//Ejecuta PRIMER TIRO de prueba
			for( int tirosDeProbabilidad2 = inicia; tirosDeProbabilidad2 <= 8 && ejecutoTiro != Estado.ENCONTROMEJORTIRO; tirosDeProbabilidad2++ ){
							
																		
				if ( tirosDeProbabilidad2 != 5 && tirosDeProbabilidad2 != 7 ){
											
					numeroMovimiento = 0;
					ejecutoTiro = Estado.FALSO;
					while ( ejecutoTiro == Estado.FALSO ){
												
						temporalF = mejorTiroF1;
						temporalC = mejorTiroC1;
									
						temporalF += vertical [ numeroMovimiento ];
						temporalC += horizontal [ numeroMovimiento ];
														
						if ( ( (temporalF >= 0 && temporalF < 8 ) && (temporalC >= 0 && temporalC < 8) ) ){
															
														
							if ( tableroJP [ temporalF ][ temporalC ] == tirosDeProbabilidad2 ){
												
								numeroDeA1 = tirosDeProbabilidad2;
																
								ejecutoTiro = Estado.ENCONTROMEJORTIRO;
														
															
							}//fin de if ultimo filtro 
													
						}//fin de if primer filtro
												
									
						numeroMovimiento++;
						if ( numeroMovimiento > 7 && ejecutoTiro != Estado.ENCONTROMEJORTIRO ){
							//pendiente esto no sirve !!! Por que nunca se va a encerrar!!
							ejecutoTiro = Estado.ENCERRADO;
															
						}
						
					}// fin de WHILE ( ejecutroTiro = Estado.FALSO )
																															
				}//fin de omitir tiros de probabilidad 5 y 7  
																			
			}//fin de FOR PRIMER TIRO de prueba
											
			ejecutoTiro = Estado.FALSO;
			//Ejecuta SEGUNDO TIRO de prueba
			for ( int tirosDeProbabilidad3 = inicia; tirosDeProbabilidad3 <= 8 && ejecutoTiro != Estado.ENCONTROMEJORTIRO;tirosDeProbabilidad3++ ){
							
							
				if ( tirosDeProbabilidad3 != 5 && tirosDeProbabilidad3 != 7 ){
											
					numeroMovimiento = 0;
					ejecutoTiro = Estado.FALSO;
					while ( ejecutoTiro == Estado.FALSO ){
									
																		
						temporalF = mejorTiroF2;
						temporalC = mejorTiroC2;
												
									
						temporalF += vertical [ numeroMovimiento ];
						temporalC += horizontal [ numeroMovimiento ];
														
														
						if ( ( (temporalF >= 0 && temporalF < 8 ) && (temporalC >= 0 && temporalC < 8) ) ){
													
															
							if ( tableroJP [ temporalF ][ temporalC ] == tirosDeProbabilidad3 ){
																
								numeroDeA2 = tirosDeProbabilidad3;
																														
								ejecutoTiro = Estado.ENCONTROMEJORTIRO;
														
							}//fin de if ultimo filtro
													
						}//fin de if primer filtro
												
						numeroMovimiento++;
						if ( numeroMovimiento > 7 && ejecutoTiro != Estado.ENCONTROMEJORTIRO ){
							ejecutoTiro = Estado.ENCERRADO;
													
						}
									
						if ( tirosDeProbabilidad3 > numeroDeA1 ){
							ejecutoTiro = Estado.ENCONTROMEJORTIRO;
										
							numeroDeA2 = tirosDeProbabilidad3;
										
						}
												
												
					}// fin de WHILE ( ejecutroTiro = Estado.FALSO )
																		
				}//fin de omitir tiros de probabilidad 5 y 7
												
			}//fin de FOR SEGUNDO TIRO de prueba
																	
		}//fin de IF ( numeroDeA1 == numeroDeA2 ) empate
					
		//Ejecuta el tiro con el numero de accesibilidad mas bajo
		if ( numeroDeA1 == numeroDeA2 && (numeroDeA1 != 0 && numeroDeA2 != 0) ){
			//Ejecutar primer tiro( con empate )
			if ( tableroJP [ mejorTiroF1 ][ mejorTiroC1 ] == valor ){
				filaActualJP = mejorTiroF1;
				columnaActualJP = mejorTiroC1;
				tableroJP [ filaActualJP ][ columnaActualJP ] = 99;
				ejecutoTiro = Estado.VERDADERO;
			}
		}
					
									
		if ( numeroDeA1 < numeroDeA2 ){
			//Ejecutar primer tiro
			if ( tableroJP [ mejorTiroF1 ][ mejorTiroC1 ] == valor ){
				filaActualJP = mejorTiroF1;
				columnaActualJP = mejorTiroC1;
				tableroJP [ filaActualJP ][ columnaActualJP ] = 99;
				ejecutoTiro = Estado.VERDADERO;
			}
				
		}else if ( numeroDeA2 < numeroDeA1 ){
			//Ejecutar segundo tiro
			if ( tableroJP [ mejorTiroF2 ][ mejorTiroC2 ] == valor  ){
				filaActualJP = mejorTiroF2;
				columnaActualJP = mejorTiroC2;
				tableroJP [ filaActualJP ][ columnaActualJP ] = 99;
				ejecutoTiro = Estado.VERDADERO;
			}
			
		}	
					
		if ( numeroDeA1 == 0 && numeroDeA2 == 0 )
			ejecutoTiro = Estado.ENCERRADO;
									
		//mostrarTableroJP();solo para deupurar	
																						
		tiroFinal = numeroMovimientoJP+1;
		//System.out.println("Tiro : "+tiroFinal);solo para deupurar
					
		//detiene el temporizador cuando se encierra el caballo
		if ( ejecutoTiro == Estado.ENCERRADO || tiroFinal == 64 ){
			ejecuta = Juego.JUEGOTERMINO;
			temporizador.stop();
			
		}
						
					
					
	}//fin de metodo moverCaballoJP
	
		
	private void tableroDeAccesibilidadJP(){
		
		//2 
		tableroJP [ 0 ][ 0 ] = 2;
		tableroJP [ 0 ][ 7 ] = 2;
		tableroJP [ 7 ][ 0 ] = 2;
		tableroJP [ 7 ][ 7 ] = 2;
		
		//3
		tableroJP [ 1 ][ 0 ] = 3;
		tableroJP [ 0 ][ 1 ] = 3;
		
		tableroJP [ 6 ][ 0 ] = 3;
		tableroJP [ 7 ][ 1 ] = 3;
		
		tableroJP [ 0 ][ 6 ] = 3;
		tableroJP [ 1 ][ 7 ] = 3;
		
		tableroJP [ 7 ][ 6 ] = 3;
		tableroJP [ 6 ][ 7 ] = 3;
		
		//4
		tableroJP [ 2 ][ 0 ] = 4;
		tableroJP [ 3 ][ 0 ] = 4;
		tableroJP [ 4 ][ 0 ] = 4;
		tableroJP [ 5 ][ 0 ] = 4;
		
		tableroJP [ 1 ][ 1 ] = 4;
		
		tableroJP [ 2 ][ 7 ] = 4;
		tableroJP [ 3 ][ 7 ] = 4;
		tableroJP [ 4 ][ 7 ] = 4;
		tableroJP [ 5 ][ 7 ] = 4;
		
		tableroJP [ 1 ][ 6 ] = 4;
		
		tableroJP [ 0 ][ 2 ] = 4;
		tableroJP [ 0 ][ 3 ] = 4;
		tableroJP [ 0 ][ 4 ] = 4;
		tableroJP [ 0 ][ 5 ] = 4;
		
		tableroJP [ 6 ][ 1 ] = 4;
		
		tableroJP [ 7 ][ 2 ] = 4;
		tableroJP [ 7 ][ 3 ] = 4;
		tableroJP [ 7 ][ 4 ] = 4;
		tableroJP [ 7 ][ 5 ] = 4;
		
		tableroJP [ 6 ][ 6 ] = 4;
		
		//6
		
		tableroJP [ 2 ][ 1 ] = 6;
		tableroJP [ 3 ][ 1 ] = 6;
		tableroJP [ 4 ][ 1 ] = 6;
		tableroJP [ 5 ][ 1 ] = 6;
		
		tableroJP [ 2 ][ 6 ] = 6;
		tableroJP [ 3 ][ 6 ] = 6;
		tableroJP [ 4 ][ 6 ] = 6;
		tableroJP [ 5 ][ 6 ] = 6;
				
		tableroJP [ 1 ][ 2 ] = 6;
		tableroJP [ 1 ][ 3 ] = 6;
		tableroJP [ 1 ][ 4 ] = 6;
		tableroJP [ 1 ][ 5 ] = 6;
		
		tableroJP [ 6 ][ 2 ] = 6;
		tableroJP [ 6 ][ 3 ] = 6;
		tableroJP [ 6 ][ 4 ] = 6;
		tableroJP [ 6 ][ 5 ] = 6;
		
		//8
		for (int fila = 2; fila <= 5; fila++){
			for (int columna = 2; columna <= 5; columna++){
				tableroJP [ fila ][ columna ] = 8;
				tableroJP [ fila ][ columna ] = 8;
				tableroJP [ fila ][ columna ] = 8;
				tableroJP [ fila ][ columna ] = 8;
				
				
			}
		}
					
	}//fin del metodo tableroDeAccesibilidadJP
	
	public static int obternerNumeroMovimientoJP(){
		return numeroMovimientoJP;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		repaint();
		
		
	}//fin de metodo actionPerformed

	
	//METODOS DE LA INTERFACE MouseListener//////////////////////////////////////////
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
				
		
	}//fin de metodo mouseCliked

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//aqui implementar le anuva posision del caballo
		if (  ejecuta == Juego.JUEGOTERMINO ){
			ejecuta = Juego.NUEVOJUEGO;
			//metodo inicializa tablero con numeros de accesibilidad
			tableroDeAccesibilidadJP();
			numeroMovimientoJP = 0;
		}else{
			JOptionPane.showMessageDialog(this, "El caballo debe de terminar el paseo");
		}
		
		
		
	}///fin del metodo mousePressed

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		filaActualJP = pintaCuadroFila;
		columnaActualJP = pintaCuadroColumna;
		//obtiene las coredandas en las que sera dibujado el caballo
		guardaCordenadasX[numeroMovimientoJP] = cordenadasTableroX[filaActualJP][columnaActualJP];//guarda las cordenadas X cada movimiento
		guardaCordenadasY[numeroMovimientoJP] = cordenadasTableroY[filaActualJP][columnaActualJP];//guarda las cordenadas Y cada movimiento
		
		
		tableroJP [ filaActualJP ][ columnaActualJP ] = 99;//ejecuta el primer tiro
		numeroMovimientoJP++;
		ejecutoTiro = Estado.FALSO;
		temporizador.start();//acciona el temporizador
	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	//METODOS DE LA INTERFACE MouseMotionListener//////////////////////////////////////////
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
			
		if (  ejecuta == Juego.JUEGOTERMINO ){
			
			mouseX = e.getX();
			mouseY = e.getY();
						
			
			boolean encontroCoincidencia = false;
			//implementar busqueda deordenadas
			for ( int f = 0; f < 8 && encontroCoincidencia != true; f++ ){
				for ( int c = 0; c < 8 && encontroCoincidencia != true; c++ ){
					
					for ( int avanzaVertical = 1; avanzaVertical <= 50 && encontroCoincidencia != true; avanzaVertical++){
						for ( int buscaHorizontal = 1; buscaHorizontal <= 50 && encontroCoincidencia != true; buscaHorizontal++ ){
							
							
							if( cordenadasTableroX[f][c] + buscaHorizontal == e.getX() && cordenadasTableroY[f][c] + avanzaVertical == e.getY()){
								mouseX = e.getX();
								mouseY = e.getY();
								pintaCuadroFila = f;
								pintaCuadroColumna = c;
								
								encontroCoincidencia = true;
								//System.out.printf("Se encontraron cordenadas = %b\n", encontroCoincidencia);
								repaint();
							}	
									
						}//fin de for buscaHorizontal
					}//fin de for avanzaVertical
											
				}//fin de for c
			}//fin de for f
			
			
		}//fin de if JUEGOTERMINO
			
			
	}//fin del metodo mouseMoved
	
	public static int obtenerMouseX(){
		return mouseX;
	}
	public static int obtenerMouseY(){
		return mouseY;
	}
	
	
}//fin de clase PaseoDelCaballoD

class PanelSur extends JPanel implements ActionListener{
	
	private Timer temporizador;
	private int tamanioNumero;
	private int contadorMovimientos;
	
	//constructor
	public PanelSur (){
	temporizador = new Timer(150, this);	
	temporizador.start();
	tamanioNumero = 0;
	
	}//fin constructor
	
	
	@Override
	public void paintComponent( Graphics g ){
	
		super.paintComponent(g);
		this.setBackground( new Color ( 250, 235, 215) );
		
		Graphics2D g2d = ( Graphics2D )g;
		
		g.setFont(  new Font("SansSerif", Font.BOLD, 30) );
		g2d.setPaint( new GradientPaint(5,30,  new Color(255, 222, 173), 100, 150,  new Color(139, 69, 19), true ) );
		g.fillRoundRect(10, 10, 210, 200, 50, 50);
		
		
		g.setColor( Color.WHITE );
		g.drawString("Posisciones ", 25, 50);
		g.drawString("alcansadas ", 30, 80);
		
			
		g.setFont(  new Font("SansSerif", Font.BOLD, 80) );
		if ( PaseoDelCaballoD.obternerNumeroMovimientoJP() < 10 )
			g.drawString(String.format("%d", PaseoDelCaballoD.obternerNumeroMovimientoJP()), 90, 170);
		else
			g.drawString(String.format("%d", PaseoDelCaballoD.obternerNumeroMovimientoJP()), 70, 170);
			
			
		g.setFont(  new Font("SansSerif", Font.BOLD, 20) );
		g.setColor( Color.BLACK );
		g.drawString("Paseo del caballo ", 20, 250);
		
		g.setFont(  new Font("SansSerif", Font.PLAIN, 15) );
		g.drawString("Da clik en cualquier lugar ", 20, 270);
		g.drawString("para que el caballo intente", 20, 290);
		g.drawString("completar el paseo por", 20, 310);
		g.drawString("el tablero", 20, 330);
		
		
		
		//SOLO PARA DEPURAR
		//g.drawString("X = "+PaseoDelCaballoD.obtenerMouseX(), 20, 350);
		//g.drawString("Y = "+PaseoDelCaballoD.obtenerMouseY(), 20, 370);
		
		tamanioNumero++;
		
		/*PENDIENTE
		contadorMovimientos++;
		if ( contadorMovimientos > 64 )
			temporizador.stop();
		
		*/
		
		
	}//fin de paintComponenet


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		repaint();
	}
	
}//fin de la clase 
