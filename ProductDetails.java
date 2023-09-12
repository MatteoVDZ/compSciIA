package com.example.demo4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.*;

public class ProductDetails {

    Products p = new Products();
    String currentProductID = p.getCurrentID();
    Integer finalProductID = p.getFinalID();

    int id;
    static ResultSet product_details_rs;
    static final String DB_URL = "jdbc:mysql://localhost:3306/clothing";
    static String driverName = "com.mysql.cj.jdbc.Driver";
    static final String USER = "root";
    static final String PASS = "WilleM8477";
    String QUERY = """
    SELECT *
    FROM products p
    JOIN
    sizes s ON p.product_id = s.size_id""";

    static Connection conn = null;

    @FXML TextField txtProductID;
    @FXML TextField txtCategory;
    @FXML TextField txtColour;
    @FXML TextField txtMaterial;
    @FXML TextField txtPrice;
    @FXML TextField txtSize;
    @FXML TextField txtLength;
    @FXML TextField txtShoulder;
    @FXML TextField txtSleeve;
    @FXML TextField txtHem;
    @FXML TextField txtQuantity;
    @FXML Button btnNew;

    @FXML Button backToProducts;
    static boolean currentlyInserting = false;
    static boolean currentlyEditing = false;

    public ProductDetails() throws SQLException {

    }

    @FXML
    public void initialize() throws SQLException {

        try {Connection conn = null;
            Class.forName(driverName).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            product_details_rs = stmt.executeQuery(QUERY);
            product_details_rs.next();
            updateTextInFields();
            setEditable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(product_details_rs.next()) {
            if(product_details_rs.getString("product_id").equals(currentProductID)) {
                updateTextInFields();
                break;
            }
        }
    }



    @FXML
    protected void next(ActionEvent event){
        try{
            if (product_details_rs.isLast()) {
                product_details_rs.first();
            } else {
                product_details_rs.next();
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
            if (product_details_rs.isFirst()) {
                product_details_rs.last();
            } else {
                product_details_rs.previous();
            }
            updateTextInFields();
        } catch (Exception e) {
            e.printStackTrace();
            //previous(event);
        }
    }

    public void insert() throws SQLException {
        if (!currentlyInserting) {
            setEditable(true);
            txtProductID.setText(String.valueOf(finalProductID));
            txtProductID.setEditable(false);
            txtCategory.clear();
            txtMaterial.clear();
            txtPrice.clear();
            txtColour.clear();
            txtSize.clear();
            txtLength.clear();
            txtSleeve.clear();
            txtShoulder.clear();
            txtHem.clear();
            txtQuantity.clear();
            currentlyInserting = true;
            btnNew.setText("Save");
        } else {
            try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {

                String insertProductsSQL = "INSERT INTO products (product_id, category, material, price, colour_name, quantity) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement productsStatement = connection.prepareStatement(insertProductsSQL);

                productsStatement.setString(1, String.valueOf(finalProductID));
                productsStatement.setString(2, txtCategory.getText());
                productsStatement.setString(3, txtMaterial.getText());
                productsStatement.setString(4, txtPrice.getText());
                productsStatement.setString(5, txtColour.getText());
                productsStatement.setString(6, txtQuantity.getText());

                String insertSizesSQL = "INSERT INTO sizes (size_id, category, length, shoulder, sleeve, hem, size) VALUES (?,?, ?, ?, ?, ?, ?)";
                PreparedStatement sizesStatement = connection.prepareStatement(insertSizesSQL);

                sizesStatement.setString(1, String.valueOf(finalProductID));
                sizesStatement.setString(2, txtCategory.getText());
                sizesStatement.setString(3, txtLength.getText());
                sizesStatement.setString(4, txtShoulder.getText());
                sizesStatement.setString(5, txtSleeve.getText());
                sizesStatement.setString(6, txtHem.getText());
                sizesStatement.setString(7, txtSize.getText());


                setEditable(false);
                currentlyEditing = false;
                btnNew.setText("New");
                updateFieldsToLast();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    private void setEditable(boolean editable) {
        txtProductID.setEditable(editable);
        txtCategory.setEditable(editable);
        txtMaterial.setEditable(editable);
        txtLength.setEditable(editable);
        txtQuantity.setEditable(editable);
        txtShoulder.setEditable(editable);
        txtHem.setEditable(editable);
        txtColour.setEditable(editable);
        txtSleeve.setEditable(editable);
        txtPrice.setEditable(editable);
        txtSize.setEditable(editable);
    }

    protected void updateTextInFields(){
        try{
            txtProductID.setText(product_details_rs.getString("product_id"));
            txtCategory.setText(product_details_rs.getString("category"));
            txtColour.setText(product_details_rs.getString("colour_name"));
            txtMaterial.setText(product_details_rs.getString("material"));
            txtQuantity.setText(product_details_rs.getString("quantity"));
            txtPrice.setText(product_details_rs.getString("price"));
            txtSize.setText(product_details_rs.getString("size"));
            txtLength.setText(product_details_rs.getString("length"));
            txtShoulder.setText(product_details_rs.getString("shoulder"));
            txtSleeve.setText(product_details_rs.getString("sleeve"));
            txtHem.setText(product_details_rs.getString("hem"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void updateFieldsToLast() throws SQLException {
        while(product_details_rs.next()) {
            if(product_details_rs.getString("product_id").equals(finalProductID)) {
                updateTextInFields();
                break;
            }
        }
    }

    public void backToProducts(ActionEvent event) {
        Main.getInstance().showProductsScene();
    }
}

