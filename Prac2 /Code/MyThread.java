public class MyThread extends Thread {
    SharedResources criticalSection;

	public MyThread(SharedResources CrSection ){
		criticalSection = CrSection;
	}

	@Override
	public void run()
	{
        for (int i = 0; i < 2 ; i++){
            criticalSection.access();
        }
    }	
}
