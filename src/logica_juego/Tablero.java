package logica_juego;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import threes.Observer;

public class Tablero {

    private final Random randomizador = new Random();
    public static final int TAMANIO = 4;
    private final Ficha[][] cuadricula;
    private final List<Observer> observers = new ArrayList<>();

    // NUEVO: Variables para balancear la aparicion de fichas 1 y 2
    private int ultimoValorGenerado = -1;
    private int contadorConsecutivos = 0;
    private int valorFichaProxima = -1;

    public Tablero() {
        cuadricula = new Ficha[TAMANIO][TAMANIO];
    }
    
    public Ficha getFicha(int fila, int col) {
        verificarValor(fila);
        verificarValor(col);
        return cuadricula[fila][col];
    }

    public void setFicha(int fila, int col, Ficha ficha) {
        verificarValor(fila);
        verificarValor(col);
        cuadricula[fila][col] = ficha;
    }

    public boolean posicionEstaVacia(int fila, int col) {
        verificarValor(fila);
        verificarValor(col);
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

    // Observer
    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    public void notificarCambio() {
        for (Observer obs : observers) {
            obs.tableroActualizado(this);
        }
    }

    // Movimiento
    public boolean moverFichas(Movimiento mov) {
        boolean huboCambio = false;
        int[] direccionFilas = ordenDeMovimiento(mov.getFila());
        int[] direccionCol = ordenDeMovimiento(mov.getCol());

        for (int fila : direccionFilas) {
            for (int columna : direccionCol) {
                huboCambio|= moverFichaIndividual(fila, columna, mov);       
                }
            }

        if (huboCambio) {
            generarFichaNueva(mov);
            notificarCambio();
        }

        return huboCambio;
    }
    
    public boolean moverFichaIndividual(int fila, int columna, Movimiento mov) {
    	Ficha fichaActual = cuadricula[fila][columna];
    	if (fichaActual == null) {
    		return false;
    		}
    	
        int filaDestino = fila + mov.getFila();
        int colDestino = columna + mov.getCol();
        if (!dentroDeLimites(filaDestino, colDestino)) {
            return false;
        }
        
        Ficha fichaDestino = cuadricula[filaDestino][colDestino];
        if (fichaDestino == null) {
            cuadricula[filaDestino][colDestino] = fichaActual;
            cuadricula[fila][columna] = null;
            return true;
        }
        
        if (fichaActual.puedeSumarseCon(fichaDestino)) {
            cuadricula[filaDestino][colDestino] = new Ficha(fichaDestino.valorSumado(fichaActual));
            cuadricula[fila][columna] = null;
            return true;
        }
        
        return false;
    	
    }

    public void generarFichaNueva(Movimiento mov) {
        if (!hayEspacioParaNuevaFicha(mov)) {
            return;
        }

        int valorFichaNuevaAlBorde = obtenerProximoValor();
        while (true) {
            int[] posicionCandidata = posicionFichaNueva(mov);
            if (posicionEstaVacia(posicionCandidata[0], posicionCandidata[1])) {
                setFicha(posicionCandidata[0], posicionCandidata[1], new Ficha(valorFichaNuevaAlBorde));
                break;
            }
        }
        
        valorFichaProxima = -1;
    }

    public int obtenerProximoValor() {
    	if (valorFichaProxima != -1) {
    		return valorFichaProxima;
    	}
    	
        int nuevoValor = randomizador.nextInt(3) + 1;

        if (nuevoValor == ultimoValorGenerado) {
            contadorConsecutivos++;
        } else {
            ultimoValorGenerado = nuevoValor;
            contadorConsecutivos = 1;
        }

        // NUEVO: Variables para balancear la aparicion de fichas 1 y 2
        if (contadorConsecutivos >= 4) {
            if (nuevoValor == 2) {
                nuevoValor = 1;
            } else if (nuevoValor == 1) {
                nuevoValor = 2;
            } else {
                nuevoValor = randomizador.nextBoolean() ? 1 : 2;
            }
            ultimoValorGenerado = nuevoValor;
            contadorConsecutivos = 1;
        }
        valorFichaProxima = nuevoValor;
        return nuevoValor;
    }

    public int[] posicionFichaNueva(Movimiento mov) {
        int posicionAzar = randomizador.nextInt(TAMANIO);
        switch (mov) {
            case ARRIBA:
                return new int[]{TAMANIO - 1, posicionAzar};
            case ABAJO:
                return new int[]{0, posicionAzar};
            case IZQUIERDA:
                return new int[]{posicionAzar, TAMANIO - 1};
            case DERECHA:
                return new int[]{posicionAzar, 0};
            default:
                throw new IllegalArgumentException("Posicion invalida");
        }
    }

    public boolean hayEspacioParaNuevaFicha(Movimiento mov) {
        boolean hayEspacio = false;
        switch (mov) {
            case ARRIBA:
                for (int pos = 0; pos < TAMANIO; pos++) {
                    hayEspacio = hayEspacio || getFicha(TAMANIO - 1, pos) == null;
                }
                return hayEspacio;
            case ABAJO:
                for (int pos = 0; pos < TAMANIO; pos++) {
                    hayEspacio = hayEspacio || getFicha(0, pos) == null;
                }
                return hayEspacio;
            case IZQUIERDA:
                for (int pos = 0; pos < TAMANIO; pos++) {
                    hayEspacio = hayEspacio || getFicha(pos, TAMANIO - 1) == null;
                }
                return hayEspacio;
            case DERECHA:
                for (int pos = 0; pos < TAMANIO; pos++) {
                    hayEspacio = hayEspacio || getFicha(pos, 0) == null;
                }
                return hayEspacio;
            default:
                throw new IllegalArgumentException("Posicion invalida");
        }
    }

    // Puntaje acumulado
    public int calcularPuntaje() {
        int puntajeTotal = 0;
        for (int fil = 0; fil < TAMANIO; fil++) {
            for (int col = 0; col < TAMANIO; col++) {
                Ficha f = cuadricula[fil][col];
                if (f != null) {
                    puntajeTotal += f.puntaje();
                }
            }
        }
        return puntajeTotal;
    }

    // Deteccion de Game Over
    public boolean hayMovimientosPosibles() {
        // 1. Si hay al menos un casillero vacio, todavia se puede jugar
        if (!casillasVacias().isEmpty()) {
            return true;
        }

        // 2. Si esta lleno, verificar si alguna ficha adyacente puede sumarse
        if (hayAlgunaCombinacionPosible()) {
        	return true;
        }
        return false;
    }

    private boolean hayAlgunaCombinacionPosible() {
    	for (int fil = 0; fil < TAMANIO; fil++) {
            for (int col = 0; col < TAMANIO; col++) {
                Ficha actual = cuadricula[fil][col];
                
                // Chequear vecino derecho
                if (posibleCombinarseConVecino(Movimiento.DERECHA, actual, fil, col)) {
                    return true;
                }
                // Chequear vecino inferior
                if (posibleCombinarseConVecino(Movimiento.ABAJO, actual, fil, col)) {
                    return true;
                }
            }
        }
    	return false;
    }
    
    public boolean posibleCombinarseConVecino(Movimiento direccion, Ficha ficha, int fil, int col) {
    	
    	if (ficha == null) {
    		return false;
    	}
    	
    	switch (direccion) {
    		case ARRIBA:
    			if (dentroDeLimites(fil - 1, col) && ficha.puedeSumarseCon(cuadricula[fil - 1][col])) {
    				return true;
    			} return false;
    			
    		case ABAJO:
    			if (dentroDeLimites(fil + 1, col) && ficha.puedeSumarseCon(cuadricula[fil + 1][col])) {
    				return true;
    			} return false;
    			
    		case IZQUIERDA:
    			if (dentroDeLimites(fil, col - 1) && ficha.puedeSumarseCon(cuadricula[fil][col - 1])) {
    				return true;
    			} return false;
    			
    		case DERECHA:
    			if (dentroDeLimites(fil, col + 1) && ficha.puedeSumarseCon(cuadricula[fil][col + 1])) {
    				return true;
    			} return false;
    			
    		default:
    			throw new IllegalArgumentException("direccion invalida");
    	}
    }
    
    // * EXTRA *
    public int[][] jugadaSugerida() {
    	int[] primerCoordenada = null;
    	int[] segundaCoordenada = null;
    	int[][] parCoordenadas = null;
    	
    	if (hayAlgunaCombinacionPosible()) {
    		while (segundaCoordenada == null) {
    			
    			int filaAleatoria = randomizador.nextInt(4);
    			int columnaAleatoria = randomizador.nextInt(4);
    			primerCoordenada = new int[]{filaAleatoria, columnaAleatoria};
    			
    			Ficha fichaCorrespondiente = getFicha(filaAleatoria, columnaAleatoria);
    			if (posibleCombinarseConVecino(Movimiento.ARRIBA, fichaCorrespondiente, filaAleatoria, columnaAleatoria)) {
    				segundaCoordenada = new int[]{filaAleatoria - 1, columnaAleatoria};
    			} else if (posibleCombinarseConVecino(Movimiento.ABAJO, fichaCorrespondiente, filaAleatoria, columnaAleatoria)) {
    				segundaCoordenada = new int[]{filaAleatoria + 1, columnaAleatoria};
    			} else if (posibleCombinarseConVecino(Movimiento.IZQUIERDA, fichaCorrespondiente, filaAleatoria, columnaAleatoria)) {
    				segundaCoordenada = new int[]{filaAleatoria, columnaAleatoria - 1};
    			} else if (posibleCombinarseConVecino(Movimiento.DERECHA, fichaCorrespondiente, filaAleatoria, columnaAleatoria)) {
    				segundaCoordenada = new int[]{filaAleatoria, columnaAleatoria};
    			}
    		}
    		parCoordenadas = new int[][]{primerCoordenada, segundaCoordenada};
    	}
    	return parCoordenadas;
    }
    
    private int[] ordenDeMovimiento(int posicion) {
        int[] ordenAvance = new int[TAMANIO];
        if (posicion > 0) {
            for (int i = 0; i < TAMANIO; i++) {
                ordenAvance[i] = TAMANIO - 1 - i;
            }
        } else {
            for (int i = 0; i < TAMANIO; i++) {
                ordenAvance[i] = i;
            }
        }
        return ordenAvance;
    }

    private void verificarValor(int valor) {
        if (valor >= TAMANIO || valor < 0) {
            throw new IllegalArgumentException("Las coordenadas no pueden sobrepasar las dimensiones del tablero");
        }
    }

    private boolean dentroDeLimites(int f, int c) {
        return f < TAMANIO && f >= 0 && c < TAMANIO && c >= 0;
    }

    public void iniciarTablero() {
        int contador = 0;
        while (contador < 9) {
            int valorFilaAleatorio = randomizador.nextInt(TAMANIO);
            int valorColumnaAleatorio = randomizador.nextInt(TAMANIO);

            if (posicionEstaVacia(valorFilaAleatorio, valorColumnaAleatorio)) {
                int valorInicial = randomizador.nextInt(3) + 1;
                setFicha(valorFilaAleatorio, valorColumnaAleatorio, new Ficha(valorInicial));
                contador++;
            }
        }
    }
}
   
