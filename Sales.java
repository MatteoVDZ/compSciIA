package com.example.demo4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.sql.*;


public class Sales {

    static final String DB_URL = "jdbc:mysql://localhost:3306/clothing";
    static String driverName = "com.mysql.cj.jdbc.Driver";
    static final String USER = "root";
    static final String PASS = "WilleM8477";
    static final String QUERY = """
            SELECT*
            FROM sales s
            JOIN
            sale_items si ON s.sale_id = si.sale_item_id;""";

    static Connection conn = null;

    static ResultSet sales_rs;
    static boolean currentlyEditing = false;
    static boolean currentlyInserting = false;


    @FXML
    TextField txtSalesID;
    @FXML
    TextField txtCustomerID;
    @FXML
    TextField txtQuantity;
    @FXML
    TextField txtPrice;
    @FXML
    TextField txtDate;


    @FXML
    Button btnEdit;
    @FXML
    Button btnInsert;
    @FXML
    Button btnDelete;

    @FXML
    Button btnNext;
    @FXML
    Button btnPrevious;

    @FXML
    Button btnProducts;
    @FXML
    Button btnCustomers;

    @FXML CheckBox filter2023;
    @FXML CheckBox filter2022;
    @FXML CheckBox filter1item;
    @FXML CheckBox filter5items;
    @FXML CheckBox noSalesFilter;
    @FXML CheckBox filterPrice100;
    @FXML CheckBox filterPrice200;

    @FXML Button btnReport;

    @FXML
    public void initialize() {

        // Open a connection
        try {
            Connection conn = null;
            Class.forName(driverName).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            sales_rs = stmt.executeQuery(QUERY);
            sales_rs.next();
            updateTextInFields();
            setEditable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setEditable(boolean editable) {
        txtSalesID.setEditable(editable);
        txtCustomerID.setEditable(editable);
        txtQuantity.setEditable(editable);
        txtPrice.setEditable(editable);
        txtDate.setEditable(editable);
    }

    @FXML
    protected void next(ActionEvent event) {

        try {
            if (sales_rs.isLast()) {
                sales_rs.first();
            } else {
                sales_rs.next();
            }
            updateTextInFields();
        } catch (Exception e) {
            e.printStackTrace();
            //next(event);
        }
    }

    @FXML
    protected void previous(ActionEvent event) {

        try {
            if (sales_rs.isFirst()) {
                sales_rs.last();
            } else {
                sales_rs.previous();
            }
            updateTextInFields();
        } catch (Exception e) {
            e.printStackTrace();
            //previous(event);
        }
    }

    protected void updateTextInFields() {
        try {
            txtCustomerID.setText(sales_rs.getString("customer_id"));
            txtSalesID.setText(sales_rs.getString("sale_id"));
            txtQuantity.setText(sales_rs.getString("quantity"));
            txtPrice.setText(sales_rs.getString("current_price"));
            txtDate.setText(sales_rs.getString("sale_date"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrentID() throws SQLException {
        return sales_rs.getString("sale_id");
    }

    public Integer getFinalID() throws SQLException {
        sales_rs.last();
        return Integer.parseInt(sales_rs.getString("sale_id")) + 1;
    }

    @FXML
    protected void update() {
        if (!currentlyEditing) {
            setEditable(true);
            currentlyEditing = true;
            btnEdit.setText("Save");
        } else {
            try {
                sales_rs.updateString("quantity", txtQuantity.getText());
                sales_rs.updateString("current_price", txtPrice.getText());
                sales_rs.updateString("sale_date", txtDate.getText());
                sales_rs.updateRow();

                setEditable(false);
                currentlyEditing = false;
                btnEdit.setText("Edit");
                updateTextInFields();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void insert(ActionEvent actionEvent) {
        Main.getInstance().showSalesDetails(true);
    }

    @FXML
    public void delete(ActionEvent actionEvent) {
        try {
            String delete_sql = "DELETE FROM sales  WHERE sale_id=" + txtSalesID.getText();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate(delete_sql);

            String delete_sizes_sql = "DELETE FROM sizes WHERE sale_item_id =" + txtSalesID.getText();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate(delete_sizes_sql);

            sales_rs.deleteRow();
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


    public void switchToProducts(ActionEvent event) {
        Main.getInstance().showProductsScene();
    }

    public void switchToCustomers(ActionEvent event) {
        Main.getInstance().showCustomersScene();
    }

    String newQuery = """
            SELECT*
            FROM sales s
            JOIN
            sale_items si ON s.sale_id = si.sale_item_id;""";

    public void filter(ActionEvent event) {

        if(noSalesFilter.isSelected()) {
            newQuery = """
            SELECT *
            FROM sales s
            JOIN
            sale_items si ON s.sale_id = si.sale_item_id;""";

            filter1item.setSelected(false);
            filter5items.setSelected(false);
            filterPrice100.setSelected(false);
            filterPrice200.setSelected(false);
            filter2022.setSelected(false);
            filter2023.setSelected(false);
        } if(filter1item.isSelected()) {
            newQuery = """
            SELECT *
            FROM sale_items si
            JOIN
            sales s ON si.sale_item_id = s.sale_id
            WHERE quantity = 1;""";
            noSalesFilter.setSelected(false);
            filter5items.setSelected(false);
            filterPrice100.setSelected(false);
            filterPrice200.setSelected(false);
            filter2022.setSelected(false);
            filter2023.setSelected(false);
        } if(filter5items.isSelected()){
            newQuery = """
            SELECT *
            FROM sale_items si
            JOIN
            sales s ON si.sale_item_id = s.sale_id
            WHERE quantity > 5;""";
            noSalesFilter.setSelected(false);
            filter1item.setSelected(false);
            filterPrice100.setSelected(false);
            filterPrice200.setSelected(false);
            filter2022.setSelected(false);
            filter2023.setSelected(false);
        } if(filter2022.isSelected()){
            newQuery = """
            SELECT *
            FROM sales s
            JOIN
            sale_items si ON s.sale_id = si.sale_item_id
            WHERE sale_date LIKE '%2022%';""";
            noSalesFilter.setSelected(false);
            filter1item.setSelected(false);
            filterPrice100.setSelected(false);
            filterPrice200.setSelected(false);
            filter5items.setSelected(false);
            filter2023.setSelected(false);
        } if(filter2023.isSelected()){
            newQuery = """
            SELECT *
            FROM sales s
            JOIN
            sale_items si ON s.sale_id = si.sale_item_id
            WHERE sale_date LIKE '%2023%';""";
            noSalesFilter.setSelected(false);
            filter1item.setSelected(false);
            filterPrice100.setSelected(false);
            filterPrice200.setSelected(false);
            filter5items.setSelected(false);
            filter2022.setSelected(false);
        } if(filterPrice100.isSelected()) {
            newQuery = """
            SELECT *
            FROM sale_items si
            JOIN
            sales s ON si.sale_item_id = s.sale_id
            WHERE current_price > 100;""";
            noSalesFilter.setSelected(false);
            filter1item.setSelected(false);
            filter2023.setSelected(false);
            filterPrice200.setSelected(false);
            filter5items.setSelected(false);
            filter2022.setSelected(false);
        } if(filterPrice200.isSelected()) {
            newQuery = """
            SELECT *
            FROM sale_items si
            JOIN
            sales s ON si.sale_item_id = s.sale_id
            WHERE current_price > 200;""";
            noSalesFilter.setSelected(false);
            filter1item.setSelected(false);
            filter2023.setSelected(false);
            filterPrice100.setSelected(false);
            filter5items.setSelected(false);
            filter2022.setSelected(false);
        }

        try {
            Connection conn = null;
            Class.forName(driverName).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            sales_rs = stmt.executeQuery(newQuery);
            sales_rs.next();
            updateTextInFields();
            setEditable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void seeMore(ActionEvent event) {
        Main.getInstance().showSalesDetails(false);
    }

    public void report(ActionEvent event) { Main.getInstance().showReportScene();}

    public void search(ActionEvent event) throws SQLException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Sale Search");
        dialog.setHeaderText("Enter Sale ID");
        dialog.setContentText("Sale ID:");

        // Traditional way to get the response value.
        java.util.Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String saleId = result.get();
            while(sales_rs.next()) {
                if(sales_rs.getString("sale_id").equals(saleId)) {
                    updateTextInFields();
                    break;
                }
            }
        }
    }
}
