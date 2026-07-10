# Directed Graph Cycle

<p align="right">Last updated - 10.07.2026</p>

- https://www.geeksforgeeks.org/problems/detect-cycle-in-a-directed-graph/1

## Approach 1: Depth-First Search (DFS)

### Why the Undirected Graph DFS Approach Fails Here

In an undirected graph, a cycle exists if we encounter a node that has already been visited and is **not** the immediate parent of the current node.

However, in a **directed graph**, simply encountering an already visited node does not guarantee a cycle. Consider a "diamond" shape graph: `0 -> 1`, `0 -> 2`, `1 -> 3`, and `2 -> 3`.

- If we perform DFS starting from `0`, we might visit `0 -> 1 -> 3`. Nodes `0`, `1`, and `3` are marked as visited.
- The DFS backtracks to `0` and then explores `0 -> 2 -> 3`.
- When we are at node `2` and look at its neighbor `3`, we see that `3` is already visited.

In an undirected graph approach, this would falsely report a cycle. But in this directed graph, there is **no cycle** because all edges point forward toward `3`. To safely confirm a cycle in a directed graph, the visited node must be part of the _current recursion stack_ (the active path being explored).

### Intuition

To detect a cycle in a directed graph, we need to find a **Back Edge**. A back edge is an edge that points from a node to one of its ancestors in the current DFS computation tree.

To track this, we maintain two tracking mechanisms:

1. `vis[]` (Visited array): Tracks whether a node has been visited across the entire execution to avoid redundant computations.
2. `pathVis[]` (Path Visited array): Tracks nodes that are part of the _current active recursion stack_.

If we move from a `node` to its neighbor `nei`, and find that `nei` is already visited (`vis[nei] == true`) **and** it is present in the current recursion path (`pathVis[nei] == true`), it means we have found a back edge, confirming the presence of a cycle. When backtracking from a node, we must unmark it from `pathVis[]` since it is no longer part of the active exploration path.

### Implementations

**Java**

```java
class Solution {
    // Function to detect cycle in a directed graph.
    public boolean isCyclic(int V, int[][] edges) {
        // Step 1: Build the adjacency list from the given edges
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
        }

        // Track overall visited nodes
        boolean[] vis = new boolean[V];
        // Track nodes in the current DFS recursion stack
        boolean[] pathVis = new boolean[V];

        // Step 2: Traverse all components of the graph
        for (int i = 0; i < V; i++) {
            if (!vis[i]) {
                if (dfs(i, vis, pathVis, adj))
                    return true; // Cycle detected
            }
        }

        return false; // No cycle found
    }

    private boolean dfs(int node, boolean[] vis, boolean[] pathVis,
        List<List<Integer>> adj) {
        // Mark the current node as visited and active in the current path
        vis[node] = true;
        pathVis[node] = true;

        // Step 3: Traverse all adjacent neighbors
        for (int nei : adj.get(node)) {
            // If the neighbor is not visited, recursively visit it
            if (!vis[nei]) {
                if (dfs(nei, vis, pathVis, adj))
                    return true;
            }
            // If the neighbor is visited AND it is in the current path, a cycle exists
            else if (vis[nei] && pathVis[nei]) {
                return true;
            }
        }

        // Step 4: Backtrack - remove the node from the current path tracking
        pathVis[node] = false;
        return false;
    }
}

```

**C++**

```cpp
#include <bits/stdc++.h>
using pointer = std::vector<std::vector<int>>;

class Solution {
private:
    bool dfs(int node, std::vector<bool>& vis, std::vector<bool>& pathVis,
             const std::vector<std::vector<int>>& adj) {
        // Mark the current node as visited and active in the current recursion path
        vis[node] = true;
        pathVis[node] = true;

        // Traverse all outgoing neighbors
        for (int nei : adj[node]) {
            // If neighbor is unvisited, run DFS on it
            if (!vis[nei]) {
                if (dfs(nei, vis, pathVis, adj))
                    return true;
            }
            // If neighbor is visited and active in the current recursion path, cycle found
            else if (vis[nei] && pathVis[nei]) {
                return true;
            }
        }

        // Backtrack: remove node from current path tracker before returning
        pathVis[node] = false;
        return false;
    }

public:
    // Function to detect cycle in a directed graph.
    bool isCyclic(int V, std::vector<std::vector<int>>& edges) {
        // Step 1: Build the adjacency list
        std::vector<std::vector<int>> adj(V);
        for (const auto& edge : edges) {
            adj[edge[0]].push_back(edge[1]);
        }

        std::vector<bool> vis(V, false);
        std::vector<bool> pathVis(V, false);

        // Step 2: Loop through all components of the graph
        for (int i = 0; i < V; i++) {
            if (!vis[i]) {
                if (dfs(i, vis, pathVis, adj)) {
                    return true;
                }
            }
        }

        return false;
    }
};

```

## Approach 2: Breadth-First Search (BFS) / Kahn's Algorithm

_(Note: While the header specifies DFS, cycle detection in a directed graph is optimally solved via Kahn's Algorithm (BFS Topology Sort) as an alternative Approach 2)._

### Intuition

Kahn’s Algorithm is traditionally used to find the Topological Sort of a Directed Acyclic Graph (DAG). A topological sort is only possible if the graph has **no cycles**.

The algorithm works by continuously removing nodes with an in-degree (number of incoming edges) of `0`. If a graph contains a cycle, the nodes involved in the cycle will never have their in-degree reduced to `0` because they depend on each other cyclically.

Therefore, if the count of elements in the generated topological sort sequence is **less than** the total number of vertices ($V$), it guarantees that a cycle exists in the graph.

### Implementations

**Java**

```java
class SolutionBFS {
    public boolean isCyclic(int V, int[][] edges) {
        // Step 1: Build adjacency list and compute in-degrees
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        int[] inDegree = new int[V];
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            inDegree[edge[1]]++;
        }

        // Step 2: Push all nodes with in-degree 0 into a queue
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        // Step 3: Process the queue and keep a count of visited nodes
        int visitedCount = 0;
        while (!queue.isEmpty()) {
            int node = queue.poll();
            visitedCount++;

            // Reduce in-degree for all adjacent neighbors
            for (int nei : adj.get(node)) {
                inDegree[nei]--;
                // If in-degree becomes 0, add it to the queue
                if (inDegree[nei] == 0) {
                    queue.add(nei);
                }
            }
        }

        // If visitedCount equals V, then topological sort is complete (No Cycle)
        // If visitedCount != V, a cycle exists.
        return visitedCount != V;
    }
}

```

**C++**

```cpp
#include <bits/stdc++.h>

class SolutionBFS {
public:
    bool isCyclic(int V, std::vector<std::vector<int>>& edges) {
        // Step 1: Build adjacency list and track in-degrees
        std::vector<std::vector<int>> adj(V);
        std::vector<int> inDegree(V, 0);
        for (const auto& edge : edges) {
            adj[edge[0]].push_back(edge[1]);
            inDegree[edge[1]]++;
        }

        // Step 2: Queue to store elements with in-degree 0
        std::queue<int> q;
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                q.push(i);
            }
        }

        // Step 3: Standard BFS process tracking the processed nodes count
        int visitedCount = 0;
        while (!q.empty()) {
            int node = q.front();
            q.pop();
            visitedCount++;

            for (int nei : adj[node]) {
                inDegree[nei]--;
                if (inDegree[nei] == 0) {
                    q.push(nei);
                }
            }
        }

        // If we processed all vertices, it's a DAG (no cycle). Otherwise, a cycle exists.
        return visitedCount != V;
    }
};

```

## Complexity Analysis

### Approach 1 (DFS)

- **Time Complexity:** $\mathcal{O}(V + E)$
  Where $V$ is the number of vertices and $E$ is the number of edges. We construct an adjacency list spending $\mathcal{O}(V + E)$ time and visit each vertex and edge at most once during the DFS traversal.
- **Space Complexity:** $\mathcal{O}(V)$
  We utilize two boolean arrays (`vis` and `pathVis`), each of size $V$, taking $\mathcal{O}(V)$ memory space. The auxiliary stack space for the recursive DFS calls can grow up to $\mathcal{O}(V)$ in the worst-case scenario (e.g., a linear graph).

### Approach 2 (BFS / Kahn's)

- **Time Complexity:** $\mathcal{O}(V + E)$
  Calculating in-degrees takes $\mathcal{O}(V + E)$ time. Every vertex is pushed and popped from the queue exactly once, and all its outgoing edges are explored, leading to a complete traversal time of $\mathcal{O}(V + E)$.
- **Space Complexity:** $\mathcal{O}(V)$
  An array tracking `inDegree` of size $V$ and a `Queue` storing processing nodes (up to a maximum of $V$ nodes simultaneously) use linear $\mathcal{O}(V)$ space.
