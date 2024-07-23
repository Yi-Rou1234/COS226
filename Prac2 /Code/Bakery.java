import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

// Name:Yi-Rou Hung
// Student Number: 22561154

public class Bakery implements Lock
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

    
	private volatile boolean[] flag;
    private volatile int[] label;
    private int n; 

    static class ThreadID {
        private static volatile int next = 0;
        private static ThreadLocal<Integer> threadID = ThreadLocal.withInitial(() -> next++);

        public static int get() {
            return threadID.get();
        }
    }

    public Bakery(int n){
		this.n = n;
		flag = new boolean[n];
		label = new int[n];
		for (int i = 0; i<n ; i++) {
			label[i] = 0;
		}
	}

	@Override
    public void lock() {
        int me = ThreadID.get();
        flag[me] = true;
        label[me] = label() + 1;
        boolean exists = true;
        
        System.out.println(
            "{" + Thread.currentThread().getName() + "}" + ": flag[" + me + "] = " +
            flag[me] + "," + "label[" + me + "] = " + label[me]);

        while (exists){
            exists = false;
            for (int k = 0; k < n; k++) {
                if ((k != me) && (flag[k]) && checkExists(k, me))
                {
                    exists = true;
                    break;
                }
            }
        }
    }

	@Override
    public void unlock() {
        int me = ThreadID.get();
        flag[me] = false;
    }

    private int label() {
        int max = label[0];
        for (int i = 1; i < n; i++) {
            if (label[i] > max) {
                max = label[i];
            }
        }
        return max;
    }

    private boolean checkExists(int k, int me){
        if(label[k] < label[me]){
            return true;
        }
        else if(label[k] == label[me] && k < me){
            return true;
        }
        else{
            return false;
        }
    }
}
