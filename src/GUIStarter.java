// ALL AARON

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.NavigationFilter;
import javax.swing.text.Position;

@SuppressWarnings("serial")
public class GUIStarter extends JFrame {

	public static final String COLOR_ONE = "black";
	public static final String COLOR_TWO = "gray";
	public static final int MAX_DIFFICULTY = 10;

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final Dimension MAIN_SIZE = new Dimension(380, 420);
	private static final Dimension CHOOSER_SIZE = new Dimension(480, 600);
	private static final Dimension PLAY_SIZE = new Dimension(480, 240);


	private Box _centerPanel;
	private JList<String> _humanColorTable;
	private JList<String> _robotColorTable;
	private JList<String> _difficultyBox;
	private JList<String> _rulesBox;

	private ExecPanel _execPanel;
	private PiecesPanel _piecesPanel;

	public GUIStarter() {
		super();
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new CardLayout());
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		// GET CENTER PANEL AND SET UP LAYOUT
		this._centerPanel = new Box(BoxLayout.Y_AXIS);

		// CREATES COLOR OPTION BOX
		JPanel colorsPanel = new JPanel();
		colorsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 1));
		colorsPanel.setBorder(new TitledBorder("Choose your Color"));

		JPanel humanColorBox = new JPanel();
		humanColorBox.setBorder(new TitledBorder("Human Color"));
		this._humanColorTable = new JList<String>(new String[] {GUIStarter.COLOR_ONE, GUIStarter.COLOR_TWO});
		this._humanColorTable.setSelectedIndex(0);
		this._humanColorTable.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		this._humanColorTable.setVisibleRowCount(1);
		this._humanColorTable.setBorder(new EmptyBorder(10, 30, 10, 30));
		humanColorBox.add(this._humanColorTable);
		colorsPanel.add(humanColorBox);

		JPanel robotColorBox = new JPanel();
		robotColorBox.setBorder(new TitledBorder("Robot Color"));
		this._robotColorTable = new JList<String>(new String[] {GUIStarter.COLOR_ONE, GUIStarter.COLOR_TWO});
		this._robotColorTable.setSelectedIndex(1);
		this._robotColorTable.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		this._robotColorTable.setVisibleRowCount(1);
		this._robotColorTable.setBorder(new EmptyBorder(10, 30, 10, 30));
		this._robotColorTable.setEnabled(false);
		robotColorBox.add(this._robotColorTable);
		colorsPanel.add(robotColorBox);

		this._centerPanel.add(colorsPanel);

		final JList<String> humanCL = this._humanColorTable;
		final JList<String> robotCL = this._robotColorTable;
		this._humanColorTable.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt_) {
				robotCL.setSelectedIndex((humanCL.getSelectedIndex()+1)%2);
			}
		});

		// CREATES DIFFICULTY OPTION BOX
		String[] numList = new String[MAX_DIFFICULTY];
		for (int i = 0; i < MAX_DIFFICULTY; i++) {
			numList[i] = String.valueOf(i+1);
		}
		JPanel difficultyPanel = new JPanel();
		difficultyPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		difficultyPanel.setBorder(new CompoundBorder(new TitledBorder("Choose the Difficulty"), new EmptyBorder(10, 30, 10, 30)));
		this._difficultyBox = new JList<String>(numList);
		this._difficultyBox.setSelectedIndex(MAX_DIFFICULTY/2);
		this._difficultyBox.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		this._difficultyBox.setVisibleRowCount(1);
		difficultyPanel.add(this._difficultyBox);
		this._centerPanel.add(difficultyPanel);

		// CREATES RULE TYPE OPTION 
		Box rulesPanel = new Box(BoxLayout.Y_AXIS);
		rulesPanel.setBorder(new CompoundBorder(new TitledBorder("Choose the Rule Type"), new EmptyBorder(10, 20, 10, 20)));
		this._rulesBox = new JList<String>(new String[] {"official", "unofficial"});
		this._rulesBox.setSelectedIndex(1);
		this._rulesBox.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		this._rulesBox.setVisibleRowCount(1);
		rulesPanel.add(this._rulesBox);
		JTextArea rulesExplanation = new JTextArea("The official rules require jumps to be taken whenever possible.  The unofficial rules do not have this requirement.", 2, 18);
		rulesExplanation.setEditable(false);
		rulesExplanation.setLineWrap(true);
		rulesExplanation.setWrapStyleWord(true);
		rulesPanel.add(Box.createVerticalStrut(10));
		rulesPanel.add(rulesExplanation);
		this._centerPanel.add(rulesPanel);

		// CREATES BUTTONS
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());

		JButton inputPiecesButton = new JButton("CHOOSE STARTING POSITIONS");
		inputPiecesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae_) {
				if (_piecesPanel == null) {
					_piecesPanel = new PiecesPanel();
					getContentPane().add(_piecesPanel, 1);
				}
				((CardLayout) getContentPane().getLayout()).next(getContentPane());
				GUIStarter.this.setResizable(false);
				GUIStarter.this.setMinimumSize(GUIStarter.CHOOSER_SIZE);
				GUIStarter.this.setSize(GUIStarter.CHOOSER_SIZE);
			}
		});
		buttonPanel.add(inputPiecesButton, BorderLayout.WEST);

		JButton playButton = new JButton("PLAY NEW GAME");
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae_) {
				if (_execPanel == null) {
					_execPanel = new ExecPanel();
					getContentPane().add(_execPanel);
				}
				try {
					_execPanel.runMainThread(Starter.class,
											 new String[] {_robotColorTable.getSelectedValue(),
														   _humanColorTable.getSelectedValue(),
														   "robot=yes",
														   _difficultyBox.getSelectedValue(),
														   _rulesBox.getSelectedValue()
											 }
					);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
				((CardLayout) getContentPane().getLayout()).last(getContentPane());
				GUIStarter.this.setResizable(true);
				GUIStarter.this.setMinimumSize(GUIStarter.PLAY_SIZE);
				GUIStarter.this.setSize(GUIStarter.CHOOSER_SIZE);
			}
		});
		buttonPanel.add(playButton, BorderLayout.CENTER);

		// FINISHES UP
		mainPanel.add(this._centerPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		contentPane.add(mainPanel);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("CheckerBot");
		this.setResizable(false);
		this.setMinimumSize(GUIStarter.MAIN_SIZE);
		this.setSize(GUIStarter.MAIN_SIZE);
		this.validate();
	}

	public static void main(String[] args) {
		Runnable runner = new Runnable() {
			public void run() {
				GUIStarter test = new GUIStarter();
				test.setVisible(true);
			}
		};
		EventQueue.invokeLater(runner);
	}

	@SuppressWarnings("serial")
	private class ExecPanel extends JPanel {
		private JButton _resetButton;
		private JTextArea _textArea;

		private Thread _mainThread;

		private int _currentTerminalLength;
		private StringBuilder _currentInputStringB;
		private PrintWriter _processWriter;

		public ExecPanel() {
			super();

			// SETS UP NEW UI ELEMENTS
			this.setLayout(new BorderLayout());

			// SETS UP TERMINAL AREA
			this._textArea = new JTextArea(20, 20);
			this._textArea.setEditable(true);
			this._textArea.setLineWrap(true);
			this._textArea.setWrapStyleWord(true);
			JScrollPane scrollPane = new JScrollPane(this._textArea);
			scrollPane.setViewportBorder(new LineBorder(Color.white));
			this.add(scrollPane, BorderLayout.CENTER);

			NavigationFilter navFilter = new NavigationFilter() {
				public void setDot(NavigationFilter.FilterBypass fb, int dot, Position.Bias bias) {
					if (dot == ExecPanel.this._currentTerminalLength) {
						fb.setDot(dot, bias);
					}
				}

				public void moveDot(NavigationFilter.FilterBypass fb, int dot, Position.Bias bias) {
					if (dot == ExecPanel.this._currentTerminalLength) {
						fb.setDot(dot, bias);
					}
				}
			};
			this._textArea.setNavigationFilter(navFilter);

			this._currentTerminalLength = this._textArea.getDocument().getLength();
			this._currentInputStringB = new StringBuilder();
			DocumentListener terminalListener = new DocumentListener() {
				public void changedUpdate(DocumentEvent e_) {

				}

				public void insertUpdate(DocumentEvent e_) {
					if (ExecPanel.this._textArea.getDocument().getLength() > ExecPanel.this._currentTerminalLength) {
						try {
							String insertedText = ExecPanel.this._textArea.getDocument().getText(e_.getOffset(), e_.getLength());
							if (insertedText.indexOf('\n') != -1) {
								String[] lines = (ExecPanel.this._currentInputStringB + insertedText).toString().split("\\n", -1);
								for (int line = 0; line < lines.length-1; line++) {
									if (ExecPanel.this._mainThread != null && ExecPanel.this._processWriter != null) {
										ExecPanel.this._processWriter.println(lines[line]);
									}
								}
								ExecPanel.this._currentInputStringB = new StringBuilder(lines[lines.length-1]);
							} else {
								ExecPanel.this._currentInputStringB.append(insertedText);
							}
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
						ExecPanel.this._currentTerminalLength = ExecPanel.this._textArea.getDocument().getLength();
					}
				}

				public void removeUpdate(DocumentEvent e_) {
					ExecPanel.this._currentTerminalLength = ExecPanel.this._textArea.getDocument().getLength();
				}
			};
			this._textArea.getDocument().addDocumentListener(terminalListener);

			// reset button
			JPanel resetPanel = new JPanel();
			resetPanel.setBorder(new CompoundBorder(new LineBorder(Color.white), new EmptyBorder(1, 2, 1, 2)));
			this._resetButton = new JButton("RESET GAME");
			this._resetButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae_) {
					close();
				}
			});
			resetPanel.add(this._resetButton);

			this.add(resetPanel, BorderLayout.SOUTH);
		}

		public void runMainThread(final Class<?> mainClass, final String[] params) throws NoSuchMethodException {
			final Method mainMethod;
			try {
				mainMethod = mainClass.getMethod("main", String[].class);
			} catch (NoSuchMethodException e) {
				throw new NoSuchMethodException("Class argument does not have main method");
			}

			JOptionPane.showMessageDialog(this, "Make sure the robot is as far back\nand to the human's right as\npossible before pressing 'OK'.", "Message", JOptionPane.WARNING_MESSAGE);

			this._textArea.setText("");

			PipedInputStream inPipe = new PipedInputStream();
			PipedOutputStream outPipe = new PipedOutputStream();
			try {
				inPipe.connect(outPipe);
			} catch (IOException e) {
				System.err.println("Exception creating pipes");
				e.printStackTrace();
			}
			this._processWriter = new PrintWriter(outPipe, true);

			final InputStream stdin = System.in;
			final PrintStream stdout = System.out;

			// CREATES STARTER THREAD
			this._mainThread = new Thread() {
				public void run() {
					try {
						mainMethod.invoke(mainClass, (Object) params);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} finally {
						System.err.print("Resetting stdin/stdout...");
						System.setIn(stdin);
						System.setOut(stdout);
						System.err.println("DONE");
					}
				}
			};

			System.setIn(new SequenceInputStream(new BufferedInputStream(inPipe), System.in));
			System.setOut(new PrintStream(new OutputStream() {
				public void write(int b) throws IOException {
					//updates output string
					ExecPanel.this._currentTerminalLength++;
					// redirects data to the text area
					ExecPanel.this._textArea.append(String.valueOf((char)b));
					// scrolls the text area to the end of data
					ExecPanel.this._textArea.setCaretPosition(ExecPanel.this._textArea.getDocument().getLength());
				}
			}));

			this._mainThread.start();
		}

		public void close() {
			System.err.println("Closing ExecPanel...");
			if (this._mainThread != null) {
				this._mainThread.interrupt();
				try {
					System.err.println("Waiting for main thread...");
					this._mainThread.join();
					System.err.println("Thread completed successfully");
				} catch (InterruptedException e) {
					System.err.println("InterruptedException waiting for main thread");
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
				this._mainThread = null;
			}
			this._processWriter = null;
			((CardLayout) this.getRootPane().getContentPane().getLayout()).next(this.getRootPane().getContentPane());
			GUIStarter.this.setResizable(false);
			GUIStarter.this.setMinimumSize(GUIStarter.MAIN_SIZE);
			GUIStarter.this.setSize(GUIStarter.MAIN_SIZE);
		}
	}

	@SuppressWarnings("serial")
	private class PiecesPanel extends JPanel {
		ImageIcon _emptyIcon;
		ImageIcon[] _icons;

		public PiecesPanel() {
			super();

			this.setLayout(new BorderLayout());

			JPanel playerPanel = new JPanel();
			playerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			playerPanel.setBorder(new CompoundBorder(new TitledBorder("Whose turn is it?"), new EmptyBorder(10, 20, 10, 20)));
			final JList<String> playerBox = new JList<String>(new String[] {"robot", "human"});
			playerBox.setSelectedIndex(0);
			playerBox.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			playerBox.setVisibleRowCount(1);
			playerPanel.add(playerBox);
			this.add(playerPanel, BorderLayout.NORTH);

			 while (true) {
				try {
					InputStream[] resourceStreams = new InputStream[] {
						PiecesPanel.class.getResourceAsStream("images/empty.png"),
						PiecesPanel.class.getResourceAsStream("images/green.png"),
						PiecesPanel.class.getResourceAsStream("images/gray.png"),
						PiecesPanel.class.getResourceAsStream("images/black.png"),
						PiecesPanel.class.getResourceAsStream("images/gray-king.png"),
						PiecesPanel.class.getResourceAsStream("images/black-king.png")
					};

					this._emptyIcon = new ImageIcon(
						ImageIO.read(resourceStreams[0]).getScaledInstance(52, 52, Image.SCALE_SMOOTH)
					);

					this._icons = new ImageIcon[5];
					for (int i = 0; i < 5; i++) {
						this._icons[i] = new ImageIcon(
							ImageIO.read(resourceStreams[i + 1]).getScaledInstance(52, 52, Image.SCALE_SMOOTH)
						);
					}
					break;
				} catch (IOException | IllegalArgumentException e) {
					System.err.println("Unable to create images from file.");

					int dialogResult = JOptionPane.showConfirmDialog(
						GUIStarter.this,
						"Problem opening image files.\nPress OK to try again,\nor CANCEL to exit.",
						"",
						JOptionPane.OK_CANCEL_OPTION
					);

					if (dialogResult != JOptionPane.OK_OPTION) {
						System.exit(1);
					}
				}
			}

			JPanel boardWrapperPanel = new JPanel();
			JPanel boardPanel = new JPanel(new GridLayout(8, 8));
			final JLabel[][] labels = new JLabel[8][];
			for (int row = 0; row<8; row++) {
				labels[row] = new JLabel[8];
				for (int column = 7; column>=0; column--) {
					if ((row+column)%2==0) {
						final JLabel label = new JLabel(this._icons[0]);
						label.addMouseListener(new MouseListener() {
							public void mouseClicked(MouseEvent e) {
								for (int currentIconIndex = 0; currentIconIndex<PiecesPanel.this._icons.length; currentIconIndex++) {
									if (PiecesPanel.this._icons[currentIconIndex] == label.getIcon()) {
										currentIconIndex = ++currentIconIndex%PiecesPanel.this._icons.length;
										label.setIcon(PiecesPanel.this._icons[currentIconIndex]);
										return;
									}
								}
							}
							public void mouseEntered(MouseEvent e) {}
							public void mouseExited(MouseEvent e) {}
							public void mousePressed(MouseEvent e) {}
							public void mouseReleased(MouseEvent e) {}
						});
						boardPanel.add(label);
						labels[row][column] = label;
					} else {
						JLabel label = new JLabel(this._emptyIcon);
						boardPanel.add(label);
						labels[row][column] = label;
					}
				}
			}
			boardWrapperPanel.add(boardPanel);
			this.add(boardWrapperPanel, BorderLayout.CENTER);

			JPanel buttonPanel = new JPanel(new BorderLayout());

			JButton returnButton = new JButton("RETURN TO MENU");
			returnButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae_) {
					((CardLayout) getContentPane().getLayout()).first(getContentPane());
					GUIStarter.this.setResizable(false);
					GUIStarter.this.setMinimumSize(GUIStarter.MAIN_SIZE);
					GUIStarter.this.setSize(GUIStarter.MAIN_SIZE);
				}
			});
			buttonPanel.add(returnButton, BorderLayout.WEST);

			JButton playButton = new JButton("PLAY GAME");
			playButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae_) {
					if (_execPanel == null) {
						_execPanel = new ExecPanel();
						getContentPane().add(_execPanel);
					}

					StringBuilder color1PBuilder = new StringBuilder("=");
					StringBuilder color2PBuilder = new StringBuilder("=");
					StringBuilder color1KBuilder = new StringBuilder("=");
					StringBuilder color2KBuilder = new StringBuilder("=");

					for (int row = 0; row<8; row++) {
						for (int column = 0; column<8; column++) {
							if (_icons[1]==labels[row][column].getIcon()) {
								color1PBuilder.append(10*row + column);
							}
							if (_icons[2]==labels[row][column].getIcon()) {
								color2PBuilder.append(10*row + column);
							}
							if (_icons[3]==labels[row][column].getIcon()) {
								color1KBuilder.append(10*row + column);
							}
							if (_icons[4]==labels[row][column].getIcon()) {
								color2KBuilder.append(10*row + column);
							}
						}
					}
					if (playerBox.getSelectedIndex() + _humanColorTable.getSelectedIndex() == 1) {
						StringBuilder holder = color1PBuilder;
						color1PBuilder = color2PBuilder;
						color2PBuilder = holder;
						
						holder = color1KBuilder;
						color1KBuilder = color2KBuilder;
						color2KBuilder = holder;
					}
					try {
						_execPanel.runMainThread(MidgameStarter.class, 
												 new String[] {playerBox.getSelectedValue(),
															   "p1" + color1PBuilder.toString(),
															   "p2" + color2PBuilder.toString(),
															   "k1" + color1KBuilder.toString(),
															   "k2" + color2KBuilder.toString(),
															   _robotColorTable.getSelectedValue(),
															   _humanColorTable.getSelectedValue(),
															   _difficultyBox.getSelectedValue(),
															   _rulesBox.getSelectedValue()
												 }
						);
					} catch (NoSuchMethodException e) {
						//this should never happen
						e.printStackTrace();
					}
					((CardLayout) getContentPane().getLayout()).last(getContentPane());
					GUIStarter.this.setResizable(true);
					GUIStarter.this.setMinimumSize(GUIStarter.PLAY_SIZE);
					GUIStarter.this.setSize(GUIStarter.CHOOSER_SIZE);
				}
			});
			buttonPanel.add(playButton, BorderLayout.CENTER);

			this.add(buttonPanel, BorderLayout.SOUTH);
		}
	}
}
