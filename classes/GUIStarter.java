// ALL AARON

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.io.*;
import java.util.ArrayList;

public class GUIStarter extends JFrame {

	public static final String COLOR_ONE = "black";
	public static final String COLOR_TWO = "gray";
	public static final int MAX_DIFFICULTY = 10;

	private static final Object LOCK = new Object();

	private JPanel _centerPanel;
	private JList<String> _humanColorTable;
	private JList<String> _robotColorTable;
	private JList<String> _difficultyBox;
	private JList<String> _rulesBox;

	private ProcessPanel _resetPanel;
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
		this._centerPanel = new JPanel();
		this._centerPanel.setLayout(new GridLayout(3, 1));

		// CREATES COLOR OPTION BOX
		JPanel colorsPanel = new JPanel();
		colorsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 1));
		colorsPanel.setBorder(new CompoundBorder(new TitledBorder("Choose your Color"), new EmptyBorder(10, 40, 10, 40)));


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
		JPanel rulesPanel = new JPanel();
		rulesPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		rulesPanel.setBorder(new CompoundBorder(new TitledBorder("Choose the Rule Type"), new EmptyBorder(10, 20, 10, 20)));
		this._rulesBox = new JList<String>(new String[] {"Official", "Unofficial"});
		this._rulesBox.setSelectedIndex(1);
		this._rulesBox.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		this._rulesBox.setVisibleRowCount(1);
		rulesPanel.add(this._rulesBox);
		this._centerPanel.add(rulesPanel);

		// CREATES BUTTONS
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		JButton playButton = new JButton("PLAY NEW GAME");
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae_) {
				if (_resetPanel == null) {
					_resetPanel = new ProcessPanel();
					getContentPane().add(_resetPanel);
				}
				_resetPanel.startProcess(new String[] {"nxjpc",
													   "Starter",
													   _humanColorTable.getSelectedValue(),
													   _robotColorTable.getSelectedValue(),
													   _difficultyBox.getSelectedValue(),
													   _rulesBox.getSelectedValue()
				});
				((CardLayout) getContentPane().getLayout()).last(getContentPane());
			}
		});
		buttonPanel.add(playButton, BorderLayout.CENTER);

		JButton inputPiecesButton = new JButton("CHOOSE STARTING POSITIONS");
		inputPiecesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae_) {
				if (_piecesPanel == null) {
					_piecesPanel = new PiecesPanel();
					getContentPane().add(_piecesPanel, 1);
				}
				((CardLayout) getContentPane().getLayout()).next(getContentPane());
			}
		});
		buttonPanel.add(inputPiecesButton, BorderLayout.EAST);

		// FINISHES UP
		mainPanel.add(this._centerPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		contentPane.add(mainPanel);

		this.setMinimumSize(new Dimension(480, 480));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.validate();
	}

	public static void main(String[] args) {
		GUIStarter test = new GUIStarter();
		test.setVisible(true);
	}

	private class ProcessPanel extends JPanel {
		private JButton _resetButton;
		private JPanel _textPanel;

		private Process _subProcess;
		private Thread _readerThread;

		public ProcessPanel() {
			super();

			// SETS UP NEW UI ELEMENTS
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			this._textPanel = new JPanel();
			this.add(this._textPanel);

			JPanel resetPanel = new JPanel();
			resetPanel.setBorder(new CompoundBorder(new LineBorder(Color.white), new EmptyBorder(1, 2, 1, 2)));
			this._resetButton = new JButton("RESET GAME");
			this._resetButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae_) {
					close();
				}
			});
			resetPanel.add(this._resetButton, BorderLayout.CENTER);
			this.add(resetPanel);
		}

		public void startProcess(String[] params) {
			JOptionPane.showMessageDialog(this, "Make sure the robot is as far back\nand to the human's right as\npossible before pressing 'OK'.", "Message", JOptionPane.WARNING_MESSAGE);

			try {
				// CREATES STARTER SUBPROCESS
				ProcessBuilder pb = new ProcessBuilder(params);
				pb.redirectErrorStream(true);
				this._subProcess = pb.start();
			} catch (IOException e) {
				System.err.println("Subprocess creation failed with IOException");
				e.printStackTrace();
				return;
			}

			// STARTS DISPLAY THREAD
			final JTextArea textArea = new JTextArea(20, 30);
			textArea.setEditable(false);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			JScrollPane scrollPane = new JScrollPane(textArea);
			scrollPane.setViewportBorder(new LineBorder(Color.white));

			this._textPanel.add(scrollPane);
			if (this._textPanel.getComponentCount()>1) {
				for (int i = 0; i < this._textPanel.getComponentCount()-1; i++) {
					this._textPanel.remove(this._textPanel.getComponent(i));
				}
			}

			this._readerThread = new Thread() {
				public void run() {
					InputStream input = _subProcess.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(input));
					try {
						for (String line; (line = br.readLine()) != null && !this.interrupted();) {
							textArea.append(line + "\n");
						}
					} catch (IOException e) {
						System.err.println("Exception reading stdout from process");
						e.printStackTrace();
					}
				}
			};
			this._readerThread.start();
		}

		public void close() {
			if (this._subProcess != null) {
				this._subProcess.destroy();
				try {
					this._subProcess.waitFor();
				} catch (InterruptedException e) {
					System.err.println("InterruptedException waiting for subprocess");
					e.printStackTrace();
				}
			}
			((CardLayout) this.getRootPane().getContentPane().getLayout()).next(this.getRootPane().getContentPane());
		}
	}

	private class PiecesPanel extends JPanel {
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

			JPanel listPanel = new JPanel();

			JPanel tp1 = new JPanel(new BorderLayout());
			JPanel tp2 = new JPanel(new BorderLayout());
			JPanel tp3 = new JPanel(new BorderLayout());
			JPanel tp4 = new JPanel(new BorderLayout());

			final JTextArea ta1 = new JTextArea(10, 7);
			final JTextArea ta2 = new JTextArea(10, 7);
			final JTextArea ta3 = new JTextArea(10, 7);
			final JTextArea ta4 = new JTextArea(10, 7);

			ta1.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			ta2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			ta3.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			ta4.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

			JScrollPane sp1 = new JScrollPane(ta1);
			JScrollPane sp2 = new JScrollPane(ta2);
			JScrollPane sp3 = new JScrollPane(ta3);
			JScrollPane sp4 = new JScrollPane(ta4);

			JTextArea titleArea1 = new JTextArea("Robot\nPieces");
			JTextArea titleArea2 = new JTextArea("Player\nPieces");
			JTextArea titleArea3 = new JTextArea("Robot\nKings");
			JTextArea titleArea4 = new JTextArea("Player\nKings");

			titleArea1.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			titleArea2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			titleArea3.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			titleArea4.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

			titleArea1.setEditable(false);
			titleArea2.setEditable(false);
			titleArea3.setEditable(false);
			titleArea4.setEditable(false);

			tp1.add(titleArea1, BorderLayout.NORTH);
			tp2.add(titleArea2, BorderLayout.NORTH);
			tp3.add(titleArea3, BorderLayout.NORTH);
			tp4.add(titleArea4, BorderLayout.NORTH);

			tp1.add(sp1, BorderLayout.CENTER);
			tp2.add(sp2, BorderLayout.CENTER);
			tp3.add(sp3, BorderLayout.CENTER);
			tp4.add(sp4, BorderLayout.CENTER);

			listPanel.add(tp1);
			listPanel.add(tp2);
			listPanel.add(tp3);
			listPanel.add(tp4);

			this.add(listPanel, BorderLayout.CENTER);

			JPanel buttonPanel = new JPanel(new BorderLayout());

			JButton returnButton = new JButton("RETURN TO MENU");
			returnButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae_) {
					((CardLayout) getContentPane().getLayout()).first(getContentPane());
				}
			});
			buttonPanel.add(returnButton, BorderLayout.WEST);

			JButton playButton = new JButton("PLAY GAME");
			playButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae_) {
					if (_resetPanel == null) {
						_resetPanel = new ProcessPanel();
						getContentPane().add(_resetPanel);
					}

					ArrayList<String> usedLocations = new ArrayList<String>();

					StringBuilder p1LBuilder = new StringBuilder("p{");
					for (String line : ta1.getText().split("\n")) {
						if (line.trim().isEmpty()) {
							continue;
						}
						if (line.matches(" *[0-7] *[0-7] *") && (Integer.parseInt(new String(new char[] {line.trim().charAt(0)}))+Integer.parseInt(new String(new char[] {line.trim().charAt(line.trim().length()-1)})))%2==0) {
							if (usedLocations.contains(new String(new char[] {line.trim().charAt(0), line.trim().charAt(line.trim().length()-1)}))) {
								JOptionPane.showMessageDialog(PiecesPanel.this, "Duplicate location used.", "", JOptionPane.ERROR_MESSAGE);
								return;
							}
							usedLocations.add(new String(new char[] {line.trim().charAt(0), line.trim().charAt(line.trim().length()-1)}));
							p1LBuilder.append(line.trim().charAt(0));
							p1LBuilder.append(line.trim().charAt(line.trim().length()-1));
						} else {
							JOptionPane.showMessageDialog(PiecesPanel.this, "Something isn't formatted correctly in the\nrobot pieces list.  Check if you have a wrong\nnumber somewhere in there.", "", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					p1LBuilder.append("}");
					StringBuilder p2LBuilder = new StringBuilder("p{");
					for (String line : ta2.getText().split("\n")) {
						if (line.trim().isEmpty()) {
							continue;
						}
						if (line.matches(" *[0-7] *[0-7] *") && (Integer.parseInt(new String(new char[] {line.trim().charAt(0)}))+Integer.parseInt(new String(new char[] {line.trim().charAt(line.trim().length()-1)})))%2==0) {
							if (usedLocations.contains(new String(new char[] {line.trim().charAt(0), line.trim().charAt(line.trim().length()-1)}))) {
								JOptionPane.showMessageDialog(PiecesPanel.this, "Duplicate location used.", "", JOptionPane.ERROR_MESSAGE);
								return;
							}
							usedLocations.add(new String(new char[] {line.trim().charAt(0), line.trim().charAt(line.trim().length()-1)}));
							p2LBuilder.append(line.trim().charAt(0));
							p2LBuilder.append(line.trim().charAt(line.trim().length()-1));
						} else {
							JOptionPane.showMessageDialog(PiecesPanel.this, "Something isn't formatted correctly in the\nplayer pieces list.  Check if you have a wrong\nnumber somewhere in there.", "", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					p2LBuilder.append("}");
					StringBuilder p1KBuilder = new StringBuilder("k{");
					for (String line : ta3.getText().split("\n")) {
						if (line.trim().isEmpty()) {
							continue;
						}
						if (line.matches(" *[0-7] *[0-7] *") && (Integer.parseInt(new String(new char[] {line.trim().charAt(0)}))+Integer.parseInt(new String(new char[] {line.trim().charAt(line.trim().length()-1)})))%2==0) {
							if (usedLocations.contains(new String(new char[] {line.trim().charAt(0), line.trim().charAt(line.trim().length()-1)}))) {
								JOptionPane.showMessageDialog(PiecesPanel.this, "Duplicate location used.", "", JOptionPane.ERROR_MESSAGE);
								return;
							}
							usedLocations.add(new String(new char[] {line.trim().charAt(0), line.trim().charAt(line.trim().length()-1)}));
							p1KBuilder.append(line.trim().charAt(0));
							p1KBuilder.append(line.trim().charAt(line.trim().length()-1));
						} else {
							JOptionPane.showMessageDialog(PiecesPanel.this, "Something isn't formatted correctly in the\nrobot kings list.  Check if you have a wrong\nnumber somewhere in there.", "", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					p1KBuilder.append("}");
					StringBuilder p2KBuilder = new StringBuilder("k{");
					for (String line : ta4.getText().split("\n")) {
						if (line.trim().isEmpty()) {
							continue;
						}
						if (line.matches(" *[0-7] *[0-7] *") && (Integer.parseInt(new String(new char[] {line.trim().charAt(0)}))+Integer.parseInt(new String(new char[] {line.trim().charAt(line.trim().length()-1)})))%2==0) {
							if (usedLocations.contains(new String(new char[] {line.trim().charAt(0), line.trim().charAt(line.trim().length()-1)}))) {
								JOptionPane.showMessageDialog(PiecesPanel.this, "Duplicate location used.", "", JOptionPane.ERROR_MESSAGE);
								return;
							}
							usedLocations.add(new String(new char[] {line.trim().charAt(0), line.trim().charAt(line.trim().length()-1)}));
							p2KBuilder.append(line.trim().charAt(0));
							p2KBuilder.append(line.trim().charAt(line.trim().length()-1));
						} else {
							JOptionPane.showMessageDialog(PiecesPanel.this, "Something isn't formatted correctly in the\nplayer kings list.  Check if you have a wrong\nnumber somewhere in there.", "", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					p2KBuilder.append("}");

					if (playerBox.getSelectedIndex()!=0) {
						StringBuilder holder = p1LBuilder;
						p1LBuilder = p2LBuilder;
						p2LBuilder = holder;
						
						holder = p1KBuilder;
						p1KBuilder = p2KBuilder;
						p2KBuilder = holder;
					}

					_resetPanel.startProcess(new String[] {"nxjpc",
														   "MidgameStarter",
														   playerBox.getSelectedValue(),
														   p1LBuilder.toString(),
														   p2LBuilder.toString(),
														   p1KBuilder.toString(),
														   p2KBuilder.toString(),
														   _humanColorTable.getSelectedValue(),
														   _robotColorTable.getSelectedValue(),
														   _difficultyBox.getSelectedValue(),
														   _rulesBox.getSelectedValue()
					});
					((CardLayout) getContentPane().getLayout()).last(getContentPane());
				}
			});
			buttonPanel.add(playButton, BorderLayout.CENTER);

			this.add(buttonPanel, BorderLayout.SOUTH);
		}
	}
}
