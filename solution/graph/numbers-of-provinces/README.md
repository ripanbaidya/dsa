## 547. Number of Provinces

<p align="right">Last updated - 07.07.2026</p>

**Prerequisites**
- DFS - https://github.com/ripanbaidya/dsa/tree/main/solution/graph/dfs
- BFS - https://github.com/ripanbaidya/dsa/tree/main/solution/graph/bfs

## 1. Intuition - DFS

The problem asks us to find the total number of connected components (provinces) in an undirected graph. Each city is a node, and a direct connection between city $i$ and city $j$ is represented by `isConnected[i][j] = 1`.

If city $A$ is connected to city $B$, and city $B$ is connected to city $C$, then $A$ and $C$ belong to the same province. This is a classic graph traversal problem where we need to find the number of disjoint sets or connected components. We can discover all cities belonging to a single province by starting a traversal (either Depth First Search (DFS) or Breadth First Search (BFS)) from an unvisited city.

## Approach

1. **Graph Representation**: The input is given as an adjacency matrix. We can traverse it directly without converting it to an adjacency list to save space and time, though converting it (as seen in your initial Java template) is also a valid approach.
2. **Visited Array**: Maintain a `visited` array/vector of size $n$ (number of cities) initialized to 0 (unvisited) to keep track of the cities we have already processed.
3. **Component Counting**:

- Iterate through all cities from `0` to `n - 1`.
- If a city has not been visited, it means we have discovered a new province. Increment our province counter (`cnt`).
- Start a traversal (DFS/BFS) from this city to mark all other cities reachable from it as visited.

4. **Traversal Logic (DFS/BFS)**: Inside the traversal, for the current city, check all other cities. If another city is connected to it and hasn't been visited yet, recursively visit it (DFS) or push it to a queue (BFS).

## Implementations

**Java**

```java []
class Solution {
    public int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        int[] visited = new int[n];
        int cnt = 0;

        for (int i = 0; i < n; i++) {
            if (visited[i] == 0) {
                dfs(i, isConnected, visited);
                cnt++;
            }
        }
        return cnt;
    }

    private void dfs(int node, int[][] isConnected, int[] visited) {
        visited[node] = 1;
        for (int neighbor = 0; neighbor < isConnected.length; neighbor++) {
            if (isConnected[node][neighbor] == 1 && visited[neighbor] == 0) {
                dfs(neighbor, isConnected, visited);
            }
        }
    }
}

```

**C ++**

```cpp []
#include <vector>

class Solution {
public:
    int findCircleNum(std::vector<std::vector<int>>& isConnected) {
        int n = isConnected.size();
        std::vector<int> visited(n, 0);
        int cnt = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs(i, isConnected, visited);
                cnt++;
            }
        }
        return cnt;
    }

private:
    void dfs(int node, const std::vector<std::vector<int>>& isConnected, std::vector<int>& visited) {
        visited[node] = 1;
        for (int neighbor = 0; neighbor < isConnected.size(); neighbor++) {
            if (isConnected[node][neighbor] == 1 && !visited[neighbor]) {
                dfs(neighbor, isConnected, visited);
            }
        }
    }
};

```

## Complexity Analysis

- **Time Complexity:** **$O(n^2)$**, We iterate through the vertices of the graph from $0$ to $n-1$. For each vertex, the DFS/BFS traversal scans its entire row in the matrix of size $n$ to find its neighbors. Since every node is visited exactly once, the inner loop runs a total of $n$ times for each node, leading to a total time complexity of $O(n^2)$, where $n$ is the number of cities.

- **Space Complexity:** **$O(n)$**, The space is occupied by the `visited` array of size $n$. Additionally, in the worst-case scenario (where all cities are linearly connected in one single province), the recursion stack for DFS (or the queue size in BFS) will take up $O(n)$ space.

---

## 2. Intuition - BFS

The problem asks us to find the total number of connected components (provinces) in an undirected graph. Each city is a node, and a direct connection between city $i$ and city $j$ is represented by `isConnected[i][j] = 1`.

Instead of diving deep into a single path like DFS, **Breadth-First Search (BFS)** explores the graph layer by layer using a queue. When we encounter an unvisited city, we start a new BFS traversal. The queue ensures that we visit all cities directly connected to the starting city, then all cities connected to those cities, and so on, until the entire province (connected component) is uncovered. Each time we have to initiate a new BFS from the outer loop, it means we have discovered a brand-new province.

## Approach

1. **Initialization**:
   - A `visited` array (or vector) of size $n$ keeps track of which cities have been processed to avoid redundant checks and infinite loops.
   - A counter `cnt` tracks the number of individual provinces.

2. **Outer Loop**: Iterate through every city from `0` to `n - 1`. If a city has not been visited yet:
   - It means it belongs to a new province. Increment `cnt`.
   - Launch a `bfs` starting from this city to mark all reachable cities within its province.

3. **BFS Mechanism**:
   - Initialize a queue and push the starting city into it. Mark it as visited.
   - While the queue is not empty, pop the current city (`currNode`).
   - Iterate through all potential neighbors `nei` from `0` to `n - 1`. If `isConnected[currNode][nei] == 1` and `nei` is not yet visited, push `nei` to the queue and mark it as visited.

## Implementations

**Java**

```java []
import java.util.Queue;
import java.util.ArrayDeque;

class Solution {

    public int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        int cnt = 0; // Number of provinces
        int[] vis = new int[n];

        for (int i = 0; i < n; i++) {
            if (vis[i] == 0) {
                bfs(i, isConnected, n, vis);
                cnt++;
            }
        }

        return cnt;
    }

    private void bfs(int start, int[][] edges, int n, int[] vis) {
        Queue<Integer> q = new ArrayDeque<>();
        q.offer(start);
        vis[start] = 1;

        while (!q.isEmpty()) {
            int currNode = q.poll();

            for (int nei = 0; nei < n; nei++) {
                // If there is a connection and the neighbor hasn't been visited
                if (edges[currNode][nei] == 1 && vis[nei] == 0) {
                    q.offer(nei);
                    vis[nei] = 1; // Mark immediately upon pushing to prevent duplicate queue entries
                }
            }
        }
    }
}

```

**C ++**

```cpp []
#include <vector>
#include <queue>

class Solution {
public:
    int findCircleNum(std::vector<std::vector<int>>& isConnected) {
        int n = isConnected.size();
        int cnt = 0; // Number of provinces
        std::vector<int> vis(n, 0);

        for (int i = 0; i < n; i++) {
            if (vis[i] == 0) {
                bfs(i, isConnected, n, vis);
                cnt++;
            }
        }

        return cnt;
    }

private:
    void bfs(int start, const std::vector<std::vector<int>>& edges, int n, std::vector<int>& vis) {
        std::queue<int> q;
        q.push(start);
        vis[start] = 1;

        while (!q.empty()) {
            int currNode = q.front();
            q.pop();

            for (int nei = 0; nei < n; nei++) {
                // If there is a connection and the neighbor hasn't been visited
                if (edges[currNode][nei] == 1 && vis[nei] == 0) {
                    q.push(nei);
                    vis[nei] = 1; // Mark immediately upon pushing to prevent duplicate queue entries
                }
            }
        }
    }
};

```

## Complexity Analysis

- **Time Complexity:** **$O(n^2)$**, Even though we use BFS, the input is given as an $n \times n$ adjacency matrix. Every city (node) is pushed and popped from the queue exactly once. Each time a node is popped, we scan its entire row of size $n$ to find its neighbors. Thus, the inner loop runs a total of $n$ times for each of the $n$ nodes, giving an overall time complexity of $O(n^2)$.

- **Space Complexity:** **$O(n)$**, The space is determined by the `vis` array/vector of size $n$. Additionally, in the worst-case scenario (e.g., a star graph where one center city is connected to all other $n-1$ cities), the BFS queue will hold up to $n-1$ elements at once, which consumes $O(n)$ space.
