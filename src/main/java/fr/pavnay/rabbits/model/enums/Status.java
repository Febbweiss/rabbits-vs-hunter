package fr.pavnay.rabbits.model.enums;

/**
 * 
 * The hunt status
 *
 */
public enum Status {

	RUNNING,
	HUNTER_WINS,
	RABBITS_WIN;
	
	@Override
	public String toString() {
		return name().charAt(0) + name().substring(1).toLowerCase().replace('_', ' ');
	}
}
