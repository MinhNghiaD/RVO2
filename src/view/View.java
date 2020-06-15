package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import agents.*;
import model.Model;
import model.*;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.simple.MovablePortrayal2D;
import sim.portrayal.simple.OrientedPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.FieldPortrayal2D;
import sim.portrayal.Inspector;
import sim.portrayal.Portrayal2D;

public class View extends GUIState 
{
	public Display2D display;
	public JFrame displayFrame;
	public ContinuousPortrayal2D yardPortrayal = new ContinuousPortrayal2D();

	public View(SimState state) 
	{
		super(state);
	}
	
//	public View(SimState state, String backgroundPath)
//	{
//		
//	}

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

		display = new Display2D(Constants.FRAME_SIZE, Constants.FRAME_SIZE, this);
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
		
		// display background		
		int scenarioId = model.getScenarionId();		
		if(scenarioId < backgroundPath.length && scenarioId >= 0)
		{
			String path = backgroundPath[scenarioId];		
			Image i = new ImageIcon(getClass().getResource(path)).getImage();
			BufferedImage b = display.getGraphicsConfiguration().createCompatibleImage(i.getWidth(null), i.getHeight(null));
			Graphics g = b.getGraphics();
			g.drawImage(i,0,0,i.getWidth(null),i.getHeight(null),null);
			g.dispose();
			display.setBackdrop(new TexturePaint(b, new Rectangle(0,0,i.getWidth(null),i.getHeight(null))));
		}
		
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
	
	public static String [] backgroundPath = 
	{
		"/images/shibuya.jpg",
		"/images/shibuya.jpg",
		"/images/shibuya.jpg",
		"/images/shibuya.jpg",
	};

}