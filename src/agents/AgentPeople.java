package agents;

public class AgentPeople extends AgentType 
{

	private static final long serialVersionUID = 1L;

	public AgentPeople(double x, double y) {
		super(x, y);
	}

	public AgentPeople(double x, double y, double velocity, double radius, double angle) {
		super(x, y, velocity, radius, angle);
	}

}
