package logica_juego;

public enum Movimiento {
	ARRIBA(-1,0),
	ABAJO(1,0),
	IZQUIERDA(0,-1),
	DERECHA(0,1);
	
	public final int fila;
	public final int col;
	
	Movimiento (int fila, int col){
		this.fila = fila;
		this.col = col;
	
	}
	
	public int getFila() {
		return this.fila;
	}

	public int getCol() {
		return this.col;
	}

	
}
