//Yi-Rou Hung
//u22561154
public class ReadWriter extends Thread {
    MRMWAtomic<Integer> register;
    public ReadWriter(MRMWAtomic<Integer> register){
        this.register = register;
    }

    public void run(){
        int me = Integer.parseInt(Thread.currentThread().getName().substring(Thread.currentThread().getName().indexOf("-")+1));
        System.out.println("(reader) "+Thread.currentThread().getName()+" : "+ register.read());
        try{
            Thread.sleep((long) (Math.random()*2000));
        }
        catch(InterruptedException e){}             
        register.write(me);   
        System.out.println("(writer) "+Thread.currentThread().getName()+" : "+ me);
        try{
            Thread.sleep((long) (Math.random()*200));
        }
        catch(InterruptedException e){}     
        System.out.println("(reader) "+Thread.currentThread().getName()+" : "+ register.read());
    }
}
