package fr.pavnay.rabbits.model;

import java.awt.Point;

public class Tree {
    
    private Point location;
    
    public Tree( Point location ) {
        this.location = location;
    }
    
    public Point getLocation() {
        return location;
    }

	@Override
	public String toString() {
		return "Tree [location=" + location + "]";
	}
    
}
