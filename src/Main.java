import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Optional;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.sql.Statement;
import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.scene.Scene;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.ButtonBar;
import java.time.LocalDate;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.print.PrinterJob;




public class Main extends Application {
    
    private Stage primaryStage;
    private ComboBox<String> categoryComboBox;
    private final ObservableList<Product> inventory = FXCollections.observableArrayList();
    private Label stockInLabel;
    private Label stockOutLabel;
    private List<String> notifications = new ArrayList<>();
    private List<String> inbox = new ArrayList<>();
    private ListView<String> inboxListView;
    private boolean isLoggedIn = false;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StageManager.setPrimaryStage(primaryStage);
        primaryStage.setTitle("Inventory Management System");
        this.primaryStage = primaryStage; 
        primaryStage.show();
        scheduleNotifications(); 
        
        categoryComboBox = new ComboBox<>();
        populateCategoryComboBox();
        
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #F3F4F6;");
        
        initializeNotifications();
      
        Button notificationButton = new Button("Show Notifications");
        notificationButton.setOnAction(e -> showNotifications());


        Label header = new Label("Dashboard");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label stockInLabel = new Label("Stock In: Loading...");
        Label stockOutLabel = new Label("Stock Out: Loading...");
        Label popularProductLabel = new Label("Popular Product: Loading...");
        Label totalSalesLabel = new Label("Total Sales: Loading...");
        Label totalExpiredLabel = new Label("Total Expired: Loading...");

        // Add Product Form
        Label productHeader = new Label("Add New Product");

        TextField barcodeField = new TextField();
        barcodeField.setPromptText("Scan or enter barcode");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter product name");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter quantity");

        TextField priceField = new TextField();
        priceField.setPromptText("Enter price");
        
        categoryComboBox = new ComboBox<>();
        Label categoryLabel = new Label("Category:");

        DatePicker expirationDatePicker = new DatePicker();
        Label expirationLabel = new Label("Expiration Date:");
    //----------------------------------------------------------------------------------------------------------    ---------------------------------------------------
        //ListView<String> notificationList = new ListView<>();
        //checkInventoryNotifications(notificationList);
        //VBox notificationBox = new VBox(new Label("Notifications"), notificationList);
        //layout.setRight(notificationBox);
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        Button addButton = new Button("Add Product");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setOnAction(e -> handleLogout());

        BorderPane layout2 = new BorderPane();

        VBox root = new VBox();
        
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setStyle("-fx-background-color: rgb(139, 32, 201);");

        Label logo = new Label("Inventory System");
        logo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

   

        topBar.getChildren().addAll(logo);

     
        layout.getChildren().addAll(
            topBar,
            header,
            stockInLabel, stockOutLabel, popularProductLabel, totalSalesLabel, totalExpiredLabel,
            productHeader,
            new Label("Barcode:"), barcodeField,
            new Label("Product Name:"), nameField,
            new Label("Quantity:"), quantityField,
            new Label("Price:"), priceField,
            categoryLabel, categoryComboBox,
            expirationLabel, expirationDatePicker,
            addButton,
            logoutButton,
            root
        );

        Scene scene = new Scene(layout, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();


        
//pag may hinahanap ka ito lang i search mo tanga 
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color:rgb(156, 37, 224); -fx-border-radius: 5; ");
        
        String[] sections = {"Dashboard", "Products", "Sales", "Add New Product", "User Management","Report",/* "Category",*/ "Logout"};

        for (String section : sections) {
            Button sectionButton = new Button(section);
            sectionButton.setStyle("-fx-background-color: rgb(131, 32, 189); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 20;");
            sectionButton.setMaxWidth(Double.MAX_VALUE);

            sectionButton.setOnAction(e -> {
                switch (section) {
                    case "Dashboard":
                        layout.setCenter(createDashboard());
                        break;
                    case "Sales":
                        layout.setCenter(createSalesChart());
                        break;
                    case "Add New Product":
                        layout.setCenter(createAddProductForm());
                        break;
                    case "Products":
                        layout.setCenter(createInventoryTable());
                        break;
                    case "User Management":
                        if (authenticateUserManagement()) {   
                            layout.setCenter(createUserManagement());
                        } else {
                            showAlert("Access Denied", "Incorrect password.");
                        }
                        break;
                    /*case "Category":
                        layout.setCenter(createCategoryList());
                        break;*/
                    case "Report":
                        layout.setCenter(createReport());
                        break;    
                    case "Notification":
                        layout.setCenter(createNotifications());
                        break;
                    case "Logout":
                        handleLogout();   
                        break;
                    default:
                        layout.setCenter(new Label(section + " is under construction."));
                }
            });

            sidebar.getChildren().add(sectionButton);  
        }
        
        logoutButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        logoutButton.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setOnAction(e -> handleLogout());

        Button inboxButton = new Button("Inbox");
        inboxButton.setStyle("-fx-background-color: rgb(131, 32, 189); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 20;");
        inboxButton.setMaxWidth(Double.MAX_VALUE);
        inboxButton.setOnAction(e -> {
            Stage inboxStage = new Stage();
            inboxStage.setTitle("Inbox");
            
            VBox inboxLayout = new VBox(10);
            inboxLayout.setPadding(new Insets(20));
            inboxLayout.setStyle("-fx-background-color: white;");
            
            Label header2 = new Label("Inbox");
            header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            
            TableView<Message> inboxTable = new TableView<>();
            ObservableList<Message> messages = FXCollections.observableArrayList(
              //  new Message("Stock Alert", "Rice stock is low."),
             //  new Message("Sales Update", "Weekly sales increased by 10%.")
            );
            
            TableColumn<Message, String> subjectColumn = new TableColumn<>("Subject");
            subjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
            
            TableColumn<Message, String> bodyColumn = new TableColumn<>("Message");
            bodyColumn.setCellValueFactory(cellData -> cellData.getValue().bodyProperty());
            bodyColumn.setPrefWidth(300);
            
            inboxTable.getColumns().addAll(subjectColumn, bodyColumn);
            inboxTable.setItems(messages);
            
            Button closeButton = new Button("Close");
            closeButton.setOnAction(closeEvent -> inboxStage.close());
            
            inboxLayout.getChildren().addAll(header, inboxTable, closeButton);
            Scene inboxScene = new Scene(inboxLayout, 600, 400);
            inboxStage.setScene(inboxScene);
            inboxStage.show();
        });

        Button userManualButton = new Button("User Manual");
        userManualButton.setStyle("-fx-background-color: rgb(131, 32, 189); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 20;");
        userManualButton.setMaxWidth(Double.MAX_VALUE);
        userManualButton.setOnAction(e -> {
            
            try {
                java.awt.Desktop.getDesktop().open(new java.io.File("UserManual.pdf"));
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to open the User Manual.");
                alert.showAndWait();
            }

           
            Stage helpStage = new Stage();
            helpStage.setTitle("User Manual");
            VBox helpContent = new VBox(10);
            helpContent.setPadding(new Insets(20));
            helpContent.getChildren().add(new Label("This is the user manual content. Add detailed instructions here."));
            Scene helpScene = new Scene(helpContent, 400, 300);
            helpStage.setScene(helpScene);
            helpStage.show();
        });
        
        logoutButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setOnAction(e -> handleLogout()); 
        
        sidebar.getChildren().addAll(inboxButton, userManualButton, logoutButton);

        
        VBox mainContent = createDashboard();
        
        Button dashboardButton = new Button("Go to Dashboard");
        dashboardButton.setOnAction(e -> {
            
        });

        VBox loginScreen = new VBox(20);
        loginScreen.setPadding(new Insets(20));
        loginScreen.setAlignment(Pos.CENTER);
        loginScreen.setMaxWidth(350);

        VBox loginBox = new VBox(15);
        loginBox.setPadding(new Insets(20));
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 0, 5);");

        Label loginTitle = new Label("Login");
        loginTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color:rgb(156, 37, 224); -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 12 20; -fx-border-radius: 5; -fx-background-radius: 5;");
        loginButton.setOnAction(e -> {
        if ("ivan".equals(usernameField.getText()) && "password".equals(passwordField.getText())) {
            isLoggedIn = true; 
            layout.setCenter(mainContent);
            layout.setLeft(sidebar);
            layout.setTop(topBar);

            showNotifications(); 
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid username or password.");
            alert.showAndWait();
        }
    });

        loginBox.getChildren().addAll(loginTitle, usernameField, passwordField, loginButton);
        loginScreen.getChildren().add(loginBox);
      
        layout.setCenter(loginScreen);
        layout.getChildren().addAll(dashboardButton, logoutButton);

        
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Inventory Management - Sales Chart");
        
        
    }
    

    private VBox createSalesChart() {
        VBox salesChart = new VBox(10);
        salesChart.setPadding(new Insets(20));
        salesChart.setStyle("-fx-background-color: white; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        Label header = new Label("Sales Chart");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        salesChart.getChildren().add(header);

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Month");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Sales Amount");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Monthly Sales Data");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Sales");

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MONTH(sale_date) AS month, SUM(total_price) AS total FROM sales GROUP BY MONTH(sale_date)")) {

            while (rs.next()) {
                int month = rs.getInt("month");
                double total = rs.getDouble("total");
                series.getData().add(new XYChart.Data<>(month, total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to retrieve sales data: " + e.getMessage());
        }

        lineChart.getData().add(series);
        salesChart.getChildren().add(lineChart);
        return salesChart;
    }

   

    
    private VBox createDashboard() {
     VBox dashboard = new VBox(20);
     dashboard.setPadding(new Insets(20));
     dashboard.setStyle("-fx-background-color: #f4f4f4;");


     HBox statsRow = new HBox(20);
     statsRow.setAlignment(Pos.CENTER_LEFT);
     statsRow.setPadding(new Insets(10));

        
     String cardStyle = "-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 10; " +
             "-fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 10);";

    
     VBox stockInCard = createDashboardCard("Stock In", "0", cardStyle);

    
     VBox stockOutCard = createDashboardCard("Stock Out", "0", cardStyle);

  
     VBox popularProductCard = createDashboardCard("Popular Product", "N/A", cardStyle);

    
     VBox totalSalesCard = createDashboardCard("Total Sales", "₱0.00", cardStyle);

    
     VBox expiredCard = createDashboardCard("Expired", "0", cardStyle);

  
     statsRow.getChildren().addAll(stockInCard, stockOutCard, popularProductCard, totalSalesCard, expiredCard);

 
     StackPane contentPane = new StackPane(statsRow);
     contentPane.setPadding(new Insets(20));
     contentPane.setStyle("-fx-background-color: #f4f4f4;");

    
     new Thread(() -> {
         try {
             ResultSet rs = null;
             try {
                 rs = DatabaseManager.getDashboardData();
                 if (rs != null && rs.next()) {
                     final ResultSet finalRs = rs;  

                     Platform.runLater(() -> {
                         try {
                            
                             ((Label) popularProductCard.getChildren().get(1))
                                     .setText(finalRs.getString("popular_product") != null ? finalRs.getString("popular_product") : "N/A");
                             ((Label) stockOutCard.getChildren().get(1))
                                     .setText(String.valueOf(finalRs.getInt("stock_out")));
                             ((Label) popularProductCard.getChildren().get(1))
                                     .setText(finalRs.getString("popular_product"));
                             ((Label) totalSalesCard.getChildren().get(1))
                                     .setText("₱" + finalRs.getDouble("total_sales"));
                             ((Label) expiredCard.getChildren().get(1))
                                     .setText(String.valueOf(finalRs.getInt("total_expired")));
                         } catch (SQLException e) {
                             e.printStackTrace();
                         }
                     });
                 }
             } finally {
                 if (rs != null) rs.close();
             }
         } catch (SQLException e) {
             e.printStackTrace();
             Platform.runLater(() -> showAlert("Error", "Failed to load dashboard data."));
         }
     }).start();

           dashboard.getChildren().add(contentPane);
           return dashboard;
        }


    private VBox createDashboardCard(String title, String value, String style) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle(style);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPrefSize(200, 100); 

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333; -fx-font-weight: bold;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: #2c3e50;");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }


    private VBox createInventoryTable() {
        VBox inventoryTable = new VBox(10);
        inventoryTable.setPadding(new Insets(20));
        inventoryTable.setStyle("-fx-background-color: white; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        Label header = new Label("Inventory");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        inventoryTable.getChildren().add(header);

        TableView<Product> table = new TableView<>();

        TableColumn<Product, String> nameColumn = new TableColumn<>("Product Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Product, Number> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());

        TableColumn<Product, Number> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());

        TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());

        TableColumn<Product, LocalDate> expirationColumn = new TableColumn<>("Expiration Date");
        expirationColumn.setCellValueFactory(cellData -> cellData.getValue().expirationDateProperty());

        table.getColumns().addAll(nameColumn, quantityColumn, priceColumn, categoryColumn, expirationColumn);

        new Thread(() -> {
            try (Connection conn = DatabaseManager.connect();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT product_name, quantity, price, expiration_date, category_id FROM products")) { // Ensure this matches your table structure

                ObservableList<Product> products = FXCollections.observableArrayList();

                while (rs.next()) {
                    LocalDate expirationDate = rs.getDate("expiration_date") != null ? rs.getDate("expiration_date").toLocalDate() : null;
                    products.add(new Product(
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        DatabaseManager.getCategoryNameById(rs.getInt("category_id")), // Fetch category name by ID
                        expirationDate
                    ));
                }

                javafx.application.Platform.runLater(() -> table.setItems(products));

            } catch (SQLException e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load products from the database.");
                    alert.showAndWait();
                });
            }
        }).start();

        inventoryTable.getChildren().add(table);

        return inventoryTable;
    }

    private VBox createAddProductForm() {
        VBox addProductForm = new VBox(15);
        addProductForm.setPadding(new Insets(20));
        addProductForm.setStyle("-fx-background-color: #f4f4f4;");

        Label header = new Label("Add New Product (With Barcode)");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label barcodeLabel = new Label("Barcode:");
        Label nameLabel = new Label("Product Name:");
        Label quantityLabel = new Label("Quantity:");
        Label priceLabel = new Label("Price:");
        Label categoryLabel = new Label("Category:");
        Label expirationLabel = new Label("Expiration Date:");

   
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

       
        TextField barcodeField = new TextField();
        barcodeField.setPromptText("Scan Barcode");

        TextField nameField = new TextField();
        nameField.setPromptText("Product Name");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        DatePicker expirationDatePicker = new DatePicker();
        expirationDatePicker.setPromptText("Select Expiration Date");

        ComboBox<String> categoryComboBox = new ComboBox<>();

       //dito ang dadag dagan ng categories wag kang bobo
        List<String> categories = DatabaseManager.getCategories();
        if (!categories.contains("canned food")) categories.add("canned food");
        if (!categories.contains("processed food")) categories.add("processed food");
        if (!categories.contains("junk food")) categories.add("junk food");
        if (!categories.contains("dairy product")) categories.add("dairy product");
        if (!categories.contains("hygiene product")) categories.add("hygiene product");
        
        categoryComboBox.getItems().addAll(categories);

        Button addButton = new Button("Add/Update Product");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        barcodeField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                String barcode = barcodeField.getText().trim();
                new Thread(() -> {
                    try {
                        DatabaseManager.Product existingProduct = DatabaseManager.getProductByBarcode(barcode);
                        javafx.application.Platform.runLater(() -> {
                            if (existingProduct != null) {
                                
                                nameField.setText(existingProduct.getName());
                                quantityField.setText(String.valueOf(existingProduct.getQuantity()));
                                priceField.setText(String.valueOf(existingProduct.getPrice()));
                                showAlert("Product Found", "Product already exists. You can update its quantity.");
                            } else {
                                showAlert("Product Not Found", "No product with this barcode.");
                            }
                        });
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        showAlert("Error", "Database error.");
                    }
                }).start();
            }
        });

        addButton.setOnAction(e -> {
            String barcode = barcodeField.getText().trim();
            String name = nameField.getText().trim();
            String quantityText = quantityField.getText().trim();
            String priceText = priceField.getText().trim();
            String category = categoryComboBox.getValue();
            String expirationDate = expirationDatePicker.getValue() != null ? expirationDatePicker.getValue().toString() : null;

            if (barcode.isEmpty() || name.isEmpty() || quantityText.isEmpty() || priceText.isEmpty() || category == null) {
                showAlert("Error", "All fields must be filled.");
                return;
            }

            try {
                int quantity = Integer.parseInt(quantityText);
                double price = Double.parseDouble(priceText);

                DatabaseManager.Product existingProduct = DatabaseManager.getProductByBarcode(barcode);
                if (existingProduct != null) {
                    int newQuantity = existingProduct.getQuantity() + quantity;
                    DatabaseManager.updateProductQuantity(barcode, newQuantity);
                    showAlert("Success", "Product quantity updated!");
                } else {
                    
                    int categoryId = DatabaseManager.getCategoryId(category);
                    DatabaseManager.insertProductWithBarcode(barcode, name, quantity, price, category, expirationDate);
                    showAlert("Success", "New product added!");
                }

              
                barcodeField.clear();
                nameField.clear();
                quantityField.clear();
                priceField.clear();
                categoryComboBox.getSelectionModel().clearSelection();
                expirationDatePicker.setValue(null);

            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid quantity or price.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to add or update product.");
            }
        });

        addProductForm.getChildren().addAll(header, barcodeLabel, barcodeField, nameLabel, nameField, quantityLabel, quantityField, priceLabel, priceField, categoryLabel, categoryComboBox, expirationLabel, expirationDatePicker, addButton);

        return addProductForm;
    }
    private void loadDashboardData(Label stockIn, Label stockOut, Label popularProduct, Label totalSales, Label totalExpired) {
        try (Connection conn = DatabaseManager.connect();
             ResultSet rs = DatabaseManager.getDashboardData()) {

            if (rs != null && rs.next()) {
                stockIn.setText("Stock In: " + rs.getInt("stock_in"));
                stockOut.setText("Stock Out: " + rs.getInt("stock_out"));
                popularProduct.setText("Popular Product: " + rs.getString("popular_product"));
                totalSales.setText("Total Sales: ₱" + rs.getDouble("total_sales"));
                totalExpired.setText("Total Expired: " + rs.getInt("total_expired"));
            } else {
                showAlert("Warning", "No dashboard data found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load dashboard data from the database.");
        }
    }
    
    private void checkInventoryNotifications(ListView<String> notificationList) {
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT product_name, quantity, expiration_date FROM products")) {

            while (rs.next()) {
                String productName = rs.getString("product_name");
                int quantity = rs.getInt("quantity");
                LocalDate expirationDate = rs.getDate("expiration_date") != null ? rs.getDate("expiration_date").toLocalDate() : null;

               
                if (quantity < 5) {
                    notificationList.getItems().add(productName + " is below 5 in quantity.");
                }

                
                if (expirationDate != null) {
                    if (expirationDate.isBefore(LocalDate.now())) {
                        notificationList.getItems().add(productName + " has expired.");
                    } else if (expirationDate.isBefore(LocalDate.now().plusDays(30))) {
                        notificationList.getItems().add(productName + " is about to expire on " + expirationDate.toString() + ".");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void checkInventoryNotifications() {
        notifications.clear(); 
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT product_name, quantity, expiration_date FROM products")) {

            while (rs.next()) {
                String productName = rs.getString("product_name");
                int quantity = rs.getInt("quantity");
                LocalDate expirationDate = rs.getDate("expiration_date") != null ? rs.getDate("expiration_date").toLocalDate() : null;

                // Check for low stock
                if (quantity < 5) {
                    notifications.add(productName + " is below 5 in quantity.");
                }

                // Check for expired products or products expiring within a month
                if (expirationDate != null) {
                    if (expirationDate.isBefore(LocalDate.now())) {
                        notifications.add(productName + " has expired.");
                    } else if (expirationDate.isBefore(LocalDate.now().plusDays(30))) {
                        notifications.add(productName + " is about to expire on " + expirationDate.toString() + ".");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to retrieve inventory data: " + e.getMessage());
        }

        // Debugging: Print notifications to console
        System.out.println("Notifications: " + notifications);
    }

    private void showAlert(String title, String message) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
            }

        private void initializeNotifications() {
            notifications.add("Stock for 'Rice' is below minimum level.");
            notifications.add("New sales report available.");
            notifications.add("Product 'Milk' has expired.");

        }

    private void showNotifications() {
        if (!isLoggedIn) {
            return; 
        }

        checkInventoryNotifications(); // Ensure notifications are updated

        if (notifications.isEmpty()) {
            showAlert("No Notifications", "You have no new notifications.");
            return;
        }

        // Create a new stage for notifications
        Stage notificationStage = new Stage();
        notificationStage.setTitle("Notifications");

        VBox notificationLayout = new VBox(10);
        notificationLayout.setPadding(new Insets(20));
        notificationLayout.setStyle("-fx-background-color: white;");

        Label header = new Label("Notifications");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        ListView<String> notificationList = new ListView<>();
        notificationList .getItems().addAll(notifications);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> notificationStage.close());

        Button readButton = new Button("Read");
        readButton.setOnAction(e -> {
            String selectedNotification = notificationList.getSelectionModel().getSelectedItem();
            if (selectedNotification != null) {
                inbox.add(selectedNotification); // Store in inbox
                notifications.remove(selectedNotification); // Remove from notifications
                notificationList.getItems().remove(selectedNotification); // Update ListView
                System.out.println("Notification read and stored in inbox: " + selectedNotification); // Debugging output
                System.out.println("Current inbox: " + inbox); // Debugging output
            } else {
                showAlert("Error", "No notification selected.");
            }
        });

        HBox buttonBox = new HBox(10, readButton, closeButton);
        notificationLayout.getChildren().addAll(header, notificationList, buttonBox);
        Scene notificationScene = new Scene(notificationLayout, 400, 300);
        notificationStage.setScene(notificationScene);
        notificationStage.show();
    }
    
    private void scheduleNotifications() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(1), e -> showNotifications()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    private void showNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType readButton = new ButtonType("Read");
        ButtonType laterButton = new ButtonType("Later");
        alert.getButtonTypes().setAll(readButton, laterButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == readButton) {
            inbox.add(message); 
        }
    }
    
    private void showInbox() {
        Stage inboxStage = new Stage();
        inboxStage.setTitle("Inbox");

        VBox inboxLayout = new VBox(10);
        inboxLayout.setPadding(new Insets(20));
        inboxLayout.setStyle("-fx-background-color: white;");

        Label header = new Label("Inbox");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        inboxListView = new ListView<>();
        inboxListView.getItems().clear(); // Clear previous items
        inboxListView.getItems().addAll(inbox); // Add current inbox items

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> inboxStage.close());

        inboxLayout.getChildren().addAll(header, inboxListView, closeButton);
        Scene inboxScene = new Scene(inboxLayout, 400, 300);
        inboxStage.setScene(inboxScene);
        inboxStage.show();
    }
    
    
    
    private VBox createUserManagement() {
        VBox userManagement = new VBox(10);
        userManagement.setPadding(new Insets(20));
        userManagement.setStyle("-fx-background-color: white; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        Label header = new Label("User Management");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        userManagement.getChildren().add(header);

        TableView<User> userTable = new TableView<>();
        ObservableList<User> users = FXCollections.observableArrayList();

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());

        userTable.getColumns().addAll(usernameColumn, roleColumn);
        userTable.setItems(users);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");

        TextField roleField = new TextField();
        roleField.setPromptText("Role");
        
        

        Button addButton = new Button("Add User");
        addButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String role = roleField.getText().trim();

            if (!username.isEmpty() && !password.isEmpty() && !role.isEmpty()) {
                new Thread(() -> {
                    try {
                        DatabaseManager.insertUser(username, password, role);
                        javafx.application.Platform.runLater(() -> {
                            users.add(new User(username, role));
                            usernameField.clear();
                            passwordField.clear();
                            roleField.clear();
                            showAlert("Success", "User added successfully!");
                        });
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        showAlert("Error", "Failed to add user.");
                    }
                }).start();
            } else {
                showAlert("Error", "All fields are required.");
            }
        });

        Button deleteButton = new Button("Delete User");
        deleteButton.setOnAction(e -> {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                new Thread(() -> {
                    try {
                        DatabaseManager.deleteUser(selectedUser.getUsername());
                        javafx.application.Platform.runLater(() -> {
                            users.remove(selectedUser);
                            showAlert("Success", "User deleted!");
                        });
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        showAlert("Error", "Failed to delete user.");
                    }
                }).start();
            } else {
                showAlert("Error", "No user selected.");
            }
        });

        HBox controls = new HBox(10, usernameField, passwordField, roleField, addButton, deleteButton);
        controls.setAlignment(Pos.CENTER);

        userManagement.getChildren().addAll(userTable, controls);
        return userManagement;
    }

    
    private boolean authenticateUserManagement() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Authentication Required");
        dialog.setHeaderText("Enter Admin Password to access User Management");
        dialog.setContentText("Password:");

        Optional<String> result = dialog.showAndWait();

        //password ng admin kahit anong gustomo puwede 
        return result.isPresent() && result.get().equals("admin123");
    }
    
    private boolean showLogin() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            try {
                return DatabaseManager.validateUser(username, password);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to connect to the database.");
            }
        }
        return false;
    }

    

    private VBox createNotifications() {
        VBox notifications = new VBox(10);
        notifications.setPadding(new Insets(20));
        notifications.setStyle("-fx-background-color: white; -fx-border-radius: 8px; -fx-background-radius: 8px;");
    
        Label header = new Label("Notifications");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        notifications.getChildren().add(header);
    

        ListView<String> notificationList = new ListView<>();
        ObservableList<String> notificationsData = FXCollections.observableArrayList(
            "Stock for 'Rice' is below minimum level.",
            "New sales report available.",
            "Product 'Milk' has expired."
        );
        notificationList.setItems(notificationsData);
    

        Button clearNotificationsButton = new Button("Clear Notifications");
        clearNotificationsButton.setOnAction(e -> notificationsData.clear());
    
        Button removeSelectedNotificationButton = new Button("Remove Selected");
        removeSelectedNotificationButton.setOnAction(e -> {
            String selected = notificationList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                notificationsData.remove(selected);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No notification selected.");
                alert.showAndWait();
            }
        });
    
        HBox controls = new HBox(10, clearNotificationsButton, removeSelectedNotificationButton);
        controls.setAlignment(Pos.CENTER);
    
        notifications.getChildren().addAll(notificationList, controls);
        return notifications;
    }
    
    
    
    public class User {
        private final javafx.beans.property.StringProperty username;
        private final javafx.beans.property.StringProperty role;
    
        public User(String username, String role) {
            this.username = new javafx.beans.property.SimpleStringProperty(username);
            this.role = new javafx.beans.property.SimpleStringProperty(role);
        }
    
        public String getUsername() {
            return username.get();
        }
    
        public javafx.beans.property.StringProperty usernameProperty() {
            return username;
        }
    
        public String getRole() {
            return role.get();
        }
    
        public javafx.beans.property.StringProperty roleProperty() {
            return role;
        }
    }
    
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
    
        
    public class Product {
        private final StringProperty name;
        private final IntegerProperty quantity;
        private final DoubleProperty price;
        private final StringProperty category;
        private final ObjectProperty<LocalDate> expirationDate;

        public Product(String name, int quantity, double price, String category, LocalDate expirationDate) {
            this.name = new SimpleStringProperty(name);
            this.quantity = new SimpleIntegerProperty(quantity);
            this.price = new SimpleDoubleProperty(price);
            this.category = new SimpleStringProperty(category);
            this.expirationDate = new SimpleObjectProperty<>(expirationDate);
        }

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public int getQuantity() {
            return quantity.get();
        }

        public IntegerProperty quantityProperty() {
            return quantity;
        }

        public double getPrice() {
            return price.get();
        }

        public DoubleProperty priceProperty() {
            return price;
        }

        public String getCategory() {
            return category.get();
        }

        public StringProperty categoryProperty() {
            return category;
        }

        public LocalDate getExpirationDate() {
            return expirationDate.get();
        }

        public ObjectProperty<LocalDate> expirationDateProperty() {
            return expirationDate;
        }
    }

        public class Message {
        private final StringProperty subject;
        private final StringProperty body;

        public Message(String subject, String body) {
            this.subject = new SimpleStringProperty(subject);
            this.body = new SimpleStringProperty(body);
        }

        public String getSubject() {
            return subject.get();
        }

        public StringProperty subjectProperty() {
            return subject;
        }

        public String getBody() {
            return body.get();
        }

        public StringProperty bodyProperty() {
            return body;
        }
    }
        
    
      //log out nandito  
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to log out.");
        alert.setContentText("Do you want to continue?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            
            primaryStage.close();
        }
    }
    private void populateCategoryComboBox() {
        if (categoryComboBox != null) {
            categoryComboBox.getItems().clear();
            categoryComboBox.getItems().addAll(DatabaseManager.getCategories());
        } else {
            System.out.println("categoryComboBox is null – cannot populate categories.");
        }
    }
    
 
    private List<ReportItem> generateReportContent() {
        List<ReportItem> reportItems = new ArrayList<>();

        String inventoryQuery = """
            SELECT p.product_name, c.category_name AS category, p.barcode, p.quantity, p.price, p.expiration_date 
            FROM products p
            LEFT JOIN categories c ON p.category_id = c.id
        """;
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(inventoryQuery)) {

            while (rs.next()) {
                String productName = rs.getString("product_name");
                String category = rs.getString("category");
                String barcode = rs.getString("barcode");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String expirationDate = rs.getString("expiration_date");

                reportItems.add(new ReportItem(productName, category, barcode, quantity, price, expirationDate));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reportItems;
    }

    private VBox createReport() {
        VBox reportLayout = new VBox(10);
        reportLayout.setPadding(new Insets(20));
        reportLayout.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        Label header = new Label("Inventory Report");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
        reportLayout.getChildren().add(header);

        HBox dateRangeBox = new HBox(10);
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();
        Button filterDateButton = new Button("Filter by Date");

        TableView<ReportItem> reportTable = new TableView<>();

        filterDateButton.setOnAction(e -> filterReportByDate(reportTable, startDatePicker.getValue(), endDatePicker.getValue()));
        dateRangeBox.getChildren().addAll(new Label("Start Date:"), startDatePicker, new Label("End Date:"), endDatePicker, filterDateButton);
        reportLayout.getChildren().add(dateRangeBox);
        HBox summaryBox = new HBox(20);
        summaryBox.setPadding(new Insets(10));
        summaryBox.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 10; -fx-border-radius: 5;");

        Label totalStockLabel = new Label("Total Stock: Loading...");
        Label totalSalesLabel = new Label("Total Sales: Loading...");
        Label totalExpiredLabel = new Label("Total Expired: Loading...");

        summaryBox.getChildren().addAll(totalStockLabel, totalSalesLabel, totalExpiredLabel);
        reportLayout.getChildren().add(summaryBox);

        TableColumn<ReportItem, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        productNameColumn.setSortable(true);

        TableColumn<ReportItem, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        categoryColumn.setSortable(true);

        TableColumn<ReportItem, String> barcodeColumn = new TableColumn<>("Barcode");
        barcodeColumn.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
        barcodeColumn.setSortable(true);

        TableColumn<ReportItem, Number> quantityColumn = new TableColumn<>("Quantity in Stock");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        quantityColumn.setSortable(true);

        TableColumn<ReportItem, Number> priceColumn = new TableColumn<>("Price (PHP)");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        priceColumn.setSortable(true);

        TableColumn<ReportItem, String> expirationDateColumn = new TableColumn<>("Expiration Date");
        expirationDateColumn.setCellValueFactory(cellData -> cellData.getValue().expirationDateProperty());
        expirationDateColumn.setSortable(true);

        reportTable.getColumns().addAll(productNameColumn, categoryColumn, barcodeColumn, quantityColumn, priceColumn, expirationDateColumn);

        new Thread(() -> {
            List<ReportItem> reportItems = generateReportContent();
            Platform.runLater(() -> {
                reportTable.getItems().setAll(reportItems);
                updateSummaryStatistics(totalStockLabel, totalSalesLabel, totalExpiredLabel, reportItems);
            });
        }).start();

        
        reportTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ReportItem selectedItem = reportTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    showDetailedView(selectedItem);
                }
            }
        });

        reportLayout.getChildren().add(reportTable);

        Button exportButton = new Button("Export to CSV");
        exportButton.setOnAction(e -> exportReportToCSV(reportTable.getItems()));
        reportLayout.getChildren().add(exportButton);

        Button printButton = new Button("Print Report");
        printButton.setOnAction(e -> printReport(reportTable));
        reportLayout.getChildren().add(printButton);

        return reportLayout;
    }

    private void updateSummaryStatistics(Label totalStockLabel, Label totalSalesLabel, Label totalExpiredLabel, List<ReportItem> reportItems) {
        int totalStock = reportItems.stream().mapToInt(ReportItem::getQuantity).sum();
        double totalSales = reportItems.stream().mapToDouble(ReportItem::getPrice).sum();
        int totalExpired = (int) reportItems.stream().filter(item -> item.getExpirationDate() != null).count();

        totalStockLabel.setText("Total Stock: " + totalStock);
        totalSalesLabel.setText("Total Sales: ₱" + totalSales);
        totalExpiredLabel.setText("Total Expired: " + totalExpired);
    }

    private void filterReportByDate(TableView<ReportItem> reportTable, LocalDate startDate, LocalDate endDate) {
        ObservableList<ReportItem> filteredList = FXCollections.observableArrayList();
        for (ReportItem item : reportTable.getItems()) {
            LocalDate expirationDate = LocalDate.parse(item.getExpirationDate());
            if ((startDate == null || !expirationDate.isBefore(startDate)) && (endDate == null || !expirationDate.isAfter(endDate))) {
                filteredList.add(item);
            }
        }
        reportTable.setItems(filteredList);
    }

    private void showDetailedView(ReportItem item) {
        // Implementation for showing detailed view
    }

    private void exportReportToCSV(List<ReportItem> reportItems) {
        File file = new File("report.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Product Name,Category,Barcode,Quantity,Price,Expiration Date\n");
            for (ReportItem item : reportItems) {
                writer.write(String.format("%s,%s,%s,%d,%.2f,%s\n",
                        item.getProductName(), item.getCategory(), item.getBarcode(),
                        item.getQuantity(), item.getPrice(), item.getExpirationDate()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printReport(TableView<ReportItem> reportTable) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(primaryStage)) {
            boolean success = job.printPage(reportTable);
            if (success) {
                job.endJob();
            }
        }
    }

    public class ReportItem {
        private final StringProperty productName;
        private final StringProperty category;
        private final StringProperty barcode;
        private final IntegerProperty quantity;
        private final DoubleProperty price;
        private final StringProperty expirationDate;

        public ReportItem(String productName, String category, String barcode, int quantity, double price, String expirationDate) {
            this.productName = new SimpleStringProperty(productName);
            this.category = new SimpleStringProperty(category);
            this.barcode = new SimpleStringProperty(barcode);
            this.quantity = new SimpleIntegerProperty(quantity);
            this.price = new SimpleDoubleProperty(price);
            this.expirationDate = new SimpleStringProperty(expirationDate);
        }

        public StringProperty productNameProperty() {
            return productName;
        }

        public StringProperty categoryProperty() {
            return category;
        }            
        public StringProperty barcodeProperty() {
            return barcode;
        }

        public IntegerProperty quantityProperty() {
            return quantity;
        }

        public DoubleProperty priceProperty() {
            return price;
        }

        public StringProperty expirationDateProperty() {
            return expirationDate;
        }

        public int getQuantity() {
            return quantity.get();
        }

        public double getPrice() {
            return price.get();
        }

        public String getExpirationDate() {
            return expirationDate.get();
        }

        public String getProductName() {
            return productName.get();
        }

        public String getCategory() {
            return category.get();
        }

        public String getBarcode() {
            return barcode.get();
        }
    }    
}

/*private void showNotifications() {
        if (!isLoggedIn) {
            return; // Do not show notifications if the user is not logged in
        }

        if (notifications.isEmpty()) {
            showAlert("No Notifications", "You have no new notifications.");
            return;
        }

        for (String notification : notifications) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText(notification);
            alert.setContentText("Choose an option:");

            ButtonType readButton = new ButtonType("Read");
            ButtonType laterButton = new ButtonType("Later");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(readButton, laterButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == readButton) {
                inbox.add(notification); // Move to inbox
            } else if (result.isPresent() && result.get() == laterButton) {
                // Do nothing, keep it in notifications
            } else {
                break; // Cancel button pressed
            }
        }

        // Clear notifications after showing
        notifications.clear();
    }*/

  /* private VBox createReport() {
        VBox reportLayout = new VBox(10);
        reportLayout.setPadding(new Insets(20));
        reportLayout.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        Label header = new Label("Inventory Report");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
        reportLayout.getChildren().add(header);

        // Create a TableView for the report
        TableView<ReportItem> reportTable = new TableView<>();
        reportTable.setStyle("-fx-background-color: white; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        // Define columns for the report
        TableColumn<ReportItem, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());

        TableColumn<ReportItem, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());

        TableColumn<ReportItem, String> barcodeColumn = new TableColumn<>("Barcode");
        barcodeColumn.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());

        TableColumn<ReportItem, Number> quantityColumn = new TableColumn<>("Quantity in Stock");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());

        TableColumn<ReportItem, Number> priceColumn = new TableColumn<>("Price (PHP)");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());

        TableColumn<ReportItem, String> expirationDateColumn = new TableColumn<>("Expiration Date");
        expirationDateColumn.setCellValueFactory(cellData -> cellData.getValue().expirationDateProperty());

        // Add columns to the table
        reportTable.getColumns().addAll(productNameColumn, categoryColumn, barcodeColumn, quantityColumn, priceColumn, expirationDateColumn);

        // Load the report data asynchronously
        new Thread(() -> {
            List<ReportItem> reportItems = generateReportContent();
            Platform.runLater(() -> {
                reportTable.getItems().setAll(reportItems);
            });
        }).start();

        reportLayout.getChildren().add(reportTable);
        return reportLayout;
    }
    
    
    private List<ReportItem> generateReportContent() {
        List<ReportItem> reportItems = new ArrayList<>();

        // Fetching Inventory Data
        String inventoryQuery = """
            SELECT p.product_name, c.category_name AS category, p.barcode, p.quantity, p.price, p.expiration_date 
            FROM products p
            LEFT JOIN categories c ON p.category_id = c.id
        """;
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(inventoryQuery)) {

            while (rs.next()) {
                String productName = rs.getString("product_name");
                String category = rs.getString("category");
                String barcode = rs.getString("barcode");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String expirationDate = rs.getString("expiration_date");

                reportItems.add(new ReportItem(productName, category, barcode, quantity, price, expirationDate));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reportItems;
    }
   /* private VBox createReport() {
        VBox reportLayout = new VBox(10);
        reportLayout.setPadding(new Insets(20));
        reportLayout.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        Label header = new Label("Inventory Report");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
        reportLayout.getChildren().add(header);

        // Date Range Selection
        HBox dateRangeBox = new HBox(10);
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();
        Button filterDateButton = new Button("Filter by Date");
        filterDateButton.setOnAction(e -> filterReportByDate(reportTable, startDatePicker.getValue(), endDatePicker.getValue()));
        dateRangeBox.getChildren().addAll(new Label("Start Date:"), startDatePicker, new Label("End Date:"), endDatePicker, filterDateButton);
        reportLayout.getChildren().add(dateRangeBox);

        // Summary Statistics
        HBox summaryBox = new HBox(20);
        summaryBox.setPadding(new Insets(10));
        summaryBox.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 10; -fx-border-radius: 5;");

        Label totalStockLabel = new Label("Total Stock: Loading...");
        Label totalSalesLabel = new Label("Total Sales: Loading...");
        Label totalExpiredLabel = new Label("Total Expired: Loading...");

        summaryBox.getChildren().addAll(totalStockLabel, totalSalesLabel, totalExpiredLabel);
        reportLayout.getChildren().add(summaryBox);

        // Create a TableView for the report
        TableView<ReportItem> reportTable = new TableView<>();
        reportTable.setStyle("-fx-background-color: white; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        // Define columns for the report
        TableColumn<ReportItem, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        productNameColumn.setSortable(true);

        TableColumn<ReportItem, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        categoryColumn.setSortable(true);

        TableColumn<ReportItem, String> barcodeColumn = new TableColumn<>("Barcode");
        barcodeColumn.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
        barcodeColumn.setSortable(true);

        TableColumn<ReportItem, Number> quantityColumn = new TableColumn<>("Quantity in Stock");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        quantityColumn.setSortable(true);

        TableColumn<ReportItem, Number> priceColumn = new TableColumn<>("Price (PHP)");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        priceColumn.setSortable(true);

        TableColumn<ReportItem, String> expirationDateColumn = new TableColumn<>("Expiration Date");
        expirationDateColumn.setCellValueFactory(cellData -> cellData.getValue().expirationDateProperty());
        expirationDateColumn.setSortable(true);

        // Add columns to the table
        reportTable.getColumns().addAll(productNameColumn, categoryColumn, barcodeColumn, quantityColumn, priceColumn, expirationDateColumn);

        // Load the report data asynchronously
        new Thread(() -> {
            List<ReportItem> reportItems = generateReportContent();
            Platform.runLater(() -> {
                reportTable.getItems().setAll(reportItems);
                updateSummaryStatistics(totalStockLabel, totalSalesLabel, totalExpiredLabel, reportItems);
            });
        }).start();

        // Add click listener for detailed view
        reportTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ReportItem selectedItem = reportTable.getSelectionModel().getSelectedItem();
                if ( selectedItem != null) {
                    showDetailedView(selectedItem);
                }
            }
        });

        reportLayout.getChildren().add(reportTable);

        // Export Button
        Button exportButton = new Button("Export to CSV");
        exportButton.setOnAction(e -> exportReportToCSV(reportTable.getItems()));
        reportLayout.getChildren().add(exportButton);

        // Print Button
        Button printButton = new Button("Print Report");
        printButton.setOnAction(e -> printReport(reportTable));
        reportLayout.getChildren().add(printButton);

        return reportLayout;
    }
    
    
   /* //---------------------------------------------------------------------------------------
    private void filterReport(TableView<ReportItem> reportTable, String searchText) {
        ObservableList<ReportItem> filteredList = FXCollections.observableArrayList();
        for (ReportItem item : reportTable.getItems()) {
            if (item.getProductName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(item);
            }
        }
        reportTable.setItems(filteredList);
    }

    private void filterReportByCategory(TableView<ReportItem> reportTable, String category) {
        ObservableList<ReportItem> filteredList = FXCollections.observableArrayList();
        for (ReportItem item : reportTable.getItems()) {
            if (category.equals("All Categories") || item.getCategory().equals(category)) {
                filteredList.add(item);
            }
        }
        reportTable.setItems(filteredList);
    }
    
    private void updateSummaryStatistics(Label totalStockLabel, Label totalSalesLabel, Label totalExpiredLabel, List<ReportItem> reportItems) {
        int totalStock = reportItems.stream().mapToInt(ReportItem::getQuantity).sum();
        double totalSales = reportItems.stream().mapToDouble(ReportItem::getPrice).sum();
        int totalExpired = (int) reportItems.stream().filter(item -> item.getExpirationDate() != null).count(); // Adjust this logic as needed

        totalStockLabel.setText("Total Stock: " + totalStock);
        totalSalesLabel.setText("Total Sales: ₱" + totalSales);
        total ExpiredLabel.setText("Total Expired: " + totalExpired);
    }
    private void exportReportToCSV(List<ReportItem> reportItems) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("Product Name,Category,Barcode,Quantity,Price,Expiration Date");
                writer.newLine();
                for (ReportItem item : reportItems) {
                    writer.write(String.join(",",
                            item.getProductName(),
                            item.getCategory(),
                            item.getBarcode(),
                            String.valueOf(item.getQuantity()),
                            String.valueOf(item.getPrice()),
                            item.getExpirationDate()));
                    writer.newLine();
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Report exported successfully!");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error exporting report: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
    
    private void filterReportByDate(TableView<ReportItem> reportTable, LocalDate startDate, LocalDate endDate) {
        ObservableList<ReportItem> filteredList = FXCollections.observableArrayList();
        for (ReportItem item : reportTable.getItems()) {
            LocalDate expirationDate = LocalDate.parse(item.getExpirationDate());
            if ((startDate == null || !expirationDate.isBefore(startDate)) && (endDate == null || !expirationDate.isAfter(endDate))) {
                filteredList.add(item);
            }
        }
        reportTable.setItems(filteredList);
    }
    
    private void showDetailedView(ReportItem item) {
        Stage detailStage = new Stage();
        VBox detailLayout = new VBox(10);
        detailLayout.setPadding(new Insets(20));
        detailLayout.setStyle("-fx-background-color: #f4f4f4;");

        Label titleLabel = new Label("Product Details");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label productNameLabel = new Label("Product Name: " + item.getProductName());
        Label categoryLabel = new Label("Category: " + item.getCategory());
        Label barcodeLabel = new Label("Barcode: " + item.getBarcode());
        Label quantityLabel = new Label("Quantity in Stock: " + item.getQuantity());
        Label priceLabel = new Label("Price (PHP): " + item.getPrice());
        Label expirationDateLabel = new Label("Expiration Date: " + item.getExpirationDate());

        detailLayout.getChildren().addAll(titleLabel, productNameLabel, categoryLabel, barcodeLabel, quantityLabel, priceLabel, expirationDateLabel);
        Scene scene = new Scene(detailLayout, 300, 250);
        detailStage.setScene(scene);
        detailStage.setTitle("Product Details");
        detailStage.show();
    }
    
    private void printReport(TableView<ReportItem> reportTable) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(reportTable.getScene().getWindow())) {
            boolean success = job.printPage(reportTable);
            if (success) {
                job.endJob();
            }
        }
    }

    
    
    
    
    
    
    
    
    //--------------------------------------------------------------------------------------------*/
  
  /*  private List<ReportItem> generateReportContent() {
        List<ReportItem> reportItems = new ArrayList<>();

        // Fetching Inventory Data
        String inventoryQuery = """
            SELECT p.product_name, c.category_name AS category, p.barcode, p.quantity, p.price, p.expiration_date 
            FROM products p
            LEFT JOIN categories c ON p.category_id = c.id
        """;
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(inventoryQuery)) {

            while (rs.next()) {
                String productName = rs.getString("product_name");
                String category = rs.getString("category");
                String barcode = rs.getString("barcode");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String expirationDate = rs.getString("expiration_date");

                reportItems.add(new ReportItem(productName, category, barcode, quantity, price, expirationDate));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reportItems;
    }

    private VBox createReport() {
        VBox reportLayout = new VBox(10);
        reportLayout.setPadding(new Insets(20));
        reportLayout.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        Label header = new Label("Inventory Report");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
        reportLayout.getChildren().add(header);

        // Date Range Selection
        HBox dateRangeBox = new HBox(10);
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();
        Button filterDateButton = new Button("Filter by Date");

        // Declare reportTable here
        TableView<ReportItem> reportTable = new TableView<>();

        filterDateButton.setOnAction(e -> filterReportByDate(reportTable, startDatePicker.getValue(), endDatePicker.getValue()));
        dateRangeBox.getChildren().addAll(new Label("Start Date:"), startDatePicker, new Label("End Date:"), endDatePicker, filterDateButton);
        reportLayout.getChildren().add(dateRangeBox);

        // Summary Statistics
        HBox summaryBox = new HBox(20);
        summaryBox.setPadding(new Insets(10));
        summaryBox.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 10; -fx-border-radius: 5;");

        Label totalStockLabel = new Label("Total Stock: Loading...");
        Label totalSalesLabel = new Label("Total Sales: Loading...");
        Label totalExpiredLabel = new Label("Total Expired: Loading...");

        summaryBox.getChildren().addAll(totalStockLabel, totalSalesLabel, totalExpiredLabel);
        reportLayout.getChildren().add(summaryBox);

        // Define columns for the report
        TableColumn<ReportItem, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        productNameColumn.setSortable(true);

        TableColumn<ReportItem, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        categoryColumn.setSortable(true);

        TableColumn<ReportItem, String> barcodeColumn = new TableColumn<>("Barcode");
        barcodeColumn.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
        barcodeColumn.setSortable(true);

        TableColumn<ReportItem, Number> quantityColumn = new TableColumn<>("Quantity in Stock");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        quantityColumn.setSortable(true);

        TableColumn<ReportItem, Number> priceColumn = new TableColumn<>("Price (PHP)");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        priceColumn.setSortable(true);

        TableColumn<ReportItem, String> expirationDateColumn = new TableColumn<>("Expiration Date ");
        expirationDateColumn.setCellValueFactory(cellData -> cellData.getValue().expirationDateProperty());
        expirationDateColumn.setSortable(true);

        // Add columns to the table
        reportTable.getColumns().addAll(productNameColumn, categoryColumn, barcodeColumn, quantityColumn, priceColumn, expirationDateColumn);

        // Load the report data asynchronously
        new Thread(() -> {
            List<ReportItem> reportItems = generateReportContent();
            Platform.runLater(() -> {
                reportTable.getItems().setAll(reportItems);
                updateSummaryStatistics(totalStockLabel, totalSalesLabel, totalExpiredLabel, reportItems);
            });
        }).start();

        // Add click listener for detailed view
        reportTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ReportItem selectedItem = reportTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    showDetailedView(selectedItem);
                }
            }
        });

        reportLayout.getChildren().add(reportTable);

        // Export Button
        Button exportButton = new Button("Export to CSV");
        exportButton.setOnAction(e -> exportReportToCSV(reportTable.getItems()));
        reportLayout.getChildren().add(exportButton);

        // Print Button
        Button printButton = new Button("Print Report");
        printButton.setOnAction(e -> printReport(reportTable));
        reportLayout.getChildren().add(printButton);

        return reportLayout;
    }

    private void updateSummaryStatistics(Label totalStockLabel, Label totalSalesLabel, Label totalExpiredLabel, List<ReportItem> reportItems) {
        int totalStock = reportItems.stream().mapToInt(ReportItem::getQuantity).sum();
        double totalSales = reportItems.stream().mapToDouble(ReportItem::getPrice).sum();
        int totalExpired = (int) reportItems.stream().filter(item -> item.getExpirationDate() != null).count(); // Adjust this logic as needed

        totalStockLabel.setText("Total Stock: " + totalStock);
        totalSalesLabel.setText("Total Sales: ₱" + totalSales);
        totalExpiredLabel.setText("Total Expired: " + totalExpired); // Fixed variable name
    }
    
    public class ReportItem {
        private final StringProperty productName;
        private final StringProperty category;
        private final StringProperty barcode;
        private final IntegerProperty quantity;
        private final DoubleProperty price;
        private final StringProperty expirationDate;

        public ReportItem(String productName, String category, String barcode, int quantity, double price, String expirationDate) {
            this.productName = new SimpleStringProperty(productName);
            this.category = new SimpleStringProperty(category);
            this.barcode = new SimpleStringProperty(barcode);
            this.quantity = new SimpleIntegerProperty(quantity);
            this.price = new SimpleDoubleProperty(price);
            this.expirationDate = new SimpleStringProperty(expirationDate);
        }

        public StringProperty productNameProperty() {
            return productName;
        }

        public StringProperty categoryProperty() {
            return category;
        }

        public StringProperty barcodeProperty() {
            return barcode;
        }

        public IntegerProperty quantityProperty() {
            return quantity;
        }

        public DoubleProperty priceProperty() {
            return price;
        }

        public StringProperty expirationDateProperty() {
            return expirationDate;
        }
    }   */  




       // loadDashboardData(stockInLabel, stockOutLabel, popularProductLabel, totalSalesLabel, totalExpiredLabel);