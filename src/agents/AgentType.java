package agents;

import model.Model;
import sim.engine.SimState;
import sim.engine.Steppable;

public class AgentType implements Steppable {

	private static final long serialVersionUID = 1L;
	public int x;
	public int y;

	@Override
	public void step(SimState state) {
		Model beings = (Model) state;
		beings.toString();
	}

}
