/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peopledb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author ipd
 */
public class DataBase {

    private Connection conn;

    DataBase() throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/people",
                "root", "root");
    }

    public void addPerson(String name, int age) throws SQLException {
        String query = "INSERT INTO person VALUES (NULL,?,?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, name);
        ps.setInt(2, age);
        ps.execute();
    }

    public ArrayList<Person> getAllPersons() throws SQLException {
        final String SELECT_PERSON = "SELECT * FROM person";
        ArrayList<Person> result = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(SELECT_PERSON);
            while (rs.next()) {
                int id = rs.getInt("idperson");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                Person p = new Person(id, name, age);
                result.add(p);
            }
        }
        return result;
    }

    public Person getPersonByID(int id) throws SQLException {
        String query = "SELECT * FROM person WHERE idperson = " + id;
        
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if(!rs.next()){
                throw new SQLException("No row found with id = "+id);
            }
        
        id = rs.getInt("idperson");
        String name = rs.getString("name");
        int age = rs.getInt("age");
        Person p = new Person(id, name, age);
        return p;
        }
    }

    public void updatePerson(Person p) throws SQLException {
        String query = "UPDATE person SET name=?,age=? WHERE personid=?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, p.name);
        ps.setInt(2, p.age);
        ps.setInt(3, p.id);
        ps.execute();
    }

    public void deletePerson(int id) throws SQLException {
        String query = "DELETE FROM person WHERE idperson = " + id;
        PreparedStatement ps = conn.prepareStatement(query);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        }
    }
}
