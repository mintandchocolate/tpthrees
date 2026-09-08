package threes;

import logica_juego.Movimiento;
import logica_juego.Tablero;

public class Controller {
	private Tablero tablero;
	private Main main;
	
	public Controller (Tablero tablero, Main main) {
		this.tablero = tablero;
		this.main = main;
		tablero.addObserver(main);
	}
	public void mover(Movimiento movimiento) {
		tablero.moverFichas(movimiento);
		//de esta manera el tablero notifica al main por observer
	}
}
