package model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import javax.imageio.ImageIO;

import org.eclipse.swt.graphics.Image;

import view.Tile;

public class PipeGameModel extends Observable implements Model{
	
	char[][] boardData = new char[4][4];
	private char[] unstraightPipes = {'7', 'J', 'L', 'F'};
	private char[] straightPipes = {'|', '-'};

	public PipeGameModel() {
		// Initialize first board state
		boardData[0][0] = 's';
		boardData[0][1] = '-';
		boardData[0][2] = '|';
		boardData[0][3] = ' ';
		boardData[1][0] = 'F';
		boardData[1][1] = 'L';
		boardData[1][2] = 'L';
		boardData[1][3] = '7';
		boardData[2][0] = ' ';
		boardData[2][1] = '-';
		boardData[2][2] = '|';
		boardData[2][3] = 'J';
		boardData[3][0] = 'L';
		boardData[3][1] = 'J';
		boardData[3][2] = 'g';
		boardData[3][3] = ' ';
		
	}
	
	@Override
	public void rotate(int x, int y) throws IOException {
		//char pipeType = pipeTileToRotate.pipeType;
		char pipeType = this.boardData[x][y];
		char[] pipesFamily = this.unstraightPipes;
		if (pipeType == '-' || pipeType == '|') {
			pipesFamily = this.straightPipes;
		}
		for (int i = 0; i < pipesFamily.length; i++)
		{
			if (pipeType == pipesFamily[i] && (i + 1) < pipesFamily.length)
			{
				pipeType = pipesFamily[i + 1];
				break;
			}
			else if(pipeType == pipesFamily[i])
			{
				pipeType = pipesFamily[0];
				break;
			}
		}
		//Should only change the boardData and then setChanged() -> then the presenter will update the Tiles.
		boardData[x][y] = pipeType;
		setChanged();
		notifyObservers();
	}

	@Override
	public char[][] getData() {
		return boardData;
	}
	
	@Override
	public void setData(char[][] data) {
		this.boardData = new char[data.length][data.length];
		for(int i=0;i<data.length;i++) {
			for(int j=0;j<data.length;j++) {
				this.boardData[i][j] = data[i][j];
			}
		}
		setChanged();
		notifyObservers();
	}	

}
