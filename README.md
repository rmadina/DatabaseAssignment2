# DatabaseAssignment2
Database Assignment README

Overview

This Java program, named Book, is designed to interact with a PostgreSQL database for managing information related to books, authors, orders, and customers. It provides functionalities such as creating books, reading book details with joins, updating stock quantities, deleting books, inserting into order details, checking the number of available books, and ordering books.

Setup

Prerequisites
Java Development Kit (JDK): Ensure that you have Java installed on your system. You can download it from Oracle JDK or use OpenJDK.
PostgreSQL Database: Install and set up a PostgreSQL database on your local machine or a server. Make sure to create a database with the specified name, username, and password used in the program.
PostgreSQL JDBC Driver: Download the PostgreSQL JDBC driver JAR file and include it in your project. You can find it here.
Project Configuration
IDE: This project is developed using IntelliJ IDEA. Open the project in IntelliJ or your preferred IDE.
JDBC Driver: Add the PostgreSQL JDBC driver JAR to your project's classpath.
Database Connection: Update the Connect method in the Book class with your PostgreSQL database details (database name, username, and password).
Usage

Database Operations
Create Book:
Use the CreatedBook method to insert a new book into the Books table. Provide the book details such as BookID, Title, AuthorID, StockQuantity, Price, and PublicationYear as arguments.
Read Book Details:
The ReadBook method performs a SELECT operation on multiple tables (Books, Authors, OrderDetails, Orders, Customers) and displays the book details with additional information using JOINs.
Update Stock Quantity:
The UpdateBook method updates the stock quantity of a book in the Books table. Provide BookID and the new StockQuantity as arguments.
Delete Book:
The DeleteBook method deletes a book from the Books table based on the provided BookID.
Insert into OrderDetails:
The Insert_Book method inserts a new record into the OrderDetails table. Provide BookID, OrderID, and StockQuantity as arguments.
Check Number of Available Books:
The CheckNumberOfBooks method checks the available stock for a given BookID and StockQuantity, ensuring there is enough stock for an order.
Order Book:
The OrderBook method handles the process of ordering a book, updating the stock quantity, and performing transaction management.
Database Metadata
The program also includes methods to retrieve metadata information about the database:

Table Detail:
The TableDetail method retrieves and prints details about tables in the connected database.
Column Detail:
The ColumnDetail method retrieves and prints details about columns in the tables of the connected database.
Primary Key:
The Primary_Key method retrieves and prints details about primary keys in the tables of the connected database.
Foreign Key:
The Foreign_Key method retrieves and prints details about foreign keys in the tables of the connected database.

#Example of Usage:

public class Main {
public static void main(String[] args) {
// Connect to the database
Connection connection = Connect("YourDatabaseName", "YourUsername", "YourPassword");

        // Example: Insert a new book
        CreatedBook(connection, "100", "The Great Gatsby", "1", "50", "19.99", "1925");

        // Example: Read book details
        ReadBook(connection);

        // Example: Update stock quantity
        UpdateBook(connection, "101", "3");

        // Example: Delete a book
        DeleteBook(connection, "101");

        // Example: Insert into OrderDetails
        Insert_Book(connection, "201", "101", "3");

        // Example: Check the number of available books
        System.out.println(CheckNumberOfBooks(connection, "201", "10"));

        // Example: Order a book
        OrderBook(connection, "201", "101", "3");

        // Example: Display database metadata
        TableDetail(connection);
        ColumnDetail(connection);
        Primary_Key(connection);
        Foreign_Key(connection);
    }
}

