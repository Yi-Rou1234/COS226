import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

// Name:Yi-Rou Hung
// Student Number: 22561154

public class Filter implements Lock
{

	public void lockInterruptibly() throws InterruptedException
	{
		throw new UnsupportedOperationException();
	}

	public boolean tryLock()
	{
		throw new UnsupportedOperationException();
	}

	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException
	{
		throw new UnsupportedOperationException();
	}

	public Condition newCondition()
	{
		throw new UnsupportedOperationException();
	}

	static class ThreadID {
        private static volatile int next = 0;
        private static ThreadLocal<Integer> threadID = ThreadLocal.withInitial(() -> next++);

        public static int get() {
            return threadID.get();
        }
    }

	private volatile int[] level;
	private volatile int[] victim;
	int n;

	public Filter(int n){
		this.n = n;
		level = new int[n];
		victim = new int[n];
		for (int i = 0; i<n ; i++) {
			level[i] = 0;
		}
	}

	public void lock() {
		int me = ThreadID.get();
		
		for (int i =1 ; i < n ; i++){
			System.out.println("Thread-" + me + ": level[" + me + "] = " + i + ", victim[" + i + "] = " + me);
			//attempt level one
			level[me] = i;
			victim[i] = me;
			//spin while confilcts exist
			// the loop spins until there is no k for which the condition holds.
			for (int k = 0; k < n ; k++){
				while ((k != me ) && (level[k] >= i && victim[i] == me)) {
					//spin wait
			   }
			}
		}
	}

	public void unlock() {
		// String threadName = Thread.currentThread().getName();
        // int startIndex = threadName.indexOf('-');
        // int me = Integer.parseInt(threadName.substring(startIndex + 1));
		int me = ThreadID.get();
		level[me] = 0;
	}
}
