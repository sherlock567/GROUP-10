import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/InventoryDB_Connection";
    private static final String USER = "root";
    private static final String PASSWORD = "244466666";
    
    //constructor

    public static Connection connect() throws SQLException {
         return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
    
    
    
    

 
    public static ResultSet getDashboardData() throws SQLException {
        String query = """
            SELECT 
                (SELECT COUNT(*) FROM stock_in) AS stock_in, 
                (SELECT COUNT(*) FROM stock_out) AS stock_out, 
                (SELECT product_name FROM products ORDER BY quantity DESC LIMIT 1) AS popular_product, 
                (SELECT IFNULL(SUM(total_price), 0) FROM sales) AS total_sales, 
                (SELECT COUNT(*) FROM products WHERE expiration_date < CURDATE()) AS total_expired
            FROM DUAL
        """;
        Connection conn = connect();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }


    public static ObservableList<User> getAllUsers() {
        ObservableList<User> userList = FXCollections.observableArrayList();
        String query = "SELECT id, username, role FROM users";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                userList.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("role")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static void insertUser(String username, String password, String role) throws SQLException {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.executeUpdate();
            
            
        }
    }

    public static void deleteUser(String username) throws SQLException {
        String query = "DELETE FROM users WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        }
    }

    public static boolean validateUser(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
    
    public static String getCategoryNameById(int categoryId) {
        String query = "SELECT category_name FROM categories WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("category_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown"; // Return a default value if not found
    }

   
    public static List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT category_name FROM categories"; 
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                categories.add(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }


    public static Product getProductByBarcode(String barcode) throws SQLException {
        String query = """
            SELECT p.product_name, p.quantity, p.price, c.category_name AS category, p.expiration_date
            FROM products p
            LEFT JOIN categories c ON p.category_id = c.id
            WHERE p.barcode = ?
        """;
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, barcode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Product(
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getString("category"),
                    rs.getDate("expiration_date").toLocalDate()
                );
            }
        }
        return null;
    }

    public static void insertProductWithBarcode(String barcode, String name, int quantity, double price, String category, String expirationDate) throws SQLException {
        int categoryId = getCategoryIdOrCreate(category);
        String query = "INSERT INTO products (barcode, product_name, quantity, price, expiration_date, category_id) VALUES (?, ?, ?, ?, ?, ?)";

        // Check if the product with the same name and expiration date already exists
        String checkQuery = "SELECT COUNT(*) FROM products WHERE product_name = ? AND expiration_date = ?";
        try (Connection conn = connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setString(1, name);
            checkStmt.setString(2, expirationDate);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("Product with the same name and expiration date already exists.");
            }
        }

    try (Connection conn = connect();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, barcode);
        stmt.setString(2, name);
        stmt.setInt(3, quantity);
        stmt.setDouble(4, price);
        stmt.setString(5, expirationDate);
        stmt.setInt(6, categoryId);
        stmt.executeUpdate();
    }
}

    public static void updateProductQuantity(String barcode, int newQuantity) throws SQLException {
        String query = "UPDATE products SET quantity = ? WHERE barcode = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, newQuantity);
            stmt.setString(2, barcode);
            stmt.executeUpdate();
        }
    }
    
    public static int getCategoryId(String category) {
        String query = "SELECT id FROM categories WHERE category_name = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  
    }

    private static int getCategoryIdOrCreate(String category) throws SQLException {
        String selectQuery = "SELECT id FROM categories WHERE category_name = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        String insertQuery = "INSERT INTO categories (category_name) VALUES (?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, category);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Failed to create category");
    }
    
   

 
    public static class Product {
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

        public String getName() { return name.get(); }
        public int getQuantity() { return quantity.get(); }
        public double getPrice() { return price.get(); }
        public String getCategory() { return category.get(); }
        public LocalDate getExpirationDate() { return expirationDate.get(); }

        public StringProperty nameProperty() { return name; }
        public IntegerProperty quantityProperty() { return quantity; }
        public DoubleProperty priceProperty() { return price; }
        public StringProperty categoryProperty() { return category; }
        public ObjectProperty<LocalDate> expirationDateProperty() { return expirationDate; }
    }
}


