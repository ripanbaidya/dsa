# 2685. Count the Number of Complete Components

<p align="right">Last updated - 10.07.2026</p>

- https://leetcode.com/problems/count-the-number-of-complete-components/description/

## Approach 1: Depth-First Search

### Intuition

A connected component in an undirected graph is **complete** (also known as a clique) if every vertex in that component is connected to every other vertex in the same component.

Mathematically, if a connected component contains $k$ nodes, then for it to be completely connected, each node must have a degree of exactly $k - 1$. If we sum up the degrees of all nodes in this component, the total number of directed edge occurrences must equal:

$$totalEdges = k \times (k - 1)$$

> **Why $k \times (k - 1)$?** In an undirected graph representation, when we sum up `adj.get(node).size()` for all nodes in a component, each undirected edge is counted exactly twice (once from each endpoint). A complete graph of $k$ vertices has $\frac{k(k-1)}{2}$ undirected edges, so traversing their adjacency lists yields $k(k-1)$ total edge counts.

By running a standard Depth-First Search (DFS), we can isolate each connected component, count its total vertices (`totalNodes`), sum up their neighbor counts (`totalEdges`), and check if the condition holds.

### Algorithm

1. **Graph Construction:** \* Convert the given edge list into an adjacency list `adj` for efficient neighbor lookups.
2. **Component Traversal:**
   - Maintain a `vis` (visited) array of size `n` initialized to `false`.
   - Iterate through all vertices from `0` to `n - 1`. If a vertex has not been visited, it marks the start of a new connected component.

3. **DFS Exploration:**
   - For an unvisited vertex, initialize tracking variables for the component: `totalNodes = 0` and `totalEdges = 0`.
   - During the DFS traversal of this component:
     - Mark the current node as visited.
     - Increment `totalNodes` by 1.
     - Add the size of the current node's adjacency list (`adj.get(node).size()`) to `totalEdges`.
     - Recursively visit all unvisited neighbors.

4. **Completeness Check:**
   - After the DFS for a component finishes, calculate the required edge count for it to be complete: $expectedEdges = totalNodes \times (totalNodes - 1)$.
   - If `totalEdges == expectedEdges`, increment your answer counter `ans`.

### Implementation

**Java**

```java
class Solution {
    public int countCompleteComponents(int n, int[][] edges) {
        // Step 1: Build the adjacency list
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]);
        }

        boolean[] vis = new boolean[n];
        int completeComponentsCount = 0;

        // Step 2: Iterate through all nodes to find components
        for (int i = 0; i < n; i++) {
            if (!vis[i]) {
                // Using 1-element arrays to pass variables by reference in Java
                int[] totalNodes = new int[1];
                int[] totalEdges = new int[1];

                dfs(i, adj, vis, totalNodes, totalEdges);

                // A complete component with 'k' nodes must have k * (k - 1) total edge occurrences
                int expectedEdges = totalNodes[0] * (totalNodes[0] - 1);

                if (totalEdges[0] == expectedEdges) {
                    completeComponentsCount++;
                }
            }
        }

        return completeComponentsCount;
    }

    private void dfs(int node, List<List<Integer>> adj, boolean[] vis, int[] totalNodes, int[] totalEdges) {
        vis[node] = true;
        totalNodes[0] += 1;
        totalEdges[0] += adj.get(node).size(); // Count all outgoing edge definitions for this node

        for (int nei : adj.get(node)) {
            if (!vis[nei]) {
                dfs(nei, adj, vis, totalNodes, totalEdges);
            }
        }
    }
}

```

**C++**

```cpp
using namespace std;

class Solution {
public:
    int countCompleteComponents(int n, vector<vector<int>>& edges) {
        // Step 1: Build the adjacency list
        vector<vector<int>> adj(n);
        for (const auto& edge : edges) {
            adj[edge[0]].push_back(edge[1]);
            adj[edge[1]].push_back(edge[0]);
        }

        vector<bool> vis(n, false);
        int completeComponentsCount = 0;

        // Step 2: Iterate through all nodes to find components
        for (int i = 0; i < n; i++) {
            if (!vis[i]) {
                int totalNodes = 0;
                int totalEdges = 0;

                dfs(i, adj, vis, totalNodes, totalEdges);

                // A complete component with 'k' nodes must have k * (k - 1) total edge occurrences
                int expectedEdges = totalNodes * (totalNodes - 1);

                if (totalEdges == expectedEdges) {
                    completeComponentsCount++;
                }
            }
        }

        return completeComponentsCount;
    }

private:
    void dfs(int node, const vector<vector<int>>& adj, vector<bool>& vis, int& totalNodes, int& totalEdges) {
        vis[node] = true;
        totalNodes += 1;
        totalEdges += adj[node].size(); // Count all outgoing edge definitions for this node

        for (int nei : adj[node]) {
            if (!vis[nei]) {
                dfs(nei, adj, vis, totalNodes, totalEdges);
            }
        }
    }
};

```

### Complexity Analysis

- **Time Complexity:** $\mathcal{O}(V + E)$
  - $V = n$ (number of vertices) and $E$ is the number of edges.
  - Building the adjacency list takes $\mathcal{O}(E)$ time.
  - The DFS visits every vertex and every edge exactly once across the entire lifecycle of the loop because of the `vis` array, taking $\mathcal{O}(V + E)$ time.
  - Total Time Complexity: $\mathcal{O}(V + E)$.

- **Space Complexity:** $\mathcal{O}(V + E)$
  - The adjacency list takes $\mathcal{O}(V + E)$ space to store the graph.
  - The `vis` array takes $\mathcal{O}(V)$ space.
  - The recursion stack for DFS can go up to $\mathcal{O}(V)$ depth in the worst case (e.g., if the graph is a long straight line).
  - Total Space Complexity: $\mathcal{O}(V + E)$.
