package model;


import java.io.IOException;

import view.Tile;

public interface Model {
	public char[][]getData();
	public void rotate(int x, int y) throws IOException;
	public void setData(char[][] data);

}
