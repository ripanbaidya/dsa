# 542. 01 Matrix

<p align="right">Last updated - 08.07.2026</p>

- https://leetcode.com/problems/01-matrix/description/

## Approach - BFS

### Intuition

The goal is to find the shortest distance from each cell to the nearest `0`.

Instead of starting from each `1` and searching for a `0` (which is highly inefficient and causes redundant work), we flip the problem on its head: **we start from all `0`s simultaneously.**

Think of it like pouring water on all the `0` cells at the exact same time. The water expands outward to adjacent `1`s level by level. The time it takes for the water to reach a `1` cell is exactly its shortest distance to a `0`. This is known as **Multi-Source BFS**.

#### Why BFS over DFS?

- **Shortest Path Guarantee:** BFS explores radially, level by level (distance 1, then distance 2, etc.). The first time BFS visits a cell, it is guaranteed to be via the shortest possible path.
- **DFS Inefficiency:** DFS explores deeply along one path. If it reaches a cell, it might not be via the shortest path, forcing it to revisit and update cells multiple times. This leads to a massive time complexity blowup ($O((M \times N)^2)$) or a StackOverflow error due to deep recursion.

### Algorithm

1. **Initialization:**
   - Create a `res` (result) matrix of the same size to store distances.
   - Initialize a Queue to store the cell coordinates along with their current distance/time: `{row, col, distance}`.

2. **Multi-Source Setup:**
   - Iterate through the entire matrix. For every `0` found, push it into the queue with a distance of `0`.

3. **BFS Traversal:**
   - While the queue is not empty, pop the front cell `{cr, cc, ct}`.
   - Look at its 4 neighbors (Up, Down, Left, Right).
   - If a neighbor is **valid** (within bounds) and **has not been visited yet** (we can track this by checking if it's a `1` in the original matrix and modifying it to `0` once visited):
     - Calculate `newDistance = ct + 1`.
     - Update the neighbor's position in `res` with `newDistance`.
     - Push the neighbor into the queue with `newDistance`.
     - Mark the neighbor as visited in the original matrix (`mat[nr][nc] = 0`) to prevent processing it again.

4. **Return** the `res` matrix.

### Implementations

```java []
// Java

import java.util.ArrayDeque;
import java.util.Queue;

class Solution {
    public int[][] updateMatrix(int[][] mat) {
        int m = mat.length;
        int n = mat[0].length;

        // Queue stores array of size 3: {row, col, current_distance}
        Queue<int[]> que = new ArrayDeque<>();
        int[][] res = new int[m][n];

        // Step 1: Push all '0's into the queue to start multi-source BFS
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 0) {
                    que.offer(new int[]{i, j, 0});
                }
            }
        }

        // Direction arrays for traveling Up, Down, Left, Right
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Step 2: Process the queue level-by-level
        while (!que.isEmpty()) {
            int[] cell = que.poll();
            int cr = cell[0];
            int cc = cell[1];
            int ct = cell[2]; // current distance/time

            for (int[] dir : dirs) {
                int nr = cr + dir[0];
                int nc = cc + dir[1];
                int newTime = ct + 1;

                // If neighbor is within bounds and is an unvisited '1'
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && mat[nr][nc] != 0) {
                    que.offer(new int[]{nr, nc, newTime});
                    mat[nr][nc] = 0;       // Mark as visited in-place
                    res[nr][nc] = newTime;  // Store the shortest distance
                }
            }
        }

        return res;
    }
}

```

```cpp []
// C ++

#include <vector>
#include <queue>

using namespace std;

class Solution {
public:
    vector<vector<int>> updateMatrix(vector<vector<int>>& mat) {
        int m = mat.size();
        int n = mat[0].size();

        // Queue stores a structure or vector: {row, col, current_distance}
        queue<vector<int>> que;
        vector<vector<int>> res(m, vector<int>(n, 0));

        // Step 1: Push all '0's into the queue to start multi-source BFS
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 0) {
                    que.push({i, j, 0});
                }
            }
        }

        // Direction vectors for traveling Up, Down, Left, Right
        vector<pair<int, int>> dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Step 2: Process the queue level-by-level
        while (!que.empty()) {
            auto cell = que.front();
            que.pop();

            int cr = cell[0];
            int cc = cell[1];
            int ct = cell[2]; // current distance

            for (auto& dir : dirs) {
                int nr = cr + dir.first;
                int nc = cc + dir.second;
                int newTime = ct + 1;

                // If neighbor is within bounds and is an unvisited '1'
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && mat[nr][nc] != 0) {
                    que.push({nr, nc, newTime});
                    mat[nr][nc] = 0;       // Mark as visited in-place
                    res[nr][nc] = newTime;  // Store the shortest distance
                }
            }
        }

        return res;
    }
};

```

### Complexity Analysis

Let $M$ be the number of rows and $N$ be the number of columns in the matrix.

- **Time Complexity:** $\mathcal{O}(M \times N)$
  - Each cell is added to the queue at most once.
  - For each popped cell, we look at its 4 neighbors, which takes $\mathcal{O}(1)$ constant time operations.
  - Total operations are directly proportional to the total number of cells.

- **Space Complexity:** $\mathcal{O}(M \times N)$
  - In the worst-case scenario (e.g., all cells are `0`), the queue will hold up to $M \times N$ elements.
  - The `res` matrix also utilizes $\mathcal{O}(M \times N)$ space.

> **Note:** > When looking for the _shortest path_ in an _unweighted grid_ with _multiple starting points_, always think **Multi-Source BFS**. Push all entry points into the queue at Level 0, track visits in-place to avoid extra space, and roll out radially!
