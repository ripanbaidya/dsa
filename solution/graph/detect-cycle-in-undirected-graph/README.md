# Undirected Graph Cycle

<p align="right">Last updated - 08.07.2026</p>

- https://www.geeksforgeeks.org/problems/detect-cycle-in-an-undirected-graph/1

## Why Do We Track the "Parent" Node?

Before diving into the algorithms, let's address your crucial question: **Why do we need a parent tracker?**

In an undirected graph, an edge between node $A$ and node $B$ goes both ways ($A \leftrightarrow B$). When you travel from $A$ to $B$, $B$ will naturally look at its neighbors and see $A$.

- **The Rule of a Cycle:** A cycle exists only if we encounter a node that has _already been visited_, and that node is **not** the immediate parent (the node we just came from).
- **What if we don't track the parent?** The algorithm will look back at the node it just left, see that it’s already visited, and falsely declare that a cycle exists.

> 💡 **Example:** Imagine a simple graph with just two nodes: `0 — 1`.
> You start at `0` (visit it), then move to `1`. From `1`, you look at its neighbor `0`. If you don't check `if (neighbor == parent)`, your code will see that `0` is already visited and say, _"Aha! A cycle!"_—which is completely wrong. Tracking the parent prevents this false positive.

## Approach 1: Breadth-First Search (BFS)

### Intuition

BFS explores the graph layer by layer using a **Queue**. We store pairs in our queue: `{current_node, parent_node}`.

As we traverse, we visit neighbors. If a neighbor is already visited **and** it is not the parent of the current node, it means another path has already reached this node. This confirms a cycle exists.

### Implementations

**Java**

```java
import java.util.*;

class Solution {
    public boolean isCycle(int V, List<List<Integer>> adj) {
        boolean[] vis = new boolean[V];

        // Handle disconnected components
        for (int i = 0; i < V; i++) {
            if (!vis[i]) {
                if (checkForCycleBFS(i, adj, vis)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkForCycleBFS(int src, List<List<Integer>> adj, boolean[] vis) {
        // Queue stores {current_node, parent_node}
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{src, -1});
        vis[src] = true;

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int node = cell[0];
            int parent = cell[1];

            for (int neighbor : adj.get(node)) {
                if (!vis[neighbor]) {
                    vis[neighbor] = true;
                    queue.add(new int[]{neighbor, node});
                }
                // If visited and not the parent, a cycle is found
                else if (neighbor != parent) {
                    return true;
                }
            }
        }
        return false;
    }
}

```

**C ++**

```cpp
#include <bits/stdc++.h>
using namespace std;

class Solution {
private:
    bool checkForCycleBFS(int src, vector<vector<int>>& adj, vector<bool>& vis) {
        // Queue stores {current_node, parent_node}
        queue<pair<int, int>> q;
        q.push({src, -1});
        vis[src] = true;

        while(!q.empty()) {
            int node = q.front().first;
            int parent = q.front().second;
            q.pop();

            for(auto neighbor : adj[node]) {
                if(!vis[neighbor]) {
                    vis[neighbor] = true;
                    q.push({neighbor, node});
                }
                // If visited and not the parent, a cycle is found
                else if(neighbor != parent) {
                    return true;
                }
            }
        }
        return false;
    }

public:
    bool isCycle(int V, vector<vector<int>>& adj) {
        vector<bool> vis(V, false);
        // Handle disconnected components
        for(int i = 0; i < V; i++) {
            if(!vis[i]) {
                if(checkForCycleBFS(i, adj, vis)) return true;
            }
        }
        return false;
    }
};

```

## Approach 2: Depth-First Search (DFS)

### Intuition

DFS goes as deep as possible along each branch before backtracking. We can implement this beautifully using recursion.

We pass the `current_node` and its `parent` into the recursive function. If any neighbor of the current node is already visited and is _not_ its parent, the recursion stack immediately unwinds and flags `true` (cycle detected).

### Implementations

**Java**

```java
import java.util.*;

class Solution {
    public boolean isCycle(int V, List<List<Integer>> adj) {
        boolean[] vis = new boolean[V];

        // Handle disconnected components
        for (int i = 0; i < V; i++) {
            if (!vis[i]) {
                if (checkForCycleDFS(i, -1, adj, vis)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkForCycleDFS(int node, int parent, List<List<Integer>> adj, boolean[] vis) {
        vis[node] = true;

        for (int neighbor : adj.get(node)) {
            if (!vis[neighbor]) {
                if (checkForCycleDFS(neighbor, node, adj, vis)) {
                    return true;
                }
            }
            // If visited and not the parent, a cycle is found
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
#include <bits/stdc++.h>
using namespace std;

class Solution {
private:
    bool checkForCycleDFS(int node, int parent, vector<vector<int>>& adj, vector<bool>& vis) {
        vis[node] = true;

        for(auto neighbor : adj[node]) {
            if(!vis[neighbor]) {
                if(checkForCycleDFS(neighbor, node, adj, vis)) return true;
            }
            // If visited and not the parent, a cycle is found
            else if(neighbor != parent) {
                return true;
            }
        }
        return false;
    }

public:
    bool isCycle(int V, vector<vector<int>>& adj) {
        vector<bool> vis(V, false);
        // Handle disconnected components
        for(int i = 0; i < V; i++) {
            if(!vis[i]) {
                if(checkForCycleDFS(i, -1, adj, vis)) return true;
            }
        }
        return false;
    }
};

```

## Complexity Analysis

Both BFS and DFS implementations share identical time and space complexity characteristics because they explore the exact same graph structures.

| Complexity Type      | Big-O Notation | Explanation                                                                                                                                                                                                               |
| -------------------- | -------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Time Complexity**  | $O(V + 2E)$    | We visit every vertex ($V$) exactly once. For every vertex, we iterate through all its adjacent edges. In an undirected graph, every edge ($E$) is traversed twice (once from each endpoint), leading to $2E$ operations. |
| **Space Complexity** | $O(V)$         | **Visited Array:** Uses $O(V)$ space.                                                                                                                                                                                     |

**BFS Queue / DFS Stack:** In the worst-case scenario (like a straight-line graph or a star graph), the queue or the recursive call stack can hold up to $O(V)$ nodes.
