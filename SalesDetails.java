package com.example.demo4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.*;

public class SalesDetails {
    Sales s = new Sales();
    String currentSalesID = s.getCurrentID();
    Integer finalSaleID = s.getFinalID();

    static ResultSet sales_details_rs;
    static final String DB_URL = "jdbc:mysql://localhost:3306/clothing";
    static String driverName = "com.mysql.cj.jdbc.Driver";
    static final String USER = "root";
    static final String PASS = "WilleM8477";
    String QUERY = """
            SELECT*
            FROM sales s
            JOIN
            sale_items si ON s.sale_id = si.sale_item_id
            JOIN
            customers c ON s.customer_id = c.customer_id""";

    static Connection conn = null;

    @FXML TextField txtCustomerName;
    @FXML TextField txtCustomerID;
    @FXML TextField txtSaleID;
    @FXML TextField txtPrice;
    @FXML TextField txtQuantity;
    @FXML TextArea txtDestination;
    @FXML TextField txtDateOfSale;
    @FXML TextField txtTimeOfSale;
    @FXML Button btnNew;

    @FXML Button backToSales;
    static boolean currentlyEditing = false;
    static boolean currentlyInserting = false;

    public SalesDetails() throws SQLException {
    }

    @FXML
    public void initialize() throws SQLException {
        backToSales.setOnAction(this::backToSales);

        try {Connection conn = null;
            Class.forName(driverName).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            sales_details_rs = stmt.executeQuery(QUERY);
            sales_details_rs.next();
            updateTextInFields();
            setEditable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(sales_details_rs.next()) {
            if(sales_details_rs.getString("sale_id").equals(currentSalesID)) {
                updateTextInFields();
                break;
            }
        }
    }

    @FXML
    protected void next(ActionEvent event){

        try{
            if (sales_details_rs.isLast()) {
                sales_details_rs.first();
            } else {
                sales_details_rs.next();
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
            if (sales_details_rs.isFirst()) {
                sales_details_rs.last();
            } else {
                sales_details_rs.previous();
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
            txtSaleID.setEditable(false);
            txtSaleID.setText(String.valueOf(finalSaleID));
            txtCustomerID.setEditable(false);
            txtCustomerID.clear();
            txtCustomerID.setDisable(true);
            txtCustomerName.clear();
            txtCustomerName.setDisable(true);
            txtPrice.clear();
            txtQuantity.clear();
            txtDestination.clear();
            txtDestination.setDisable(true);
            txtDateOfSale.clear();
            txtTimeOfSale.clear();
            currentlyInserting = true;
            btnNew.setText("Save");
        } else {
            try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String insertDate = txtDateOfSale.getText() + " " + txtTimeOfSale.getText();

                String insertSalesSQL = "INSERT INTO sales (sale_id, sale_date) VALUES (?, ?)";
                PreparedStatement salesStatement = connection.prepareStatement(insertSalesSQL);

                salesStatement.setString(1, String.valueOf(finalSaleID));
                salesStatement.setString(2, insertDate);


                String insertSaleItemsSQL = "INSERT INTO sale_items (sale_item_id, quantity, current_price) VALUES (?, ?, ?)";
                PreparedStatement saleItemsStatement = connection.prepareStatement(insertSaleItemsSQL);

                saleItemsStatement.setString(1, String.valueOf(finalSaleID));
                saleItemsStatement.setString(2, txtQuantity.getText());
                saleItemsStatement.setString(3, txtPrice.getText());


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
        txtSaleID.setEditable(editable);
        txtCustomerName.setEditable(editable);
        txtPrice.setEditable(editable);
        txtDestination.setEditable(editable);
        txtQuantity.setEditable(editable);
        txtDateOfSale.setEditable(editable);
        txtTimeOfSale.setEditable(editable);
    }

    protected void updateTextInFields(){
        try{
            txtCustomerName.setText(sales_details_rs.getString("first_name") + " " + sales_details_rs.getString("last_name"));
            txtCustomerID.setText(sales_details_rs.getString("customer_id"));
            txtSaleID.setText(sales_details_rs.getString("sale_id"));
            txtPrice.setText(sales_details_rs.getString("current_price"));
            txtDestination.setText(sales_details_rs.getString("address") + ", " + sales_details_rs.getString("postal_code") + ", " + sales_details_rs.getString("city")+ ", " + sales_details_rs.getString("state") + ", " + sales_details_rs.getString("country"));
            txtQuantity.setText(sales_details_rs.getString("quantity"));

            String[] splittingDate = sales_details_rs.getString("sale_date").split(" ");
            txtDateOfSale.setText(splittingDate[0]);
            txtTimeOfSale.setText(splittingDate[1]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void updateFieldsToLast() throws SQLException {
        while(sales_details_rs.next()) {
            if(sales_details_rs.getString("sale_id").equals(finalSaleID)) {
                updateTextInFields();
                break;
            }
        }
    }

    public void backToSales(ActionEvent event) {
        Main.getInstance().showSalesScene();
    }
}
