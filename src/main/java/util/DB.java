/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author theos
 */
public class DB {
    private static final String URL = "jdbc:sqlite:sms.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

}
