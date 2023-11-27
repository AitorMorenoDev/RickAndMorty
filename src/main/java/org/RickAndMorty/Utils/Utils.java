/*
This class will contain methods that will be used to manage the database, like adding a character, searching by text, etc.
 */

package org.RickAndMorty.Utils;

import org.RickAndMorty.Data.Character;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {

    // Declaration of the variables to connect to the database
    private static final String URL = "jdbc:postgresql://localhost:5432/serie";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgre";
    static Scanner sc = new Scanner(System.in);

    public static void addCharStatement() {
        try {
            // Load the driver and connect to the database
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Create a Statement object to execute SQL queries
            Statement statement = connection.createStatement();

            /* ---------------------------------------------

            Esto en caso de no poder poner autoincrementable el ID en la base de datos

            // Consultar el valor actual del ID más alto
            ResultSet resultSet = statement.executeQuery("SELECT MAX(id) FROM character");
            int currentMaxId = 0;
            if (resultSet.next()) {
                currentMaxId = resultSet.getInt(1);
            }

            // Incrementar el valor del ID
            int newId = currentMaxId + 1;

            --------------------------------------------- */

            // Call the method to ask the character's info
            Character newCharacter = askCharInfo();

            // Crate and execute the SQL query by Statement
            String sqlQuery = "INSERT INTO character VALUES (" + /*newId*/ "DEFAULT" + ", '" + newCharacter.getName() + "', '" +
                    newCharacter.getStatus() + "', '" + newCharacter.getSpecies() + "', '" + newCharacter.getType() + "', '" +
                    newCharacter.getGender() + "', '" + newCharacter.getIdOriginAux()+ "', '" + newCharacter.getIdLocationAux() + "');";

            statement.executeUpdate(sqlQuery);

            // Close the connection
            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addCharPreparedStatement() {
        try {
            // Load the driver and connect to the database
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Call the method to ask the character's info
            Character newCharacter = askCharInfo();

            // Create and execute the SQL query by PreparedStatement
            String sqlQuery = "INSERT INTO character VALUES (" + 835 + ", ?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, newCharacter.getName());
            preparedStatement.setString(2, newCharacter.getStatus());
            preparedStatement.setString(3, newCharacter.getSpecies());
            preparedStatement.setString(4, newCharacter.getType());
            preparedStatement.setString(5, newCharacter.getGender());
            preparedStatement.setInt(6, newCharacter.getIdOriginAux());
            preparedStatement.setInt(7, newCharacter.getIdLocationAux());

            preparedStatement.executeUpdate();

            // Close the connection
            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Character askCharInfo() {

        Character character = new Character();

        System.out.println("Enter the character's info below to add it into the database: ");

        System.out.print("Name: ");
        character.setName(sc.nextLine());

        System.out.print("Status: \n 1. Alive \n 2. Dead \n 3. Unknown \n");
        int statusOpt = sc.nextInt();
        switch (statusOpt) {
            case 1 -> character.setStatus("Alive");
            case 2 -> character.setStatus("Dead");
            case 3 -> character.setStatus("Unknown");
            default -> System.out.println("Invalid option \n");
        }

        System.out.print("Species: \n 1. Human \n 2. Alien \n 3. Humanoid \n 4. Mythological Creature \n 5. Animal \n " +
                "6. Robot \n 7. Cronenberg \n 8. Disease \n 9. Poopybutthole \n 10. Unknown \n");
        int speciesOpt = sc.nextInt();
        switch (speciesOpt) {
            case 1 -> character.setSpecies("Human");
            case 2 -> character.setSpecies("Alien");
            case 3 -> character.setSpecies("Humanoid");
            case 4 -> character.setSpecies("Mythological Creature");
            case 5 -> character.setSpecies("Animal");
            case 6 -> character.setSpecies("Robot");
            case 7 -> character.setSpecies("Cronenberg");
            case 8 -> character.setSpecies("Disease");
            case 9 -> character.setSpecies("Poopybutthole");
            case 10 -> character.setSpecies("Unknown");
            default -> System.out.println("Invalid option \n");
        }

        // Duda aquí (Muchos tipos de especies, locura preguntar todos)
        System.out.print("Type: ");
        character.setType(sc.nextLine());

        System.out.println("Gender: \n 1. Male \n 2. Female \n 3. Genderless \n 4. Unknown \n");
        int genderOpt = sc.nextInt();
        switch (genderOpt) {
            case 1 -> character.setGender("Male");
            case 2 -> character.setGender("Female");
            case 3 -> character.setGender("Genderless");
            case 4 -> character.setGender("Unknown");
            default -> System.out.println("Invalid option \n");
        }

        // Duda aquí, ¿preguntar al usuario por el ID de la localización y el origen?
        System.out.println("Origin: ");
        character.setIdOriginAux(sc.nextInt());
        sc.nextLine();
        System.out.println("Location: ");
        character.setIdLocationAux(sc.nextInt());

        return character;
    }

    public static List<Character> SearchByText(String text) throws SQLException {
        List<Character> charactersFound = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            try (CallableStatement cs = connection.prepareCall("{ ? = call search_characters(?) }")) {
                cs.registerOutParameter(1, Types.OTHER);
                cs.setString(2, text);

                // Execute the callable statement
                cs.execute();

                // Obtain the result set
                ResultSet resultSet = (ResultSet) cs.getObject(1);

                // Process the results and add them to the list
                while (resultSet.next()) {
                    Character character = new Character();
                    character.setId(resultSet.getInt("id"));
                    character.setName(resultSet.getString("name"));
                    character.setStatus(resultSet.getString("status"));
                    character.setSpecies(resultSet.getString("species"));
                    character.setType(resultSet.getString("type"));
                    character.setGender(resultSet.getString("gender"));
                    character.setIdOriginAux(resultSet.getInt("id_origin"));
                    character.setIdLocationAux(resultSet.getInt("id_location"));

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

        return charactersFound;
    }
}
