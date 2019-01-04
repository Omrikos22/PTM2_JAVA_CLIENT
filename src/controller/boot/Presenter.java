package controller.boot;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.graphics.Image;

import model.Model;
import view.Tile;
import view.View;

public class Presenter implements Observer{
	
	View ui;
	Model m;
	
	public Presenter(View ui, Model m) {
		this.ui = ui;
		this.m = m;
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o == ui) {
			// Something changed at the view
			Tile input = ui.getUserCommand();
			char[][] levelToLoad = ui.getLevelToLoad();
			try {
				if(levelToLoad != null) {
					ui.setLevelToLoad(null);
					this.m.setData(levelToLoad);
				}
				else if(input == null) {
					char[][] currentBoard = this.m.getData();
					StringBuilder output = new StringBuilder();
					for(int i=0;i< currentBoard.length;i++) {
						for(int j=0;j<currentBoard.length;j++) {
							output.append(currentBoard[i][j]);
						}
						output.append('\n');
					}
					output.append("done\n");
					// Get solution from server
					try (Socket socket = new Socket("", 6400)) {
						 
			            InputStream inputS = socket.getInputStream();
			            OutputStream outputS = socket.getOutputStream();
						byte[] data = String.valueOf(output).getBytes();
						outputS.write(data);
						outputS.flush();
						
						BufferedInputStream bis = new BufferedInputStream(inputS);
						int result = bis.read();
						ByteArrayOutputStream buf = new ByteArrayOutputStream();
						String message = new String();
			            while (!message.contains("done")) {
			            	buf.write((byte) result);
			     		    result = bis.read();
			     		    message = buf.toString("UTF-8");
			            }
			            buf.flush();
			            String messageToClient = message.substring(0, message.length() - 4);
			            System.out.println("Client got this message: ");
			            String steps[] = messageToClient.split("\n");
			            for(int i=0;i<steps.length;i++) {
			            	String[] splittedStep = steps[i].split(",");
			            	int rowIndexPipeToRotate = Integer.parseInt(splittedStep[0]);
			            	int columnIndexPipeToRotate = Integer.parseInt(splittedStep[1]);
			            	int timesToRotate = Integer.parseInt(splittedStep[2]);
			            	for(int j=0;j<timesToRotate;j++) {
			            		this.m.rotate(rowIndexPipeToRotate, columnIndexPipeToRotate);
			            	}
			            			
			            }
			 
			 
			        } catch (UnknownHostException ex) {
			 
			            System.out.println("Server not found: " + ex.getMessage());
			 
			        } catch (IOException ex) {
			 
			            System.out.println("I/O error: " + ex.getMessage());
			        }
			  
				}
				else {
					m.rotate(input.x, input.y);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Rotate the pipe
		}
		
		if(o == m) {
			// Something changed at the model
			ui.displayData(m.getData());
		}
		
	}

}
