package fr.pavnay.rabbits.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import org.apache.log4j.Logger;

import fr.pavnay.rabbits.engine.HuntEngine;
import fr.pavnay.rabbits.ui.panel.Canvas;
import fr.pavnay.rabbits.ui.panel.CanvasLegend;
import fr.pavnay.rabbits.ui.panel.CharacterPanel;
import fr.pavnay.rabbits.ui.panel.LogPanel;

/**
 * 
 * The frame to see the hunt.
 *
 */
public class MainFrame extends JFrame implements PropertyChangeListener, ActionListener {

	private static final long serialVersionUID = -5070100141993068939L;
	
	private static final Logger logger = Logger.getLogger(MainFrame.class);
	
	private HuntEngine engine;
	
	private Timer timer; // Timer used to refresh UI periodically
	
	private JPanel canvas; // The hunt panel
	private CharacterPanel characterPanel; // Characters stats panel
	private LogPanel logPanel; // Hunt event panel
	private JLabel statusLabel; // Hunt status
	
	private HuntWorker worker;
	
	public MainFrame( HuntEngine engine ) {
		setTitle("Rabbits vs Hunter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout());
		
		this.engine = engine;
		
		JPanel statusPanel = new JPanel();
		statusPanel.add(new JLabel("Status :"));
		statusLabel = new JLabel(engine.getStatus().toString());
		statusPanel.add(statusLabel);
		add(statusPanel, BorderLayout.NORTH);
		
		canvas = new Canvas(engine.getForest(), engine.getHunter());
		timer = new Timer((int) HuntWorker.SLEEP_TIME, this);
		add(canvas, BorderLayout.CENTER);
		
		characterPanel = new CharacterPanel(engine.getHunter(), engine.getForest());
		logPanel = new LogPanel();

		int VERT_GAP = 10;
	    int EB_GAP = 5;
		JPanel sidePanel = new JPanel();
	    sidePanel.setBorder(BorderFactory.createEmptyBorder(EB_GAP, EB_GAP, EB_GAP, EB_GAP));
	    sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.PAGE_AXIS));
	    sidePanel.add(characterPanel);
	    sidePanel.add(Box.createVerticalStrut(VERT_GAP));
	    sidePanel.add(Box.createVerticalStrut(VERT_GAP));
	    sidePanel.add(new JScrollPane(logPanel));
		add(sidePanel, BorderLayout.EAST);

		add( new CanvasLegend(engine.getForest()), BorderLayout.SOUTH );
		
		worker = new HuntWorker(engine, this);
		worker.addPropertyChangeListener(this);
		worker.execute();
		timer.start();
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void propertyChange(PropertyChangeEvent event) {
		if ("state".equals(event.getPropertyName()) && SwingWorker.StateValue.DONE == event.getNewValue()) { // Worker ends
			statusLabel.setText(engine.getStatus().toString());
			statusLabel.repaint();
			
			logger.debug(engine.getHunter());
			engine.getForest().printStats();
        }
	}

	public void actionPerformed(ActionEvent event) {
		if(event.getSource()==timer){
			canvas.repaint();
			characterPanel.refresh();
	    }
	}
	
	public void report(String log) {
		logPanel.addLog(log);
	}

}
