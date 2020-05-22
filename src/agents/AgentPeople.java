package agents;

public class AgentPeople extends AgentType 
{

	private static final long serialVersionUID = 1L;

	public AgentPeople(int x, int y) {
		super(x, y);
	}

	public AgentPeople(int x, int y, double velocity, double radius, double angle) {
		super(x, y, velocity, radius, angle);
	}

}
