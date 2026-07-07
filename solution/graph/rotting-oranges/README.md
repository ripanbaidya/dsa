# 994. Rotting Oranges

<p align="right">Last updated - 07.07.2026</p>

- https://leetcode.com/problems/rotting-oranges/description/

### Intuition (BFS)

The problem models a process of **simultaneous propagation** or contamination over time. A rotting orange spreads its rot to all its adjacent fresh neighbors (up, down, left, right) in exactly 1 unit of time.

Because the rot spreads outward level by level at equal intervals, this problem is equivalent to finding the shortest path/time in an unweighted graph. **Breadth-First Search (BFS)** is the ideal algorithm here.

Instead of starting a BFS from just one source, we start a **Multi-Source BFS**. All initially rotten oranges are added to the queue at time `0` because they all begin rotting their neighbors at the exact same moment.

To determine if it's impossible to rot all oranges, we count the total number of fresh oranges at the start. If the number of fresh oranges we successfully rot during our BFS traversal doesn't match the original count, it means some fresh oranges were isolated, and we return `-1`.

### Approach

1. **Initialization:**
   - Determine the dimensions of the grid (`m` rows, `n` cols).
   - Create a Queue to store the cell properties in the format `[row, column, time_taken_to_rot]`.
   - Initialize a counter `fresh` to keep track of total fresh oranges and `rottened` to keep track of how many fresh oranges turn rotten during BFS.

2. **Pre-processing (Multi-source collection):**
   - Traverse the entire grid.
   - If a cell contains a fresh orange (`1`), increment `fresh`.
   - If a cell contains a rotten orange (`2`), push its coordinates along with a starting time of `0` into the queue (`{i, j, 0}`).

3. **BFS Traversal:**
   - Keep track of the maximum time elapsed using `miniTime`.
   - Use a directions array `dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}` to easily look up, down, left, and right.
   - While the queue is not empty:
     - Poll/pop the current cell `[cr, cc, ct]`.
     - For each of the 4 directions, calculate the neighbor's coordinates (`nr`, `nc`) and the new time (`nt = ct + 1`).
     - If the neighbor is within the grid boundaries and is a **fresh orange** (`grid[nr][nc] == 1`):
       - Mark it as rotten (`grid[nr][nc] = 2`) so it won't be processed again.
       - Push it to the queue with its updated timestamp `nt`.
       - Increment the `rottened` counter.
       - Update `miniTime` to be the maximum of its current value and `nt`.

4. **Final Check:**
   - Once the queue is exhausted, compare `fresh` and `rottened`. If they are equal, return `miniTime`. Otherwise, return `-1`.

### Implementation

**Java**

```java []
import java.util.ArrayDeque;
import java.util.Queue;

class Solution {
  public int orangesRotting(int[][] grid) {
    int m = grid.length;
    int n = grid[0].length;

    // Queue stores each cell as an array: {row, column, time_elapsed}
    Queue<int[]> que = new ArrayDeque<>();
    int fresh = 0;     // Tracks total initial fresh oranges
    int rottened = 0;  // Tracks how many fresh oranges successfully turn rotten

    // Step 1: Scan the grid to count fresh oranges and enqueue all initially rotten oranges
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        if (grid[i][j] == 1) {
          fresh++;
        } else if (grid[i][j] == 2) {
          // All initial rotten oranges start spreading at time = 0
          que.offer(new int[]{i, j, 0});
        }
      }
    }

    int miniTime = 0; // Tracks the maximum time required to rot all reachable oranges
    // Coordinate offsets for moving up, down, left, and right
    int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    // Step 2: Multi-source BFS traversal
    while (!que.isEmpty()) {
      int[] cell = que.poll();
      int cr = cell[0]; // Current row
      int cc = cell[1]; // Current column
      int ct = cell[2]; // Current time

      // Explore all 4 adjacent neighbors
      for (int[] dir : dirs) {
        int nr = cr + dir[0]; // Neighbor row
        int nc = cc + dir[1]; // Neighbor column
        int nt = ct + 1;      // Neighbor time (current time + 1 minute)

        // Check if the neighbor is within grid bounds and contains a fresh orange
        if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] == 1) {
          que.offer(new int[]{nr, nc, nt}); // Push the newly rotten orange into the queue
          grid[nr][nc] = 2;                  // Mark it as rotten in the grid to avoid revisiting
          rottened++;                        // Increment the count of processed fresh oranges
          miniTime = Math.max(miniTime, nt); // Update the maximum time taken so far
        }
      }
    }

    // Step 3: If we couldn't reach and rot all fresh oranges, return -1; otherwise, return the total time
    return fresh != rottened ? -1 : miniTime;
  }
}
```

**C++**

```cpp []
#include <vector>
#include <queue>
#include <algorithm>

using namespace std;

class Solution {
public:
    int orangesRotting(vector<vector<int>>& grid) {
        int m = grid.size();
        int n = grid[0].size();

        // Queue stores each cell configuration as a vector: {row, column, time_elapsed}
        queue<vector<int>> que;
        int fresh = 0;    // Tracks total initial fresh oranges
        int rottened = 0; // Tracks how many fresh oranges successfully turn rotten

        // Step 1: Scan the grid to count fresh oranges and enqueue all initially rotten oranges
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    fresh++;
                } else if (grid[i][j] == 2) {
                    // All initial rotten oranges start spreading at time = 0
                    que.push({i, j, 0});
                }
            }
        }

        int miniTime = 0; // Tracks the maximum time required to rot all reachable oranges
        // Coordinate offsets for moving up, down, left, and right
        vector<vector<int>> dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Step 2: Multi-source BFS traversal
        while (!que.empty()) {
            vector<int> cell = que.front();
            que.pop();
            int cr = cell[0]; // Current row
            int cc = cell[1]; // Current column
            int ct = cell[2]; // Current time

            // Explore all 4 adjacent neighbors
            for (const auto& dir : dirs) {
                int nr = cr + dir[0]; // Neighbor row
                int nc = cc + dir[1]; // Neighbor column
                int nt = ct + 1;      // Neighbor time (current time + 1 minute)

                // Check if the neighbor is within grid bounds and contains a fresh orange
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] == 1) {
                    que.push({nr, nc, nt}); // Push the newly rotten orange into the queue
                    grid[nr][nc] = 2;        // Mark it as rotten in the grid to avoid revisiting
                    rottened++;              // Increment the count of processed fresh oranges
                    miniTime = max(miniTime, nt); // Update the maximum time taken so far
                }
            }
        }

        // Step 3: If we couldn't reach and rot all fresh oranges, return -1; otherwise, return the total time
        return fresh != rottened ? -1 : miniTime;
    }
};
```

### Complexity Analysis

- **Time Complexity:** $\mathcal{O}(m \times n)$
  - **Grid Scan:** Initially, we scan the entire grid of size $m \times n$ once to find the initial positions of rotten and fresh oranges. This takes $\mathcal{O}(m \times n)$ time.
  - **BFS Traversal:** In the worst-case scenario, every single cell in the grid holds a fresh orange that gets rotted. Each cell is added to the queue at most once and visited. For each cell, we check its 4 adjacent neighbors (constant time $\mathcal{O}(1)$ operations). Thus, the BFS loop also takes $\mathcal{O}(m \times n)$ time.
  - **Total Time Complexity:** $\mathcal{O}(m \times n) + \mathcal{O}(m \times n) = \mathcal{O}(m \times n)$.

- **Space Complexity:** $\mathcal{O}(m \times n)$
  - **Queue Space:** In the worst case, the queue will store all elements of the grid (e.g., if almost all oranges are rotten initially). The maximum size of the queue is bounded by the total number of cells, which is $m \times n$.
  - **Grid Modification:** Since the algorithm modifies the `grid` in-place (`grid[nr][nc] = 2`) to keep track of visited cells, no additional auxiliary matrix is required.
  - **Total Space Complexity:** $\mathcal{O}(m \times n)$ to maintain the BFS queue.
