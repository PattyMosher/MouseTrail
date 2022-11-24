package trail;


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MouseTrail extends Application{

	private Canvas canvas;
	private AnimatorSkeleton animator;

	@Override
	public void init() throws Exception{
		super.init();
	}

	@Override
	public void start( Stage primaryStage) throws Exception{
		BorderPane rootPane = new BorderPane();

		canvas = new Canvas( 750, 750);
		animator = new AnimatorSkeleton( canvas);

		BorderPane canvasPane = new BorderPane();
		canvasPane.setStyle( "-fx-background-color: BLACK;");
		canvasPane.setCenter( canvas);

		rootPane.setCenter( canvasPane);

		Scene scene = new Scene( rootPane);
		primaryStage.setScene(scene);
		primaryStage.setTitle( "Mouse Trail");
		primaryStage.show();

		primaryStage.addEventHandler( KeyEvent.KEY_RELEASED, e -> {
			if( e.getCode() == KeyCode.ESCAPE)
				primaryStage.hide();
		});
		canvas.widthProperty().bind( rootPane.widthProperty());
		canvas.heightProperty().bind( rootPane.heightProperty());
		ChangeListener< Number> dimensionUpdate = ( observable, oldValue, newValue) -> {
			animator.clearCanvas();
		};
		canvas.widthProperty().addListener( dimensionUpdate);
		canvas.heightProperty().addListener( dimensionUpdate);

		registerMouseListeners();
		clearCanvasAndStart();
	}

	@Override
	public void stop() throws Exception{
		super.stop();
		animator.stop();
	}

	public void registerMouseListeners(){
		canvas.addEventHandler( MouseEvent.MOUSE_PRESSED, this::mousePressed);
		canvas.addEventHandler( MouseEvent.MOUSE_DRAGGED, this::mouseDragged);
		canvas.addEventHandler( MouseEvent.MOUSE_RELEASED, this::mouseReleased);
	}

	public void mousePressed( MouseEvent e){
		if( e.getButton() == MouseButton.PRIMARY)
			animator.addStartVector( e.getX(), e.getY());
	}

	public void mouseDragged( MouseEvent e){
		if( e.getButton() == MouseButton.PRIMARY)
			animator.addVector( e.getX(), e.getY());
	}

	public void mouseReleased( MouseEvent e){
		if( e.getButton() == MouseButton.PRIMARY)
			animator.clean();
	}

	public void clearCanvasAndStart(){
		animator.clearCanvas();
		animator.start();
	}

	public static void main( String[] args){
		launch( args);
	}
}
