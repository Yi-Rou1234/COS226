import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class FineGrainedSynchronization {
    private final Queue<Person> galleryQueue = new LinkedList<>();
    private final Lock queueLock = new ReentrantLock();

    public void enterGallery(Person person) {
        try {
            queueLock.lock();
            galleryQueue.add(person);
            System.out.println(Thread.currentThread().getName() + ": ADD (" + person + ")");
        } finally {
            queueLock.unlock();
        }
    }

    public void exitGallery(Person person) {
        try {
            queueLock.lock();
            galleryQueue.remove(person);
            System.out.println(Thread.currentThread().getName() + ": " + galleryQueue);
        } finally {
            queueLock.unlock();
        }
    }
}

class Person {
    private final String name;
    private int timeLeft;

    public Person(String name, int timeLeft) {
        this.name = name;
        this.timeLeft = timeLeft;
    }

    public void decrementTime() {
        timeLeft -= 200;
    }

    @Override
    public String toString() {
        return "(" + name + ", " + timeLeft + "ms)";
    }
}

class SecurityPersonnel extends Thread {
    private final FineGrainedSynchronization gallery;
    private final int accessPoint;
    private Random random = new Random();

    public SecurityPersonnel(FineGrainedSynchronization gallery, int accessPoint) {
        this.gallery = gallery;
        this.accessPoint = accessPoint;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            int randomTime = 100 + random.nextInt(901);
            Person person = new Person("P-" + i, randomTime);

            gallery.enterGallery(person);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            person.decrementTime();

            gallery.exitGallery(person);
        }
    }
}
