package logica_juego;

import static org.junit.Assert.*;

import org.junit.Test;

public class TableroTest {

	@Test (expected = IllegalArgumentException.class)
	public void testFilaInvalida() {
		Tablero tablero = new Tablero();
		tablero.setFicha(4, 0, null);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testColInvalida() {
		Tablero tablero = new Tablero();
		tablero.setFicha(0, 4, null);
	}
	
	@Test
	public void fusionUnoConDos() {
		Tablero tablero = new Tablero();
		tablero.setFicha(0, 0, new Ficha(1));
		tablero.setFicha(0, 1, new Ficha(2));
		tablero.moverFichas(Movimiento.IZQUIERDA);
		assertEquals(3, tablero.getFicha(0, 0).getvalor());
	}
	
	@Test
	public void fusionMismoValor() {
		Tablero tablero = new Tablero();
		tablero.setFicha(0, 2, new Ficha(6));
		tablero.setFicha(0, 3, new Ficha(6));
		tablero.moverFichas(Movimiento.DERECHA);
		assertEquals(12, tablero.getFicha(0, 3).getvalor());
	}
}
