package com.example.demo4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.*;

public class CustomerDetails {

    Customers c = new Customers();
    String currentCustomerID = c.getCurrentID();
    Integer finalCustomerID = c.getFinalID();

    static ResultSet  customer_details_rs;
    static final String DB_URL = "jdbc:mysql://localhost:3306/clothing";
    static String driverName = "com.mysql.cj.jdbc.Driver";
    static final String USER = "root";
    static final String PASS = "WilleM8477";
    String QUERY = "SELECT * FROM customers";

    static Connection conn = null;

    @FXML TextField txtCustomerName;
    @FXML TextField txtCustomerID;
    @FXML TextField txtAddress;
    @FXML TextField txtPostalCode;
    @FXML TextField txtCity;
    @FXML TextField txtCountry;
    @FXML TextField txtPhoneNumber;
    @FXML TextField txtEmailAddress;
    @FXML TextField txtPaymentMethod;
    @FXML TextField txtNumberOfPurchases;
    @FXML Button btnNew;
    @FXML Button btnNext;
    @FXML Button btnPrevious;

    @FXML Button backToCustomers;
    static boolean currentlyEditing = false;
    static boolean currentlyInserting = false;

    public CustomerDetails() throws SQLException {
    }

    @FXML
    public void initialize() throws SQLException {

        try {Connection conn = null;
            Class.forName(driverName).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            customer_details_rs = stmt.executeQuery(QUERY);
            customer_details_rs.next();
            updateTextInFields();
            setEditable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(customer_details_rs.next()) {
            if(customer_details_rs.getString("customer_id").equals(currentCustomerID)) {
                updateTextInFields();
                break;
            }
        }
    }

    @FXML
    protected void next(ActionEvent event){

        try{
            if (customer_details_rs.isLast()) {
                customer_details_rs.first();
            } else {
                customer_details_rs.next();
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
            if (customer_details_rs.isFirst()) {
                customer_details_rs.last();
            } else {
                customer_details_rs.previous();
            }
            updateTextInFields();
        } catch (Exception e) {
            e.printStackTrace();
            //previous(event);
        }
    }

    public void insert() {
        if (!currentlyInserting) {
            setEditable(true);
            txtCustomerID.setEditable(false);
            txtCustomerID.setText(String.valueOf(finalCustomerID));
            txtCustomerName.clear();
            txtAddress.clear();
            txtPostalCode.clear();
            txtCity.clear();
            txtCountry.clear();
            txtPhoneNumber.clear();
            txtEmailAddress.clear();
            txtPaymentMethod.clear();
            txtNumberOfPurchases.clear();
            currentlyInserting = true;
            btnNew.setText("Save");
        } else {
            try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String[] splittingName = txtCustomerName.getText().split(" ");

                String insertProductsSQL = "INSERT INTO customers (customer_id, first_name, last_name, address, postal_code, city, country, phone, email, payment_method, purchases) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement productsStatement = connection.prepareStatement(insertProductsSQL);

                productsStatement.setString(1, String.valueOf(finalCustomerID));
                productsStatement.setString(2, splittingName[0]);
                productsStatement.setString(3, splittingName[1]);
                productsStatement.setString(4, txtAddress.getText());
                productsStatement.setString(5, txtPostalCode.getText());
                productsStatement.setString(6, txtCity.getText());
                productsStatement.setString(7, txtCountry.getText());
                productsStatement.setString(8, txtPhoneNumber.getText());
                productsStatement.setString(9, txtEmailAddress.getText());
                productsStatement.setString(10, txtPaymentMethod.getText());
                productsStatement.setString(11, txtNumberOfPurchases.getText());


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
        txtCustomerID.setEditable(editable);
        txtCustomerName.setEditable(editable);
        txtAddress.setEditable(editable);
        txtPostalCode.setEditable(editable);
        txtCountry.setEditable(editable);
        txtPaymentMethod.setEditable(editable);
        txtEmailAddress.setEditable(editable);
        txtPhoneNumber.setEditable(editable);
        txtCity.setEditable(editable);
        txtNumberOfPurchases.setEditable(editable);
    }

    protected void updateTextInFields(){
        try{
            txtCustomerName.setText(customer_details_rs.getString("first_name") + " " + customer_details_rs.getString("last_name"));
            txtCustomerID.setText(customer_details_rs.getString("customer_id"));
            txtAddress.setText(customer_details_rs.getString("address"));
            txtPostalCode.setText(customer_details_rs.getString("postal_code"));
            txtCountry.setText(customer_details_rs.getString("country"));
            txtCity.setText(customer_details_rs.getString("city"));
            txtPaymentMethod.setText(customer_details_rs.getString("payment_method"));
            txtEmailAddress.setText(customer_details_rs.getString("email"));
            txtPhoneNumber.setText(customer_details_rs.getString("phone"));
            txtNumberOfPurchases.setText(customer_details_rs.getString("purchases"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void updateFieldsToLast() throws SQLException {
        while(customer_details_rs.next()) {
            if(customer_details_rs.getString("customer_id").equals(finalCustomerID)) {
                updateTextInFields();
                break;
            }
        }
    }

    public void backToCustomers(ActionEvent event) {
        Main.getInstance().showCustomersScene();
    }
    public void showTableView(ActionEvent event) {Main.getInstance().showTableView("customerPurchases");}
}
