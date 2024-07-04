# Grocify Grocery App

Grocify is a native Android application that facilitates grocery shopping through two distinct apps: one for users and another for shopkeepers or supermarket owners. This project aims to provide a seamless and efficient grocery shopping experience with real-time features and a clean code structure.

## Project Description

### User App
- **Order Groceries**: Users can order groceries and track their order status in real-time.
- **Search and Browse**: Users can search for grocery items and browse products by category.

### Admin App
- **Manage Products**: Shopkeepers can add, update, and remove products, including uploading images.
- **Order Management**: Shopkeepers can view and update the status of orders in real-time.
- **Real-time Updates**: All changes, including order status updates and product management, are reflected in real-time.

## Tech Stack
- **Language**: Kotlin
- **Authentication**: Firebase Phone Authentication
- **Architecture**: MVVM (Model-View-ViewModel)
- **Navigation**: Android Navigation Component
- **Database**: Firebase Realtime Database, Room Database
- **Coding Practices**: Clean Code principles

## Features
- **OTP Authentication**: Secure login using Firebase phone authentication.
- **Real-time Database**: Firebase Realtime Database for instant updates.
- **Room Database**: Local storage for offline capabilities.
- **Product Management**: Add, update, and remove products with images.
- **Order Tracking**: Real-time order status updates for users.

## Installation

### Prerequisites
- Android Studio installed
- Firebase project setup with authentication and database

### Steps
1. **Clone the Repository**
   ```sh
   git clone https://github.com/abhinavraj-code/GrocifyGroceryApp.git
   cd grocify

2. **Open the Project in Android Studio**
   - Open Android Studio.
   - Click on "File" -> "Open" and navigate to the `GrocifyGroceryApp` directory where you cloned the repository. Select this directory to open the project in Android Studio.
   - Android Studio will take a moment to index the project and set up the environment.

3. **Configure Firebase**
   - Add your `google-services.json` file to the `app` directory. This file can be downloaded from your Firebase project settings.
   - Enable Phone Authentication in your Firebase console:
        - Go to the Firebase Console.
        - Select your project.
        - Click on "Authentication" in the left menu.
        - Click on the "Sign-in method" tab.
        - Enable "Phone".
   - Set up Firebase Realtime Database rules:
        - Go to the Firebase Console.
        - Select your project.
        - Click on "Database" in the left menu.
        - Click on the "Realtime Database" tab.
        - Click on "Rules" and configure your database rules as needed.
4. **Build the Project**
   - Sync the project with Gradle files by clicking on the "Sync Project with Gradle Files" button in the toolbar or by selecting "File" -> "Sync Project with Gradle Files".
   - Build and run the app on an emulator or a physical device by clicking on the "Run" button or selecting "Run" -> "Run 'app'" from the menu.
  

## Usage


### User App
   - **Login**: Users can log in using their phone number via OTP authentication.
   - **Browse Products**: Users can search for products or browse by category.
   - **Place Orders**: Add products to the cart and place an order.
   - **Track Orders**: View and track the status of placed orders.

### Admin App (Owner)
- **Login**: Admin can log in using their phone number via OTP authentication.
- **Manage Products**: Add, update, or remove products with images.
- **Order Management**: View and update the status of customer orders in real-time.


## Demo Video
https://github.com/abhinavraj-code/GrocifyGroceryApp/assets/72371963/64c2b39b-903e-47b9-9340-95c3e20217cd


### Contributions are welcome! Please fork the repository and submit a pull request with your changes...

