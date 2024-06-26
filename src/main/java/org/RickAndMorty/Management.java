/*
    Class that contains the methods to execute the menu of the program
 */

package org.RickAndMorty;

import org.RickAndMorty.Utils.Database;
import org.RickAndMorty.Utils.Utils;

import java.sql.SQLException;
import java.util.Scanner;

public class Management {
    static Scanner sc = new Scanner(System.in);
    private static void ShowMenu() {
        System.out.println("Choose an option:");
        System.out.println("1. Reset database");
        System.out.println("2. Add character");
        System.out.println("3. Search by text");
        System.out.println("4. Characters without episode");
        System.out.println("5. Exit");
        System.out.println();
    }

    public static void ExecuteMenu() {
        int option;
        do {
            ShowMenu();
            option = sc.nextInt();
            System.out.println();
            switch (option) {
                case 1 -> Database.fillTables();
                case 2 -> AddChar();
                case 3 -> Search();
                case 4 -> CharsWithoutEpisode();
                case 5 -> Exit();
                default -> System.out.println("Invalid option \n");
            }
        } while (option != 5);
    }

    private static void AddChar() {
        int option;
        do {
            System.out.println("Enter the option of the way you want to add the character: ");
            System.out.println("1. Add by 'Statement'");
            System.out.println("2. Add by 'PreparedStatement'");
            System.out.println();
            option = sc.nextInt();
            switch (option) {
                case 1 -> Utils.addCharStatement();
                case 2 -> Utils.addCharPreparedStatement();
                default -> System.out.println("Invalid option \n");
            }
        } while (option != 1 && option != 2);
    }

    private static void Search() {

        // I Had to declare another scanner because the first one was giving me problems
        Scanner sc2 = new Scanner(System.in);
        System.out.println("Enter the text you want to search: ");
        String text = sc2.nextLine();

        try {
            Utils.SearchByText(text);
            if (Utils.SearchByText(text).isEmpty()) {
                System.out.println("No results found");
                System.out.println();
            } else {
                System.out.println(Utils.SearchByText(text));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void CharsWithoutEpisode() {
        Utils.CharactersWithoutEpisode();
        if (Utils.CharactersWithoutEpisode().isEmpty()) {
            System.out.println("No results found");
            System.out.println();
        } else {
            System.out.println("The next characters don't appear in any episode: ");
            System.out.println(Utils.CharactersWithoutEpisode());
            System.out.println();
        }
    }

    private static void Exit() {
        System.out.println("Thank you for using the Rick And Morty API. Goodbye!!");
    }
}
