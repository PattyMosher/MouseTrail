package trail;

import math.Vector;

@FunctionalInterface
public interface DrawInterface{
	
	void draw( Vector middle, Vector perpendicularCenterX, Vector perpendicularCenterY);
}
