package airport_flight_management;

import java.io.Serializable;
import java.lang.Math;


public class Position implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7233459839599909384L;
	private float x, y, z;
	
	public Position() {
		this.x = -1;
		this.y = -1;
		this.z = -1;
	}

	public Position(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	public float calculerDistance(float x, float y) {
		
		return (float) Math.sqrt(Math.pow((x-this.x), 2) + Math.pow((y-this.y), 2) );
	
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
	
	

}