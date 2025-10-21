FarmProject: Inventory & Queue Management

This repository contains two complete, independent applications for managing a customer queue and item inventory: a classic desktop application and a modern web application.

1. Desktop Application (desktop-app)

A self-contained desktop application built with Java Swing and Maven. It provides a native GUI for managing inventory and a customer queue.

To Run: Open the desktop-app folder in your IDE and run the main method in src/main/java/app/MainGUI.java.

2. Web Application (web-app)

A modern client-server application.

Backend (web-app/backend): A Java Spring Boot application that provides a REST API for all inventory and queue operations.

Frontend (web-app/frontend): A React + TypeScript single-page application that provides the user interface in a web browser.

To Run:

Run the main method in the Spring Boot Application.java to start the server.

In a terminal, navigate to the web-app/frontend directory, run npm install, and then npm run dev to start the frontend.

High-Impact Next Steps & Improvements

Here are the most valuable features to consider adding next to enhance your applications.

⭐ Top Priority: Data Persistence

Currently, both applications store their data in memory, meaning all inventory and queue information is lost when the application is closed. Implementing persistence is the most critical next step.

For the Desktop App:

Suggestion: Save the inventory and queue lists to a local file (like inventory.json) when the application closes and load it back on startup.

Tools: You could use a simple library like Google's Gson or Jackson to easily convert your Java objects to JSON and back.

For the Web App Backend:

Suggestion: Connect the StoreService to a database to persist data permanently.

Tools: Start with an easy-to-use in-memory database like H2 for development. Then, you can graduate to a production-grade database like PostgreSQL using Spring Data JPA. This is a standard and powerful way to handle data in Spring Boot.

Other Key Improvements

For the Desktop App (desktop-app):

File Menu: Add a "File" menu to the menu bar with explicit "Save" and "Load" options, giving the user control over data persistence.

Confirmation Dialogs: Add JOptionPane.showConfirmDialog pop-ups before destructive actions like deleting an item or clearing the entire queue to prevent accidental data loss.

Status Bar: Instead of showing a pop-up for every small action, add a status bar at the bottom of the window to display messages like "Item 'Apples' added successfully."

For the Web App Backend (web-app/backend):

Unit & Integration Testing: Write tests for your StoreService (unit tests) and ApiController (integration tests) to ensure your API is reliable and bug-free.

API Documentation: Add a tool like SpringDoc (Swagger) to automatically generate interactive documentation for your REST API.

Global Error Handling: Implement a @ControllerAdvice to create a centralized exception handler for cleaner error responses to the frontend.

For the Web App Frontend (web-app/frontend):

Routing: Add react-router-dom to the project. Even if you only have one page now, this sets you up to easily add more pages in the future (e.g., a settings page, a detailed view for an item, etc.).

Code Quality Tools: Integrate ESLint and Prettier to automatically enforce consistent code style and catch potential errors early.

Environment Variables: Create a .env file in the frontend directory to manage the API URL (VITE_API_BASE=http://localhost:8080), which keeps sensitive or environment-specific variables out of your source code.
