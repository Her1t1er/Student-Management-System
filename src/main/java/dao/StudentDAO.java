/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import util.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.sqlite.SQLiteException;

/**
 *
 * @author theos
 */
public class StudentDAO {
    /**
     * Inserts a new student.
     * 
     * @return true on success, false if the email already exists.
     * @throws RuntimeException for unexpected DB errors.
     */
    public boolean addStudent(String name, String email, String course, int marks) {
        String sql = "INSERT INTO students(name,email,course,marks) VALUES(?,?,?,?)";

        try (Connection conn = DB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, course);
            ps.setInt(4, marks);

            ps.executeUpdate();
            return true;

        } catch (SQLiteException e) {
            // SQLITE_CONSTRAINT (19) covers UNIQUE violations
            if (e.getResultCode().code == 19) {
                return false; // duplicate email
            }
            throw new RuntimeException("DB error adding student", e);
        } catch (Exception e) {
            throw new RuntimeException("DB error adding student", e);
        }
    }

    /**
     * Updates an existing student's information.
     *
     * @return true on success, false if the email is already used by another
     *         student.
     */
    public boolean updateStudent(int id, String name, String email, String course, int marks) {
        String sql = "UPDATE students SET name=?, email=?, course=?, marks=? WHERE id=?";
        try (Connection conn = DB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, course);
            ps.setInt(4, marks);
            ps.setInt(5, id);
            ps.executeUpdate();
            return true;
        } catch (SQLiteException e) {
            if (e.getResultCode().code == 19) {
                return false; // duplicate email
            }
            throw new RuntimeException("DB error updating student", e);
        } catch (Exception e) {
            throw new RuntimeException("DB error updating student", e);
        }
    }

    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("DB error deleting student", e);
        }
    }

    public List<Object[]> getAllStudents() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DB.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Object[] {
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("course"),
                        rs.getInt("marks")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
