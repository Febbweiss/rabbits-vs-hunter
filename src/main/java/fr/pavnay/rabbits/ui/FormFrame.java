package fr.pavnay.rabbits.ui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.NumberFormatter;

import fr.pavnay.rabbits.engine.HuntEngine;
import fr.pavnay.rabbits.model.Forest;
import fr.pavnay.rabbits.model.Hunter;

/**
 * 
 * The entry point in which forest area, trees, burrows and rabbit numbers are set. 
 *
 */
public class FormFrame extends JFrame implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 4594334659003442873L;

	private JFormattedTextField areaInput; // Only number with specificities (min, max)
	private JFormattedTextField treeInput;
	private JFormattedTextField rabbitInput;
	private JFormattedTextField burrowInput;

	private JButton validateBtn;

	public FormFrame() {
		setTitle("Rabbits vs Hunter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new GridBagLayout());
		UiUtils.add(this, new JLabel("Area"), 0, 0, 1, 1);
		areaInput = new JFormattedTextField(getFormatter(1, 10));
		areaInput.setValue(10);
		areaInput.addActionListener(this);
		UiUtils.add(this, areaInput, 1, 0, 1, 1);
		UiUtils.add(this, new JLabel("Trees"), 0, 1, 1, 1);
		
		treeInput = new JFormattedTextField(getFormatter(0, 300));
		treeInput.setValue(20);
		treeInput.addActionListener(this);
		UiUtils.add(this, treeInput, 1, 1, 1, 1);
		UiUtils.add(this, new JLabel("Rabbits"), 0, 2, 1, 1);
		rabbitInput = new JFormattedTextField(getFormatter(1, 20));
		rabbitInput.setValue(5);
		rabbitInput.addActionListener(this);
		UiUtils.add(this, rabbitInput, 1, 2, 1, 1);
		UiUtils.add(this, new JLabel("Burrows"), 0, 3, 1, 1);
		burrowInput = new JFormattedTextField(getFormatter(1, 20));
		burrowInput.setValue(10);
		burrowInput.addPropertyChangeListener(this);
		UiUtils.add(this, burrowInput, 1, 3, 1, 1);

		validateBtn = new JButton("Validate");
		validateBtn.addActionListener(this);
		
		UiUtils.add(this, validateBtn, 0, 4, 2, 1);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private NumberFormatter getFormatter( int min, int max) {
		NumberFormat format = NumberFormat.getInstance();
	    NumberFormatter formatter = new NumberFormatter(format);
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(min);
	    formatter.setMaximum(max);
	    formatter.setAllowsInvalid(false);
	    return formatter;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if( Integer.parseInt(burrowInput.getText()) < Integer.parseInt(rabbitInput.getText()) ) {
			JOptionPane.showMessageDialog(this, "Add some burrows. Greater or equals than rabbits count.", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			Forest forest = new Forest(Integer.parseInt(areaInput.getText()), Integer.parseInt(treeInput.getText()), 
					Integer.parseInt(burrowInput.getText()), Integer.parseInt(rabbitInput.getText()));
			
			Random rand = new Random();
			Hunter hunter = new Hunter(new Point(rand.nextInt(forest.getEdgeArea()), rand.nextInt(forest.getEdgeArea())), 20);
			hunter.setDestination(new Point(rand.nextInt(forest.getEdgeArea()), rand.nextInt(forest.getEdgeArea())));
			HuntEngine engine = new HuntEngine(forest, hunter);
			
			new MainFrame(engine);
			dispose();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		validateBtn.setEnabled(!"".equals(areaInput.getText()) && !"".equals(treeInput.getText()) 
				&& !"".equals(rabbitInput.getText()) && !"".equals(burrowInput.getText()));
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(250, 200);
	}

}
