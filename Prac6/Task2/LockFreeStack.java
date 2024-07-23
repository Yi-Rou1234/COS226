import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack<T> {
    private final AtomicReference<Node<T>> top = new AtomicReference<>(null);
    private static final int MIN_DELAY = 100; 
    private static final int MAX_DELAY = 100; 
    private final Backoff backoff = new Backoff(MIN_DELAY, MAX_DELAY);

    protected boolean tryPush(Node<T> node) {
        Node<T> oldTop = top.get();
        node.next = oldTop;
        return top.compareAndSet(oldTop, node);
    }

    public void push(T value) {
        Node<T> node = new Node<>(value);
        while (true) {
            if (tryPush(node)) {
                return;
            } else {
                try {
                    backoff.backoff();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    protected Node<T> tryPop() throws EmptyException {
        Node<T> oldTop = top.get();
        if (oldTop == null) {
            throw new EmptyException();
        }
        Node<T> newTop = oldTop.next;
        if (top.compareAndSet(oldTop, newTop)) {
            return oldTop;
        } else {
            return null;
        }
    }

    public T pop() throws EmptyException {
        while (true) {
            Node<T> returnNode = tryPop();
            if (returnNode != null) {
                return returnNode.value;
            } else {
                try {
                    backoff.backoff();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
