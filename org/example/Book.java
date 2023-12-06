package org.example;

import java.sql.*;

public class Book {
    public static Connection Connect(String database_name, String username, String password) {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database_name, username, password);
            if (connection != null) {
                System.out.println("connected");
            } else {
                System.out.println("not connected");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return connection;
    }

    public static void CreatedBook(Connection connection, String BookID, String Title, String AuthorID, String StockQuantity, String Price, String PublicationYear) {
        Statement statement;
        try {
            String query = String.format("INSERT INTO Books (BookID, Title, AuthorID, StockQuantity, Price, PublicationYear) VALUES(%s, '%s', %s, %s, %s, %s)", BookID, Title, AuthorID, StockQuantity, Price, PublicationYear);
            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("INSERT 0 1");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public static void ReadBook(Connection connection) {
        Statement statement;
        ResultSet resultSet;
        try {
            String query = "SELECT * FROM Books " +
                    "JOIN Authors ON Books.AuthorID = Authors.AuthorID " +
                    "JOIN OrderDetails ON Books.BookID = OrderDetails.BookID " +
                    "JOIN Orders ON OrderDetails.OrderID = Orders.OrderID " +
                    "JOIN Customers ON Orders.CustomerID = Customers.CustomerID";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                System.out.print(resultSet.getString("BookID") + " " + resultSet.getString("Title") + " ");
                System.out.print(resultSet.getString("Authors.FirstName") + " " + resultSet.getString("Price") + " ");
                System.out.println(resultSet.getString("PublicationYear") + " " + resultSet.getString("StockQuantity"));
                System.out.print("OrderID: " + resultSet.getString("OrderID") + " ");
                System.out.print("Customer: " + resultSet.getString("Customers.FirstName") + " " + resultSet.getString("Customers.LastName") + " ");
                System.out.println("Quantity: " + resultSet.getString("Quantity"));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public static void UpdateBook(Connection connection, String BookId, String StockQuantity) {
        try {
            String query = String.format("UPDATE Books SET StockQuantity = %s WHERE BookID = %s", BookId, StockQuantity);
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Book Table Updated");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public static void DeleteBook(Connection connection, String BookId) {
        try {
            String query = String.format("DELETE FROM Books WHERE BookID = %s", BookId);
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row with BookID = " + BookId + " Deleted!");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public static void Insert_Book(Connection connection, String BookID, String OrderID, String StockQuantity) {
        try {
            String query = String.format("INSERT INTO OrderDetails (BookID, OrderID, StockQuantity) VALUES (%s, %s, %s);", BookID, OrderID, StockQuantity);
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Inserted into OrderDetails");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static int CheckNumberOfBooks(Connection connection, String BookId, String StockQuantity) {
        try {
            String query = String.format("SELECT StockQuantity FROM Books WHERE BookID = %s", BookId);
            Statement statement;
            ResultSet rs;
            try {
                statement = connection.createStatement();
                rs = statement.executeQuery(query);

                if (rs.next()) {
                    if (rs.getInt("StockQuantity") - Integer.valueOf(StockQuantity) >= 0) {
                        return (rs.getInt("StockQuantity") - Integer.valueOf(StockQuantity));
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public static void OrderBook(Connection connection, String BookId, String CustomerId, String StockQuantity) {
        try {
            connection.setAutoCommit(false);
            if (CheckNumberOfBooks(connection, BookId, StockQuantity) >= 0) {
                Insert_Book(connection, BookId, CustomerId, StockQuantity);
                String quantity = String.valueOf(CheckNumberOfBooks(connection, BookId, StockQuantity));
                UpdateBook(connection, BookId, quantity);
                System.out.println("Customer Ordered Book");
                connection.commit();
            } else {
                connection.rollback();
                System.out.println("Can't order book because there's not enough stock");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public static void TableDetail(Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet resultSet = metaData.getTables(null, null, null, types);
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                System.out.println("Table: " + tableName);
                ResultSet columns = metaData.getColumns(null, null, tableName, null);
                System.out.println("Columns: ");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String dataType = columns.getString("TYPE_NAME");
                    int size = columns.getInt("COLUMN_SIZE");
                    System.out.println("Name: " + columnName + ", Data Type: " + dataType + ", Size: " + size);
                }
                columns.close();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void ColumnDetail(Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet table = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (table.next()) {
                String tableName = table.getString("TABLE_NAME");
                System.out.println("Table: " + tableName);
                ResultSet resultSet = metaData.getColumns(null, null, tableName, null);
                while (resultSet.next()) {
                    System.out.println("Name: " + resultSet.getString("COLUMN_NAME") + ", Data Type: " + resultSet.getString("TYPE_NAME"));
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void Primary_Key(Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                System.out.println("Table: " + tableName);
                ResultSet primary_keys = metaData.getPrimaryKeys(null, null, tableName);
                while (primary_keys.next()) {
                    String columnName = primary_keys.getString("COLUMN_NAME");
                    System.out.println("  Primary Key Column: " + columnName);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void Foreign_Key(Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                System.out.println("Table: " + tableName);
                ResultSet foreignKeys = metaData.getImportedKeys(null, null, tableName);
                while (foreignKeys.next()) {
                    String column_name = foreignKeys.getString("FKCOLUMN_NAME");
                    String reference_table = foreignKeys.getString("PKTABLE_NAME");
                    String reference_column = foreignKeys.getString("PKCOLUMN_NAME");
                    System.out.println("Foreign Key: " + column_name + ", Table: " + reference_table + ", Column: " + reference_column);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Connection connection = Connect("MadinaDataAssign2", "madinarustamova", "Medine2233!");

        // Insert examples into Books and Customers tables
        CreatedBook(connection, "100", "The Great Gatsby", "1", "50", "19.99", "1925");
//        // other insert statements...
//
//        // Read data from Books table with additional JOINs
//        ReadBook(connection);
//
//        // Update the stock quantity of a book
//        UpdateBook(connection, "101", "3");
//
//        // Delete a row from the Books table
//        DeleteBook(connection, "101");
//
//        // Insert into OrderDetails table
//        Insert_Book(connection, "201", "101", "3");
//
//        // Check the number of available books
//        System.out.println(CheckNumberOfBooks(connection, "201", "10"));
//
//        // Order a book
//        OrderBook(connection, "201", "101", "3");
//
//        // Display table details, column details, primary keys, and foreign keys
//        TableDetail(connection);
//        ColumnDetail(connection);
//        Primary_Key(connection);
//        Foreign_Key(connection);
        //   }
    }
}