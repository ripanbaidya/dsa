# Distance of nearest cell having 1

<p align="right">Last updated - 08.07.2026</p>

- https://www.geeksforgeeks.org/problems/distance-of-nearest-cell-having-1-1587115620/1
- Related Problem - https://leetcode.com/problems/01-matrix/description/

## Approach - BFS

### Intuition

The goal is to find the shortest distance from each cell to the nearest 1.

Instead of starting from each 0 and searching for a 1 (which is highly inefficient and causes redundant work), we flip the problem on its head: we start from all 1s simultaneously.

Think of it like pouring water on all the 1 cells at the exact same time. The water expands outward to adjacent 0s level by level. The time it takes for the water to reach a 0 cell is exactly its shortest distance to a 1. This is known as `Multi-Source BFS`.

#### Why BFS over DFS?

- **Radial Expansion:** BFS guarantees shortest paths in an unweighted grid because it explores uniformly outward (all cells at distance 1, then distance 2, etc.).
- **No Redundancy:** By marking a cell as "visited" (`grid[nr][nc] = 1`) the moment we step on it, we ensure no cell is processed twice, keeping our run time perfectly linear.

###Algorithm

1. **Initialization:**
   - Create a `res` grid to hold our computed distances.
   - Define a Queue holding `{row, col, distance}`.

2. **Multi-Source Setup:**
   - Scan the grid. For every `1` found, push its coordinates into the queue with a distance of `0`.

3. **BFS Traversal:**
   - Dequeue the front cell `{cr, cc, ct}`.
   - Check all 4 neighboring directions (Up, Down, Left, Right).
   - If a neighbor is valid (within matrix boundaries) and contains a `0` (meaning it hasn't been visited yet):
     - Increment the distance: `newTime = ct + 1`.
     - Store `newTime` in `res[nr][nc]`.
     - Push the neighbor into the queue with `newTime`.
     - Set `grid[nr][nc] = 1` to mark it visited in-place.

4. **Formatting:** Convert the internal `res` array into an `ArrayList<ArrayList<Integer>>` (Java) or `vector<vector<int>>` (C++) as required by the problem's signature.

### Implementations

```java
// Java
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

class Solution {
    public ArrayList<ArrayList<Integer>> nearest(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        Queue<int[]> que = new ArrayDeque<>(); // Stores: {row, col, current_distance}
        int[][] res = new int[m][n];

        // Step 1: Push all '1's into the queue to start multi-source BFS
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    que.offer(new int[] {i, j, 0});
                }
            }
        }

        // Direction arrays for traveling Up, Down, Left, Right
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Step 2: Run BFS level by level
        while (!que.isEmpty()) {
            int[] cell = que.poll();
            int cr = cell[0];
            int cc = cell[1];
            int ct = cell[2];

            for (int[] dir : dirs) {
                int nr = cr + dir[0];
                int nc = cc + dir[1];
                int newTime = ct + 1;

                // If neighbor is within bounds and is an unvisited '0'
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] != 1) {
                    que.offer(new int[] {nr, nc, newTime});
                    grid[nr][nc] = 1;      // Mark as visited in-place
                    res[nr][nc] = newTime;  // Record shortest distance
                }
            }
        }

        // Step 3: Convert the primitive 2D matrix to the required format
        ArrayList<ArrayList<Integer>> finalResult = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            finalResult.add(new ArrayList<>());
            for (int j = 0; j < n; j++) {
                finalResult.get(i).add(res[i][j]);
            }
        }

        return finalResult;
    }
}

```

```cpp
// C ++
#include <vector>
#include <queue>

using namespace std;

class Solution {
public:
    vector<vector<int>> nearest(vector<vector<int>>& grid) {
        int m = grid.size();
        int n = grid[0].size();

        // Queue stores a vector of size 3: {row, col, current_distance}
        queue<vector<int>> que;
        vector<vector<int>> res(m, vector<int>(n, 0));

        // Step 1: Push all '1's into the queue to start multi-source BFS
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    que.push({i, j, 0});
                }
            }
        }

        // Direction vectors for traveling Up, Down, Left, Right
        vector<pair<int, int>> dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Step 2: Run BFS level by level
        while (!que.empty()) {
            auto cell = que.front();
            que.pop();

            int cr = cell[0];
            int cc = cell[1];
            int ct = cell[2];

            for (auto& dir : dirs) {
                int nr = cr + dir.first;
                int nc = cc + dir.second;
                int newTime = ct + 1;

                // If neighbor is within bounds and is an unvisited '0'
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] != 1) {
                    que.push({nr, nc, newTime});
                    grid[nr][nc] = 1;      // Mark as visited in-place
                    res[nr][nc] = newTime;  // Record shortest distance
                }
            }
        }

        return res;
    }
};

```

### Complexity Analysis

Let $M$ be the number of rows and $N$ be the number of columns in the grid.

- **Time Complexity:** $\mathcal{O}(M \times N)$
  - Every single cell is processed at most once because it gets marked visited immediately upon entry into the queue.
  - Checking neighbors takes constant $\mathcal{O}(1)$ time per cell.

- **Space Complexity:** $\mathcal{O}(M \times N)$
  - The BFS queue can store up to $M \times N$ elements in the worst case.
  - The auxiliary `res` grid takes $\mathcal{O}(M \times N)$ space.

> 💡 **Cheat-Sheet:**
>
> - Nearest `0` from `1` **OR** Nearest `1` from `0` $\rightarrow$ **Multi-Source BFS**.
> - Push all targets (`0`s or `1`s) into the queue initially with distance `0`.
> - Update distances and modify the original grid in-place to avoid extra `visited` boolean arrays.
