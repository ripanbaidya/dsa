> **Note:** It took me **30+ minutes** to come up with the optimal solution because I missed a simple `edge case`. Make sure you read the entire article to understand the technique behind the `Optimal` solution.

> **Prerequisites:** Arrays, Two Pointers (not necessary), and about **5 minutes** of your time.

> **All the very best! 👍** I'll soon record a video, upload it to my YouTube channel, and attach the link here.

---

## Naive Approach: Using Auxiliary Array

### **Intuition**

When looking at two sorted arrays, the most straightforward way to merge them is to build a brand-new sorted array from scratch. By starting at the beginning of both arrays, we can look at the smallest available elements and copy them over one by one into our temporary array. This prevents us from overwriting any elements in `nums1` prematurely.

### **Approach**

1. Create a temporary array `tmp` of size $m + n$.
2. Use two pointers, `p` for `nums1` and `q` for `nums2`, both starting at index `0`.
3. Compare `nums1[p]` and `nums2[q]`. Copy the smaller element into `tmp` and advance that pointer.
4. Once one array is exhausted, copy any remaining elements from the other array into `tmp`.
5. Finally, copy all elements from `tmp` back into `nums1`.

### **Implementation**

```java []
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        // Create an auxiliary array to hold the merged result
        int[] tmp = new int[m + n];
        int p = 0, q = 0;
        int it = 0;

        // Compare elements from the front of both arrays
        while (p < m && q < n) {
            if (nums1[p] <= nums2[q]) {
                tmp[it++] = nums1[p++];
            } else {
                tmp[it++] = nums2[q++];
            }
        }

        // Copy remaining elements of nums1, if any
        while (p < m) {
            tmp[it++] = nums1[p++];
        }

        // Copy remaining elements of nums2, if any
        while (q < n) {
            tmp[it++] = nums2[q++];
        }

        // Copy the merged result back into nums1
        for (int i = 0; i < m + n; i++) {
            nums1[i] = tmp[i];
        }
    }
}

```

```cpp []
class Solution {
public:
    void merge(vector<int>& nums1, int m, vector<int>& nums2, int n) {
        vector<int> tmp(m + n);
        int p = 0, q = 0;
        int it = 0;

        while (p < m && q < n) {
            if (nums1[p] <= nums2[q]) {
                tmp[it++] = nums1[p++];
            } else {
                tmp[it++] = nums2[q++];
            }
        }

        while (p < m) {
            tmp[it++] = nums1[p++];
        }

        while (q < n) {
            tmp[it++] = nums2[q++];
        }

        for (int i = 0; i < m + n; i++) {
            nums1[i] = tmp[i];
        }
    }
};

```

```python []
class Solution:
    def merge(self, nums1: List[int], m: int, nums2: List[int], n: int) -> None:
        tmp = [0] * (m + n)
        p, q = 0, 0
        it = 0

        while p < m and q < n:
            if nums1[p] <= nums2[q]:
                tmp[it] = nums1[p]
                p += 1
            else:
                tmp[it] = nums2[q]
                q += 1
            it += 1

        while p < m:
            tmp[it] = nums1[p]
            p += 1
            it += 1

        while q < n:
            tmp[it] = nums2[q]
            q += 1
            it += 1

        # Copy back to nums1
        for i in range(m + n):
            nums1[i] = tmp[i]

```

### **Complexity Analysis**

* **Time Complexity:** $O(m + n)$ – We iterate through both arrays to fill `tmp`, and then iterate again to copy back to `nums1`.
* **Space Complexity:** $O(m + n)$ – We allocate a completely new array `tmp` of size $m + n$.

---

## Optimal Solution: Backward Three-Pointer Technique

### **Key Idea & Intuition**

To get rid of the $O(m + n)$ extra space, we need to modify `nums1` **in-place**.

If we start merging from the front, we will overwrite elements in `nums1` before we've had a chance to look at them. However, notice that `nums1` has empty buffer space (filled with `0`s) at its **very end**. By shifting our perspective and comparing the **largest** elements instead of the smallest, we can safely fill `nums1` from right to left without breaking anything.

### **Approach & Detailed Dry Run**

We place three pointers at the trailing ends:

* `p` at the last valid element of `nums1` ($m - 1$)
* `q` at the last element of `nums2` ($n - 1$)
* `it` at the absolute last slot of `nums1` ($(m + n) - 1$)

#### **Dry Run Example**

* **Input:** `nums1 = [1, 2, 3, 0, 0, 0]`, `m = 3`; `nums2 = [2, 5, 6]`, `n = 3`
* **Pointers:** `p = 2` (val: `3`), `q = 2` (val: `6`), `it = 5`

1. **Step 1:** `nums1[p]` (`3`) vs `nums2[q]` (`6`). Since `6 > 3`, write `6` to `nums1[5]`. Decrement `q` and `it`.
    * *Array:* `[1, 2, 3, 0, 0, 6]` | `q = 1`, `it = 4`


2. **Step 2:** `nums1[p]` (`3`) vs `nums2[q]` (`5`). Since `5 > 3`, write `5` to `nums1[4]`. Decrement `q` and `it`.
    * *Array:* `[1, 2, 3, 0, 5, 6]` | `q = 0`, `it = 3`


3. **Step 3:** `nums1[p]` (`3`) vs `nums2[q]` (`2`). Since `3 > 2`, write `3` to `nums1[3]`. Decrement `p` and `it`.
    * *Array:* `[1, 2, 3, 3, 5, 6]` | `p = 1`, `it = 2`


4. **Step 4:** `nums1[p]` (`2`) vs `nums2[q]` (`2`). They are equal; write `2` from `nums1` to `nums1[2]`. Decrement `p` and `it`.
    * *Array:* `[1, 2, 2, 3, 5, 6]` | `p = 0`, `it = 1`


5. **Step 5:** `nums1[p]` (`1`) vs `nums2[q]` (`2`). Since `2 > 1`, write `2` to `nums1[1]`. Decrement `q` and `it`.
    * *Array:* `[1, 2, 2, 3, 5, 6]` | `q = -1`, `it = 0`


6. **Termination:** The primary loop ends because `q` fell below `0`. The remaining element `1` in `nums1` is already in its perfect spot.

### **Crucial Edge Case**

> **What happens if `nums1` runs out of elements first (`p < 0`) while `nums2` still has elements left?**
> If `nums1` is exhausted first (e.g., all elements in `nums2` are smaller than the smallest element of `nums1`), the main loop exits. We **must** copy the leftover elements from `nums2` into the front of `nums1`.
> *Conversely, if `nums2` runs out first (`q < 0`), we don't need to do anything because the remaining elements of `nums1` are already sorted and in their final correct positions.*

### **Implementation**

```java []
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int p = m - 1;
        int q = n - 1;
        int it = (m + n) - 1;

        // Merge from the back
        while (p >= 0 && q >= 0) {
            if (nums1[p] >= nums2[q]) {
                nums1[it--] = nums1[p--];
            } else {
                nums1[it--] = nums2[q--];
            }
        }

        // Edge case: Copy remaining elements from nums2 if nums1 finished early
        while (q >= 0) {
            nums1[it--] = nums2[q--];
        }
    }
}

```

```cpp []
class Solution {
public:
    void merge(vector<int>& nums1, int m, vector<int>& nums2, int n) {
        int p = m - 1;
        int q = n - 1;
        int it = (m + n) - 1;

        while (p >= 0 && q >= 0) {
            if (nums1[p] >= nums2[q]) {
                nums1[it--] = nums1[p--];
            } else {
                nums1[it--] = nums2[q--];
            }
        }

        while (q >= 0) {
            nums1[it--] = nums2[q--];
        }
    }
};

```

```python []
class Solution:
    def merge(self, nums1: List[int], m: int, nums2: List[int], n: int) -> None:
        p = m - 1
        q = n - 1
        it = (m + n) - 1

        while p >= 0 and q >= 0:
            if nums1[p] >= nums2[q]:
                nums1[it] = nums1[p]
                p -= 1
            else:
                nums1[it] = nums2[q]
                q -= 1
            it -= 1

        # Copy remaining elements from nums2 if any
        while q >= 0:
            nums1[it] = nums2[q]
            q -= 1
            it -= 1

```

### **Complexity Analysis**

* **Time Complexity:** $O(m + n)$, We process each element from both arrays at most once.
* **Space Complexity:** $O(1)$,  No extra memory allocated. The process is completely **in-place**.