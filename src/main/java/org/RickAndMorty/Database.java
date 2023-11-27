package org.RickAndMorty;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.RickAndMorty.Data.Character;
import org.RickAndMorty.Data.Episode;
import org.RickAndMorty.Data.Location;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String URL = "jdbc:postgresql://localhost:5432/serie";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgre";

    private static void clearTables(){

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

    private static <T> List<T> fetchData(String apiUrl, int maxID, Class<T> dataType) {
        List<T> data = new ArrayList<>();
        int id = 1;

        while (id <= maxID) {
            URL url;
            try {
                url = new URI(apiUrl + id).toURL();
            } catch (URISyntaxException | MalformedURLException e) {
                throw new RuntimeException(e);
            }

            try (InputStream is = url.openStream();
                 InputStreamReader reader = new InputStreamReader(is)) {
                JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();

                Gson gson = new Gson();
                T item = gson.fromJson(obj, dataType);
                data.add(item);

                id++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return data;
    }

    private static List<Character> getCharacters() {
        return fetchData("https://rickandmortyapi.com/api/character/", 826, Character.class);
    }

    private static List<Location> getLocations() {
        return fetchData("https://rickandmortyapi.com/api/location/", 126, Location.class);
    }

    private static List<Episode> getEpisodes() {
        return fetchData("https://rickandmortyapi.com/api/episode/", 51, Episode.class);
    }

    private static void getApiData() {
        System.out.println("Loading data from the API, please, wait...");
        getCharacters();
        getLocations();
        getEpisodes();
    }

    private static String generateInsertQuery(String tableName) {
        switch (tableName) {
            case "character" -> {
                return "INSERT INTO " + tableName + " (id, name, status, species, type, gender, id_origin, id_location) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            }
            case "location" -> {
                return "INSERT INTO " + tableName + " (id, name, type, dimension) VALUES (?, ?, ?, ?)";
            }
            case "episode" -> {
                return "INSERT INTO " + tableName + " (id, name, air_date, episode) VALUES (?, ?, ?, ?)";
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + tableName);
        }
    }

    private static <T> void setPreparedStatementValues(PreparedStatement preparedStatement, T item) throws SQLException {
        if (item instanceof Character) {
            Character character = (Character) item;
            preparedStatement.setInt(1, character.getId());
            preparedStatement.setString(2, character.getName());
            preparedStatement.setString(3, character.getStatus());
            preparedStatement.setString(4, character.getSpecies());
            preparedStatement.setString(5, character.getType());
            preparedStatement.setString(6, character.getGender());
            preparedStatement.setInt(7, character.getId_origin());
            preparedStatement.setInt(8, character.getId_location());
        } else if (item instanceof Location) {
            Location location = (Location) item;
            preparedStatement.setInt(1, location.getId());
            preparedStatement.setString(2, location.getName());
            preparedStatement.setString(3, location.getType());
            preparedStatement.setString(4, location.getDimension());
        } else if (item instanceof Episode) {
            Episode episode = (Episode) item;
            preparedStatement.setInt(1, episode.getId());
            preparedStatement.setString(2, episode.getName());
            try {
                preparedStatement.setDate(3, episode.getSqlDate());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            preparedStatement.setString(4, episode.getEpisode());
        }
    }

    private static <T> void insertData(Connection connection, List<T> data, String tableName) throws SQLException {
        String insertQuery = generateInsertQuery(tableName);
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            for (T item : data) {
                setPreparedStatementValues(preparedStatement, item);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
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
            getApiData();

            // Then, insert data into the tables
            System.out.println("Inserting data into the database, please, wait...");
            String insertUnknown = "INSERT INTO location (id, name, type, dimension) VALUES (0, 'Unknown', 'Unknown', 'Unknown')";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertUnknown)) {
                preparedStatement.executeUpdate();
            }
            insertData(connection, getCharacters(), "character");
            insertData(connection, getLocations(), "location");
            insertData(connection, getEpisodes(), "episode");

            // Finally, close connection
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}