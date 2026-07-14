# [802. Find Eventual Safe States](https://leetcode.com/problems/find-eventual-safe-states/description/)

<p align="right">Last updated - 14.07.2026</p>

##

### The Core Concept: Why Reverse the Graph?

A node is **safe** if every possible path starting from it leads to a **terminal node** (a node with no outgoing edges).

If a node is part of a cycle, or can reach a cycle, it is **not safe** because a path starting from it could loop infinitely.

If we try to use standard Kahn's Algorithm (BFS) directly:

- Kahn's algorithm works by processing nodes with an **InDegree of 0** (nodes with no incoming edges).
- However, "safety" is defined by **outgoing edges** (OutDegree). Terminal nodes have an OutDegree of 0.
- To make Kahn's algorithm process terminal nodes first, we **reverse all the edges** of the graph.
  - After reversing, any node with an OutDegree of 0 in the original graph will have an **InDegree of 0** in the reversed graph.
  - As we run BFS on the reversed graph, any node whose InDegree drops to 0 is guaranteed to only have paths leading to already confirmed safe nodes.
  - Nodes that are part of a cycle (or lead to one) will never have their InDegree drop to 0 and will be left out.

### Algorithm

1. Create a reversed adjacency list `adj` where an edge $u \rightarrow v$ in the original graph becomes $v \rightarrow u$.
2. Compute the `inDegree` array for this reversed graph. Note that the InDegree of node $u$ in the reversed graph is simply the OutDegree of node $u$ in the original graph (`graph[u].length`).
3. Initialize a queue and push all nodes with `inDegree == 0` (the original terminal nodes).
4. Initialize a boolean array `isSafe` of size $n$ to track safe nodes.
5. While the queue is not empty:
   - Poll a node. Mark it as safe (`isSafe[node] = true`).
   - For each of its neighbors in the reversed graph (which are the nodes that pointed to it in the original graph), decrement their InDegree by 1.
   - If a neighbor's InDegree drops to 0, push it into the queue.

6. Iterate from $0$ to $n-1$ and collect all nodes where `isSafe[node]` is true. Returning them in this order avoids the need for sorting.

### Implementation

**Java**

```java
import java.util.*;

class Solution {
    public List<Integer> eventualSafeNodes(int[][] graph) {
        int n = graph.length;

        // Step 1: Build the reversed adjacency list and compute InDegrees
        List<List<Integer>> revAdj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            revAdj.add(new ArrayList<>());
        }

        int[] inDegree = new int[n];
        for (int u = 0; u < n; u++) {
            for (int v : graph[u]) {
                // Original: u -> v | Reversed: v -> u
                revAdj.get(v).add(u);
            }
            // InDegree in reversed graph is the OutDegree in original graph
            inDegree[u] = graph[u].length;
        }

        // Step 2: Push all terminal nodes (InDegree = 0 in reversed graph)
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        // Step 3: Process the queue
        boolean[] isSafe = new boolean[n];
        while (!queue.isEmpty()) {
            int node = queue.poll();
            isSafe[node] = true;

            for (int neighbor : revAdj.get(node)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Step 4: Collect safe nodes in sorted order naturally
        List<Integer> safeNodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (isSafe[i]) {
                safeNodes.add(i);
            }
        }

        return safeNodes;
    }
}

```

**C++**

```cpp
#include <vector>
#include <queue>
#include <algorithm>

using namespace std;

class Solution {
public:
    vector<int> eventualSafeNodes(vector<vector<int>>& graph) {
        int n = graph.size();

        // Step 1: Build the reversed adjacency list and compute InDegrees
        vector<vector<int>> revAdj(n);
        vector<int> inDegree(n, 0);

        for (int u = 0; u < n; u++) {
            for (int v : graph[u]) {
                // Original: u -> v | Reversed: v -> u
                revAdj[v].push_back(u);
            }
            // InDegree in reversed graph is the OutDegree in original graph
            inDegree[u] = graph[u].size();
        }

        // Step 2: Push all terminal nodes (InDegree = 0 in reversed graph)
        queue<int> que;
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                que.push(i);
            }
        }

        // Step 3: Process the queue
        vector<bool> isSafe(n, false);
        while (!que.empty()) {
            int node = que.front();
            que.pop();
            isSafe[node] = true;

            for (int neighbor : revAdj[node]) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    que.push(neighbor);
                }
            }
        }

        // Step 4: Collect safe nodes in sorted order naturally
        vector<int> safeNodes;
        for (int i = 0; i < n; i++) {
            if (isSafe[i]) {
                safeNodes.push_back(i);
            }
        }

        return safeNodes;
    }
};

```

### Dry Run

Let's use an example where a cycle exists:

- `graph = [[1, 2], [2, 3], [5], [0], [5], [], []]` ($n = 7$)
  - Nodes `5` and `6` are terminal nodes (empty lists).
  - Nodes `0, 1, 3` form a cycle: `0 -> 1 -> 2 -> 3 -> 0`.

#### 1. Reverse Graph Setup

- **`revAdj`:**
  - `0` $\rightarrow$ `[3]`
  - `1` $\rightarrow$ `[0]`
  - `2` $\rightarrow$ `[0, 1]`
  - `3` $\rightarrow$ `[1]`
  - `4` $\rightarrow$ `[]`
  - `5` $\rightarrow$ `[2, 4]`
  - `6` $\rightarrow$ `[]`

- **`inDegree` (Original OutDegree):** `[0: 2, 1: 2, 2: 1, 3: 1, 4: 1, 5: 0, 6: 0]`

#### 2. Processing

- **Queue Initialization:** Nodes with `inDegree == 0` are `[5, 6]`.
- **Iteration 1:** Poll `5`. `isSafe[5] = true`.
  - Neighbors of `5` in `revAdj` are `2` and `4`.
  - Decrement `inDegree[2]` to 0 $\rightarrow$ push `2`.
  - Decrement `inDegree[4]` to 0 $\rightarrow$ push `4`.
  - Queue is now `[6, 2, 4]`.

- **Iteration 2:** Poll `6`. `isSafe[6] = true`.
  - Neighbors of `6` is empty. No updates.
  - Queue is now `[2, 4]`.

- **Iteration 3:** Poll `2`. `isSafe[2] = true`.
  - Neighbors of `2` are `0` and `1`.
  - Decrement `inDegree[0]` to 1.
  - Decrement `inDegree[1]` to 1.
  - Queue is now `[4]`.

- **Iteration 4:** Poll `4`. `isSafe[4] = true`.
  - No neighbors in `revAdj`.
  - Queue is empty.

**Remaining InDegrees:** `[0: 1, 1: 1, 3: 1]`. They never reached `0` because they form a cycle dependency.

**Safe Nodes:** `[2, 4, 5, 6]`.

## Complexity Analysis

- **Time Complexity:** $\mathcal{O}(V + E)$ where $V$ is the number of nodes ($n$) and $E$ is the total number of edges in `graph`. Building the reversed graph takes $\mathcal{O}(V + E)$, and the BFS processes each node and reversed edge exactly once. Collecting the result at the end takes $\mathcal{O}(V)$ (no sorting needed since we iterate from $0$ to $n-1$).
- **Space Complexity:** $\mathcal{O}(V + E)$ to store the reversed adjacency list `revAdj` representation, along with $\mathcal{O}(V)$ helper space for the `inDegree` array, the safety tracking array, and the BFS queue.
