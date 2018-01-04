package fr.pavnay.rabbits.model;

import java.awt.Point;

import org.apache.log4j.Logger;

import fr.pavnay.rabbits.model.enums.Speed;

public class Hunter implements Character {
    
	private static final Logger logger = Logger.getLogger(Hunter.class);
	
	private final static int INITIAL_AMMOS = 10;
	
    private int ammos = INITIAL_AMMOS;
    private int hungryLevel = 0;
    private int distance = 0;
    private Point location;
    private int range;
    private int speed;
    
    private Point destination;
    
    public Hunter( Point location, int range ) {
        this.location = location;
        this.range = range;
        this.speed = Speed.MEDIUM.getSpeed();
    }
    
    public int getAmmos() {
        return ammos;
    }
    public void setAmmos(int ammos) {
		this.ammos = ammos;
	}
    public int getHungryLevel() {
        return hungryLevel;
    }
    public void setHungryLevel(int hungryLevel) {
		this.hungryLevel = hungryLevel;
	}
    public int getDistance() {
        return distance;
    }
    @Override
    public Point getLocation() {
        return location;
    }
    @Override
    public Point getDestination() {
		return destination;
	}
    @Override
    public void setDestination(Point destination) {
		this.destination = destination;
	}
    @Override
    public int getSpeed() {
    	return speed;
    }
    /**
     * Increasing the walked distance increases the hungry level
     * 
     */
    @Override
    public void increaseDistance(int distance) {
    	this.distance += distance;
    	setHungryLevel(Math.min(10,  (int) (this.distance / 15)));
    }
    
    /**
     * Tries to shoot a rabbit
     * 
     * @param rabbit
     * @return
     */
    public boolean shoot(Rabbit rabbit) {
    	if( ammos == 0 ) {
    		logger.info("Click... No more ammos");
    		return false;
    	}
    	double rate = getAccuracyRate(rabbit);
    	ammos--;
    	return Math.random() < rate; // If the random value is less than the computed accuracy, the shot is good
    }
    
    /**
     * The hunter finds a rabbit. He goes to its last position.
     * 
     * @param rabbit
     */
    public void purchase(Rabbit rabbit) {
    	rabbit.purchased();
    	destination = rabbit.getLocation();
    }
    
    /**
     * Accuracy depends to the distance with the target, the number of leaving ammos and the hungry level.
     * 
     * @param rabbit
     * @return
     */
    protected double getAccuracyRate(Rabbit rabbit) {
    	double rate = 1 - getLocation().distance(rabbit.getLocation()) / range;
    	
    	rate -= 0.005 * (INITIAL_AMMOS - ammos);
    	rate -= 0.01 * hungryLevel;
    	
    	return rate;
    }
	
    /**
     * The given rabbit is in range if the distance between the hunter and the rabbit is less than the hunter range.
     * 
     * @param rabbit
     * @return
     */
	public boolean isInRange(Rabbit rabbit) {
		logger.debug(location.distance(rabbit.getLocation())  + "<=" + range);
		return location.distance(rabbit.getLocation()) <= range;
	}

	@Override
	public String toString() {
		return "Hunter [ammos=" + ammos + ", hungryLevel=" + hungryLevel + ", distance=" + distance + ", location="
				+ location + ", range=" + range + "]";
	}
    
}
