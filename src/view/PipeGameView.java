package view;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class PipeGameView extends Observable implements View,Runnable{
	
	Display display;
	Shell shell;
	Board board;
	Menu menuBar, fileMenu, helpMenu;
	 MenuItem fileMenuHeader, helpMenuHeader;
	 MenuItem fileExitItem, fileSaveItem, helpGetHelpItem;
	char[][] boardData;
	Tile clickedTile;
	char[][] levelToLoad;
	private Map<Character, String> pipeImageAndTypeMap = new HashMap<Character,String>(){{
		put('s', "resources/StartEnd.jpg");
		put('g', "resources/StartEnd.jpg");
		put('7', "resources/7Pipe.jpg");
		put('F', "resources/FPipe.jpg");
		put('J', "resources/JPipe.jpg");
		put('L', "resources/LPipe.jpg");
		put('-', "resources/Straight.jpg");
		put('|', "resources/UpDown.jpg");
		put(' ', "resources/EmptyTile.png");
	}};
	
	public PipeGameView(char[][] boardData) {
		this.boardData = boardData;
	}
	
	private void initComponents() throws IOException {
		display = new Display();
		shell = new Shell(display);
		Image backgroundImange = new Image(this.display, "resources/bgImage.jpg");
		shell.setBackgroundImage(backgroundImange);
		shell.setLayout(new GridLayout(2, false));
		shell.setSize(900, 650);
		shell.setText("Best PipeGame");
		
		try {
	         // OMusic with audio file to the game
	         URL url = this.getClass().getClassLoader().getResource("gameMusic.wav");
	         AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	         // Get a sound clip resource.
	         Clip clip = AudioSystem.getClip();
	         // Open audio clip and load samples from the audio input stream.
	         clip.open(audioIn);
	         clip.start();
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
		
		
	    menuBar = new Menu(shell, SWT.BAR);
	    fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    fileMenuHeader.setText("&File");

	    fileMenu = new Menu(shell, SWT.DROP_DOWN);
	    fileMenuHeader.setMenu(fileMenu);

	    fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileSaveItem.setText("&Save");

	    fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileExitItem.setText("E&xit");

	    helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    helpMenuHeader.setText("&Help");

	    helpMenu = new Menu(shell, SWT.DROP_DOWN);
	    helpMenuHeader.setMenu(helpMenu);

	    helpGetHelpItem = new MenuItem(helpMenu, SWT.PUSH);
	    helpGetHelpItem.setText("&Get Help");

	   
	    shell.setMenuBar(menuBar);
		
		Button b1 = new Button(shell, SWT.PUSH);
		b1.setText("Load");
		b1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		b1.addSelectionListener(new SelectionAdapter() {
		    @Override
	        public void widgetSelected(SelectionEvent e) {
		    	// User want to load a level
	            System.out.println("Load Clicked!");
	            FileDialog fd = new FileDialog(shell, SWT.OPEN);
	            fd.setFilterPath("C:/");
	            String[] filterExtenstions = {".txt"};
	            fd.setFilterExtensions(filterExtenstions);
	            String selectedLevel = fd.open();
	            try {
	            	if(selectedLevel != null) {
	            		BufferedReader br = new BufferedReader(new FileReader(selectedLevel));
						int i = 0;
						String currentLine = br.readLine();
						char[][] levelToLoad = new char[currentLine.length()][currentLine.length()];
						while(currentLine != null) {
							char[] currentCharLine = currentLine.toCharArray();
							for(int j=0;j<currentCharLine.length;j++) {
								levelToLoad[i][j] = currentCharLine[j];
							}
							i += 1;
							currentLine = br.readLine();
						}
						setLevelToLoad(levelToLoad);
						setChanged();
						notifyObservers();
	            	}
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        }
	    });
		
		
		this.board = new Board(shell, SWT.PUSH, this.boardData);
		GridData data = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 2);
		data.heightHint = 500;
		data.widthHint = 500;
		this.board.setLayoutData(data);
		this.board.setSize(600, 400);
		this.mouseListener();
		Button b2 = new Button(shell, SWT.PUSH);
		b2.setText("Solve");
		b2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		b2.addSelectionListener(new SelectionAdapter() {
		    @Override
	        public void widgetSelected(SelectionEvent e) {
	            System.out.println("Solve Clicked!");
	            setChanged();
	            notifyObservers();
	        }
	    });
		
		shell.open();
		
	}
	
	@Override
	public void run(){
		try {
			initComponents();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(!shell.isDisposed()){
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	@Override
	public void displayData(char[][] data) {
		for(int i=0;i<data.length;i++)
			for(int j=0;j<data.length;j++)
			{
				String pipeImagePath = this.pipeImageAndTypeMap.get(data[i][j]);
				Image pipeImage = new Image(this.board.getDisplay(), pipeImagePath);
				this.board.tiles[i][j].setValue(pipeImage, data[i][j]);
			}
		
	}

	@Override
	public void setUserCommand(Tile clickedTile) {
		this.clickedTile = clickedTile;
	}
	
	@Override
	public Tile getUserCommand() {
		return clickedTile;
	}
	
	@Override
	public void setLevelToLoad(char[][] levelBuffer) {
		this.levelToLoad = levelBuffer;
	}
	
	@Override
	public char[][] getLevelToLoad() {
		return levelToLoad;
	}
	
	 public void mouseListener(){
		 Board board = this.board;
		 for(int i=0;i<4;i++)
			 for(int j=0;j<4;j++) {
				 this.board.tiles[i][j].addMouseListener(new MouseListener() {
						@Override
						public void mouseDoubleClick(MouseEvent e) {
							//
							
						}
						@Override
						public void mouseDown(MouseEvent e) {
							//
							
						}
						@Override
						public void mouseUp(MouseEvent e) {
							//User clicked on the board
							Tile clickedTile = (Tile) e.getSource();
							setUserCommand(clickedTile);
							setChanged();
							notifyObservers();
							setUserCommand(null);
						}
			         
			        });
			 }
	       
	    }

}
