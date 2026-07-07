# 733. Flood Fill

<p align="right">Last updated - 07.07.2026</p>

- https://leetcode.com/problems/flood-fill/description/

## Approach 1. DFS

### Intuition

The problem asks us to start at a specific pixel $(s_r, s_c)$ and change its color, then propagate this change to all adjacent pixels that originally shared the _same color_. This is exactly how the "bucket fill" tool works in digital paint programs.

Mathematically and structurally, the image grid can be viewed as an **unweighted graph** where:

- Each pixel is a **node**.
- **Edges** exist between pixels that are 4-directionally adjacent (up, down, left, right) and have the **same starting color**.

Because we want to traverse all connected nodes sharing the initial color, we can use standard graph traversal algorithms like **Depth-First Search (DFS)** or **Breadth-First Search (BFS)**.

### Algorithm

We will use a **Depth-First Search (DFS)** approach because it is elegant to write recursively and highly efficient for this grid size.

1. **Edge Case Check**: First, check if the starting pixel already has the target `color`. If it does, we don't need to do anything—return the image immediately to avoid an infinite recursion loop.
2. **Save Initial Color**: Store the original color of `image[sr][sc]` so we know which pixels are eligible to be changed.
3. **Recursive DFS**:
   - Change the current pixel's color to the new `color`.
   - Look in all 4 directions (up, down, left, right).
   - For each neighbor, check if it is **within boundaries** and if its color matches the **initial color**.
   - If both conditions are met, recursively call the DFS function on that neighbor.

### Implementations

#### Java

```java []
class Solution {
    public int[][] floodFill(int[][] image, int sr, int sc, int color) {
        int initialColor = image[sr][sc];

        // If the starting pixel already has the target color, no changes are needed
        if (initialColor != color) {
            dfs(image, sr, sc, initialColor, color);
        }

        return image;
    }

    private void dfs(int[][] image, int r, int c, int initialColor, int newColor) {
        // Boundary check and color match check
        if (r < 0 || r >= image.length || c < 0 || c >= image[0].length || image[r][c] != initialColor) {
            return;
        }

        // Update color
        image[r][c] = newColor;

        // Recurse in 4 directions
        dfs(image, r - 1, c, initialColor, newColor); // Up
        dfs(image, r + 1, c, initialColor, newColor); // Down
        dfs(image, r, c - 1, initialColor, newColor); // Left
        dfs(image, r, c + 1, initialColor, newColor); // Right
    }
}

```

#### C++

```cpp []
#include <vector>

class Solution {
public:
    std::vector<std::vector<int>> floodFill(std::vector<std::vector<int>>& image, int sr, int sc, int color) {
        int initialColor = image[sr][sc];

        // If the starting pixel already has the target color, no changes are needed
        if (initialColor != color) {
            dfs(image, sr, sc, initialColor, color);
        }

        return image;
    }

private:
    void dfs(std::vector<std::vector<int>>& image, int r, int c, int initialColor, int newColor) {
        // Boundary check and color match check
        if (r < 0 || r >= image.size() || c < 0 || c >= image[0].size() || image[r][c] != initialColor) {
            return;
        }

        // Update color
        image[r][c] = newColor;

        // Recurse in 4 directions
        dfs(image, r - 1, c, initialColor, newColor); // Up
        dfs(image, r + 1, c, initialColor, newColor); // Down
        dfs(image, r, c - 1, initialColor, newColor); // Left
        dfs(image, r, c + 1, initialColor, newColor); // Right
    }
};

```

### Complexity Analysis

Let $M$ be the number of rows and $N$ be the number of columns in the `image`.

- **Time Complexity**: **$O(M \times N)$**
  In the worst-case scenario (e.g., every single pixel in the grid has the initial color), the algorithm will visit every pixel exactly once. For each pixel, we perform $O(1)$ operations checking neighbors.
- **Space Complexity**: **$O(M \times N)$**
  The space complexity is governed by the memory overhead of the recursive call stack. In the worst case (such as a long snake-like line or a completely uniform grid), the call stack can grow up to the total number of pixels, which is $M \times N$.

---

## Approach 2. BFS

### Intuition

While DFS explores a path as deeply as possible before backtracking, **BFS** explores the image layer by layer, radiating outward from the starting pixel $(s_r, s_c)$ like ripples in a pond.

By using a **Queue**, we can process the starting pixel, then process all its valid 4-directional neighbors, then all of _their_ neighbors, and so on. This ensures an iterative, level-by-level traversal of the connected component.

## Algorithm

1. **Edge Case Check**: Just like DFS, if the starting pixel's color is already equal to the target `color`, return the image immediately to avoid processing or getting stuck in an infinite loop.
2. **Initialize Queue**: Store the original color of the starting pixel. Create a queue to hold the coordinates `(row, col)` of the pixels to visit, and enqueue the starting position `(sr, sc)`.
3. **Change Starting Color**: Update the starting pixel's color to the new `color`.
4. **Process Queue**: While the queue is not empty:

    - Dequeue the current pixel coordinates.
    - Check its 4 neighbors (Up, Down, Left, Right).
    - For each neighbor, check if it is within the grid boundaries and if its color matches the `initialColor`.
    - If valid, change its color to the target `color` **immediately before pushing it to the queue** (this prevents duplicate entries in the queue).
    - Enqueue the neighbor's coordinates.

### Implementations

#### Java

```java []
import java.util.Queue;
import java.util.LinkedList;

class Solution {
    public int[][] floodFill(int[][] image, int sr, int sc, int color) {
        int initialColor = image[sr][sc];

        // If the starting pixel already has the target color, no changes are needed
        if (initialColor == color) {
            return image;
        }

        int rows = image.length;
        int cols = image[0].length;

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{sr, sc});
        image[sr][sc] = color; // Change color immediately upon entering queue

        // Direction vectors for moving Up, Down, Left, Right
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int r = curr[0];
            int c = curr[1];

            for (int[] dir : directions) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                // Check boundaries and if neighbor matches the original color
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && image[nr][nc] == initialColor) {
                    image[nr][nc] = color; // Modify color
                    queue.offer(new int[]{nr, nc}); // Enqueue neighbor
                }
            }
        }

        return image;
    }
}

```

#### C++

```cpp []
#include <vector>
#include <queue>

class Solution {
public:
    std::vector<std::vector<int>> floodFill(std::vector<std::vector<int>>& image, int sr, int sc, int color) {
        int initialColor = image[sr][sc];

        // If the starting pixel already has the target color, no changes are needed
        if (initialColor == color) {
            return image;
        }

        int rows = image.size();
        int cols = image[0].size();

        std::queue<std::pair<int, int>> q;
        q.push({sr, sc});
        image[sr][sc] = color; // Change color immediately upon entering queue

        // Direction vectors for moving Up, Down, Left, Right
        std::vector<std::pair<int, int>> directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!q.empty()) {
            auto [r, c] = q.front();
            q.pop();

            for (auto& dir : directions) {
                int nr = r + dir.first;
                int nc = c + dir.second;

                // Check boundaries and if neighbor matches the original color
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && image[nr][nc] == initialColor) {
                    image[nr][nc] = color; // Modify color
                    q.push({nr, nc}); // Enqueue neighbor
                }
            }
        }

        return image;
    }
};

```

### Complexity Analysis

Let $M$ be the number of rows and $N$ be the number of columns in the grid.

- **Time Complexity**: **$O(M \times N)$**
  In the worst-case scenario where all pixels share the same initial color, every single pixel will be enqueued and dequeued exactly once. For each pixel, checking the 4 directions takes a constant $O(1)$ time.
- **Space Complexity**: **$O(M \times N)$**
  The maximum size of the queue depends on the perimeter of the expanding fill region. In the worst-case scenario (e.g., a highly uniform large grid), the queue can hold up to $O(M \times N)$ coordinates at the peak of its traversal layer.
