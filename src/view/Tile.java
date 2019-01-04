package view;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class Tile extends Canvas{
	
	private Image value;
	public int x; // X coordinate on board
	public int y; // Y coordinate on board
	public char pipeType;
	
	public Tile(Composite parent, int style, int x, int y, char pipeType) {
		super(parent, style);
		this.value = null;
		this.x = x;
		this.y = y;
		this.pipeType = pipeType;
		
		Font f = getFont();
		Font nf = new Font(getDisplay(), f.getFontData()[0].getName(), 16, SWT.BOLD);
		setFont(nf);
		addPaintListener(new PaintListener() {
			// Will call this code when the tile asked to get painted
			
			@Override
			public void paintControl(PaintEvent e) {
				FontMetrics fm = e.gc.getFontMetrics();
				int width = fm.getAverageCharWidth();
				int mx = getSize().x/20 - ("" + value).length() * width / 20;
				int my = getSize().y/20 - fm.getHeight()/20 - fm.getDescent();
				if(value != null)
					//setBackgroundImage(value);
					e.gc.drawImage(value, 0, 0);
				
			}
		});
	}
	
	public void setValue(Image value, char pipeType) {
		this.value = value;
		this.pipeType = pipeType;
		changeBackgroundColor();
		redraw();
	}
	
	private void changeBackgroundColor() {
		if(value == null)
			setBackground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		else
			setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
	}

}
