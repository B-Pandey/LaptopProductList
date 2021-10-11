import java.sql.*;
import java.util.Scanner;

public class LaptopTest1 {

    public static String sqlSearchQuery = "SELECT * FROM LAPTOP";
    public static String sqlCreateQuery = "INSERT INTO LAPTOP VALUES (?,?,?,?)";
    public static String sqlUpdateQuery = "UPDATE LAPTOP SET name = ?, brand = ?, price= ? WHERE id = ?";
    public static String sqlDeleteQuery = "DELETE FROM LAPTOP  WHERE id=?";

    public static void main(String[] args) {
        getUserChoice();
    }

    public static int getOption(Scanner scanner) {
        final int MIN_OPTION = 1;
        final int MAX_OPTION = 5;
        int menuChoice = 0;
        boolean valid = true;
        do {
            System.out.println("Choose the following option:\n" +
                    "[1] : Create Laptop Information.\n" +
                    "[2] : Display Laptop List. \n" +
                    "[3] : Delete Laptop List.\n" +
                    "[4] : Update Laptop List. \n" +
                    "[5] : Exit the program.");
            if (scanner.hasNextInt()) {
                menuChoice = scanner.nextInt();
                if (menuChoice >= MIN_OPTION && menuChoice <= MAX_OPTION) {
                    valid = true;
                } else {
                    System.out.println("Invalid choice, please select option between 1-5.");
                }
            } else {
                scanner.next();
                valid = false;
                System.out.println("Enter a valid integer value");
            }
        } while (!valid);
        return menuChoice;
    }

    public static void getUserChoice() {
        Connection connection = getConnection();
        Laptop laptopList = new Laptop();
        Scanner scanner = new Scanner(System.in);

        int menuChoice = getOption(scanner);

        if (menuChoice == 1) {
            System.out.println("How many quantities would you like to create.");
            createLaptopList(connection, laptopList);
            getUserChoice();

        } else if (menuChoice == 2) {
            displayLaptopList(connection);
        } else if (menuChoice == 3) {
            displayLaptopList(connection);
            deleteLaptopFromList(connection, scanner);
            System.out.println("Updated Record");
            displayLaptopList(connection);
            getUserChoice();

        } else if (menuChoice == 4) {
            System.out.println("Current Laptop Lists:");
            displayLaptopList(connection);
            updateLaptopList(connection, laptopList, scanner);
            getUserChoice();

        } else if (menuChoice == 5) {
            System.out.println("Thank you.");
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            DriverManager.registerDriver(new org.hsqldb.jdbc.JDBCDriver());
            conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/mydb", "SA", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static int isValidNumber() {
        Scanner scanner = new Scanner(System.in);
        int number = -1;
        while (number < 0) {
            String inputValue = scanner.nextLine();
            try {
                number = Integer.parseInt(inputValue);
            } catch (NumberFormatException e) {
                number = -1;
                System.out.println("Enter Valid number");
              //  scanner.nextLine();
            }
        }
        return number;
    }

    public static void getUserInput(Laptop laptopList, int itemQuantity) {
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < itemQuantity; i++) {
            System.out.println("Enter ID");
            laptopList.setId(isValidNumber());

            System.out.println("Enter the Laptop name:");
            laptopList.setName(scanner.nextLine());

            System.out.println("Enter the Laptop Brand:");
            laptopList.setBrand(scanner.next());

            System.out.println("Enter the Laptop Price:");
            laptopList.setPrice(isValidNumber());

        }
    }

    public static void createLaptopList(Connection conn, Laptop laptopList) {
        getUserInput(laptopList, isValidNumber());
        int row = 0;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(sqlCreateQuery);
            preparedStatement.setInt(1, laptopList.getId());
            preparedStatement.setString(2, laptopList.getName());
            preparedStatement.setString(3, laptopList.getBrand());
            preparedStatement.setDouble(4, laptopList.getPrice());

            row = preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(row + " items updated. Thank you.");

    }

    public static void displayLaptopList(Connection conn) {
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlSearchQuery);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            System.out.println((resultSetMetaData.getColumnName(1) + "\t\t" +
                    resultSetMetaData.getColumnName(2) + "\t\t\t" +
                    resultSetMetaData.getColumnName(3)) + "\t\t\t" +
                    resultSetMetaData.getColumnName(4));
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + "\t\t" +
                        resultSet.getString("name") + "\t\t\t" +
                        resultSet.getString("brand") + "\t\t\t" +
                        resultSet.getString("price"));
                System.out.println("----------------------------");
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteLaptopFromList(Connection connection, Scanner scanner) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteQuery);

            System.out.println("Enter the Laptop id you wish to delete: ");
            preparedStatement.setInt(1, isValidNumber());

            int rows = preparedStatement.executeUpdate();

            if (rows != 0) {
                System.out.println(rows + "Record deleted");
            } else {
                System.out.println("Record no found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateLaptopList(Connection connection, Laptop laptopList, Scanner scanner) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateQuery);
            System.out.println("Enter the row id to update the data");
            laptopList.setId(isValidNumber());

            System.out.println("Enter the Laptop name:");
            scanner.next();
            laptopList.setName(scanner.nextLine());

            System.out.println("Enter the Laptop Brand:");
            laptopList.setBrand(scanner.nextLine());

            System.out.println("Enter the Laptop Price:");
            laptopList.setPrice(isValidNumber());

            preparedStatement.setString(1, laptopList.getName());
            preparedStatement.setString(2, laptopList.getBrand());
            preparedStatement.setDouble(3, laptopList.getPrice());
            preparedStatement.setInt(4, laptopList.getId());
            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("rowUpdated");
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}