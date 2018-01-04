package fr.pavnay.rabbits.model.predicate;

import org.apache.commons.collections4.Predicate;

import fr.pavnay.rabbits.model.Hunter;
import fr.pavnay.rabbits.model.Rabbit;

/**
 * 
 * An utility class to check if a rabbit is in the hunter range
 *
 */
public class RangePredicate implements Predicate<Rabbit> {

	private Hunter hunter;
	
	public RangePredicate(Hunter hunter) {
		this.hunter = hunter;
	}
	
	public boolean evaluate(Rabbit rabbit) {
		return hunter.isInRange(rabbit);
	}

}
