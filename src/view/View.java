package view;

import org.eclipse.swt.graphics.Image;

public interface View {
	public void displayData(char[][] data);
	public Tile getUserCommand();
	public void setUserCommand(Tile tileCommanded);
	public char[][] getLevelToLoad();
	public void setLevelToLoad(char[][] levelBuffer);

}
