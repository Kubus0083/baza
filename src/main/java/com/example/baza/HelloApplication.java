package com.example.baza;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    private TextField nameField, ageField, gradeField, idField;
    private Button addButton, updateButton, deleteButton;
    private TableView<Student> studentTable;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Pola tekstowe
        nameField = new TextField();
        nameField.setPromptText("Enter Name");

        ageField = new TextField();
        ageField.setPromptText("Enter Age");

        gradeField = new TextField();
        gradeField.setPromptText("Enter Grade");
        idField = new TextField();
        idField.setPromptText("Enter Id");

        // Przyciski
        addButton = new Button("Add");
        updateButton = new Button("Update");
        deleteButton = new Button("Delete");

        // Tabela studentów
        studentTable = new TableView<>();
        studentTable.getColumns().addAll(createColumns());

        // Zdarzenia przycisków
        addButton.setOnAction((e)->{baza.addStudent(nameField.getText(), Integer.parseInt(ageField.getText()),gradeField.getText());refreshTable();});
        updateButton.setOnAction(e -> {baza.updateStudent(nameField.getText(), Integer.parseInt(ageField.getText()),gradeField.getText(), Integer.parseInt(idField.getText()));refreshTable();});
        deleteButton.setOnAction(e -> {baza.deleteStudent(Integer.parseInt(idField.getText()));refreshTable();});

        // Układ
        VBox layout = new VBox(10, nameField, ageField, gradeField,idField, addButton, updateButton, deleteButton, studentTable);
        Scene scene = new Scene(layout, 400, 400);

        primaryStage.setTitle("Student Management");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Inicjalizacja tabeli


        refreshTable();
    }
    private void refreshTable() {
        // Pobieramy wszystkie rekordy ze źródła (np. bazy danych)
        ObservableList<Student> students = baza.getAllStudents();

        // Ustawiamy nowe dane w tabeli
        studentTable.setItems(students);
    }

    // Tworzenie kolumn w tabeli
    private List<TableColumn<Student, ?>> createColumns() {


        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Student, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty().asObject());

        TableColumn<Student, String> gradeColumn = new TableColumn<>("Grade");
        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty());

        TableColumn<Student, String> idCollumn = new TableColumn<>("Id");
        idCollumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asString());
        // Dodajemy bezpośrednio kolumny do listy
        List<TableColumn<Student, ?>> columns = new ArrayList<>();
        columns.add(idCollumn);
        columns.add(nameColumn);
        columns.add(ageColumn);
        columns.add(gradeColumn);

        return columns;
    }
}
