public class Backoff {
    private int minDelay;
    private final int maxDelay;

    public Backoff(int minDelay, int maxDelay) {
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
    }

    public void backoff() throws InterruptedException {
        // Exponential backoff strategy, causes the IN and OUT to interleave
        int delay = minDelay;
        minDelay = Math.min(maxDelay, 2 * minDelay); 
        Thread.sleep(delay);
    }
}
