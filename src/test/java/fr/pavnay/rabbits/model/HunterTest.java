package fr.pavnay.rabbits.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

import org.junit.Test;

public class HunterTest {

	@Test
	public void testInRangeSuccess() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Rabbit rabbit = new Rabbit(new Point(10, 0));
		assertTrue("Rabbit is in hunter range", hunter.isInRange(rabbit));
	}
	
	@Test
	public void testInRangeFailure() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Rabbit rabbit = new Rabbit(new Point(11, 0));
		assertFalse("Rabbit is out of hunter range", hunter.isInRange(rabbit));
	}
	
	@Test
	public void testShootSuccess() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Rabbit rabbit = new Rabbit(new Point(0, 0));
		assertTrue("Rabbit is close to the hunter. Shot successfully", hunter.shoot(rabbit));
		assertEquals("Hunter's ammos decreases", 9, hunter.getAmmos());
	}
	
	@Test
	public void testShootNoMoreAmmosFailure() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Rabbit rabbit = new Rabbit(new Point(0, 0));
		hunter.shoot(rabbit);
		hunter.shoot(rabbit);
		hunter.shoot(rabbit);
		hunter.shoot(rabbit);
		hunter.shoot(rabbit);
		hunter.shoot(rabbit);
		hunter.shoot(rabbit);
		hunter.shoot(rabbit);
		hunter.shoot(rabbit);
		hunter.shoot(rabbit);
		assertFalse("No more ammos. Shot failure", hunter.shoot(rabbit));
	}
	
	@Test
	public void testShootFailure() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Rabbit rabbit = new Rabbit(new Point(10, 0));
		assertFalse("Rabbit is out of hunter's range. Shot failure", hunter.shoot(rabbit));
		assertEquals("Hunter's ammos decreases", 9, hunter.getAmmos());
	}
	
	@Test
	public void testGetAccuracyRate() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Rabbit rabbit = new Rabbit(new Point(5, 0));
		assertEquals("Hunter's half rate to shot", hunter.getAccuracyRate(rabbit), 0.5, 0);
	}
	
	@Test
	public void testGetAccuracyRateHalfAmmo() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Rabbit rabbit = new Rabbit(new Point(5, 0));
		hunter.setAmmos(5);
		assertEquals("Hunter's half rate to shot", hunter.getAccuracyRate(rabbit), 0.475, 0);
	}
	
	@Test
	public void testGetAccuracyRateHalfHungryLevel() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Rabbit rabbit = new Rabbit(new Point(5, 0));
		hunter.setHungryLevel(5);
		assertEquals("Hunter's half hungry level", hunter.getAccuracyRate(rabbit), 0.45, 0);
	}
	
	@Test
	public void testGetAccuracyRateFullHungryLevel() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Rabbit rabbit = new Rabbit(new Point(5, 0));
		hunter.setHungryLevel(10);
		assertEquals("Hunter's half hungry level", hunter.getAccuracyRate(rabbit), 0.4, 0.001);
	}
	
	@Test
	public void testGetAccuracyRateHalfAmmosHalfHungryLevel() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Rabbit rabbit = new Rabbit(new Point(5, 0));
		hunter.setAmmos(5);
		hunter.setHungryLevel(5);
		assertEquals("Hunter's half hungry level", hunter.getAccuracyRate(rabbit), 0.425, 0.001);
	}
	
	@Test
	public void testGetAccuracyRateHalfAmmosFullHungryLevel() {
		Hunter hunter = new Hunter(new Point(0, 0), 10);
		Rabbit rabbit = new Rabbit(new Point(5, 0));
		hunter.setAmmos(5);
		hunter.setHungryLevel(10);
		assertEquals("Hunter's half hungry level", hunter.getAccuracyRate(rabbit), 0.375, 0);
	}
}
