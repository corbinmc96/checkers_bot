import charva.awt.*;
import charva.awt.event.*;
import charvax.swing.*;
import charvax.swing.border.*;

public class CharvaTest extends JFrame implements ActionListener {

	public static final String COLOR_ONE = "Red";
	public static final String COLOR_TWO = "Black";

	public CharvaTest() {
		super();
        this.setForeground(Color.green);
        this.setBackground(Color.black);
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());

        JMenuBar menubar = new JMenuBar();
        JMenu jMenuFile = new JMenu("File");
        jMenuFile.setMnemonic('F');

        JMenuItem jMenuItemFileExit = new JMenuItem("Exit", 'x');
        jMenuItemFileExit.addActionListener(this);
        jMenuFile.add(jMenuItemFileExit);

        menubar.add(jMenuFile);
        this.setJMenuBar(menubar);

        JPanel centerPanel = new JPanel();
        centerPanel.setBorder(new TitledBorder("Main panel"));
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(3, 0, 3, 0);

        // CREATES COLOR OPTION BOX
        JPanel colorsPanel = new JPanel();
        colorsPanel.setBorder(new TitledBorder(new CompoundBorder(new LineBorder(Color.white), new EmptyBorder(3, 3, 3, 3)), "Choose your Color"));


        JPanel humanColorBox = new JPanel();
        humanColorBox.setBorder(new TitledBorder("Human Color"));
        JSideList humanColorTable = new JSideList(new String[] {CharvaTest.COLOR_ONE, CharvaTest.COLOR_TWO}, 7);
        humanColorBox.add(humanColorTable);
        colorsPanel.add(humanColorBox);

        JPanel robotColorBox = new JPanel();
        robotColorBox.setBorder(new TitledBorder("Robot Color"));
        JSideList robotColorTable = new JSideList(new String[] {CharvaTest.COLOR_ONE, CharvaTest.COLOR_TWO}, 7);
        robotColorBox.add(robotColorTable);
        colorsPanel.add(robotColorBox);

        centerPanel.add(colorsPanel, gbc);

        // CREATES DIFFICULTY OPTION BOX
        gbc.gridy++;
        centerPanel.add(new JTextField("JTextField #2."), gbc);

        // CREATES RULE TYPE OPTION BOX
        gbc.gridy++;
        centerPanel.add(new JTextField("JTextField #3."), gbc);

        // CREATES PLAY BUTTON
        gbc.gridy++;
        centerPanel.add(new JTextField("JTextField #4."), gbc);

        contentPane.add(centerPanel, BorderLayout.CENTER);


        this.setLocation(0,0);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.validate();
	}

	public static void main(String[] args) {
		CharvaTest test = new CharvaTest();
		test.show();
	}

	public void actionPerformed(ActionEvent ae_) {
        String actionCommand = ae_.getActionCommand();
        if (actionCommand.equals("Exit")) {
            System.gc();	// so that HPROF reports only live objects.
            System.exit(0);
        } else {
            JOptionPane.showMessageDialog(this, "Menu item \"" + actionCommand +
                    "\" not implemented yet",
                    "Error",
                    JOptionPane.PLAIN_MESSAGE);
        }
        // Trigger garbage-collection after every menu action.
        Toolkit.getDefaultToolkit().triggerGarbageCollection(this);
    }

	private class JSideList extends JComponent {
		private Object[] _items;
		private int _currentItem;
		private int _selectedIndex;
		private int _itemWidth;

		public JSideList(Object[] items, int columnWidth) {
			this._items = items;
			this._currentItem = 0;
			this._selectedIndex = 0;
			this._itemWidth = columnWidth;
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
	        Toolkit.getDefaultToolkit().setCursor(origin.addOffset(this._currentItem*this._itemWidth, 0));
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

	        for (int i = 0; i < this._items.length; i++) {

	            term.setCursor(origin.x + i*this._itemWidth, origin.y);

	            if (i == this._selectedIndex)
	                attribute = Toolkit.A_REVERSE;
	            else
	                attribute = 0;

	            if (i == this._currentItem)
	                attribute += Toolkit.A_BOLD;

	            String item = this._items[i].toString();

	            StringBuffer buffer = new StringBuffer(item);
	            for (int j = item.length(); j < this._itemWidth; j++)
	                buffer.append(' ');

	            term.addString(buffer.toString(), attribute, colorpair);
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

	        Toolkit term = Toolkit.getDefaultToolkit();
	        EventQueue evtqueue = term.getSystemEventQueue();
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
	            System.err.print("    ");
	        System.err.println("JList origin=" + _origin +
	                " size=" + minimumSize());
	    }

	    public Dimension minimumSize() {
	        /* Take into account the border inherited from the JComponent
	         * superclass.
	         */
	        Insets insets = super.getInsets();
	        return new Dimension(this._itemWidth*this._items.length + insets.left + insets.right,
	                1 + insets.top + insets.bottom);
	    }

	    public Dimension getSize() {
	        return new Dimension(this.getWidth(), this.getHeight());
	    }

	    public int getWidth() {
	        Insets insets = super.getInsets();
	        return this._itemWidth*this._items.length + insets.left + insets.right;
	    }

	    public int getHeight() {
	        Insets insets = super.getInsets();
	        return 1 + insets.top + insets.bottom;
	    }

	}
}
