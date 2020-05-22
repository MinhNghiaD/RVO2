package main;

import sim.display.Console;
import view.View;
import model.Model;

public class Main 
{
	public static void main(String[] args) 
	{
		Model model = new Model(System.currentTimeMillis()); 
		View gui = new View(model);
		Console console = new Console(gui);
		console.setVisible(true);
	}
}