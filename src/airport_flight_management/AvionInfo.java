package airport_flight_management;

public class AvionInfo {

	
	private Position position;
	
	private String numRef;
	
	private State state;
	
	public AvionInfo() {
		
	}
	public AvionInfo(String numRef) {
		this.numRef = numRef;
	}	
	public AvionInfo(Position position, String numRef) {
		super();
		this.position = position;
		this.numRef = numRef;
	}
	
	public AvionInfo(String numRef, State state) {
		super();
		this.numRef = numRef;
		this.state = state;
	}
	public AvionInfo(Position position, String numRef, State state) {
		super();
		this.position = position;
		this.numRef = numRef;
		this.state = state;
	}
	
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public String getNumRef() {
		return numRef;
	}
	public void setNumRef(String numRef) {
		this.numRef = numRef;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	
	
	
}
