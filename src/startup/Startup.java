package startup;

import gui.GuiPathChoice;

public class Startup{
	
	// GuiElemente
	private static GuiPathChoice pathchocie;
	private static RunProgramm runprogramm;
	
	public static void main(String[] args) {
		runprogramm = new RunProgramm();
		askForPath();
	}
	
	private static void askForPath(){
		pathchocie = new GuiPathChoice(runprogramm);
		pathchocie.setVisible(true);
	}
}
