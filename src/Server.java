

import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application {
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    // Text area for displaying contents
    TextArea ta = new TextArea();

    // Create a scene and place it in the stage
    Scene scene = new Scene(new ScrollPane(ta), 450, 200);
    primaryStage.setTitle("Server"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage
    
    new Thread( () -> {
      try {
        // Create a server socket
        ServerSocket serverSocket = new ServerSocket(8000);
        Platform.runLater(() ->
          ta.appendText("Server started at " + new Date() + '\n'));
  
        // Listen for a connection request
        Socket socket = serverSocket.accept();
  
        // Create data input and output streams
        DataInputStream inputFromClient = new DataInputStream(
          socket.getInputStream());
        DataOutputStream outputToClient = new DataOutputStream(
          socket.getOutputStream());
  
        while (true) {
          // Receive int from the client application
          int isprime = inputFromClient.readInt();

          boolean prime = false;

          //calculating if the number recieved is a prime number

          prime = testPrime(isprime);

  
          // Send area back to the client using the dataoutputstream socket made earlier\\
          outputToClient.writeBoolean(prime);

          //wrapping the boolean to use in the runlater lambda
          boolean finalPrime = prime;
          Platform.runLater(() -> {
            ta.appendText("Radius received from client: " 
              + finalPrime + '\n');
            ta.appendText("Area is: " + isprime + '\n');
          });
        }
      }
      catch(IOException ex) {
        ex.printStackTrace();
      }
    }).start();
  }

 //main used to launch the javafx thread
  public static void main(String[] args) {
    launch(args);
  }

  static boolean testPrime(int x){
    if(x <= 1){
      return false;
    }
    for(int i =2 ; i<x; i++){
      //if any number divides evenly with modulo then we know the number cannot be prime
      if(x % i == 0){
        return false;
      }
    }
    return true;
  }
}
