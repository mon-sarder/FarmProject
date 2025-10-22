🚜 FarmProject: Inventory & Queue Management System

A comprehensive system for managing item inventory and a customer queue, available as both a classic Desktop App and a modern Web App. This repository serves as a showcase for building the same business logic using two different architectural patterns.

✨ Features

This repository contains two complete, independent applications:

🖥️ Desktop Application: A self-contained, native desktop application built with Java Swing. It provides a classic graphical user interface with tabs for inventory and queue management.

🌐 Web Application: A modern, client-server application featuring a Java & Spring Boot backend API and a responsive React & TypeScript frontend that runs in any web browser.

Core Logic: Both applications share the same core functionality for tracking product stock, managing a customer line, and searching records.

🛠️ Built With

Desktop App

Web App

📋 Prerequisites

To build and run these applications, you will need the following software installed:

Java Development Kit (JDK) 17 or higher.

Apache Maven.

Node.js and npm (for the web app frontend).

🚀 Getting Started

Instructions on how to get a local copy up and running.

🖥️ Desktop App (/desktop-app)

Navigate to the desktop app directory:

cd desktop-app


Build the executable .jar file using Maven:

mvn clean package


Run the application:

java -jar target/desktop-app-1.0-SNAPSHOT.jar


🌐 Web App (/web-app)

You will need to run the backend and frontend in two separate terminal windows.

1. Backend Server (/web-app/backend)

In a new terminal, navigate to the backend directory:

cd web-app/backend


Start the Spring Boot API server:

mvn spring-boot:run


The API will be running on http://localhost:8080.

2. Frontend UI (/web-app/frontend)

In a second terminal, navigate to the frontend directory:

cd web-app/frontend


Install the necessary packages:

npm install


Start the development server:

npm run dev


Open your browser to http://localhost:5173 to view the application.

🌱 Future Roadmap

While both applications are functional, here are the most valuable features that could be added next.

⭐ Top Priority: Data Persistence

Desktop App: Serialize data to a local JSON file on close/save.

Web App: Integrate a database (like H2 or PostgreSQL) with the Spring Boot backend.

Desktop App Improvements:

Add a JMenuBar with explicit "Save" and "Load" actions.

Implement JOptionPane confirmation dialogs for destructive actions.

Add a status bar for non-intrusive user feedback.

Web App Improvements:

Backend: Write unit/integration tests and add Swagger UI for API documentation.

Frontend: Integrate a server-state library like TanStack Query to improve data handling and user experience.
