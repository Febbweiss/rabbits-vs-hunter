package fr.pavnay.rabbits.model.enums;

/**
 * 
 * An utility enum to give some default speeds
 *
 */
public enum Speed {

	STOPPED(0),
	SLOW(1),
	MEDIUM(5),
	RUNNING(10);
	
	private int speed;
	
	private Speed(int speed) {
		this.speed = speed;
	}
	
	public int getSpeed() {
		return speed;
	}
	
}
