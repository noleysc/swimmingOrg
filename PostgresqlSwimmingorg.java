import java.sql.*;
import java.util.Scanner;

public class PostgresqlSwimmingorg {
    private static final String URL = "jdbc:postgresql://localhost:5432/swimmingorg";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "32332322";
    
    private Connection connection;
    private Scanner scanner;
    
    public PostgresqlSwimmingorg() {
        scanner = new Scanner(System.in);
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Java JDBC PostgreSQL swimmingorg");
            System.out.println("Connected to PostgreSQL database!");
            System.out.println("Reading swimming organization records...");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            System.exit(1);
        }
    }
    
    public void displayMenu() {
        System.out.println("Please enter your choice:");
        System.out.println("1. Names and phones of all swimmers currently in level (of id) 3:");
        System.out.println("2. Name(s) of caretakers who are the primary (main) caretakers of at least two swimmers:");
        System.out.println("3. Names of all caretakers who have volunteered for the task 'Recording' but not the task 'Officiating':");
        System.out.println("4. quit");
        System.out.print(">");
    }
    /* query 1- swimmers in level 3-----
    SELECT FName, LName, Phone
    FROM Swimmer
    WHERE CurrentLevelId=3
    ORDER BY LName, FName

    show swimmers with CurrentLevelId=3
    ------------------------------------*/

    public void query1_SwimmersInLevel3() {
        System.out.println("Names and phones of all swimmers currently in level (of id) 3:");
        System.out.println("fname | lname | phone");
        
        String sql = "SELECT FName, LName, Phone FROM Swimmer WHERE CurrentLevelId = 3 ORDER BY LName, FName";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                String firstName = rs.getString("FName");
                String lastName = rs.getString("LName");
                String phone = rs.getString("Phone");
                System.out.printf("%s | %s | %s%n", firstName, lastName, phone);
            }
            
            if (!hasResults) {
                System.out.println("No swimmers found in Level 3.");
            }
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            scanner.nextLine(); //pause and wait for input       
        } catch (SQLException e) {
            System.err.println("Error executing query 1: " + e.getMessage());
        }
    }
    /*query 2- Caretakes with Multiple Swimmers----------------------
    SELECT c.FName, c.Phone 
    FROM Caretaker c 
    WHERE c.CT_Id IN (
    SELECT s.Main_CT_Id 
    FROM Swimmer s 
    GROUP BY s.Main_CT_Id 
    HAVING COUNT(s.SwimmerId) >= 2) 
    ORDER BY c.FName;

    select caretakers that are associated with 
    swimmers by CT_Id in respective swimmer row in swimmer table
    -----------------------------------------------------------------*/
    public void query2_CaretakersWithMultipleSwimmers() {
        System.out.println("Name(s) of caretakers who are the primary (main) caretakers of at least two swimmers:");
        System.out.println("fname | phone");
        
        String sql = "SELECT c.FName, c.Phone " +
                    "FROM Caretaker c " +
                    "WHERE c.CT_Id IN (" +
                    "    SELECT s.Main_CT_Id " +
                    "    FROM Swimmer s " +
                    "    GROUP BY s.Main_CT_Id " +
                    "    HAVING COUNT(s.SwimmerId) >= 2" +
                    ") " +
                    "ORDER BY c.FName";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                String firstName = rs.getString("FName");
                String phone = rs.getString("Phone");
                System.out.printf("%s | %s%n", firstName, phone);
            }
            
            if (!hasResults) {
                System.out.println("No caretakers found with 2 or more swimmers.");
            }
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            scanner.nextLine(); //pause and wait for input
        } catch (SQLException e) {
            System.err.println("Error executing query 2: " + e.getMessage());
        }
    }
    /*query 3- recording but not officiating ---------------------------------------
    SELECT DISTINCT c.FName, c.LName 
    FROM Caretaker c 
    JOIN Commitment cm ON c.CT_Id = cm.CT_Id 
    JOIN V_Task vt ON cm.VT_Id = vt.VT_Id 
    WHERE vt.Name = 'Recording' 
    AND c.CT_Id NOT IN (
    SELECT c2.CT_Id 
    FROM Caretaker c2 
    JOIN Commitment cm2 ON c2.CT_Id = cm2.CT_Id 
    JOIN V_Task vt2 ON cm2.VT_Id = vt2.VT_Id 
    WHERE vt2.Name = 'Officiating'
) 
    ORDER BY c.FName, c.LName;

        show caretakers with the "recording" committment, but not the "officiating" committment
------------------------------------*/
    public void query3_RecordingButNotOfficiating() {
        System.out.println("Names of all caretakers who have volunteered for the task 'Recording' but not the task 'Officiating':");
        System.out.println("fname | lname");
        
        String sql = "SELECT DISTINCT c.FName, c.LName " +
                    "FROM Caretaker c " +
                    "JOIN Commitment cm ON c.CT_Id = cm.CT_Id " +
                    "JOIN V_Task vt ON cm.VT_Id = vt.VT_Id " +
                    "WHERE vt.Name = 'Recording' " +
                    "AND c.CT_Id NOT IN (" +
                    "    SELECT c2.CT_Id " +
                    "    FROM Caretaker c2 " +
                    "    JOIN Commitment cm2 ON c2.CT_Id = cm2.CT_Id " +
                    "    JOIN V_Task vt2 ON cm2.VT_Id = vt2.VT_Id " +
                    "    WHERE vt2.Name = 'Officiating'" +
                    ") " +
                    "ORDER BY c.FName, c.LName";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                String firstName = rs.getString("FName");
                String lastName = rs.getString("LName");
                System.out.printf("%s | %s%n", firstName, lastName);
            }
            
            if (!hasResults) {
                System.out.println("No caretakers found doing Recording but not Officiating.");
            }
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            scanner.nextLine(); //pause and wait for input
        } catch (SQLException e) {
            System.err.println("Error executing query 3: " + e.getMessage());
        }
    }
    
    public void run() {
        int choice;
        
        do {
            displayMenu();
            try {
                choice = scanner.nextInt();
                
                switch (choice) {
                    case 1:
                        query1_SwimmersInLevel3();
                        break;
                    case 2:
                        query2_CaretakersWithMultipleSwimmers();
                        break;
                    case 3:
                        query3_RecordingButNotOfficiating();
                        break;
                    case 4:
                        System.out.println("Exit");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1-4.");
                }
                
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number between 1-4.");
                scanner.nextLine(); // clear invalid input
                choice = 0; // continue loop
            }
            
        } while (choice != 4);
    }
    
    public void close() {
        try {
            if (scanner != null) {
                scanner.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        PostgresqlSwimmingorg app = new PostgresqlSwimmingorg();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.close();
        }));
        
        app.run();
        app.close();
    }
}