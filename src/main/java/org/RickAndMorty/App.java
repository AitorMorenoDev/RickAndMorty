package org.RickAndMorty;

import java.io.IOException;

public class App 
{
    public static void main( String[] args ) throws IOException {
        Database.clearTables();
        Database.fillTables();
        System.out.println("Welcome to the Rick & Morty API!");
        System.out.println();
        Management.ExecuteMenu();
    }
}
