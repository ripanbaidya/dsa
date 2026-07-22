# [1631. Path With Minimum Effort](https://leetcode.com/problems/path-with-minimum-effort/description/)

<p align="right">Last updated - 22.07.2026</p>

To solve the **"Path With Minimum Effort"** problem using **Dijkstra's Algorithm**, we treat the 2D grid as a weighted, directed graph where:

- **Nodes (Vertices):** Each cell `(row, col)` in the grid.
- **Edges:** Connections to the 4 neighboring cells (up, down, left, right).
- **Edge Weight:** The absolute difference in heights between the two adjacent cells: $\vert{}heights[r_1][c_1] - heights[r_2][c_2]\vert{}$.

Our goal is to find a path from the top-left cell `(0, 0)` to the bottom-right cell `(rows-1, cols-1)` such that the **maximum edge weight along the path is minimized**.

### Why Dijkstra's Algorithm Works Here

Standard Dijkstra finds the path with the _minimum sum_ of edge weights. However, with a slight modification to how we update (relax) our distances, we can use it to find the path with the **minimized maximum step (effort)**.

Instead of adding weights:

$$\text{effort to neighbor} = \max(\text{effort to current cell}, \text{difference between current and neighbor})$$

Because we always want to explore the path of least effort first, a **Min-Priority Queue (Min-Heap)** guarantees we find the optimal path in the shortest time.

## Algorithm

1. **Initialize the Effort Table:** Create a 2D array `efforts` of the same size as `heights`, initialized to $\infty$ (infinity). Set `efforts[0][0] = 0` because starting at the first cell requires 0 effort.

2. **Initialize the Priority Queue:** Use a Min-Heap storing elements in the format `(effort, row, col)`. Push the starting cell: `(0, 0, 0)` into the heap.

3. **Process the Queue (Dijkstra's loop):** While the Priority Queue is not empty:
   - Pop the cell `(current_effort, r, c)` with the lowest effort.
   - **Optimization/Base Case:** If `(r, c)` is the bottom-right destination `(rows-1, cols-1)`, return `current_effort` immediately. This is guaranteed to be the minimum effort.
   - If `current_effort` is greater than the already recorded `efforts[r][c]`, skip processing (it's an outdated path).
   - **Explore Neighbors:** For each of the 4 adjacent neighbors `(nr, nc)`:
     - Calculate the weight of the step: `step_effort = abs(heights[r][c] - heights[nr][nc])`.
     - Calculate the total effort to reach this neighbor through the current cell: `new_effort = max(current_effort, step_effort)`.
     - **Relaxation:** If `new_effort` is strictly less than the previously recorded `efforts[nr][nc]`:
       - Update `efforts[nr][nc] = new_effort`.
       - Push `(new_effort, nr, nc)` into the Priority Queue.

## Implementation

```java []
class Solution {
    public int minimumEffortPath(int[][] heights) {
        int rows = heights.length;
        int cols = heights[0].length;

        // Direction vectors for moving: up, down, left, right
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // efforts[r][c] stores the minimum effort to reach cell (r, c)
        int[][] efforts = new int[rows][cols];
        for (int[] row : efforts) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        efforts[0][0] = 0;

        // Min-Heap: stores array {effort, row, col}, sorted by effort
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        pq.offer(new int[]{0, 0, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int currentEffort = current[0];
            int r = current[1];
            int c = current[2];

            // If we reached the destination, return the effort
            if (r == rows - 1 && c == cols - 1) {
                return currentEffort;
            }

            // If we found a shorter way to this cell already, skip
            if (currentEffort > efforts[r][c]) {
                continue;
            }

            // Explore all 4 directions
            for (int[] dir : directions) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                // Check grid boundaries
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                    // Effort required to transition from (r,c) to (nr,nc)
                    int stepEffort = Math.abs(heights[r][c] - heights[nr][nc]);
                    // Total effort of the path to (nr, nc)
                    int newEffort = Math.max(currentEffort, stepEffort);

                    // If this path offers a lower effort to reach (nr, nc)
                    if (newEffort < efforts[nr][nc]) {
                        efforts[nr][nc] = newEffort;
                        pq.offer(new int[]{newEffort, nr, nc});
                    }
                }
            }
        }

        return 0; // Fallback
    }
}

```

## Complexity Analysis

- **Time Complexity:** $\mathcal{O}(V \log V + E) = \mathcal{O}(M \cdot N \log(M \cdot N))$ where $M$ is the number of rows and $N$ is the number of columns. Each cell is pushed and popped from the priority queue at most once, and each transition takes logarithmic time.

- **Space Complexity:** $\mathcal{O}(M \cdot N)$ to store the 2D `efforts` array and the elements in the priority queue.

## Dry-run

**Input Grid (`heights`):**

$$\begin{pmatrix} 1 & 2 & 2 \\ 3 & 8 & 2 \\ 5 & 3 & 5 \end{pmatrix}$$

- **Dimensions:** $3 \times 3$ (Rows: $0 \text{ to } 2$, Cols: $0 \text{ to } 2$).
- **Destination Cell:** $(2, 2)$ (bottom-right, value is $5$).

**Variables:**

1. **`efforts` Array (initialized to $\infty$):**

$$\begin{pmatrix} 0 & \infty & \infty \\ \infty & \infty & \infty \\ \infty & \infty & \infty \end{pmatrix}$$

_(Note: `efforts[0][0]` is set to `0` since we start there with zero initial effort)._ 2. **Priority Queue (`pq` - Min-Heap):**
Stores elements as `[effort, row, col]`, sorted by `effort`.

- Initial state: `pq = [ [0, 0, 0] ]`

---

### Step-by-Step Execution

#### **Iteration 1**

1. **Pop from `pq`:** `[0, 0, 0]` (Cell $(0, 0)$, current effort = $0$).
2. **Destination Check:** Is $(0, 0) == (2, 2)$? No.
3. **Validity Check:** Is `currentEffort (0) > efforts[0][0] (0)`? No, continue.
4. **Explore 4 Neighbors of $(0, 0)$:**
   - **Neighbor Down: $(1, 0)$ (height = $3$):**
     - `stepEffort = |heights[0][0] - heights[1][0]| = |1 - 3| = 2`
     - `newEffort = max(currentEffort, stepEffort) = max(0, 2) = 2`
     - Since `newEffort (2) < efforts[1][0] (∞)`:
       - Update `efforts[1][0] = 2`
       - Push `[2, 1, 0]` to `pq`.

   - **Neighbor Right: $(0, 1)$ (height = $2$):**
     - `stepEffort = |heights[0][0] - heights[0][1]| = |1 - 2| = 1`
     - `newEffort = max(0, 1) = 1`
     - Since `newEffort (1) < efforts[0][1] (∞)`:
       - Update `efforts[0][1] = 1`
       - Push `[1, 0, 1]` to `pq`.

**End of Iteration 1:**

- **`efforts`:**

$$\begin{pmatrix} 0 & 1 & \infty \\ 2 & \infty & \infty \\ \infty & \infty & \infty \end{pmatrix}$$

- **`pq`:** `[ [1, 0, 1], [2, 1, 0] ]` (sorted by lowest effort first).

#### **Iteration 2**

1. **Pop from `pq`:** `[1, 0, 1]` (Cell $(0, 1)$, current effort = $1$).
2. **Destination Check:** Is $(0, 1) == (2, 2)$? No.
3. **Explore Neighbors of $(0, 1)$:**
   - **Neighbor Left: $(0, 0)$ (height = $1$):**
     - `stepEffort = |2 - 1| = 1`
     - `newEffort = max(1, 1) = 1`
     - Is `newEffort (1) < efforts[0][0] (0)`? No, ignore.

   - **Neighbor Down: $(1, 1)$ (height = $8$):**
     - `stepEffort = |2 - 8| = 6`
     - `newEffort = max(1, 6) = 6`
     - Since `newEffort (6) < efforts[1][1] (∞)`:
       - Update `efforts[1][1] = 6`
       - Push `[6, 1, 1]` to `pq`.

   - **Neighbor Right: $(0, 2)$ (height = $2$):**
     - `stepEffort = |2 - 2| = 0`
     - `newEffort = max(1, 0) = 1`
     - Since `newEffort (1) < efforts[0][2] (∞)`:
       - Update `efforts[0][2] = 1`
       - Push `[1, 0, 2]` to `pq`.

**End of Iteration 2:**

- **`efforts`:**

$$\begin{pmatrix} 0 & 1 & 1 \\ 2 & 6 & \infty \\ \infty & \infty & \infty \end{pmatrix}$$

- **`pq`:** `[ [1, 0, 2], [2, 1, 0], [6, 1, 1] ]`

---

#### **Iteration 3**

1. **Pop from `pq`:** `[1, 0, 2]` (Cell $(0, 2)$, current effort = $1$).
2. **Destination Check:** Is $(0, 2) == (2, 2)$? No.
3. **Explore Neighbors of $(0, 2)$:**
   - **Neighbor Left: $(0, 1)$ (height = $2$):**
     - `stepEffort = |2 - 2| = 0` -> `newEffort = max(1, 0) = 1`. Not smaller than `efforts[0][1] (1)`. Ignore.

   - **Neighbor Down: $(1, 2)$ (height = $2$):**
     - `stepEffort = |2 - 2| = 0`
     - `newEffort = max(1, 0) = 1`
     - Since `newEffort (1) < efforts[1][2] (∞)`:
       - Update `efforts[1][2] = 1`
       - Push `[1, 1, 2]` to `pq`.

**End of Iteration 3:**

- **`efforts`:**

$$\begin{pmatrix} 0 & 1 & 1 \\ 2 & 6 & 1 \\ \infty & \infty & \infty \end{pmatrix}$$

- **`pq`:** `[ [1, 1, 2], [2, 1, 0], [6, 1, 1] ]`

---

#### **Iteration 4**

1. **Pop from `pq`:** `[1, 1, 2]` (Cell $(1, 2)$, current effort = $1$).
2. **Destination Check:** Is $(1, 2) == (2, 2)$? No.
3. **Explore Neighbors of $(1, 2)$:**
   - **Neighbor Up: $(0, 2)$:** Already explored/no improvement.
   - **Neighbor Left: $(1, 1)$ (height = $8$):**
     - `stepEffort = |2 - 8| = 6` -> `newEffort = max(1, 6) = 6`. Not smaller than `efforts[1][1] (6)`. Ignore.

   - **Neighbor Down: $(2, 2)$ (height = $5$):**
     - `stepEffort = |2 - 5| = 3`
     - `newEffort = max(1, 3) = 3`
     - Since `newEffort (3) < efforts[2][2] (∞)`:
       - Update `efforts[2][2] = 3`
       - Push `[3, 2, 2]` to `pq`.

**End of Iteration 4:**

- **`efforts`:**

$$\begin{pmatrix} 0 & 1 & 1 \\ 2 & 6 & 1 \\ \infty & \infty & 3 \end{pmatrix}$$

- **`pq`:** `[ [2, 1, 0], [3, 2, 2], [6, 1, 1] ]`

---

#### **Iteration 5**

1. **Pop from `pq`:** `[2, 1, 0]` (Cell $(1, 0)$, current effort = $2$).
2. **Destination Check:** Is $(1, 0) == (2, 2)$? No.
3. **Explore Neighbors of $(1, 0)$:**
   - **Neighbor Up: $(0, 0)$:** Ignore.
   - **Neighbor Right: $(1, 1)$ (height = $8$):**
     - `stepEffort = |3 - 8| = 5` -> `newEffort = max(2, 5) = 5`.
     - Since `5 < efforts[1][1] (6)`:
       - Update `efforts[1][1] = 5` (We found a slightly better path to $(1,1)$!)
       - Push `[5, 1, 1]` to `pq`.

   - **Neighbor Down: $(2, 0)$ (height = $5$):**
     - `stepEffort = |3 - 5| = 2`
     - `newEffort = max(2, 2) = 2`
     - Since `newEffort (2) < efforts[2][0] (∞)`:
       - Update `efforts[2][0] = 2`
       - Push `[2, 2, 0]` to `pq`.

**End of Iteration 5:**

- **`efforts`:**

$$\begin{pmatrix} 0 & 1 & 1 \\ 2 & 5 & 1 \\ 2 & \infty & 3 \end{pmatrix}$$

- **`pq`:** `[ [2, 2, 0], [3, 2, 2], [5, 1, 1], [6, 1, 1] ]` _(Notice we now have a redundant duplicate of cell $(1,1)$ in the heap, which is normal for Dijkstra. The one with effort 5 will be processed first, and the old effort 6 will be skipped)._

---

#### **Iteration 6**

1. **Pop from `pq`:** `[2, 2, 0]` (Cell $(2, 0)$, current effort = $2$).
2. **Destination Check:** Is $(2, 0) == (2, 2)$? No.
3. **Explore Neighbors of $(2, 0)$:**
   - **Neighbor Up: $(1, 0)$:** Ignore.
   - **Neighbor Right: $(2, 1)$ (height = $3$):**
     - `stepEffort = |5 - 3| = 2`
     - `newEffort = max(2, 2) = 2`
     - Since `newEffort (2) < efforts[2][1] (∞)`:
       - Update `efforts[2][1] = 2`
       - Push `[2, 2, 1]` to `pq`.

**End of Iteration 6:**

- **`efforts`:**

$$\begin{pmatrix} 0 & 1 & 1 \\ 2 & 5 & 1 \\ 2 & 2 & 3 \end{pmatrix}$$

- **`pq`:** `[ [2, 2, 1], [3, 2, 2], [5, 1, 1], [6, 1, 1] ]`

---

#### **Iteration 7**

1. **Pop from `pq`:** `[2, 2, 1]` (Cell $(2, 1)$, current effort = $2$).
2. **Destination Check:** Is $(2, 1) == (2, 2)$? No.
3. **Explore Neighbors of $(2, 1)$:**
   - **Neighbor Left: $(2, 0)$:** Ignore.
   - **Neighbor Up: $(1, 1)$ (height = $8$):**
     - `stepEffort = |3 - 8| = 5` -> `newEffort = max(2, 5) = 5`. Not smaller than `efforts[1][1] (5)`. Ignore.

   - **Neighbor Right: $(2, 2)$ (height = $5$):**
     - `stepEffort = |3 - 5| = 2`
     - `newEffort = max(2, 2) = 2`
     - Since `newEffort (2) < efforts[2][2] (3)`:
       - Update `efforts[2][2] = 2` (We found a cheaper path to the target!)
       - Push `[2, 2, 2]` to `pq`.

**End of Iteration 7:**

- **`efforts`:**

$$\begin{pmatrix} 0 & 1 & 1 \\ 2 & 5 & 1 \\ 2 & 2 & 2 \end{pmatrix}$$

- **`pq`:** `[ [2, 2, 2], [3, 2, 2], [5, 1, 1], [6, 1, 1] ]`

---

#### **Iteration 8**

1. **Pop from `pq`:** `[2, 2, 2]` (Cell $(2, 2)$—**our destination!**, current effort = $2$).
2. **Destination Check:** Is $(2, 2) == (2, 2)$? **YES!**
3. **Return:** The algorithm immediately returns `currentEffort`, which is **`2`**.

### Conclusion

The path taken to achieve this minimum effort of **2** was:

$$(0,0) \to (1,0) \to (2,0) \to (2,1) \to (2,2)$$

Which corresponds to heights:

$$1 \to 3 \to 5 \to 3 \to 5$$

The differences are:

$$\vert{}1-3\vert{}=2,\quad \vert{}3-5\vert{}=2,\quad \vert{}5-3\vert{}=2,\quad \vert{}3-5\vert{}=2$$

The maximum absolute difference along this route is indeed **2**, which matches LeetCode's expected output!
