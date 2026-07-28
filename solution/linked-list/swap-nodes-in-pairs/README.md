# [Swap Nodes in Pairs](https://leetcode.com/problems/swap-nodes-in-pairs/description/)

<p align="right">Last updated - 28.07.2026</p>

### Intuition

Swapping adjacent nodes in pairs is a special case of reversing nodes in groups of $k$, where $k = 2$.

Instead of writing a completely separate algorithm for swapping pairs, we can reuse the $k$-group reversal logic:

1. Divide the linked list into blocks of size $k = 2$.
2. For every full pair (e.g., nodes $A \rightarrow B$), detach the pair, reverse it to get $B \rightarrow A$, and connect the tail of the previous reversed pair to $B$.
3. If an odd node remains at the end (a group of size $< 2$), leave it untouched and connect it to the tail of the last swapped pair.

### Algorithm

1. **Handle Base Cases:** If `head` is `null` or `head.next == null`, the list has 0 or 1 node, so return `head` directly.
2. **Set Group Size $k = 2$:** Delegate the pair-swapping logic to `reverseKGroup(head, 2)`.
3. **Group Traversal & Swap:**
   - Find the 2nd node (`kthNode`) from the start of the current group using `findKthNode(temp, 2)`.
   - If `kthNode == null`, a full pair does not exist (e.g., single remaining node at the end). Connect `prevNode.next = temp` and exit the loop.
   - Save the node following the pair (`kthNext = kthNode.next`).
   - Isolate the pair by setting `kthNode.next = null`.
   - Reverse the isolated pair using `reverseList(temp)`.

4. **Reconnect Chunks:**
   - If processing the first pair (`head == temp`), update `head = kthNode` to point to the new overall head of the list.
   - Otherwise, link the previous group's tail to `kthNode` (`prevNode.next = kthNode`).
   - Move `prevNode` to `temp` (which is now the tail of the reversed pair).
   - Move `temp` forward to `kthNext` to process the next pair.

5. **Return `head`:** Return the modified list head.

### Implementations

```java []
class Solution {

    /**
     * Reverses adjacent nodes in pairs by delegating to reverseKGroup with k = 2.
     */
    public ListNode swapPairs(ListNode head) {
        return reverseKGroup(head, 2);
    }

    /**
     * Reverses the nodes of a linked list in groups of size k.
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode currentGroupHead = head;
        ListNode previousGroupTail = null;

        while (currentGroupHead != null) {
            ListNode kthNode = findKthNode(currentGroupHead, k);

            // If fewer than k nodes remain, connect tail and stop
            if (kthNode == null) {
                if (previousGroupTail != null) {
                    previousGroupTail.next = currentGroupHead;
                }
                break;
            }

            // Save head of next group and detach current group
            ListNode nextGroupHead = kthNode.next;
            kthNode.next = null;

            // Reverse the current k-group
            reverseList(currentGroupHead);

            // Update head pointer on the first group reversal
            if (head == currentGroupHead) {
                head = kthNode;
            } else {
                previousGroupTail.next = kthNode;
            }

            // Current group head becomes its new tail after reversal
            previousGroupTail = currentGroupHead;

            // Advance to next group
            currentGroupHead = nextGroupHead;
        }

        return head;
    }

    /**
     * Finds the k-th node starting from the given node (1-indexed).
     */
    public ListNode findKthNode(ListNode startNode, int k) {
        k -= 1;

        while (startNode != null && k != 0) {
            k--;
            startNode = startNode.next;
        }

        return startNode;
    }

    /**
     * Reverses a standard singly linked list iteratively.
     */
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode currentNode = head;
        ListNode previousNode = null;

        while (currentNode != null) {
            ListNode nextNode = currentNode.next;
            currentNode.next = previousNode;
            previousNode = currentNode;
            currentNode = nextNode;
        }

        return previousNode;
    }
}

```

### Complexity Analysis

- Time Complexity: $O(N)$
  - **Finding 2nd Node (`findKthNode`):** Traverses 1 step per pair ($O(N)$ total time).
  - **Reversing Pairs (`reverseList`):** Each pair of 2 nodes is reversed in $O(1)$ time ($O(N)$ total time across the entire list).
  - **Total Time:** $O(N) + O(N) = O(2N) \rightarrow \mathbf{O(N)}$, where $N$ is the total number of nodes in the linked list.

- Space Complexity: $O(1)$
  - The algorithm performs pointer swaps in-place using a few temporary reference variables (`currentGroupHead`, `previousGroupTail`, `kthNode`, `nextGroupHead`).
  - **Total Space:** **$O(1)$** auxiliary space.
