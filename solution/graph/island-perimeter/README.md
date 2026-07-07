# 463. Island Perimeter

<p align="right">Last updated - 07.07.2026</p>

- https://leetcode.com/problems/island-perimeter/description/

## Approach 1: Depth-First Search (DFS)

### Intuition

Think of each land cell as a square with 4 potential perimeter edges. When performing a DFS from a land cell, we look in all 4 directions.

- If our step goes **out of bounds** or lands on a **water cell (`0`)**, it means we have crossed an external edge of the island. We count this edge by returning `1`.
- If we hit an **already visited land cell**, it represents an internal connection between land masses, which does not form part of the outer perimeter. We return `0`.

### Algorithm

1. Scan the grid to find the first land cell (`1`). Since there is only one island, starting a DFS from this first cell is sufficient.
2. Inside the DFS helper:
   - **Base Case:** If the current index is out of bounds, return `1`.
   - **Base Case:** If the cell is water (`0`), return `1`.
   - **Base Case:** If the cell is already visited (`vis[i][j] == 1`), return `0`.

3. Mark the cell as visited (`vis[i][j] = 1`).
4. Recursively sum up the results of moving in all **4 directions** (Up, Down, Left, Right).
5. Return the cumulative perimeter.

### Implementation

**Java**

```java []
class Solution {
    public int islandPerimeter(int[][] grid) {
        int r = grid.length;
        int c = grid[0].length;
        int[][] vis = new int[r][c];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (grid[i][j] == 1) {
                    // Start DFS from the first land cell found and return immediately
                    return dfs(i, j, r, c, grid, vis);
                }
            }
        }
        return 0;
    }

    private int dfs(int i, int j, int r, int c, int[][] grid, int[][] vis) {
        // Out of bound or water cell indicates an exposed perimeter edge
        if (i < 0 || i >= r || j < 0 || j >= c) return 1;
        if (grid[i][j] == 0) return 1;
        if (vis[i][j] == 1) return 0;

        vis[i][j] = 1; // Mark as visited

        int perimeter = 0;
        perimeter += dfs(i - 1, j, r, c, grid, vis); // up
        perimeter += dfs(i + 1, j, r, c, grid, vis); // down
        perimeter += dfs(i, j - 1, r, c, grid, vis); // left
        perimeter += dfs(i, j + 1, r, c, grid, vis); // right

        return perimeter;
    }
}

```

**C++**

```cpp []
#include <vector>
using namespace std;

class Solution {
public:
    int islandPerimeter(vector<vector<int>>& grid) {
        int r = grid.size();
        int c = grid[0].size();
        vector<vector<int>> vis(r, vector<int>(c, 0));

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (grid[i][j] == 1) {
                    return dfs(i, j, r, c, grid, vis);
                }
            }
        }
        return 0;
    }

private:
    int dfs(int i, int j, int r, int c, const vector<vector<int>>& grid, vector<vector<int>>& vis) {
        if (i < 0 || i >= r || j < 0 || j >= c) return 1;
        if (grid[i][j] == 0) return 1;
        if (vis[i][j] == 1) return 0;

        vis[i][j] = 1;

        int perimeter = 0;
        perimeter += dfs(i - 1, j, r, c, grid, vis); // up
        perimeter += dfs(i + 1, j, r, c, grid, vis); // down
        perimeter += dfs(i, j - 1, r, c, grid, vis); // left
        perimeter += dfs(i, j + 1, r, c, grid, vis); // right

        return perimeter;
    }
};

```

### Complexity Analysis

- **Time Complexity:** $\mathcal{O}(R \times C)$
  - **Grid Scan:** Checking every cell to find the starting point takes $\mathcal{O}(R \times C)$ time.
  - **DFS Exploration:** Every land cell is visited exactly once due to the tracking array (`vis`). From each cell, we check 4 constant directions. In the worst case where the entire grid is one island, DFS processes all cells, making the total time $\mathcal{O}(R \times C)$.

- **Space Complexity:** $\mathcal{O}(R \times C)$
  - **Visited Array:** Allocating a tracking grid of size $R \times C$ takes $\mathcal{O}(R \times C)$ space.
  - **Call Stack:** In a worst-case snake-like grid layout, the recursion stack depth can reach $\mathcal{O}(R \times C)$.

## Approach 2. Iterative

### Intuition

Instead of exploring neighbors recursively, we can look at every single land cell independently. If a cell contains land (`1`), we actively look around its 4 adjacent neighbors. Every neighbor that is either **out of bounds** or **filled with water (`0`)** represents a physical boundary edge. We simply add up these boundary edges on the fly.

### Algorithm

1. Initialize `perimeter = 0`.
2. Set up a direction matrix `dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}` representing up, down, left, and right directions.
3. Traverse the entire grid using a nested loop.
4. If `grid[i][j] == 1`:

    - Check all 4 directions around the current cell.
    - If a neighbor cell coordinates `(nr, nc)` goes out of bounds, or if `grid[nr][nc] == 0`, increment `perimeter`.

5. Return the total `perimeter`.

### Implementation

**Java**

```java []
class Solution {
    public int islandPerimeter(int[][] grid) {
        int r = grid.length;
        int c = grid[0].length;
        int perimeter = 0;

        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (grid[i][j] == 1) {
                    // Check all four directions
                    for (int[] dir : dirs) {
                        int nr = i + dir[0];
                        int nc = j + dir[1];

                        // When out of bounds or water, increment perimeter edge
                        if (nr < 0 || nr >= r || nc < 0 || nc >= c || grid[nr][nc] == 0) {
                            perimeter++;
                        }
                    }
                }
            }
        }
        return perimeter;
    }
}

```

**C++** 

```cpp []
#include <vector>
using namespace std;

class Solution {
public:
    int islandPerimeter(vector<vector<int>>& grid) {
        int r = grid.size();
        int c = grid[0].size();
        int perimeter = 0;

        int dirs[4][2] = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (grid[i][j] == 1) {
                    // Check all four directions
                    for (int d = 0; d < 4; d++) {
                        int nr = i + dirs[d][0];
                        int nc = j + dirs[d][1];

                        // When out of bounds or water, increment perimeter edge
                        if (nr < 0 || nr >= r || nc < 0 || nc >= c || grid[nr][nc] == 0) {
                            perimeter++;
                        }
                    }
                }
            }
        }
        return perimeter;
    }
};

```

### Complexity Analysis

- **Time Complexity:** $\mathcal{O}(R \times C)$
  - The nested loops scan every single cell of the grid exactly once. For each land cell, checking the 4 directions takes a constant $\mathcal{O}(1)$ time.

- **Space Complexity:** $\mathcal{O}(1)$
  - No auxiliary matrices or recursive call stacks are utilized. We only use a fixed direction array and basic counter variables, which consumes constant extra space.
