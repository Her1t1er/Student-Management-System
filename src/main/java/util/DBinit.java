/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.Statement;

/**
 *
 * @author theos
 */
public class DBinit {
    public static void init() {
        try (Connection conn = DB.getConnection();
                Statement st = conn.createStatement()) {

            st.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS users(
                            username TEXT PRIMARY KEY,
                            password TEXT NOT NULL,
                            full_name TEXT,
                            email TEXT
                        )
                    """);

            // Add new columns if they don't exist yet (for existing databases)
            try {
                st.executeUpdate("ALTER TABLE users ADD COLUMN full_name TEXT");
            } catch (Exception ignored) {
            }
            try {
                st.executeUpdate("ALTER TABLE users ADD COLUMN email TEXT");
            } catch (Exception ignored) {
            }

            st.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS students(
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL,
                            email TEXT NOT NULL UNIQUE,
                            course TEXT NOT NULL,
                            marks INTEGER NOT NULL
                        )
                    """);

            // default login user
            st.executeUpdate("""
                        INSERT OR IGNORE INTO users(username, password)
                        VALUES('admin', 'admin123')
                    """);

        } catch (Exception e) {
            throw new RuntimeException("DB Init failed: " + e.getMessage(), e);
        }
    }
}
