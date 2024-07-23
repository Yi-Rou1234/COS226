import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

class OptimisticSynchronization {
    private final AtomicReference<Queue<Person>> galleryQueueRef = new AtomicReference<>(new LinkedList<>());

    public void enterGallery(Person person) {
        boolean successfulAdd = false;
        Queue<Person> galleryQueue;
        do {
            galleryQueue = galleryQueueRef.get();
            Queue<Person> newGalleryQueue = new LinkedList<>(galleryQueue);
            newGalleryQueue.add(person);
            successfulAdd = galleryQueueRef.compareAndSet(galleryQueue, newGalleryQueue);
        } while (!successfulAdd);

        System.out.println(Thread.currentThread().getName() + ": ADD (" + person + ")");
    }

    public void exitGallery(Person person) {
        boolean successfulRemove = false;
        Queue<Person> galleryQueue;
        do {
            galleryQueue = galleryQueueRef.get();
            Queue<Person> newGalleryQueue = new LinkedList<>(galleryQueue);
            newGalleryQueue.remove(person);
            successfulRemove = galleryQueueRef.compareAndSet(galleryQueue, newGalleryQueue);
        } while (!successfulRemove);

        System.out.println(Thread.currentThread().getName() + ": " + galleryQueueRef.get());
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

class OSecurityPersonnel extends Thread {
    private final OptimisticSynchronization sync;
    private final int accessPoint;
    private Random random = new Random();

    public OSecurityPersonnel(OptimisticSynchronization sync, int accessPoint) {
        this.sync = sync;
        this.accessPoint = accessPoint;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            int randomTime = 100 + random.nextInt(901);
            Person person = new Person("P-" + i, randomTime);

            sync.enterGallery(person);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            person.decrementTime();

            sync.exitGallery(person);
        }
    }
}
