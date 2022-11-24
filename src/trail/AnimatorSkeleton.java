package trail;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import math.Vector;

public class AnimatorSkeleton extends AnimationTimer{

	private static final double ONE_SECOND = 1000000000L;
	private static final double HALF_SECOND = ONE_SECOND / 2F;
	private static final int MAX_VECTORS = 360;
	private final Random RAND = new Random();

	private LinkedList< Vector> trailVectors;
	private GraphicsContext gc;
	private Vector lastVector;
	private String fpsDisplay;
	private Canvas canvas;
	private boolean clean;
	private int frameCount;
	private double hue, hueShift, lastTime;

	public AnimatorSkeleton( Canvas canvas){
		this.canvas = canvas;
		gc = canvas.getGraphicsContext2D();
//		hue = 0;
//		hueShift = 0;
//		lastTime = 0;
	}

	public void start(){
		if( trailVectors == null)
			trailVectors = new LinkedList<>();
		trailVectors.clear();
		frameCount = 0;
		clean = false;
		lastTime = System.nanoTime();
		super.start();
	}

	public void clean(){
		clean = true;
	}

	public void addStartVector( double x, double y){
		clean = false;
		lastVector = new Vector( x, y);
	}

	public double rand( double min, double max){
		return min + (max - min) * RAND.nextDouble();
	}

	public double roughRand( double min, double max, double minScale, double maxScale){
		if( min > max){
			double temp = min;
			min = max;
			max = temp;
		}
		return rand( min * rand( minScale, maxScale), max * rand( minScale, maxScale));
	}

	public void clearCanvas(){
		gc.clearRect( 0, 0, canvas.getWidth(), canvas.getHeight());
	}

	public void displayFPS(){
		gc.setFont( Font.font( gc.getFont().getFamily(), FontWeight.BLACK, 24));
		gc.setStroke( Color.WHITE);
		gc.setLineWidth( 1);
		gc.strokeText( fpsDisplay, 10, 25);
	}

	public void drawPerpendicularLines( Vector middle, Vector perpendicularCenterX, Vector perpendicularCenterY){
		gc.setLineWidth( 5);
		gc.setStroke( Color.hsb( (hue += .5f) % 360, 1, 1));
		gc.strokeLine( middle.getX(), middle.getY(), perpendicularCenterX.getX(), perpendicularCenterX.getY());
		gc.strokeLine( middle.getX(), middle.getY(), perpendicularCenterY.getX(), perpendicularCenterY.getY());
	}

	public void drawCloud( Vector middle, Vector perpendicularCenterX, Vector perpendicularCenterY){
		gc.setFill( Color.hsb( (hue += .5f) % 360, 1, .7));
		for( int i = 0; i < 100; i++){
			double x = roughRand( perpendicularCenterY.getX(), perpendicularCenterX.getX(), .95, 1.05);
			double y = roughRand( perpendicularCenterY.getY(), perpendicularCenterX.getY(), .95, 1.05);
			gc.fillOval( x, y, 2, 2);
		}
	}
	public void removeFirst(int count) {
		for( int i =0; i < count && !trailVectors.isEmpty(); i++)
			trailVectors.removeFirst();
	}
	
	@Override
	public void handle(long now) {
		if (lastVector != null) {
			calculateFPS(now);
			clearCanvas();
			shortenTrail(5);
			drawingLoop(trailVectors, (m,px,py)-> drawCloud(m,px,py));
			resetColorAndVector();
			drawingLoop(trailVectors, (m,px,py)-> drawPerpendicularLines(m,px,py));
			resetColorAndVector();
			displayFPS();
		}
	}
	public void resetColorAndVector() {
		++hueShift;
		hue = hueShift%360;
//		hue = ++hueShift%360;
		
		if (!trailVectors.isEmpty()) {
			lastVector = trailVectors.getFirst();
		}
	}

	public void addVector(double x, double y) {
		if (trailVectors.size() >= MAX_VECTORS)
			removeFirst(1);
		trailVectors.addLast(new Vector(x,y));
	}
	
	public void drawingLoop(List<Vector> trailVectors, DrawInterface drawInterface) {
		Vector middle;
		Vector perpendicularCenterX;
		Vector perpendicularCenterY;
		Vector perpendicularX;
		Vector perpendicularY;
		Vector currentVector;
		
		for(Vector v: trailVectors) {		
			currentVector = v.sub(lastVector);
			v.mult(4);
			perpendicularY = v.perpendicularY();
			currentVector = v.sub(lastVector);
			v.mult(4);
			perpendicularX = v.perpendicularX();
			v.add(v);
			middle = (v.mult(0.5));
			v.add(middle);
			perpendicularCenterY = v.add(middle);
			v.add(middle);
			perpendicularCenterX = v.add(middle);
			
			drawInterface.draw(middle, perpendicularCenterX, perpendicularCenterY);
			lastVector = currentVector;
		}
		
	}
	void shortenTrail(int count){
		if(clean && !trailVectors.isEmpty()) removeFirst(count);
	}
	
	void calculateFPS(long now) {
		if (now - lastTime >= HALF_SECOND) {
			fpsDisplay = String.valueOf(frameCount*2);
			frameCount = 0; lastTime = now;
		}
		frameCount++;
	}
}
