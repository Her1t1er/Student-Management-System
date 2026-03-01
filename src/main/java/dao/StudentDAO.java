/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import util.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author theos
 */
public class StudentDAO {
    public void addStudent(String name, String email, String course, int marks) {
        String sql = "INSERT INTO students(name,email,course,marks) VALUES(?,?,?,?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, course);
            ps.setInt(4, marks);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Object[]> getAllStudents() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Object[]{
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
