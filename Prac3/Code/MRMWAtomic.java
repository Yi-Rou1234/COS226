//Yi-Rou Hung
//u22561154
public class MRMWAtomic<T> implements Register<T> {
    private StampedValue<T>[] table_a;
    public MRMWAtomic(int capacity, T init) {
        table_a = (StampedValue<T>[]) new StampedValue[capacity];
        StampedValue<T> value = new StampedValue<T>(init);
        for (int i = 0; i < table_a.length; i++) {
            table_a[i] = value;
        }
    }
    public void write(T value){
        int me = (int) Thread.currentThread().getId()%table_a.length;
        StampedValue<T> max = StampedValue.MIN_VALUE;
        for (int i = 0; i < table_a.length; i++) {
            max = StampedValue.max(max, table_a[i]);
        }
        table_a[me] = new StampedValue(max.stamp+1,value);
    }
    public T read(){
        StampedValue<T> max = StampedValue.MIN_VALUE;
        for (int i = 0; i < table_a.length; i++) {
            max = StampedValue.max(max, table_a[i]);
        }
        return max.value;
    }
    
}
