package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sportapp.Clock;

class TestClock {
	private Clock testClock;
	
	@BeforeEach
	public void setUp() {
		testClock = Clock.getInstance();
	}

	@Test
	public void testGetNow() {
		LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
		assertEquals(now, testClock.getNow());
	}
	
	@Test
	public void testGetToday() {
		LocalDate today = LocalDate.now();
		assertEquals(today, testClock.getToday());
	}

	@Test
	public void testGetHour() {
		int currentHour = LocalTime.now().getHour();
		assertEquals(currentHour, testClock.getHour());
	}
}
