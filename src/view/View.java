package view;

import java.awt.Color;

import javax.swing.JFrame;

import agents.*;
import model.Model;
import model.*;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Portrayal;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.Inspector;

public class View extends GUIState 
{
	public Display2D display;
	public JFrame displayFrame;
	public SparseGridPortrayal2D yardPortrayal = new SparseGridPortrayal2D();
	
		
	public View(SimState state) 
	{
		super(state);
	}
	
	public static String getName() 
	{ 
		return "ClearPath"; 
	}
	
	public void start() 
	{ 
		super.start(); 
		setupPortrayals(); 
	}
	
	public void load(SimState state) 
	{
		super.load(state);
		setupPortrayals();
	} 
	
	public void init(Controller c) 
	{
		super.init(c);
				
		display = new Display2D(Constants.FRAME_SIZE,Constants.FRAME_SIZE,this);
		display.setClipping(false);
		
		
		displayFrame = display.createFrame();
		displayFrame.setTitle(View.getName());
		
		c.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		display.attach( yardPortrayal, "Yard" );
	}
		
	public void setupPortrayals() 
	{ 
		Model model = (Model) state;
		yardPortrayal.setField(model.yard);
		yardPortrayal.setPortrayalForClass( AgentPeople.class, getAgentPortrayal());
		display.reset(); 
		display.setBackdrop(Color.LIGHT_GRAY);
		display.repaint();
	}


	private Portrayal getAgentPortrayal()
	{ 
		OvalPortrayal2D r = new OvalPortrayal2D();
		r.paint = Color.DARK_GRAY;
		r.filled = true;
		return r;
	}

	public Object getSimulationInspectedObject()
	{
		return state;
	}
	
	public Inspector getInspector()
	{
		Inspector i= super.getInspector();
		i.setVolatile(true);
		return i;
	}
	

}