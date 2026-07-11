# Connected Components in an Undirected Graph

<p align="right">Last updated - 11.07.2026</p>

- https://www.geeksforgeeks.org/problems/connected-components-in-an-undirected-graph/1

Here is a comprehensive breakdown of the Depth First Search (DFS) approach used in your solution to find all connected components in an undirected graph.

## Approach 1: DFS

### Intuition

An undirected graph can be divided into independent subgraphs called **connected components**. Within each component, every vertex is reachable from any other vertex via a path.

The approach utilizes **Depth First Search (DFS)** to traverse the graph. Imagine waking up in a maze (the graph) with multiple disconnected sections. If you start at an unvisited room (vertex) and explore every reachable path completely before returning, you will have mapped out one entire isolated section of the maze. By repeating this process for every room you haven't visited yet, you can discover all the separate sections.

1. We use a tracking mechanism (a `visited` array) to ensure we don't count the same vertex twice or get stuck in infinite loops.
2. We loop through all vertices from $0$ to $V-1$. If a vertex has not been visited, it means it belongs to a new connected component.
3. We launch a DFS from that vertex to discover and collect all other vertices belonging to the same component, marking them as visited along the way.

### Algorithm

1. **Build Adjacency List:** Convert the input edge list into an adjacency list representation for efficient neighbor lookups.
2. **Initialize Tracking:** Create a boolean array `vis` of size `V` initialized to `false` and a list `ans` to store all components.
3. **Iterate Through Vertices:** Loop through each vertex $i$ from $0$ to $V-1$:
   - If `vis[i]` is `false`:
     - Create a new temporary list `list` to store the current component's vertices.
     - Call the `dfs` function starting from vertex $i$.
     - After the `dfs` traversal completes, add `list` to `ans`.

4. **DFS Function:**
   - Mark the current `node` as visited (`vis[node] = true`).
   - Add the `node` to the current component's `list`.
   - For each neighbor `nei` of the current `node`:
     - If `vis[nei]` is `false`, recursively call `dfs` for `nei`.

5. **Return Result:** Return `ans` containing all connected components.

### Implementation

```java
class Solution {
    public ArrayList<ArrayList<Integer>> getComponents(int n, int[][] edges) {
        // Step 1: Build the adjacency list for the graph
        ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]); // Because the graph is undirected
        }

        ArrayList<ArrayList<Integer>> ans = new ArrayList<>();
        boolean[] vis = new boolean[n]; // Tracks visited vertices

        // Step 2: Iterate through all vertices to find unvisited components
        for (int i = 0; i < n; i++) {
            if (!vis[i]) {
                ArrayList<Integer> list = new ArrayList<>();
                // Launch DFS to explore the entire connected component
                dfs(i, adj, vis, list);
                ans.add(list); // Store the discovered component
            }
        }
        return ans;
    }

    // Helper method to perform Depth First Search
    private void dfs(int node, ArrayList<ArrayList<Integer>> adj, boolean[] vis, ArrayList<Integer> list) {
        vis[node] = true; // Mark current node as visited
        list.add(node);   // Include it in the current component list

        // Recursively visit all unvisited neighbors
        for (int nei : adj.get(node)) {
            if (!vis[nei]) {
                dfs(nei, adj, vis, list);
            }
        }
    }
}

```

```cpp
class Solution {
private:
    // Helper function to perform Depth First Search
    void dfs(int node, const vector<vector<int>>& adj, vector<bool>& vis, vector<int>& component) {
        vis[node] = true;             // Mark current node as visited
        component.push_back(node);    // Include it in the current component list

        // Recursively visit all unvisited neighbors
        for (int nei : adj[node]) {
            if (!vis[nei]) {
                dfs(nei, adj, vis, component);
            }
        }
    }

public:
    vector<vector<int>> getComponents(int n, vector<vector<int>>& edges) {
        // Step 1: Build the adjacency list for the graph
        vector<vector<int>> adj(n);
        for (const auto& edge : edges) {
            adj[edge[0]].push_back(edge[1]);
            adj[edge[1]].push_back(edge[0]); // Because the graph is undirected
        }

        vector<vector<int>> ans;
        vector<bool> vis(n, false); // Tracks visited vertices

        // Step 2: Iterate through all vertices to find unvisited components
        for (int i = 0; i < n; i++) {
            if (!vis[i]) {
                vector<int> component;
                // Launch DFS to explore the entire connected component
                dfs(i, adj, vis, component);
                ans.push_back(component); // Store the discovered component
            }
        }
        return ans;
    }
};

```

### Complexity Analysis

- Time Complexity: $\mathcal{O}(V + E)$
  - **Graph Construction:** Building the adjacency list takes $\mathcal{O}(V + E)$ time as we iterate through all $E$ edges and initialize $V$ lists.
  - **DFS Traversal:** Each vertex is visited exactly once because of the `vis` tracking array ($\mathcal{O}(V)$). For every vertex, we traverse its neighbors. Across the entire execution, every edge is looked at exactly twice (once from each endpoint), taking $\mathcal{O}(E)$ time.
  - Total Time Complexity balances out to **$\mathcal{O}(V + E)$**, which is optimal for graph traversals.

- Space Complexity: $\mathcal{O}(V + E)$
  - **Adjacency List:** Storing the graph requires $\mathcal{O}(V + 2E)$ space.
  - **Visited Array / List:** The `vis` array takes $\mathcal{O}(V)$ space.
  - **Call Stack:** In the worst-case scenario (a graph structured like a single straight line), the recursion stack can go as deep as $V$ frames, consuming $\mathcal{O}(V)$ space.
  - Total Auxiliary Space Complexity stands at **$\mathcal{O}(V + E)$**.
