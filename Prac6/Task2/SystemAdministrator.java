import java.util.Random;

public class SystemAdministrator implements Runnable {
    private final LockFreeStack<Job> centralDatabase;
    private final Random random = new Random();
    private static final int MAX_JOBS_TO_PROCESS = 10;

    private int jobsPrinted = 0;

    public SystemAdministrator(LockFreeStack<Job> centralDatabase) {
        this.centralDatabase = centralDatabase;
    }

    @Override
    public void run() {
        while (jobsPrinted < MAX_JOBS_TO_PROCESS) {
            try {
                Job job = centralDatabase.pop();

                int approvalNumber = random.nextInt(24) + 1;
                String approvalStatus = (job.getHours() < approvalNumber) ? "Approved" : "Disapproved";
                job.setApprovalStatus(approvalStatus);

                System.out.println("(OUT) [" + Thread.currentThread().getName() + "] [Job-" + job.getJobNumber() + " " + job.getHours() + " " + job.getApprovalStatus() + "]");
                jobsPrinted++;
            } catch (EmptyException e) {
                try {
                    Thread.sleep(100); 
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

