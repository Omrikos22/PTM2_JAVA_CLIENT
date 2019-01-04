package view;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

public class Board extends Composite{
	
	Image[][] boardData;
	Tile[][] tiles;
	int N;
	
	public Board(Composite parent, int style, char[][] boardData) throws IOException {
		super(parent, style);	// canvas Constructor
		setLayout(new GridLayout(4, true));
		
		Image StartEnd = new Image(this.getDisplay(), "resources/StartEnd.jpg");
		Image SevenPipe = new Image(this.getDisplay(), "resources/7Pipe.jpg");
		Image FPipe = new Image(this.getDisplay(), "resources/FPipe.jpg");
		Image JPipe = new Image(this.getDisplay(), "resources/JPipe.jpg");
		Image LPipe = new Image(this.getDisplay(), "resources/LPipe.jpg");
		Image Straight = new Image(this.getDisplay(), "resources/Straight.jpg");
		Image UpDown = new Image(this.getDisplay(), "resources/UpDown.jpg");
		Image EmptyTile = new Image(this.getDisplay(), "resources/EmptyTile.png");
	
		Map<Character, Image> pipeImageAndTypeMap = new HashMap<Character,Image>(){{
			put('s', StartEnd);
			put('g', StartEnd);
			put('7', SevenPipe);
			put('F', FPipe);
			put('J', JPipe);
			put('L', LPipe);
			put('-', Straight);
			put('|', UpDown);
			put(' ', EmptyTile);
		}};
		int boardLength = boardData.length;
		
		this.tiles = new Tile[boardLength][boardLength];
		this.boardData = new Image[boardLength][boardLength];
		for(int i=0;i<boardData.length;i++)
			for(int j=0;j<boardData.length;j++)
			{
				this.boardData[i][j] = pipeImageAndTypeMap.get(boardData[i][j]);
			}
		
		for(int i = 0; i < boardData.length; i++)
			for(int j = 0;j < boardData.length;j++) {
				tiles[i][j] = new Tile(this, SWT.BORDER, i, j, boardData[i][j]);
				tiles[i][j].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				tiles[i][j].setValue(this.boardData[i][j], boardData[i][j]);
			}
		
		
		
	}

}
