import charva.awt.*;
import charva.awt.event.*;
import charvax.swing.*;
import charvax.swing.border.*;

public class CharvaTest extends JFrame {

	public static final String COLOR_ONE = "Red";
	public static final String COLOR_TWO = "Black";
	public static final int MAX_DIFFICULTY = 10;

	public CharvaTest() {
		super();
		this.setForeground(Color.green);
		this.setBackground(Color.black);
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		// GET MAIN PANEL AND SET UP LAYOUT
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
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
		JSideList humanColorTable = new JSideList(new String[] {CharvaTest.COLOR_ONE, CharvaTest.COLOR_TWO}, 3);
		humanColorTable.setBorder(new EmptyBorder(1, 2, 1, 2));
		humanColorBox.add(humanColorTable);
		colorsPanel.add(humanColorBox);

		JPanel robotColorBox = new JPanel();
		robotColorBox.setBorder(new TitledBorder("Robot Color"));
		JSideList robotColorTable = new JSideList(new String[] {CharvaTest.COLOR_ONE, CharvaTest.COLOR_TWO}, 3);
		robotColorTable.setBorder(new EmptyBorder(1, 2, 1, 2));
		robotColorTable.setEnabled(false);
		robotColorBox.add(robotColorTable);
		colorsPanel.add(robotColorBox);

		centerPanel.add(colorsPanel, gbc);

		// CREATES DIFFICULTY OPTION BOX
		gbc.gridy++;
		String[] numList = new String[MAX_DIFFICULTY];
		for (int i = 0; i < MAX_DIFFICULTY; i++) {
			numList[i] = String.valueOf(i+1);
		}
		JSideList difficultyBox = new JSideList(numList, 3);
		difficultyBox.setBorder(new CompoundBorder(new TitledBorder("Choose the Difficulty"), new EmptyBorder(1, 3, 1, 3)));
		centerPanel.add(difficultyBox, gbc);

		// CREATES RULE TYPE OPTION BOX
		gbc.gridy++;
		JSideList rulesBox = new JSideList(new String[] {"Official", "Unofficial"}, 4);
		rulesBox.setBorder(new CompoundBorder(new TitledBorder("Choose the Rule Type"), new EmptyBorder(1, 3, 1, 3)));
		centerPanel.add(rulesBox, gbc);

		// CREATES PLAY BUTTON
		gbc.gridy++;
		centerPanel.add(new JButton("PLAY"), gbc);

		// FINISHES UP
		contentPane.add(centerPanel, BorderLayout.CENTER);

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

	private class JSideList extends JComponent {
		private String[] _items;
		private int _currentItem;
		private int _selectedIndex;
		private int _interimSpace;

		public JSideList(String[] items, int interimSpace) {
			this._items = items;
			this._currentItem = 0;
			this._selectedIndex = 0;
			this._interimSpace = interimSpace;
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
			for (int i = 0; i < this._selectedIndex; i++) {
				columns += this._items[i].length();
			}
			Toolkit.getDefaultToolkit().setCursor(origin.addOffset(columns + this._selectedIndex*this._interimSpace, 0));
		}

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

				if (i == this._selectedIndex)
					attribute = Toolkit.A_REVERSE;
				else
					attribute = 0;

				if (i == this._currentItem)
					attribute += Toolkit.A_BOLD;

				term.addString(this._items[i], attribute, colorpair);

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
					break;

				case KeyEvent.VK_LEFT:
					/* If we are already at the top of the list, ignore
					 * this keystroke.
					 */
					if (this._currentItem < 1)
						return;

					this._currentItem--;
					break;

				case KeyEvent.VK_ENTER:
					this._selectCurrent();
					break;
			}

			if ((this.getParent() instanceof JViewport) == false) {
				this.draw();
				this.requestFocus();
				super.requestSync();
			}
		}

		private void _selectCurrent() {
			/* Pressing ENTER or double-clicking on a row selects/deselects
			 * the current row. If the list is empty, ignore the keystroke.
			 */
			this._selectedIndex = this._currentItem;
			this.repaint();
		}

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
