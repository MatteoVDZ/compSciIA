package com.example.demo4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.sql.*;

public class Products {

    static final String DB_URL = "jdbc:mysql://localhost:3306/clothing";
    static String driverName = "com.mysql.cj.jdbc.Driver";
    static final String USER = "root";
    static final String PASS = "WilleM8477";
    static final String QUERY = "SELECT * FROM products";
    static Connection conn = null;

    static ResultSet products_rs;
    static boolean currentlyEditing = false;
    static boolean currentlyInserting = false;

    @FXML
    TextField txtProductID;
    @FXML TextField txtCategory;
    @FXML TextField txtMaterial;
    @FXML TextField txtPrice;
    @FXML TextField txtColour;
    @FXML Button btnEdit;
    @FXML Button btnDetails;
    @FXML Button btnInsert;
    @FXML Button btnDelete;

    @FXML Button btnNext;
    @FXML Button btnPrevious;
    @FXML Button btnCustomers;
    @FXML Button btnSales;

    @FXML CheckBox noProductsFilter;
    @FXML CheckBox filterTShirts;
    @FXML CheckBox filterJackets;
    @FXML CheckBox filterJeans;
    @FXML CheckBox filterSweaters;
    @FXML CheckBox filterWhite;
    @FXML CheckBox filterBlack;
    @FXML Button btnReport;

    @FXML
    public void initialize(){

        // Open a connection
        try {Connection conn = null;
            Class.forName(driverName).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            products_rs = stmt.executeQuery(QUERY);
            products_rs.next();
            updateTextInFields();
            setEditable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setEditable(boolean editable) {
        txtProductID.setEditable(editable);
        txtCategory.setEditable(editable);
        txtMaterial.setEditable(editable);
        txtPrice.setEditable(editable);
        txtColour.setEditable(editable);
    }

    @FXML
    protected void next(ActionEvent event){

        try{
            if (products_rs.isLast()) {
                products_rs.first();
            } else {
                products_rs.next();
            }
            updateTextInFields();
        } catch (Exception e) {
            e.printStackTrace();
            //next(event);
        }
    }

    @FXML
    protected void previous(ActionEvent event){

        try{
            if (products_rs.isFirst()) {
                products_rs.last();
            } else {
                products_rs.previous();
            }
            updateTextInFields();
        } catch (Exception e) {
            e.printStackTrace();
            //previous(event);
        }
    }

    protected void updateTextInFields(){
        try{
            txtProductID.setText(products_rs.getString("product_id"));
            txtCategory.setText(products_rs.getString("category"));
            txtMaterial.setText(products_rs.getString("material"));
            txtPrice.setText(products_rs.getString("price"));
            txtColour.setText(products_rs.getString("colour_name"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrentID() throws SQLException {
        return products_rs.getString("product_id");
    }

    public Integer getFinalID() throws SQLException {
        products_rs.last();
        return Integer.parseInt(products_rs.getString("product_id")) + 1;
    }

    @FXML protected void update(){
        if (!currentlyEditing) {
            setEditable(true);
            currentlyEditing = true;
            btnEdit.setText("Save");
        }
        else {
            try{
                products_rs.updateString("category", txtCategory.getText());
                products_rs.updateString("material", txtMaterial.getText());
                products_rs.updateString("price", txtPrice.getText());
                products_rs.updateString("colour_name", txtColour.getText());
                products_rs.updateRow();

                setEditable(false);
                currentlyEditing = false;
                btnEdit.setText("Edit");
                updateTextInFields();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void insert(ActionEvent actionEvent) throws SQLException {
        Main.getInstance().showProductDetails(true);
    }

    @FXML
    public void delete(ActionEvent actionEvent) {
        try {
            String delete_product_sql = "DELETE FROM products WHERE product_id=" + txtProductID.getText();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate(delete_product_sql);

            String delete_sizes_sql = "DELETE FROM sizes WHERE size_id =" + txtProductID.getText();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate(delete_sizes_sql);

            products_rs.deleteRow();
            updateTextInFields();
            Dialog dialog = new Dialog();
            dialog.setContentText("Record Deleted");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
            closeButton.managedProperty().bind(closeButton.visibleProperty());
            closeButton.setVisible(false);
            dialog.showAndWait();
        } catch (Exception e) {
            Dialog dialog = new Dialog();
            dialog.setContentText("Error in Deleting");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
            closeButton.managedProperty().bind(closeButton.visibleProperty());
            closeButton.setVisible(false);
            dialog.showAndWait();

            e.printStackTrace();
        }
    }

    public void switchToCustomers(ActionEvent event) {
        // Call a method in the main application class to switch scenes
        Main.getInstance().showCustomersScene();
    }

    public void switchToSales(ActionEvent event) {
        // Call a method in the main application class to switch scenes
        Main.getInstance().showSalesScene();
    }

    String newQuery = "SELECT * FROM products";

    public void filter(ActionEvent event) {
        if(noProductsFilter.isSelected()) {
            newQuery = "SELECT * FROM products";
            filterTShirts.setSelected(false);
            filterJackets.setSelected(false);
            filterSweaters.setSelected(false);
            filterJeans.setSelected(false);
            filterWhite.setSelected(false);
            filterBlack.setSelected(false);
        } if(filterTShirts.isSelected()) {
            newQuery = "SELECT * FROM products WHERE category = 'T-Shirt'";
            noProductsFilter.setSelected(false);
            filterJackets.setSelected(false);
            filterSweaters.setSelected(false);
            filterJeans.setSelected(false);
            filterWhite.setSelected(false);
            filterBlack.setSelected(false);
        } if(filterJackets.isSelected()){
            newQuery = "SELECT * FROM products WHERE category = 'Jacket'";
            noProductsFilter.setSelected(false);
            filterTShirts.setSelected(false);
            filterSweaters.setSelected(false);
            filterJeans.setSelected(false);
            filterWhite.setSelected(false);
            filterBlack.setSelected(false);
        } if(filterSweaters.isSelected()){
            newQuery = "SELECT * FROM products WHERE category = 'Sweater'";
            noProductsFilter.setSelected(false);
            filterTShirts.setSelected(false);
            filterJackets.setSelected(false);
            filterJeans.setSelected(false);
            filterWhite.setSelected(false);
            filterBlack.setSelected(false);
        } if(filterJeans.isSelected()){
            newQuery = "SELECT * FROM products WHERE category = 'Jeans'";
            noProductsFilter.setSelected(false);
            filterTShirts.setSelected(false);
            filterJackets.setSelected(false);
            filterSweaters.setSelected(false);
            filterWhite.setSelected(false);
            filterBlack.setSelected(false);
        } if(filterWhite.isSelected()) {
            newQuery = "SELECT * FROM products WHERE colour_name = 'White'";
            noProductsFilter.setSelected(false);
            filterTShirts.setSelected(false);
            filterJackets.setSelected(false);
            filterJeans.setSelected(false);
            filterSweaters.setSelected(false);
            filterBlack.setSelected(false);
        } if(filterBlack.isSelected()) {
            newQuery = "SELECT * FROM products WHERE colour_name = 'Black'";
            noProductsFilter.setSelected(false);
            filterTShirts.setSelected(false);
            filterJackets.setSelected(false);
            filterJeans.setSelected(false);
            filterSweaters.setSelected(false);
            filterWhite.setSelected(false);
        }

        try {
            Connection conn = null;
            Class.forName(driverName).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            products_rs = stmt.executeQuery(newQuery);
            products_rs.next();
            updateTextInFields();
            setEditable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void seeMore(ActionEvent event) {
        Main.getInstance().showProductDetails(false);
    }
    public void report(ActionEvent event) { Main.getInstance().showReportScene();}

    public void search(ActionEvent event) throws SQLException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Product Search");
        dialog.setHeaderText("Enter Product ID");
        dialog.setContentText("Product ID:");

        // Traditional way to get the response value.
        java.util.Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String productId = result.get();
            while(products_rs.next()) {
                if(products_rs.getString("product_id").equals(productId)) {
                    updateTextInFields();
                    break;
                }
            }
        }
    }
}
