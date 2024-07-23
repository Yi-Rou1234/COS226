import java.util.concurrent.atomic.AtomicReference;

class Node<T> {
    T value;
    AtomicReference<Node<T>> next;

    public Node(T value) {
        this.value = value;
        this.next = new AtomicReference<>(null);
    }
}
