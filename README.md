# PharmaStock - Pharmacy Inventory Management System

PharmaStock is a desktop application I built for my Object Oriented Programming course at COMSATS University Islamabad. The idea was simple — pharmacies still manage their inventory manually or with very basic tools, so I wanted to build something that actually solves that problem while covering all the OOP concepts required for the project.

The application is built entirely in Java using JavaFX for the interface, and it handles everything from adding medicines and managing stock to generating customer bills and even answering inventory questions through an AI chatbot.

---

## What it does

When you open the app, you get a login screen. There are two types of users — Admin and regular User. Admin has full control over the system including saving data and deleting medicines. A regular user can add medicines and process sales but cannot delete records or save to file. This role-based system was one of the more interesting parts to implement.

Once logged in, the main window has two tabs. The first is Inventory Management where you can add new medicines, view the full inventory in a table, search by name, update stock, and sort alphabetically. The second tab is the Billing System where you can select medicines, add them to a cart, enter customer details, and generate a proper invoice receipt.

There is also a floating AI assistant button in the corner. Click it and a chat window opens where you can ask questions about your inventory in plain English — things like how many medicines do we have, which ones are running low, what is the price of a specific medicine. It uses the Groq API with an LLaMA model to answer based on your actual inventory data.

---

## How to run it

You need Java 21 or higher installed. Then just run:

```
java -jar PharmaStock.jar
```

If you want to open it in IntelliJ, clone the repo, add JavaFX 21 to your project libraries, and run `pharmastock.ui.MainApp` as the main class.

---

## Login details

- Admin account: username `admin`, password `admin123`
- User account: username `user`, password `user123`

---

## Project structure

The code is organized into four packages:

- `model` contains the core classes — Medication is an abstract parent class, and PrescriptionDrug and OverTheCounter extend it
- `service` has InventoryManager which handles all data operations including file saving and loading, and ChatBot which handles the AI API calls
- `exception` has a custom InsufficientStockException for stock validation
- `ui` has all the JavaFX windows — LoginWindow, MainWindow, ChatBotButton, and MainApp as the entry point

---

## OOP concepts used

The project covers pretty much everything from the course — abstract classes, inheritance, polymorphism, method overloading, encapsulation with getters and setters, the Comparable interface for sorting, custom exceptions, ArrayList for data storage, file handling with Scanner and PrintWriter, and the entire JavaFX side including lambda expressions and event-driven programming.

---

## Tech stack

- Java 21
- JavaFX 21
- Maven
- Groq API (LLaMA model)
- IntelliJ IDEA

---

## Developer
Muhammad Farhan  
Shadab Khan

COMSATS University Islamabad Wah Campus
BS Computer Science
OOP project 
