// ALL AARON

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.io.*;;

public class GUIStarter extends JFrame implements ActionListener {

	public static final String COLOR_ONE = "x";
	public static final String COLOR_TWO = "o";
	public static final int MAX_DIFFICULTY = 10;

	private static final Object LOCK = new Object();

	private JPanel _centerPanel;
	private JList<String> _humanColorTable;
	private JList<String> _robotColorTable;
	private JList<String> _difficultyBox;
	private JList<String> _rulesBox;

	private ProcessPanel _resetPanel;

	public GUIStarter() {
		super();
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new CardLayout());
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// GET MAIN PANEL AND SET UP LAYOUT
		this._centerPanel = new JPanel();
		this._centerPanel.setLayout(new GridLayout(4, 1));

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
		this._humanColorTable.setBorder(new EmptyBorder(10, 50, 10, 50));
		humanColorBox.add(this._humanColorTable);
		colorsPanel.add(humanColorBox);

		JPanel robotColorBox = new JPanel();
		robotColorBox.setBorder(new TitledBorder("Robot Color"));
		this._robotColorTable = new JList<String>(new String[] {GUIStarter.COLOR_ONE, GUIStarter.COLOR_TWO});
		this._robotColorTable.setSelectedIndex(1);
		this._robotColorTable.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		this._robotColorTable.setVisibleRowCount(1);
		this._robotColorTable.setBorder(new EmptyBorder(10, 50, 10, 50));
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

		// CREATES PLAY BUTTON
		JButton playButton = new JButton("PLAY");
		playButton.addActionListener(this);
		this._centerPanel.add(playButton);


		// FINISHES UP
		contentPane.add(this._centerPanel, BorderLayout.CENTER);

		this.setSize(480, 480);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.validate();
	}

	public static void main(String[] args) {
		GUIStarter test = new GUIStarter();
		test.setVisible(true);
	}

	public void actionPerformed(ActionEvent ae_) {
		String eventName = ae_.getActionCommand();
		if (eventName.equals("PLAY")) {
			if (this._resetPanel == null)
				this._resetPanel = new ProcessPanel();
				this.getContentPane().add(this._resetPanel);
			this._resetPanel.startProcess(new String[] {"nxjpc",
													  	 "Starter",
														 this._humanColorTable.getSelectedValue(),
														 this._robotColorTable.getSelectedValue(),
														 this._difficultyBox.getSelectedValue(),
														 this._rulesBox.getSelectedValue()
			});
			((CardLayout) this.getContentPane().getLayout()).next(this.getContentPane());
		}
	}

	private class ReaderThread extends Thread {
		private Process _process;
		private JTextArea _textArea;

		public ReaderThread(Process p, JPanel outputArea) {
			this._process = p;

			this._textArea = new JTextArea(20, 30);
			this._textArea.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(this._textArea);
			scrollPane.setViewportBorder(new LineBorder(Color.white));

			outputArea.add(scrollPane);
			if (outputArea.getComponentCount()>1) {
				for (int i = 0; i < outputArea.getComponentCount()-1; i++) {
					outputArea.remove(outputArea.getComponent(i));
				}
			}
		}

		public void run() {
			InputStream input = this._process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			try {
				for (String line; (line = br.readLine()) != null && !this.interrupted();) {
					this._textArea.append(line + "\n");
				}
			} catch (IOException e) {
				System.err.println("Exception reading stdout from process");
				e.printStackTrace();
			}
		}
	}

	private class ProcessPanel extends JPanel implements ActionListener {
		private JButton _resetButton;
		private JPanel _textPanel;

		private Process _subProcess;
		private Thread _listenerThread;
		private ReaderThread _readerThread;

		public ProcessPanel() {
			super();

			// SETS UP NEW UI ELEMENTS
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			this._textPanel = new JPanel();
			this.add(this._textPanel);

			JPanel resetPanel = new JPanel();
			resetPanel.setBorder(new CompoundBorder(new LineBorder(Color.white), new EmptyBorder(1, 2, 1, 2)));
			this._resetButton = new JButton("RESET GAME");
			this._resetButton.addActionListener(this);
			resetPanel.add(this._resetButton, BorderLayout.CENTER);
			this.add(resetPanel);
		}

		public void startProcess(String[] params) {
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
			this._readerThread = new ReaderThread(this._subProcess, this._textPanel);
			this._readerThread.start();

			// STARTS LISTENER THREAD
			this._listenerThread = new Thread() {
				public void run() {
					try {
						_subProcess.waitFor();
					} catch (InterruptedException e) {
						System.err.println("Interrupted waiting for process");
						e.printStackTrace();
					}
					close();
				}
			};
			this._listenerThread.start();
		}

		public void close() {
				if (this._listenerThread != null) {
					this._listenerThread.interrupt();
					try {
						this._listenerThread.join();
					} catch (InterruptedException e) {
						System.err.println("InterruptedException waiting for listener thread");
						e.printStackTrace();
					}
				}
				if (this._readerThread != null) {
					this._readerThread.interrupt();
					try {
						this._readerThread.join();
					} catch (InterruptedException e) {
						System.err.println("InterruptedException waiting for reader thread");
						e.printStackTrace();
					}
				}
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

		public void actionPerformed(ActionEvent ae_) {
			String eventName = ae_.getActionCommand();
			if (eventName.equals("RESET GAME")) {
				this.close();
			}
		}
	}
}
