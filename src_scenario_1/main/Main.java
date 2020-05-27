package main;

import sim.display.Console;
import view.View;
import model.Model;

public class Main 
{
	public static void main(String[] args) 
	{
		String type = args[0];
		Model model = new Model(System.currentTimeMillis(), type); 
		View gui = new View(model);
		Console console = new Console(gui);
		console.setVisible(true);
		System.out.println(type);
	}
}