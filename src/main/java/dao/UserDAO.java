/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import util.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author theos
 */
public class UserDAO {
    public boolean login(String username, String password) {
        String sql = "SELECT 1 FROM users WHERE username=? AND password=?";

        try (Connection conn = DB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            throw new RuntimeException("Login error: " + e.getMessage(), e);
        }
    }

    
    public boolean register(String fullName, String email, String username, String password) {
        String checkSql = "SELECT 1 FROM users WHERE username=?";
        String insertSql = "INSERT INTO users(username, password, full_name, email) VALUES(?,?,?,?)";

        try (Connection conn = DB.getConnection()) {
            // Check if username already taken
            try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                check.setString(1, username);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next())
                        return false; // username already exists
                }
            }
            // Insert new lecturer
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, fullName);
                ps.setString(4, email);
                ps.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("Register error: " + e.getMessage(), e);
        }
    }
}
