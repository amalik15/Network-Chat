# Networked Chat Application

This project is a networked chat application that allows multiple clients to connect to a server and exchange messages.

## Features

-   **Client-Server Architecture**: The application follows a client-server model where the server hosts the chat and clients connect to it.
-   **GUI Interface**: Both the client and server have graphical user interfaces built with Java Swing.
-   **Multi-threading**: The server can handle multiple client connections simultaneously using multi-threading.

### Prerequisites

-   Java Development Kit (JDK) 1.8 or higher
-   An IDE like Eclipse or IntelliJ IDEA

### Compilation Instructions

To compile the project, open a terminal and navigate to the project's root directory. Then, run the following command:

```sh
javac -d bin src/*.java
```

### Run Instructions

To run the project, follow these steps:

1.  Open a terminal and navigate to the `bin` directory.
2.  Run the application use the following command:

        ```sh
        java MainGUI server
        ```

    One instance of the server needs to run. Use the information generated on the server GUI to pass it to the other client instances.

## Authors

-   amalik15
-   dqiao4

## License

This project is licensed under the MIT License.
