# [Reverse Nodes in k-Group](https://leetcode.com/problems/reverse-nodes-in-k-group/description/)

<p align="right">Last updated - 28.07.2026</p>

### Intuition

Reversing a linked list in groups of $k$ is an extension of reversing a single sub-list:

1. **Check Group Sufficiency:** Before reversing any segment, walk $k - 1$ steps forward from the start of the current group (`currentGroupHead`) to find its $k$-th node (`kthNode`).
2. **Handle Incomplete Groups:** If fewer than $k$ nodes remain (`kthNode == null`), leave them untouched and connect them directly to the tail of the previously reversed group.
3. **Isolate and Reverse:** If a full group of $k$ nodes exists:
   - Save the head of the next group (`nextGroupHead = kthNode.next`).
   - Terminate the current group (`kthNode.next = null`).
   - Reverse the isolated $k$-node group using a helper function.

4. **Connect Chunks:**
   - Re-link the tail of the previous reversed group (`previousGroupTail`) to the new head of this reversed group (`reversedGroupHead`).
   - Move `previousGroupTail` to the new tail of this reversed group (which is `currentGroupHead`).
   - Proceed to the next group.

### Algorithm

1. **Base Case Check:** If `head` is `null` or $k = 1$, return `head` as no modification is required.
2. **Find the $k$-th Node:** Use a helper function `getKthNode` to move $k - 1$ steps ahead from the current position.
3. **Loop Through List:** While `currentGroupHead` is not `null`:
   - Locate `kthNode`.
   - If `kthNode` is `null`, append the remaining nodes to `previousGroupTail` and terminate the loop.
   - Save `nextGroupHead = kthNode.next` and detach the group by setting `kthNode.next = null`.
   - Reverse the current group using `reverseList`.
   - Update `newHead` if this is the very first group being reversed.
   - Attach `reversedGroupHead` to `previousGroupTail` if a previous group exists.
   - Update `previousGroupTail = currentGroupHead`.
   - Advance `currentGroupHead = nextGroupHead`.

4. **Return Result:** Return `newHead` (the head of the first reversed $k$-group).

### Implementations

```java []
class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        // Base case: empty list or k = 1 requires no group reversals
        if (head == null || k == 1) {
            return head;
        }

        ListNode currentGroupHead = head;
        ListNode resultHead = null;
        ListNode previousGroupTail = null;

        while (currentGroupHead != null) {
            // Find the k-th node from currentGroupHead
            ListNode kthNode = getKthNode(currentGroupHead, k);

            // If fewer than k nodes remain, preserve their original order
            if (kthNode == null) {
                if (previousGroupTail != null) {
                    previousGroupTail.next = currentGroupHead;
                }
                break;
            }

            // Store the start of the next group and isolate the current group
            ListNode nextGroupHead = kthNode.next;
            kthNode.next = null;

            // Reverse the current k-group
            ListNode reversedGroupHead = reverseList(currentGroupHead);

            // Set the overall head of the result list (first group's new head)
            if (resultHead == null) {
                resultHead = reversedGroupHead;
            }

            // Connect the previous group's tail to the new head of this reversed group
            if (previousGroupTail != null) {
                previousGroupTail.next = reversedGroupHead;
            }

            // The original head of this group is now its tail
            previousGroupTail = currentGroupHead;

            // Connect current group's tail to the remaining unreversed list temporarily
            currentGroupHead.next = nextGroupHead;

            // Move pointer to process the next group
            currentGroupHead = nextGroupHead;
        }

        return resultHead;
    }

    // Reverses a full linked list and returns the new head
    private ListNode reverseList(ListNode node) {
        ListNode previousNode = null;
        ListNode currentNode = node;

        while (currentNode != null) {
            ListNode nextNode = currentNode.next;
            currentNode.next = previousNode;
            previousNode = currentNode;
            currentNode = nextNode;
        }

        return previousNode;
    }

    // Traverses k-1 steps forward to locate the k-th node in the current segment
    private ListNode getKthNode(ListNode node, int k) {
        while (node != null && --k > 0) {
            node = node.next;
        }
        return node;
    }
}

```

### Complexity Analysis

- Time Complexity: $O(N)$
  - **Group Check (`getKthNode`):** Scans each node once to verify group size ($O(N)$ overall).
  - **Group Reversal (`reverseList`):** Each node is reversed at most once ($O(N)$ overall).
  - **Total Time:** $O(N) + O(N) = O(2N) \rightarrow \mathbf{O(N)}$, where $N$ is the total number of nodes in the linked list.

- Space Complexity: $O(1)$
  - The algorithm uses a fixed set of pointers (`currentGroupHead`, `previousGroupTail`, `resultHead`, `kthNode`, `nextGroupHead`) to manipulate links in place.
  - **Total Space:** **$O(1)$** auxiliary space.
