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

            // Migration: ensure the UNIQUE constraint on students.email exists.
            // SQLite cannot ALTER TABLE to add a constraint, so we use the
            // rename -> create-new -> copy -> drop approach if needed.
            boolean uniqueExists = false;
            try (java.sql.ResultSet idx = st.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='index' " +
                            "AND tbl_name='students' AND sql LIKE '%email%' AND sql LIKE '%UNIQUE%'")) {
                if (idx.next()) {
                    uniqueExists = true;
                }
            } catch (Exception ignored) {
            }

            if (!uniqueExists) {
                // Rename old table, re-create with UNIQUE, copy data, drop old
                st.executeUpdate("ALTER TABLE students RENAME TO students_old");
                st.executeUpdate("""
                            CREATE TABLE students(
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                name TEXT NOT NULL,
                                email TEXT NOT NULL UNIQUE,
                                course TEXT NOT NULL,
                                marks INTEGER NOT NULL
                            )
                        """);
                // Copy only rows with a unique email (keep the first occurrence per email)
                st.executeUpdate("""
                            INSERT INTO students(id, name, email, course, marks)
                            SELECT id, name, email, course, marks FROM students_old
                            WHERE id IN (
                                SELECT MIN(id) FROM students_old GROUP BY email
                            )
                        """);
                st.executeUpdate("DROP TABLE students_old");
            }

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
