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
}
