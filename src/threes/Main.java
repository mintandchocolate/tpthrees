package threes;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import java.awt.BorderLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.SwingConstants;
import java.awt.Rectangle;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;

import logica_juego.Tablero;
import logica_juego.Ficha;
import logica_juego.Movimiento;

import logica_juego.Tablero;

public class Main implements Observer {

	private JFrame frmThrees;
	private JTable tablaDelJuego;
	private Tablero tablero;

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

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
		funcionalidadTeclas();
		tablero = new Tablero();
		tablero.llenarTablero();
		actualizarTablero(tablero);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmThrees =  new JFrame();
		frmThrees.setTitle("THREES");
		frmThrees.setResizable(false);
		frmThrees.setBounds(100, 100, 316, 476);
		frmThrees.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{300, 0};
		gridBagLayout.rowHeights = new int[]{437, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		frmThrees.getContentPane().setLayout(gridBagLayout);
		
		JPanel panelParaElJuego = new JPanel();
		panelParaElJuego.setBackground(new Color(255, 255, 255));
		GridBagConstraints gbc_panelParaElJuego = new GridBagConstraints();
		gbc_panelParaElJuego.insets = new Insets(80, 30, 35, 30);
		gbc_panelParaElJuego.weighty = 0.6;
		gbc_panelParaElJuego.weightx = 0.6;
		gbc_panelParaElJuego.fill = GridBagConstraints.BOTH;
		gbc_panelParaElJuego.gridx = 0;
		gbc_panelParaElJuego.gridy = 0;
		frmThrees.getContentPane().add(panelParaElJuego, gbc_panelParaElJuego);
		panelParaElJuego.setLayout(new BorderLayout(0, 0));
		
		tablaDelJuego = new JTable();
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
		
		panelParaElJuego.addComponentListener(new java.awt.event.ComponentAdapter() {
		    @Override
		    public void componentResized(java.awt.event.ComponentEvent e) {
		        int alto = panelParaElJuego.getHeight();
		        if (alto > 0) {
		            tablaDelJuego.setRowHeight(alto / 4 + 1);
		        }
		    }
		});
		panelParaElJuego.add(tablaDelJuego, BorderLayout.CENTER);
	}
	
	//COSAS DEL OBSERVER
	@Override
	public void tableroActualizado(Tablero tablero) {
		// TODO Auto-generated method stub
		
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
	                    celda.setBackground(Color.BLUE);
	                    celda.setForeground(Color.WHITE);
	                } else if (valor == 2) {
	                    celda.setBackground(Color.RED);
	                    celda.setForeground(Color.WHITE);
	                } else {
	                    celda.setBackground(Color.WHITE);
	                    celda.setForeground(Color.BLACK);
	                }
	            }

	            return celda;
	        }
	    };

	    for (int i = 0; i < tablaDelJuego.getColumnCount(); i++) {
	        tablaDelJuego.getColumnModel().getColumn(i).setCellRenderer(disenioDeTablas);
	    }
	}
	
	// esto se usa para evitar dobles teclas y multiples inputs accidentales
	private int teclasPresionadas;
	private boolean bloqueoTeclas = false;
	
	// metodo para poder jugar usando las teclas :p
	public void funcionalidadTeclas() {
		JComponent contentPane = (JComponent) frmThrees.getContentPane();
		
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
	                tablero.moverFichas(Movimiento.ARRIBA);
	                actualizarTablero(tablero);
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
	                tablero.moverFichas(Movimiento.ABAJO);
	                actualizarTablero(tablero);
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
	                tablero.moverFichas(Movimiento.IZQUIERDA);
	                actualizarTablero(tablero);
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
	                tablero.moverFichas(Movimiento.DERECHA);
	                actualizarTablero(tablero);
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
