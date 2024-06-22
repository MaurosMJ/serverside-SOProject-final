/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package somonitoring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 *
 * @author 320167484
 */
public class SOMonitoringMain {
    
    public static void main(String[] args) {
        /**
        int port = 3000;  // Use the port that is mapped in Docker

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    Executors.newSingleThreadExecutor().execute(() -> handleClient(socket));
                } catch (IOException e) {
                    System.out.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        *  */
        
        
                int port = 3000; // Porta de comunicação

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado e aguardando conexões...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }
 /**
    private static void handleClient(Socket socket) {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            System.out.println("New client connected");

            while (true) {
                double cpuUsage = getCpuUsage();
                double memUsage = getMemoryUsage();
                double diskUsage = getDiskUsage();

                String message = String.format("CPU Usage: %.2f%%, Memory Usage: %.2f%%, Disk Usage: %.2f%%%n", cpuUsage, memUsage, diskUsage);
                out.println(message);

                // Esperar 5 segundos antes de verificar novamente
                Thread.sleep(5000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }   
    }
**/
   
    
    
    
    private static double getCpuUsage() throws IOException {
        String command = "top -bn1 | grep 'Cpu(s)'";
        String output = executeCommand(command);

        if (output != null && !output.isEmpty()) {
            String[] tokens = output.split(",");
            if (tokens.length > 3) {
                String cpuIdleStr = tokens[3].trim().split(" ")[0];
                try {
                    double cpuIdle = Double.parseDouble(cpuIdleStr);
                    return 100.0 - cpuIdle;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1; // Retornar -1 para indicar erro
    }

    private static double getMemoryUsage() throws IOException {
        String command = "free | grep Mem";
        String output = executeCommand(command);

        if (output != null && !output.isEmpty()) {
            String[] tokens = output.split("\\s+");
            if (tokens.length > 2) {
                try {
                    double totalMem = Double.parseDouble(tokens[1]);
                    double usedMem = Double.parseDouble(tokens[2]);
                    return (usedMem / totalMem) * 100.0;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1; // Retornar -1 para indicar erro
    }

    private static double getDiskUsage() throws IOException {
        String command = "df -h --total | grep total";
        String output = executeCommand(command);

        if (output != null && !output.isEmpty()) {
            String[] tokens = output.split("\\s+");
            if (tokens.length > 4) {
                String diskUsageStr = tokens[4].replace("%", "");
                try {
                    return Double.parseDouble(diskUsageStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1; // Retornar -1 para indicar erro
    }

    private static String executeCommand(String command) throws IOException {
        StringBuilder output = new StringBuilder();
        Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString().trim();
    }
}