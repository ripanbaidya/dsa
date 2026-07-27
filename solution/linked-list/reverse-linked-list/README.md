# [Reverse Linked List](https://leetcode.com/problems/reverse-linked-list/description/)

<p align="right">Last updated - 28.07.2026</p>

## Approach 1: Stack based

### Intuition

A **Stack** works on a **Last-In, First-Out (LIFO)** principle. When you push items into a stack one by one and then pop them out, they come out in the exact reverse order of how they went in.

By traversing the linked list from head to tail and pushing each node's value onto the stack, the last node's value ends up at the top of the stack. Traversing the linked list a second time and replacing each node's value with the popped value effectively reverses the order of the list's values.

### Algorithm

1. Initialize an empty stack to hold integers.
2. First Traversal (Collect Values):
   - Traverse the linked list starting from `head`.
   - Push the value (`val`) of each node onto the stack.
   - Move to the next node.

3. Second Traversal (Overwrite Values):
   - Reset the traversal pointer back to `head`.
   - For each node, pop the top value from the stack and assign it to the current node's `val`.
   - Move to the next node.

4. **Return `head**` (the structure/links of the nodes remain unchanged, but the values are reversed).

### Implementations

```java []
import java.util.ArrayDeque;
import java.util.Deque;

class Solution {
    public ListNode reverseList(ListNode head) {
        // Using Deque (ArrayDeque) as a stack
        Deque<Integer> stack = new ArrayDeque<>();
        ListNode current = head;

        // Step 1: Push all node values onto the stack
        while (current != null) {
            stack.push(current.val);
            current = current.next;
        }

        // Step 2: Reset pointer to head and pop values back into nodes
        current = head;
        while (current != null) {
            current.val = stack.pop();
            current = current.next;
        }

        return head;
    }
}

```

> **Tip:** In modern Java, `ArrayDeque` is preferred over `java.util.Stack` because `Stack` inherits from `Vector` and carries unnecessary synchronization overhead.

### Complexity Analysis

- Time Complexity: $O(N)$
  - **First loop:** Iterates through all $N$ nodes to push their values onto the stack ($O(N)$ time).
  - **Second loop:** Iterates through all $N$ nodes again to pop and reassign values ($O(N)$ time).
  - **Total Time:** $O(N) + O(N) = O(2N)$, which simplifies to **$O(N)$**, where $N$ is the number of nodes in the linked list.

- Space Complexity: $O(N)$
  - The stack stores $N$ integer values from the linked list simultaneously.
  - **Total Space:** **$O(N)$** auxiliary space.

## Approach 2: Iterative

### Intuition

Instead of copying node values into an extra data structure (like a stack), we can reverse the list in-place by changing the directions of the node pointers.

Normally, each node points forward to the `next` node (`1 -> 2 -> 3`). To reverse the list, we need each node to point backward to its preceding node (`1 <- 2 <- 3`).

As we traverse the list:

1. We keep track of the node behind us (`prev`).
2. We temporarily save the node ahead of us so we don't lose the rest of the list.
3. We flip the current node's pointer to point backward to `prev`.
4. We step forward and repeat until we reach the end.

### Algorithm

1. **Initialize `prev` to `null`:** This will eventually become the `next` pointer for the original head (which becomes the new tail).
2. **Traverse the list while `head` is not `null`:**
   - **Save Next Node:** Store `head.next` in a temporary variable `front` (or `nextTemp`) so we don't break the link to the rest of the list.
   - **Reverse Link:** Point `head.next` backward to `prev`.
   - **Move `prev` Forward:** Move `prev` to the current node (`head`).
   - **Move `head` Forward:** Move `head` to `front` to proceed to the next node in the original list.

3. **Return `prev`:** When `head` becomes `null`, `prev` will be pointing to the last node of the original list, which is the new head of the reversed list.

### Implementations

```java []
class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode previousNode = null;
        ListNode currentNode = head;

        while (currentNode != null) {
            // Step 1: Store reference to the next node
            ListNode nextNode = currentNode.next;

            // Step 2: Reverse the pointer of the current node
            currentNode.next = previousNode;

            // Step 3: Move the previous pointer one step forward
            previousNode = currentNode;

            // Step 4: Move the current pointer one step forward
            currentNode = nextNode;
        }

        // previousNode is now the head of the reversed list
        return previousNode;
    }
}

```

### Complexity Analysis

- Time Complexity: $O(N)$
  - We traverse the linked list of length $N$ exactly once.
  - Performing pointer reassignments at each node takes constant time, $O(1)$.
  - **Total Time:** **$O(N)$**, where $N$ is the number of nodes in the linked list.

- Space Complexity: $O(1)$
  - The algorithm only uses two pointer variables (`previousNode` and `nextNode`) regardless of the size of the linked list.
  - **Total Space:** **$O(1)$** (constant extra space).

## Approach 3: Recursive

### Intuition

Think of recursion as breaking a big problem down into smaller versions of itself:

1. If we assume the entire rest of the list (from `head.next` onward) is already reversed, all that remains is fixing the connection for the current `head` node.
2. The node right after `head` (let's call it `front` or `next`) is now the **tail** of that newly reversed sublist.
3. To attach `head` to the end of that reversed sublist, we make `front.next` point back to `head`.
4. Finally, we set `head.next = null` so `head` becomes the new tail (preventing a cycle).

### Algorithm

1. **Base Case:**
   - If `head` is `null` (empty list) or `head.next` is `null` (single-node list), return `head` directly. It is already reversed.

2. **Recursive Step:**
   - Call `reverseList(head.next)` recursively. This unwinds all the way to the end of the list and returns the new head (`newHead`), which will be the original last node.

3. **Re-link Pointers:**
   - Access the original next node: `ListNode nextNode = head.next;`.
   - Re-orient its pointer back to the current node: `nextNode.next = head;`.
   - Disconnect current node's forward link: `head.next = null;`.

4. **Return `newHead`:** Pass the new head node up through all call frames.

## Implementations

```java []
class Solution {
    public ListNode reverseList(ListNode head) {
        // Base case: empty list or reaching the last node
        if (head == null || head.next == null) {
            return head;
        }

        // Recursively reverse the remaining sublist
        ListNode newHead = reverseList(head.next);

        // head.next is currently the tail of the reversed sublist.
        // Point its 'next' back to head to reverse the connection.
        ListNode nextNode = head.next;
        nextNode.next = head;

        // Disconnect the current node's original forward pointer
        head.next = null;

        // Propagate the new head up the call stack
        return newHead;
    }
}

```

### Complexity Analysis

- Time Complexity: $O(N)$
  - The function visits every node in the linked list of length $N$ exactly once during the recursive call stack unwinding.
  - Pointer manipulation at each frame takes $O(1)$ time.
  - **Total Time:** **$O(N)$**, where $N$ is the number of nodes in the linked list.

- Space Complexity: $O(N)$
  - Unlike the iterative solution, recursion uses memory on the system **call stack**.
  - For a list of $N$ nodes, there will be $N$ recursive call frames stored in memory simultaneously before the stack begins to unwind.
  - **Total Space:** **$O(N)$** auxiliary space (due to the call stack).
