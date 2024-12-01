import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Api {
	
	private static final int PORT = 8090; // Choose a port

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("API server started on port " + PORT);

            // Sample data
            Map<String, String> data = new HashMap<>();
            data.put("hello", "world");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");

                try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     Scanner in = new Scanner(clientSocket.getInputStream())) {

                    String request = in.nextLine();
                    System.out.println("Request: " + request);

                    String response = processRequest(request, data);
                    System.out.println("Response: " + response);

                    out.println(response);
                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                } finally {
                    clientSocket.close();
                    System.out.println("Client disconnected");
                }
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    private static String processRequest(String request, Map<String, String> data) {
        String[] parts = request.split("\\?");
        String path = parts[0];
        Map<String, String> params = new HashMap<>();
        if (parts.length > 1) {
            String[] queryParams = parts[1].split("&");
            for (String queryParam : queryParams) {
                String[] keyValue = queryParam.split("=");
                params.put(keyValue[0], keyValue[1]);
            }
        }

        if (path.equals("/get")) {
            String key = params.getOrDefault("key", "");
            if (data.containsKey(key)) {
                return data.get(key);
            } else {
                return "Key not found";
            }
        } else if (path.equals("/set")) {
            String key = params.getOrDefault("key", "");
            String value = params.getOrDefault("value", "");
            data.put(key, value);
            return "Key set successfully";
        }

        return "Invalid request";
    }

	 

}