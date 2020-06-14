package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import agents.*;
import clearpath.Obstacle;
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
import sim.portrayal.simple.RectanglePortrayal2D;
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

		try {
			display = new Display2D(Constants.FRAME_SIZE, Constants.FRAME_SIZE, this) {
				private static final long serialVersionUID = 1L;
				// The Image to store the background image in.
			    Image img = ImageIO.read(new File("src/images/shibuya.jpg"));
			    @Override
			    public void paintComponent(Graphics g)
			    {
			        super.paintComponent(g);
			    	g.drawImage(img, 0, 0, null);
			    }
			    
			};
		} catch (IOException e) {
			display = new Display2D(Constants.FRAME_SIZE, Constants.FRAME_SIZE, this);
			e.printStackTrace();
		}
		display.setClipping(false);

		displayFrame = display.createFrame();
		displayFrame.setTitle(View.getName());

		c.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		display.attach(yardPortrayal, "Yard");
	}

	public void setupPortrayals()
	{
		Model model = (Model) state;
		yardPortrayal.setField(model.getYard());
		
		// display AgentPeople 
		yardPortrayal.setPortrayalForClass(AgentPeople.class,
				new MovablePortrayal2D(
						new OrientedPortrayal2D(
								new OvalPortrayal2D()
								{
									private static final long serialVersionUID = 1L;
				
									public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
									{
										AgentPeople a = (AgentPeople) object;
										paint = a.getColorType();
										filled = true;
										scale = Constants.SCALE_AGENT;
										super.draw(object, graphics, info);
									}
								})
						{
							private static final long serialVersionUID = 1L;
		
							@Override
							public double getOrientation(Object object, DrawInfo2D info)
							{
								AgentPeople a = (AgentPeople) object;
								return a.getAngle();
							}
						}));

		display.reset();
		display.repaint();
	}

	public Object getSimulationInspectedObject()
	{
		return state;
	}

	public Inspector getInspector() 
	{
		Inspector i = super.getInspector();
		i.setVolatile(true);

		return i;
	}

}