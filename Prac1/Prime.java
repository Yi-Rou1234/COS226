public class Prime extends Thread {
    private int[] array;
    private int start;
    private int end;

    public Prime(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = start; i <= end; i++) {
            if (i >= array.length) {
                break;
            }
            if (isPrime(array[i])) {
                System.out.println(Thread.currentThread().getName() + " [" + start + "-" + end + "]: " + array[i]);
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