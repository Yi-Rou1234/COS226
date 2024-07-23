class Job {
    private String developerName;
    private int jobNumber;
    private int hours;

    public Job(String developerName, int jobNumber) {
        this.developerName = developerName;
        this.jobNumber = jobNumber;
        this.hours = (int) (Math.random() * 24) + 1;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public int getJobNumber() {
        return jobNumber;
    }

    public int getHours() {
        return hours;
    }
}
