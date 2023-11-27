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

    // Declaration of the variables to connect to the database
    private static final String URL = "jdbc:postgresql://localhost:5432/serie";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgre";


    //Method to clear the tables
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

            System.out.println("Tables cleared successfully");

            // Close connection
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //METHODS TO GET DATA FROM THE API
    private static <T> List<T> fetchData(String apiUrl, int maxID, Class<T> dataType) {
        // Method to fetch data from the API, in order to avoid repeating code
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

    // Different methods to get the data from the API according to the type of data
    private static List<Character> getCharacters() {
        try (InputStream inputStream = new URL("https://rickandmortyapi.com/api/character/").openStream();
             InputStreamReader reader = new InputStreamReader(inputStream)) {
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
            int maxID = obj.get("info").getAsJsonObject().get("count").getAsInt();
            return fetchData("https://rickandmortyapi.com/api/character/", maxID, Character.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Location> getLocations() {
        try (InputStream inputStream = new URL("https://rickandmortyapi.com/api/location/").openStream();
             InputStreamReader reader = new InputStreamReader(inputStream)) {
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
            int maxID = obj.get("info").getAsJsonObject().get("count").getAsInt();
            return fetchData("https://rickandmortyapi.com/api/location/", maxID, Location.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Episode> getEpisodes() {
        try (InputStream inputStream = new URL("https://rickandmortyapi.com/api/episode/").openStream();
             InputStreamReader reader = new InputStreamReader(inputStream)) {
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
            int maxID = obj.get("info").getAsJsonObject().get("count").getAsInt();
            return fetchData("https://rickandmortyapi.com/api/episode/", maxID, Episode.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getApiData() {
        // Method which calls the methods above to get the whole data from the API
        System.out.println("Loading data from the API, please, wait...");
        getCharacters();
        getLocations();
        getEpisodes();
    }


    //METHODS TO INSERT DATA INTO THE DATABASE
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

           String origin = character.getOrigin().getUrl();
            int lastSlashOrigin = origin.lastIndexOf("/");
            if (!origin.equals("")) {
                int idOrigin = Integer.parseInt(origin.substring(lastSlashOrigin + 1));
                preparedStatement.setInt(7, idOrigin);
            } else {
                preparedStatement.setInt(7, 0);
            }

            String location = character.getLocation().getUrl();
            int lastSlashLocation = location.lastIndexOf("/");
            if (!location.equals("")) {
                int idLocation = Integer.parseInt(location.substring(lastSlashLocation + 1));
                preparedStatement.setInt(8, idLocation);
            } else {
                preparedStatement.setInt(8, 0);
            }

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

    private static <T> void insertCharacterInEpisode(Connection connection, List<T> data) {
        String insertQuery = "INSERT INTO character_in_episode (id_episode, id_character) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            for (T item : data) {
                if (item instanceof Episode) {
                    Episode episode = (Episode) item;
                    int episodeId = episode.getId();

                    for (String characterUrl : episode.getCharacters()) {
                        int lastSlash = characterUrl.lastIndexOf("/");
                        int characterId = Integer.parseInt(characterUrl.substring(lastSlash + 1));

                        preparedStatement.setInt(1, episodeId);
                        preparedStatement.setInt(2, characterId);

                        preparedStatement.addBatch();
                    }
                }
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //Method to fill the tables
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

            // Then, insert data into the tables --------------------
            System.out.println("Inserting data into the database, please, wait...");
                // Insert unknown location with id value 0
                String insertUnknown = "INSERT INTO location (id, name, type, dimension) VALUES (0, 'Unknown', 'Unknown', 'Unknown')";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertUnknown)) {
                preparedStatement.executeUpdate();
            }
            insertData(connection, getLocations(), "location");
            insertData(connection, getEpisodes(), "episode");
            insertData(connection, getCharacters(), "character");
            insertCharacterInEpisode(connection, getEpisodes());
            // ------------------------------------------------------

            // Finally, close connection
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}