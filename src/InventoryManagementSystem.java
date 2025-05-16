/*import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;

import java.sql.*;

public class InventoryManagementSystem extends Application {
    private Stage primaryStage;
    
    private ComboBox<String> categoryComboBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Inventory Management System");

        // Create a VBox layout for the form
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        populateCategoryComboBox();

        // Dashboard labels (your existing ones)
        Label header = new Label("Dashboard");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label stockInLabel = new Label("Stock In: Loading...");
        Label stockOutLabel = new Label("Stock Out: Loading...");
        Label popularProductLabel = new Label("Popular Product: Loading...");
        Label totalSalesLabel = new Label("Total Sales: Loading...");
        Label totalExpiredLabel = new Label("Total Expired: Loading...");

        // Labels and fields for adding a new product
        Label productHeader = new Label("Add New Product");
        Label barcodeLabel = new Label("Barcode:");
        TextField barcodeField = new TextField();
        barcodeField.setPromptText("Scan or enter barcode");

        Label nameLabel = new Label("Product Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter product name");

        Label quantityLabel = new Label("Quantity:");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter quantity");

        Label priceLabel = new Label("Price:");
        TextField priceField = new TextField();
        priceField.setPromptText("Enter price");

        // ComboBox and DatePicker for category and expiration
        Label categoryLabel = new Label("Category:");
        ComboBox<String> categoryComboBox = new ComboBox<>();
        Label expirationLabel = new Label("Expiration Date:");
        DatePicker expirationDatePicker = new DatePicker();

        Button addButton = new Button("Add Product");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setOnAction(e -> handleLogout());

        // Add action for the Add Product button
        addButton.setOnAction(e -> {
            String barcode = barcodeField.getText().trim();
            String name = nameField.getText().trim();
            String quantityText = quantityField.getText().trim();
            String priceText = priceField.getText().trim();
            String category = categoryComboBox.getValue();
            String expirationDate = expirationDatePicker.getValue() != null ? expirationDatePicker.getValue().toString() : "";

            if (barcode.isEmpty() || name.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
                showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
                return;
            }

            try {
                int quantity = Integer.parseInt(quantityText.replaceAll("\\s", ""));
                double price = Double.parseDouble(priceText.replaceAll("\\s", ""));

                // Insert product into the database
                DatabaseManager.insertProductWithBarcode(barcode, name, quantity, price, category, expirationDate);
                showAlert("Success", "Product added successfully!", Alert.AlertType.INFORMATION);

                // Clear the fields after adding the product
                barcodeField.clear();
                nameField.clear();
                quantityField.clear();
                priceField.clear();
                categoryComboBox.getSelectionModel().clearSelection();
                expirationDatePicker.setValue(null);

                // Reload dashboard data
                loadDashboardData(stockInLabel, stockOutLabel, popularProductLabel, totalSalesLabel, totalExpiredLabel);

            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid quantity or price. Please enter valid numbers.", Alert.AlertType.ERROR);
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to add product!", Alert.AlertType.ERROR);
            }
        });


        layout.getChildren().addAll(
            header, stockInLabel, stockOutLabel, popularProductLabel, totalSalesLabel, totalExpiredLabel,
            productHeader, barcodeLabel, barcodeField,
            nameLabel, nameField,
            quantityLabel, quantityField,
            priceLabel, priceField,
            categoryLabel, categoryComboBox, // Category ComboBox
            expirationLabel, expirationDatePicker, // Expiration DatePicker
            addButton
        );

        // Create a scene and set it to the primaryStage
        Scene scene = new Scene(layout, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load the dashboard data
        loadDashboardData(stockInLabel, stockOutLabel, popularProductLabel, totalSalesLabel, totalExpiredLabel);
    }

    private void loadDashboardData(Label stockIn, Label stockOut, Label popularProduct, Label totalSales, Label totalExpired) {
        try (Connection conn = DatabaseManager.connect();
             ResultSet rs = DatabaseManager.getDashboardData()) {

            if (rs != null && rs.next()) {
                stockIn.setText("Stock In: " + rs.getInt("stock_in"));
                stockOut.setText("Stock Out: " + rs.getInt("stock_out"));
                popularProduct.setText("Popular Product: " + rs.getString("popular_product"));
                totalSales.setText("Total Sales: PHP " + rs.getDouble("total_sales"));
                totalExpired.setText("Total Expired: " + rs.getInt("total_expired"));
            } else {
                showAlert("Error", "No data found.", Alert.AlertType.WARNING);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load products from the database.", Alert.AlertType.ERROR);
        }
    }
    public void populateCategoryComboBox() {
        try (Connection conn = DatabaseManager.connect()) {
            String query = "SELECT category_name FROM categories"; // Adjust this query if needed
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                categoryComboBox.getItems().add(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load categories from database.", Alert.AlertType.ERROR);
        }
    }

    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to log out.");
        alert.setContentText("Do you want to continue?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            // Close the application on logout
            primaryStage.close();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



/*import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;  
import java.sql.*;



import static javafx.application.Application.launch;

public class InventoryManagementSystem extends Application {
    private Stage primaryStage; 

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Inventory Management System");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        DatePicker expirationDatePicker = new DatePicker();
        
        
        
        
        


        Label header = new Label("Dashboard");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label stockInLabel = new Label("Stock In: Loading...");
        Label stockOutLabel = new Label("Stock Out: Loading...");
        Label popularProductLabel = new Label("Popular Product: Loading...");
        Label totalSalesLabel = new Label("Total Sales: Loading...");
        Label totalExpiredLabel = new Label("Total Expired: Loading...");

       
        Label productHeader = new Label("Add New Product");

        Label barcodeLabel = new Label("Barcode:");
        TextField barcodeField = new TextField();
        barcodeField.setPromptText("Scan or enter barcode");

        Label nameLabel = new Label("Product Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter product name");

        Label quantityLabel = new Label("Quantity:");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter quantity");

        Label priceLabel = new Label("Price:");
        TextField priceField = new TextField();
        priceField.setPromptText("Enter price");

        Button addButton = new Button("Add Product");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setOnAction(e -> handleLogout());

 
        addButton.setOnAction(e -> {
            String barcode = barcodeField.getText().trim();
            String name = nameField.getText().trim();
            String quantityText = quantityField.getText().trim();
            String priceText = priceField.getText().trim();

            if (barcode.isEmpty() || name.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
                showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
                return;
            }

            try {
               
                int quantity = Integer.parseInt(quantityText.replaceAll("\\s", ""));
                double price = Double.parseDouble(priceText.replaceAll("\\s", ""));
                
                LocalDate expirationDate = expirationDatePicker.getValue(); // Assuming you get the expiration date from a DatePicker
                DatabaseManager.insertProductWithBarcode(barcode, name, quantity, price, expirationDate);
                showAlert("Success", "Product added successfully!", Alert.AlertType.INFORMATION);

            
                barcodeField.clear();
                nameField.clear();
                quantityField.clear();
                priceField.clear();

          
                loadDashboardData(stockInLabel, stockOutLabel, popularProductLabel, totalSalesLabel, totalExpiredLabel);

            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid quantity or price. Please enter valid numbers.", Alert.AlertType.ERROR);
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to add product!", Alert.AlertType.ERROR);
            }
        });
        
        

        layout.getChildren().addAll(
                header, stockInLabel, stockOutLabel, popularProductLabel, totalSalesLabel, totalExpiredLabel,
                productHeader, barcodeLabel, barcodeField, nameLabel, nameField,
                quantityLabel, quantityField, priceLabel, priceField, addButton
        );

        Scene scene = new Scene(layout, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.show();


        loadDashboardData(stockInLabel, stockOutLabel, popularProductLabel, totalSalesLabel, totalExpiredLabel);
    }

    private void loadDashboardData(Label stockIn, Label stockOut, Label popularProduct, Label totalSales, Label totalExpired) {
        try (Connection conn = DatabaseManager.connect();
             ResultSet rs = DatabaseManager.getDashboardData()) {

            if (rs != null && rs.next()) {
                stockIn.setText("Stock In: " + rs.getInt("stock_in"));
                stockOut.setText("Stock Out: " + rs.getInt("stock_out"));
                popularProduct.setText("Popular Product: " + rs.getString("popular_product"));
                totalSales.setText("Total Sales: PHP " + rs.getDouble("total_sales"));
                totalExpired.setText("Total Expired: " + rs.getInt("total_expired"));
            } else {
                showAlert("Error", "No data found.", Alert.AlertType.WARNING);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load products from the database.", Alert.AlertType.ERROR);
        }
    }
    
     private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to log out.");
        alert.setContentText("Do you want to continue?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            // Close the application on logout
            primaryStage.close();
        }
    }


     private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



/*import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class InventoryManagementSystem extends Application {
    private Stage primaryStage; 

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Inventory Management System");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        
        
        


        Label header = new Label("Dashboard");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label stockInLabel = new Label("Stock In: Loading...");
        Label stockOutLabel = new Label("Stock Out: Loading...");
        Label popularProductLabel = new Label("Popular Product: Loading...");
        Label totalSalesLabel = new Label("Total Sales: Loading...");
        Label totalExpiredLabel = new Label("Total Expired: Loading...");

       
        Label productHeader = new Label("Add New Product");

        Label barcodeLabel = new Label("Barcode:");
        TextField barcodeField = new TextField();
        barcodeField.setPromptText("Scan or enter barcode");

        Label nameLabel = new Label("Product Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter product name");

        Label quantityLabel = new Label("Quantity:");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter quantity");

        Label priceLabel = new Label("Price:");
        TextField priceField = new TextField();
        priceField.setPromptText("Enter price");

        Button addButton = new Button("Add Product");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setOnAction(e -> handleLogout());

 
        addButton.setOnAction(e -> {
            String barcode = barcodeField.getText().trim();
            String name = nameField.getText().trim();
            String quantityText = quantityField.getText().trim();
            String priceText = priceField.getText().trim();

            if (barcode.isEmpty() || name.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
                showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
                return;
            }

            try {
                // ✅ Trim and parse correctly
                int quantity = Integer.parseInt(quantityText.replaceAll("\\s", ""));
                double price = Double.parseDouble(priceText.replaceAll("\\s", ""));

                DatabaseManager.insertProductWithBarcode(barcode, name, quantity, price);
                showAlert("Success", "Product added successfully!", Alert.AlertType.INFORMATION);

            
                barcodeField.clear();
                nameField.clear();
                quantityField.clear();
                priceField.clear();

          
                loadDashboardData(stockInLabel, stockOutLabel, popularProductLabel, totalSalesLabel, totalExpiredLabel);

            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid quantity or price. Please enter valid numbers.", Alert.AlertType.ERROR);
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to add product!", Alert.AlertType.ERROR);
            }
        });
        
        

        layout.getChildren().addAll(
                header, stockInLabel, stockOutLabel, popularProductLabel, totalSalesLabel, totalExpiredLabel,
                productHeader, barcodeLabel, barcodeField, nameLabel, nameField,
                quantityLabel, quantityField, priceLabel, priceField, addButton
        );

        Scene scene = new Scene(layout, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.show();


        loadDashboardData(stockInLabel, stockOutLabel, popularProductLabel, totalSalesLabel, totalExpiredLabel);
    }

    private void loadDashboardData(Label stockIn, Label stockOut, Label popularProduct, Label totalSales, Label totalExpired) {
        try (Connection conn = DatabaseManager.connect();
             ResultSet rs = DatabaseManager.getDashboardData()) {

            if (rs != null && rs.next()) {
                stockIn.setText("Stock In: " + rs.getInt("stock_in"));
                stockOut.setText("Stock Out: " + rs.getInt("stock_out"));
                popularProduct.setText("Popular Product: " + rs.getString("popular_product"));
                totalSales.setText("Total Sales: PHP " + rs.getDouble("total_sales"));
                totalExpired.setText("Total Expired: " + rs.getInt("total_expired"));
            } else {
                showAlert("Error", "No data found.", Alert.AlertType.WARNING);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load products from the database.", Alert.AlertType.ERROR);
        }
    }
    
     private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to log out.");
        alert.setContentText("Do you want to continue?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            // Close the application on logout
            primaryStage.close();
        }
    }


    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}*/
