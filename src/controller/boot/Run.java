package controller.boot;

import model.PipeGameModel;
import view.PipeGameView;

public class Run {
	public static void main(String[] args) {
		PipeGameModel m = new PipeGameModel();
		PipeGameView ui = new PipeGameView(m.getData());
		Presenter p = new Presenter(ui, m);
		ui.addObserver(p);
		m.addObserver(p);
		//new Thread(ui).start(); # Check it out on windows at the end - should work
		ui.run();
		
	}

}
