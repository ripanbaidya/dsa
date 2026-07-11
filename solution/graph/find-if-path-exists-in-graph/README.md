# 1971. Find if Path Exists in Graph

<p align="right">Last updated - 11.07.2026</p>

- https://leetcode.com/problems/find-if-path-exists-in-graph/description/

## Approach 1: Depth-First Search

### Intuition

The problem asks whether a valid path exists between a `source` vertex and a `destination` vertex in an undirected graph.

Your approach leverages the concept of **Connected Components**. In an undirected graph, if two nodes belong to the same connected component, there is guaranteed to be a valid path between them.
Instead of doing a single targeted search from the `source`, your approach iterates through all vertices of the graph. When it encounters an unvisited node, it launches a Depth-First Search (DFS) to explore its entire connected component. During this exploration, we keep track of whether we encounter the `source` (tracked at index `0`) or the `destination` (tracked at index `1`) using a boolean tracking array (`sd`). If both flags become `true` within the same component exploration, a valid path exists, and we can immediately return `true`.

### Algorithm

1. **Graph Construction:** Build an adjacency list `adj` from the given `edges` array to efficiently look up neighbors.
2. **Visited Tracking:** Maintain a boolean array `vis` of size $n$ initialized to `false` to avoid reprocessing nodes and getting stuck in infinite loops.
3. **Component Iteration:** Loop through all vertices from `0` to $n - 1$:

    - If a vertex $i$ has not been visited, it marks the start of a new connected component.
    - Initialize a local tracking array `sd = [false, false]`.
    - Call the helper `dfs` function for this component.
    - After the DFS completes for the current component, check if `sd[0]` (source found) **and** `sd[1]` (destination found) are both `true`. If they are, return `true`.

4. **DFS Traversal (`dfs` function):**

    - Mark the current `node` as visited (`vis[node] = true`).
    - Check if the current `node` matches `source` or `destination`, and update `sd[0]` or `sd[1]` accordingly.
    - Iterate through all neighbors (`nei`) of the current `node`. If a neighbor has not been visited (`!vis[nei]`), recursively call `dfs` on that neighbor.

5. **Fallback:** If all connected components are explored and no component contains both the source and destination, return `false`.

### Implementations

**Java**

```java
class Solution {
    public boolean validPath(int n, int[][] edges, int source, int destination) {
        // Build the adjacency list
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]);
        }

        boolean[] vis = new boolean[n];

        // Iterate through all nodes to explore each connected component
        for (int i = 0; i < n; i++) {
            if (!vis[i]) {
                // source destination endpoints, where sd[0]: source found, sd[1]: destination found
                boolean[] sd = new boolean[2];
                dfs(i, adj, vis, source, destination, sd);

                // If both source and destination are found in this component, a path exists
                if (sd[0] && sd[1]) {
                    return true;
                }
            }
        }
        return false;
    }

    private void dfs(int node, List<List<Integer>> adj, boolean[] vis, int src, int dest, boolean[] sd) {
        vis[node] = true;

        if (node == src) sd[0] = true;
        if (node == dest) sd[1] = true;

        for (int nei : adj.get(node)) {
            // Traverse unvisited neighbors
            if (!vis[nei]) {
                dfs(nei, adj, vis, src, dest, sd);
            }
        }
    }
}

```

**C++**

```cpp
class Solution {
public:
    bool validPath(int n, vector<vector<int>>& edges, int source, int destination) {
        // Build the adjacency list
        vector<vector<int>> adj(n);
        for (const auto& edge : edges) {
            adj[edge[0]].push_back(edge[1]);
            adj[edge[1]].push_back(edge[0]);
        }

        vector<bool> vis(n, false);

        // Iterate through all nodes to explore each connected component
        for (int i = 0; i < n; i++) {
            if (!vis[i]) {
                vector<bool> sd(2, false); // sd[0]: source found, sd[1]: destination found
                dfs(i, adj, vis, source, destination, sd);

                // If both source and destination are found in this component, a path exists
                if (sd[0] && sd[1]) {
                    return true;
                }
            }
        }
        return false;
    }

private:
    void dfs(int node, const vector<vector<int>>& adj, vector<bool>& vis,
             int src, int dest, vector<bool>& sd) {
        vis[node] = true;

        if (node == src) sd[0] = true;
        if (node == dest) sd[1] = true;

        for (int nei : adj[node]) {
            // Traverse unvisited neighbors
            if (!vis[nei]) {
                dfs(nei, adj, vis, src, dest, sd);
            }
        }
    }
};

```

### Complexity Analysis

Let $V$ be the number of vertices ($n$) and $E$ be the number of edges (`edges.length`).

- Time Complexity: $\mathcal{O}(V + E)$

    - **Graph Construction:** Iterating through the list of edges takes $\mathcal{O}(E)$ time to build the adjacency list.
    - **DFS Traversal:** Each vertex is marked as visited and processed exactly once across the outer loop iterations. For every vertex visited, we iterate through its outgoing edges. Summing up the degrees of all vertices in an undirected graph gives $2E$. Thus, the total traversal time is bounded by $\mathcal{O}(V + E)$.
    - **Total Time Complexity:** $\mathcal{O}(V + E)$.

- Space Complexity: $\mathcal{O}(V + E)$

    - **Adjacency List:** Storing the graph requires $\mathcal{O}(V + E)$ space.
    - **Visited Array:** The `vis` array requires $\mathcal{O}(V)$ space.
    - **Call Stack:** In the worst-case scenario (e.g., a graph shaped like a straight line), the recursion stack for DFS can go up to $V$ levels deep, requiring $\mathcal{O}(V)$ space.
    - **Total Space Complexity:** $\mathcal{O}(V + E)$.
