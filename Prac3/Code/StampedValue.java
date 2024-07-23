//Yi-Rou Hung
//u22561154
class StampedValue<T> {
    public long stamp;
    public T value;

    public StampedValue(T init) {
        this.stamp = 0;
        this.value = init;
    }

    public StampedValue(long stamp, T value) {
        this.stamp = stamp;
        this.value = value;
    }

    public static StampedValue max(StampedValue x, StampedValue y) {
        return (x.stamp > y.stamp) ? x : y;
    }

    public static StampedValue MIN_VALUE = new StampedValue(null);
}
