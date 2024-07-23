//Yi-Rou Hung
//u22561154
public class main {
    public static void main(String [] args){
        MRMWAtomic<Integer> reg = new MRMWAtomic<Integer>(10, 1);
        ReadWriter[] arr = new ReadWriter[10];
        for(int i = 0; i < 10; i++){
            arr[i] = new ReadWriter(reg);
        }
        for(int i = 0; i < 10; i++){
            arr[i].start();
        }
    }
}