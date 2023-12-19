/**
 * Unit.java
 * 
 * @author pxv8780
 * @date   2023/03/28
 */

import java.awt.Color;
import java.util.Random;

abstract class Unit {

	// Where x0, y0 is previous location
	int x, y, x0, y0, size;
	char name;
	Color color;
	GameModelMediator m;
	volatile boolean alive;

	public Unit(GameModelMediator m, int x, int y) {
		this.size = m.getGameSize();
		this.m = m;
		this.x = x;
		this.y = y;
		alive = true;
	}

	public Unit(GameModelMediator m) {
		this.size = m.getGameSize();
		this.m = m;
		alive = true;
	}
}

class Player extends Unit {
   
	public Player(GameModelMediator m, int x, int y) {
		super(m, x, y);

		this.name = 'P';
		this.color = Color.YELLOW;
	}

	public void move(int num) {
		x0 = x;
		y0 = y;
		switch (num) {
			case 0 -> y = y - 1 < 0 ? size - 1 : y - 1; // Up
			case 1 -> x = x + 1 >= size ? 0 : x + 1; // Right
			case 2 -> y = y + 1 >= size ? 0 : y + 1; // Down
			case 3 -> x = x - 1 < 0 ? size - 1 : x - 1; // Left
		}
	}
}


class Enemy extends Unit implements Runnable {

	private final Random r = new Random();

	public Enemy(GameModelMediator m, int x, int y) {
		super(m, x, y);

		this.name = getRandomName();
		this.color = getRandomColor();
	}

	public Enemy(GameModelMediator m) {
		super(m);

		this.x = r.nextInt(size);
		this.y = r.nextInt(size);
		this.name = getRandomName();
		this.color = getRandomColor();
	}

	public void move() {
		x0 = x;
		y0 = y;
		switch (r.nextInt(3)) {
			case 0 -> y = y - 1 < 0 ? size - 1 : y - 1; // Up
			case 1 -> x = x + 1 >= size ? 0 : x + 1; // Right
			case 2 -> y = y + 1 >= size ? 0 : y + 1; // Down
			case 3 -> x = x - 1 < 0 ? size - 1 : x - 1; // Left
		}
	}

	public void run() {
		sleep(m.getGameSpeed());
		while (alive) {
			if (m.getGamePaused()) {
				sleep(1000);
			} else {
				move();
				m.moveEnemy(this);
				sleep(m.getGameSpeed());
			}
		}
	}

	public void sleep(int delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private Color getRandomColor() {
		return (new Color(r.nextInt(192) + 64, r.nextInt(192) + 64, r.nextInt(192) + 64));
	}

	private char getRandomName() {
		int c;
		do {
			c = r.nextInt(256);
		} while (c < 33 || (c >= 127 && c < 161));
		return (char) c;
	}
}