public class Main {
    public static void main(String[] args) {
        System.out.println("TASK 1:");
	    task1();
        System.out.println("TASK 2:");
        task2();
    }
    public static void task1()
    {
        MyThread[] threads = new MyThread[5];
        // Initialize FilterLock with 5 threads
        Filter filterLock = new Filter(5); 
        SharedResources CriticalSection = new SharedResources(filterLock);

        for(int i = 0; i < 5; i++)
            threads[i] = new MyThread(CriticalSection);

        for(MyThread t : threads)
            t.start();

        //ensure all the threads end before we run task 2
        for(MyThread t : threads){
            try{
                t.join();
            }
            catch(InterruptedException e){}
        }
    }

    public static void task2()
    {
        int numThreads = 5;
        Bakery bakeryLock = new Bakery(numThreads);
        for (int i = 0; i < numThreads; i++) {
            SharedResources sharedResources = new SharedResources(bakeryLock);
            MyThread thread = new MyThread(sharedResources);
            thread.start();
        }
        
    }
}
