package fr.pavnay.rabbits.engine;

import java.awt.Point;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import fr.pavnay.rabbits.model.Burrow;
import fr.pavnay.rabbits.model.Character;
import fr.pavnay.rabbits.model.Forest;
import fr.pavnay.rabbits.model.Hunter;
import fr.pavnay.rabbits.model.Rabbit;
import fr.pavnay.rabbits.model.enums.Speed;
import fr.pavnay.rabbits.model.enums.Status;

/**
 * 
 * This class provides the hunt mechanisms like moving, shooting, etc...
 *
 */
public class HuntEngine {

	private static final Logger logger = Logger.getLogger(HuntEngine.class);
	
	private Forest forest;
	private Hunter hunter;
	
	private int stepCount = 0;
	private int lastShotStep = -4;
	private Status status = Status.RUNNING;
	
	public HuntEngine(Forest forest, Hunter hunter) {
		this.forest = forest;
		this.hunter = hunter;
	}
	public Forest getForest() {
		return forest;
	}
	public Hunter getHunter() {
		return hunter;
	}
	public Status getStatus() {
		return status;
	}
	
	/**
	 * The core engine method. To have a full hunt cycle, call this method in a loop still the HuntEngine.getStatus() returns a value different than RUNNING.
	 * 
	 * @return
	 */
	public Status step() {
		logger.debug("Step " + stepCount);
		boolean hunterHasShot = false; // Only one shot per step
		int stepSinceLastShot = stepCount - lastShotStep;

		move(hunter);
		
		Random rand = new Random();
		for (Rabbit rabbit : forest.getRabbits() ) {
			
			if( stepSinceLastShot > 3 && !rabbit.isScared() ) {
				if( rabbit.getSpeed() > Speed.MEDIUM.getSpeed() ) {
					rabbit.slowDown();
				} else {
					rabbit.setSpeed(rand.nextInt(Speed.RUNNING.getSpeed()));
				}
			} else if( stepSinceLastShot > 5 && rabbit.isScared() ) {
				rabbit.setScared(false);
			}
		
			move(rabbit);
			
			boolean canShoot = !hunterHasShot && stepSinceLastShot > 5; // One shot per step and only 5 steps after the last shot.
			
			if( canShoot && forest.canView( hunter, rabbit ) && hunter.isInRange(rabbit) ) {
				hunterHasShot = true;
				lastShotStep = stepCount;
				if( hunter.shoot(rabbit) ) {
					forest.killedRabbit(rabbit);
				} else {
					logger.info(String.format("Hunter missed a shot. %d ammos remaining", hunter.getAmmos()));
					hunter.purchase(rabbit);
				}
			}
		}

		// A shot occurs, so rabbits must escape
		if( hunterHasShot ) {
			logger.debug("Escape from the hunter");
			forest.escape(hunter);
		}
		
		stepCount++;
		return updateStatus();
	}
	
	/**
	 * The rabbit must choose a destination - Random, the last one or a burrow
	 * 
	 * @param rabbit The rabbit to move
	 */
	protected void move( Rabbit rabbit ) {
		Point location = rabbit.getLocation();
		Point destination = rabbit.getDestination();
		
		if( location.distance(destination) <= rabbit.getSpeed() ) {
			List<Burrow> refuges = rabbit.getRefuges();
			if( refuges.size() > 0 ) { // Purchased rabbit
				Burrow refuge = refuges.get( refuges.size() -1 );
				if( refuge.isFree() ) { // Only one rabbit per burrow
					refuge.setFree(false);
					forest.savedRabbit( rabbit );
				} else {
					Burrow burrow = forest.getNearestBurrow(hunter, rabbit); // Find another burrow
					rabbit.setRefuge(burrow); // Keep in mind this burrow is full
					rabbit.setDestination(burrow.getLocation());
				}
			} else {
				setRandomDestination(rabbit);
			}
		}
		
		if( rabbit.getDestination() != null ) {
			go(rabbit);
		}
	}
	
	/**
	 * The hunter must choose a destination : the last one or a new randomized destination.
	 * 
	 * @param hunter
	 */
	protected void move( Hunter hunter ) {
		Point location = hunter.getLocation();
		Point destination = hunter.getDestination();
		
		if( location.distance(destination) < hunter.getSpeed() ) {
			destination = setRandomDestination(hunter);
		}
		
		go(hunter);
	}

	/**
	 * Move the given character (Hunter or Rabbit)
	 * 
	 * @param character
	 */
	protected void go(Character character) {
		Point location = character.getLocation();
		Point destination = character.getDestination();
		
		double x = destination.getX() - location.getX();
		double y = destination.getY() - location.getY();
		
		double rate = character.getSpeed() / location.distance(destination);
		
		double newX = Math.max(0, location.getX() + x * rate);
		double newY = Math.max(0, location.getY() + y * rate);
		Point newLocation = new Point((int) Math.min(newX, forest.getEdgeArea()), (int) Math.min(newY, forest.getEdgeArea()));
		character.increaseDistance((int) location.distance(newLocation)); 
		location.setLocation( newLocation );
	}

	/**
	 * "Choose" a random destination for the given character (Hunter or Rabbit)
	 * 
	 * @param character The character to move
	 * @return The new destination
	 */
	protected Point setRandomDestination(Character character) {
		Random rand = new Random();
		Point destination = new Point(rand.nextInt(forest.getEdgeArea()), rand.nextInt(forest.getEdgeArea()));
		character.setDestination(destination);
		
		return destination;
	}

	/**
	 * Update the hunt status
	 * 
	 * @return The new Status
	 */
	protected Status updateStatus() {
		if( forest.getRabbits().size() == 0 ) { // No more running rabbits, so the hunt ends
			if( (forest.getRabbits().size() + forest.getSavedRabbits().size()) < forest.getDeadRabbits().size() ) { // Too many dead rabbit, the hunter wins
				status = Status.HUNTER_WINS;
			} else {
				status = Status.RABBITS_WIN; // else, rabbits win
			}
			logger.info("Hunt ended : " + status);
		} else if( hunter.getAmmos() == 0 ) { // No more ammos, rabbits win
			status = Status.RABBITS_WIN;
			logger.info("Hunt ended : " + status);
		} else { // Hunt still running
			status = Status.RUNNING;
		}
		return status;
	}
	
}
