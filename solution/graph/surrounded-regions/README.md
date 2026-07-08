# 130. Surrounded Regions

<p align="right">Last updated - 08.07.2026</p>

- https://leetcode.com/problems/surrounded-regions/description/

## Approach 1 - DFS + Using Extra Space

### Intuition

The core problem requires us to find all regions of `'O'`s that are completely surrounded by `'X'`s. By definition, any `'O'` that is sitting on the border of the board (or is connected horizontally/vertically to a border `'O'`) can **never** be fully surrounded.

To solve this, we can invert our thinking: instead of looking for surrounded regions, let's find all the **unsurrounded** regions starting from the boundaries.

**How this approach works:**

1. We create an external 2D tracking grid (`track`) initialized to `0`.
2. We run a Depth-First Search (DFS) starting _only_ from the `'O'` cells located on the 4 boundaries (top, bottom, left, right).
3. Any cell reached during this DFS is marked as `1` in our `track` grid, meaning _"this cell is safe and cannot be captured."_
4. Finally, we loop through the entire board. If a cell's corresponding position in `track` is `0`, it means it was never reached from the boundary, making it completely surrounded. We flip these cells to `'X'`.

### Algorithm

1. **Initialization:** Create a 2D integer matrix `track` of size $M \times N$ filled with `0`.
2. **Boundary Scan:** - Iterate through the first and last rows. If `board[i][j] == 'O'`, trigger `dfs()`.
   - Iterate through the first and last columns. If `board[i][j] == 'O'`, trigger `dfs()`.

3. **DFS Marking:** For each visited cell during DFS, if it's within bounds, is an `'O'`, and hasn't been tracked yet, mark `track[r][c] = 1` and recursively visit its 4 neighbors.
4. **Final Mapping:** Loop through every cell in the grid. If `track[i][j] == 0`, overwrite `board[i][j] = 'X'`. Otherwise, leave it or restore it as `'O'`.

### Implementation

```java []
class Solution {
  public void solve(char[][] board) {
    int m = board.length;
    int n = board[0].length;

    // Track the cells which are NOT part of a surrounded region (safe cells)
    // 0 Represents Completely surrounded and 1 Represent Connected to the boundry
    int[][] track = new int[m][n];

    // Step 1: Scan top and bottom boundaries
    for (int j = 0; j < n; j++) {
      if (board[0][j] == 'O')
        dfs(0, j, m, n, board, track);
      if (board[m-1][j] == 'O')
        dfs(m-1, j, m, n, board, track);
    }

    // Step 2: Scan left and right boundaries
    for (int i = 0; i < m; i++) {
      if (board[i][0] == 'O')
        dfs(i, 0, m, n, board, track);
      if (board[i][n-1] == 'O')
        dfs(i, n-1, m, n, board, track);
    }

    // Step 3: Update the board based on the tracking matrix
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        if (track[i][j] == 0)
          board[i][j] = 'X'; // Completely surrounded
        else
          board[i][j] = 'O'; // Connected to boundary, remains 'O'
      }
    }
  }

  private void dfs(int r, int c, int m, int n, char[][] board, int[][] track) {
    // Base case: Out of bounds, hit an 'X', or already tracked
    if (r < 0 || r >= m || c < 0 || c >= n || board[r][c] == 'X' || track[r][c] == 1)
      return;

    track[r][c] = 1; // Mark as safe (not part of a surrounded region)

    // Explore 4 directional neighbors
    dfs(r-1, c, m, n, board, track); // up
    dfs(r+1, c, m, n, board, track); // down
    dfs(r, c-1, m, n, board, track); // left
    dfs(r, c+1, m, n, board, track); // right
  }
}
```

```cpp []
#include <vector>

using namespace std;

class Solution {
public:
    void solve(vector<vector<char>>& board) {
        if (board.empty()) return;

        int m = board.size();
        int n = board[0].size();

        // Track the cells which are NOT part of a surrounded region (safe cells)
        // 0 Represents Completely surrounded and 1 Represent Connected to the boundary
        vector<vector<int>> track(m, vector<int>(n, 0));

        // Step 1: Scan top and bottom boundaries
        for (int j = 0; j < n; j++) {
            if (board[0][j] == 'O')
                dfs(0, j, m, n, board, track);
            if (board[m - 1][j] == 'O')
                dfs(m - 1, j, m, n, board, track);
        }

        // Step 2: Scan left and right boundaries
        for (int i = 0; i < m; i++) {
            if (board[i][0] == 'O')
                dfs(i, 0, m, n, board, track);
            if (board[i][n - 1] == 'O')
                dfs(i, n - 1, m, n, board, track);
        }

        // Step 3: Update the board based on the tracking matrix
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (track[i][j] == 0)
                    board[i][j] = 'X'; // Completely surrounded
                else
                    board[i][j] = 'O'; // Connected to boundary, remains 'O'
            }
        }
    }

private:
    void dfs(int r, int c, int m, int n, const vector<vector<char>>& board, vector<vector<int>>& track) {
        // Base case: Out of bounds, hit an 'X', or already tracked
        if (r < 0 || r >= m || c < 0 || c >= n || board[r][c] == 'X' || track[r][c] == 1)
            return;

        track[r][c] = 1; // Mark as safe (not part of a surrounded region)

        // Explore 4 directional neighbors
        dfs(r - 1, c, m, n, board, track); // up
        dfs(r + 1, c, m, n, board, track); // down
        dfs(r, c - 1, m, n, board, track); // left
        dfs(r, c + 1, m, n, board, track); // right
    }
};
```

### Complexity Analysis

- **Time Complexity:** $\mathcal{O}(M \times N)$,
  - We traverse the boundary cells and run a DFS. In the absolute worst case (e.g., the entire board is filled with `'O'`), the DFS will visit every cell exactly once. The final nested loop also visits every cell once.

- **Space Complexity:** $\mathcal{O}(M \times N)$
  - **Auxiliary Space:** $\mathcal{O}(M \times N)$ because we explicitly allocate a separate `track` matrix of size $M \times N$ to store the state of each cell.
  - **Call Stack Space:** $\mathcal{O}(M \times N)$ in the worst-case scenario due to the recursive depth of the DFS call stack.

## Approach 2 - Without Extra Space

### Intuition

In **Approach 1**, we identified that any `'O'` connected to the boundary of the matrix cannot be flipped into an `'X'` (as they can never be truly surrounded). To keep track of these "safe" boundary-connected `'O'`s, we used a secondary `track` matrix of size $M \times N$.

While that approach is perfectly correct, using an extra $M \times N$ matrix introduces an additional space complexity of $O(M \times N)$.

**Why and What are we optimizing?**
Instead of using external space to remember which cells are safe, we can optimize space by utilizing the input `board` itself as our tracker. We can temporarily mutate the safe `'O'`s into a special dummy character, such as `'#',` during our boundary DFS traversals.

**How will we optimize?**

1. We will scan the boundaries (first row, last row, first column, last column) for any `'O'`.
2. When we find an `'O'`, we kick off a DFS. Instead of marking a `track` array, we flip that `'O'` and all its connected neighbors directly to `'#'`.
3. Once all boundary-connected regions are safely renamed to `'#'`, we iterate through the entire board:

- Any cell that is still an `'O'` means it was truly surrounded by `'X'`s, so we flip it to `'X'`.
- Any cell that is a `'# '` means it was a safe cell, so we restore it back to `'O'`.

This removes the need for an external tracking matrix, reducing the auxiliary space complexity significantly.

### Algorithm

1. **Boundary Traversal:** Run a DFS starting from every `'O'` located on the borders (top, bottom, left, right).
2. **In-place Flagging (DFS):** During the DFS, change the visited `'O'` cells to `'#'`. Check all 4 directional neighbors recursively, continuing only if the neighbor is within bounds and is an `'O'`.
3. **Final Board Scan:** Loop through every cell of the board:

- If `board[i][j] == 'O'`, change it to `'X'`.
- If `board[i][j] == '#'`, restore it back to `'O'`.

### Implementation

```java []
class Solution {
    public void solve(char[][] board) {
        // Edge case: if board is empty or null
        if (board == null || board.length == 0) return;

        int m = board.length;
        int n = board[0].length;

        // Step 1: Traverse the top and bottom boundaries
        for (int j = 0; j < n; j++) {
            // Check top row
            if (board[0][j] == 'O') {
                dfs(0, j, m, n, board);
            }
            // Check bottom row
            if (board[m - 1][j] == 'O') {
                dfs(m - 1, j, m, n, board);
            }
        }

        // Step 2: Traverse the left and right boundaries
        for (int i = 0; i < m; i++) {
            // Check left column
            if (board[i][0] == 'O') {
                dfs(i, 0, m, n, board);
            }
            // Check right column
            if (board[i][n - 1] == 'O') {
                dfs(i, n - 1, m, n, board);
            }
        }

        // Step 3: Scan the board to update cells in-place
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // If it's still 'O', it means it was completely surrounded
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                }
                // If it's '#', it was connected to a boundary, so restore it to 'O'
                else if (board[i][j] == '#') {
                    board[i][j] = 'O';
                }
            }
        }
    }

    private void dfs(int r, int c, int m, int n, char[][] board) {
        // Base Condition: Check boundaries and if the current cell is not 'O'
        if (r < 0 || r >= m || c < 0 || c >= n || board[r][c] != 'O') {
            return;
        }

        // Mark the current boundary-connected 'O' with a placeholder '#'
        board[r][c] = '#';

        // Traverse in all 4 directions (Up, Down, Left, Right)
        dfs(r - 1, c, m, n, board); // up
        dfs(r + 1, c, m, n, board); // down
        dfs(r, c - 1, m, n, board); // left
        dfs(r, c + 1, m, n, board); // right
    }
}

```

```cpp []
#include <vector>

using namespace std;

class Solution {
public:
    void solve(vector<vector<char>>& board) {
        if (board.empty()) return;

        int m = board.size();
        int n = board[0].size();

        // Step 1: Traverse the top and bottom boundaries
        for (int j = 0; j < n; j++) {
            // Check top row
            if (board[0][j] == 'O') {
                dfs(0, j, m, n, board);
            }
            // Check bottom row
            if (board[m - 1][j] == 'O') {
                dfs(m - 1, j, m, n, board);
            }
        }

        // Step 2: Traverse the left and right boundaries
        for (int i = 0; i < m; i++) {
            // Check left column
            if (board[i][0] == 'O') {
                dfs(i, 0, m, n, board);
            }
            // Check right column
            if (board[i][n - 1] == 'O') {
                dfs(i, n - 1, m, n, board);
            }
        }

        // Step 3: Scan the board to update cells in-place
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // If it's still 'O', it means it was completely surrounded
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                }
                // If it's '#', it was connected to a boundary, so restore it to 'O'
                else if (board[i][j] == '#') {
                    board[i][j] = 'O';
                }
            }
        }
    }

private:
    void dfs(int r, int c, int m, int n, vector<vector<char>>& board) {
        // Base Condition: Check boundaries and if the current cell is not 'O'
        if (r < 0 || r >= m || c < 0 || c >= n || board[r][c] != 'O') {
            return;
        }

        // Mark the current boundary-connected 'O' with a placeholder '#'
        board[r][c] = '#';

        // Traverse in all 4 directions (Up, Down, Left, Right)
        dfs(r - 1, c, m, n, board); // up
        dfs(r + 1, c, m, n, board); // down
        dfs(r, c - 1, m, n, board); // left
        dfs(r, c + 1, m, n, board); // right
    }
};

```

### Complexity Analysis

- **Time Complexity:** $\mathcal{O}(M \times N)$
  - Every cell is visited a constant number of times. The boundary DFS traverses each cell in boundary-connected regions exactly once, and the final nested loop scans the grid exactly once.

- **Space Complexity:** $\mathcal{O}(M \times N)$ in the worst-case scenario.
  - **Auxiliary Space:** $\mathcal{O}(1)$ since we modified the input matrix `board` directly and eliminated the extra `track` matrix completely.
  - **Call Stack Space:** In the worst case (e.g., if the entire board is made of `'O'`s), the recursive call stack for DFS can go up to a depth of $M \times N$.
