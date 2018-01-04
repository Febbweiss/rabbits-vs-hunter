package fr.pavnay.rabbits.ui.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.pavnay.rabbits.model.Forest;
import fr.pavnay.rabbits.model.Hunter;
import fr.pavnay.rabbits.ui.UiUtils;

/**
 * 
 * This class gives some hunt statistics
 *
 */
public class CharacterPanel extends JPanel {

	private static final long serialVersionUID = -7823773213443395686L;

	private Hunter hunter;
	private Forest forest;
	
	private JLabel ammosCountLabel;
	private JLabel hungryLevelLabel;
	private JLabel distanceLabel;
	
	private JLabel runningRabbitsLabel;
	private JLabel savedRabbitsLabel;
	private JLabel deadRabbitsLabel;
	
	public CharacterPanel(Hunter hunter, Forest forest) {
		
		this.hunter = hunter;
		this.forest = forest;
			
		JPanel innerPanel = new JPanel(new GridBagLayout());
		
		UiUtils.add(innerPanel, new JLabel("Hunter"), 0, 0, 3, 1);
		JPanel tmpPanel = new JPanel(new GridBagLayout());
		UiUtils.add(tmpPanel,new JLabel("Ammos"), 0, 0, 1, 1);
		ammosCountLabel = new JLabel(String.valueOf(hunter.getAmmos()));
		Font plainFont = ammosCountLabel.getFont().deriveFont(Font.PLAIN);
		ammosCountLabel.setFont(plainFont);
		UiUtils.add(tmpPanel, ammosCountLabel, 1, 0, 1, 1);
		
		UiUtils.add(tmpPanel, new JLabel("Hungry"), 2, 0, 1, 1);
		hungryLevelLabel = new JLabel(String.valueOf(hunter.getHungryLevel()));
		hungryLevelLabel.setFont(plainFont);
		UiUtils.add(tmpPanel, hungryLevelLabel, 3, 0, 1, 1);
		
		UiUtils.add(tmpPanel, new JLabel("Distance"), 4, 0, 1, 1);
		distanceLabel = new JLabel(String.valueOf(hunter.getDistance()));
		distanceLabel.setFont(plainFont);
		UiUtils.add(tmpPanel, distanceLabel, 5, 0, 1, 1);
		
		UiUtils.add(innerPanel, tmpPanel, 0, 1, 1, 1);
		
		UiUtils.add(innerPanel, new JLabel("Rabbits"), 0, 2, 3, 1);
		tmpPanel = new JPanel(new GridBagLayout());
		UiUtils.add(tmpPanel,new JLabel("Running"), 0, 0, 1, 1);
		runningRabbitsLabel = new JLabel(String.valueOf(forest.getRabbits().size()));
		plainFont = runningRabbitsLabel.getFont().deriveFont(Font.PLAIN);
		runningRabbitsLabel.setFont(plainFont);
		runningRabbitsLabel.setForeground(Color.BLACK);
		UiUtils.add(tmpPanel, runningRabbitsLabel, 1, 0, 1, 1);
		
		UiUtils.add(tmpPanel, new JLabel("Saved"), 2, 0, 1, 1);
		savedRabbitsLabel = new JLabel(String.valueOf(forest.getSavedRabbits().size()));
		savedRabbitsLabel.setFont(plainFont);
		savedRabbitsLabel.setForeground(Color.GREEN);
		UiUtils.add(tmpPanel, savedRabbitsLabel, 3, 0, 1, 1);
		
		UiUtils.add(tmpPanel, new JLabel("Dead"), 4, 0, 1, 1);
		deadRabbitsLabel = new JLabel(String.valueOf(forest.getDeadRabbits().size()));
		deadRabbitsLabel.setFont(plainFont);
		deadRabbitsLabel.setForeground(Color.RED);
		UiUtils.add(tmpPanel, deadRabbitsLabel, 5, 0, 1, 1);
		
		UiUtils.add(innerPanel, tmpPanel, 0, 3, 1, 1);
		
		add(innerPanel);
	
	}
	
	public void refresh() {
		ammosCountLabel.setText(String.valueOf(hunter.getAmmos()));
		hungryLevelLabel.setText(String.valueOf(hunter.getHungryLevel()));
		distanceLabel.setText(String.valueOf(hunter.getDistance()));
		
		runningRabbitsLabel.setText(String.valueOf(forest.getRabbits().size()));
		savedRabbitsLabel.setText(String.valueOf(forest.getSavedRabbits().size()));
		deadRabbitsLabel.setText(String.valueOf(forest.getDeadRabbits().size()));
	}
	
}
