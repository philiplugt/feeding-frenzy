/**
 * Model.java
 * 
 * @author pxv8780
 * @date   2023/03/28
 */

import java.awt.Color;
import java.lang.Thread;
import java.util.Random;

import java.util.Timer;
import java.util.TimerTask;


// Model handles data for the program
public class Model implements GameModelMediator {
	
	// Member variables
	Random r = new Random();
	int enemiesEaten = 0;
	boolean paused = false;
	int speed = Constants.SPEED;

	// Constructor variables
	int size;
	Enemy[][] board;
	ViewObserver observer;
	Player player;
	Timer timer;

	public Model(int size) {
		this.size = size;
		board = new Enemy[size][size];
	}

	public void start() {
		addPlayer();
		startTimer();
	}

	public void registerObserver(ViewObserver observer) {
		this.observer = observer;
	}

	public void notifyObserverOfMessage(String event, String message) {
		observer.onMessageNotification(event, message);
	}

	public void notifyObserverOfAction(String event, 
			int x, int y, int x0, int y0,
			char name, Color color) {
		observer.onActionNotification(event, x, y, x0, y0, name, color);
	}

	public void addPlayer() {
		player = new Player(this, r.nextInt(size), r.nextInt(size));
		playerEat();
		notifyObserverOfAction(Constants.CREATE_UPDATE, 
			player.x, player.y, player.x0, player.y0, player.name, player.color);
	}

	public synchronized void movePlayer(int move) {
		if (player != null) {
			player.move(move);
			playerEat();
			notifyObserverOfAction(Constants.MOVE_UPDATE, 
				player.x, player.y, player.x0, player.y0, player.name, player.color);
		}
	}

	public synchronized void playerEat() {
		if (board[player.x][player.y] != null) {
			incrementEaten();
			eatenMessage(player.name, board[player.x][player.y].name);
			board[player.x][player.y].alive = false;
			board[player.x][player.y] = null;
		}
	}

	public void addEnemy() {
		Enemy enemy = new Enemy(this);
		enemyEat(enemy);
		notifyObserverOfAction(Constants.CREATE_UPDATE, 
			enemy.x, enemy.y, enemy.x0, enemy.y0, enemy.name, enemy.color);
		new Thread(enemy).start();
	}
	
	public void addEnemy(int x, int y) {
		Enemy enemy = new Enemy(this, x, y);
		enemyEat(enemy);
		notifyObserverOfAction(Constants.CREATE_UPDATE, 
			enemy.x, enemy.y, enemy.x0, enemy.y0, enemy.name, enemy.color);
		new Thread(enemy).start();
	}

	public synchronized void moveEnemy(Enemy enemy) {
		enemyEat(enemy);
		notifyObserverOfAction(Constants.MOVE_UPDATE, 
			enemy.x, enemy.y, enemy.x0, enemy.y0, enemy.name, enemy.color);
	}

	public synchronized void enemyEat(Enemy e) {
		// If board square is occupied, eat it.
		if (board[e.x][e.y] != null) {
			eatenMessage(e.name, board[e.x][e.y].name);
			board[e.x][e.y].alive = false;
			board[e.x][e.y] = null;
		}

		// Update board to occupy square
		board[e.x0][e.y0] = null;
		board[e.x][e.y] = e;

		// If the player was eaten, end the game
		if (player != null && player.x == e.x && player.y == e.y) {
			eatenMessage(e.name, player.name);
			gameOver();
		}
	}

	public void disposeEnemies() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (board[i][j] != null) {
					board[i][j].alive = false;
					board[i][j] = null;
				}
			}
		}
	}

	// Methods to handle speed changes
	public void increaseSpeed() {
		if (speed > 10) {
			speed -= 50;
			notifyObserverOfMessage(Constants.TEXT_UPDATE, "Increased speed!");
		} else {
			notifyObserverOfMessage(Constants.TEXT_UPDATE, "Maximum speed!");
		}	
	}

	public void decreaseSpeed() {
		speed += 50;
		notifyObserverOfMessage(Constants.TEXT_UPDATE, "Decreased speed!");
	}

	public void resetSpeed() {
		speed = Constants.SPEED;
	}

	// Methods to handle pausing and unpausing of the game
	public void pause() {
		paused = true;
		timer.cancel();
		notifyObserverOfMessage(Constants.TEXT_UPDATE, "Paused!");
	}

	public void unpause() {
		paused = false;
		startTimer();
		notifyObserverOfMessage(Constants.TEXT_UPDATE, "Unpaused!");
	}

	// Methods to deal with amount eaten label
	public void eatenMessage(char a, char b) {
		notifyObserverOfMessage(Constants.TEXT_UPDATE, a + " ate " + b);
	}

	public void incrementEaten() {
		enemiesEaten += 1;
		notifyObserverOfMessage(Constants.COUNT_UPDATE, "" + enemiesEaten);
	}

	public void resetEaten() {
		enemiesEaten = 0;
		notifyObserverOfMessage(Constants.COUNT_UPDATE, "" + enemiesEaten);
	}

	public void gameOver() {
		timer.cancel();
		disposeEnemies();
		player = null;
		notifyObserverOfMessage(Constants.STATE_UPDATE, "Game Over!");
		notifyObserverOfMessage(Constants.DISABLE_UPDATE, null);
	}

	// Method for quitting the game
	public void quit() {
		timer.cancel();
		disposeEnemies();
		player = null;
		notifyObserverOfMessage(Constants.QUIT_UPDATE, null);
	}

	// Method for restarting the game
	public void reset() {
		timer.cancel();
		disposeEnemies();
		player = null;

		notifyObserverOfMessage(Constants.RESET_UPDATE, null);

		resetEaten();
		resetSpeed();

		notifyObserverOfMessage(Constants.TEXT_UPDATE, "Restarting.");
		notifyObserverOfMessage(Constants.TEXT_UPDATE, "Nothing eaten yet.");
		notifyObserverOfMessage(Constants.STATE_UPDATE, "Game Running!");

		addPlayer();
		if (!paused) {
			startTimer();
		}
	}

	// GameModelMediator
	public int getGameSize() {
		return size;
	}

	public int getGameSpeed() {
		return speed;
	}

	public boolean getGamePaused() {
		return paused;
	}

	// A timer to spawn enemies every few seconds
	private void startTimer() {
		timer = new Timer();
		timer.schedule(new TimerTask() { 
			public void run() {
				addEnemy();
			}
		}, Constants.FIRST_SPAWN_TIME, Constants.NEXT_SPAWN_TIME);
	}
}