# 785. Is Graph Bipartite?

<p align="right">Last updated - 09.07.2026</p>

- https://leetcode.com/problems/is-graph-bipartite/

### Intuition

A graph is **bipartite** if its vertices can be divided into two independent sets, say Set 0 and Set 1, such that every edge connects a vertex in Set 0 to a vertex in Set 1. In terms of coloring, this means we should be able to color the graph using exactly **two colors** (0 and 1) such that no two adjacent nodes share the same color.

Using **Breadth-First Search (BFS)**, we can simulate this coloring process layer by layer:

1. Start with an uncolored node and assign it a color (e.g., `0`).
2. Traverse all its neighbors. If a neighbor is uncolored, it must be assigned the opposite color (`1`).
3. If a neighbor is already colored and has the _same_ color as the current node, we have found a conflict (an odd-length cycle). This means it is impossible to partition the graph into two independent sets, so the graph is not bipartite.
4. Since a graph can be disconnected, we must repeat this check for every unvisited node to cover all disconnected components.

### Algorithm

1. **Initialization:**
   - Create a `color` array of size `V` (number of vertices) initialized to `-1` to represent that all nodes are initially uncolored.

2. **Component Traversal:**
   - Loop through each node from `0` to `V - 1`. If a node is uncolored (`color[i] == -1`), initiate a BFS from that node.

3. **BFS Function:**
   - Use a queue to facilitate BFS. Push the starting node into the queue and color it `0`.
   - While the queue is not empty:
     - Pop the front node `curNode`.
     - Iterate through all its neighbors `nei` in the adjacency list `graph[curNode]`.
     - **Case 1: Neighbor is uncolored (`color[nei] == -1`)**
       - Assign it the opposite color of `curNode`. This can be efficiently done using bitwise XOR: `color[nei] = color[curNode] ^ 1`.
       - Push `nei` into the queue.

     - **Case 2: Neighbor is already colored**
       - Check if `color[nei] == color[curNode]`. If they have the same color, return `false` immediately (graph is not bipartite).

4. **Conclusion:**

- If the BFS completes for all components without any color conflicts, return `true`.

### Implementation

**Java**

```java []
class Solution {
    public boolean isBipartite(int[][] graph) {
        int v = graph.length;
        int[] color = new int[v];
        Arrays.fill(color, -1); // Initialize all vertices as uncolored (-1)

        // Loop through all nodes to handle disconnected components
        for (int i = 0; i < v; i++) {
            if (color[i] == -1) {
                // If any component is not bipartite, the entire graph is not bipartite
                if (!bfs(i, graph, color)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean bfs(int startNode, int[][] graph, int[] color) {
        Queue<Integer> que = new ArrayDeque<>();

        que.offer(startNode);
        color[startNode] = 0; // Start coloring the first node of the component with 0

        while (!que.isEmpty()) {
            int curNode = que.poll();
            int curColor = color[curNode];

            // Traverse all adjacent neighbors
            for (int nei : graph[curNode]) {
                // If the neighbor is not colored yet
                if (color[nei] == -1) {
                    color[nei] = curColor ^ 1; // Assign opposite color (0 -> 1 or 1 -> 0)
                    que.offer(nei);
                }
                // If the neighbor has the same color as the current node, conflict found
                else if (color[nei] == curColor) {
                    return false;
                }
            }
        }
        return true;
    }
}

```

**C++**

```cpp []
using namespace std;

class Solution {
public:
    bool isBipartite(vector<vector<int>>& graph) {
        int v = graph.size();
        vector<int> color(v, -1); // Initialize all vertices as uncolored (-1)

        // Loop through all nodes to handle disconnected components
        for (int i = 0; i < v; i++) {
            if (color[i] == -1) {
                // If any component is not bipartite, the entire graph is not bipartite
                if (!bfs(i, graph, color)) {
                    return false;
                }
            }
        }
        return true;
    }

private:
    bool bfs(int startNode, const vector<vector<int>>& graph, vector<int>& color) {
        queue<int> que;

        que.push(startNode);
        color[startNode] = 0; // Start coloring the first node of the component with 0

        while (!que.empty()) {
            int curNode = que.front();
            que.pop();
            int curColor = color[curNode];

            // Traverse all adjacent neighbors
            for (int nei : graph[curNode]) {
                // If the neighbor is not colored yet
                if (color[nei] == -1) {
                    color[nei] = curColor ^ 1; // Assign opposite color (0 -> 1 or 1 -> 0)
                    que.push(nei);
                }
                // If the neighbor has the same color as the current node, conflict found
                else if (color[nei] == curColor) {
                    return false;
                }
            }
        }
        return true;
    }
};

```

### Complexity Analysis

- **Time Complexity:** $\mathcal{O}(V + E)$
  - $V$ is the number of vertices (nodes) and $E$ is the number of edges.
  - Every vertex is pushed into and popped from the queue exactly once.
  - Every edge is examined exactly twice (once from each of its endpoints). Thus, the traversal takes linear time proportional to the size of the graph.

- **Space Complexity:** $\mathcal{O}(V)$
  - The `color` array takes $\mathcal{O}(V)$ space to keep track of the colors of all nodes.
  - The BFS queue can hold at most $V$ nodes in the worst case (e.g., a star graph structure).

## Reference

- Bipartite graph - https://youtu.be/yelUNp4l740?si=5TTqsFoMWFIcy4ri
