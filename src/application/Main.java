package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;



public class Main extends Application {
	private int counter = 0;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
//APP WINDOW
			
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,640,450);
			root.setId("my-root");
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

//LEFT SIDE
            
	        Image image = new Image("https://www.desktopbackground.org/download/320x480/2014/04/06/742671_rgb-spectrum-hd-desktop-wallpapers-high-definition-fullscreen_960x640_h.jpg"); 
	        
	        ImagePattern image_pattern = new ImagePattern(image,0,0,320,480,false);
	        
			Rectangle rectangle = new Rectangle(0,0,280,400);
			rectangle.setStroke(Color.BLACK);
            rectangle.setFill(image_pattern);
            
            VBox left_vbox = new VBox(10);
			root.setLeft(left_vbox);
			left_vbox.setPadding(new Insets(20,0,0,10));
			left_vbox.getChildren().addAll(rectangle);
			
//RIGHT SIDE
			
			Button button1 = new Button(" Wczytaj   ");
			button1.setId("my-button"); 
			button1.setTranslateX(-30);
			Button button2 = new Button(" Czyść     ");
			button2.setId("my-button");
			button2.setTranslateX(70);
            button2.setTranslateY(-37);
            Label label = new Label("Wycinki w kolejności malejącej\nśredniej wartości składowej\nczerwonej.");
            label.setTranslateY(-30);
              
            FlowPane flowPane = new FlowPane();
            flowPane.setTranslateX(0);
            flowPane.setTranslateY(-30);
            flowPane.setPrefWrapLength(250);
            flowPane.setVgap(8);
            flowPane.setHgap(8);
            
         // TAB OF SMALL SQUARES SETUP
            
            Rectangle squareTab[] = new Rectangle[25];
            for (int i = 0; i < 25; i++) {
            	squareTab[i] = new Rectangle(41, 41);
            	squareTab[i].setStroke(Color.BLACK);
            	squareTab[i].setFill(Color.TRANSPARENT);
                flowPane.getChildren().addAll(squareTab[i]);
            }
   			
         // TAB OF RED VALUE AND TAB OF SMALL SQUARE PAINTS
            
         			double howMuchRedTab[] = new double[25];
         			Paint smallSquarePaint[] = new Paint[25];
         			
         //MOUSE EVENT
         			
    			rectangle.addEventHandler(
    					MouseEvent.MOUSE_PRESSED,
    					event->{
    						counter();
    			
    			 double r1 = event.getX();
                 double r2 = event.getY();
                 double finalRedValue = 0;
                    			
    			int height = (int)image.getHeight();
    			int width = (int)image.getWidth();
    			WritableImage dstImage = new WritableImage(width,height);
    			
    			PixelReader reader = image.getPixelReader();
    			PixelWriter writer = dstImage.getPixelWriter();
    			
    			for(int x=0; x < width; x++) {
    				for(int y=0; y < height; y++) {
    					Color color = reader.getColor(x, y);
    					writer.setColor(x, y, Color.color(
    							color.getRed(), 
    							color.getGreen(), 
    							color.getBlue()
    							));
    					
    		// RED LEVEL READER
    					
                      PixelReader redReader = dstImage.getPixelReader();
                      Color redValue = redReader.getColor((int)r1, (int)r2);
                      redValue.getRed();
                      finalRedValue += redValue.getRed();
    				}
    			}
    			
    		// ASSIGN RED VALUE TO ARRAY ELEMENTS
    			
    			finalRedValue = finalRedValue/(41*41);
    			howMuchRedTab[counter-1] = finalRedValue;
    			

    		// SMALL SQUARE FILLING
    			
    			squareTab[counter-1].setFill(new ImagePattern(dstImage,-(r1-20.5),-(r2-20.5),320,480,false));
    			smallSquarePaint[counter-1] = squareTab[counter-1].getFill();
                
                
            // RED VALUE BUBBLE SORT AND ARRAY ELEMENTS ASSIGMENT   
                
                if (counter > 1) {
    	        int i, j;
    	        double tmp;
    	        Paint tempPaint = smallSquarePaint[counter-1];
    	        
    	        	for (i = 0; i < howMuchRedTab.length - 1; i++){       
    	        		for (j = 0; j < howMuchRedTab.length - 1; j++){

    	        			if(howMuchRedTab[j+1] > howMuchRedTab[j]){
    	        				tmp = howMuchRedTab[j+1];
    	        				howMuchRedTab[j+1] = howMuchRedTab[j];
    	        				howMuchRedTab[j] = tmp;
    	                    
    	        				tempPaint = smallSquarePaint[j+1];
    	        				smallSquarePaint[j+1] = smallSquarePaint[j];
    	        				smallSquarePaint[j] = tempPaint;
    	                    
    	        				squareTab[j].setFill(smallSquarePaint[j]); 
    	        				squareTab[j+1].setFill(smallSquarePaint[j+1]);
    	                          
    	        			}
    	        		}
    	        	}
                }
    			});
    			
    		// CLEAR BUTTON
    			
    			button2.setOnAction(event->{
    				for (int i = 0; i < 25; i++) {
    					squareTab[i].setFill(null);
    					smallSquarePaint[i] = null;
    					howMuchRedTab[i] = 0;
    					counter = 0;
    				}
    			}); 
    			
            VBox right_vbox = new VBox(10);
            right_vbox.getChildren().addAll(button1, button2, label, flowPane);
            root.setRight(right_vbox);
            right_vbox.setPadding(new Insets(20, 40, 20, -270));

            
// STAGE SHOW
            
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void counter(){
	        counter++;
	    }
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
