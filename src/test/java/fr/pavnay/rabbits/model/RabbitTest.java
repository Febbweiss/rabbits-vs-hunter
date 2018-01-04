package fr.pavnay.rabbits.model;

import java.awt.Point;

import org.junit.Test;

import fr.pavnay.rabbits.model.enums.Speed;

import static org.junit.Assert.assertEquals;

public class RabbitTest {

	@Test
	public void testSlowDown() {
		Rabbit rabbit = new Rabbit(new Point(0,0));
		rabbit.setSpeed(Speed.MEDIUM.getSpeed());
		rabbit.slowDown();
		assertEquals("Rabbit slow down", Speed.MEDIUM.getSpeed() - 1, rabbit.getSpeed());
	}
	
	@Test
	public void testSlowDownStopped() {
		Rabbit rabbit = new Rabbit(new Point(0,0));
		rabbit.setSpeed(Speed.SLOW.getSpeed());
		rabbit.slowDown();
		rabbit.slowDown();
		assertEquals("Rabbit stopped", Speed.STOPPED.getSpeed(), rabbit.getSpeed());
	}
}
