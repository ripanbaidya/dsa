---
Practice Link: https://www.geeksforgeeks.org/problems/shortest-path-in-directed-acyclic-graph/1
---

# [Shortest path in Directed Acyclic Graph](https://www.geeksforgeeks.org/problems/shortest-path-in-directed-acyclic-graph/1)

<p align="right">Last updated - 23.07.2026</p>

## Approach 1: Dijkstra Algorithm (Not Optimal)

### Intuition

1. At any point, we maintain a `dist` array initialized to infinity, with `dist[src] = 0`.
2. We use a **Min-Priority Queue** (min-heap) to store pairs of `(current_distance, node)`.
3. The priority queue always gives us the unvisited node with the **smallest calculated distance**.
4. Once we pop a node with its minimum distance, we know we've found its absolute shortest path. We then try to **relax** all its outgoing edges to see if we can improve the distance to neighboring nodes.

While Topological Sort is $O(V + E)$ specifically for DAGs, Dijkstra's Algorithm is a versatile $O((V + E) \log V)$ approach that works for **any graph with non-negative edge weights** (DAG or not).

### Algorithm

1. **Graph Construction:** Build an adjacency list `adj` where `adj[u]` contains pairs `(v, weight)` representing directed edges $u \to v$.
2. **Initialize Distance & Priority Queue:**
   - Create a distance array `dist` of size $V$, filled with a large value (`INF` or `1e9`). Set `dist[0] = 0`.
   - Initialize a Min-Priority Queue and push the source tuple `(0, 0)` representing `(distance, node)`.

3. **Dijkstra Traversal:**

    - Pop the element with the smallest distance `(currDist, currNode)` from the priority queue.
    - **Lazy Deletion check:** If `currDist > dist[currNode]`, skip it because a shorter path to `currNode` was already processed.
    - Iterate through all neighbors `(neighborNode, weight)` of `currNode`.
    - If `dist[currNode] + weight < dist[neighborNode]`, update `dist[neighborNode]` and push `(dist[neighborNode], neighborNode)` into the priority queue.

4. **Post-processing:** Convert all unreachable nodes (where distance remains `INF` / `1e9`) to `-1`.

### Implementation

```java []
// Java
class Solution {
    public int[] shortestPath(int V, int E, int[][] edges) {
        // Step 1: Build adjacency list
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], weight = edge[2];
            adj.get(u).add(new int[]{v, weight});
        }

        // Step 2: Initialize distances and Min-Priority Queue
        int[] dist = new int[V];
        Arrays.fill(dist, (int) 1e9); // Using 1e9 as INF to prevent overflow
        dist[0] = 0; // Source vertex is 0

        // Min-heap storing {distance, node}
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        pq.offer(new int[]{0, 0});

        // Step 3: Standard Dijkstra Loop
        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int currDist = top[0];
            int currNode = top[1];

            // Skip outdated pairs
            if (currDist > dist[currNode]) {
                continue;
            }

            for (int[] neighbor : adj.get(currNode)) {
                int nextNode = neighbor[0];
                int weight = neighbor[1];

                // Relaxation step
                if (dist[currNode] + weight < dist[nextNode]) {
                    dist[nextNode] = dist[currNode] + weight;
                    pq.offer(new int[]{dist[nextNode], nextNode});
                }
            }
        }

        // Step 4: Mark unreachable nodes with -1
        for (int i = 0; i < V; i++) {
            if (dist[i] == (int) 1e9) {
                dist[i] = -1;
            }
        }

        return dist;
    }
}

```

```cpp
// C ++
class Solution {
public:
    vector<int> shortestPath(int V, int E, vector<vector<int>>& edges) {
        // Step 1: Build adjacency list
        vector<vector<pair<int, int>>> adj(V);
        for (int i = 0; i < E; ++i) {
            int u = edges[i][0];
            int v = edges[i][1];
            int weight = edges[i][2];
            adj[u].push_back({v, weight});
        }

        // Step 2: Initialize distances and Min-Priority Queue
        const int INF = 1e9;
        vector<int> dist(V, INF);
        dist[0] = 0; // Source vertex is 0

        // Min-heap storing pair<distance, node>
        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
        pq.push({0, 0});

        // Step 3: Standard Dijkstra Loop
        while (!pq.empty()) {
            auto [currDist, currNode] = pq.top();
            pq.pop();

            // Skip outdated pairs
            if (currDist > dist[currNode]) {
                continue;
            }

            for (const auto& neighbor : adj[currNode]) {
                int nextNode = neighbor.first;
                int weight = neighbor.second;

                // Relaxation step
                if (dist[currNode] + weight < dist[nextNode]) {
                    dist[nextNode] = dist[currNode] + weight;
                    pq.push({dist[nextNode], nextNode});
                }
            }
        }

        // Step 4: Mark unreachable nodes with -1
        for (int i = 0; i < V; ++i) {
            if (dist[i] == INF) {
                dist[i] = -1;
            }
        }

        return dist;
    }
};

```

### Complexity Analysis

- **Time Complexity**: **$O((V + E) \log V)$**, Extracting the minimum distance node from the priority queue takes $O(\log V)$ time. Each edge is relaxed at most once, inserting a new element into the priority queue of max size $V$, taking $O(E \log V)$. Total time is $O((V + E) \log V)$.

- **Space Complexity**: **$O(V + E)$**, Adjacency list requires $O(V + E)$ space. The distance array and priority queue take $O(V)$ auxiliary space.

## Approach 2: Topological Sort/ Khan's Alogrithm (Optimal) 

### Intuition

In a general weighted graph, we often use **Dijkstra's Algorithm** (which uses a min-heap / priority queue to process nodes in increasing order of distance). However, because the graph is a **DAG (Directed Acyclic Graph)**, we can do much better!

A DAG can be ordered linearly using **Topological Sort** such that for every directed edge $u \to v$, node $u$ appears before node $v$.
If we process nodes in topological order:

1. When we arrive at node $u$, we are **guaranteed** to have already computed the absolute shortest path distance to $u$. No future node can ever update $u$'s distance because there are no cycles back to $u$.
2. We can simply relax all outgoing edges from $u$ to its neighbors.

This linear ordering allows us to find all shortest paths in **$O(V + E)$ time** using simple BFS/DFS topological sorting, avoiding the $O((V + E) \log V)$ overhead of Dijkstra's PriorityQueue.

### Approach

1. **Build Adjacency List:** Construct an adjacency list where `adj[u]` stores pairs `(v, weight)` for each directed edge $u \to v$.
2. **Topological Sort:** Perform a DFS/Kahn's algorithm to find the topological ordering of the vertices. Using DFS:
   - For every unvisited node, run DFS.
   - Once all outgoing edges of a node are visited, push it onto a stack.

3. **Distance Calculation:**
   - Initialize a `dist` array of size $V$ with infinity (`INF`), and set `dist[src] = 0` (here `src = 0`).
   - Pop nodes one by one from the Topological Sort stack.
   - For each popped node $u$, if `dist[u]` is not infinity, iterate through all its adjacent edges $(u, v, wt)$ and relax:

   $$\text{dist}[v] = \min(\text{dist}[v], \text{dist}[u] + wt)$$

4. **Unreachable Nodes:** Replace any remaining `INF` values in `dist` with `-1`.

### Implementation

```java []
// Java
class Solution {
    // DFS helper for Topological Sort
    private void topoSortDFS(int node, List<List<int[]>> adj, boolean[] visited, Stack<Integer> stack) {
        visited[node] = true;
        for (int[] neighbor : adj.get(node)) {
            int nextNode = neighbor[0];
            if (!visited[nextNode]) {
                topoSortDFS(nextNode, adj, visited, stack);
            }
        }
        stack.push(node);
    }

    public int[] shortestPath(int V, int E, int[][] edges) {
        // Step 1: Build the adjacency list
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], weight = edge[2];
            adj.get(u).add(new int[]{v, weight});
        }

        // Step 2: Find Topological Order using DFS
        boolean[] visited = new boolean[V];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                topoSortDFS(i, adj, visited, stack);
            }
        }

        // Step 3: Initialize distances and process in Topological Order
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[0] = 0; // Source vertex is 0

        while (!stack.isEmpty()) {
            int u = stack.pop();

            // Relax edges only if the current node is reachable
            if (dist[u] != Integer.MAX_VALUE) {
                for (int[] neighbor : adj.get(u)) {
                    int v = neighbor[0];
                    int weight = neighbor[1];

                    if (dist[u] + weight < dist[v]) {
                        dist[v] = dist[u] + weight;
                    }
                }
            }
        }

        // Step 4: Mark unreachable nodes with -1
        for (int i = 0; i < V; i++) {
            if (dist[i] == Integer.MAX_VALUE) {
                dist[i] = -1;
            }
        }

        return dist;
    }
}

```

```cpp []
// C ++
class Solution {
private:
    // DFS helper for Topological Sort
    void topoSortDFS(int node, const vector<vector<pair<int, int>>>& adj, vector<bool>& visited, stack<int>& st) {
        visited[node] = true;
        for (const auto& edge : adj[node]) {
            int nextNode = edge.first;
            if (!visited[nextNode]) {
                topoSortDFS(nextNode, adj, visited, st);
            }
        }
        st.push(node);
    }

public:
    vector<int> shortestPath(int V, int E, vector<vector<int>>& edges) {
        // Step 1: Build the adjacency list
        vector<vector<pair<int, int>>> adj(V);
        for (int i = 0; i < E; ++i) {
            int u = edges[i][0];
            int v = edges[i][1];
            int weight = edges[i][2];
            adj[u].push_back({v, weight});
        }

        // Step 2: Find Topological Order using DFS
        vector<bool> visited(V, false);
        stack<int> st;
        for (int i = 0; i < V; ++i) {
            if (!visited[i]) {
                topoSortDFS(i, adj, visited, st);
            }
        }

        // Step 3: Initialize distances and process in Topological Order
        vector<int> dist(V, INT_MAX);
        dist[0] = 0; // Source vertex is 0

        while (!st.empty()) {
            int u = st.top();
            st.pop();

            // Relax edges only if current node is reachable
            if (dist[u] != INT_MAX) {
                for (const auto& edge : adj[u]) {
                    int v = edge.first;
                    int weight = edge.second;

                    if (dist[u] + weight < dist[v]) {
                        dist[v] = dist[u] + weight;
                    }
                }
            }
        }

        // Step 4: Mark unreachable nodes with -1
        for (int i = 0; i < V; ++i) {
            if (dist[i] == INT_MAX) {
                dist[i] = -1;
            }
        }

        return dist;
    }
};

```

### Complexity Analysis

- **Time Complexity**: **$O(V + E)$**, Building the adjacency list takes $O(E)$. Topological Sort via DFS visits every node and edge once, taking $O(V + E)$. Distance relaxation processes each node and outgoing edge once in topological order, taking $O(V + E)$.

- **Space Complexity**: **$O(V + E)$**, The adjacency list requires $O(V + E)$ space. The `visited` array, recursion stack, stack data structure, and distance array require $O(V)$ auxiliary space.
