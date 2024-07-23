import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Crud {
    private final Queue<Info> database = new LinkedList<>();
    private final Queue<Info> createQueue = new LinkedList<>();
    private final Queue<Boolean> readQueue = new LinkedList<>();
    private final Queue<Info> updateQueue = new LinkedList<>();
    private final Queue<Info> deleteQueue = new LinkedList<>();

    private final MCSLock createLock = new MCSLock();
    private final MCSLock readLock = new MCSLock();
    private final MCSLock updateLock = new MCSLock();
    private final MCSLock deleteLock = new MCSLock();

    public Crud() {
        String ids[] = {"u123", "u456", "u789", "u321", "u654", "u987", "u147", "u258", "u369", "u741", "u852", "u963"};
        String names[] = {"Thabo", "Luke", "James", "Lunga", "Ntando", "Scott", "Michael", "Ntati", "Lerato", "Niel", "Saeed", "Rebecca"};

        for (int i = 0; i < 20; i++) {
            readQueue.add(true);

            if (i < 12) createQueue.add(new Info(ids[i], names[i], 'c'));

            if (i < 4) updateQueue.add(new Info(ids[i + 1], names[i + 1], 'u'));
            if (i < 4) deleteQueue.add(new Info(ids[i + 2], names[i + 2], 'd'));

            if (i >= 9 && i < 12) {
                updateQueue.add(new Info(ids[i], names[i], 'u'));
                deleteQueue.add(new Info(ids[i], names[i], 'd'));
            }
        }
    }

    public void start() {
        int numThreads = 5; // Change the number of threads as needed
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new WorkerThread("Thread-" + i));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class WorkerThread implements Runnable {
        private final String threadName;

        public WorkerThread(String name) {
            this.threadName = name;
        }

        @Override
        public void run() {
            while (!createQueue.isEmpty() || !readQueue.isEmpty() || !updateQueue.isEmpty() || !deleteQueue.isEmpty()) {
                // Determine which operation to perform based on available queues
                boolean createOperation = !createQueue.isEmpty();
                boolean readOperation = !readQueue.isEmpty();
                boolean updateOperation = !updateQueue.isEmpty();
                boolean deleteOperation = !deleteQueue.isEmpty();

                if (createOperation) {
                    performCreateOperation();
                } else if (readOperation) {
                    performReadOperation();
                } else if (updateOperation) {
                    performUpdateOperation();
                } else if (deleteOperation) {
                    performDeleteOperation();
                } else {
                    // Sleep if no operations are available
                    System.out.println(threadName + " is sleeping.");
                    try {
                        Thread.sleep(new Random().nextInt(51) + 50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void performCreateOperation() {
            Info info = createQueue.poll();
            if (info != null) {
                createLock.lock();
                try {
                    database.add(info);
                    System.out.println(threadName + " CREATE success " + info.id);
                } finally {
                    createLock.unlock();
                }
                sleepRandom();
            }
        }

        private void performReadOperation() {
            readLock.lock();
            try {
                boolean success = readQueue.poll();
                if (success) {
                    System.out.println(threadName + " READ -----------------------");
                    for (Info info : database) {
                        System.out.println(info.id + " " + info.name + " " + info.practicals + " " + info.assignments);
                    }
                    System.out.println("-----------------------");
                } else {
                    System.out.println(threadName + " READ failed");
                }
            } finally {
                readLock.unlock();
            }
            sleepRandom();
        }

        private void performUpdateOperation() {
            Info info = updateQueue.poll();
            if (info != null) {
                updateLock.lock();
                try {
                    for (Info record : database) {
                        if (record.id.equals(info.id)) {
                            record.update(info.practicals, info.assignments);
                            System.out.println(threadName + " UPDATE success " + info.id);
                            break;
                        }
                    }
                } finally {
                    updateLock.unlock();
                }
                sleepRandom();
            }
        }
        

        private void performDeleteOperation() {
            Info info = deleteQueue.poll();
            if (info != null) {
                deleteLock.lock();
                try {
                    boolean deleted = false;
                    for (Info record : database) {
                        if (record.id.startsWith(info.id)) {
                            database.remove(record);
                            deleted = true;
                            System.out.println(threadName + " DELETE success " + info.id);
                            break;
                        }
                    }
                    if (!deleted) {
                        info.attempt++;
                        if (info.attempt <= 2) {
                            deleteQueue.add(info);
                            System.out.println(threadName + " DELETE failed " + info.id);
                        } else {
                            System.out.println(threadName + " DELETE exhausted " + info.id);
                        }
                    }
                } finally {
                    deleteLock.unlock();
                }
                sleepRandom();
            }
        }

        private void sleepRandom() {
            System.out.println(threadName + " is sleeping.");
            try {
                Thread.sleep(new Random().nextInt(51) + 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
