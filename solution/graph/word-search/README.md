# [Word Search](https://leetcode.com/problems/word-search/description/)

<p align="right">Last updated - 29.07.2026</p>

## Approach 1: Using Extra Space

### Intuition

When searching for the word in the grid, we cannot reuse the same cell twice within the same word path.

To enforce this constraint without modifying the original grid:

1. We create a `boolean[][] visited` matrix of the same dimensions as `board`.
2. As we traverse through a potential path, we mark `visited[row][col] = true` to block the current path from revisiting this cell.
3. If an exploration path fails (hits a dead end or character mismatch), we **backtrack** by unmarking `visited[row][col] = false`, making it available again for other potential paths that might pass through this cell later.

### Algorithm

1. **Grid Traversal:**
   - Initialize a `boolean[][] visited` matrix of size $M \times N$.
   - Iterate through every cell `(row, col)` in the grid.
   - if `board[row][col] == word.charAt(0)`, start the DFS search from `(row, col)` with `wordIndex = 0`.

2. **DFS Function (`dfs`):**
   - **Base Case:** If `wordIndex == word.length()`, all characters have been matched; return `true`.
   - **Boundary & Validation Check:** Return `false` if:
     - `row` or `col` is out of grid bounds.
     - `visited[row][col]` is `true` (already used in the current path).
     - `board[row][col] != word.charAt(wordIndex)` (character mismatch).

   - **Mark Visited:** Set `visited[row][col] = true`.
   - **Explore 4 Directions:** Recursively call `dfs` for all four neighbors (up, down, left, right) with `wordIndex + 1`. If any direction returns `true`, propagate `true` back up.
   - **Backtrack:** Reset `visited[row][col] = false` before returning `false` to free up the cell for alternative paths.

3. **Return Result:** Return `true` if any path matches the word; otherwise, return `false`.

## Implementations

```java []
class Solution {
    // 4 directions: up, down, left, right
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public boolean exist(char[][] board, String word) {
        int rows = board.length;
        int cols = board[0].length;

        // Extra space to track visited cells for the current path
        boolean[][] visited = new boolean[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Trigger DFS if the first character matches
                if (board[row][col] == word.charAt(0)) {
                    if (dfs(board, visited, word, row, col, 0)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean dfs(char[][] board, boolean[][] visited, String word, int row, int col, int wordIndex) {
        // Base case: matched all characters in the word
        if (wordIndex == word.length()) {
            return true;
        }

        int rows = board.length;
        int cols = board[0].length;

        // Boundary checks, visited check, and character match check
        if (row < 0 || row >= rows || col < 0 || col >= cols
                || visited[row][col]
                || board[row][col] != word.charAt(wordIndex)) {
            return false;
        }

        // Mark current cell as visited
        visited[row][col] = true;

        // Explore all 4 orthogonal directions
        for (int[] dir : DIRECTIONS) {
            int nextRow = row + dir[0];
            int nextCol = col + dir[1];

            if (dfs(board, visited, word, nextRow, nextCol, wordIndex + 1)) {
                return true;
            }
        }

        // Backtrack: unmark visited status for other potential paths
        visited[row][col] = false;

        return false;
    }
}

```

### Complexity Analysis

#### Time Complexity: $O(M \cdot N \cdot 3^L)$

- **$M \times N$ Grid Scan:** We check up to $M \cdot N$ starting positions in the worst case.
- **$3^L$ Path Branching:** From each step, DFS explores up to 3 unexplored directions for a word of length $L$.
- **Total Time:** **$O(M \cdot N \cdot 3^L)$**, where $M$ is rows, $N$ is columns, and $L$ is word length.

#### Space Complexity: $O(M \cdot N + L)$

- **Visited Matrix:** The 2D boolean array `visited` requires $O(M \cdot N)$ extra memory.
- **Recursion Stack:** The maximum depth of the call stack is bounded by the length of the word $L$, taking $O(L)$ space.
- **Total Space:** **$O(M \cdot N + L)$**, which simplifies to **$O(M \cdot N)$** since grid size usually dominates $L$.

## Approach 2: Without Using Extra Space

### Intuition

Think of the grid as a maze where each cell contains a letter:

1. **Find Starting Points:** We scan the grid to locate cells matching the first character of the word.
2. **Explore Paths (DFS):** From a matching cell, we move to adjacent cells (up, down, left, right) that match the next required character in the target word.
3. **Track Visited Cells:** We mark cells as visited during the current path exploration so we don't reuse the same cell twice in a single word match.
4. **Backtrack:** If a path hits a dead end (no adjacent cell matches the next character), we backtrack by unmarking the current cell so it remains available for other potential paths.

### Algorithm

1. **Grid Traversal:** Iterate through every cell `(row, col)` in the $m \times n$ matrix.
2. **Start Search:** Whenever `board[row][col] == word.charAt(0)`, trigger the DFS recursion starting from index `0`.
3. **DFS Recursive Function:**
   - **Base Case:** If the matched index equals `word.length()`, the complete word has been found; return `true`.
   - **Boundary & Match Validation:** Check if the current position is out of bounds, already visited, or does not match `word.charAt(index)`. If any condition fails, return `false`.
   - **Mark Visited:** Mark `visited[row][col] = true` (or temporarily mutate `board[row][col]` to avoid extra space).
   - **Explore Neighbors:** Recursively invoke DFS for all 4 orthogonal directions with `index + 1`. If any direction succeeds, return `true`.
   - **Backtrack:** Reset `visited[row][col] = false` so other paths can use this cell.

4. **Return Result:** If any starting cell completes the word, return `true`. If all cells are checked with no match, return `false`.

## Implementation

> **Interview Optimization:** Instead of passing a separate `boolean[][] visited` array (which takes $O(m \times n)$ extra space) or re-instantiating it inside loops, we can temporarily mutate `board[r][c]` to a special character like `'#'` during DFS and restore it when backtracking.

```java []
class Solution {
    // 4 directions: up, down, left, right
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public boolean exist(char[][] board, String word) {
        int rows = board.length;
        int cols = board[0].length;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Start DFS if the first character matches
                if (board[row][col] == word.charAt(0)) {
                    if (dfs(board, word, row, col, 0)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean dfs(char[][] board, String word, int row, int col, int wordIndex) {
        // Base case: successfully matched all characters in the word
        if (wordIndex == word.length()) {
            return true;
        }

        int rows = board.length;
        int cols = board[0].length;

        // Check boundaries, visited state (indicated by '#'), and character match
        if (row < 0 || row >= rows || col < 0 || col >= cols
            || board[row][col] != word.charAt(wordIndex)) {
            return false;
        }

        // Save original character and mark cell as visited
        char tempChar = board[row][col];
        board[row][col] = '#';

        // Explore all 4 adjacent directions
        for (int[] dir : DIRECTIONS) {
            int nextRow = row + dir[0];
            int nextCol = col + dir[1];

            if (dfs(board, word, nextRow, nextCol, wordIndex + 1)) {
                return true;
            }
        }

        // Backtrack: restore original character for other search paths
        board[row][col] = tempChar;

        return false;
    }
}

```

### Complexity Analysis

- Time Complexity: $O(M \cdot N \cdot 3^L)$
  - **$M \times N$ Grid Scanning:** There are $M \cdot N$ starting cells in the grid.
  - **$3^L$ Branching Factor:** For a word of length $L$, the DFS branches in up to 3 directions at each step (since we don't return to the cell we just came from).
  - **Total Time:** **$O(M \cdot N \cdot 3^L)$**, where $M$ is the number of rows, $N$ is the number of columns, and $L$ is the length of the target word.

- Space Complexity: $O(L)$
  - **Call Stack:** The recursion depth is bounded by the length of the word $L$, requiring $O(L)$ space on the system call stack.
  - **Auxiliary Memory:** By mutating `board` directly in place to mark visited cells, we avoid spending $O(M \cdot N)$ extra space for a `boolean[][] visited` matrix.
  - **Total Space:** **$O(L)$** auxiliary space.
