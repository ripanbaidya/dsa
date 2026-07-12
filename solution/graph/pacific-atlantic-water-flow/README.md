# 417. Pacific Atlantic Water Flow

<p align="right">Last updated - 12.07.2026</p>

- https://leetcode.com/problems/pacific-atlantic-water-flow/description/

### Mistakes to Avoid: The Individual Cell Traversal Trap

The most common trap when approaching this problem is attempting to run a brand-new Depth-First Search (DFS) or Breadth-First Search (BFS) from **every individual cell** in the grid to see if it can reach both the Pacific and Atlantic oceans.

- **Why it fails:** If you start a search from each of the $M \times N$ cells, and each search potentially traverses the entire grid of size $M \times N$, your total time complexity balloons to $O((M \times N)^2)$. With grid constraints up to $200 \times 200$ ($40,000$ cells), this results in up to $1.6 \times 10^9$ operations, consistently triggering a **Time Limit Exceeded (TLE)** error.
- **The Lesson:** Do not track water flowing _downward_ from land to sea for every cell independently. Instead, invert the direction of the flow.

### Intuition

Instead of simulating water flowing down from the mountains to the oceans, imagine the oceans flooding **upward** into the island.

1. **Reverse the Flow:** Water naturally flows from a higher (or equal) elevation to a lower elevation. If we start at the ocean edges and move inward, we reverse this rule: we can only step onto an adjacent cell if its height is **greater than or equal to** our current cell's height ($\text{heights}[nr][nc] \ge \text{heights}[r][c]$).
2. **Independent Ocean Reachability:**
   - We find all cells reachable from the **Pacific Ocean** (Top and Left edges) by climbing upward.
   - We independently find all cells reachable from the **Atlantic Ocean** (Bottom and Right edges) by climbing upward.

3. **Intersection:** Any cell that gets marked as reachable by _both_ independent ocean searches is a point where water can flow to both oceans.

### Algorithm

1. Initialize two 2D boolean matrices, `pacific` and `atlantic`, of size $M \times N$ to track reachability.
2. Iterate through the grid edges:
   - For every column along the top edge (Pacific) and bottom edge (Atlantic), initiate a DFS.
   - For every row along the left edge (Pacific) and right edge (Atlantic), initiate a DFS.

3. Inside the `dfs(r, c, oceanGrid)` function:
   - Mark `oceanGrid[r][c] = true`.
   - Look at all 4 neighboring directions $(nr, nc)$.
   - Move to a neighbor if it is within bounds, has **not** been visited yet by this ocean's search, and satisfies the upward flow condition: $\text{heights}[nr][nc] \ge \text{heights}[r][c]$.

4. After completing the searches, traverse the entire grid. If `pacific[i][j]` and `atlantic[i][j]` are both true, add the coordinates `[i, j]` to the final result list.

### Implementation

**Java**

```java []
class Solution {
    // Direction vectors for moving up, down, left, right
    private final int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        List<List<Integer>> result = new ArrayList<>();
        if (heights == null || heights.length == 0 || heights[0].length == 0) {
            return result;
        }

        int m = heights.length;
        int n = heights[0].length;

        // Matrices to maintain reachability from each ocean
        boolean[][] pacific = new boolean[m][n];
        boolean[][] atlantic = new boolean[m][n];

        // 1. Start DFS from horizontal boundaries (Top and Bottom rows)
        for (int c = 0; c < n; c++) {
            dfs(0, c, heights, pacific);     // Top row touches Pacific
            dfs(m - 1, c, heights, atlantic); // Bottom row touches Atlantic
        }

        // 2. Start DFS from vertical boundaries (Left and Right columns)
        for (int r = 0; r < m; r++) {
            dfs(r, 0, heights, pacific);     // Left column touches Pacific
            dfs(r, n - 1, heights, atlantic); // Right column touches Atlantic
        }

        // 3. Find cells that can flow to both oceans
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (pacific[i][j] && atlantic[i][j]) {
                    result.add(Arrays.asList(i, j));
                }
            }
        }

        return result;
    }

    private void dfs(int r, int c, int[][] heights, boolean[][] visited) {
        // Mark the current cell as reachable from this ocean
        visited[r][c] = true;

        for (int[] dir : dirs) {
            int nr = r + dir[0];
            int nc = c + dir[1];

            // Check boundary limits
            if (nr >= 0 && nr < heights.length && nc >= 0 && nc < heights[0].length) {
                // Climb up: Neighbor must be >= current cell height and not yet visited
                if (!visited[nr][nc] && heights[nr][nc] >= heights[r][c]) {
                    dfs(nr, nc, heights, visited);
                }
            }
        }
    }
}

```

**C++**

```cpp []
class Solution {
private:
    // Direction vectors for moving up, down, left, right
    const vector<pair<int, int>> dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    void dfs(int r, int c, const vector<vector<int>>& heights, vector<vector<bool>>& visited) {
        // Mark the current cell as reachable from this ocean
        visited[r][c] = true;

        int m = heights.size();
        int n = heights[0].size();

        for (const auto& dir : dirs) {
            int nr = r + dir.first;
            int nc = c + dir.second;

            // Check boundary limits
            if (nr >= 0 && nr < m && nc >= 0 && nc < n) {
                // Climb up: Neighbor must be >= current cell height and not yet visited
                if (!visited[nr][nc] && heights[nr][nc] >= heights[r][c]) {
                    dfs(nr, nc, heights, visited);
                }
            }
        }
    }

public:
    vector<vector<int>> pacificAtlantic(vector<vector<int>>& heights) {
        vector<vector<int>> result;
        if (heights.empty() || heights[0].empty()) return result;

        int m = heights.size();
        int n = heights[0].size();

        // Matrices to maintain reachability from each ocean
        vector<vector<bool>> pacific(m, vector<bool>(n, false));
        vector<vector<bool>> atlantic(m, vector<bool>(n, false));

        // 1. Start DFS from horizontal boundaries (Top and Bottom rows)
        for (int c = 0; c < n; ++c) {
            dfs(0, c, heights, pacific);     // Top row touches Pacific
            dfs(m - 1, c, heights, atlantic); // Bottom row touches Atlantic
        }

        // 2. Start DFS from vertical boundaries (Left and Right columns)
        for (int r = 0; r < m; ++r) {
            dfs(r, 0, heights, pacific);     // Left column touches Pacific
            dfs(r, n - 1, heights, atlantic); // Right column touches Atlantic
        }

        // 3. Find cells that can flow to both oceans
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (pacific[i][j] && atlantic[i][j]) {
                    result.push_back({i, j});
                }
            }
        }

        return result;
    }
};

```

### Complexity Analysis

- **Time Complexity:** $\mathcal{O}(M \times N)$
  Each cell in the grid is visited at most a constant number of times across both ocean DFS sweeps (once for Pacific traversal and once for Atlantic traversal). Collecting the final results takes a final linear sweep over the grid elements.
- **Space Complexity:** $\mathcal{O}(M \times N)$
  The space is dominated by the two $M \times N$ boolean matrices (`pacific` and `atlantic`) to record reachability state. The maximum recursion stack depth for the DFS is also bounded by the total number of cells, $\mathcal{O}(M \times N)$, in the worst-case layout (e.g., a long continuous snake path).
