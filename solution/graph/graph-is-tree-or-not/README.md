# [Graph is Tree or Not](https://www.geeksforgeeks.org/problems/is-it-a-tree/1)

<p align="right">Last updated - 22.07.2026</p>

To determine whether an undirected graph is a **valid tree**, we need to check two core properties:

1. **Connectedness:** Every node in the graph must be reachable from any other node (it forms a single connected component).
2. **Acyclicity:** The graph must not contain any cycles (no closed loops).

An important mathematical property of a tree with $n$ nodes is that **it must have exactly $n - 1$ edges**.

## Intuition

Think of a tree like a family tree or a network where every point is connected without any redundant "shortcuts" or loops.

- **Edge Count Check ($m == n - 1$):** If an undirected graph has $n$ nodes, it **must** have exactly $n - 1$ edges to be a tree.
  - If it has fewer than $n - 1$ edges, it's impossible for all nodes to be connected.
  - If it has more than $n - 1$ edges, a cycle is guaranteed to exist.

- **Traversal (DFS):** Even with $n - 1$ edges, a graph could be disconnected and contain a cycle in one of its separate components (for example, a triangle of 3 nodes plus 1 completely isolated node). To rule this out, we start a **Depth-First Search (DFS)** from node `0`:
  - As we visit nodes, if we ever run into a node we've _already visited_ that isn't the parent we just came from, we've found a cycle!
  - After the DFS finishes, if every node was visited, the graph is connected.

Combined, **(Edges = $n - 1$) + (All nodes connected from node 0) = Valid Tree**.

## Algorithm

1. **Quick Base Check:** If the number of edges $m \neq n - 1$, immediately return `0` (False).
2. **Build Adjacency List:** Construct an adjacency list representation of the undirected graph.
3. **Run DFS:**
   - Maintain a `visited` array initialized to zeros.
   - Start DFS from node `0`, keeping track of the `parent` node to avoid counting the edge we just traveled across as a cycle.
   - If during DFS we encounter an already-visited neighbor that is **not** the parent node, return `true` (cycle detected).

4. **Check Connectivity:**
   - If DFS detects a cycle, return `0`.
   - Otherwise, check the `visited` array. If any node was left unvisited, return `0`.

5. If both cycle and connectivity checks pass, return `1` (True).

## Implementation

**Java**

```java
class Solution {
    public int isTree(int n, int m, int[][] edges) {
        // Property 1: A tree with 'n' nodes must have exactly 'n - 1' edges.
        if (m != n - 1) {
            return 0;
        }

        // Build the adjacency list for the undirected graph
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]);
        }

        boolean[] vis = new boolean[n];

        // Property 2: Check for cycles starting from node 0
        if (hasCycleDFS(0, -1, vis, adj)) {
            return 0; // Cycle detected
        }

        // Property 3: Check if all nodes were visited (graph is connected)
        for (boolean visited : vis) {
            if (!visited) {
                return 0; // Disconnected node found
            }
        }

        return 1; // Graph is a valid tree
    }

    private boolean hasCycleDFS(int node, int parent, boolean[] vis, List<List<Integer>> adj) {
        vis[node] = true;

        for (int neighbor : adj.get(node)) {
            // If neighbor is not visited, recursively visit it
            if (!vis[neighbor]) {
                if (hasCycleDFS(neighbor, node, vis, adj)) {
                    return true;
                }
            }
            // If neighbor is visited and not the immediate parent, a cycle exists
            else if (neighbor != parent) {
                return true;
            }
        }

        return false;
    }
}

```

**C++**

```cpp
class Solution {
public:
    int isTree(int n, int m, vector<vector<int>>& edges) {
        // Property 1: A tree with 'n' nodes must have exactly 'n - 1' edges.
        if (m != n - 1) {
            return 0;
        }

        // Build the adjacency list for the undirected graph
        vector<vector<int>> adj(n);
        for (const auto& edge : edges) {
            adj[edge[0]].push_back(edge[1]);
            adj[edge[1]].push_back(edge[0]);
        }

        vector<bool> vis(n, false);

        // Property 2: Check for cycles starting from node 0
        if (hasCycleDFS(0, -1, vis, adj)) {
            return 0; // Cycle detected
        }

        // Property 3: Check if all nodes were visited (graph is connected)
        for (bool visited : vis) {
            if (!visited) {
                return 0; // Disconnected node found
            }
        }

        return 1; // Graph is a valid tree
    }

private:
    bool hasCycleDFS(int node, int parent, vector<bool>& vis, const vector<vector<int>>& adj) {
        vis[node] = true;

        for (int neighbor : adj[node]) {
            // If neighbor is not visited, recursively visit it
            if (!vis[neighbor]) {
                if (hasCycleDFS(neighbor, node, vis, adj)) {
                    return true;
                }
            }
            // If neighbor is visited and not the immediate parent, a cycle exists
            else if (neighbor != parent) {
                return true;
            }
        }

        return false;
    }
};

```

## Complexity Analysis

- **Time Complexity:** $\mathcal{O}(V + E)$
  - Building the adjacency list takes $\mathcal{O}(E)$ time, where $E = m$.
  - The DFS visits every node $V = n$ and explores every edge $E = m$ at most twice.
  - Checking the visited array takes $\mathcal{O}(V)$ time.
  - Overall time simplifies to $\mathcal{O}(n + m)$. Since $m = n - 1$, this runs in linear time $\mathcal{O}(n)$.

- **Space Complexity:** $\mathcal{O}(V + E)$
  - **Adjacency List:** Stores $n$ nodes and $2m$ directed entries, taking $\mathcal{O}(n + m)$ space.
  - **Visited Array:** Uses $\mathcal{O}(n)$ space.
  - **Recursion Stack:** In the worst-case scenario (a skewed graph like a line), the recursion stack can go up to $n$ calls deep, requiring $\mathcal{O}(n)$ stack space.
