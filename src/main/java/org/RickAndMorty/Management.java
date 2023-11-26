package org.RickAndMorty;

import java.io.IOException;
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

    public static void ExecuteMenu() throws IOException {
        int option;
        do {
            ShowMenu();
            option = sc.nextInt();
            System.out.println();
            switch (option) {
                case 1 -> Database.clearTables(), Database.fillTables();
                case 2 -> AddChar();
                case 3 -> Search();
                case 4 -> CharsWithoutEpisode();
                case 5 -> Exit();
                default -> System.out.println("Invalid option \n");
            }
        } while (option != 5);
    }

    private static void AddChar() {

    }

    private static void Search() {

    }

    private static void CharsWithoutEpisode() {

    }

    private static void Exit() {
        System.out.println("Thank you for using the Rick And Morty API. Goodbye!!");
    }
}
