> It took me an hour to submit the optimal solution because I kept missing a single edge case. You don't have to make the same mistake. Here's exactly how you should think about and solve this problem in an interview.

Note::

> prerequisites - Reversal of LinkedList, dummyNode and a bit patience.


## Approach 1: Brute Force (Using a Stack)

### Intuition

A stack follows the **Last-In, First-Out (LIFO)** principle, which naturally reverses whatever you put into it. By traversing the list and pushing the values between positions `left` and `right` onto a stack, we can then traverse the list a second time and replace those exact positions' values with the values popped from the stack.

### Algorithm

1. Traverse the linked list using a 1-based index tracker (`pos`).
2. When `pos` falls within the range `[left, right]`, push the current node's value onto a stack.
3. Reset your traversal pointer to the head and index tracker back to `0`.
4. Traverse the linked list again. When `pos` falls within `[left, right]`, overwrite the node's value by popping from the stack.
5. Return the modified `head`.

### Edge Cases

* `left == right`: The range is a single node; no actual reversal happens.
* `head == null` or `head.next == null`: Empty list or single-node list.

### Implementation

```java []
class Solution {
    public ListNode reverseBetween(ListNode head, int left, int right) {
        if (head == null || head.next == null || left == right) return head;
        
        Stack<Integer> st = new Stack<>();
        ListNode temp = head;
        int pos = 0;
        
        // Step 1: Collect values into the stack
        while (temp != null) {
            pos++;
            if (pos >= left && pos <= right) {
                st.push(temp.val);
            }
            temp = temp.next;
        }
        
        // Step 2: Overwrite values from the stack
        temp = head;
        pos = 0;
        while (temp != null) {
            pos++;
            if (pos >= left && pos <= right) {
                temp.val = st.pop();
            }
            temp = temp.next;
        }
        
        return head;
    }
}

```

```cpp []
class Solution {
public:
    ListNode* reverseBetween(ListNode* head, int left, int right) {
        if (!head || !head->next || left == right) return head;
        
        stack<int> st;
        ListNode* temp = head;
        int pos = 0;
        
        while (temp) {
            pos++;
            if (pos >= left && pos <= right) st.push(temp->val);
            temp = temp->next;
        }
        
        temp = head;
        pos = 0;
        while (temp) {
            pos++;
            if (pos >= left && pos <= right) {
                temp->val = st.top();
                st.pop();
            }
            temp = temp->next;
        }
        return head;
    }
};

```

```python []
class Solution:
    def reverseBetween(self, head: Optional[ListNode], left: int, right: int) -> Optional[ListNode]:
        if not head or not head.next or left == right:
            return head
            
        stack = []
        temp = head
        pos = 0
        
        while temp:
            pos += 1
            if left <= pos <= right:
                stack.append(temp.val)
            temp = temp.next
            
        temp = head
        pos = 0
        while temp:
            pos += 1
            if left <= pos <= right:
                temp.val = stack.pop()
            temp = temp.next
            
        return head

```

### Complexity Analysis

* **Time Complexity:** $\mathcal{O}(N)$ — We traverse the entire list twice, where $N$ is the number of nodes in the list.
* **Space Complexity:** $\mathcal{O}(R - L + 1)$ — In the worst-case scenario (reversing the entire list), the stack stores $N$ elements, requiring $\mathcal{O}(N)$ extra space.

---

## Approach 2: Optimal (In-Place Reversal)

### Intuition

Instead of modifying data values or tracking items in an external data structure, we can structurally isolate the target sub-segment, reverse its pointers in-place, and sew it back into the main list. This avoids allocating extra memory.

### Algorithm

1. Create a `dummy` node pointing to `head` to elegantly manage edge cases where `left = 1`.
2. Move a pointer `temp` to find the bounds of our sub-list. Keep track of:
    * `leftPrev`: The node right *before* the sub-segment.
    * `leftStart`: The actual starting node of the sub-segment.
    * `rightEnd`: The ending node of the sub-segment.


3. Detach the sub-segment by capturing `rightEnd.next` (saved as `rightNext`) and cleanly setting `rightEnd.next = null`.
4. Run a standard standard iterative linked-list reversal function on the isolated segment starting at `leftStart`.
5. Connect `leftPrev.next` to the newly reversed segment's head (`rightEnd`), and point `leftStart.next` forward to `rightNext`.
6. Return `dummy.next`.

### Edge Cases

* `left == 1`: The head of the list itself is altered. The `dummy` node absorbs this scenario gracefully, ensuring we don't drop reference to the list's new head.

### Implementation

```java []
class Solution {
    public ListNode reverseBetween(ListNode head, int left, int right) {
        if (head == null || head.next == null || left == right) return head;
        
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode prev = dummy;
        ListNode temp = head;
        
        ListNode leftPrev = dummy;
        ListNode leftStart = head;
        ListNode rightEnd = head;
        int pos = 1;
        
        // Find split boundaries
        while (temp != null) {
            if (pos == left) {
                leftPrev = prev;
                leftStart = temp;
            }
            if (pos == right) {
                rightEnd = temp;
                break;
            }
            prev = temp;
            temp = temp.next;
            pos++;
        }
        
        // Detach the target sub-list
        ListNode rightNext = rightEnd.next;
        rightEnd.next = null;
        
        // Reverse and patch back seamlessly
        leftPrev.next = reverse(leftStart);
        leftStart.next = rightNext;
        
        return dummy.next;
    }
    
    private ListNode reverse(ListNode head) {
        ListNode prev = null;
        while (head != null) {
            ListNode front = head.next;
            head.next = prev;
            prev = head;
            head = front;
        }
        return prev;
    }
}

```

```cpp []
class Solution {
private:
    ListNode* reverse(ListNode* head) {
        ListNode* prev = nullptr;
        while (head) {
            ListNode* front = head->next;
            head->next = prev;
            prev = head;
            head = front;
        }
        return prev;
    }

public:
    ListNode* reverseBetween(ListNode* head, int left, int right) {
        if (!head || !head->next || left == right) return head;
        
        ListNode* dummy = new ListNode(-1);
        dummy->next = head;
        ListNode* prev = dummy;
        ListNode* temp = head;
        
        ListNode* leftPrev = dummy;
        ListNode* leftStart = head;
        ListNode* rightEnd = head;
        int pos = 1;
        
        while (temp) {
            if (pos == left) {
                leftPrev = prev;
                leftStart = temp;
            }
            if (pos == right) {
                rightEnd = temp;
                break;
            }
            prev = temp;
            temp = temp->next;
            pos++;
        }
        
        ListNode* rightNext = rightEnd->next;
        rightEnd->next = nullptr;
        
        leftPrev->next = reverse(leftStart);
        leftStart->next = rightNext;
        
        ListNode* newHead = dummy->next;
        delete dummy; // Clean up dummy node memory
        return newHead;
    }
};

```

```python []
class Solution:
    def reverse(self, head: Optional[ListNode]) -> Optional[ListNode]:
        prev = None
        while head:
            front = head.next
            head.next = prev
            prev = head
            head = front
        return prev

    def reverseBetween(self, head: Optional[ListNode], left: int, right: int) -> Optional[ListNode]:
        if not head or not head.next or left == right:
            return head
            
        dummy = ListNode(-1)
        dummy.next = head
        prev = dummy
        temp = head
        
        leftPrev = dummy
        leftStart = head
        rightEnd = head
        pos = 1
        
        while temp:
            if pos == left:
                leftPrev = prev
                leftStart = temp
            if pos == right:
                rightEnd = temp
                break
            prev = temp
            temp = temp.next
            pos += 1
            
        rightNext = rightEnd.next
        rightEnd.next = None
        
        leftPrev.next = self.reverse(leftStart)
        leftStart.next = rightNext
        
        return dummy.next

```

### Complexity Analysis

* **Time Complexity:** $\mathcal{O}(N)$ — We traverse down to the `right` element index once to locate pointers, and then run a linear-time reversal exclusively over the localized span. This ensures exactly one pass overall.
* **Space Complexity:** $\mathcal{O}(1)$ — No extra data structures are allocated. We manipulate pointer structures purely in place.