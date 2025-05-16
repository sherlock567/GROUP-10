
import javafx.stage.Stage;

public class StageManager {
    private static Stage primaryStage;

    
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

   
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}

/*


import javafx.stage.Stage;

public class StageManager {
    private static Stage primaryStage;

    // Set the primary stage
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    // Get the primary stage
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
*/