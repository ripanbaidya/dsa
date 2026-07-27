# [Reverse Linked List II](https://leetcode.com/problems/reverse-linked-list-ii/description/)

<p align="right">Last updated - 28.07.2026</p>

## Approach

### Intuition

Reversing a sub-list between positions `left` and `right` can be viewed as taking a slice of a chain, reversing that slice, and connecting it back into place:

1. **Locate the Cut Points:** We locate the node right before position `left` (`leftPrev`), the first node to reverse (`leftStart`), the last node to reverse (`rightEnd`), and the node immediately following position `right` (`rightNext`).
2. **Isolate and Reverse:** We temporarily detach the sub-list from `leftStart` to `rightEnd` by setting `rightEnd.next = null`, then run a standard full-list reversal on this isolated sub-list.
3. **Reconnect:** After reversing, `rightEnd` becomes the new start of the sub-list, and `leftStart` becomes its new tail. We attach `leftPrev.next` to `rightEnd` and `leftStart.next` to `rightNext`.

### Algorithm

1. **Handle Base Cases:** If the list has fewer than 2 nodes or `left == right`, no reversal is needed; return `head`.
2. **Use a Dummy Node:** Create a `dummyNode` pointing to `head` to gracefully handle edge cases where `left = 1` (reversing starting from the head node).
3. **Find Sub-list Boundaries:**
   - Traverse the list with a position counter starting at `1`.
   - Record `leftPrev` (the node at `left - 1`) and `leftStart` (the node at `left`).
   - Record `rightEnd` (the node at `right`) and break early.

4. **Detach and Reverse:**
   - Save `rightNext` (`rightEnd.next`).
   - Break the link after `rightEnd` (`rightEnd.next = null`) to isolate the sub-list.
   - Pass `leftStart` into a helper function to reverse this sub-list.

5. **Reconnect:**
   - Point `leftPrev.next` to the head of the newly reversed sub-list (`rightEnd`).
   - Point the tail of the newly reversed sub-list (`leftStart.next`) to `rightNext`.

6. **Return `dummyNode.next`:** This returns the updated head of the entire list.

### Implementation

```java []
class Solution {
    public ListNode reverseBetween(ListNode head, int left, int right) {
        // Edge case: no reversal needed
        if (head == null || head.next == null || left == right) {
            return head;
        }

        // Dummy node helps handle cases where sub-list starts at the head (left = 1)
        ListNode dummyNode = new ListNode(-1);
        dummyNode.next = head;

        ListNode previousNode = dummyNode;
        ListNode currentNode = head;

        ListNode leftPrevious = dummyNode;
        ListNode leftStart = head;
        ListNode rightEnd = head;

        int currentPosition = 1;

        // Step 1: Traverse to locate the boundaries at 'left' and 'right'
        while (currentNode != null) {
            if (currentPosition == left) {
                leftPrevious = previousNode;
                leftStart = currentNode;
            }

            if (currentPosition == right) {
                rightEnd = currentNode;
                break;
            }

            previousNode = currentNode;
            currentNode = currentNode.next;
            currentPosition++;
        }

        // Step 2: Save reference to the node right after the sub-list and detach
        ListNode rightNext = rightEnd.next;
        rightEnd.next = null;

        // Step 3: Reverse the detached sub-list and reconnect
        leftPrevious.next = reverseList(leftStart);
        leftStart.next = rightNext;

        return dummyNode.next;
    }

    // Standard iterative function to reverse a full linked list
    private ListNode reverseList(ListNode head) {
        ListNode previousNode = null;
        ListNode currentNode = head;

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
  - **Finding boundaries:** Traverses up to position `right` ($O(\text{right})$ operations).
  - **Reversing sub-list:** Traverses the sub-list of length $(\text{right} - \text{left} + 1)$ ($O(\text{right} - \text{left})$ operations).
  - **Total Time:** $O(N)$ where $N$ is the total number of nodes in the linked list, as we traverse parts of the list at most twice.

- Space Complexity: $O(1)$
  - The algorithm uses a fixed number of pointer variables (`dummyNode`, `leftPrevious`, `leftStart`, `rightEnd`, `rightNext`, etc.) regardless of the list length.
  - **Total Space:** **$O(1)$** auxiliary space.
