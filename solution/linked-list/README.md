# LinkedList

<p align="right">Last updated - 27.07.2026</p>

## Contents

- [Introduction to Linked List](#introduction-to-linkedlist)
- [Singly Linked List Implementation](#implementation---singly-linkedlist)
- [Doubly Linked List Implementation](#implementation---doubly-linkedlist)
- [Circular Linked List Implementation](#implementation---circular-linkedlist)
- [Java Collection Framework – LinkedList](#java-collection-framework---linkedlist)

## Introduction to LinkedList

A **Linked List** is a linear data structure where elements (called **Nodes**) are not stored at contiguous memory locations. Instead, each node contains two things:

1. **Data:** The actual value.
2. **Pointer/Reference:** A memory reference pointing to the next node in the sequence.

![](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYcne38avWS80hztRfyofcXMnP4ARpp2q0C_91l8bAoCH6UQESRpvUxfM&s=10)

As shown above, each node holds its value alongside the address of the next node (e.g., node containing `15` holds address `3600`). The entry point is the **Head**, and the chain ends when a node points to **NULL**.

### Why Linked List when we already have Arrays?

The short answer comes down to **memory layout** and **cost of insertion/deletion**.

| Feature                                  | Array / ArrayList                              | Linked List                                      |
| ---------------------------------------- | ---------------------------------------------- | ------------------------------------------------ |
| **Memory Allocation**                    | Contiguous block in memory                     | Non-contiguous (nodes can live anywhere in heap) |
| **Access Time**                          | **$O(1)$** via index (e.g., `arr[3]`)          | **$O(N)$** must traverse from head node by node  |
| **Insertion/Deletion at Head**           | **$O(N)$** (requires shifting all elements)    | **$O(1)$** (just re-link pointers)               |
| **Insertion/Deletion at Given Position** | **$O(N)$** (shifting elements)                 | **$O(1)$** after reaching the node               |
| **Resizing**                             | Costly (allocating a larger array and copying) | Dynamic (grows and shrinks naturally at runtime) |

### Advantages & Disadvantages

#### Advantages

- **Dynamic Sizing:** Allocates memory on the fly. No need to pre-allocate fixed capacity.
- **Efficient Insertions/Deletions:** Inserting or deleting at the front takes $O(1)$ time without shifting elements.

#### Disadvantages

- **No Random Access:** You cannot directly access the $k$-th element in $O(1)$ time.
- **Memory Overhead:** Every node spends extra bytes storing pointers/references.
- **Cache Unfriendliness:** Array elements sit next to each other, taking advantage of CPU L1/L2 cache locality. Linked list nodes are scattered across RAM, causing frequent CPU cache misses.

### Representing a Node in Java and C++

Here is how you write a basic `Node` class in Java and C++.

#### Java

```java
class Node {
    int val;
    Node next;

    // Constructors
    public Node(int data) {
        this.val = data;
        this.next = null;
    }

    public Node(int data, Node nextNode) {
        this.val = data;
        this.next = nextNode;
    }
}
```

#### C++

```cpp
class Node {
public:
    int val;
    Node* next;

    // Constructors
    Node(int data) {
        val = data;
        next = nullptr;
    }

    Node(int data, Node* nextNode) {
        val = data;
        next = nextNode;
    }
};
```

## Implementation - Singly LinkedList

Implementation of a **Singly Linked List** in Java, including `addFirst`, `addLast`, `addAt`, `deleteFirst`, `deleteLast`, `deleteAt`, `getLength`, and `print`.

```java
/**
 * Custom Singly Linked List Implementation in Java.
 */
public class SinglyLinkedList {

    private static class Node {
        int val;
        Node next;

        Node(int val) {
            this.val = val;
            this.next = null;
        }

        Node(int val, Node next) {
            this.val = val;
            this.next = next;
        }
    }

    private Node head;
    private int size;

    public SinglyLinkedList() {
        this.head = null;
        this.size = 0;
    }

    // Add Operation

    /**
     * Inserts a new node at the beginning of the list.
     * Time Complexity: O(1)
     */
    public void addFirst(int val) {
        Node newNode = new Node(val);
        newNode.next = head;
        head = newNode;
        size++;
    }

    /**
     * Inserts a new node at the end of the list.
     * Time Complexity: O(N)
     */
    public void addLast(int val) {
        if (head == null) {
            addFirst(val);
            return;
        }

        Node curr = head;
        while (curr.next != null) {
            curr = curr.next;
        }

        curr.next = new Node(val);
        size++;
    }

    /**
     * Inserts a new node at a specific 0-based index.
     * Time Complexity: O(N)
     */
    public void addAt(int index, int val) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index + " for size: " + size);
        }

        if (index == 0) {
            addFirst(val);
            return;
        }

        if (index == size) {
            addLast(val);
            return;
        }

        // Traverse to the node right before the desired index (index - 1)
        Node curr = head;
        for (int i = 0; i < index - 1; i++) {
            curr = curr.next;
        }

        Node newNode = new Node(val, curr.next);
        curr.next = newNode;
        size++;
    }

    // Delete Operation

    /**
     * Removes and returns the first element of the list.
     * Time Complexity: O(1)
     */
    public int deleteFirst() {
        if (head == null) {
            throw new IllegalStateException("Cannot delete from an empty list.");
        }

        int removedVal = head.val;
        head = head.next;
        size--;
        return removedVal;
    }

    /**
     * Removes and returns the last element of the list.
     * Time Complexity: O(N)
     */
    public int deleteLast() {
        if (head == null) {
            throw new IllegalStateException("Cannot delete from an empty list.");
        }

        if (size == 1) {
            return deleteFirst();
        }

        // Traverse to the second-to-last node (size - 2 index)
        Node curr = head;
        while (curr.next.next != null) {
            curr = curr.next;
        }

        int removedVal = curr.next.val;
        curr.next = null;
        size--;
        return removedVal;
    }

    /**
     * Removes and returns the element at a specific 0-based index.
     * Time Complexity: O(N)
     */
    public int deleteAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index + " for size: " + size);
        }

        if (index == 0) {
            return deleteFirst();
        }

        if (index == size - 1) {
            return deleteLast();
        }

        // Traverse to the node right before the target node (index - 1)
        Node curr = head;
        for (int i = 0; i < index - 1; i++) {
            curr = curr.next;
        }

        int removedVal = curr.next.val;
        curr.next = curr.next.next; // Bypass the deleted node
        size--;
        return removedVal;
    }

    // Utility Operation

    /**
     * Returns the current number of elements in the list.
     * Time Complexity: O(1)
     */
    public int getLength() {
        return this.size;
    }

    /**
     * Prints the entire list in readable format: [ 10 -> 20 -> 30 -> NULL ]
     * Time Complexity: O(N)
     */
    public void print() {
        if (head == null) {
            System.out.println("[] (Empty List)");
            return;
        }

        StringBuilder sb = new StringBuilder("[ ");
        Node curr = head;
        while (curr != null) {
            sb.append(curr.val).append(" -> ");
            curr = curr.next;
        }
        sb.append("NULL ]");
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        SinglyLinkedList list = new SinglyLinkedList();

        System.out.println("Testing Add Operations");
        list.addFirst(20);
        list.addFirst(10); // List: 10 -> 20
        list.addLast(40);  // List: 10 -> 20 -> 40
        list.addAt(2, 30); // List: 10 -> 20 -> 30 -> 40
        list.print();      // Expected: [ 10 -> 20 -> 30 -> 40 -> NULL ]
        System.out.println("Length: " + list.getLength()); // Expected: 4

        System.out.println("\n--- Testing Delete Operations ---");
        System.out.println("Deleted First: " + list.deleteFirst()); // Removes 10
        list.print();                                              // [ 20 -> 30 -> 40 -> NULL ]

        System.out.println("Deleted Last: " + list.deleteLast());   // Removes 40
        list.print();                                              // [ 20 -> 30 -> NULL ]

        list.addLast(50);
        list.addLast(60);  // List: 20 -> 30 -> 50 -> 60
        list.print();

        System.out.println("Deleted At Index 2: " + list.deleteAt(2)); // Removes 50
        list.print();                                                  // [ 20 -> 30 -> 60 -> NULL ]
        System.out.println("Final Length: " + list.getLength());       // Expected: 3
    }
}
```

## Implementation - Doubly LinkedList

In a **Doubly Linked List (DLL)**, each node contains references to both its next node (`next`) and its previous node (`prev`).

![](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxS1iIJZdO2eYWidGAjJutj9AsE17VOBqDwZO4w-Zb8L7YB3oRj8Pg-ek&s=10)

Notice how maintaining a `tail` reference alongside `head` turns `addLast` and `deleteLast` into $O(1)$ operations because we don't have to traverse the entire list to find the last node.

```java
public class DoublyLinkedList {

    private static class Node {
        int val;
        Node prev;
        Node next;

        Node(int val) {
            this.val = val;
            this.prev = null;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // Add Operation

    /**
     * Time Complexity: O(1)
     */
    public void addFirst(int val) {
        Node newNode = new Node(val);

        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    /**
     * Time Complexity: O(1)
     */
    public void addLast(int val) {
        if (head == null) {
            addFirst(val);
            return;
        }

        Node newNode = new Node(val);
        tail.next = newNode;
        newNode.prev = tail;
        tail = newNode;
        size++;
    }

    /**
     * Time Complexity: O(N)
     */
    public void addAt(int index, int val) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        if (index == 0) {
            addFirst(val);
            return;
        }
        if (index == size) {
            addLast(val);
            return;
        }

        // Optimization: Traverse from head or tail depending on which is closer
        Node curr = getNodeAt(index);
        Node prevNode = curr.prev;
        Node newNode = new Node(val);

        newNode.next = curr;
        newNode.prev = prevNode;
        prevNode.next = newNode;
        curr.prev = newNode;

        size++;
    }

    // Delete Operation

    /**
     * Time Complexity: O(1)
     */
    public int deleteFirst() {
        if (head == null) {
            throw new IllegalStateException("List is empty");
        }
        int removedVal = head.val;
        if (head == tail) { // Single node case
            head = tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }

        size--;
        return removedVal;
    }

    /**
     * Time Complexity: O(1)
     */
    public int deleteLast() {
        if (tail == null) {
            throw new IllegalStateException("List is empty");
        }
        if (head == tail) {
            return deleteFirst();
        }
        int removedVal = tail.val;
        tail = tail.prev;
        tail.next = null;
        size--;
        return removedVal;
    }

    /**
     * Time Complexity: O(N)
     */
    public int deleteAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        if (index == 0) return deleteFirst();
        if (index == size - 1) return deleteLast();

        Node target = getNodeAt(index);
        int removedVal = target.val;

        target.prev.next = target.next;
        target.next.prev = target.prev;

        size--;
        return removedVal;
    }

    // Utility

    /**
     * Traversal optimization: O(N/2)
     */
    private Node getNodeAt(int index) {
        Node curr;
        if (index < size / 2) {
            curr = head;
            for (int i = 0; i < index; i++)
                curr = curr.next;
        } else {
            curr = tail;
            for (int i = size - 1; i > index; i--)
                curr = curr.prev;
        }
        return curr;
    }

    public int getLength() {
        return size;
    }

    public void printForward() {
        StringBuilder sb = new StringBuilder("Forward:  [ ");
        Node curr = head;
        while (curr != null) {
            sb.append(curr.val).append(" <-> ");
            curr = curr.next;
        }
        sb.append("NULL ]");
        System.out.println(sb.toString());
    }

    public void printBackward() {
        StringBuilder sb = new StringBuilder("Backward: [ ");
        Node curr = tail;
        while (curr != null) {
            sb.append(curr.val).append(" <-> ");
            curr = curr.prev;
        }
        sb.append("NULL ]");
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        DoublyLinkedList list = new DoublyLinkedList();

        list.addFirst(20);
        list.addFirst(10);
        list.addLast(40);
        list.addAt(2, 30); // 10 <-> 20 <-> 30 <-> 40

        list.printForward();  // Expected: [ 10 <-> 20 <-> 30 <-> 40 <-> NULL ]
        list.printBackward(); // Expected: [ 40 <-> 30 <-> 20 <-> 10 <-> NULL ]

        System.out.println("Deleted First: " + list.deleteFirst()); // 10
        System.out.println("Deleted Last: "  + list.deleteLast());  // 40
        list.printForward();                                       // [ 20 <-> 30 <-> NULL ]
    }
}
```

## Implementation - Circular LinkedList

A **Circular Linked List (CLL)** is a variation of a linked list where the last node points back to the first node (or `head`) instead of pointing to `NULL`. This forms a continuous, closed loop.

There are two main types:

1. **Circular Singly Linked List:** Last node points to `head`.
2. **Circular Doubly Linked List:** Last node's `next` points to `head`, and `head`'s `prev` points to the last node.

### The "Tail Pointer" Trick (Crucial for $O(1)$ Operations)

Instead of maintaining a `head` pointer, the standard technique for a Circular Linked List is to maintain a **`tail` pointer**.

- Since `tail` points to the last node, `tail.next` directly gives us the `head` node in **$O(1)$** time.
- This allows **$O(1)$** insertions at both the beginning (`addFirst`) and the end (`addLast`) without traversing the whole list to find the last node.

> Here is a full implementation of a **Circular Singly Linked List** using the `tail` pointer approach:

```java
/**
 * Custom Circular Singly Linked List Implementation.
 */
public class CircularLinkedList {

    private static class Node {
        int val;
        Node next;

        Node(int val) {
            this.val = val;
            this.next = null;
        }
    }

    // Points to the last node (tail.next is head)
    private Node tail;
    private int size;

    public CircularLinkedList() {
        this.tail = null;
        this.size = 0;
    }

    // Add Operation

    /**
     * Inserts at the front of the list.
     * Time Complexity: O(1)
     */
    public void addFirst(int val) {
        Node newNode = new Node(val);
        if (tail == null) {
            newNode.next = newNode; // Points to itself
            tail = newNode;
        } else {
            newNode.next = tail.next; // New node points to old head
            tail.next = newNode;      // Tail points to new head
        }
        size++;
    }

    /**
     * Inserts at the end of the list.
     * Time Complexity: O(1)
     */
    public void addLast(int val) {
        if (tail == null) {
            addFirst(val);
            return;
        }
        Node newNode = new Node(val);
        newNode.next = tail.next; // Points to head
        tail.next = newNode;      // Old tail points to new node
        tail = newNode;           // Advance tail pointer
        size++;
    }

    // Delete Operation

    /**
     * Deletes the first node (head).
     * Time Complexity: O(1)
     */
    public int deleteFirst() {
        if (tail == null) {
            throw new IllegalStateException("List is empty");
        }

        Node head = tail.next;
        int removedVal = head.val;

        if (tail == head) { // Single node case
            tail = null;
        } else {
            tail.next = head.next; // Bypass old head
        }

        size--;
        return removedVal;
    }

    /**
     * Deletes the last node (tail).
     * Time Complexity: O(N)
     */
    public int deleteLast() {
        if (tail == null) {
            throw new IllegalStateException("List is empty");
        }

        Node head = tail.next;
        if (tail == head) {
            return deleteFirst();
        }

        // Traverse to second-to-last node
        Node curr = head;
        while (curr.next != tail) {
            curr = curr.next;
        }

        int removedVal = tail.val;
        curr.next = head; // Point new tail to head
        tail = curr;      // Update tail pointer

        size--;
        return removedVal;
    }

    // Utility

    public int getLength() {
        return size;
    }

    /**
     * Prints the continuous loop safely without infinite looping.
     * Time Complexity: O(N)
     */
    public void print() {
        if (tail == null) {
            System.out.println("[] (Empty List)");
            return;
        }

        Node head = tail.next;
        Node curr = head;
        StringBuilder sb = new StringBuilder("[ ");

        do {
            sb.append(curr.val).append(" -> ");
            curr = curr.next;
        } while (curr != head);

        sb.append("(Back to Head: ").append(head.val).append(") ]");
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        CircularLinkedList cll = new CircularLinkedList();

        cll.addFirst(20);
        cll.addFirst(10);
        cll.addLast(30);
        cll.addLast(40);

        cll.print(); // Expected: [ 10 -> 20 -> 30 -> 40 -> (Back to Head: 10) ]

        System.out.println("Deleted First: " + cll.deleteFirst()); // 10
        cll.print(); // Expected: [ 20 -> 30 -> 40 -> (Back to Head: 20) ]

        System.out.println("Deleted Last: " + cll.deleteLast());   // 40
        cll.print(); // Expected: [ 20 -> 30 -> (Back to Head: 20) ]
    }
}
```

### Real-World Applications & Interview Questions

- **Operating System Scheduling:** Round-robin CPU scheduling uses circular lists so processes cycle through continuously.
- Music Player Playlists: Repeating a playlist in a loop.
- Classic Problem: Josephus Problem (LeetCode 1823 / Find the Winner of the Circular Game)

## Java Collection Framework - LinkedList

### Internal Implementation

Under the hood, Java’s `java.util.LinkedList` is implemented as a **Doubly Linked List**.

```java
// Simplified structure of how Java implements LinkedList internally
public class LinkedList<E> {
    private transient Node<E> first; // Head pointer
    private transient Node<E> last;  // Tail pointer
    private transient int size = 0;  // Size tracker

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
}

```

### Key Technical Specs

1. **Implements Two Interfaces:** It implements both `List<E>` and `Deque<E>` (Double-Ended Queue).
2. **Allows Nulls & Duplicates:** You can store `null` values as well as duplicate elements.
3. **Not Thread-Safe:** Operations are not synchronized. Use `Collections.synchronizedList()` or `ConcurrentLinkedQueue` for multi-threaded environments.
4. **Optimized Search Traversal:** When calling `get(index)`, Java checks if `index < (size >> 1)`. If the index is in the first half, it searches forward from `first`; otherwise, it searches backward from `last`.

### Essential Methods & Cheat Sheet

Since `LinkedList` implements `List`, `Queue`, and `Deque`, it provides multiple sets of methods for similar operations.

| Operation        | Standard List Method             | Queue Method (Throws Exception on Fail) | Queue Method (Returns Special Value/Null) | Deque / Stack Method          | Time Complexity |
| ---------------- | -------------------------------- | --------------------------------------- | ----------------------------------------- | ----------------------------- | --------------- |
| **Insert Front** | `add(0, val)`                    | —                                       | `offerFirst(val)`                         | `addFirst(val)` / `push(val)` | $O(1)$          |
| **Insert End**   | `add(val)`                       | `add(val)`                              | `offer(val)` / `offerLast(val)`           | `addLast(val)`                | $O(1)$          |
| **Read Front**   | `get(0)`                         | `element()`                             | `peek()` / `peekFirst()`                  | `getFirst()`                  | $O(1)$          |
| **Read End**     | `get(size-1)`                    | —                                       | `peekLast()`                              | `getLast()`                   | $O(1)$          |
| **Remove Front** | `remove(0)`                      | `remove()`                              | `poll()` / `pollFirst()`                  | `removeFirst()` / `pop()`     | $O(1)$          |
| **Remove End**   | `remove(size-1)`                 | —                                       | `pollLast()`                              | `removeLast()`                | $O(1)$          |
| **Access Index** | `get(index)`                     | —                                       | —                                         | —                             | **$O(N)$**      |
| **Search Value** | `indexOf(val)` / `contains(val)` | —                                       | —                                         | —                             | **$O(N)$**      |

> **Tips:** For Queue operations, prefer `offer()`, `poll()`, and `peek()`. They return `null` or `false` on empty/failure cases instead of throwing runtime exceptions like `remove()` or `element()`.

### When to Use `java.util.LinkedList` vs Custom `ListNode`

This is the **#1 source of confusion** for DSA beginners.

#### Scenario A: Standard LeetCode / Interview Linked List Problems

If the question is explicitly testing **Linked List Manipulation** (e.g., _Reverse Linked List_, _Merge Two Sorted Lists_, _Detect Cycle_), _DO NOT_ use `java.util.LinkedList**`.

Platforms like LeetCode will define a simple custom class:

```java
public class ListNode {
    int val;
    ListNode next;
    ListNode(int val) {
        this.val = val;
    }
}

```

Here, you are expected to manipulate raw pointers (`next`, `prev`) directly.

#### Scenario B: Higher-Level Problems (Graphs, BFS, Stacks, Queues)

When solving problems on **BFS (Breadth-First Search)**, **Sliding Window**, or **Topological Sort**, you need a **Double-Ended Queue (Deque)** or **Queue**.

You instantiate Java's `LinkedList` like this:

```java
// Recommended Queue declaration in Java
Queue<Integer> queue = new LinkedList<>();

// Recommended Deque declaration in Java
Deque<Integer> deque = new LinkedList<>();
// Note: ArrayDeque is usually faster than LinkedList in practice due to cache locality!

```

### Crucial Interview Traps & Pitfalls

#### Trap 1: Using `LinkedList.get(i)` inside a Loop ($O(N^2)$ Disasters)

```java
LinkedList<Integer> list = getLargeLinkedList();

// BAD: This takes O(N^2) time! get(i) traverses nodes from head every iteration!
for (int i = 0; i < list.size(); i++) {
    System.out.println(list.get(i));
}

// GOOD: Use Iterator or For-Each (O(N) time)
for (int val : list) {
    System.out.println(val);
}

```

#### Trap 2: Modifying `LinkedList` while Iterating

Using standard `list.remove(val)` inside a for-each loop will throw a `ConcurrentModificationException`.

```java
// BAD
for (Integer val : list) {
    if (val == 5) list.remove(val); // Throws ConcurrentModificationException!
}

// GOOD: Use Iterator
Iterator<Integer> it = list.iterator();
while (it.hasNext()) {
    if (it.next() == 5) {
        it.remove(); // Safe removal
    }
}

// EVEN BETTER (Java 8+)
list.removeIf(val -> val == 5);

```

#### Trap 3: `LinkedList` vs `ArrayList` vs `ArrayDeque` Performance

In real systems and coding rounds:

- **`ArrayList`** is faster than `LinkedList` for almost everything (even random insertions) due to **CPU Cache Locality** (contiguous memory block).
- **`ArrayDeque`** is faster and uses less memory than `LinkedList` when implementing a Queue or Stack, because `LinkedList` allocates a new `Node` object for every single element, creating garbage collection overhead.

### Cheat Sheet Code snippet for Java `LinkedList`

```java
import java.util.LinkedList;
import java.util.Queue;
import java.util.Deque;

public class LinkedListDemo {
    public static void main(String[] args) {
        // 1. As a Double-Ended Queue (Deque)
        Deque<Integer> deque = new LinkedList<>();
        deque.addFirst(10);
        deque.addLast(20);
        deque.offerFirst(5); // 5 -> 10 -> 20

        System.out.println("First: " + deque.peekFirst()); // 5
        System.out.println("Last: " + deque.peekLast());   // 20

        deque.pollFirst(); // Removes 5
        deque.pollLast();  // Removes 20

        // 2. As a Queue for BFS
        Queue<String> bfsQueue = new LinkedList<>();
        bfsQueue.offer("NodeA");
        bfsQueue.offer("NodeB");

        while (!bfsQueue.isEmpty()) {
            String curr = bfsQueue.poll();
            System.out.println("Processing: " + curr);
        }
    }
}

```

## References

- Introduction to LinkedList - https://www.geeksforgeeks.org/dsa/linked-list-data-structure/
- Youtube Video - https://youtu.be/Nq7ok-OyEpg
- Oracle API docs on LinkedList - https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.html
