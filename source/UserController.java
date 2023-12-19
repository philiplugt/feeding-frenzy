/**
 * UserController.java
 * 
 * @author pxv8780
 * @date   2023/03/28
 */

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;

import javax.swing.SwingUtilities;

public class UserController implements AWTEventListener {

	private final Model model;
	private static View view;

	public UserController(View view, Model model) {
		UserController.view = view;
		this.model = model;
		this.model.registerObserver(view);
		addAllListeners();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				view.init();
			}
		});
	}

	private void addAllListeners() {
		view.pauseCheckBox.addItemListener(new PauseListener());
		view.increaseButton.addActionListener(new IncreaseButtonListener());
		view.decreaseButton.addActionListener(new DecreaseButtonListener());
		view.restartButton.addActionListener(new RestartButtonListener());
		view.quitButton.addActionListener(new QuitButtonListener());
		for (int i = 0; i < model.size; i++) {
			for (int j = 0; j < model.size; j++) {
				view.gridButtons[i][j].addActionListener(new GridButtonListener(i, j));
			}
		}

		// Allow controller to listen for AWTEvents (keys)
		view.getToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
	}

	public void eventDispatched(AWTEvent awt) {
		if (awt instanceof KeyEvent key) {
			if (key.getID() == KeyEvent.KEY_PRESSED && !model.paused) {
				switch (key.getKeyCode()) {
					case KeyEvent.VK_UP -> model.movePlayer(0);
					case KeyEvent.VK_RIGHT -> model.movePlayer(1);
					case KeyEvent.VK_DOWN -> model.movePlayer(2);
					case KeyEvent.VK_LEFT -> model.movePlayer(3);
				}
			}
		}
	}

	// GridButtonListener class
	class GridButtonListener implements ActionListener {

		private final int x, y;
		
		GridButtonListener(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public void actionPerformed(ActionEvent e) {
			model.addEnemy(x, y);
		}
	}

	// Quits the program. And adds a listener for the button which will do this.
	class QuitButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			model.quit();
		}
	}

	// Pauses the program and adds a listener for the selected CheckBox
	class PauseListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (!model.paused) {
				model.pause();
			} else {
				model.unpause();
			}
		}
	}
	
	// Increases the enemy speed, adds a listener for the button which will do this
	class IncreaseButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e)	 {
			model.increaseSpeed();
		}
	}
	
	// Decreases the enemy speed and adds a listener for the button which will do this
	class DecreaseButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			model.decreaseSpeed();
		}
	}

	// Restarts the program and adds a listener for the button that will do this
	class RestartButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			model.reset();
		}
	}
}