package fr.pavnay.rabbits.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.rules.ExpectedException;

@SuppressWarnings("deprecation")
public class ForestTest {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testInitSuccess() {
		Forest forest = new Forest(10, 1, 1, 1);
		assertNotNull("Forest created", forest);
	}

	@Test
	public void testInitFailureAreaMin() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(JUnitMatchers.containsString("Area"));
		  
		new Forest(0, 1, 1, 1);
	}
	
	@Test
	public void testInitFailureAreaMax() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(JUnitMatchers.containsString("Area"));
		
		new Forest(11, 1, 1, 1);
	}
	
	@Test
	public void testInitFailureTreesMin() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(JUnitMatchers.containsString("Tree"));
		  
		new Forest(1, 0, 1, 1);
	}
	
	@Test
	public void testInitFailureTreesMax() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(JUnitMatchers.containsString("Tree"));
		
		new Forest(10, 1001, 1, 1);
	}
	
	@Test
	public void testInitFailureNoRabbits() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(JUnitMatchers.containsString("rabbits"));
		
		new Forest(10, 100, 1, 0);
	}
	
	@Test
	public void testInitFailureNotEnoughBurrows() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(JUnitMatchers.containsString("burrow"));
		
		new Forest(10, 100, 1, 2);
	}
	
	@Test
	public void testIsBetweenSuccess() {
		Hunter hunter = new Hunter(new Point(0,0), 10);
		Forest forest = new Forest(10, 1, 1, 1);
		Rabbit rabbit = new Rabbit(new Point(4, 4));
		Tree tree = new Tree(new Point(2, 2));
		assertTrue("Tree is between hunter and rabbit", forest.isBetween(hunter.getLocation(), rabbit.getLocation(), tree.getLocation()));
	}
	
	@Test
	public void testCanViewSuccess() {
		Hunter hunter = new Hunter(new Point(0,0), 10);
		Forest forest = new Forest(10, 2, 1, 1);
		forest.getTrees().get(0).getLocation().setLocation(10, 0);
		forest.getTrees().get(1).getLocation().setLocation(20, 0);
		Rabbit rabbit = new Rabbit(new Point(4, 4));
		assertTrue("No Tree between hunter and rabbit", forest.canView(hunter, rabbit));
	}
	
	@Test
	public void testCanViewFailure() {
		Hunter hunter = new Hunter(new Point(0,0), 10);
		Forest forest = new Forest(10, 2, 1, 1);
		forest.getTrees().get(0).getLocation().setLocation(2, 2);
		forest.getTrees().get(1).getLocation().setLocation(2, 0);
		Rabbit rabbit = new Rabbit(new Point(4, 4));
		assertFalse("Tree is between hunter and rabbit", forest.canView(hunter, rabbit));
	}
	
	@Test
	public void testIsBetweenFailureTreeBehind() {
		Hunter hunter = new Hunter(new Point(0,0), 10);
		Forest forest = new Forest(10, 1, 1, 1);
		Rabbit rabbit = new Rabbit(new Point(4, 4));
		Tree tree = new Tree(new Point(5, 5));
		assertFalse("Tree is behind the rabbit", forest.isBetween(hunter.getLocation(), rabbit.getLocation(), tree.getLocation()));
	}
	
	@Test
	public void testIsBetweenFailureTreeSomewhere() {
		Hunter hunter = new Hunter(new Point(0,0), 10);
		Forest forest = new Forest(10, 1, 1, 1);
		Rabbit rabbit = new Rabbit(new Point(4, 4));
		Tree tree = new Tree(new Point(10, 5));
		assertFalse("Tree is not in the hunter-rabbit aligment", forest.isBetween(hunter.getLocation(), rabbit.getLocation(), tree.getLocation()));
	}
	
	@Test
	public void testKilledRabbitSuccess() {
		Forest forest = new Forest(10, 1, 2, 2);
		Rabbit rabbit = forest.getRabbits().get(0);
		forest.killedRabbit(rabbit);
		
		assertFalse("Poor killed rabbit still running", forest.getRabbits().contains(rabbit));
		assertEquals("Still one rabbit alive", 1, forest.getRabbits().size());
		assertTrue("Poor killed rabbit can't run", forest.getDeadRabbits().contains(rabbit));
		assertEquals("One dead rabbit", 1, forest.getDeadRabbits().size());
	}
	
	@Test
	public void testSavedRabbitSuccess() {
		Forest forest = new Forest(10, 1, 1, 1);
		Rabbit rabbit = forest.getRabbits().get(0);
		forest.savedRabbit(rabbit);
		
		assertFalse("Poor saved rabbit still running", forest.getRabbits().contains(rabbit));
		assertTrue("Lucky rabbit can't run", forest.getSavedRabbits().contains(rabbit));
	}

	@Test
	public void testGetRabbitsInRangeSuccess() {
		Hunter hunter = new Hunter(new Point(0,0), 10);
		Forest forest = new Forest(10, 1, 2, 2);
		forest.getRabbits().get(0).getLocation().setLocation(0, 5); // First rabbit in range
		forest.getRabbits().get(1).getLocation().setLocation(0, 15); // Seconde one out of range
		
		List<Rabbit> rabbitsInRange = forest.getRabbitsInRange(hunter);
		assertEquals("One rabbit in range", 1, rabbitsInRange.size());
		assertTrue("First rabbit in range", rabbitsInRange.contains(forest.getRabbits().get(0)));
		assertFalse("Second rabbit out of range", rabbitsInRange.contains(forest.getRabbits().get(1)));
	}
	
	@Test
	public void testGetNearestBurrowFirst() {
		Hunter hunter = new Hunter(new Point(0, 1), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		forest.getBurrows().get(0).getLocation().setLocation(7, 2);
		forest.getBurrows().get(1).getLocation().setLocation(9, 0);
		Rabbit rabbit = forest.getRabbits().get(0);
		rabbit.getLocation().setLocation(4, 1);
		
		assertEquals("Second burrow is nearest from rabbit", forest.getBurrows().get(0), forest.getNearestBurrow(hunter, rabbit));
	}
	
	@Test
	public void testGetNearestBurrowSecond() {
		Hunter hunter = new Hunter(new Point(0, 1), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		forest.getBurrows().get(0).getLocation().setLocation(9, 0);
		forest.getBurrows().get(1).getLocation().setLocation(7, 2);
		Rabbit rabbit = forest.getRabbits().get(0);
		rabbit.getLocation().setLocation(4, 1);
		
		assertEquals("Second burrow is nearest from rabbit", forest.getBurrows().get(1), forest.getNearestBurrow(hunter, rabbit));
	}
	
	@Test
	public void testGetNearestBurrowWorstIsBetter() {
		Hunter hunter = new Hunter(new Point(0, 1), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		forest.getBurrows().get(0).getLocation().setLocation(7, 2);
		forest.getBurrows().get(1).getLocation().setLocation(3, 2);
		Rabbit rabbit = forest.getRabbits().get(0);
		rabbit.getLocation().setLocation(4, 1);
		
		assertEquals("Second burrow is nearest from rabbit", forest.getBurrows().get(1), forest.getNearestBurrow(hunter, rabbit));
	}
	
	@Test
	public void testGetNearestBurrowNotVisited() {
		Hunter hunter = new Hunter(new Point(0, 1), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		forest.getBurrows().get(0).getLocation().setLocation(7, 2);
		forest.getBurrows().get(1).getLocation().setLocation(9, 0);
		Rabbit rabbit = forest.getRabbits().get(0);
		rabbit.getLocation().setLocation(4, 1);
		rabbit.setRefuge(forest.getBurrows().get(0));
		
		assertEquals("Second burrow was not visited by the rabbit", forest.getBurrows().get(1), forest.getNearestBurrow(hunter, rabbit));
	}
	
	@Test
	public void testEscapeScared() {
		Hunter hunter = new Hunter(new Point(3, 2), 10);
		Forest forest = new Forest(10, 1, 1, 1);
		Rabbit rabbit = forest.getRabbits().get(0);
		rabbit.getLocation().setLocation(6, 3);
		rabbit.setScared(true);
		
		forest.escape(hunter, rabbit);
		Burrow burrow = forest.getBurrows().get(0);
		assertEquals("Right location X", burrow.getLocation().getX(), rabbit.getDestination().getX(), 0);
		assertEquals("Right location Y", burrow.getLocation().getY(), rabbit.getDestination().getY(), 0);
	}
	
	@Test
	public void testEscapeNotScared() {
		Hunter hunter = new Hunter(new Point(3, 2), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		Rabbit rabbit = forest.getRabbits().get(0);
		rabbit.getLocation().setLocation(6, 3);
		
		forest.escape(hunter, rabbit);
		
		assertEquals("Right location X", 9, rabbit.getDestination().getX(), 0);
		assertEquals("Right location Y", 4, rabbit.getDestination().getY(), 0);
	}
	
	@Test
	public void testEscapeNotScaredOutOfForest() {
		Hunter hunter = new Hunter(new Point(5, 3), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		Rabbit rabbit = forest.getRabbits().get(0);
		rabbit.getLocation().setLocation(2, 2);
		
		forest.escape(hunter, rabbit);
		
		assertEquals("Right location X", 0, rabbit.getDestination().getX(), 0);
		assertEquals("Right location Y", 1, rabbit.getDestination().getY(), 0);
	}
}
