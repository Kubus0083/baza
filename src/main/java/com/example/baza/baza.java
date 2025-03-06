package com.example.baza;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class baza {

    private static  String URL = "jdbc:mysql://localhost:3306/school";
    private static  String USER = "root";
    private static  String PASSWORD = "";
    private static  String DB_NAME = "school";
    private static  String TABLE_NAME = "students";

    public static Connection getConnection() throws SQLException {
        Connection connection = null;

        try {

            connection = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
            System.out.println("Połączenie udane.");

        } catch (SQLException e) {
            // Jeśli połączenie się nie uda, sprawdzamy, czy baza danych istnieje
            System.out.println("Połączenie nieudane. Tworzę bazę danych...");

            // Tworzymy nową bazę danych
            try (Connection tempConnection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                Statement stmt = tempConnection.createStatement();
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            } catch (SQLException ex) {
                System.out.println("Błąd przy tworzeniu bazy danych: " + ex.getMessage());
            }

        }
            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + "id INT(11) AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(100), "
                    + "age INT(11), "
                    + "grade VARCHAR(3)"
                    + ")";

            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(createTableSQL);
            } catch (SQLException e) {
                System.out.println("Błąd przy tworzeniu tabeli: " + e.getMessage());
            }
        return connection;
    }
    public static void addStudent(String name, int  age, String grade) {
        String query = "INSERT INTO students (name, age, grade) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, grade);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static ObservableList<Student> getAllStudents() {
        ObservableList<Student> students = FXCollections.observableArrayList();
        String query = "SELECT * FROM students";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                students.add(new Student(rs.getInt("id"), rs.getString("name"), rs.getInt("age"), rs.getString("grade")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public static void updateStudent(String name, int  age, String grade, int id) {
        String query = "UPDATE students SET name = ?, age = ?, grade = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, grade);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteStudent(int id) {
        String query = "DELETE FROM students WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}