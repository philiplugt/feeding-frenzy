/**
 * GameModelMediator.java
 * 
 * @author pxv8780
 * @date   2023/03/28
 */

interface GameModelMediator {

    public int getGameSize();

    public int getGameSpeed();

    public boolean getGamePaused();

    public void moveEnemy(Enemy enemy);

}