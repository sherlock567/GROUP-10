public class Product {
    private final javafx.beans.property.StringProperty name;
    private final javafx.beans.property.IntegerProperty quantity;
    private final javafx.beans.property.DoubleProperty price;
    private final javafx.beans.property.StringProperty category; 
    private final javafx.beans.property.ObjectProperty<java.time.LocalDate> expirationDate; 


    public Product(String name, int quantity, double price, String category, java.time.LocalDate expirationDate) {
        this.name = new javafx.beans.property.SimpleStringProperty(name);
        this.quantity = new javafx.beans.property.SimpleIntegerProperty(quantity);
        this.price = new javafx.beans.property.SimpleDoubleProperty(price);
        this.category = new javafx.beans.property.SimpleStringProperty(category);
        this.expirationDate = new javafx.beans.property.SimpleObjectProperty<>(expirationDate);
    }

   
    public Product(int id, String name, int quantity, double price, String category, java.time.LocalDate expirationDate) {
        this.name = new javafx.beans.property.SimpleStringProperty(name);
        this.quantity = new javafx.beans.property.SimpleIntegerProperty(quantity);
        this.price = new javafx.beans.property.SimpleDoubleProperty(price);
        this.category = new javafx.beans.property.SimpleStringProperty(category);
        this.expirationDate = new javafx.beans.property.SimpleObjectProperty<>(expirationDate);
    }

    
    public String getCategory() {
        return category.get();
    }

    public javafx.beans.property.StringProperty categoryProperty() {
        return category;
    }

    public java.time.LocalDate getExpirationDate() {
        return expirationDate.get();
    }

    public javafx.beans.property.ObjectProperty<java.time.LocalDate> expirationDateProperty() {
        return expirationDate;
    }


    public String getName() {
        return name.get();
    }

    public javafx.beans.property.StringProperty nameProperty() {
        return name;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public javafx.beans.property.IntegerProperty quantityProperty() {
        return quantity;
    }

    public double getPrice() {
        return price.get();
    }

    public javafx.beans.property.DoubleProperty priceProperty() {
        return price;
    }
}



/*import javafx.beans.property.*;


import java.time.LocalDate;
import javafx.scene.control.DatePicker;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ObjectProperty;

public class Product { 
    private final javafx.beans.property.StringProperty name;
    private final javafx.beans.property.IntegerProperty quantity;
    private final javafx.beans.property.DoubleProperty price;
    private final ObjectProperty<LocalDate> expirationDate;
    

    
    public Product(String name, int quantity, double price, LocalDate expirationDate) {
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
        this.expirationDate = new SimpleObjectProperty<>(expirationDate);
    }
    
        
           



   

    public String getName() {
        return name.get();
    }

    public javafx.beans.property.StringProperty nameProperty() {
        return name;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public javafx.beans.property.IntegerProperty quantityProperty() {
        return quantity;
    }

    public double getPrice() {
        return price.get();
    }

    public javafx.beans.property.DoubleProperty priceProperty() {
        return price;
    }
     public LocalDate getExpirationDate() {
        return expirationDate.get();
    }

    public ObjectProperty<LocalDate> expirationDateProperty() {
        return expirationDate;
    }
}


/*import javafx.beans.property.*;




import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDate;

import java.time.LocalDate;

public class Product {
    private final StringProperty barcode;
    private final StringProperty productName;
    private final DoubleProperty price;
    private final IntegerProperty quantity;
    private final ObjectProperty<LocalDate> expirationDate;
    private final StringProperty category;

    public Product(String barcode, String productName, double price, int quantity, LocalDate expirationDate, String category) {
        this.barcode = new SimpleStringProperty(barcode);
        this.productName = new SimpleStringProperty(productName);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.expirationDate = new SimpleObjectProperty<>(expirationDate);
        this.category = new SimpleStringProperty(category);
    }

    // --- Getters ---
    public String getBarcode() {
        return barcode.get();
    }

    public String getProductName() {
        return productName.get();
    }

    public double getPrice() {
        return price.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public LocalDate getExpirationDate() {
        return expirationDate.get();
    }

    public String getCategory() {
        return category.get();
    }

    // --- Setters ---
    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate.set(expirationDate);
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    // --- Property Methods (required for JavaFX bindings) ---
    public StringProperty barcodeProperty() {
        return barcode;
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public ObjectProperty<LocalDate> expirationDateProperty() {
        return expirationDate;
    }

    public StringProperty categoryProperty() {
        return category;
    }
}
*/