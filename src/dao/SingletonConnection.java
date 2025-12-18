package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class SingletonConnection {

    private static Connection connection;

    private SingletonConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/gestion",
                    "root",
                    ""
                );
            }
        } catch (Exception e) {
            System.out.println("Erreur connexion BD : " + e.getMessage());
        }
        return connection;
    }
}
