public class Developer implements Runnable {
    private final LockFreeStack<Job> centralDatabase;
    private final String name;

    public Developer(LockFreeStack<Job> centralDatabase, String name) {
        this.centralDatabase = centralDatabase;
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            Job job = new Job(name, i);
            centralDatabase.push(job);
            System.out.println("(IN) [" + Thread.currentThread().getName() + "] [Job-" + job.getJobNumber() + " " + job.getHours() + "]");
        }
    }
}
