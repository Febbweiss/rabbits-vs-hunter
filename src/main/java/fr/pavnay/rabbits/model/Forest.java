package fr.pavnay.rabbits.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import fr.pavnay.rabbits.model.predicate.RangePredicate;

/**
 * 
 * The main "entity" where rabbits live.
 *
 */
public class Forest {
    
	private static final Logger logger = Logger.getLogger(Forest.class);
	
    private List<Tree> trees = new ArrayList<Tree>();
    private List<Rabbit> rabbits = new CopyOnWriteArrayList<Rabbit>();
    private List<Rabbit> savedRabbits = new CopyOnWriteArrayList<Rabbit>();
    private List<Rabbit> deadRabbits = new CopyOnWriteArrayList<Rabbit>();
    private List<Burrow> burrows = new ArrayList<Burrow>();
    
    private int edgeArea;
    
    public Forest( int area, int treesCount, int burrowsCount, int rabbitsCount ) {
    	if( area <= 0 || area > 10) {
    		throw new IllegalArgumentException("Area must be between 1 and 10 ");
    	}
    	
    	if( treesCount <= 0 || treesCount > 1000) {
    		throw new IllegalArgumentException("Tree count must be between 1 and 1000 ");
    	}
    	
    	if( rabbitsCount <= 0 ) {
    		throw new IllegalArgumentException("Add some rabbits");
    	}
    	
    	if( burrowsCount <= 0 || burrowsCount < rabbitsCount) {
    		throw new IllegalArgumentException("Add some burrows. Greater or equals than rabbits count.");
    	}
    	
    	
    	edgeArea = (int) Math.sqrt(area * 1000);
    	
    	populate(treesCount, burrowsCount, rabbitsCount);
    }
    
    /**
     * Add trees, burrows and rabbits to the forest. These entities have random location (and rabbits random destination)
     * 
     * @param treesCount Number of trees to create
     * @param burrowsCount Number of burrows to create
     * @param rabbitsCount Number of rabbits to create
     */
    private void populate(int treesCount, int burrowsCount, int rabbitsCount) {
    	Random rand = new Random(); 
    	for( int i = 0; i < treesCount; i++ ) {
    		trees.add(new Tree(new Point(rand.nextInt(edgeArea), rand.nextInt(edgeArea))));
    	}
    	
    	for( int i = 0; i < burrowsCount; i++ ) {
    		burrows.add(new Burrow(new Point(rand.nextInt(edgeArea), rand.nextInt(edgeArea))));
    	}
    	
    	for( int i = 0; i < rabbitsCount; i++ ) {
    		Rabbit rabbit = new Rabbit(new Point(rand.nextInt(edgeArea), rand.nextInt(edgeArea)));
    		rabbit.setDestination(new Point(rand.nextInt(edgeArea), rand.nextInt(edgeArea)));
    		rabbits.add(rabbit);
    	}
    }
    
    public int getEdgeArea() {
		return edgeArea;
	}
    
    public List<Rabbit> getRabbits() {
		return rabbits;
	}
    
    /**
     * 
     * @param hunter
     * @return Rabbits in hunter range
     */
    public List<Rabbit> getRabbitsInRange(Hunter hunter) {
    	return (List<Rabbit>) CollectionUtils.select(rabbits, new RangePredicate(hunter));
    }
    
    /**
     * 
     * @return Dead rabbits
     */
    public List<Rabbit> getDeadRabbits() {
		return deadRabbits;
	}
    
    /**
     * 
     * @return Rabbits in burrows
     */
    public List<Rabbit> getSavedRabbits() {
		return savedRabbits;
	}
    
    /**
     * Move a rabbit from "running" to "dead"
     * @param rabbit
     */
    public void killedRabbit(Rabbit rabbit) {
    	if( rabbits.remove(rabbit) ) {
    		logger.info("Poor rabbit... Killed...");
    		deadRabbits.add(rabbit);
    	} else {
    		throw new RuntimeException("Unable to kill a poor rabbit...");
    	}
    }
    
    /**
     * Move a rabbit from "running" to "saved"
     * @param rabbit
     */
    public void savedRabbit(Rabbit rabbit) {
    	if( rabbits.remove(rabbit) ) {
    		logger.info("In a burrow... Rabbit saved");
    		rabbit.setDestination(null);
    		savedRabbits.add(rabbit);
    	} else {
    		throw new RuntimeException("Unable to save a poor rabbit...");
    	}
    }
    
    public List<Burrow> getBurrows() {
		return burrows;
	}
    
    /**
     * Rabbits in hunter range must escape
     * @param hunter
     */
    public void escape(Hunter hunter) {
    	for( Rabbit rabbit : getRabbitsInRange(hunter) ) {
    		escape(hunter, rabbit);
    	}
    }
    
    /**
     * Choose a rabbit destination to escape. If the given rabbit is scared (the one on which the hunter shot), it must find a burrow. 
     * The others must go at the opposite of hunter location.
     *  
     * @param hunter
     * @param rabbit
     */
    protected void escape( Hunter hunter, Rabbit rabbit ) {
    	Point target = null;
    	if( rabbit.isScared() ) {
    		logger.debug("Rabbit's searching a burrow");
    		Burrow refuge = getNearestBurrow(hunter, rabbit);
    		rabbit.setRefuge(refuge);
    	} else {
    		logger.debug("Go away from the hunter");
    		// Hunter -> Rabbit vector
    		double a = rabbit.getLocation().getX() - hunter.getLocation().getX();
    		double b = rabbit.getLocation().getY() - hunter.getLocation().getY();
    		
    		// Keep in forest
    		double x = Math.max(0, rabbit.getLocation().getX() + a);
    		double y = Math.max(0, rabbit.getLocation().getY() + b);
    		target = new Point((int) Math.min(x, edgeArea), (int) Math.min(y, edgeArea));
    		rabbit.setDestination(target);
    	}
    	
    }
    
	/**
	 * Compute 2 nearest burrows. One with back to the hunter and another more safe. The last one is preferred
	 * 
	 * @param hunter
	 * @param rabbit
	 * @return
	 */
    public Burrow getNearestBurrow(Hunter hunter, Rabbit rabbit) {
    	logger.debug("Searching the nearest burrow");
    	double distance = Double.MAX_VALUE, worthDistance = Double.MAX_VALUE;
    	Burrow nearest = null, worthNearest = null;
    	
    	for( Burrow burrow : burrows ) {
    		if( rabbit.getRefuges().contains(burrow) ) {
    			continue;
    		}
    		double tmpDistance = burrow.getLocation().distance(rabbit.getLocation());
    		double dotProduct = dotProduct(hunter.getLocation(), rabbit.getLocation(), burrow.getLocation());
    		if( dotProduct <= 0 && tmpDistance < distance ) {
    			distance = tmpDistance;
    			nearest = burrow;
    		}
    		else if( dotProduct > 0 && tmpDistance < worthDistance ) { // Back to the hunter
    			worthDistance = tmpDistance;
    			worthNearest = burrow;
    		}
    	}
    	
    	return distance <= worthDistance ? nearest : worthNearest;
    }
    
    public List<Tree> getTrees() {
		return trees;
	}
    
    /**
     * A rabbit is viewed when there are no trees between the hunter and it.
     * 
     * @param hunter
     * @param rabbit
     * @return
     */
    public boolean canView(Hunter hunter, Rabbit rabbit) {
    	for( Tree tree : trees ) {
    		if( isBetween(hunter.getLocation(), rabbit.getLocation(), tree.getLocation())) {
    			logger.debug("Rabbit is hidden");
    			return false;
    		}
    	};
    	
    	return true;
	}
	
    private static final int EPSILON = 5;
    
    /**
     * Checks if a tree is between a rabbit and the hunter.
     * 
     * @param hunter
     * @param rabbit
     * @param tree
     * @return
     */
    protected boolean isBetween(Point hunter, Point rabbit, Point tree) {
    	double crossproduct = (tree.getY() - hunter.getY()) * (rabbit.getX() - hunter.getX()) - (tree.getX() - hunter.getX()) * (rabbit.getY() - hunter.getY());
    	if (Math.abs(crossproduct) > EPSILON )  {
    		return false;
    	}
    	
    	double dotproduct = dotProduct(hunter, rabbit, tree); // Tree is far away
    	if ( dotproduct < 0 ) {
    		return false;
    	}
    	
    	double squaredlengthba = (rabbit.getX()- hunter.getX())*(rabbit.getX() - hunter.getX()) + (rabbit.getY() - hunter.getY())*(rabbit.getY() - hunter.getY());
    	if ( dotproduct > squaredlengthba) { // Tree is too far
    		return false;
    	}
    	
    	return true;
    }
    
    /**
     * dotProduct to know if the tree is between the hunter and the rabbit.
     * 
     * @param hunter
     * @param rabbit
     * @param tree
     * @return If positive, the tree is between.
     */
    private double dotProduct(Point hunter, Point rabbit, Point tree) {
    	return (tree.getX() - hunter.getX()) * (rabbit.getX() - hunter.getX()) + (tree.getY() - hunter.getY())*(rabbit.getY() - hunter.getY());
    }


    public void printStats() {
    	logger.debug("Running rabbits");
    	printRabbits(rabbits);
    	logger.debug("Saved rabbits");
    	printRabbits(savedRabbits);
    	logger.debug("Dead rabbits");
    	printRabbits(deadRabbits);
    }
    private void printRabbits(List<Rabbit> rabbits) {
    	for( Rabbit rabbit : rabbits ) {
    		logger.debug(rabbit);
    	}
    	
    }
	@Override
	public String toString() {
		return "Forest [trees=" + trees + ", rabbits=" + rabbits + ", burrows=" + burrows + ", edgeArea=" + edgeArea
				+ "]";
	}
    
}
