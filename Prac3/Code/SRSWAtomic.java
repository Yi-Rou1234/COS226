//Yi-Rou Hung
//u22561154
public class SRSWAtomic<T> implements Register<T> {
    ThreadLocal<Long> LastValue;
    ThreadLocal<StampedValue<T>> ReadLast;
    StampedValue<T> value_s;
    public SRSWAtomic(T init){
        value_s = new StampedValue<T>(init);
        LastValue = new ThreadLocal<Long>(){
            protected Long initialValue(){
                return 0L;
            }
        };
        ReadLast = new ThreadLocal<StampedValue<T>>(){
            protected StampedValue<T> initialValue(){
                return value_s;
            }
        };
    }
    public T read(){
        StampedValue<T> value = value_s;
        StampedValue<T> last = ReadLast.get();
        StampedValue<T> result = StampedValue.max(value, last);
        ReadLast.set(result);
        return result.value;
    }
    public void write(T v){
        long stamp = LastValue.get()+1;
        value_s = new StampedValue<T>(stamp, v);
        LastValue.set(stamp);
    }
    
}
