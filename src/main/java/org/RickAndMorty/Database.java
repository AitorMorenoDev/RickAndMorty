package org.RickAndMorty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String URL = "jdbc:postgresql://localhost:5432/serie";
    private static final String USER = "postgre";
    private static final String PASSWORD = "postgre";

    public static void loadData() {
        try {
            // Establecer la conexión a la base de datos
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Deshabilitar el autocommit para transacciones
            connection.setAutoCommit(false);

            // Lógica para vaciar las tablas (hazlo en el orden correcto para evitar conflictos)
            clearTables(connection);

            // Lógica para cargar datos desde la API y insertar en las tablas
            // (implementa la lógica para obtener datos de la API y realizar las inserciones)

            // Confirmar transacción
            connection.commit();

            // Cerrar la conexión
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar la excepción o lanzarla
        }
    }

    private static void clearTables(Connection connection) throws SQLException {
        // Implementar la lógica para vaciar las tablas en el orden correcto
        // Utiliza DELETE FROM nombre_tabla para vaciar una tabla
        // (asegúrate de seguir el orden correcto para evitar conflictos de clave externa)
    }

}