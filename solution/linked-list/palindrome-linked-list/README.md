# [Palindrome Linked List](https://leetcode.com/problems/palindrome-linked-list/description/)

<p align="right">Last updated - 27.07.2026</p>

## Approach 1

### Intuition

Checking if a linked list is a palindrome can be tricky because linked lists only allow us to traverse in one direction (forward). We cannot easily look at elements from right to left like we can with an array or string.

To overcome this, we copy all node values from the linked list into an `ArrayList`. Once the numbers are stored in an indexed list, we can use a classic **two-pointer technique**:

- Place one pointer at the **beginning** (left).
- Place another pointer at the **end** (right).
- Move both pointers toward the middle step-by-step, checking if the values match at every step.

If every pair of elements matches until the pointers cross, the list is a palindrome!

### Algorithm

1. **Extract Values:** Traverse the linked list from `head` to `null`, appending each node's `val` to a list called `nodeValues`.
2. **Initialize Two Pointers:** Create a `leftIndex` starting at `0` and a `rightIndex` starting at `nodeValues.size() - 1`.
3. **Compare Elements:** Loop while `leftIndex < rightIndex`:
   - Use `.get()` or `.equals()` to compare the elements at `leftIndex` and `rightIndex`.
   - If the values differ, return `false` immediately.
   - Increment `leftIndex` and decrement `rightIndex`.

4. **Return Result:** If the loop finishes without finding any mismatched pairs, return `true`.

### Implementation

```java []
import java.util.ArrayList;
import java.util.List;

class Solution {

    public boolean isPalindrome(ListNode head) {
        // Step 1: Copy linked list node values into a dynamic array
        List<Integer> nodeValues = new ArrayList<>();
        ListNode currentNode = head;

        while (currentNode != null) {
            nodeValues.add(currentNode.val);
            currentNode = currentNode.next;
        }

        // Step 2: Check if the extracted list of values is a palindrome
        return checkPalindrome(nodeValues);
    }

    // Helper method to verify palindrome property using two pointers
    private boolean checkPalindrome(List<Integer> values) {
        int leftIndex = 0;
        int rightIndex = values.size() - 1;

        while (leftIndex < rightIndex) {
            // Note: Use !values.get(...).equals(...) in Java when comparing Integer objects
            // outside the small-integer cache range (-128 to 127)
            if (!values.get(leftIndex).equals(values.get(rightIndex))) {
                return false;
            }
            leftIndex++;
            rightIndex--;
        }

        return true;
    }
}

```

> **Interview Tip:** In Java, `ArrayList<Integer>` stores `Integer` objects rather than primitive `int`s. Comparing two `Integer` objects using `!=` checks memory references rather than values when values fall outside `-128` to `127`. Using `.equals()` avoids subtle bugs during tests with large numbers!

### Complexity Analysis

- Time Complexity: $O(N)$
  - **Traversal:** Copying $N$ linked list nodes into the `ArrayList` takes $O(N)$ time.
  - **Two-Pointer Check:** Comparing elements from both ends takes $N / 2$ steps, which is $O(N)$.
  - **Total Time:** $O(N) + O(N) = \mathbf{O(N)}$ linear time.

- Space Complexity: $O(N)$
  - We store all $N$ values in an `ArrayList`, which requires extra memory proportional to the size of the linked list, taking **$O(N)$** space.

## Approach 2 (Optimal): Fast & Slow Pointer

### Intuition

A sequence is a palindrome if it reads the same forward and backward. Since a singly linked list only lets us traverse forward, we can check for palindrome symmetry by **reversing the second half of the list** and comparing it node-by-node with the first half.

1. **Find the Middle:** Use the fast and slow pointer technique to locate the center node.
2. **Reverse the Second Half:** Reverse the sublist starting from the middle node to the end.
3. **Compare Halves:** Move through the first half and the reversed second half simultaneously. If all values match, the list is a palindrome!

### Algorithm

1. **Find Middle:** Use `slow` (1 step) and `fast` (2 steps) pointers. When `fast` reaches the end, `slow` points to the middle node.
2. **Reverse Second Half:** Reverse the list starting at `slow`. This returns a new head pointer for the reversed second half (`secondHalfHead`).
3. **Compare Values:** Initialize `firstHalfHead` at `head` and `secondHalfHead` at the reversed middle.
   - Loop while `secondHalfHead != null`.
   - Compare `firstHalfHead.val` with `secondHalfHead.val`. If they differ, return `false`.
   - Move both pointers forward one node at a time.

4. **Return Result:** If all corresponding pairs match, return `true`.

### Implementation

```java
class Solution {

    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }

        // Step 1: Find the middle node of the linked list
        ListNode middleNode = getMiddle(head);

        // Step 2: Reverse the second half of the linked list starting from middleNode
        ListNode secondHalfHead = reverseList(middleNode);

        // Step 3: Compare nodes from the first half and the reversed second half
        ListNode firstHalfHead = head;

        while (secondHalfHead != null) {
            if (firstHalfHead.val != secondHalfHead.val) {
                return false;
            }
            firstHalfHead = firstHalfHead.next;
            secondHalfHead = secondHalfHead.next;
        }

        return true;
    }

    // Helper method to locate the middle node using fast and slow pointers
    private ListNode getMiddle(ListNode head) {
        ListNode slowPointer = head;
        ListNode fastPointer = head;

        while (fastPointer != null && fastPointer.next != null) {
            slowPointer = slowPointer.next;
            fastPointer = fastPointer.next.next;
        }

        return slowPointer;
    }

    // Helper method to reverse a linked list iteratively
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

> **Interview Note on Loop Condition:** Changing the comparison loop condition to `while (secondHalfHead != null)` makes the code much cleaner and works seamlessly for both odd and even length lists without needing `cur != mid`.

### Complexity Analysis

- Time Complexity: $O(N)$
  - **Finding Middle:** Fast and slow pointers scan $N$ nodes in $N/2$ steps $\rightarrow O(N)$.
  - **Reversing Second Half:** Reversing the second half takes $N/2$ steps $\rightarrow O(N)$.
  - **Comparison Loop:** Iterating through the second half takes $N/2$ steps $\rightarrow O(N)$.
  - **Total Time:** $O(N) + O(N) + O(N) = \mathbf{O(N)}$ linear time.

- Space Complexity: $O(1)$
  - No external data structures (like arrays or lists) are used.
  - The list reversal is done in-place by altering pointer references, requiring only constant auxiliary memory (**$O(1)$**).
