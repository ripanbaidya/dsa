# GFG. Find the Number of Islands

<p align="right">Last updated - 07.07.2026</p>

- https://www.geeksforgeeks.org/problems/find-the-number-of-islands/1

### Intuition

Think of the 2D grid as a graph where land cells (`'L'`) are nodes, and connected land cells form connected components (islands).

The goal is to find the total number of disconnected land groups. To do this:

1. We traverse the grid cell by cell.
2. When we encounter a piece of land (`'L'`), we know we have found a new island. We increment our island count.
3. To avoid counting the same island multiple times, we need to discover and sink (or mark) all the land cells connected to this starting point. We use **BFS** to spread out in all 8 directions from the initial cell, changing every connected `'L'` to water (`'W'`) to mark it as visited.

### Algorithm

1. Initialize a variable `cnt = 0` to store the number of islands.
2. Iterate through each cell $(i, j)$ of the grid using a nested loop.
3. If `grid[i][j] == 'L'`:

	- Increment `cnt` by 1.
	- Start a **BFS** from this cell to mark all connected land cells.

4. **BFS Mechanism:**

	- Create a queue and push the starting cell `(i, j)`. Mark it as visited by setting `grid[i][j] = 'W'`.
	- While the queue is not empty:
		- Dequeue the front cell $(r, c)$.
		- Check all **8 possible directions** (horizontal, vertical, and diagonal) from $(r, c)$.
		- If a neighboring cell is within the grid boundaries and is still land (`'L'`), push it to the queue and immediately mark it as `'W'` (or `'0'`) to prevent it from being processed multiple times.

5. Return `cnt` once the full grid is scanned.

### Implementation

#### **Java**

```java []
import java.util.*;

class Solution {
    public int countIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        int m = grid.length;
        int n = grid[0].length;
        int cnt = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // If we hit land, it's a new island
                if (grid[i][j] == 'L') {
                    cnt++;
                    bfs(grid, i, j, m, n);
                }
            }
        }
        return cnt;
    }

    private void bfs(char[][] grid, int startR, int startC, int m, int n) {
        Queue<int[]> que = new LinkedList<>();

        que.offer(new int[] {startR, startC});
        grid[startR][startC] = 'W'; // Mark as visited

        // 8 directions: Up, Down, Left, Right, and 4 Diagonals
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        while (!que.isEmpty()) {
            int[] cell = que.poll();
            int r = cell[0];
            int c = cell[1];

            for (int[] dir : dirs) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                // Check boundaries and if it's unvisited land
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] == 'L') {
                    que.offer(new int[] {nr, nc});
                    grid[nr][nc] = 'W'; // Mark as visited right after pushing
                }
            }
        }
    }
}

```

#### **C++**

```cpp []
#include <vector>
#include <queue>

using namespace std;

class Solution {
public:
    int countIslands(vector<vector<char>>& grid) {
        if (grid.empty()) return 0;

        int m = grid.size();
        int n = grid[0].size();
        int cnt = 0;

        // 8 directions shifts
        int dr[] = {-1, 1, 0, 0, -1, -1, 1, 1};
        int dc[] = {0, 0, -1, 1, -1, 1, -1, 1};

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'L') {
                    cnt++;

                    // Start BFS
                    queue<pair<int, int>> que;
                    que.push({i, j});
                    grid[i][j] = 'W'; // Mark as visited

                    while (!que.empty()) {
                        auto [r, c] = que.front();
                        que.pop();

                        for (int d = 0; d < 8; d++) {
                            int nr = r + dr[d];
                            int nc = c + dc[d];

                            if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] == 'L') {
                                que.push({nr, nc});
                                grid[nr][nc] = 'W'; // Mark as visited
                            }
                        }
                    }
                }
            }
        }
        return cnt;
    }
};

```

### Complexity Analysis

- **Time Complexity:** $\mathcal{O}(M \times N)$
	- $M$ is the number of rows and $N$ is the number of columns.
	- Every cell is visited a constant number of times. Even though we run a BFS inside the loops, cells turned into `'W'` are never processed by the outer loops again.

- **Space Complexity:** $\mathcal{O}(\min(M, N))$
	- In the worst case (e.g., an entire grid filled with land), the maximum number of elements inside the BFS queue at any given time is bounded by the diagonal or the minimum dimension of the grid.
