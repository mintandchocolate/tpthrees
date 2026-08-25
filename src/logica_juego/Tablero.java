package logica_juego;

import java.util.ArrayList;
import java.util.List;

public class Tablero {

    public static final int TAMANIO = 4;

    private final Ficha[][] cuadricula;

    public Tablero() {
        cuadricula = new Ficha[TAMANIO][TAMANIO];
    }

    public Ficha getFicha(int fila, int col) {
        return cuadricula[fila][col];
    }

    public void setFicha(int fila, int col, Ficha Ficha) {
        cuadricula[fila][col] = Ficha;
    }

    public boolean estaVacia(int fila, int col) {
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
    
    public void moverFichas(Movimiento mov) {
    	int [] direccionFilas = ordenDeMovimiento(mov.getFila());
    	int [] direccionCol = ordenDeMovimiento(mov.getCol());
    	
    	for (int f : direccionFilas) {
    		for (int c : direccionCol) {
    			Ficha fichaActual = cuadricula[f][c];
    			if (fichaActual == null) {
    				continue;
    			}
    			if (f+mov.getFila()>= TAMANIO || c+mov.getCol() >= TAMANIO) {
    				continue;
    			}
    			Ficha fichaDestino = cuadricula[f+mov.getFila()][c+mov.getCol()];
    			if(fichaDestino==null ) {
    				fichaDestino = fichaActual;
    				cuadricula[f][c]=null;
    			}
    			else if (fichaActual.puedeSumarseCon(fichaDestino)) {
    				cuadricula[f+mov.getFila()][c+mov.getCol()] = new Ficha (fichaDestino.valorSumado(fichaActual));
    			}
    		}
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
    
}
