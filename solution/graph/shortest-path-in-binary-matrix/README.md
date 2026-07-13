# 1091. Shortest Path in Binary Matrix

<p align="right">Last updated - 13.07.2026</p>

- https://leetcode.com/problems/shortest-path-in-binary-matrix/description/

## Approach: BFS

### Intuition

Imagine you are standing at the top-left cell `(0, 0)` of a grid-based maze, and your goal is to reach the bottom-right cell `(n-1, n-1)` by taking the fewest steps possible.

1. **The Traversal Nature:** You are allowed to move in 8 directions (up, down, left, right, and all 4 diagonals). You can only step on cells that contain `0` (clear path); you cannot step on cells containing `1` (blocked path).

2. **Finding the _Shortest_ Path:** When looking for the shortest path in an unweighted grid (where moving from one cell to an adjacent cell always counts as exactly `1` step), **Breadth-First Search (BFS)** is the optimal choice.

3. **Why BFS?** BFS explores the grid layer by layer, moving outward uniformly from the starting point like ripples in water. The moment the BFS algorithm reaches the destination cell, we can guarantee that the path taken is the absolute shortest. A Depth-First Search (DFS) would explore a single path deeply, possibly finding a long, winding route first, which is inefficient for this problem.

### Algorithm

1. **Edge Cases:** - If the starting cell `grid[0][0]` or the destination cell `grid[n-1][n-1]` is blocked (`1`), it's impossible to start or finish. Return `-1` immediately.
2. **Initialization:**
   - Create a **Queue** to keep track of the coordinates to visit. Store the row index, column index, and the current path length (distance).
   - Enqueue the starting point `(0, 0)` with a distance of `1`.
   - **Avoid Redundant Work:** Mark `grid[0][0]` as `1` (visited) to prevent the algorithm from circling back to it.

3. **BFS Traversal:** While the queue is not empty:
   - Dequeue the front element. Let's call its properties `(curRow, curCol, curDist)`.
   - **Check Destination:** If `curRow == n - 1` and `curCol == n - 1`, we have arrived at the destination! Return `curDist`.
   - **Explore 8 Directions:** Loop through all 8 possible directional shifts. For each direction, calculate the neighboring cell's coordinates `(neiRow, neiCol)`.
   - **Validate the Step:** Check if the neighbor is:
     - Inside the boundaries of the grid (`0 <= neiRow < n` and `0 <= neiCol < n`).
     - Clear and unvisited (`grid[neiRow][neiCol] == 0`).

   - **Process the Neighbor:** If the neighbor passes validation:
     - Enqueue it with an incremented distance: `(neiRow, neiCol, curDist + 1)`.
     - Mark it as visited by updating its value in the grid: `grid[neiRow][neiCol] = 1`.

4. **No Path Found:** If the queue becomes empty and the destination has not been reached, a path does not exist. Return `-1`.

### Implementations

**Java**

```java []
class Solution {
    public int shortestPathBinaryMatrix(int[][] grid) {
        int n = grid.length;

        // Edge Cases: If start or end is blocked, return -1
        if (grid[0][0] == 1 || grid[n - 1][n - 1] == 1) {
            return -1;
        }

        // Queue to store coordinates and current path length: {row, col, distance}
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{0, 0, 1});
        grid[0][0] = 1; // Mark the starting cell as visited

        // Arrays representing the 8 possible movements (row offset, col offset)
        int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},   // Up, Down, Left, Right
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}   // Diagonals
        };

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int curRow = current[0];
            int curCol = current[1];
            int curDist = current[2];

            // If we reached the bottom-right corner, return the path length
            if (curRow == n - 1 && curCol == n - 1) {
                return curDist;
            }

            // Explore all 8 adjacent neighbors
            for (int[] dir : directions) {
                int neiRow = curRow + dir[0];
                int neiCol = curCol + dir[1];

                // Check boundaries and if the cell is clear (0)
                if (neiRow >= 0 && neiRow < n && neiCol >= 0 && neiCol < n && grid[neiRow][neiCol] == 0) {
                    queue.offer(new int[]{neiRow, neiCol, curDist + 1});
                    grid[neiRow][neiCol] = 1; // Mark as visited
                }
            }
        }

        // Destination is unreachable
        return -1;
    }
}

```

**C++**

```cpp []
class Solution {
public:
    int shortestPathBinaryMatrix(vector<vector<int>>& grid) {
        int n = grid.size();

        // Edge Cases: If start or end is blocked, return -1
        if (grid[0][0] == 1 || grid[n - 1][n - 1] == 1) {
            return -1;
        }

        // Queue to store coordinates and current path length: {row, col, distance}
        queue<vector<int>> q;
        q.push({0, 0, 1});
        grid[0][0] = 1; // Mark the starting cell as visited

        // Vector representing the 8 possible movements (row offset, col offset)
        vector<pair<int, int>> directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},   // Up, Down, Left, Right
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}   // Diagonals
        };

        while (!q.empty()) {
            auto current = q.front();
            q.pop();

            int curRow = current[0];
            int curCol = current[1];
            int curDist = current[2];

            // If we reached the bottom-right corner, return the path length
            if (curRow == n - 1 && curCol == n - 1) {
                return curDist;
            }

            // Explore all 8 adjacent neighbors
            for (auto& dir : directions) {
                int neiRow = curRow + dir.first;
                int neiCol = curCol + dir.second;

                // Check boundaries and if the cell is clear (0)
                if (neiRow >= 0 && neiRow < n && neiCol >= 0 && neiCol < n && grid[neiRow][neiCol] == 0) {
                    q.push({neiRow, neiCol, curDist + 1});
                    grid[neiRow][neiCol] = 1; // Mark as visited
                }
            }
        }

        // Destination is unreachable
        return -1;
    }
};

```

### **Complexity Analysis**

Let $N$ be the number of rows (or columns) in the $N \times N$ matrix. The total number of cells in the grid is $N^2$.

- **Time Complexity:** $\mathcal{O}(N^2)$
  In the worst-case scenario, the algorithm might visit every single cell in the grid exactly once. For each cell, we perform a constant amount of work by checking its 8 neighbors. Since the number of cells is $N^2$, the total execution time is directly proportional to $N^2$.
- **Space Complexity:** $\mathcal{O}(N^2)$
  The space complexity is determined by the size of the BFS queue. In a grid, the queue can hold at most the outer boundary or a wave of cells at any single tier of exploration. In the worst case, the queue size scales with the total number of cells, giving a space complexity bounded by $\mathcal{O}(N^2)$. _(Note: By modifying the input matrix directly to track visited cells, we avoid an extra $\mathcal{O}(N^2)$ auxiliary memory allocation)._
