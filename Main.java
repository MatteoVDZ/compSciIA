package com.example.demo4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {
    private Stage primaryStage;
    private static Main instance;

    @Override
    public void start(Stage primaryStage) throws IOException {
        instance = this;
        this.primaryStage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        primaryStage.setResizable(false);
        primaryStage.setTitle("FXML Switching Example");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Main getInstance() {
        return instance;
    }

    public void showCustomersScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Customers.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root,1000, 700);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showProductsScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Products.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root,1000, 700);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSalesScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Sales.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root,1000, 700);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCustomerDetails(Boolean isNew) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("customerDetails.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root,1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        CustomerDetails controller = loader.getController();
        if (isNew) controller.insert();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showProductDetails(Boolean isNew) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("productDetails.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root,1000, 700);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);

            ProductDetails controller = loader.getController();
            if (isNew) controller.insert();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void showSalesDetails(Boolean isNew) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SalesDetails.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root,1000, 700);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);

            SalesDetails controller = loader.getController();
            if (isNew) controller.insert();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void showReportScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("salesReport.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root,1000, 700);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showTableView(String desiredTable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tableView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root,1000, 700);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
