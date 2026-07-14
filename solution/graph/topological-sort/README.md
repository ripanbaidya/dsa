# [Topological Sort](https://www.geeksforgeeks.org/problems/topological-sort/1)

<p align="right">Last updated - 14.07.2026</p>

### Understanding Topological Sort

Topological sorting for a **Directed Acyclic Graph (DAG)** is a linear ordering of vertices such that for every directed edge $u \rightarrow v$, vertex $u$ comes before $v$ in the ordering.

_Note: Topological sorting is only possible if the graph is a DAG. If there is a cycle, a valid topological sort cannot exist._

## Approach 1: DFS with Stack

### Intuition

In a Depth-First Search (DFS), we visit a node and recursively explore all its unvisited neighbors. When a node has no more unvisited neighbors left, it means all nodes that depend on this node (or are reachable from it) have already been fully explored.

Since these dependent nodes should appear _after_ the current node in the topological sort, the current node is safe to be placed before them. By pushing a node onto a Stack **only after** all its neighbors are fully processed (post-order processing), we ensure that the dependencies are resolved in reverse. Popping everything from the stack at the end reverses the order back to the correct topological sequence.

### Algorithm

1. Create a `visited` boolean array of size $V$ initialized to false, and an empty stack `stk`.
2. Loop through all vertices from $0$ to $V-1$. If a vertex is not visited, call the helper function `dfs(node)`.
3. In the `dfs(node)` function:
   - Mark the current `node` as visited (`visited[node] = true`).
   - Iterate through all adjacent neighbors of the current `node`. If a neighbor is not visited, recursively call `dfs(neighbor)`.
   - After exploring all neighbors, push the current `node` onto the stack `stk`.

4. After processing all nodes, pop elements from the stack one by one and append them to the topological sort result list.

### Implementation

**Java**

```java
import java.util.*;

class Solution {
    // Function to return list containing vertices in Topological order.
    public ArrayList<Integer> topoSort(int V, int[][] edges) {
        // Step 1: Build the Adjacency List
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
        }

        boolean[] vis = new boolean[V];
        Stack<Integer> stk = new Stack<>();

        // Step 2: Invoke DFS for all unvisited components
        for (int i = 0; i < V; i++) {
            if (!vis[i]) {
                dfs(i, adj, stk, vis);
            }
        }

        // Step 3: Extract elements from stack to get topological order
        ArrayList<Integer> topo = new ArrayList<>();
        while (!stk.isEmpty()) {
            topo.add(stk.pop());
        }

        return topo;
    }

    private void dfs(int node, List<List<Integer>> adj, Stack<Integer> stk, boolean[] vis) {
        // Mark the current node as visited
        vis[node] = true;

        // Recur for all the vertices adjacent to this vertex
        for (int nei : adj.get(node)) {
            if (!vis[nei]) {
                dfs(nei, adj, stk, vis);
            }
        }

        // Push current vertex to stack which stores the result
        stk.push(node);
    }
}

```

**C++**

```cpp
#include <bits/stdc++.h>

using namespace std;

class Solution {
private:
    void dfs(int node, const vector<vector<int>>& adj, stack<int>& stk, vector<bool>& vis) {
        // Mark the current node as visited
        vis[node] = true;

        // Recur for all the vertices adjacent to this vertex
        for (int nei : adj[node]) {
            if (!vis[nei]) {
                dfs(nei, adj, stk, vis);
            }
        }

        // Push current vertex to stack after all its neighbors are processed
        stk.push(node);
    }

public:
    // Function to return list containing vertices in Topological order.
    vector<int> topoSort(int V, vector<vector<int>>& edges) {
        // Step 1: Build the Adjacency List
        vector<vector<int>> adj(V);
        for (auto& edge : edges) {
            adj[edge[0]].push_back(edge[1]);
        }

        vector<bool> vis(V, false);
        stack<int> stk;

        // Step 2: Invoke DFS for all unvisited components
        for (int i = 0; i < V; i++) {
            if (!vis[i]) {
                dfs(i, adj, stk, vis);
            }
        }

        // Step 3: Extract elements from stack to get topological order
        vector<int> topo;
        while (!stk.empty()) {
            topo.push_back(stk.top());
            stk.pop();
        }

        return topo;
    }
};

```

### Dry Run

Consider a graph with $V = 4$ and `edges = [[1, 0], [2, 0], [3, 1], [3, 2]]`.

- **Adjacency List:**
  - `0` -> `[]`
  - `1` -> `[0]`
  - `2` -> `[0]`
  - `3` -> `[1, 2]`

**Tracing Loop (`i` from 0 to 3):**

1. **`i = 0`**: `vis[0]` is false $\rightarrow$ call `dfs(0)`.
   - `vis[0] = true`. Neighbors of `0`: none.
   - Push `0` to Stack. Stack: `[0]`

2. **`i = 1`**: `vis[1]` is false $\rightarrow$ call `dfs(1)`.
   - `vis[1] = true`. Neighbors of `1`: `0`.
   - `0` is already visited.
   - Push `1` to Stack. Stack: `[0, 1]`

3. **`i = 2`**: `vis[2]` is false $\rightarrow$ call `dfs(2)`.
   - `vis[2] = true`. Neighbors of `2`: `0`.
   - `0` is already visited.
   - Push `2` to Stack. Stack: `[0, 1, 2]`

4. **`i = 3`**: `vis[3]` is false $\rightarrow$ call `dfs(3)`.
   - `vis[3] = true`. Neighbors of `3`: `1`, `2`.
   - Both `1` and `2` are already visited.
   - Push `3` to Stack. Stack: `[0, 1, 2, 3]`

**Popping Stack into `topo`:** `[3, 2, 1, 0]` _(A valid topological sort)_

### Complexity Analysis

- **Time Complexity:** $\mathcal{O}(V + E)$ — We visit each vertex exactly once and traverse all its outgoing edges across the DFS calls.
- **Space Complexity:** $\mathcal{O}(V)$ — $\mathcal{O}(V)$ for the recursion call stack, $\mathcal{O}(V)$ for the tracking stack, and $\mathcal{O}(V)$ for the visited array.

## Approach 2: BFS with InDegree Array (Kahn's Algorithm)

### Intuition

If a node has an InDegree of 0, it means there are no dependencies or prerequisites required to reach or process this node. We can start our topological sorting sequence with these nodes safely.

When we remove a node with an InDegree of 0 from the graph and add it to our topological list, it no longer acts as a prerequisite for its neighbors. Therefore, we can decrement the InDegrees of all its outgoing neighbors. If any neighbor's InDegree drops to 0 because of this, it means all its dependencies have been satisfied, and it is ready to be put into the processing queue.

### Algorithm

1. Compute the InDegree (number of incoming edges) for each vertex.
2. Initialize an empty queue `que` and insert all vertices with `inDegree == 0`.
3. Initialize an empty list `topo` to hold the final sorted order.
4. While the `que` is not empty:
   - Extract the front element `top` from the queue and add it to `topo`.
   - Iterate through all the adjacent neighbors of `top`. For each neighbor, decrement its InDegree by 1.
   - If a neighbor's InDegree becomes 0, push it into the `que`.

5. Return `topo`.

### Implementation

**Java**

```java
import java.util.*;

class Solution {
    public ArrayList<Integer> topoSort(int V, int[][] edges) {
        // Step 1: Convert 2D edge list into an adjacency list and compute InDegrees
        List<List<Integer>> adj = new ArrayList<>();
        int[] inDegree = new int[V];

        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            inDegree[edge[1]]++; // Increment inward degree for destination node
        }

        // Step 2: Push all nodes with InDegree 0 into the queue
        Queue<Integer> que = new ArrayDeque<>();
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                que.offer(i);
            }
        }

        // Step 3: Process the queue
        ArrayList<Integer> topo = new ArrayList<>();
        while (!que.isEmpty()) {
            int current = que.poll();
            topo.add(current);

            // Reduce InDegree for all neighbors
            for (int nei : adj.get(current)) {
                inDegree[nei]--;
                // If InDegree becomes 0, add it to the queue
                if (inDegree[nei] == 0) {
                    que.offer(nei);
                }
            }
        }

        return topo;
    }
}

```

**C++**

```cpp
#include <bits/stdc++.h>

using namespace std;

class Solution {
public:
    // Function to return list containing vertices in Topological order.
    vector<int> topoSort(int V, vector<vector<int>>& edges) {
        // Step 1: Convert 2D edge list into an adjacency list and compute InDegrees
        vector<vector<int>> adj(V);
        vector<int> inDegree(V, 0);

        for (auto& edge : edges) {
            adj[edge[0]].push_back(edge[1]);
            inDegree[edge[1]]++; // Increment inward degree for destination node
        }

        // Step 2: Push all nodes with InDegree 0 into the queue
        queue<int> que;
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                que.push(i);
            }
        }

        // Step 3: Process the queue
        vector<int> topo;
        while (!que.empty()) {
            int current = que.front();
            que.pop();
            topo.push_back(current);

            // Reduce InDegree for all neighbors
            for (int nei : adj[current]) {
                inDegree[nei]--;
                // If InDegree becomes 0, add it to the queue
                if (inDegree[nei] == 0) {
                    que.push(nei);
                }
            }
        }

        return topo;
    }
};

```

### Dry Run

Using the same graph: $V = 4$, `edges = [[1, 0], [2, 0], [3, 1], [3, 2]]`

- **InDegree Array Initially:** `[0: 2, 1: 1, 2: 1, 3: 0]` (Node 0 has 2 incoming edges from 1 and 2, Node 3 has 0 incoming edges).

**Tracing Kahn's execution:**

1. **Queue Setup:** Initial nodes with `inDegree == 0` is `[3]`. `que = [3]`.
2. **Iteration 1:**
   - Pop `3` from queue. `topo = [3]`.
   - Neighbors of `3` are `1` and `2`.
   - Decrement InDegree of `1` $\rightarrow$ `inDegree[1] = 0`. Since it's 0, push `1` to queue.
   - Decrement InDegree of `2` $\rightarrow$ `inDegree[2] = 0`. Since it's 0, push `2` to queue.
   - `que = [1, 2]`.

3. **Iteration 2:**
   - Pop `1` from queue. `topo = [3, 1]`.
   - Neighbor of `1` is `0`.
   - Decrement InDegree of `0` $\rightarrow$ `inDegree[0] = 1`. (Not 0 yet).
   - `que = [2]`.

4. **Iteration 3:**
   - Pop `2` from queue. `topo = [3, 1, 2]`.
   - Neighbor of `2` is `0`.
   - Decrement InDegree of `0` $\rightarrow$ `inDegree[0] = 0`. Since it's 0, push `0` to queue.
   - `que = [0]`.

5. **Iteration 4:**
   - Pop `0` from queue. `topo = [3, 1, 2, 0]`.
   - Node `0` has no neighbors. Queue becomes empty.

   **Final output:** `[3, 1, 2, 0]` _(Another perfectly valid topological sort)_

### Complexity Analysis

- **Time Complexity:** $\mathcal{O}(V + E)$ — Every vertex is queued and dequeued at most once ($\mathcal{O}(V)$), and for each vertex, we iterate over its outgoing edges ($\mathcal{O}(E)$).
- **Space Complexity:** $\mathcal{O}(V)$ — Used by the `inDegree` array ($\mathcal{O}(V)$) and the `que` structure ($\mathcal{O}(V)$).

## References

- DFS - https://youtu.be/5lZ0iJMrUMk?si=TV3F8fHLQsDhfDov
- BFS/Khan's Algorithm - https://youtu.be/73sneFXuTEg?si=oo8K8YicNnsz2MPI