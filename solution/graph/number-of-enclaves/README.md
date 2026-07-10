# 1020. Number of Enclaves

<p align="right">Last updated - 11.07.2026</p>

- https://leetcode.com/problems/number-of-enclaves/description/

## Approach 1: Depth-First Search (DFS)

### Intuition

Using the boundary-traversal logic, we loop through all the cells on the four borders of the grid. Whenever we encounter a land cell (`grid[r][c] == 1`), we trigger a DFS traversal. The DFS recursively visits all four cardinal directions (up, down, left, right) to find connected land cells.

To optimize space and avoid using a separate `visited[][]` array, we perform an **in-place transformation**. We change every visited valid land cell to `0`. This effectively sinks the boundary-connected islands. Finally, a simple nested loop counts the surviving `1`s.

### Algorithm

1. **Identify the Boundaries:**
   - Loop through the **top and bottom rows** ($r = 0$ and $r = m-1$).
   - Loop through the **left and right columns** ($c = 0$ and $c = n-1$).

2. **Trigger the Traversal:**
   - For every boundary cell, if its value is `1` (land), invoke the `dfs` function for that cell coordinate $(r, c)$.

3. **Define the Recursive `dfs(r, c)` Function:**
   - **Base Cases:** If the current coordinates $(r, c)$ are out of bounds ($r < 0, r \ge m, c < 0, c \ge n$) or the cell value is `0` (water/already visited), return immediately.
   - **Sink the Cell:** Change the value of the current cell to `0` (`grid[r][c] = 0`). This acts as an in-place "visited" marker and prevents endless cycles.
   - **Explore Neighbors:** Recursively call `dfs` on the four cardinal neighbors:
     - Up: $(r-1, c)$
     - Down: $(r+1, c)$
     - Left: $(r, c-1)$
     - Right: $(r, c+1)$

4. **Count Remaining Enclaves:**
   - After completing the boundary traversals, iterate through the entire grid using a nested loop.
   - Count the number of cells that still contain a `1`. These cells were completely isolated from the borders.
   - Return the final count.

### Implementations

**Java**

```java []
class Solution {
    public int numEnclaves(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        // Step 1: Traverse Top & Bottom boundaries
        for (int col = 0; col < n; col++) {
            dfs(0, col, grid);       // Top boundary
            dfs(m - 1, col, grid);   // Bottom boundary
        }

        // Step 2: Traverse Left & Right boundaries
        for (int row = 0; row < m; row++) {
            dfs(row, 0, grid);       // Left boundary
            dfs(row, n - 1, grid);   // Right boundary
        }

        // Step 3: Count remaining unvisited land cells (enclaves)
        int land = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1)
                    land++;
            }
        }
        return land;
    }

    private void dfs(int r, int c, int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        // Base case: Out of bounds or encountered water (0)
        if (r < 0 || r >= m || c < 0 || c >= n || grid[r][c] == 0)
            return;

        // Sink the land cell to prevent duplicate processing (acts as visited)
        grid[r][c] = 0;

        // Direction vectors for moving: Up, Down, Left, Right
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : dirs) {
            dfs(r + dir[0], c + dir[1], grid);
        }
    }
}

```

**C++**

```cpp []
#include <vector>

class Solution {
private:
    void dfs(int r, int c, std::vector<std::vector<int>>& grid) {
        int m = grid.size();
        int n = grid[0].size();

        // Base case: check boundary violations or if cell is already water
        if (r < 0 || r >= m || c < 0 || c >= n || grid[r][c] == 0) {
            return;
        }

        // Mark current cell as visited by converting it to water
        grid[r][c] = 0;

        // Direction arrays for traveling Up, Down, Left, Right
        int delRow[] = {-1, 1, 0, 0};
        int delCol[] = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            dfs(r + delRow[i], c + delCol[i], grid);
        }
    }

public:
    int numEnclaves(std::vector<std::vector<int>>& grid) {
        int m = grid.size();
        int n = grid[0].size();

        // Step 1: Scan top and bottom borders
        for (int col = 0; col < n; col++) {
            dfs(0, col, grid);
            dfs(m - 1, col, grid);
        }

        // Step 2: Scan left and right borders
        for (int row = 0; row < m; row++) {
            dfs(row, 0, grid);
            dfs(row, n - 1, grid);
        }

        // Step 3: Count surviving land cells
        int landCount = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    landCount++;
                }
            }
        }

        return landCount;
    }
};
```

### Complexity Analysis

- **Time Complexity:** $\mathcal{O}(m \times n)$
  We scan the boundaries first, which takes $\mathcal{O}(m + n)$ time. In the worst-case scenario (e.g., the grid is filled entirely with land), the DFS traversal visits every single matrix element exactly once, leading to $\mathcal{O}(m \times n)$ operations. The final nested loop also takes $\mathcal{O}(m \times n)$ time.

- **Space Complexity:** $\mathcal{O}(m \times n)$
  Since we modify the input matrix in place to denote visited states, we do not require extra linear auxiliary memory buffers. However, the runtime call stack for the recursive DFS can deep dive up to $\mathcal{O}(m \times n)$ times in a catastrophic worst-case layout (e.g., a winding snake-like single land strip).

## Approach 2: Breadth-First Search (BFS)

### Intuition

An alternative approach to parsing the boundary paths is Breadth-First Search (BFS). Instead of handling each boundary land cell deeply one by one via recursion, we harvest _all_ boundary land coordinates upfront and queue them up together.

Using a queue structure allows us to process the boundary connected components level by level. Just like the DFS method, as cells are popped from the queue, we sink them to `0` and insert any of their valid unvisited land neighbors into the queue.

### Algorithm

1. **Initialize a Queue:**
   - Create an empty queue to store the coordinate pairs $(r, c)$ of land cells.

2. **Collect and Sink Boundary Entry Points:**
   - Iterate through every cell on the four outer edges of the grid.
   - If a boundary cell contains a `1`:
     - Push its coordinates $(r, c)$ into the queue.
     - Flip its value to `0` immediately to mark it as visited.

3. **Process the Queue (Multi-Source BFS):**
   - While the queue is not empty:
     - Pop the front element $(r, c)$ from the queue.
     - For each of its four cardinal neighbors (Up, Down, Left, Right), calculate the new coordinates $(nr, nc)$.
   - Check if the neighbor is **valid**:
     - Within grid boundaries ($0 \le nr < m$ and $0 \le nc < n$).
     - Is a land cell (`grid[nr][nc] == 1`).

   - If valid, flip `grid[nr][nc]` to `0` and push $(nr, nc)$ into the queue.

4. **Count Remaining Enclaves:**
   - Scan the grid with a nested loop.
   - Total up all surviving `1`s that were untouched by the BFS flood fill.
   - Return the total count.

### Implementations

**Java**

```java []
class Solution {
    public int numEnclaves(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        Queue<int[]> queue = new LinkedList<>();

        // Step 1: Push all boundary land cells into the queue and sink them
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // Check if the cell is on any of the 4 borders
                if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                    if (grid[i][j] == 1) {
                        queue.add(new int[]{i, j});
                        grid[i][j] = 0; // Mark as visited instantly
                    }
                }
            }
        }

        // Step 2: Multi-source BFS traversal
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0];
            int c = cell[1];

            for (int[] dir : dirs) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                // If neighbor is inside boundaries and is land, add to queue
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] == 1) {
                    grid[nr][nc] = 0;
                    queue.add(new int[]{nr, nc});
                }
            }
        }

        // Step 3: Count remaining enclaves
        int land = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) land++;
            }
        }
        return land;
    }
}

```

**C++**

```cpp []
class Solution {
public:
    int numEnclaves(std::vector<std::vector<int>>& grid) {
        int m = grid.size();
        int n = grid[0].size();
        std::queue<std::pair<int, int>> q;

        // Step 1: Collect boundary land cells
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                    if (grid[i][j] == 1) {
                        q.push({i, j});
                        grid[i][j] = 0; // Sink cell
                    }
                }
            }
        }

        // Step 2: Run level-by-level BFS
        int delRow[] = {-1, 1, 0, 0};
        int delCol[] = {0, 0, -1, 1};

        while (!q.empty()) {
            auto [r, c] = q.front();
            q.pop();

            for (int i = 0; i < 4; i++) {
                int nr = r + delRow[i];
                int nc = c + delCol[i];

                if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] == 1) {
                    grid[nr][nc] = 0;
                    q.push({nr, nc});
                }
            }
        }

        // Step 3: Count remaining enclaves
        int landCount = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    landCount++;
                }
            }
        }

        return landCount;
    }
};
```

### Complexity Analysis

- **Time Complexity:** $\mathcal{O}(m \times n)$
  Finding the starting boundaries checks all cells, taking $\mathcal{O}(m \times n)$ time. The multi-source BFS handles every cell and its neighbors at most once, which ensures the linear processing bound stays strictly bound within $\mathcal{O}(m \times n)$.
- **Space Complexity:** $\mathcal{O}(m \times n)$
  No extra structures are utilized for tracking visits. The space consumption is completely governed by the `Queue` storage capacity, which can hold up to $\mathcal{O}(m \times n)$ references concurrently during heavy network spreads.

## Core Concept: Why Boundary Traversal?

An **enclave** is defined as a set of land cells (`1`s) from which we _cannot_ walk off the boundary of the grid.

By definition, any land cell that touches the boundary—or is connected to another land cell touching the boundary—can **never** be part of an enclave.

Instead of searching from the inside out, the most efficient approach is to work from the **outside in**:

1. Identify all land cells located right on the 4 boundaries (top, bottom, left, right).
2. Use these boundary cells as starting anchor points to initiate a traversal (DFS or BFS).
3. Any land cell reachable from these boundaries is explicitly marked as "not an enclave" by flipping its value to `0` (water).
4. After traversing all boundary-connected chains, any remaining `1`s left unchanged in the grid are isolated from the boundaries—these are our enclaves.
