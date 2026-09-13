package threes;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import java.awt.BorderLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.LineBorder;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

import logica_juego.Tablero;
import logica_juego.Ficha;
import logica_juego.Movimiento;
import java.awt.FlowLayout;
import javax.swing.JLayeredPane;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Dimension;


public class Main implements Observer {

	private JFrame frmThrees;
	private JTable tablaDelJuego;
	private Tablero tablero;
	private Controller controller;
	private Color AZULFICHA = new Color(114, 202, 242);
	private Color ROJOFICHA = new Color(241, 103, 128);

	public JFrame obtenerFrame() {
		return frmThrees;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmThrees.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		initialize();
		iniciarPartida();
	}

	private void iniciarPartida() {
		tablero = new Tablero();
		controller = new Controller(tablero, this);
		controller.funcionalidadTeclas();
		tablero.iniciarTablero();
		actualizarTablero(tablero);
		reiniciarTimerInactividad();
	}
	
	private void initialize() {
		frmThrees =  new JFrame();
		frmThrees.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		frmThrees.setTitle("THREES");
		frmThrees.setResizable(false);
		frmThrees.setBounds(100, 100, 316, 476);
		frmThrees.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmThrees.getContentPane().setLayout(null);
		
		JPanel panelParaElJuego = new JPanel();
		panelParaElJuego.setBounds(34, 90, 233, 315);
		panelParaElJuego.setBackground(new Color(255, 255, 255));
		frmThrees.getContentPane().add(panelParaElJuego);
		panelParaElJuego.setLayout(new BorderLayout(0, 0));
		
		tablaDelJuego = new JTable();
		tablaDelJuego.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablaDelJuego.setRowHeight(79);
		panelParaElJuego.add(tablaDelJuego, BorderLayout.CENTER);
		tablaDelJuego.setEnabled(false);
		tablaDelJuego.setRowSelectionAllowed(false);
		tablaDelJuego.setBorder(new LineBorder(new Color(0, 0, 0)));
		tablaDelJuego.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
			},
			new String[] {
				"New column", "New column", "New column", "New column"
			}
		));
		
		fondoProximaFicha = new JPanel();
		fondoProximaFicha.setBounds(125, 0, 45, 60);
		frmThrees.getContentPane().add(fondoProximaFicha);
		fondoProximaFicha.setBackground(new Color(207, 231, 224));
		fondoProximaFicha.setLayout(null);
		
		proximaFicha = new JPanel();
		proximaFicha.setSize(new Dimension(25, 25));
		proximaFicha.setBounds(10, 28, 25, 25);
		fondoProximaFicha.add(proximaFicha);
		proximaFicha.setLayout(null);
		
		panelParaElJuego.addComponentListener(new java.awt.event.ComponentAdapter() {
		    @Override
		    public void componentResized(java.awt.event.ComponentEvent e) {
		        int alto = panelParaElJuego.getHeight();
		        if (alto > 0) {
		            tablaDelJuego.setRowHeight(alto / 4 + 1);
		        }
		    }
		});
	}
	
	//COSAS DEL OBSERVER
	@Override
	public void tableroActualizado(Tablero tablero) {
		actualizarTablero(tablero);
		verificarFinDelJuego();

	}
	
	private void verificarFinDelJuego() {
		if (!tablero.hayMovimientosPosibles()) {
			//mostrarDialogoGameOver();
		}
	}

	public void actualizarTablero(Tablero tablero) {
	    Object[][] datos = new Object[tablero.TAMANIO][tablero.TAMANIO];

	    for (int fila = 0; fila < tablero.TAMANIO; fila++) {
	        for (int columna = 0; columna < tablero.TAMANIO; columna++) {
	            Ficha ficha = tablero.getFicha(fila, columna);
	            datos[fila][columna] = (ficha != null) ? ficha.getvalor() : null;
	        }
	    }

	    DefaultTableModel modelo = new DefaultTableModel(
	            datos, new String[] {"", "", "", ""}
	    );

	    tablaDelJuego.setModel(modelo);

	    // centrar el texto de cada celda y dar colores
	    DefaultTableCellRenderer disenioDeTablas = new DefaultTableCellRenderer() {
	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	            Component celda = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	            setHorizontalAlignment(SwingConstants.CENTER);

	            if (value == null) {
	                celda.setBackground(Color.WHITE);
	                celda.setForeground(Color.BLACK);
	            } else {
	                int valor = (int) value;
	                if (valor == 1) {
	                    celda.setBackground(AZULFICHA);
	                    celda.setForeground(Color.WHITE);
	                } else if (valor == 2) {
	                    celda.setBackground(ROJOFICHA);
	                    celda.setForeground(Color.WHITE);
	                } else {
	                    celda.setBackground(Color.WHITE);
	                    celda.setForeground(Color.BLACK);
	                }
	            }
	            
	            boolean casillaPalpitando = false;
	            if (sugerenciaJugada != null) {
	                boolean esLaPrimera = (row == sugerenciaJugada[0][0] && column == sugerenciaJugada[0][1]);
	                boolean esLaSegunda = (row == sugerenciaJugada[1][0] && column == sugerenciaJugada[1][1]);
	                casillaPalpitando = esLaPrimera || esLaSegunda;
	            }

	            if (casillaPalpitando) {
	                celda.setFont(new Font("Verdana", Font.BOLD, tamanioFuenteCasillaSugerida));
	            } else {
	            	celda.setFont(new Font("Verdana", Font.BOLD, 24));
	            }
	            return celda;
	        }
	    };

	    for (int i = 0; i < tablaDelJuego.getColumnCount(); i++) {
	        tablaDelJuego.getColumnModel().getColumn(i).setCellRenderer(disenioDeTablas);
	    }
	    
	    actualizarPreviewSiguienteFicha();
	}
	
	private int[][] sugerenciaJugada = null;
	private Timer palpitaciones;
	private boolean creciendo = true;
	private int tamanioFuenteCasillaSugerida = 24;
	
	public void mostrarSugerencia() {
		sugerenciaJugada = tablero.jugadaSugerida();
		
		if (sugerenciaJugada == null) {
			return;
		}
		
		if (palpitaciones != null) {
			palpitaciones.stop();
		}
		
		palpitaciones = new Timer(40, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent accion) {
				if (creciendo) {
					tamanioFuenteCasillaSugerida++;
					if (tamanioFuenteCasillaSugerida >= 32) {
						creciendo = false;
					}
				} else {
					tamanioFuenteCasillaSugerida--;
					if (tamanioFuenteCasillaSugerida <= 24) {
						creciendo = true;
					}
				}
				actualizarTablero(tablero);
			}
		});
		
		palpitaciones.start();
	}
	
	private Timer inactividad;
	private JPanel fondoProximaFicha;
	private JPanel proximaFicha;
	
	public void reiniciarTimerInactividad() {
		if (inactividad != null) {
			inactividad.stop();
			}
		sugerenciaJugada = null; 
		
		// si no tocas nada por 8 segundos muestra una jugada sugerida :p
		inactividad = new Timer(8000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarSugerencia();
				}
			});
		inactividad.setRepeats(false);
		inactividad.start();
	}
	
	private void actualizarPreviewSiguienteFicha() {
		int valorDeFichaSiguiente = tablero.obtenerProximoValor();
		
		if (valorDeFichaSiguiente <= 0 || valorDeFichaSiguiente > 3) {
			return;
		}
		
		if (valorDeFichaSiguiente == 1) {
			proximaFicha.setBackground(AZULFICHA);
		} else if (valorDeFichaSiguiente == 2) {
			proximaFicha.setBackground(ROJOFICHA);
		} else {
			proximaFicha.setBackground(Color.WHITE);
		}
	}
}
