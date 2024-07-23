public class GallerySimulation {
    public static void main(String[] args) {
        // task1();
        // task2();
        // task3();
        task4();
    }
    
    public static void task1()
    {
        FineGrainedSynchronization sync = new FineGrainedSynchronization();
        for (int i = 1; i <= 5; i++) {
            SecurityPersonnel personnel = new SecurityPersonnel(sync, i);
            personnel.start();
        }
    }

    public static void task2()
    {
        OptimisticSynchronization sync = new OptimisticSynchronization();
        for (int i = 1; i <= 5; i++) {
            OSecurityPersonnel personnel = new OSecurityPersonnel(sync, i);
            personnel.start();
        }
    }

    public static void task3()
    {
        LazySynchronization sync = new LazySynchronization();
        for (int i = 1; i <= 5; i++) {
            LSecurityPersonnel personnel = new LSecurityPersonnel(sync, i);
            personnel.start();
        }
    }

    public static void task4()
    {
        NonBlockingSynchronization sync = new NonBlockingSynchronization();
        for (int i = 1; i <= 5; i++) {
            NBSecurityPersonnel personnel = new NBSecurityPersonnel(sync, i);
            personnel.start();
        }
    }
}