//Yi-rou Hung, u22561154
public class main {
    public static void main(String[] args) {
        // task1();
        task2();
    }

    public static void task1(){
        int n = 0;
        int m = 40; 
        int numberOfThreads = 10; 

        int[] array = new int[m - n + 1];
        for (int i = 0; i < array.length; i++) {
            array[i] = n + i;
        }

        int numbersPerThread = (m - n + 1) / numberOfThreads;
        Thread[] threads = new Thread[numberOfThreads * 2];

        // Task 1 threads
        System.out.println("Task 1:");
        for (int i = 0; i < numberOfThreads; i++) {
            int start = n + i * numbersPerThread;
            int end = (i == numberOfThreads - 1) ? m : start + numbersPerThread - 1;

            Thread primeThread = new Prime(array, start, end);
            primeThread.setName("Thread-" + i);
            threads[i] = primeThread;
            primeThread.start();
        }

        // Wait for Task 1 threads to finish
        for (int i = 0; i < numberOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void task2(){
        int n = 1;
        int m = 100; 
        int numberOfThreads = 2; 

        int[] array = new int[m-n+1];           
        for (int i = 0; i < array.length; i++) {
            array[i] = n + i;
        }

        Thread[] threads = new Thread[numberOfThreads];
        SharedCounter counter = new SharedCounter();
        PetersonLock lock = new PetersonLock();

        // Task 2 threads
        System.out.println("Task 2:");
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Task2(array, lock, counter, n, m);
            threads[i].setName("Thread-" + i);
            threads[i].start();
        }

        // Wait for Task 2 threads to finish
        for (int i = 0; i < numberOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
