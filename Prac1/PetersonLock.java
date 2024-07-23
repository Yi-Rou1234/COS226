import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

class PetersonLock implements Lock {
    private boolean[] flag = new boolean[2];
    //victim is volatile, to allow mutual exclusion
    private volatile int victim;

    public void lock() {
        int i = ThreadID.get();
        int j = 1 - i;
        flag[i] = true;
        victim = i;
        while (flag[j] && victim == i) {
            // Wait
        }
    }

    public void unlock() {
        int i = ThreadID.get();
        flag[i] = false;
    }

    // Rest of the methods from the Lock interface
    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean tryLock() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("Not implemented");
    }

    static class ThreadID {
        private static volatile int next = 0;
        private static ThreadLocal<Integer> threadID = ThreadLocal.withInitial(() -> next++);

        public static int get() {
            return threadID.get();
        }
    }
}

class Task2 extends Thread {
    private int[] array;
    private Lock lock;
    private SharedCounter counter;
    private int start;
    private int end;

    public Task2(int[] array, PetersonLock lock, SharedCounter counter,  int start, int end) {
        this.array = array;
        this.lock = lock;
        this.counter = counter;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        int index = 0;
        //whenever we lock we also Increment
        lock.lock();
        try{
            index = counter.getAndIncrement();
        }
        finally{
            lock.unlock();
        }
        while (index < array.length) {
                int num = array[index];
                
                if (isPrime(num)) {
                    //Output 
                    System.out.println(Thread.currentThread().getName() + " [" + start + "-" + end + "]: " + array[index]);
                }
                try{
                    index = counter.getAndIncrement();
                }
                finally{
                    lock.unlock();
                }
        }
    }

    private boolean isPrime(int num) {
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
}

class SharedCounter {
    private int value = 0;
    //access to counter , no lock stored here
    public synchronized int getAndIncrement() {
        int current = value;
        value++;
        return current;
    }
}