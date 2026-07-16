# [Shortest Path in Unweighted Graph](https://www.geeksforgeeks.org/problems/shortest-path-in-undirected-graph-having-unit-distance/1)

<p align="right">Last updated - 16.07.2026</p>

### Intuition

In an **unweighted graph** (or a graph where every edge has an identical unit weight of `1`), finding the shortest path boils down to exploring the graph level by level.

**Breadth-First Search (BFS)** is the perfect tool here. Think of it like throwing a stone into still water: the ripples spread outward radially, hitting all points at a distance of `1` first, then all points at a distance of `2`, and so forth. Because BFS explores nodes in increasing order of their distance from the source, the **very first time** we encounter the destination node, we are guaranteed to have found the absolute shortest path to it.

Using a standard `visited` tracker ensures we never re-evaluate a node via a longer, circuitous path later on.

### Algorithm

1. **Build Adjacency List:** Convert the given edge list into a standard undirected adjacency list representation for quick neighbor lookup.
2. **Setup Tracking Structures:** \* A `visited` array (or set) to ensure each node is processed exactly once.
   - A standard `Queue` storing pairs of `(currentNode, currentDistance)`.

3. **Initialize:** Mark the `src` as visited and push `(src, 0)` into the queue.
4. **BFS Traversal:** While the queue is not empty:
   - Pop the front element `(currentNode, currentDistance)`.
   - **Early Exit:** If `currentNode == dest`, immediately return `currentDistance`.
   - **Explore Neighbors:** For each unvisited neighbor of `currentNode`:
     - Mark the neighbor as visited.
     - Enqueue `(neighborNode, currentDistance + 1)`.

5. **Fallback:** If the queue becomes empty and the destination was never reached, it means the graph is disconnected. Return `-1`.

### Implementation

**Java**

```java
class Solution {
    public int shortestPath(int V, int[][] edges, int src, int dest) {
        // Build the Adjacency list
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            adj.get(u).add(v);
            adj.get(v).add(u);
        }

        // BFS tracking structures
        boolean[] vis = new boolean[V];
        Deque<int[]> que = new ArrayDeque<>(); // {node, distanceFromSource}

        // Initialize BFS with the source node
        que.offer(new int[] {src, 0});
        vis[src] = true;

        // Traverse level by level
        while (!que.isEmpty()) {
            int[] current = que.poll();
            int currentNode = current[0];
            int currentDistance = current[1];

            // Due to the level-order nature of BFS, the first time we see the destination is the shortest path
            if (currentNode == dest) {
                return currentDistance;
            }

            // Explore all unvisited neighbors
            for (int neighborNode : adj.get(currentNode)) {
                if (!vis[neighborNode]) {
                    vis[neighborNode] = true;
                    que.offer(new int[] {neighborNode, currentDistance + 1});
                }
            }
        }

        // Destination node is unreachable from the source
        return -1;
    }
}

```

**C++**

```cpp
using namespace std;

class Solution {
public:
    int shortestPath(int V, vector<vector<int>>& edges, int src, int dest) {
        // Build the Adjacency List
        vector<vector<int>> adj(V);
        for (const auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            adj[u].push_back(v);
            adj[v].push_back(u);
        }

        // BFS tracking structures
        vector<bool> vis(V, false);
        queue<pair<int, int>> que; // {node, distanceFromSource}

        // Initialize BFS with the source node
        que.push({src, 0});
        vis[src] = true;

        // Traverse level by level
        while (!que.empty()) {
            auto [currentNode, currentDistance] = que.front();
            que.pop();

            // Due to the level-order nature of BFS, the first time we see the destination is the shortest path
            if (currentNode == dest) {
                return currentDistance;
            }

            // Explore all unvisited neighbors
            for (int neighborNode : adj[currentNode]) {
                if (!vis[neighborNode]) {
                    vis[neighborNode] = true;
                    que.push({neighborNode, currentDistance + 1});
                }
            }
        }

        // Destination node is unreachable from the source
        return -1;
    }
};

```

### Complexity Analysis

Let $V$ represent the number of vertices (`V`) and $E$ represent the number of edges.

- **Time Complexity:** $\mathcal{O}(V + E)$
  - **Graph Construction:** Building the adjacency list takes $\mathcal{O}(E)$ time as we iterate through all edges.
  - **BFS Traversal:** In the worst-case scenario, every single node is pushed into and popped out of the queue exactly once ($\mathcal{O}(V)$). For every node popped, we check all its outgoing neighbors. Summed across all nodes, this maps to examining every edge in the graph, adding an $\mathcal{O}(E)$ component.

- **Space Complexity:** $\mathcal{O}(V + E)$
  - $\mathcal{O}(V + E)$ space to maintain the graph's structural adjacency list representation.
  - $\mathcal{O}(V)$ auxiliary space allocated for both the `vis` array and the queue array structures.

## Reference

- https://www.geeksforgeeks.org/problems/topological-sort/1