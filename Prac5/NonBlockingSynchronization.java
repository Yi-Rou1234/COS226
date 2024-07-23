import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicMarkableReference;

class NonBlockingSynchronization {
    private final AtomicReference<Queue<Person>> galleryQueueRef = new AtomicReference<>(new LinkedList<>());

    public void enterGallery(Person person) {
        boolean added = false;
        Queue<Person> galleryQueue;
        Queue<Person> newGalleryQueue;
        
        while (!added) {
            galleryQueue = galleryQueueRef.get();
            newGalleryQueue = new LinkedList<>(galleryQueue);
            newGalleryQueue.add(person);
            added = galleryQueueRef.compareAndSet(galleryQueue, newGalleryQueue);
        }

        System.out.println(Thread.currentThread().getName() + ": ADD (" + person + ")");
    }

    public void exitGallery(Person person) {
        boolean removed = false;
        Queue<Person> galleryQueue;
        Queue<Person> newGalleryQueue;
        
        while (!removed) {
            galleryQueue = galleryQueueRef.get();
            newGalleryQueue = new LinkedList<>(galleryQueue);
            newGalleryQueue.remove(person);
            removed = galleryQueueRef.compareAndSet(galleryQueue, newGalleryQueue);
        }

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

class NBSecurityPersonnel extends Thread {
    private final NonBlockingSynchronization gallery;
    private final int accessPoint;
    private Random random = new Random();

    public NBSecurityPersonnel(NonBlockingSynchronization gallery, int accessPoint) {
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