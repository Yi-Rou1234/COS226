import java.util.concurrent.atomic.AtomicReference;

public class MCSLock {
    private static class Node {
        volatile boolean locked = true;
        volatile Node next = null;
    }

    private final ThreadLocal<Node> myNode;
    private final ThreadLocal<Node> myPred;
    private final AtomicReference<Node> tail;

    public MCSLock() {
        myNode = ThreadLocal.withInitial(Node::new);
        myPred = ThreadLocal.withInitial(() -> null);
        tail = new AtomicReference<>(null);
    }

    public void lock() {
        Node node = myNode.get();
        node.locked = true;

        Node pred = tail.getAndSet(node);
        if (pred != null) {
            pred.next = node;
            while (node.locked) {
                // Spin-wait until the predecessor releases the lock
            }
        }
    }

    public void unlock() {
        Node node = myNode.get();
        node.locked = false;

        if (node.next == null) {
            if (tail.compareAndSet(node, null)) {
                return;
            }
            while (node.next == null) {
                // Spin-wait until the next node is set
            }
        }
        node.next.locked = false;
        node.next = null;
    }
}
