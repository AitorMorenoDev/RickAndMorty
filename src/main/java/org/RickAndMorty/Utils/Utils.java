/*
This class contains methods that will be used to manage the database, like adding a character, searching by text, etc.
 */

package org.RickAndMorty.Utils;

import org.RickAndMorty.Data.Character;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Utils {

    // Declaration of the variables to connect to the database
    private static final String URL = "jdbc:postgresql://localhost:5432/serie";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgre";
    static Scanner sc = new Scanner(System.in);


    // METHODS TO ADD CHARACTERS TO THE DATABASE
    public static void addCharStatement() {
        try {
            // Load the driver and connect to the database
            Class.forName("org.postgresql.Driver");

            // Method to add a character to the database using Statement
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement statement = connection.createStatement()) {


                // Get the maximum ID of the character
                int newId = getMaxID(statement) + 1;

                // Call the method to ask the character's info
                Character newCharacter = askCharInfo();

                // Crate and execute the SQL query by Statement
                String sqlQuery = "INSERT INTO character VALUES (" + newId + ", '" + newCharacter.getName() + "', '" +
                        newCharacter.getStatus() + "', '" + newCharacter.getSpecies() + "', '" + newCharacter.getType() + "', '" +
                        newCharacter.getGender() + "', '" + newCharacter.getIdOriginAux() + "', '" + newCharacter.getIdLocationAux() + "');";

                statement.executeUpdate(sqlQuery);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addCharPreparedStatement() {
        try {
            // Load the driver and connect to the database
            Class.forName("org.postgresql.Driver");

            // Method to add a character to the database using PreparedStatement
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement statement = connection.createStatement()) {

                // Consult the maximum ID of the table and increment it by 1
                int newID = getMaxID(statement) + 1;

                // Create and execute the SQL query by PreparedStatement
                String sqlQuery = "INSERT INTO character VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

                // Call the method to ask the character's info
                Character newCharacter = askCharInfo();

                preparedStatement.setInt(1, newID);
                preparedStatement.setString(2, newCharacter.getName());
                preparedStatement.setString(3, newCharacter.getStatus());
                preparedStatement.setString(4, newCharacter.getSpecies());
                preparedStatement.setString(5, newCharacter.getType());
                preparedStatement.setString(6, newCharacter.getGender());
                preparedStatement.setInt(7, newCharacter.getIdOriginAux());
                preparedStatement.setInt(8, newCharacter.getIdLocationAux());

                preparedStatement.executeUpdate();
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to ask the character's info to the user
    private static Character askCharInfo() {
        Character character = new Character();
        System.out.println("Enter the character's info below to add it into the database: ");

        character.setName(getStringInput("Name: "));

        System.out.println("Status: \n 1. Alive \n 2. Dead \n 3. Unknown");
        character.setStatus(getStringInputWithMapping("Status: ", Map.of(1, "Alive", 2, "Dead", 3, "Unknown")));

        System.out.println("Species: \n 1. Human \n 2. Alien \n 3. Humanoid \n 4. Mythological Creature \n 5. Animal \n " +
                "6. Robot \n 7. Cronenberg \n 8. Disease \n 9. Poopybutthole \n 10. Unknown");
        character.setSpecies(getStringInputWithMapping("Species: ", Map.of(1, "Human", 2, "Alien", 3, "Humanoid",
                4, "Mythological Creature", 5, "Animal", 6, "Robot", 7, "Cronenberg",
                8, "Disease", 9, "Poopybutthole", 10, "Unknown")));

        sc.nextLine();

        character.setType(getStringInput("Type: "));

        System.out.println("Gender: \n 1. Male \n 2. Female \n 3. Genderless \n 4. Unknown");
        character.setGender(getStringInputWithMapping("Gender: ", Map.of(1, "Male", 2, "Female",
                3, "Genderless", 4, "Unknown")));

        int idOrigin;
        do {
            idOrigin = getIntInput("Origin: ");
            if (locationExists(idOrigin)) {
                System.out.println("Error: The specified Origin ID does not exist in the Location table. Please, enter a valid ID.");
            } else {
                character.setIdOriginAux(idOrigin);
            }
        } while (locationExists(idOrigin));

        int idLocation;
        do {
            idLocation = getIntInput("Location: ");
            if (locationExists(idLocation)) {
                System.out.println("Error: The specified Location ID does not exist in the Location table. Please, enter a valid ID.");
            } else {
                character.setIdLocationAux(idLocation);
            }
        } while (locationExists(idLocation));

        sc.nextLine();

        return character;
    }

    // Auxiliary methods to ask the character's info to the user
    private static String getStringInput(String text) {
        String userInput;

        do {
            System.out.print(text);
            userInput = sc.nextLine().trim();

            if (userInput.isEmpty()) {
                System.out.println("This field cannot be empty. Please enter a valid string.");
            }

        } while (userInput.isEmpty());

        return userInput;
    }

    private static String getStringInputWithMapping(String text, Map<Integer, String> options) {
        while (true) {
            int userInput = getIntInput(text);

            if (options.containsKey(userInput)) {
                return options.get(userInput);
            } else {
                System.out.println("Invalid option. Please enter a valid option.");
            }
        }
    }

    private static int getIntInput(String text) {
        System.out.print(text);
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input. Please, enter a valid integer.");
            sc.next();
            System.out.print(text);
        }
        return sc.nextInt();
    }

    // Method to obtain the maximum ID of the table
    private static int getMaxID(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT MAX(id) FROM character");
        return resultSet.next() ? resultSet.getInt(1) : 0;
    }

    // Boolean method to check if the id (origin and location) exists in the database
    private static boolean locationExists(int id) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM Location WHERE id = ?")) {
                preparedStatement.setInt(1, id);
                ResultSet rs = preparedStatement.executeQuery();
                rs.next();
                int count = rs.getInt(1);
                return count <= 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }


    // METHODS TO MANAGE THE CALLABLE STATEMENTS OF THE DATABASE
    // Method to search characters by text using a CallableStatement
    public static String SearchByText(String text) throws SQLException {
        List<Character> charactersFound = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            try (CallableStatement cs = connection.prepareCall("{call search_characters(?) }")) {
                cs.setString(1, text);

                // Execute the callable statement
                cs.execute();

                // Obtain the result set
                ResultSet rs = cs.executeQuery();

                // Process the results and add them to the list
                while (rs.next()) {
                    Character character = new Character();
                    character.setId(rs.getInt("id"));
                    character.setName(rs.getString("name"));
                    character.setStatus(rs.getString("status"));
                    character.setSpecies(rs.getString("species"));
                    character.setType(rs.getString("type"));
                    character.setGender(rs.getString("gender"));
                    character.setIdOriginAux(rs.getInt("id_origin"));
                    character.setIdLocationAux(rs.getInt("id_location"));

                    charactersFound.add(character);
                }

                // Close the connection
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return charactersFound.stream().map(Character::toString).collect(Collectors.joining("\n"));
    }

    // Method to obtain the characters without episode using a CallableStatement
    public static String CharactersWithoutEpisode() {
        List<Character> charactersWithoutEpisode = new ArrayList<>();

        try {
            // Load the driver and connect to the database
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Call the stored procedure using CallableStatement
            try (CallableStatement cs = connection.prepareCall("{call characters_without_episode()}")) {
                // Execute the callable statement
                ResultSet rs = cs.executeQuery();

                // Process the results and add them to the list
                while (rs.next()) {
                    Character character = new Character();
                    character.setId(rs.getInt("id"));
                    character.setName(rs.getString("name"));
                    character.setStatus(rs.getString("status"));
                    character.setSpecies(rs.getString("species"));
                    character.setType(rs.getString("type"));
                    character.setGender(rs.getString("gender"));
                    character.setIdOriginAux(rs.getInt("id_origin"));
                    character.setIdLocationAux(rs.getInt("id_location"));

                    charactersWithoutEpisode.add(character);
                }
            }

            // Close the connection
            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        return charactersWithoutEpisode.stream().map(Character::toString).collect(Collectors.joining("\n"));
    }

}
