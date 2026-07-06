## 695. Max Area of Island

<p align="right">Last updated - 07.07.2026</p>

- https://leetcode.com/problems/max-area-of-island/description/

## DFS (Depth-First Search) Approach

### Intuition

The problem asks us to find the maximum area of an island, where an island is a 4-directionally connected group of `1`s (land). Think of this as finding the largest connected component in a 2D grid graph. When we encounter a piece of land (`1`), we can explore its entire island by moving in all four valid directions (up, down, left, right) as far as possible. To avoid counting the same land cell multiple times and getting stuck in an infinite loop, we can sink the island (turn `1`s to `0`s) as we visit each cell.

### Approach

1. Iterate through every cell of the grid using nested loops.
2. If a cell contains `1`, it means we've found a piece of an island. We trigger a DFS from this cell to calculate the total area of this specific island.
3. In the DFS function:

    - **Base Case:** If the current position is out of bounds or the cell is `0` (water/visited), return `0`.
    - **Mark Visited:** Set the current cell to `0` so it won't be processed again.
    - **Recursive Exploration:** Recursively call DFS for all four adjacent cells (up, down, left, right).
    - **Return Area:** The area of the current subgrid is $1 + \text{sum of areas from all 4 directions}$.

4. Update our maximum area found so far with the area of the current island.
5. Return the maximum area.

### Implementation

#### Java

```java []
class Solution {
    public int maxAreaOfIsland(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int maxArea = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    int[] area = new int[1];
                    maxArea = Math.max(maxArea, dfs(grid, i, j, m, n, area));
                    maxArea = Math.max(maxArea, area[0]);
                }
            }
        }
        return maxArea;
    }

    private int dfs(int[][] grid, int r, int c, int m, int n, int[] area) {
        // Base case: boundaries and water check
        if (r < 0 || r >= m || c < 0 || c >= n || grid[r][c] == 0) {
            return 0;
        }

        // Mark as visited
        grid[r][c] = 0;
        // Count current cell
        area[0] ++;

        dfs(grid, r - 1, c, m, n, area)  // Up
        dfs(grid, r + 1, c, m, n, area)  // Down
        dfs(grid, r, c - 1, m, n, area)  // Left
        dfs(grid, r, c + 1, m, n, area); // Right
    }
}

```

#### C++

```cpp []
class Solution {
public:
    int maxAreaOfIsland(vector<vector<int>>& grid) {
        int m = grid.size();
        int n = grid[0].size();
        int maxArea = 0;

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (grid[i][j] == 1) {
                    maxArea = max(maxArea, dfs(grid, i, j, m, n));
                }
            }
        }
        return maxArea;
    }

private:
    int dfs(vector<vector<int>>& grid, int r, int c, int m, int n) {
        if (r < 0 || r >= m || c < 0 || c >= n || grid[r][c] == 0) {
            return 0;
        }

        grid[r][c] = 0; // Mark as visited

        return 1 + dfs(grid, r - 1, c, m, n)
                 + dfs(grid, r + 1, c, m, n)
                 + dfs(grid, r, c - 1, m, n)
                 + dfs(grid, r, c + 1, m, n);
    }
};

```

### Complexity Analysis

- **Time Complexity:** $O(M \times N)$, where $M$ is the number of rows and $N$ is the number of columns. We visit every cell at most a constant number of times.
- **Space Complexity:** $O(M \times N)$ in the worst case (e.g., the entire grid is one giant island) due to the recursive call stack depth of DFS.

---

## BFS (Breadth-First Search) Approach

### Intuition

Instead of exploring deeply into one direction like DFS, BFS expands outward layer by layer like a ripple in a pond. Using a queue, we can visit a land cell, add its unvisited neighbors to the queue, and systematically compute the size of the island level by level.

### Approach

1. Iterate through every cell of the grid. If `grid[i][j] == 1`, start a BFS traversal.
2. Initialize a queue and push the starting cell coordinate `(i, j)` into it. Mark `grid[i][j] = 0` immediately to flag it as visited.
3. Keep track of a local `currentArea` counter initialized to 0.
4. While the queue is not empty:

    - Pop a cell from the queue and increment `currentArea`.
    - Look at its four neighbors. If a neighbor is within boundaries and is land (`1`), change its value to `0` (mark visited) and push its coordinates into the queue.

5. After the queue becomes empty, compare `currentArea` with `maxArea` and update it accordingly.

### Implementation

#### Java

```java []
import java.util.LinkedList;
import java.util.Queue;

class Solution {
    public int maxAreaOfIsland(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int maxArea = 0;

        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    int currentArea = 0;
                    Queue<int[]> queue = new LinkedList<>();

                    queue.offer(new int[]{i, j});
                    grid[i][j] = 0; // Mark visited immediately

                    while (!queue.isEmpty()) {
                        int[] cell = queue.poll();
                        currentArea++;

                        for (int d = 0; d < 4; d++) {
                            int nr = cell[0] + dRow[d];
                            int nc = cell[1] + dCol[d];

                            if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] == 1) {
                                queue.offer(new int[]{nr, nc});
                                grid[nr][nc] = 0; // Mark visited
                            }
                        }
                    }
                    maxArea = Math.max(maxArea, currentArea);
                }
            }
        }
        return maxArea;
    }
}

```

#### C++

```cpp []
#include <vector>
#include <queue>
#include <algorithm>

using namespace std;

class Solution {
public:
    int maxAreaOfIsland(vector<vector<int>>& grid) {
        int m = grid.size();
        int n = grid[0].size();
        int maxArea = 0;

        int dRow[] = {-1, 1, 0, 0};
        int dCol[] = {0, 0, -1, 1};

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (grid[i][j] == 1) {
                    int currentArea = 0;
                    queue<pair<int, int>> q;

                    q.push({i, j});
                    grid[i][j] = 0; // Mark visited

                    while (!q.empty()) {
                        auto [r, c] = q.front();
                        q.pop();
                        currentArea++;

                        for (int d = 0; d < 4; ++d) {
                            int nr = r + dRow[d];
                            int nc = c + dCol[d];

                            if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] == 1) {
                                q.push({nr, nc});
                                grid[nr][nc] = 0; // Mark visited
                            }
                        }
                    }
                    maxArea = max(maxArea, currentArea);
                }
            }
        }
        return maxArea;
    }
};

```

### Complexity Analysis

- **Time Complexity:** $O(M \times N)$. Every cell is processed and pushed to the queue at most once.
- **Space Complexity:** $O(\min(M, N))$ in the average matrix configuration due to the maximum queue size containing the perimeter of the island layer. In the absolute worst-case scenario (like a dense diagonal zig-zag island), it can scale up to $O(M \times N)$.
