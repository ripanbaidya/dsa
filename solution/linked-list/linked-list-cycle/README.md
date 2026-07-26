# [Linked List Cycle](https://leetcode.com/problems/linked-list-cycle/description/)

<p align="right">Last updated - 27.07.2026</p>

## Approach 1 (Naive): Using Hashing

### Intuition

A cycle occurs in a linked list when a node's `next` pointer points back to a node that was visited earlier.

If you walk down a path and mark each landmark on a map, the moment you stumble onto a landmark already on your map, you know you are walking in a circle.

We can track every visited node using a **HashSet**. HashSets allow us to look up and store memory addresses (node references) in constant time. As we traverse node by node, we check if the current node is already in our set:

- **Already present:** A cycle exists, so we return `true`.
- **Reach `null`:** The end of the list was reached safely without revisiting any nodes, so no cycle exists (`false`).

### Algorithm

1. **Initialize a Set:** Create a `HashSet` of `ListNode` references to keep track of nodes we have seen so far.
2. **Traverse the List:** Use a pointer starting at `head` to walk through the list.
3. **Check for Cycles:** At each step:
   - If `visitedNodes.contains(currentNode)` is true, return `true` (cycle detected).
   - Otherwise, add `currentNode` to `visitedNodes`.
   - Advance `currentNode = currentNode.next`.

4. **Return Result:** If the loop terminates because `currentNode` becomes `null`, return `false`.

### Implementation

```java
import java.util.HashSet;

public class Solution {

    public boolean hasCycle(ListNode head) {
        // Set to store unique node references we've already visited
        HashSet<ListNode> visitedNodes = new HashSet<>();
        ListNode currentNode = head;

        // Traverse through the linked list
        while (currentNode != null) {
            // If the current node is already in the set, we found a cycle
            if (visitedNodes.contains(currentNode)) {
                return true;
            }

            // Record the current node as visited
            visitedNodes.add(currentNode);

            // Move to the next node
            currentNode = currentNode.next;
        }

        // Reached the end of the list without finding any loops
        return false;
    }
}
```

### Complexity Analysis

- Time Complexity: $O(N)$
  - Inserting into and looking up items in a `HashSet` takes **$O(1)$** average time.
  - If there is no cycle, we visit each of the $N$ nodes once: $N \times O(1) = O(N)$.
  - If a cycle exists, we stop as soon as we revisit the first looped node (at most $N$ operations).

- Space Complexity: $O(N)$
  - In the worst case (where there is no cycle, or the cycle is at the very end), the `HashSet` will store up to $N$ node references. This requires **$O(N)$** additional memory.

## Approach 2 (Optimal): Fash & Slow Pointer

### Intuition

Imagine two runners, a slow runner and a fast runner, on a race track:

- If the track is a **straight line**, the fast runner will quickly reach the finish line, leaving the slow runner behind.
- If the track contains a **circular loop** (a cycle), both runners will be trapped inside the loop forever. Because the fast runner covers ground twice as fast, they will eventually lap the slow runner and collide from behind.

By advancing `slowPointer` by **1 step** and `fastPointer` by **2 steps** per iteration:

1. If there is **no cycle**, `fastPointer` will hit `null` (the finish line) and stop.
2. If there **is a cycle**, both pointers will enter the loop. Once inside, the relative distance between them decreases by 1 step in every turn until `slowPointer == fastPointer`.

### Algorithm

1. **Initialize Pointers:** Set both `slowPointer` and `fastPointer` at the `head` of the list.
2. **Traverse the List:** Loop as long as `fastPointer` and `fastPointer.next` are not `null`:
   - Move `slowPointer` forward by 1 node (`slowPointer = slowPointer.next`).
   - Move `fastPointer` forward by 2 nodes (`fastPointer = fastPointer.next.next`).

3. **Check Intersection:** After updating both pointers, check if `slowPointer == fastPointer` (they refer to the exact same memory location):
   - If **equal**, a cycle is detected — return `true`.

4. **Return Result:** If the loop ends because `fastPointer` reaches the end (`null`), return `false`.

### Implementation

```java
public class Solution {

    public boolean hasCycle(ListNode head) {
        // Initialize fast and slow pointers at the start of the list
        ListNode slowPointer = head;
        ListNode fastPointer = head;

        // Traverse while the fast pointer has valid nodes to advance through
        while (fastPointer != null && fastPointer.next != null) {
            slowPointer = slowPointer.next;            // Advance by 1 step
            fastPointer = fastPointer.next.next;       // Advance by 2 steps

            // If fast and slow pointers meet, a cycle exists
            if (slowPointer == fastPointer) {
                return true;
            }
        }

        // Fast pointer reached the end, so there is no cycle
        return false;
    }
}

```

### Complexity Analysis

- Time Complexity: $O(N)$
  - **No Cycle:** The `fastPointer` reaches the end of the list in $N / 2$ steps, taking **$O(N)$** time.
  - **Has Cycle:** Let $K$ be the distance from the head to the start of the cycle, and $C$ be the length of the cycle loop.
    - Entering the loop takes $K$ steps.
    - Once inside, the fast pointer catches up to the slow pointer in at most $C$ steps (since the gap narrows by 1 step per iteration).
    - Total steps = $K + C \le N$, giving an overall time complexity of **$O(N)$**.

- Space Complexity: $O(1)$
  - Unlike the `HashSet` approach, we only track two reference pointers (`slowPointer` and `fastPointer`).
  - No additional memory structures are allocated, keeping space complexity strictly constant (**$O(1)$**).
