//Yi-Rou Hung
//u22561154
public class MRSWAtomic<T> implements Register<T> {
    ThreadLocal<Long> LastValue;
    private StampedValue<T>[][] table_a;
    public MRSWAtomic(T init, int readers){
        LastValue = new ThreadLocal<Long>(){
            protected Long initialValue(){
                return 0L;
            };
        };
        table_a = (StampedValue<T>[][]) new StampedValue[readers][readers];
        StampedValue<T> value = new StampedValue<T>(init);
        for(int i = 0; i<readers; i++){
            for(int j = 0; j<readers; j++){
                table_a[i][j] = value;
            }
        }
    }
    public T read(){
        int me = (int) Thread.currentThread().getId();
        StampedValue<T> value = table_a[me][me];
        for(int i = 0; i<table_a.length; i++){
            if(i != me){
                value = StampedValue.max(value, table_a[i][me]);
            }
        }
        for(int i = 0; i<table_a.length; i++){
            if(i==me){continue;}
            table_a[me][i] = value;
        }
        return value.value;
    }
    public void write(T v){
        long stamp = LastValue.get() + 1;
        LastValue.set(stamp);
        StampedValue<T> value = new StampedValue<T>(stamp, v);
        for(int i = 0; i<table_a.length; i++){
            table_a[i][i] = value;
        }
    }    
}
