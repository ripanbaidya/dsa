# Stack

<p align="right">Last updated - 08.08.2026</p>

## Contents

- [Introduction to Stack](#introduction-to-stack)
- [Stack Implementation](#stack-implementation)
  - [Using Array](#using-array-fixed-size)
  - [Using LinkedList](#using-linked-list-dynamic-size)
- [Collection & Framework - Stack](#collection--framework)

## Introduction to Stack

A **Stack** is a linear data structure that follows the **LIFO (Last In, First Out)** principle. The last element added to the stack is the first one to be removed—much like a stack of plates.

### Core Operations & Complexity

- **Push:** Adds an element to the top of the stack. `O(1)`
- **Pop:** Removes and returns the top element. `O(1)`
- **Peek/Top:** Returns the top element without removing it. `O(1)`
- **IsEmpty:** Checks if the stack is empty. `O(1)`

### Pros & Cons

| Pros                                                                                  | Cons                                                                            |
| ------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------- |
| **Simple & Fast:** Constant time complexity `O(1)` for core operations.               | **Restricted Access:** Cannot directly access elements in the middle or bottom. |
| **Memory Management:** Efficiently manages memory during function calls (Call Stack). | **Overflow/Underflow:** Fixed-size implementations risk Stack Overflow.         |
| **Prevents Corruption:** Strict LIFO structure prevents arbitrary insertion/deletion. | **No Random Search:** Searching requires popping elements one by one.           |

## Stack Implementation

### Using Array (Fixed Size)

An array implementation uses an index variable (`top`) to keep track of the upper element.

- **Pros:** Memory efficient (no pointer overhead), fast cache access.
- **Cons:** Fixed capacity; triggers Overflow if full.

```java
class Stack {
    private int maxSize;
    private int[] stackArray;
    private int top;

    public Stack(int size) {
        maxSize = size;
        stackArray = new int[maxSize];
        top = -1;
    }

    public void push(int value) {
        if (top == maxSize - 1) throw new StackOverflowError("Stack is Full");
        stackArray[++top] = value;
    }

    public int pop() {
        if (isEmpty()) throw new IllegalStateException("Stack is Empty");
        return stackArray[top--];
    }

    public int peek() {
        if (isEmpty()) throw new IllegalStateException("Stack is Empty");
        return stackArray[top];
    }

    public boolean isEmpty() {
        return (top == -1);
    }
}

```

### Using Linked List (Dynamic Size)

A linked list implementation adds and removes nodes from the head of the list.

- **Pros:** Dynamic sizing; no predefined memory limit.
- **Cons:** Consumes extra memory for node pointers.

```java
class LinkedListStack {
    private class Node {
        int data;
        Node next;
        Node(int data) { this.data = data; }
    }

    private Node top;

    public void push(int value) {
        Node newNode = new Node(value);
        newNode.next = top;
        top = newNode;
    }

    public int pop() {
        if (isEmpty()) throw new IllegalStateException("Stack is Empty");
        int value = top.data;
        top = top.next;
        return value;
    }

    public int peek() {
        if (isEmpty()) throw new IllegalStateException("Stack is Empty");
        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }
}

```

## Collection & Framework

Java provides a built-in `Stack` class that inherits from `Vector`.

```java
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();

        stack.push(10);
        stack.push(20);

        System.out.println(stack.peek()); // 20
        System.out.println(stack.pop());  // 20
        System.out.println(stack.isEmpty()); // false
    }
}

```

#### **Modern Java Recommendation:**

> `java.util.Stack` is considered **legacy** because it extends `Vector`, making every operation synchronized (locking introduces performance overhead).
> For a thread-safe or single-threaded alternative, use **`ArrayDeque`**:

```java
Deque<Integer> stack = new ArrayDeque<>();
stack.push(10);
stack.pop();
```
