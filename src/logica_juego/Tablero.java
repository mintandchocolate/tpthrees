package logica_juego;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import threes.Observer;

public class Tablero {

	// para el metodo del final para testear
	Random randomizador = new Random();
	
    public static final int TAMANIO = 4;

    private final Ficha[][] cuadricula;
    
    private List<Observer> observers = new ArrayList<>();
  
    public Tablero() {
        cuadricula = new Ficha[TAMANIO][TAMANIO];
    }

    public Ficha getFicha(int fila, int col) {
        return cuadricula[fila][col];
    }

    public void setFicha(int fila, int col, Ficha Ficha) {
    	verificarValor(fila);
    	verificarValor(col);
    	
        cuadricula[fila][col] = Ficha;
    }

    public boolean posicionEstaVacia (int fila, int col) {
        return cuadricula[fila][col] == null;
    }

    public List<int[]> casillasVacias() {
        List<int[]> casillas = new ArrayList<>();
        for (int r = 0; r < TAMANIO; r++) {
            for (int c = 0; c < TAMANIO; c++) {
                if (cuadricula[r][c] == null) {
                    casillas.add(new int[]{r, c});
                }
            }
        }
        return casillas;
    }
    //COSAS DEL OBSERVER
    public void addObserver(Observer obs) {
    	observers.add(obs);
    }
    public void notificarCambio() {
    	for(Observer obs : observers) {
    		obs.tableroActualizado(this);
    	}
    }
    
    public void moverFichas(Movimiento mov) {
    	int [] direccionFilas = ordenDeMovimiento(mov.getFila());
    	int [] direccionCol = ordenDeMovimiento(mov.getCol());
    	
    	for (int fila : direccionFilas) {
    		for (int columna : direccionCol) {
    			Ficha fichaActual = cuadricula[fila][columna];
    			if (fichaActual == null) {
    				continue;
    			}
    			
    			int filaDestino = fila+mov.getFila();
    			int colDestino = columna+mov.getCol();
    			if (!dentroDeLimites(filaDestino, colDestino)) {
    				continue;
    			}
    			
    			Ficha fichaDestino = cuadricula[filaDestino][colDestino];
    			if(fichaDestino==null ) {
    				cuadricula[filaDestino][colDestino] = fichaActual;
    				cuadricula[fila][columna]=null;

    			}
    			
    			else if (fichaActual.puedeSumarseCon(fichaDestino)) {
    				cuadricula[filaDestino][colDestino] = new Ficha (fichaDestino.valorSumado(fichaActual));
    				cuadricula[fila][columna] = null;
    			}
    		}
    	}
    	generarFichaNueva(mov);	
    }
    
    /* el metodo ahora chequea si existe algún espacio vacío y si lo hay se 
     * dedica a intentar espacios hasta que encuentre uno
     */
    public void generarFichaNueva(Movimiento mov) {
		int valorFichaNuevaAlBorde = randomizador.nextInt(3) + 1;
		if(hayEspacioParaNuevaFicha(mov)) {
			while (true) {
				int[] posicionCandidata = posicionFichaNueva(mov);
				if (posicionEstaVacia(posicionCandidata[0], posicionCandidata[1])) {
					setFicha(posicionCandidata[0], posicionCandidata[1], new Ficha(valorFichaNuevaAlBorde));
					break;
				} else {
					continue;
				}
			}
		}

    }
    
    public int[] posicionFichaNueva (Movimiento mov) {
    	// removí el -1 de TAMANIO pues el randomizador empieza en 0 y acaba en el final -1 de todas formas
 	   int posicionAzar = randomizador.nextInt(TAMANIO);
 	    switch(mov) {
 	    case ARRIBA : return new int[] {TAMANIO-1, posicionAzar};
 	    case ABAJO : return new int[] {0, posicionAzar};
 	    case IZQUIERDA : return new int[] {posicionAzar,TAMANIO-1};
 	    case DERECHA : return new int[] {posicionAzar,0};
 	    default : throw new IllegalArgumentException("Posición inválida");
 	    }
 			   
 	}
    
    // metodo para saber si debe buscar un lugar, porque lo hay, o simplemente no hacer nada
    public boolean hayEspacioParaNuevaFicha(Movimiento mov) {
    	boolean hayEspacio = false;
    	switch(mov) {
    	
    	case ARRIBA :
    		for (int posicion = 0; posicion < TAMANIO; posicion++) {
    			hayEspacio = hayEspacio || getFicha(TAMANIO - 1, posicion) == null;
    		}
    		return hayEspacio;
    		
    	case ABAJO :
    		for (int posicion = 0; posicion < TAMANIO; posicion++) {
    			hayEspacio = hayEspacio || getFicha(0, posicion) == null;
    		}
    		return hayEspacio;
    		
    	case IZQUIERDA :
    		for (int posicion = 0; posicion < TAMANIO; posicion++) {
    			hayEspacio = hayEspacio || getFicha(posicion, TAMANIO - 1) == null;
    		}
    		return hayEspacio;
    		
    	case DERECHA :
    		for (int posicion = 0; posicion < TAMANIO; posicion++) {
    			hayEspacio = hayEspacio || getFicha(posicion, 0) == null;
    		}
    		return hayEspacio;
    		
    	default : 
    		throw new IllegalArgumentException("Posición inválida");
    		
    	}
    }
    
    private int[] ordenDeMovimiento (int posicion) { //porque no siempre se empiezan a mover de izq a der.
    	int[] ordenAvance = new int [TAMANIO];
    	if (posicion>0) {
    		for (int i = 0; i < TAMANIO; i++) {
    			ordenAvance[i] = TAMANIO - 1 - i;
    		}
    	}
    	else {
    		for (int i = 0; i < TAMANIO; i++) {
    			ordenAvance[i] = i;
    		}
    	}
  	
    	return ordenAvance;
    }
 
    private void verificarValor(int valor) {
    	if(valor>=TAMANIO || valor<0) {
    		throw new IllegalArgumentException("Las coordenadas de fila y/o columna no pueden sobrepasar las dimensiones del tablero");
    	}
    }
    
    private boolean dentroDeLimites(int f, int c) {
    	return f < TAMANIO && f >= 0 && c < TAMANIO && c >=0;
    }
    
/* saqué el -1 de TAMANIO porque de esa forma no lograba generar fichas correctamente
 * generaba las fichas en un patrón de 3x3
 */
    
   public void iniciarTablero() {
	   int contador = 0;
	   
	   while (contador < 9) {
		   int valorFilaAleatorio = randomizador.nextInt(TAMANIO);
		   int valorColumnaAleatorio = randomizador.nextInt(TAMANIO);
		   
		   if (posicionEstaVacia(valorFilaAleatorio, valorColumnaAleatorio)) {
			   int valorInicial = randomizador.nextInt(3) + 1;
			   Ficha fichaNueva = new Ficha(valorInicial);
			   setFicha(valorFilaAleatorio, valorColumnaAleatorio, fichaNueva);
			   contador++;
				   }
			   }
		   }
 

   }

   
