import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResourceOverload {
    
    public static void main(String[] args) {
      
        
        //Sobrecarrega a CPU
        Thread memoryThread = new Thread(ResourceOverload::overloadMemory);
        Thread diskThread = new Thread(ResourceOverload::overloadDisk);
        int cpuThreadsCount = Runtime.getRuntime().availableProcessors(); 
        
        List<Thread> cpuThreads = new ArrayList<>();
        for (int i = 0; i < cpuThreadsCount * 2; i++) { 
            Thread cpuThread = new Thread(ResourceOverload::overloadCPU);
            cpuThreads.add(cpuThread);
        }

        memoryThread.start();
        diskThread.start();
        cpuThreads.forEach(Thread::start);

        try {
            Thread.sleep(10000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        performMathOperations();
    }

    private static void overloadMemory() {
        // Sobrecarga de memória
        List<byte[]> memoryLoad = new ArrayList<>();
        try {
            while (true) {
                byte[] block = new byte[10 * 1024 * 1024]; // 10MB
                memoryLoad.add(block);
                System.out.println("Alocando memória: " + (memoryLoad.size() * 10) + "MB");
                Thread.sleep(100); // Pausa para desacelerar a alocação
            }
        } catch (OutOfMemoryError e) {
            System.err.println("Memória esgotada!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void overloadDisk() {
        //Sobrecarrega o disco
        try {
            File file = new File("/tmp/diskload.txt");
            FileOutputStream fos = new FileOutputStream(file);
            byte[] data = new byte[1024 * 1024]; 
            for (int i = 0; i < data.length; i++) {
                data[i] = 'A';
            }
            for (int i = 0; i < 1024; i++) { 
                fos.write(data);
                System.out.println("Gravando no disco: " + (i + 1) + "MB");
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void overloadCPU() {
        // Sobrecarga de CPU
        try {
            while (true) {
                for (int i = 2; i < Integer.MAX_VALUE; i++) {
                    isPrime(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    private static void performMathOperations() {
        try {
            while (true) {
                double a = 12345.6789;
                double b = 98765.4321;
                double sum = a + b;
                double difference = b - a;
                double product = a * b;
                double quotient = b / a;

                if (sum != 111111.1110 || difference != 86419.7532 || product != 1219326311.2694 || quotient != 8.0000494993) {
                    System.err.println("Erro nos cálculos matemáticos!");
                    break;
                } else {
                    System.out.println("Cálculos matemáticos corretos: sum = " + sum + ", difference = " + difference + ", product = " + product + ", quotient = " + quotient);
                }
                Thread.sleep(500); // Pausa para desacelerar as operações
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}