import java.util.concurrent.locks.Lock;

public class SharedResources {
    Lock l;

	public SharedResources(Lock l){
		this.l = l;
	}

	public void access(){
		// Implement the behavior of the thread when it enters the critical section.
        // Here, we'll acquire the Filter lock, access the shared resource at least twice,
        // and sleep for a random time between 200 and 1000 milliseconds each time.
		int threadId = Integer.parseInt(Thread.currentThread().getName().split("-")[1]);
        try {
            l.lock();
                // System.out.println("Thread-" + threadId + ": level[" + threadId + "] = " + i + ", victim[" + i + "] = " + threadId);
                int sleepTime = (2457 * threadId + 351) % 800 + 200;
                Thread.sleep(sleepTime);

            System.out.println("Thread-" + threadId + ": ---------------- DONE");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		finally{
			l.unlock();
		}
	}
}
