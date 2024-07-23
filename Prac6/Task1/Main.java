public class Main {
    public static void main(String[] args) {
        LockFreeQueue<Job> jobQueue = new LockFreeQueue<>();

        for (int i = 1; i <= 4; i++) {
            Thread developer = new Thread(() -> {
                for (int j = 1; j <= 3; j++) {
                    Job job = new Job(Thread.currentThread().getName(), j);
                    jobQueue.enqueue(job);
                    System.out.println("(IN) [" + Thread.currentThread().getName() + "] Job " + job.getJobNumber() + " " + job.getHours() + " hours");
                }
                // Add a stop job when the developer is done
                Job stopJob = new Job(Thread.currentThread().getName(), -1);
                jobQueue.enqueue(stopJob);
            });
            developer.start();
        }

        // Create system administrators
        for (int i = 1; i <= 2; i++) {
            Thread systemAdmin = new Thread(() -> {
                while (true) {
                    Job job = jobQueue.dequeue();
                    if (job == null) {
                        continue;
                    }
                    if (job.getJobNumber() == -1) {
                        // This is the stop job, terminate the thread
                        break;
                    }
                    int randomApproval = (int) (Math.random() * 24) + 1;
                    String approvalStatus = (job.getHours() <= randomApproval) ? "Approved" : "Disapproved";
                    System.out.println("(OUT) [" + Thread.currentThread().getName() + "] Job " + job.getJobNumber() + " " + job.getHours() + " hours " + approvalStatus);
                }
            });
            systemAdmin.start();
        }
    }
}
