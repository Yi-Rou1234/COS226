public class Main {
    public static void main(String[] args) {
        LockFreeStack<Job> centralDatabase = new LockFreeStack<>();

        int numOfDevelopers = 4; 
        int numOfAdmins = 2;     

        for (int i = 0; i < numOfDevelopers; i++) {
            new Thread(new Developer(centralDatabase, "Developer-" + i)).start();
        }

        for (int i = 0; i < numOfAdmins; i++) {
            new Thread(new SystemAdministrator(centralDatabase)).start();
        }
    }
}
