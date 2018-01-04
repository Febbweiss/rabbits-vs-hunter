package fr.pavnay.rabbits.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

import org.junit.Test;

import fr.pavnay.rabbits.model.Burrow;
import fr.pavnay.rabbits.model.Forest;
import fr.pavnay.rabbits.model.Hunter;
import fr.pavnay.rabbits.model.Rabbit;
import fr.pavnay.rabbits.model.enums.Speed;
import fr.pavnay.rabbits.model.enums.Status;

public class HuntEngineTest {
	
	@Test
	public void testUpdateStatusRunning() {
		Hunter hunter = new Hunter(new Point(3, 2), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		HuntEngine engine = new HuntEngine(forest, hunter);
		engine.updateStatus();
		
		assertEquals( "Default status is RUNNING", Status.RUNNING, engine.getStatus());
	}
	
	@Test
	public void testUpdateStatusRabbitsWin() {
		Hunter hunter = new Hunter(new Point(3, 2), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		HuntEngine engine = new HuntEngine(forest, hunter);
		hunter.setAmmos(0);
		engine.updateStatus();
		
		assertEquals( "Default status is RUNNING", Status.RABBITS_WIN, engine.getStatus());
	}
	
	@Test
	public void testUpdateStatusHunterWins() {
		Hunter hunter = new Hunter(new Point(3, 2), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		HuntEngine engine = new HuntEngine(forest, hunter);
		forest.getDeadRabbits().addAll(forest.getRabbits());
		forest.getRabbits().clear();
		engine.updateStatus();
		
		assertEquals( "Default status is RUNNING", Status.HUNTER_WINS, engine.getStatus());
	}
	
	@Test
	public void testUpdateStatusHunterLoose() {
		Hunter hunter = new Hunter(new Point(3, 2), 10);
		Forest forest = new Forest(10, 1, 2, 2);
		HuntEngine engine = new HuntEngine(forest, hunter);
		forest.getDeadRabbits().add(forest.getRabbits().get(0));
		forest.getSavedRabbits().add(forest.getRabbits().get(1));
		forest.getRabbits().clear();
		engine.updateStatus();
		
		assertEquals( "Default status is RUNNING", Status.RABBITS_WIN, engine.getStatus());
	}
	
	@Test
	public void testMoveHunterCloseToDestination() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		hunter.setDestination(new Point(2, 0));
		Forest forest = new Forest(10, 1, 2, 1);
		HuntEngine engine = new HuntEngine(forest, hunter);
		
		engine.move(hunter);
		assertNotEquals("Destination has changed - X", 2, hunter.getDestination().getX());
		assertNotEquals("Destination has changed - Y", 0, hunter.getDestination().getY());
	}
	
	@Test
	public void testMoveHunterFarFromDestination() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		hunter.setDestination(new Point(20, 0));
		Forest forest = new Forest(10, 1, 2, 1);
		HuntEngine engine = new HuntEngine(forest, hunter);
		
		engine.move(hunter);
		assertNotEquals("Destination has changed - X", 20, hunter.getDestination().getX());
		assertNotEquals("Destination has changed - Y", 0, hunter.getDestination().getY());
	}
	
	@Test
	public void testMoveRabbitCloseToFreeBurrow() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		HuntEngine engine = new HuntEngine(forest, hunter);
		
		Burrow burrow = forest.getBurrows().get(0);
		burrow.getLocation().setLocation(2, 0);
		
		Rabbit rabbit = forest.getRabbits().get(0);
		rabbit.getLocation().setLocation(new Point(0, 0));
		rabbit.setSpeed(Speed.MEDIUM.getSpeed());
		rabbit.setRefuge(burrow);
		
		engine.move(rabbit);
		assertFalse("Lucky rabbit doesn't run anymore", forest.getRabbits().contains(rabbit));
		assertNull("No more destination", rabbit.getDestination());
	}
	
	@Test
	public void testMoveRabbitCloseToNotFreeBurrow() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		HuntEngine engine = new HuntEngine(forest, hunter);
		
		Burrow burrow = forest.getBurrows().get(0);
		burrow.getLocation().setLocation(2, 0);
		burrow.setFree(false);
		
		Rabbit rabbit = forest.getRabbits().get(0);
		rabbit.getLocation().setLocation(new Point(0, 0));
		rabbit.setSpeed(Speed.MEDIUM.getSpeed());
		rabbit.setRefuge(burrow);
		
		engine.move(rabbit);
		assertTrue("Rabbit still running", forest.getRabbits().contains(rabbit));
		assertEquals("New destination : next burrow",forest.getBurrows().get(1).getLocation(), rabbit.getDestination());
	}
	
	@Test
	public void testMoveRabbitToFarFromBurrow() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		HuntEngine engine = new HuntEngine(forest, hunter);
		
		Burrow burrow = forest.getBurrows().get(0);
		burrow.getLocation().setLocation(20, 0);
		
		Rabbit rabbit = forest.getRabbits().get(0);
		rabbit.getLocation().setLocation(new Point(0, 0));
		rabbit.setSpeed(Speed.MEDIUM.getSpeed());
		rabbit.setRefuge(burrow);
		
		engine.move(rabbit);
		assertTrue("Rabbit still running", forest.getRabbits().contains(rabbit));
		assertEquals("Burrow still the destination", burrow.getLocation(), rabbit.getDestination());
	}
	
	@Test
	public void testMoveRabbitCloseToDestination() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		HuntEngine engine = new HuntEngine(forest, hunter);
		
		Rabbit rabbit = forest.getRabbits().get(0);
		rabbit.getLocation().setLocation(new Point(0, 0));
		rabbit.setSpeed(Speed.MEDIUM.getSpeed());
		rabbit.setDestination(new Point(3, 0));
		engine.move(rabbit);
		assertNotEquals("New destination", new Point(3, 0), rabbit.getDestination());
	}
	
	@Test
	public void testMoveRabbitFarFromDestination() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		HuntEngine engine = new HuntEngine(forest, hunter);
		
		Rabbit rabbit = forest.getRabbits().get(0);
		rabbit.getLocation().setLocation(new Point(0, 0));
		rabbit.setSpeed(Speed.MEDIUM.getSpeed());
		rabbit.setDestination(new Point(13, 0));
		
		engine.move(rabbit);
		assertEquals("Same destination", new Point(13, 0), rabbit.getDestination());
	}
	
	@Test
	public void testSetRandomDestinationHunter() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		hunter.setDestination(null);
		Forest forest = new Forest(10, 1, 2, 1);
		HuntEngine engine = new HuntEngine(forest, hunter);
		
		engine.setRandomDestination(hunter);
		assertNotNull("Destination set", hunter.getDestination());
	}
	
	@Test
	public void testSetRandomDestinationRabbit() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		HuntEngine engine = new HuntEngine(forest, hunter);
		Rabbit rabbit = forest.getRabbits().get(0);
		rabbit.setDestination(null);
		engine.setRandomDestination(rabbit);
		assertNotNull("Destination set", rabbit.getDestination());
	}
	
	@Test
	public void testGoHunter() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		hunter.setDestination(new Point(20, 20));
		Forest forest = new Forest(10, 1, 2, 1);
		HuntEngine engine = new HuntEngine(forest, hunter);
		
		engine.go(hunter);
		
		assertEquals("Location has changed", new Point(3, 3), hunter.getLocation());
	}
	
	@Test
	public void testGoRabbit() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		HuntEngine engine = new HuntEngine(forest, hunter);
		
		Rabbit rabbit = forest.getRabbits().get(0);
		rabbit.getLocation().setLocation(new Point(0, 0));
		rabbit.setSpeed(Speed.MEDIUM.getSpeed());
		rabbit.setDestination(new Point(13, 13));
		
		engine.go(rabbit);
		
		assertEquals("Location has changed", new Point(3, 3), rabbit.getLocation());
	}
	
	@Test
	public void testSavedRabbit() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Forest forest = new Forest(10, 1, 2, 1);
		HuntEngine engine = new HuntEngine(forest, hunter);
		
		Burrow burrow = forest.getBurrows().get(0);
		burrow.getLocation().setLocation(2, 0);
		
		Rabbit rabbit = forest.getRabbits().get(0);
		rabbit.getLocation().setLocation(new Point(0, 0));
		rabbit.setSpeed(Speed.MEDIUM.getSpeed());
		rabbit.setRefuge(burrow);
		
		engine.go(rabbit);
		
		assertEquals("Destination has not changed", new Point(5, 0), rabbit.getLocation());
	}
}
