/**
 * 	@author Marcelo Mariduena
 * 
 * 	@contributors: ---
 * 
 * 	@resources: 
 * 		docs.oracle.com/javase/8/javafx/*
 * 		docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MyPaint extends Application 
{
	private String shapeText = "\t\tShapes drawn: ";
	private int shapeCount = 0;
	
	@Override 
	public void start(Stage primaryStage)
	{
		
		/* ------ BUTTONS & TEXT AREAS ------ */
		RadioButton btn1 = new RadioButton("Draw");
		RadioButton btn2 = new RadioButton("Circle");
		RadioButton btn3 = new RadioButton("Erase");
		RadioButton btn4 = new RadioButton("1pt");
		RadioButton btn5 = new RadioButton("2pt");
		RadioButton btn6 = new RadioButton("5pt");
		RadioButton btn7 = new RadioButton("10pt");
		
		ToggleGroup exclusiveToggling1 = new ToggleGroup();
		btn1.setToggleGroup(exclusiveToggling1);
		btn2.setToggleGroup(exclusiveToggling1);
		btn3.setToggleGroup(exclusiveToggling1);
		
		ToggleGroup exclusiveToggling2 = new ToggleGroup();
		btn4.setToggleGroup(exclusiveToggling2);
		btn5.setToggleGroup(exclusiveToggling2);
		btn6.setToggleGroup(exclusiveToggling2);
		btn7.setToggleGroup(exclusiveToggling2);
		btn4.setSelected(true);
		
//		TextArea colorInput = new TextArea();
//		colorInput.setPrefWidth(50);
//		colorInput.setPrefHeight(5);
		
		ColorPicker colorPicker = new ColorPicker();
		
		Button resetButton = new Button("Clear");
		Text coordinates = new Text();
		Text shapeCounterText = new Text(shapeText + shapeCount);
		
		
		/* ------ TOP BAR ------ */
		ToolBar topBar = new ToolBar();
		topBar.setPrefHeight(25);
		topBar.getItems().addAll(btn4, btn5, btn6, btn7);
		
		
		/* ------ SIDE BAR ------ */
		ToolBar sideBar = new ToolBar();
		sideBar.setOrientation(Orientation.VERTICAL);
		sideBar.setPrefWidth(150);
		sideBar.setPadding(new Insets(10, 10, 10, 10));
		sideBar.getItems().addAll(colorPicker, btn1, btn2, btn3, resetButton);
		
		
		/* ------ BOTTOM BAR ------ */
		ToolBar bottomBar = new ToolBar();
		bottomBar.setPrefHeight(25);
		bottomBar.getItems().addAll(coordinates, shapeCounterText);
				
		
		/* ------ CANVAS ------ */
		Canvas canvas = new Canvas(650,550);
		canvas.setWidth(650);
		canvas.setHeight(550);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		StackPane holder = new StackPane();
		holder.getChildren().add(canvas);
		holder.setStyle("-fx-background-color: white");

		
		/* ------ SHAPES ------ */
		Circle circle = new Circle();
		
		
		/* ------ DRAW / ERASE / CIRCLE ------ */
		canvas.setOnMousePressed(event -> {
			/* Draw and erase */
			if(btn1.isSelected() || btn3.isSelected()) 
			{
				gc.setStroke(btn1.isSelected() ? colorPicker.getValue() : Color.WHITE);
				gc.beginPath();
			}
			/* Circle */
			else if(btn2.isSelected())
			{
//				canvas.setCursor(Cursor.CROSSHAIR);
				gc.setFill(colorPicker.getValue());
                circle.setCenterX(event.getX());
                circle.setCenterY(event.getY());
			}
		});
        canvas.setOnMouseDragged(event -> {
        	/* Draw and erase */
	    	if(btn1.isSelected() || btn3.isSelected())
	    	{
	    		gc.setLineWidth(
	    			btn4.isSelected() ? 1 : 
	    			btn5.isSelected() ? 2 : 
	    			btn6.isSelected() ? 5 : 10
	    		);
	    		gc.lineTo(event.getX(), event.getY());
		        gc.stroke();
	    	}
	    });
        canvas.setOnMouseReleased(event -> {
        	/* Draw and erase */
        	if(btn1.isSelected() || btn3.isSelected()) gc.closePath();
        	/* Circle */
        	else if(btn2.isSelected())
			{
        		if(event.getX() < circle.getCenterX())
        			circle.setRadius(Math.abs(event.getX() - circle.getCenterX()));
        		else 
        			circle.setRadius(Math.abs(circle.getCenterX() - event.getX()));

    			gc.fillOval(
            		circle.getCenterX() - circle.getRadius(), 
                	circle.getCenterY() - circle.getRadius(), 
                	circle.getRadius() * 2, 
                	circle.getRadius() * 2
                );
    			
    			shapeCounterText.setText(shapeText + ++shapeCount);
			}
        });
		
        
		/* ------ COORDINATE SYSTEM ------ */
		canvas.setOnMouseMoved(event -> 
			coordinates.setText(
				String.format("%.2f", event.getX()) 
				+ "\t" 
				+ String.format("%.2f", event.getY())
			)
		);
		
		
		/* ------ CLEARING THE CANVAS ------ */
		resetButton.setOnMouseClicked(event -> {
			shapeCount = 0;
			shapeCounterText.setText(shapeText + shapeCount);
			gc.clearRect(0, 0, 650, 550);
		});
		
		
		/* ------ STAGE ------ */
		BorderPane pane = new BorderPane();
		pane.setTop(topBar);
		pane.setLeft(sideBar);
		pane.setBottom(bottomBar);
		pane.setCenter(holder);

		primaryStage.setTitle("MyPaint - Marcelo Mariduena");
		primaryStage.setScene(new Scene(pane, 850, 650));
		primaryStage.show();
	}
	
	
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
