# [Middle of the Linked List](https://leetcode.com/problems/middle-of-the-linked-list/description/)

<p align="right">Last updated - 27.07.2026</p>

## Approach 1 (Naive): Calculate Length & Find Middle

### Intuition

To find the middle node of a linked list, we first need to know how many total nodes exist. Since a standard singly linked list only allows us to move forward one node at a time, we cannot instantly look up its size like an array.

1. **First Pass (Find the Length):** Traverse from the start to the end, counting every node until we reach `null`.
2. **Second Pass (Reach the Middle):** Using integer division (`length / 2`), calculate how many steps are required to reach the center node. Then, start from the head again and step forward that exact number of times.

> **Note on even-length lists:** If the list has an even number of nodes (e.g., 6 nodes), `6 / 2 = 3`. Moving 3 steps from the start lands on the 4th node (0-indexed position 3), which corresponds to the second middle node as required.

### Algorithm

1. **Calculate Length:** Initialize a counter `length` to `0`. Traverse the linked list using a temporary pointer, incrementing `length` for each node until `null` is reached.
2. **Calculate Middle Index:** Compute `middleIndex = length / 2`.
3. **Traverse to Middle:** Reset or use a pointer starting at `head`. Move forward `middleIndex` times using a simple loop.
4. **Return Result:** The pointer will now be resting on the middle node. Return this node.

### Implementation

```java []
class Solution {

    public ListNode middleNode(ListNode head) {
        // Step 1: Calculate the total number of nodes in the list
        int totalLength = getLength(head);

        // Step 2: Determine how many steps to take to reach the middle node
        int targetIndex = totalLength / 2;

        // Step 3: Advance the head pointer to the middle node
        ListNode currentNode = head;
        for (int i = 0; i < targetIndex; i++) {
            currentNode = currentNode.next;
        }

        return currentNode;
    }

    // Helper method to count total nodes in the linked list
    private int getLength(ListNode head) {
        int count = 0;
        ListNode currentNode = head;

        while (currentNode != null) {
            count++;
            currentNode = currentNode.next;
        }
        return count;
    }
}

```

### Complexity Analysis

- Time Complexity: $O(N)$
  - **First Pass:** Counting the total number of nodes takes $N$ steps, where $N$ is the number of nodes in the list.
  - **Second Pass:** Traversing to the middle takes $\lfloor N / 2 \rfloor$ steps.
  - **Total Time:** $N + \lfloor N / 2 \rfloor = O(N)$ linear time.

- Space Complexity: $O(1)$
  - We only use a few primitive variables (`totalLength`, `targetIndex`, `count`) and pointers (`currentNode`). No extra memory proportional to the size of the list is allocated, keeping the space complexity constant.

## Approach 2 (Optimal): Fast & Slow Pointer

### Intuition

Instead of making two passes over the linked list (one to count nodes and another to reach the middle), we can find the middle node in a **single pass** using two pointers that move at different speeds:

- **Slow Pointer:** Moves **1 step** at a time.
- **Fast Pointer:** Moves **2 steps** at a time.

Because the fast pointer moves at twice the speed of the slow pointer, by the time the fast pointer reaches the end of the list, the slow pointer will naturally be sitting right in the middle!

> **Analogy:** Think of two runners on a track. If Runner A runs twice as fast as Runner B, when Runner A crosses the finish line, Runner B will be exactly halfway through the course.

## Algorithm

1. **Initialize Pointers:** Place both `slowPointer` and `fastPointer` at the `head` of the linked list.
2. **Traverse the List:** Loop while `fastPointer` is not `null` AND `fastPointer.next` is not `null`:
   - Move `slowPointer` forward by 1 node (`slowPointer = slowPointer.next`).
   - Move `fastPointer` forward by 2 nodes (`fastPointer = fastPointer.next.next`).

3. **Handle List Endings:**
   - **Odd Number of Nodes:** The fast pointer stops _on_ the last node (`fastPointer.next == null`).
   - **Even Number of Nodes:** The fast pointer moves _past_ the last node (`fastPointer == null`).

4. **Return Result:** Once the loop terminates, `slowPointer` will point directly to the middle node (or the second middle node for even lengths).

### Implementation

```java []
class Solution {

    public ListNode middleNode(ListNode head) {
        // Initialize two pointers starting at the head of the list
        ListNode slowPointer = head;
        ListNode fastPointer = head;

        // Traverse until the fast pointer reaches the end of the list.
        // We check both fastPointer and fastPointer.next to safely advance by 2 steps.
        while (fastPointer != null && fastPointer.next != null) {
            slowPointer = slowPointer.next;      // Moves 1 step
            fastPointer = fastPointer.next.next; // Moves 2 steps
        }

        // When the fast pointer hits the end, the slow pointer is at the middle
        return slowPointer;
    }
}

```

### Complexity Analysis

- Time Complexity: $O(N)$
  - The fast pointer traverses the list by skipping every second node, reaching the end in $\lceil N / 2 \rceil$ loop iterations (where $N$ is the number of nodes).
  - Since constant factors are dropped in Big-O notation, $O(N / 2)$ simplifies to **$O(N)$**. This is an optimal single-pass solution.

- Space Complexity: $O(1)$
  - We only store two pointer variables (`slowPointer` and `fastPointer`).
  - No extra data structures are allocated, keeping the memory overhead strictly constant.
