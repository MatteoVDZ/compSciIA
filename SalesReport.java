package com.example.demo4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.*;

public class SalesReport {
    static final String DB_URL = "jdbc:mysql://localhost:3306/clothing";
    static String driverName = "com.mysql.cj.jdbc.Driver";
    static final String USER = "root";
    static final String PASS = "WilleM8477";
    public String QUERY = "";


    static Connection conn = null;

    @FXML
    private String selectedMonth = "";
    private String monthDigits = "";

    static ResultSet report_rs;
    @FXML Button backToCustomers;

    @FXML TextField txtTotalSales;
    @FXML TextField txtTotalUnits;
    @FXML TextField txtTotalPrice;
    @FXML TextField txtAveragePrice;
    @FXML TextField txtAverageQuantity;

    @FXML
    TextArea areaCountries;

    @FXML ChoiceBox monthChoiceBox;

    @FXML PieChart pieChart;
    int JacketNum;
    int SweaterNum;
    int SkirtNum;
    int JeansNum;
    int tShirtNum;

    PieChart.Data dataA;
    PieChart.Data dataB;
    PieChart.Data dataC;
    PieChart.Data dataD;
    PieChart.Data dataE;

    public void initialize() throws SQLException {


        backToCustomers.setOnAction(this::backToCustomers);

        monthChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedMonth = newValue.toString();
            switch (selectedMonth) {
                case "January" -> monthDigits = "01";
                case "February" -> monthDigits = "02";
                case "March" -> monthDigits = "03";
                case "April" -> monthDigits = "04";
                case "May" -> monthDigits = "05";
                case "June" -> monthDigits = "06";
                case "July" -> monthDigits = "07";
                case "August" -> monthDigits = "08";
                case "September" -> monthDigits = "09";
                case "October" -> monthDigits = "10";
                case "November" -> monthDigits = "11";
                case "December" -> monthDigits = "12"; }

            QUERY = "SELECT * " +
                    "FROM sales s " +
                    "JOIN sale_items si ON s.sale_id = si.sale_item_id " +
                    "JOIN customers c ON s.customer_id = c.customer_id " +
                    "JOIN products p ON s.sale_id = p.product_id " +
                    "WHERE sale_date LIKE '_____" + monthDigits + "%'";

            try {
                Connection conn = null;
                Class.forName(driverName).newInstance();
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                report_rs = stmt.executeQuery(QUERY);
                report_rs.next();
                updatePieChart();
                updateTextInFields();
                setEditable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }



    private void updatePieChart() throws SQLException {
        report_rs.first();
        while(report_rs.next()) {
            if(report_rs.getString("category").equals("Jacket")) {
                JacketNum += 1;
            } else if(report_rs.getString("category").equals("Sweater")) {
                SweaterNum += 1;
            } else if(report_rs.getString("category").equals("Skirt")) {
                SkirtNum += 1;
            } else if(report_rs.getString("category").equals("T-Shirt")) {
                tShirtNum += 1;
            } else if(report_rs.getString("category").equals("Jeans")) {
                JeansNum += 1;
            }
        }

        dataA = new PieChart.Data("Jackets", JacketNum);
        dataB = new PieChart.Data("Sweaters", SweaterNum);
        dataC = new PieChart.Data("T-Shirts", tShirtNum);
        dataD = new PieChart.Data("Skirt", SkirtNum);
        dataE = new PieChart.Data("Jeans", JeansNum);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(dataA, dataB, dataC, dataD, dataE);
        pieChart.setData(pieChartData);

        JacketNum = 0;
        SweaterNum = 0;
        SkirtNum = 0;
        tShirtNum = 0;
        JeansNum = 0;
    }

    private void setEditable(boolean editable) {
        txtTotalSales.setEditable(editable);
        txtTotalUnits.setEditable(editable);
        txtTotalPrice.setEditable(editable);
        txtAveragePrice.setEditable(editable);
        txtAverageQuantity.setEditable(editable);
        areaCountries.setEditable(editable);
    }

    protected void updateTextInFields(){
        try{

            report_rs.last();
            int rowCount = report_rs.getRow();

            report_rs.first();
            int totalQuantity = report_rs.getInt("quantity");
            while(report_rs.next()) {
                totalQuantity += report_rs.getInt("quantity");
            }

            report_rs.first();
            int totalPrice = report_rs.getInt("current_price");
            int averagePrice = 0;
            while(report_rs.next()) {
                totalPrice += report_rs.getInt("current_price");
            }
            averagePrice = totalPrice / rowCount;

            int averageQuantity = totalQuantity / rowCount;

            String countriesString = "";
            report_rs.first();
            countriesString = report_rs.getString("country");
            report_rs.next();
            while(report_rs.next()) {
                countriesString = countriesString + ", " + report_rs.getString("country");
            }

            txtTotalSales.setText(String.valueOf(rowCount) + " sales made in " + selectedMonth);
            txtTotalUnits.setText(String.valueOf(totalQuantity) + " units");
            txtTotalPrice.setText("$" + String.valueOf(totalPrice));
            txtAveragePrice.setText("$" + String.valueOf(averagePrice));
            txtAverageQuantity.setText(String.valueOf(averageQuantity) + " units");
            areaCountries.setText(countriesString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backToCustomers(ActionEvent event) {
        Main.getInstance().showCustomersScene();
    }
}
