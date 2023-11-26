package org.RickAndMorty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:postgresql://localhost:5432/serie";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgre";

    public static void clearTables(){

        try {

            // Load the driver
            Class.forName("org.postgresql.Driver");

            // Connection to the database
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Create a statement
            Statement statement = connection.createStatement();

            //SQL Sentences
            statement.executeUpdate("DELETE FROM character");
            statement.executeUpdate("DELETE FROM episode");
            statement.executeUpdate("DELETE FROM location");
            statement.executeUpdate("DELETE FROM character_in_episode");

            // Close connection
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadData() {

    }

    public static void fillTables() {
        try {
            // Load the driver
            Class.forName("org.postgresql.Driver");

            // Connection to the database
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // First, clear the tables
            clearTables();

            // After that, load data from the API


            // Lógica para cargar datos desde la API y insertar en las tablas
            // (implementa la lógica para obtener datos de la API y realizar las inserciones)

            // Close connection
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}