# LeetCode 1905. Count Sub Islands

<p align="right">Last updated - 12.07.2026</p>

- https://leetcode.com/problems/count-sub-islands/

## Problem

Before solving the problem, we need to understand what **Sub Island** actually means.

We are given two grids:

- `grid1`
- `grid2`

Both contain only `0` (water) and `1` (land).

An **island** is a group of connected `1`s (connected in **4 directions**: up, down, left, right).

Our task is:

> Count how many islands in `grid2` are completely contained inside an island of `grid1`.

### What does "completely contained" mean?

Many people misunderstand this part.

It **does NOT** mean:

- Both islands should have the same shape.
- Both islands should have the same size.

It simply means:

> **Every land cell (`1`) belonging to an island in `grid2` must also be land (`1`) in `grid1`.**

That's all.

---

**Example 1**

`grid1`

```text
1 1 1
1 1 1
1 1 1
```

`grid2`

```text
0 1 0
0 1 0
0 0 0
```

The island in `grid2` consists of

```text
(0,1)
(1,1)
```

Both positions are also land in `grid1`.

Therefore,

✅ This is a **Sub Island**.

Notice that the island in `grid1` is much bigger.

That doesn't matter.

---

**Example 2**

`grid1`

```text
1 1 1
1 0 1
1 1 1
```

`grid2`

```text
0 1 0
0 1 0
0 0 0
```

Again, the island in `grid2` contains

```text
(0,1)
(1,1)
```

But

```text
grid1[1][1] = 0
```

One land cell of `grid2` falls on water in `grid1`.

Therefore,

❌ This is **NOT** a Sub Island.

Even **one mismatching cell** is enough to reject the entire island.

## Key Observation

We don't compare one island with another island.
Instead, while traversing **one island of `grid2`**, we simply ask:

> **Is every visited land cell also land in `grid1`?**

If yes,

→ Count it.

Otherwise,

→ Ignore it.

## Intuition

Since we need to check **every cell of one island**, DFS is a natural choice.

Whenever we find an unvisited land cell in `grid2`, we start a DFS.

During DFS:

- Visit every land cell of that island.
- For every visited cell, check whether the corresponding cell in `grid1` is also land.
- If any cell is water in `grid1`, the entire island becomes invalid.
- Still continue the DFS so that the whole island gets marked as visited.
- Finally, return whether the complete island was valid or not.

If DFS returns `true`, we found one Sub Island.

## Algorithm

1. Create a `visited` array.
2. Traverse every cell of `grid2`.
3. Whenever an unvisited land cell is found:
   - Start DFS.
   - DFS returns whether the entire island is a Sub Island.

4. If DFS returns `true`, increment the answer.
5. Return the final answer.

## Implementations

**Java**

```java []
class Solution {

    public int countSubIslands(int[][] grid1, int[][] grid2) {
        int m = grid1.length;
        int n = grid1[0].length;

        boolean[][] vis = new boolean[m][n];
        int subIslands = 0;

        // Traverse every cell of grid2
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // Start DFS only from an unvisited land cell
                if (grid2[i][j] == 1 && !vis[i][j]) {

                    // If the entire island is valid, count it
                    if (dfs(i, j, grid1, grid2, vis))
                        subIslands++;
                }
            }
        }

        return subIslands;
    }

    private boolean dfs(int r, int c, int[][] grid1, int[][] grid2, boolean[][] vis) {
        int m = grid2.length;
        int n = grid2[0].length;

        // Stop if out of bounds, water, or already visited
        // Returning true means this path does not invalidate the island
        if (r < 0 || r >= m || c < 0 || c >= n ||
            grid2[r][c] == 0 || vis[r][c]) {
            return true;
        }

        vis[r][c] = true;

        // Current cell must also be land in grid1
        boolean isSubIsland = (grid1[r][c] == 1);

        // {row, col} - up, down, left, right
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // All neighboring parts must also be valid
        for (int[] dir : dirs) {

            int nr = r + dir[0];
            int nc = c + dir[1];

            isSubIsland &= dfs(nr, nc, grid1, grid2, vis);
        }

        return isSubIsland;
    }
}
```

**C ++**

```cpp []
class Solution {
public:

    bool dfs(int r, int c, vector<vector<int>>& grid1, vector<vector<int>>& grid2, vector<vector<bool>>& vis) {
        int m = grid2.size();
        int n = grid2[0].size();

        // Stop if out of bounds, water, or already visited
        if (r < 0 || r >= m || c < 0 || c >= n ||
            grid2[r][c] == 0 || vis[r][c])
            return true;

        vis[r][c] = true;

        // Current cell must also be land in grid1
        bool isSubIsland = (grid1[r][c] == 1);

        // {row, col} - up, down, left, right
        vector<pair<int,int>> dirs = { {-1,0}, {1,0}, {0,-1}, {0,1} };

        // All neighboring parts must also be valid
        for (auto &dir : dirs) {

            int nr = r + dir.first;
            int nc = c + dir.second;

            isSubIsland &= dfs(nr, nc, grid1, grid2, vis);
        }

        return isSubIsland;
    }

    int countSubIslands(vector<vector<int>>& grid1,vector<vector<int>>& grid2) {
        int m = grid1.size();
        int n = grid1[0].size();

        vector<vector<bool>> vis(m, vector<bool>(n, false));

        int subIslands = 0;

        // Traverse every cell of grid2
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // Start DFS from every unvisited land cell
                if (grid2[i][j] == 1 && !vis[i][j]) {

                    if (dfs(i, j, grid1, grid2, vis))
                        subIslands++;
                }
            }
        }

        return subIslands;
    }
};
```

### Why does DFS return `true` for water or out of bounds?

This is one of the most important parts.

Suppose DFS reaches:

- outside the grid
- water cell
- already visited cell

That path has **nothing left to explore**.

It also **does not make the island invalid**.

Therefore we return

```java
true
```

Think of it as:

> "This direction is fine."

Since later we combine all recursive calls using **AND (`&=`)**,

returning `true` does not affect the final answer.

### Why do we use AND (`&=`)?

Suppose the current cell is valid.

Now all four neighboring parts must also be valid.

So we need

```text
Current Cell
AND Up
AND Down
AND Left
AND Right
```

If even one of them is false,

the entire island is not a Sub Island.

## Complexity Analysis

- Time Complexity: **O(M × N)**

Every cell is visited only once during DFS.

So the total work is proportional to the number of cells in the grid.

- Space Complexity - **O(M × N)**

- `visited` array takes `O(M × N)` space.
- In the worst case, the recursion stack can also grow up to `O(M × N)` if the entire grid is one large island.

## Revision Notes (Key Takeaways)

- Traverse **only islands in `grid2`**.
- While exploring one island, check whether **every land cell is also land in `grid1`**.
- If **even one cell** maps to water in `grid1`, the whole island is **not** a sub-island.
- Do **not stop DFS early** after finding an invalid cell. Continue to visit the entire island so it is not processed again.
- Return `true` for **out of bounds, water, and already visited cells** because they do not invalidate the island.
- Use **logical AND (`&=`)** to combine the validity of the current cell and all recursive DFS calls.
- The key idea is:

> **An island in `grid2` is a sub-island if and only if every land cell visited during its DFS also has land at the same position in `grid1`.**
