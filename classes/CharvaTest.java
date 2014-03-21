import charva.awt.*;
import charva.awt.event.*;
import charvax.swing.*;
import charvax.swing.border.*;
import charvax.swing.event.*;

import java.io.*;;

public class CharvaTest extends JFrame implements ActionListener {

	public static final String COLOR_ONE = "x";
	public static final String COLOR_TWO = "o";
	public static final int MAX_DIFFICULTY = 10;

	private JPanel _centerPanel;
	private JSideList _humanColorTable;
	private JSideList _robotColorTable;
	private JSideList _difficultyBox;
	private JSideList _rulesBox;

	private JFrame _resetWindow;

	private Process _subProcess;
	private Thread _listenerThread;
	private JPanel _textPanel;

	public CharvaTest() {
		super();
		this.setForeground(Color.green);
		this.setBackground(Color.black);
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		// GET MAIN PANEL AND SET UP LAYOUT
		this._centerPanel = new JPanel();
		this._centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(3, 0, 3, 0);

		// CREATES COLOR OPTION BOX
		JPanel colorsPanel = new JPanel();
		colorsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 1));
		colorsPanel.setBorder(new CompoundBorder(new TitledBorder("Choose your Color"), new EmptyBorder(1, 4, 1, 4)));


		JPanel humanColorBox = new JPanel();
		humanColorBox.setBorder(new TitledBorder("Human Color"));
		this._humanColorTable = new JSideList(new String[] {CharvaTest.COLOR_ONE, CharvaTest.COLOR_TWO}, 0, 3, Color.cyan);
		this._humanColorTable.setBorder(new EmptyBorder(1, 2, 1, 2));
		humanColorBox.add(this._humanColorTable);
		colorsPanel.add(humanColorBox);

		JPanel robotColorBox = new JPanel();
		robotColorBox.setBorder(new TitledBorder("Robot Color"));
		this._robotColorTable = new JSideList(new String[] {CharvaTest.COLOR_ONE, CharvaTest.COLOR_TWO}, 1, 3, Color.cyan);
		this._robotColorTable.setBorder(new EmptyBorder(1, 2, 1, 2));
		this._robotColorTable.setEnabled(false);
		robotColorBox.add(this._robotColorTable);
		colorsPanel.add(robotColorBox);

		this._centerPanel.add(colorsPanel, gbc);

		final JSideList humanCL = this._humanColorTable;
		final JSideList robotCL = this._robotColorTable;
		this._humanColorTable.addSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt_) {
				robotCL.processKeyEvent(new KeyEvent(humanCL.getSelectedItem()==CharvaTest.COLOR_ONE ? KeyEvent.VK_RIGHT : KeyEvent.VK_LEFT,
													 KeyEvent.KEY_TYPED,
													 robotCL));
			}
		});

		// CREATES DIFFICULTY OPTION BOX
		gbc.gridy++;
		String[] numList = new String[MAX_DIFFICULTY];
		for (int i = 0; i < MAX_DIFFICULTY; i++) {
			numList[i] = String.valueOf(i+1);
		}
		this._difficultyBox = new JSideList(numList, 4, 3, Color.cyan);
		this._difficultyBox.setBorder(new CompoundBorder(new TitledBorder("Choose the Difficulty"), new EmptyBorder(1, 3, 1, 3)));
		this._centerPanel.add(this._difficultyBox, gbc);

		// CREATES RULE TYPE OPTION BOX
		gbc.gridy++;
		this._rulesBox = new JSideList(new String[] {"Official", "Unofficial"}, 1, 4, Color.cyan);
		this._rulesBox.setBorder(new CompoundBorder(new TitledBorder("Choose the Rule Type"), new EmptyBorder(1, 3, 1, 3)));
		this._centerPanel.add(this._rulesBox, gbc);

		// CREATES PLAY BUTTON
		gbc.gridy++;
		JButton playButton = new JButton("PLAY");
		playButton.addActionListener(this);
		this._centerPanel.add(playButton, gbc);


		// FINISHES UP
		contentPane.add(this._centerPanel, BorderLayout.CENTER);

		this.setLocation(0,0);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.validate();
	}

	public static void main(String[] args) {
		CharvaTest test = new CharvaTest();
		test.show();
	}

	public void processEvent(AWTEvent evt_) {
		if (evt_ instanceof KeyEvent && ((KeyEvent) evt_).getKeyCode() == 101) {
			// System.err.println(((KeyEvent) evt_).getKeyCode());
			System.gc();	// so that HPROF reports only live objects.
			System.exit(0);
		}

		super.processEvent(evt_);
	}

	public void actionPerformed(ActionEvent ae_) {
		String eventName = ae_.getActionCommand();
		if (eventName.equals("PLAY")) {
			try {
				// CREATES STARTER SUBPROCESS
				ProcessBuilder pb = new ProcessBuilder(new String[] {
													  "java",
													  "Starter",
													  this._humanColorTable.getSelectedItem(),
													  this._robotColorTable.getSelectedItem(),
													  this._difficultyBox.getSelectedItem(),
													  this._rulesBox.getSelectedItem()
				});
				pb.redirectErrorStream(true);
				this._subProcess = pb.start();
			} catch (IOException e) {
				System.err.println("Subprocess creation failed with IOException");
				return;
			}

			// SETS UP NEW UI ELEMENTS
			if (this._resetWindow == null) {
				this._resetWindow = new JFrame();
				this._resetWindow.setForeground(Color.green);
				this._resetWindow.setBackground(Color.black);
				JPanel resetPanel = new JPanel();
				resetPanel.setBorder(new CompoundBorder(new LineBorder(Color.white), new EmptyBorder(2, 4, 2, 4)));
				JButton resetButton = new JButton("RESET GAME");
				resetButton.addActionListener(this);
				resetPanel.add(resetButton, BorderLayout.CENTER);
				this._resetWindow.getContentPane().setLayout(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridy++;
				this._resetWindow.getContentPane().add(resetPanel, gbc);
				this._resetWindow.setLocation(0, 0);
				this._resetWindow.setSize(Toolkit.getDefaultToolkit().getScreenSize());

				this._textPanel = new JPanel();
				gbc.gridy--;
				this._resetWindow.getContentPane().add(this._textPanel, gbc);
			}

			// STARTS LISTENER THREAD
			this._listenerThread = new ListenerThread(this._subProcess, this._textPanel);
			this._listenerThread.start();
			
			this._resetWindow.show();

		} else if (eventName.equals("RESET GAME")) {
			if (this._listenerThread != null)
				this._listenerThread.interrupt();
			this._subProcess.destroy();
			this._resetWindow.hide();
		}
	}

	private class ListenerThread extends Thread {
		private Process _process;
		private JTextArea _textArea;

		public ListenerThread(Process p, JPanel outputArea) {
			this._process = p;

			this._textArea = new JTextArea("", 15, 20);
			this._textArea.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(this._textArea);
			scrollPane.setViewportBorder(new LineBorder(Color.white));

			outputArea.add(scrollPane);
		}

		public void run() {
			InputStream input = this._process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			try {
				for (String line; (line = br.readLine()) != null;) {
					this._textArea.append(line + "\n");
				}
			} catch (IOException e) {
				//pass
			}
			EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
			queue.postEvent(new ActionEvent(new JButton(), "RESET GAME"));
		}
	}

	private class JSideList extends JComponent /*implements FocusListener*/ {

		private String[] _items;
		private int _currentItem;
		// private int _selectedIndex;
		private int _interimSpace;
		private DefaultListSelectionModel _model;
		private Color _highlightColor;

		public JSideList(String[] items, int initialSelection, int interimSpace, Color highlightColor) {
			this._items = items;
			this._currentItem = ((initialSelection>=0 ? initialSelection : 0)<items.length ? (initialSelection>=0 ? initialSelection : 0) : items.length-1);
			// this._selectedIndex = 0;
			this._interimSpace = interimSpace;
			this._model = new DefaultListSelectionModel();
			this._model.setSelectionInterval(this._currentItem, this._currentItem);

			this._highlightColor = highlightColor;

			// this.addFocusListener(this);
		}

		public String getSelectedItem() {
			return this._items[this._model.getMaxSelectionIndex()];
		}

		public void addSelectionListener(ListSelectionListener l_) {
			this._model.addListSelectionListener(l_);
		}

		public void requestFocus() {
			/* Generate the FOCUS_GAINED event.
			 */
			super.requestFocus();

			/* Get the absolute origin of this component
			 */
			Point origin = this.getLocationOnScreen();
			Insets insets = super.getInsets();
			origin.translate(insets.left, insets.top);
			
			int columns = 0;
			for (int i = 0; i < this._currentItem; i++) {
				columns += this._items[i].length();
			}
			Toolkit.getDefaultToolkit().setCursor(origin.addOffset(columns + this._currentItem*this._interimSpace, 0));
		}

		// public void focusGained(FocusEvent fe_) {
		// }

		// public void focusLost(FocusEvent fe_) {
		// 	this._currentItem = this._selectedIndex;
		// }

		public void draw() {

			/* Draw the border if it exists
			 */
			super.draw();

			/* Get the absolute origin of this component.
			 */
			Point origin = this.getLocationOnScreen();
			Insets insets = super.getInsets();
			origin.translate(insets.left, insets.top);

			Toolkit term = Toolkit.getDefaultToolkit();

			int colorpair = this.getCursesColor();
			int attribute;

			int columns = 0;
			
			for (int i = 0; i < this._items.length; i++) {
				term.setCursor(origin.x + columns + i*this._interimSpace, origin.y);

				// if (i == this._currentItem)
				// 	attribute = Toolkit.A_REVERSE;
				// else
					attribute = 0;

				if (i == this._currentItem) {
					attribute += Toolkit.A_BOLD;
					Color prevColor = this.getForeground();
					this.setForeground(this._highlightColor);
					int highlightColorPair = this.getCursesColor();
					term.addString(this._items[i], attribute, highlightColorPair);
					this.setForeground(prevColor);
				} else {
					term.addString(this._items[i], attribute, colorpair);
				}

				columns += this._items[i].length();
			}
		}

		public void processKeyEvent(KeyEvent ke_) {
			/* First call all KeyListener objects that may have been registered
			 * for this component.
			 */
			super.processKeyEvent(ke_);

			/* Check if any of the KeyListeners consumed the KeyEvent.
			 */
			if (ke_.isConsumed())
				return;

			int key = ke_.getKeyCode();
			switch (key) {
				case '\t':
					this.getParent().nextFocus();
					return;

				case KeyEvent.VK_BACK_TAB:
					this.getParent().previousFocus();
					return;

				case KeyEvent.VK_RIGHT:
					/* If we are already at the bottom of the list, ignore
					 * this keystroke.
					 */
					if (this._currentItem >= this._items.length - 1)
						return;

					this._currentItem++;
					this._model.setSelectionInterval(this._currentItem, this._currentItem);
					break;

				case KeyEvent.VK_LEFT:
					/* If we are already at the top of the list, ignore
					 * this keystroke.
					 */
					if (this._currentItem < 1)
						return;

					this._currentItem--;
					this._model.setSelectionInterval(this._currentItem, this._currentItem);
					break;

				// case KeyEvent.VK_ENTER:
				// 	this._selectCurrent();
				// 	break;
			}

			this.draw();
			this.requestFocus();
			super.requestSync();
		}

		// private void _selectCurrent() {
		// 	this._selectedIndex = this._currentItem;
		// 	this.repaint();
		// }

		public void debug(int level_) {
			for (int i = 0; i < level_; i++)
				System.err.print("	");
			System.err.println("JList origin=" + _origin + " size=" + minimumSize());
		}

		public Dimension minimumSize() {
			/* Take into account the border inherited from the JComponent
			 * superclass.
			 */
			Insets insets = super.getInsets();
			int columns = 0;
			for (int i = 0; i < this._items.length; i++) {
				columns += this._items[i].length();
			}
			return new Dimension(columns + this._interimSpace*(this._items.length - 1) + insets.left + insets.right, 1 + insets.top + insets.bottom);
		}

		public Dimension getSize() {
			return new Dimension(this.getWidth(), this.getHeight());
		}

		public int getWidth() {
			Insets insets = super.getInsets();
			int columns = 0;
			for (int i = 0; i < this._items.length; i++) {
				columns += this._items[i].length();
			}
			return columns + this._interimSpace*(this._items.length - 1) + insets.left + insets.right;
		}

		public int getHeight() {
			Insets insets = super.getInsets();
			return 1 + insets.top + insets.bottom;
		}
	}
}
