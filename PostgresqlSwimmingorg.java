import java.sql.*;
import java.util.Scanner;
//establish connection with database
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
            System.out.println("Java JDBC PostgreSQL Swimming Organization");
            System.out.println("Connected to PostgreSQL database successfully!");
            System.out.println();
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            System.exit(1);
        }
    }
    
    public void displayMenu() {
        System.out.println("Swimming Organization Database Menu");
        System.out.println("1. Find swimmers in Level 3");
        System.out.println("2. Find primary caretakers with 2 or more swimmers");
        System.out.println("3. Find caretakers doing Recording but not Officiating");
        System.out.println("4. Exit");
        System.out.print("Enter your choice (1-4): ");
    }
    
    public void query1_SwimmersInLevel3() {
        System.out.println("Query 1: Swimmers in Level 3");
        String sql = "SELECT FName, LName, Phone FROM Swimmer WHERE CurrentLevelId = 3 ORDER BY LName, FName";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            System.out.println("Swimmers currently in Level 3 (Yellow):");
            System.out.println();
            System.out.printf("%-15s %-15s %-15s%n", "First Name", "Last Name", "Phone");
            System.out.println();
            
            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                String firstName = rs.getString("FName");
                String lastName = rs.getString("LName");
                String phone = rs.getString("Phone");
                System.out.printf("%-15s %-15s %-15s%n", firstName, lastName, phone);
            }
            
            if (!hasResults) {
                System.out.println("No swimmers found in Level 3.");
            }
            
        } catch (SQLException e) {
            System.err.println("Error executing query 1: " + e.getMessage());
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
                        break;
                    case 3:
                        break;
                    case 4:
                        System.out.println("Thank you for using Swimming Organization Database!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1-4.");
                }
                
                if (choice != 4) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    scanner.nextLine(); //wait for user input
                }
                
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number between 1-4.");
                scanner.nextLine(); //clear input
                choice = 0; //continue loop
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
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        PostgresqlSwimmingorg app = new PostgresqlSwimmingorg();
        
        //shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.close();
        }));
        
        app.run();
        app.close();
    }
}