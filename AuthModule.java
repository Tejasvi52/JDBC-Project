package projectUser;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;

public class AuthModule {
	static final String DB_URL = "jdbc:mysql://localhost:3306/userdb";
    static final String DB_USER = "root";
    static final String DB_PASS = "root"; 
    static Scanner sc = new Scanner(System.in);
    static Connection conn = null;

    public static void main(String[] args) throws Exception {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        createTable();
        while (true) {
        	
        	System.out.println("****************************************");
        	System.out.println("\nMenu Options:");
            System.out.println("\n1. Register\n2. Login\n3. Logout\n4. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    logout();
                    break;
                case 4:
                	Exit();
                	 System.exit(0);
            }
        }
    }
		
	

	static void createTable() throws Exception {
        String sql1 = "CREATE TABLE IF NOT EXISTS registrationuser (user_id INT PRIMARY KEY, Name VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL, mob_no VARCHAR(20) NOT NULL, DOB DATE NOT NULL)";
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps1.executeUpdate();

        String sql2 = "CREATE TABLE IF NOT EXISTS userregist2 (Id INT, username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL)";
        PreparedStatement ps2 = conn.prepareStatement(sql2);
        ps2.executeUpdate();
    }

    static void register() throws Exception {
        System.out.print("Enter User ID: ");
        int Id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Mobile No: ");
        String mobileNo = sc.nextLine();
        System.out.print("Enter DOB (YYYY-MM-DD): ");
        String dob = sc.nextLine();
        String username = generateUsername(name);
        String password = generatePassword();

        String sql1 = "INSERT INTO registrationuser (user_id, Name, email, mob_no, DOB) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps1 = conn.prepareStatement(sql1);
        ps1.setInt(1, Id);
        ps1.setString(2, name);
        ps1.setString(3, email);
        ps1.setString(4, mobileNo);
        ps1.setDate(5, Date.valueOf(dob));
        ps1.executeUpdate();

        String sql2 = "INSERT INTO userregist2 (Id, username, password) VALUES (?, ?, ?)";
        PreparedStatement ps2 = conn.prepareStatement(sql2);
        ps2.setInt(1, Id);
        ps2.setString(2, username);
        ps2.setString(3, password);
        ps2.executeUpdate();

        System.out.println("\n Registered Successfully!");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
    }

    static void login() throws Exception {
        System.out.print("Enter Username: ");
        String uname = sc.nextLine();
        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        //&String sql = "SELECT r.Name FROM registrationuser r JOIN userregist2 u ON r.user_id = u.Id WHERE u.username=? AND u.password=?";
        String sql = "SELECT * FROM registrationuser JOIN userregist2 ON registrationuser.user_id = userregist2.Id WHERE userregist2.username=? AND userregist2.password=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, uname);
        ps.setString(2, pass);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("\n Login Successful. Welcome " + rs.getString("Name"));
        } else {
            System.out.println("\n Login Failed. Invalid credentials.");
        }
    }

    static void logout() {
        System.out.println("\n You are logged out.");
    }
    private static void Exit() {
        	System.out.println("Exit System-----");
    }

    static String generateUsername(String name) {
        return name.toLowerCase().replaceAll(" ", "") + new Random().nextInt(1000);
    }

    static String generatePassword() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String symbols = "!@#$&*";
        String all = upper + lower + digits + symbols;
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(all.charAt(r.nextInt(all.length())));
        }
        return sb.toString();
    }
    }