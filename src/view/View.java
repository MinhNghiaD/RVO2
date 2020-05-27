package view;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JFrame;

import agents.*;
import model.Model;
import model.*;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.simple.MovablePortrayal2D;
import sim.portrayal.simple.OrientedPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.Inspector;

public class View extends GUIState 
{
	public Display2D display;
	public JFrame displayFrame;
	public ContinuousPortrayal2D yardPortrayal = new ContinuousPortrayal2D();
	
		
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
		yardPortrayal.setField(model.getYard());
		yardPortrayal.setPortrayalForClass(AgentPeople.class,
			new MovablePortrayal2D(
				new OrientedPortrayal2D( 
                    new OvalPortrayal2D(){
						private static final long serialVersionUID = 1L;
						public void draw(Object object, Graphics2D graphics, DrawInfo2D info){
							paint = Color.DARK_GRAY;
                        	filled = true;
                    		super.draw(object, graphics, info);
						}
					}
                 ) {
						private static final long serialVersionUID = 1L;
						@Override
						public double getOrientation(Object object, DrawInfo2D info) {
							AgentPeople a = (AgentPeople) object;
							return a.angle;
						}
					}
			)
		);
		display.reset(); 
		display.setBackdrop(Color.LIGHT_GRAY);
		display.repaint();
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