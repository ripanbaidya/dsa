# [Linked List Cycle II](https://leetcode.com/problems/linked-list-cycle-ii/description/)

<p align="right">Last updated - 27.07.2026</p>

## Approach 1 (Naive): Hashing

### Intuition

This problem builds directly on checking if a cycle exists. The key difference is that instead of returning `true` or `false`, we need to return the **exact node** where the cycle begins.

Imagine entering a building and taking photos of every room you pass through:

- The moment you step into a room that you already have a photo of, you know two things:
  1. You are walking in a loop.
  2. **This specific room** is the entry door to that loop.

By storing each visited `ListNode` reference in a `HashSet`, the very first node that generates a duplicate lookup (`set.contains(node)`) is guaranteed to be the node where the cycle begins.

### Algorithm

1. **Initialize a Set:** Create a `HashSet` of `ListNode` references to track unique node memory locations.
2. **Traverse the List:** Use a pointer starting at `head` to move through the nodes.
3. **Check for Duplicate Reference:** At each step:
   - Check if `visitedNodes.contains(currentNode)`.
   - **If yes:** `currentNode` is the entry point of the cycle. Return `currentNode` immediately.
   - **If no:** Add `currentNode` to `visitedNodes` and advance `currentNode = currentNode.next`.

4. **Return Result:** If `currentNode` becomes `null`, the list has a clear end without loops. Return `null`.

### Implementation

```java []
import java.util.HashSet;
import java.util.Set;

public class Solution {

    public ListNode detectCycle(ListNode head) {
        // Set to keep track of nodes we have already visited
        Set<ListNode> visitedNodes = new HashSet<>();
        ListNode currentNode = head;

        // Traverse through the linked list
        while (currentNode != null) {
            // The first node we encounter that is already in the set
            // is the exact starting node of the cycle
            if (visitedNodes.contains(currentNode)) {
                return currentNode;
            }

            // Mark the current node as visited
            visitedNodes.add(currentNode);

            // Move to the next node
            currentNode = currentNode.next;
        }

        // Reached the end of the list, meaning there is no cycle
        return null;
    }
}

```

### Complexity Analysis

- Time Complexity: $O(N)$
  - `HashSet` operations (`contains` and `add`) run in **$O(1)$** average time.
  - We traverse each node at most once. If a cycle exists with $N$ total nodes, we stop after visiting at most $N$ nodes. Thus, overall time is **$O(N)$**.

- Space Complexity: $O(N)$
  - In the worst case, we store all $N$ nodes of the linked list inside the `HashSet` before detecting the cycle or reaching the end, requiring **$O(N)$** auxiliary space.

## Approach 2 (Optimal): Fast & Slow Pointer

### Intuition

This problem is solved in two phases using math:

#### Phase 1: Detecting the Cycle

- We send a `slowPointer` (1 step/turn) and a `fastPointer` (2 steps/turn) through the list.
- If there is a cycle, they will eventually meet at some intersection point inside the loop.

#### Phase 2: Finding the Cycle Start

Let's break down the distances traveled when they meet:

$$\text{Head} \xrightarrow[\quad L_1 \quad]{} \text{Cycle Start} \xrightarrow[\quad L_2 \quad]{} \text{Meeting Point} \xrightarrow[\quad L_3 \quad]{} \text{Cycle Start}$$

- $L_1$: Distance from `head` to the start of the cycle.
- $L_2$: Distance from the cycle start to the meeting point inside the loop.
- $L_3$: Distance from the meeting point back to the cycle start.

Notice that the cycle length is $C = L_2 + L_3$.

When the two pointers meet:

- Distance covered by **Slow Pointer** = $L_1 + L_2$
- Distance covered by **Fast Pointer** = $L_1 + L_2 + k \cdot C$ (where $k$ is the number of full loops fast took)

Since the fast pointer runs at **twice** the speed of the slow pointer:

$$2 \cdot (L_1 + L_2) = L_1 + L_2 + k \cdot C$$

$$L_1 + L_2 = k \cdot C$$

$$L_1 = k \cdot C - L_2 = (k - 1) \cdot C + L_3$$

This equation tells us that **$L_1$ (distance from `head` to cycle start) is equal to $L_3$ (distance from meeting point to cycle start) plus some full loop iterations.**

Therefore, if we reset `slowPointer` back to `head` and advance **both** `slowPointer` and `fastPointer` **one step at a time**, they will travel equal distances ($L_1$ and $L_3$) and meet right at the **cycle start node**!

### Algorithm

1. **Phase 1 — Find Intersection:**
   - Move `slowPointer` by 1 step and `fastPointer` by 2 steps.
   - If `fastPointer` reaches `null`, there is no cycle; return `null`.
   - If `slowPointer == fastPointer`, stop — we found an intersection point.

2. **Phase 2 — Find Cycle Entry:**
   - Reset `slowPointer` back to `head`.
   - Keep `fastPointer` at the meeting point.
   - Move **both** pointers forward 1 step at a time until `slowPointer == fastPointer`.
   - Return `slowPointer` (or `fastPointer`), as it points directly to the cycle entry node.

### Implementation

```java []
public class Solution {

    public ListNode detectCycle(ListNode head) {
        ListNode slowPointer = head;
        ListNode fastPointer = head;

        // Phase 1: Determine if a cycle exists in the list
        while (fastPointer != null && fastPointer.next != null) {
            slowPointer = slowPointer.next;            // Advance by 1 step
            fastPointer = fastPointer.next.next;       // Advance by 2 steps

            // Cycle detected!
            if (slowPointer == fastPointer) {

                // Phase 2: Find the starting node of the cycle
                // Reset slow pointer to the head of the list
                slowPointer = head;

                // Move both pointers 1 step at a time until they meet
                while (slowPointer != fastPointer) {
                    slowPointer = slowPointer.next;
                    fastPointer = fastPointer.next;
                }

                // The node where they meet is the start of the cycle
                return slowPointer;
            }
        }

        // Fast pointer reached null, meaning no cycle exists
        return null;
    }
}

```

### Complexity Analysis

- Time Complexity: $O(N)$
  - **Phase 1:** Finding the collision point takes $O(N)$ steps because the fast pointer catches up to the slow pointer inside the cycle within at most $N$ operations.
  - **Phase 2:** Walking both pointers to the cycle entry point takes at most $N$ steps.
  - **Total Time:** $O(N) + O(N) = \mathbf{O(N)}$ linear time.

- Space Complexity: $O(1)$
  - We only keep track of two pointers (`slowPointer` and `fastPointer`).
  - No auxiliary data structures (like HashSets) are created, resulting in constant space.
