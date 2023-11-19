package org.RickAndMorty;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        Database.loadData();
        System.out.println("Welcome to the Rick And Morty API!");
        System.out.println();
        Management.ExecuteMenu();
    }
}
