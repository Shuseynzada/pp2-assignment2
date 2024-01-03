# Movie Database Management System

## Overview
The Movie Database Management System is a comprehensive application designed to manage movie data. It allows users to add new movies, update existing information, retrieve movies based on specific criteria, and list all movies in the database. 

## Features
- **Add Movies to the Database**: Users can add new movies to the database with details such as title, director, release year, and running time.
- **Add Movies to the Watchlist**: Users can add movies to the watchlist from the list of general movies.
- **Update Movies**: The system allows for updating the movie list, ensuring that the latest information is always stored.
- **Retrieve Movies**: Users can retrieve movies based on their indices in the database.
- **List All Movies**: The system can display all movies currently stored in the database.
- **Sort Movies**: Users can sort movies by title, director, release year, and running time.
- **Filter Movies**: Users can filter the list of movies based on the title, director, minimum and maximum release year and running time.
- **User Authentication**: Includes user registration and login functionality, with password validation.

## How to Use

### Adding a Movie
To add a movie, provide the movie's title, director, release year, and running time. The system will assign a unique index to each new movie.

### Updating the Movie File
The movie file can be updated to reflect the latest changes made to the movie list.

### Retrieving Movies by Index
Retrieve specific movies by providing their indices.

### Listing All Movies
The system can print out the details of all movies in the database.

### User Registration and Login
Users can register with a username and password, and log in to the system. Passwords are validated for length, digits, and uppercase letters.

## Technical Details

### Classes and Packages
- **Movie**: Represents a movie object.
- **User**: Manages user information and authentication.
- **MovieDatabase**: Handles operations related to movie data storage and retrieval.
- **Exceptions**: Includes custom exceptions for error handling.

### Exception Handling
The system uses custom exceptions to handle various errors.
- **IncorrectPassword**: During login, password does not match with associated user.
- **UsernameAlreadyExists**: During sign up, the username already exists.
- **UserNotFound**: During login, there were not such user in the database.
- **InvalidUsername**: If the username blank is empty.
- **InvalidPasswordDigit**: During sign up, password does not contain a digit.
- **InvalidPasswordLength**: During sign up, password is less than 6 characters.
- **InvalidPasswordUppercase**: During sign up, password does not contain an uppercase letter.0
- **InvalidRunningTime**: While adding the movie to the system, running time is negative.
- **InvalidReleaseYear**: While adding the movie to the system, release year was not in the range 1885 - 2023.

## Getting Started
- Clone the repository to your local machine.
- Compile and run the application.
- Follow the on-screen prompts to interact with the system.
