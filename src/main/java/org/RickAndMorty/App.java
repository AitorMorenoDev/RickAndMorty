package org.RickAndMorty;

import org.RickAndMorty.Utils.Database;

public class App
{
    public static void main( String[] args ) {
        System.out.println("Welcome to the Rick & Morty API!");
        System.out.println();
        Database.fillTables();
        Management.ExecuteMenu();
    }
}
