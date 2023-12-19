/**
 * ViewObserver.java
 * 
 * @author pxv8780
 * @date   2023/03/28
 */

import java.awt.Color;

interface ViewObserver {

	public void onMessageNotification(String event, String message);

	public void onActionNotification(String event, int x, int y, int x0, int y0, char name, Color color);

}