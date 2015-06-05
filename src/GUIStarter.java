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
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
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

/**
 * @author Aaron Miller
 */

@SuppressWarnings("serial")
public class GUIStarter extends JFrame {

	public static final String COLOR_ONE = "black";
	public static final String COLOR_TWO = "gray";
	public static final int MAX_DIFFICULTY = 10;

	private static final Dimension MAIN_SIZE = new Dimension(380, 480);
	private static final Dimension CHOOSER_SIZE = new Dimension(480, 600);
	private static final Dimension PLAY_SIZE = new Dimension(480, 240);

	private CardLayout mainLayout;

	private JList<String> humanColorList;
	private JList<String> robotColorList;
	private JList<String> difficultyList;
	private JList<String> rulesList;

	private ExecPanel execPanel;

	public GUIStarter() {
		super();
		Container contentPane = this.getContentPane();
		this.mainLayout = new CardLayout();
		contentPane.setLayout(this.mainLayout);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		MainPanel mainPanel = new MainPanel();
		contentPane.add(mainPanel);

		PiecesPanel piecesPanel = new PiecesPanel();
		contentPane.add(piecesPanel);

		this.execPanel = new ExecPanel();
		contentPane.add(execPanel);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("CheckerBot");
		this.setResizable(false);
		this.setMinimumSize(GUIStarter.MAIN_SIZE);
		this.setSize(GUIStarter.MAIN_SIZE);
		this.validate();
	}

	public static void main(String[] args) {
		Runnable runner = new Runnable() {
			@Override
			public void run() {
				GUIStarter test = new GUIStarter();
				test.setVisible(true);
			}
		};
		EventQueue.invokeLater(runner);
	}

	private class MainPanel extends JPanel {
		public MainPanel() {
			// SET UP LAYOUT
			this.setLayout(new BorderLayout());
			Box mainBox = new Box(BoxLayout.Y_AXIS);

			// CREATE COLOR OPTION BOX
			JPanel colorPanel = new JPanel();
			colorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 1));
			colorPanel.setBorder(new TitledBorder("Choose your Color"));

			JPanel humanColorPanel = new JPanel();
			humanColorPanel.setBorder(new TitledBorder("Human Color"));
			GUIStarter.this.humanColorList = new JList<String>(
				new String[] {GUIStarter.COLOR_ONE, GUIStarter.COLOR_TWO}
			);
			GUIStarter.this.humanColorList.setSelectedIndex(0);
			GUIStarter.this.humanColorList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			GUIStarter.this.humanColorList.setVisibleRowCount(1);
			GUIStarter.this.humanColorList.setBorder(new EmptyBorder(10, 30, 10, 30));
			humanColorPanel.add(GUIStarter.this.humanColorList);
			colorPanel.add(humanColorPanel);

			JPanel robotColorPanel = new JPanel();
			robotColorPanel.setBorder(new TitledBorder("Robot Color"));
			GUIStarter.this.robotColorList = new JList<String>(
				new String[] {GUIStarter.COLOR_ONE, GUIStarter.COLOR_TWO}
			);
			GUIStarter.this.robotColorList.setSelectedIndex(1);
			GUIStarter.this.robotColorList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			GUIStarter.this.robotColorList.setVisibleRowCount(1);
			GUIStarter.this.robotColorList.setBorder(new EmptyBorder(10, 30, 10, 30));
			GUIStarter.this.robotColorList.setEnabled(false);
			robotColorPanel.add(GUIStarter.this.robotColorList);
			colorPanel.add(robotColorPanel);

			mainBox.add(colorPanel);

			final JList<String> humanCL = GUIStarter.this.humanColorList;
			final JList<String> robotCL = GUIStarter.this.robotColorList;
			GUIStarter.this.humanColorList.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent event) {
					robotCL.setSelectedIndex((humanCL.getSelectedIndex()+1)%2);
				}
			});

			// CREATE DIFFICULTY OPTION BOX
			String[] numList = new String[MAX_DIFFICULTY];
			for (int i = 0; i < MAX_DIFFICULTY; i++) {
				numList[i] = String.valueOf(i+1);
			}
			JPanel difficultyPanel = new JPanel();
			difficultyPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			difficultyPanel.setBorder(new CompoundBorder(new TitledBorder("Choose the Difficulty"), new EmptyBorder(10, 30, 10, 30)));
			GUIStarter.this.difficultyList = new JList<String>(numList);
			GUIStarter.this.difficultyList.setSelectedIndex(MAX_DIFFICULTY/2);
			GUIStarter.this.difficultyList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			GUIStarter.this.difficultyList.setVisibleRowCount(1);
			difficultyPanel.add(GUIStarter.this.difficultyList);
			mainBox.add(difficultyPanel);

			// CREATES RULE TYPE OPTION
			Box rulesBox = new Box(BoxLayout.Y_AXIS);
			rulesBox.setBorder(new CompoundBorder(new TitledBorder("Choose the Rule Type"), new EmptyBorder(10, 20, 10, 20)));
			GUIStarter.this.rulesList = new JList<String>(new String[] {"official", "unofficial"});
			GUIStarter.this.rulesList.setSelectedIndex(1);
			GUIStarter.this.rulesList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			GUIStarter.this.rulesList.setVisibleRowCount(1);
			rulesBox.add(GUIStarter.this.rulesList);
			JTextArea rulesExplanation = new JTextArea("The official rules require jumps to be taken whenever possible.  The unofficial rules do not have this requirement.", 2, 18);
			rulesExplanation.setEditable(false);
			rulesExplanation.setLineWrap(true);
			rulesExplanation.setWrapStyleWord(true);
			rulesBox.add(Box.createVerticalStrut(10));
			rulesBox.add(rulesExplanation);
			mainBox.add(rulesBox);

			// CREATE BUTTONS
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new BorderLayout());

			JButton inputPiecesButton = new JButton("CHOOSE STARTING POSITIONS");
			inputPiecesButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					GUIStarter.this.mainLayout.next(getContentPane());
					GUIStarter.this.setResizable(false);
					GUIStarter.this.setMinimumSize(GUIStarter.CHOOSER_SIZE);
					GUIStarter.this.setSize(GUIStarter.CHOOSER_SIZE);
				}
			});
			buttonPanel.add(inputPiecesButton, BorderLayout.WEST);

			JButton playButton = new JButton("PLAY NEW GAME");
			playButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					GUIStarter.this.execPanel.runMainThread(
						Starter.class,
						new String[] {
							GUIStarter.this.robotColorList.getSelectedValue(),
							GUIStarter.this.humanColorList.getSelectedValue(),
							"robot=yes",
							GUIStarter.this.rulesList.getSelectedValue(),
							GUIStarter.this.difficultyList.getSelectedValue()
						}
					);
					GUIStarter.this.mainLayout.last(getContentPane());
					GUIStarter.this.setResizable(true);
					GUIStarter.this.setMinimumSize(GUIStarter.PLAY_SIZE);
					GUIStarter.this.setSize(GUIStarter.CHOOSER_SIZE);
				}
			});
			buttonPanel.add(playButton, BorderLayout.CENTER);

			// FINISH UP
			this.add(mainBox, BorderLayout.CENTER);
			this.add(buttonPanel, BorderLayout.SOUTH);
		}
	}

	@SuppressWarnings("serial")
	private class ExecPanel extends JPanel {
		private JButton resetButton;
		private JTextArea textArea;

		private Thread mainThread;

		private int currentTerminalLength;
		private StringBuilder currentInputStringB;
		private PrintWriter processWriter;

		public ExecPanel() {
			super();

			// SETS UP NEW UI ELEMENTS
			this.setLayout(new BorderLayout());

			// SETS UP TERMINAL AREA
			this.textArea = new JTextArea(20, 20);
			this.textArea.setEditable(true);
			this.textArea.setLineWrap(true);
			this.textArea.setWrapStyleWord(true);
			JScrollPane scrollPane = new JScrollPane(this.textArea);
			scrollPane.setViewportBorder(new LineBorder(Color.white));
			this.add(scrollPane, BorderLayout.CENTER);

			NavigationFilter navFilter = new NavigationFilter() {
				@Override
				public void setDot(NavigationFilter.FilterBypass fb, int dot, Position.Bias bias) {
					if (dot == ExecPanel.this.currentTerminalLength) {
						fb.setDot(dot, bias);
					}
				}

				@Override
				public void moveDot(NavigationFilter.FilterBypass fb, int dot, Position.Bias bias) {
					if (dot == ExecPanel.this.currentTerminalLength) {
						fb.setDot(dot, bias);
					}
				}
			};
			this.textArea.setNavigationFilter(navFilter);

			this.currentTerminalLength = this.textArea.getDocument().getLength();
			this.currentInputStringB = new StringBuilder();
			DocumentListener terminalListener = new DocumentListener() {
				@Override
				public void changedUpdate(DocumentEvent event) {
				}

				@Override
				public void insertUpdate(DocumentEvent event) {
					if (ExecPanel.this.textArea.getDocument().getLength() > ExecPanel.this.currentTerminalLength) {
						try {
							String insertedText = ExecPanel.this.textArea.getDocument().getText(event.getOffset(), event.getLength());
							if (insertedText.indexOf('\n') != -1) {
								String[] lines = (ExecPanel.this.currentInputStringB + insertedText).toString().split("\\n", -1);
								for (int line = 0; line < lines.length-1; line++) {
									if (ExecPanel.this.mainThread != null && ExecPanel.this.processWriter != null) {
										ExecPanel.this.processWriter.println(lines[line]);
									}
								}
								ExecPanel.this.currentInputStringB = new StringBuilder(lines[lines.length-1]);
							} else {
								ExecPanel.this.currentInputStringB.append(insertedText);
							}
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
						ExecPanel.this.currentTerminalLength = ExecPanel.this.textArea.getDocument().getLength();
					}
				}

				@Override
				public void removeUpdate(DocumentEvent event) {
					ExecPanel.this.currentTerminalLength = ExecPanel.this.textArea.getDocument().getLength();
				}
			};
			this.textArea.getDocument().addDocumentListener(terminalListener);

			// reset button
			JPanel resetPanel = new JPanel();
			resetPanel.setBorder(new CompoundBorder(new LineBorder(Color.white), new EmptyBorder(1, 2, 1, 2)));
			this.resetButton = new JButton("RESET GAME");
			this.resetButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					close();
				}
			});
			resetPanel.add(this.resetButton);

			this.add(resetPanel, BorderLayout.SOUTH);
		}

		public void runMainThread(Class<? extends Starter> mainClass, final String[] params) {
			JOptionPane.showMessageDialog(
				this,
				"Make sure the robot is as far back\nand to the human's right as\npossible before pressing 'OK'.",
				"Message",
				JOptionPane.WARNING_MESSAGE
			);

			this.textArea.setText("");

			PipedInputStream inPipe = new PipedInputStream();
			PipedOutputStream outPipe = new PipedOutputStream();
			try {
				inPipe.connect(outPipe);
			} catch (IOException e) {
				System.err.println("Exception creating pipes");
				e.printStackTrace();
			}
			this.processWriter = new PrintWriter(outPipe, true);

			final InputStream stdin = System.in;
			final PrintStream stdout = System.out;

			// CREATES STARTER THREAD
			this.mainThread = new Thread() {
				@Override
				public void run() {
					try {
						ExecPanel.this.runMainThreadHelper(params);
					} catch (InterruptedException e) {
						// do nothing else, as the thread will complete cleanup and exit
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
				@Override
				public void write(int b) throws IOException {
					//updates output string
					ExecPanel.this.currentTerminalLength++;
					// redirects data to the text area
					ExecPanel.this.textArea.append(String.valueOf((char)b));
					// scrolls the text area to the end of data
					ExecPanel.this.textArea.setCaretPosition(ExecPanel.this.textArea.getDocument().getLength());
				}
			}));

			this.mainThread.start();
		}

		private <T extends Starter> void runMainThreadHelper(String[] params) throws InterruptedException {
			T.main(params);
		}

		public void close() {
			System.err.println("Closing ExecPanel...");
			if (this.mainThread != null) {
				this.mainThread.interrupt();
				try {
					System.err.println("Waiting for main thread...");
					this.mainThread.join();
					System.err.println("Thread completed successfully");
				} catch (InterruptedException e) {
					System.err.println("InterruptedException waiting for main thread");
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
				this.mainThread = null;
			}
			this.processWriter = null;
			GUIStarter.this.mainLayout.next(this.getRootPane().getContentPane());
			GUIStarter.this.setResizable(false);
			GUIStarter.this.setMinimumSize(GUIStarter.MAIN_SIZE);
			GUIStarter.this.setSize(GUIStarter.MAIN_SIZE);
		}
	}

	@SuppressWarnings("serial")
	private class PiecesPanel extends JPanel {
		private ImageIcon emptyIcon;
		private ImageIcon[] icons;
		private JLabel[][] labels;
		private JList<String> turnSelectionList;

		private static final int GREEN_ICON = 0;
		private static final int GRAY_PIECE_ICON = 1;
		private static final int BLACK_PIECE_ICON = 2;
		private static final int GRAY_KING_ICON = 3;
		private static final int BLACK_KING_ICON = 4;

		public PiecesPanel() {
			super();

			this.setLayout(new BorderLayout());

			/*
			 * This creates the upper panel of the midgame start menu, 
			 * which gives the user a choice of whose turn it currently is.
			 */
			JPanel currentTurnPanel = new JPanel();
			currentTurnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			currentTurnPanel.setBorder(
				new CompoundBorder(
					new TitledBorder("Whose turn is it?"),
					new EmptyBorder(10, 20, 10, 20)
				)
			);
			this.turnSelectionList = new JList<String>(new String[] {"robot", "human"});
			this.turnSelectionList.setSelectedIndex(0);
			this.turnSelectionList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			this.turnSelectionList.setVisibleRowCount(1);
			currentTurnPanel.add(this.turnSelectionList);
			this.add(currentTurnPanel, BorderLayout.NORTH);

			/*
			 * This creates the middle panel, which holds the 
			 * representation of the board with which the player 
			 * will choose the current locations of the pieces.
			 */
			this.loadImages();

			JPanel boardWrapperPanel = new JPanel();
			JPanel boardPanel = this.generateBoardPanel();
			boardWrapperPanel.add(boardPanel);
			this.add(boardWrapperPanel, BorderLayout.CENTER);

			/*
			 * This creates the lower panel, which holds the button the user can press to quit the 
			 * current game and return to the main menu.
			 */
			JPanel buttonPanel = new JPanel(new BorderLayout());

			JButton returnButton = new JButton("RETURN TO MENU");
			returnButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					GUIStarter.this.mainLayout.first(getContentPane());
					GUIStarter.this.setResizable(false);
					GUIStarter.this.setMinimumSize(GUIStarter.MAIN_SIZE);
					GUIStarter.this.setSize(GUIStarter.MAIN_SIZE);
				}
			});
			buttonPanel.add(returnButton, BorderLayout.WEST);

			JButton playButton = new JButton("PLAY GAME");
			playButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					PiecesPanel.this.play();
				}
			});
			buttonPanel.add(playButton, BorderLayout.CENTER);

			this.add(buttonPanel, BorderLayout.SOUTH);
		}

		/**
		 * Loads the image resources representing each square of the board. The resources are 
		 * assumed to be in the .jar file containing this class, in the images directory.
		 *
		 * @return  True if the images were loaded successfully, false otherwise
		 */
		private boolean loadImages() {
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

					this.emptyIcon = new ImageIcon(
						ImageIO.read(resourceStreams[0]).getScaledInstance(52, 52, Image.SCALE_SMOOTH)
					);

					this.icons = new ImageIcon[5];
					for (int i = 0; i < 5; i++) {
						this.icons[i] = new ImageIcon(
							ImageIO.read(resourceStreams[i + 1]).getScaledInstance(52, 52, Image.SCALE_SMOOTH)
						);
					}
					return true;
				} catch (IOException | IllegalArgumentException e) {
					System.err.println("Unable to create images from file.");

					int dialogResult = JOptionPane.showConfirmDialog(
						GUIStarter.this,
						"Problem opening image files.\nPress OK to try again,\nor CANCEL to exit.",
						"",
						JOptionPane.OK_CANCEL_OPTION
					);

					if (dialogResult != JOptionPane.OK_OPTION) {
						return false;
					}
				}
			}
		}

		/**
		 * Creates a JPanel to hold the images representing each square of the board.
		 * The indices of the squares go as follows:
		 * <ul>
		 * <li>(0, 0) is the bottom left
		 * <li>(0, 7) is the top left
		 * <li>(7, 0) is the bottom right
		 * <li>(7, 7) is the top right
		 * </ul>
		 * 
		 * @return The panel 
		 */
		private JPanel generateBoardPanel() {
			JPanel boardPanel = new JPanel(new GridLayout(8, 8));
			this.labels = new JLabel[8][8];
			for (int row = 0; row < 8; row++) {
				for (int column = 7; column >= 0; column--) {
					if ((row + column) % 2 == 0) {
						final JLabel label = new JLabel(this.icons[GREEN_ICON]);
						label.addMouseListener(new MouseListener() {
							@Override
							public void mouseClicked(MouseEvent e) {
								for (int currentIconIndex = 0; currentIconIndex < PiecesPanel.this.icons.length; currentIconIndex++) {
									if (PiecesPanel.this.icons[currentIconIndex] == label.getIcon()) {
										currentIconIndex = ++currentIconIndex % PiecesPanel.this.icons.length;
										label.setIcon(PiecesPanel.this.icons[currentIconIndex]);
										return;
									}
								}
							}
							@Override
							public void mouseEntered(MouseEvent e) {}
							@Override
							public void mouseExited(MouseEvent e) {}
							@Override
							public void mousePressed(MouseEvent e) {}
							@Override
							public void mouseReleased(MouseEvent e) {}
						});
						boardPanel.add(label);
						this.labels[row][column] = label;
					} else {
						JLabel label = new JLabel(this.emptyIcon);
						boardPanel.add(label);
						this.labels[row][column] = label;
					}
				}
			}

			return boardPanel;
		}

		/**
		 * Collects preferences and starting conditions chosen by the user, and uses the 
		 * information to run the game.
		 */
		private void play() {
			StringBuilder color1PBuilder = new StringBuilder("=");
			StringBuilder color2PBuilder = new StringBuilder("=");
			StringBuilder color1KBuilder = new StringBuilder("=");
			StringBuilder color2KBuilder = new StringBuilder("=");

			for (int row = 0; row < 8; row++) {
				for (int column = 0; column < 8; column++) {
					if (PiecesPanel.this.icons[GRAY_PIECE_ICON] == this.labels[row][column].getIcon()) {
						color1PBuilder.append(row);
						color1PBuilder.append(column);
					}
					if (PiecesPanel.this.icons[BLACK_PIECE_ICON] == this.labels[row][column].getIcon()) {
						color2PBuilder.append(row);
						color2PBuilder.append(column);
					}
					if (PiecesPanel.this.icons[GRAY_KING_ICON] == this.labels[row][column].getIcon()) {
						color1KBuilder.append(row);
						color1KBuilder.append(column);
					}
					if (PiecesPanel.this.icons[BLACK_KING_ICON] == this.labels[row][column].getIcon()) {
						color2KBuilder.append(row);
						color2KBuilder.append(column);
					}
				}
			}
			if (this.turnSelectionList.getSelectedIndex() + GUIStarter.this.humanColorList.getSelectedIndex() == 1) {
				StringBuilder holder = color1PBuilder;
				color1PBuilder = color2PBuilder;
				color2PBuilder = holder;
				
				holder = color1KBuilder;
				color1KBuilder = color2KBuilder;
				color2KBuilder = holder;
			}
			GUIStarter.this.execPanel.runMainThread(
				MidgameStarter.class,
				new String[] {
					this.turnSelectionList.getSelectedValue(),
					"p1" + color1PBuilder.toString(),
					"p2" + color2PBuilder.toString(),
					"k1" + color1KBuilder.toString(),
					"k2" + color2KBuilder.toString(),
					GUIStarter.this.robotColorList.getSelectedValue(),
					GUIStarter.this.humanColorList.getSelectedValue(),
					"robot=yes",
					GUIStarter.this.rulesList.getSelectedValue(),
					GUIStarter.this.difficultyList.getSelectedValue()
				}
			);
			GUIStarter.this.mainLayout.last(getContentPane());
			GUIStarter.this.setResizable(true);
			GUIStarter.this.setMinimumSize(GUIStarter.PLAY_SIZE);
			GUIStarter.this.setSize(GUIStarter.CHOOSER_SIZE);
		}
	}
}
