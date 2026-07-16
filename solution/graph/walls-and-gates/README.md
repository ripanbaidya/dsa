# [286. Walls and Gates 🔒](https://leetcode.com/problems/walls-and-gates)

<p align="right">Last updated - 14.07.2026</p>

## Description

<p>You are given an <code>m x n</code> grid <code>rooms</code>&nbsp;initialized with these three possible values.</p>

<ul>
	<li><code>-1</code>&nbsp;A wall or an obstacle.</li>
	<li><code>0</code> A gate.</li>
	<li><code>INF</code> Infinity means an empty room. We use the value <code>2<sup>31</sup> - 1 = 2147483647</code> to represent <code>INF</code> as you may assume that the distance to a gate is less than <code>2147483647</code>.</li>
</ul>

<p>Fill each empty room with the distance to <em>its nearest gate</em>. If it is impossible to reach a gate, it should be filled with <code>INF</code>.</p>

<p>&nbsp;</p>
<p><strong class="example">Example 1:</strong></p>
<img alt="" src="https://fastly.jsdelivr.net/gh/doocs/leetcode@main/solution/0200-0299/0286.Walls%20and%20Gates/images/grid.jpg" style="width: 500px; height: 223px;" />
<pre>
<strong>Input:</strong> rooms = [[2147483647,-1,0,2147483647],[2147483647,2147483647,2147483647,-1],[2147483647,-1,2147483647,-1],[0,-1,2147483647,2147483647]]
<strong>Output:</strong> [[3,-1,0,1],[2,2,1,-1],[1,-1,2,-1],[0,-1,3,4]]
</pre>

<p><strong class="example">Example 2:</strong></p>

<pre>
<strong>Input:</strong> rooms = [[-1]]
<strong>Output:</strong> [[-1]]
</pre>

<p>&nbsp;</p>
<p><strong>Constraints:</strong></p>

<ul>
	<li><code>m == rooms.length</code></li>
	<li><code>n == rooms[i].length</code></li>
	<li><code>1 &lt;= m, n &lt;= 250</code></li>
	<li><code>rooms[i][j]</code> is <code>-1</code>, <code>0</code>, or <code>2<sup>31</sup> - 1</code>.</li>
</ul>

## Approach: BFS

### Intuition

The problem asks us to find the shortest distance from every empty room (`INF`) to its nearest gate (`0`).

If we were to start a Breadth-First Search (BFS) from each individual empty room, we would end up recalculating overlapping paths constantly, resulting in a highly inefficient time complexity.

Instead, we reverse the perspective and start the traversal from the **gates themselves**.

- By pushing all gates into a queue and running a **Multi-Source BFS**, we propagate distance waves outward from every gate simultaneously.
- The first time a wave reaches an empty room (`INF`), that path is guaranteed to be the absolute shortest route from _any_ gate to that room.
- Obstacles/walls (`-1`) naturally block the wave front because we strictly filter them out.

### Algorithm

1. **Initialization:**
   - Get the dimensions of the grid ($m \times n$).
   - Initialize a queue to hold the grid cell data. Each entry stores a triplet or array format: `[row, col, distance]`.
   - Iterate through the entire grid. For every cell that contains a gate (`rooms[i][j] == 0`), push its coordinates along with an initial distance of `0` into the queue.

2. **Multi-Source BFS Processing:**
   - While the queue is not empty, extract the front element to get the current row (`cr`), current column (`cc`), and current distance (`cd`).
   - Explore all 4 adjacent directional neighbors (`nr`, `nc`) using the direction vectors: `{-1, 0}`, `{1, 0}`, `{0, -1}`, `{0, 1}`.
   - For each neighbor, check if it satisfies the following validation criteria:
     - It is within the matrix boundaries ($0 \le nr < m$ and $0 \le nc < n$).
     - It is not a wall/obstacle (`rooms[nr][nc] != -1`).
     - It is an unvisited empty room (`rooms[nr][nc] == INF`).

   - If the neighbor is valid:
     - Compute the new distance: $\text{newDist} = cd + 1$.
     - Update the grid directly: `rooms[nr][nc] = newDist`.
     - Push `[nr, nc, newDist]` back into the queue so that this cell can serve as a starting point to update its own neighbors in subsequent layers.

### Implementations

**Java**

```java
import java.util.ArrayDeque;
import java.util.Queue;

class Solution {
    private static final int INF = Integer.MAX_VALUE;
    // Direction vectors: up, down, left, right
    private static final int[][] DIRS = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };

    public void wallsAndGates(int[][] rooms) { // Renamed method and parameter
        if (rooms == null || rooms.length == 0) return;

        int m = rooms.length;
        int n = rooms[0].length;
        Queue<int[]> que = new ArrayDeque<>(); // {row, col, dist}

        // Add all gates (0) to the queue first
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (rooms[i][j] == 0) {
                    que.offer(new int[] {i, j, 0});
                }
            }
        }

        // Multi-source BFS
        while (!que.isEmpty()) {
            int[] top = que.poll();
            int cr = top[0];
            int cc = top[1];
            int cd = top[2];

            for (int[] dir : DIRS) {
                int nr = cr + dir[0];
                int nc = cc + dir[1];
                int newDist = cd + 1;

                // Check boundaries and make sure the room is empty (INF)
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && rooms[nr][nc] == INF) {
                    rooms[nr][nc] = newDist;
                    que.offer(new int[] {nr, nc, newDist});
                }
            }
        }
    }
}

```

**C++**

```cpp
#include <vector>
#include <queue>
#include <climits>

using namespace std;

class Solution {
private:
    const vector<pair<int, int>> DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    const int INF = INT_MAX;

public:
    void wallsAndGates(vector<vector<int>>& rooms) { // Renamed method and parameter
        if (rooms.empty()) return;

        int m = rooms.size();
        int n = rooms[0].size();
        queue<pair<int, int>> que; // stores {row, col}

        // Add all gates (0) to the queue first
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (rooms[i][j] == 0) {
                    que.push({i, j});
                }
            }
        }

        // Multi-source BFS
        while (!que.empty()) {
            auto [cr, cc] = que.front();
            que.pop();

            for (const auto& dir : DIRS) {
                int nr = cr + dir.first;
                int nc = cc + dir.second;

                // Check boundaries and make sure the room is empty (INF)
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && rooms[nr][nc] == INF) {
                    rooms[nr][nc] = rooms[cr][cc] + 1;
                    que.push({nr, nc});
                }
            }
        }
    }
};

```

### Complexity Analysis

Let $M$ be the number of rows and $N$ be the number of columns in the grid.

- Time Complexity: $\mathcal{O}(M \times N)$
  - **Queue Initialization:** The nested loops iterate through every single cell in the grid exactly once to locate the gates (`0`). This initial phase takes $\mathcal{O}(M \times N)$ time.
  - **BFS Traversal:** In the Multi-Source BFS phase, each grid cell is pushed into and popped from the queue at most once. For every cell processed, we evaluate its 4 directional neighbors. Since the number of neighbors checked per cell is bounded by a constant factor ($4$), the work done per cell is $\mathcal{O}(1)$.
  - **Total Time:** Combining both parts, the algorithm visits each cell a constant number of times, resulting in a linear time complexity relative to the total number of cells in the grid: $\mathcal{O}(M \times N)$.

- Space Complexity: $\mathcal{O}(M \times N)$
  - **Queue Size:** In the worst-case scenario (for example, if a significant fraction or all of the cells in the grid are gates), the BFS queue will hold a number of elements proportional to the grid size, bounding the queue size to $\mathcal{O}(M \times N)$.
  - **Array allocations:** The queue stores small fixed-size arrays (`int[]` of size 3), which scale linearly with the number of elements tracked.
  - **In-Place Modification:** The matrix itself is modified directly in-place without copying or allocating additional grid data structures. Thus, the auxiliary space is entirely dominated by the queue size, yielding $\mathcal{O}(M \times N)$.
