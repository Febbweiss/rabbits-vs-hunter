package fr.pavnay.rabbits.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import fr.pavnay.rabbits.model.enums.Color;
import fr.pavnay.rabbits.model.enums.Speed;

public class Rabbit implements Character {
    
    private Point location;
    private int speed;
    private Color color;
    private int distance;
    
    private Point destination;
    
    private List<Burrow> refuges = new ArrayList<Burrow>();
    private boolean scared;
    
    public Rabbit(Point location) {
        this.color = Math.random() < 0.5 ? Color.BROWN : Color.WHITE;
        this.location = location;
        speed = Speed.SLOW.getSpeed();
        distance = 0;
    }
    
    @Override
    public Point getLocation() {
        return location;
    }
    @Override
    public int getSpeed() {
        return speed;
    }
    @Override
    public void increaseDistance(int distance) {
    	this.distance += distance;
    }
    public void setSpeed(int speed) {
    	this.speed = speed;
    }
    public Color getColor() { 
        return color;
    }
    public int getDistance() {
        return distance;
    }
    public boolean isScared() {
		return scared;
	}
    public void setScared(boolean scared) {
		this.scared = scared;
	}
    @Override
    public void setDestination(Point destination) {
		this.destination = destination;
	}
    @Override
    public Point getDestination() {
		return destination;
	}
    
    /**
     * The rabbit finds a burrow. So it keeps in mind its location and to go it.
     * 
     * @param refuge
     */
    public void setRefuge(Burrow refuge) {
		this.refuges.add( refuge );
		destination = refuge.getLocation();
	}
    public List<Burrow> getRefuges() {
		return refuges;
	}
    public void slowDown() {
    	speed = Math.max(0, speed - 1);
    }
    
    public void purchased() {
    	scared = true;
    }
    
	@Override
	public String toString() {
		return "Rabbit [location=" + location + ", speed=" + speed + ", color=" + color + ", distance=" + distance
				+ "]";
	}
	
}
