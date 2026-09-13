package threes;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import logica_juego.Movimiento;
import logica_juego.Tablero;

public class Controller {
	private Tablero tablero;
	private Vista main;
	
	public Controller (Tablero tablero, Vista main) {
		this.tablero = tablero;
		this.main = main;
		tablero.addObserver(main);
	}
	
	public void mover(Movimiento movimiento) {
		tablero.moverFichas(movimiento);
		//de esta manera el tablero notifica al main por observer
	}
	
	// esto se usa para evitar dobles teclas y multiples inputs accidentales
		private int teclasPresionadas;
		private boolean bloqueoTeclas = false;
		
		// metodo para poder jugar usando las teclas :p
		public void funcionalidadTeclas() {
			JComponent contentPane = (JComponent) main.obtenerFrame().getContentPane();
			
			contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed UP"), "ARRIBA");
		    contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released UP"), "SOLTAR_ARRIBA");

		    contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed DOWN"), "ABAJO");
		    contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released DOWN"), "SOLTAR_ABAJO");

		    contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed LEFT"), "IZQUIERDA");
		    contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released LEFT"), "SOLTAR_IZQUIERDA");

		    contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed RIGHT"), "DERECHA");
		    contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released RIGHT"), "SOLTAR_DERECHA");
		    
		    contentPane.getActionMap().put("ARRIBA", new AbstractAction() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		            teclasPresionadas++;
		            if (teclasPresionadas == 1 && !bloqueoTeclas) {
		                bloqueoTeclas = true;
		                mover(Movimiento.ARRIBA);
		                main.reiniciarTimerInactividad();
		            }
		        }
		    });
		    
		    contentPane.getActionMap().put("SOLTAR_ARRIBA", new AbstractAction() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		            teclasPresionadas--;
		            if (teclasPresionadas <= 0) {
		                teclasPresionadas = 0;
		                bloqueoTeclas = false;
		            }
		        }
		    });

		    contentPane.getActionMap().put("ABAJO", new AbstractAction() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		            teclasPresionadas++;
		            if (teclasPresionadas == 1 && !bloqueoTeclas) {
		            	bloqueoTeclas = true;
		            	mover(Movimiento.ABAJO);
		            	main.reiniciarTimerInactividad();
		            }
		        }
		    });
		    
		    contentPane.getActionMap().put("SOLTAR_ABAJO", new AbstractAction() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		            teclasPresionadas--;
		            if (teclasPresionadas <= 0) {
		                teclasPresionadas = 0;
		                bloqueoTeclas = false;
		            }
		        }
		    });

		    contentPane.getActionMap().put("IZQUIERDA", new AbstractAction() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		            teclasPresionadas++;
		            if (teclasPresionadas == 1 && !bloqueoTeclas) {
		            	bloqueoTeclas = true;
		            	mover(Movimiento.IZQUIERDA);
		            	main.reiniciarTimerInactividad();
		            }
		        }
		    });
		    
		    contentPane.getActionMap().put("SOLTAR_IZQUIERDA", new AbstractAction() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		            teclasPresionadas--;
		            if (teclasPresionadas <= 0) {
		                teclasPresionadas = 0;
		                bloqueoTeclas = false;
		            }
		        }
		    });

		    contentPane.getActionMap().put("DERECHA", new AbstractAction() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		            teclasPresionadas++;
		            if (teclasPresionadas == 1 && !bloqueoTeclas) {
		            	bloqueoTeclas = true;
		            	mover(Movimiento.DERECHA);
		            	main.reiniciarTimerInactividad();
		            }
		        }
		    });
		    
		    contentPane.getActionMap().put("SOLTAR_DERECHA", new AbstractAction() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		            teclasPresionadas--;
		            if (teclasPresionadas <= 0) {
		                teclasPresionadas = 0;
		                bloqueoTeclas = false;
		            }
		        }
		    });
		}
}
