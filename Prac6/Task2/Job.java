import java.util.Random;

public class Job {
    private final String threadName;
    private final int jobNumber;
    private final int hours;
    private String approvalStatus;

    public Job(String threadName, int jobNumber) {
        this.threadName = threadName;
        this.jobNumber = jobNumber;
        this.hours = new Random().nextInt(24) + 1; // Random hours between 1-24
        this.approvalStatus = "Pending";
    }

    public String getThreadName() {
        return threadName;
    }

    public int getJobNumber() {
        return jobNumber;
    }

    public int getHours() {
        return hours;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String status) {
        this.approvalStatus = status;
    }
}
