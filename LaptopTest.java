import org.hsqldb.jdbc.JDBCDataSourceFactory;

import java.sql.*;
import java.util.Scanner;

public class LaptopTest {
    public static void main(String[] args) {

        Laptop laptopList = new Laptop();

        Connection connection = null;
        try {
            DriverManager.registerDriver(new org.hsqldb.jdbc.JDBCDriver());
            connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/mydb", "SA", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < 2; i++) {
            System.out.println("Enter the Laptop id: ");
            laptopList.setId(scanner.nextInt());
            scanner.nextLine();

            System.out.println("Enter the Laptop name: ");
            laptopList.setName(scanner.nextLine());

            System.out.println("Enter the Laptop Brand: ");
            laptopList.setBrand(scanner.nextLine());

            System.out.println("Enter the Laptop Price: ");
            laptopList.setPrice(scanner.nextFloat());
            scanner.nextLine();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("insert into LAPTOP values (?,?,?,?)");
                preparedStatement.setInt(1, laptopList.getId());
                preparedStatement.setString(2, laptopList.getName());
                preparedStatement.setString(3, laptopList.getBrand());
                preparedStatement.setDouble(4, laptopList.getPrice());

                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM LAPTOP");
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

                while(resultSet.next()) {
                    System.out.println("Id: " + resultSet.getInt("id"));
                    System.out.println("Name: " + resultSet.getString("name"));
                    System.out.println("Brand: " + resultSet.getString("brand"));
                    System.out.println("Price: " + resultSet.getString("price"));
                    statement.close();
                    // System.out.println(resultSet.getString(1) +" " +  resultSet.getString(2) + " " + resultSet.getFloat(3));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("delete from LAPTOP  WHERE id=?");
                System.out.println("Enter the Laptop id you wish to delete: ");
                int id = scanner.nextInt();
                preparedStatement.setInt(1, id);

                int rows = preparedStatement.executeUpdate();

                if (rows != 0) {
                    System.out.println("Record deleted");
                } else {
                    System.out.println("Record no found");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update LAPTOP set name = ?, brand = ?, price= ? where id = ?");
            boolean rowUpdated = false;
            //System.out.println("Enter the row id to update the data");
            //int rowNumber = scanner.nextInt();
                System.out.println("Enter the Laptop id: ");
                laptopList.setId(scanner.nextInt());
                scanner.nextLine();

                System.out.println("Enter the Laptop name: ");
                laptopList.setName(scanner.nextLine());

                System.out.println("Enter the Laptop Brand: ");
                laptopList.setBrand(scanner.nextLine());

                System.out.println("Enter the Laptop Price: ");
                laptopList.setPrice(scanner.nextFloat());
                scanner.nextLine();

            preparedStatement.setString(1, laptopList.getName());
            preparedStatement.setString(2, laptopList.getBrand());
            preparedStatement.setDouble(3, laptopList.getPrice());
            preparedStatement.setInt(4, laptopList.getId());
            if(preparedStatement.executeUpdate() > 0){
                System.out.println( "rowUpdated");
            }

            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }


    }
}