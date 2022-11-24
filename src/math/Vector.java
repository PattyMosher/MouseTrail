package math;


public class Vector{

	private double x, y;

	public Vector( double x, double y){
		set( x, y);
	}

	private void set( double x, double y){
		this.x = x;
		this.y = y;
	}

	public double getX(){
		return x;
	}

	public double getY(){
		return y;
	}

	public Vector perpendicularX(){
		return new Vector( y, -x);
	}

	public Vector perpendicularY(){
		return new Vector( -y, x);
	}

	public Vector mult( double length){
		return new Vector( x * length, y * length);
	}

	public Vector add( Vector vec){
		return new Vector( x + vec.x, y + vec.y);
	}

	public Vector add( double x, double y){
		return new Vector( x + this.x, y + this.y);
	}

	public Vector sub( Vector vec){
		return new Vector( x - vec.x, y - vec.y);
	}
}
