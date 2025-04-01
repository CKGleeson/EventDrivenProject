package Model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Manages client-side network connections to the server using a singleton pattern.
 * Handles sending messages and receiving responses through a TCP socket connection.
 */
public class ClientConnection {
    /** The single instance of ClientConnection */
    private static ClientConnection instance;
    /** Network socket for server communication */
    private Socket socket;
    /** Output stream to send messages to server */
    private PrintWriter out;
    /** Input stream to receive server responses */
    private BufferedReader in;
    
    /** Server IP address for connection */
    private static final String SERVER_ADDRESS = "localhost";
    /** Server port number for connection */
    private static final int SERVER_PORT = 12345;
    
    /**
     * Private constructor establishing connection to server.
     * @throws Exception if connection fails or streams can't be initialized
     */
    private ClientConnection() throws Exception {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    
    /**
     * Gets singleton instance of ClientConnection.
     * @return The single ClientConnection instance
     * @throws Exception if initial connection attempt fails
     */
    public static ClientConnection getInstance() throws Exception {
        if (instance == null) {
            instance = new ClientConnection();
        }
        return instance;
    }
    
    /**
     * Sends a message to the server and returns the response.
     * @param message The message to send to the server
     * @return Server response as a String. For LOAD_CSV requests, returns CSV data.
     * @throws Exception if communication fails or connection breaks
     */
    public String sendMessage(String message) throws Exception {
        out.println(message);
        StringBuilder response = new StringBuilder();
        String line;
        
        if ("LOAD_CSV".equalsIgnoreCase(message)) {
            // Handle multi-line CSV response
            while ((line = in.readLine()) != null) {
                if (line.equals("END_CSV")) break;
                response.append(line).append("\n");
            }
        } else {
            // Handle single-line response
            line = in.readLine();
            if (line != null) {
                response.append(line);
            }
        }
        
        return response.toString().trim();
    }
    
    /**
     * Closes the connection to the server. Sends STOP command before closing.
     * Handles exceptions internally by printing stack traces.
     */
    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                out.println("STOP");
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}