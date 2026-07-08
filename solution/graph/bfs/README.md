## BFS - Breadth First Search

<p align="right">Last updated - 08.07.2026</p>

## Intuition

Think of **Breadth-First Search (BFS)** like a ripple in a pond. You start at a source node (the center) and explore all its immediate neighbors first (Layer 1) before moving on to the neighbors' neighbors (Layer 2).

To ensure we visit vertices level-by-level without getting stuck in infinite loops (cycles), we use:

1. A **Queue** to track the order of exploration (First In, First Out).
2. A **Visited Array** to make sure we don't push the same node into our queue multiple times.

## Approach

1. **Initialize**: Create a `visited` tracking array/list, a tracking `queue`, and a list to store the final `result`.
2. **Kickstart**: Push the starting vertex (`0`) into the queue and immediately mark it as visited.
3. **Traverse (While Queue is not empty)**:

- **Pop** the front node from the queue and append it to your `result` list.
- **Explore Neighbors**: Look at all unvisited neighbors of this popped node.
- **Push & Mark**: For each unvisited neighbor, push it into the queue and _immediately_ mark it as visited (doing this right at insertion prevents duplicate entries).

## Dry Run Example

**Graph Input**: `V = 4`, `adj = [[1, 2], [0, 2, 3], [0, 1, 3], [1, 2]]` (Start node = `0`)

| Step     | Queue (Front $\rightarrow$ Back) | Visited Array (`0` to `3`) | Popped Node | Result List    | Action / Neighbors Explored                                     |
| -------- | -------------------------------- | -------------------------- | ----------- | -------------- | --------------------------------------------------------------- |
| **Init** | `[0]`                            | `[1, 0, 0, 0]`             | —           | `[]`           | Node `0` pushed & marked visited.                               |
| **1**    | `[1, 2]`                         | `[1, 1, 1, 0]`             | `0`         | `[0]`          | Pop `0`. Neighbors `1, 2` pushed & marked.                      |
| **2**    | `[2, 3]`                         | `[1, 1, 1, 1]`             | `1`         | `[0, 1]`       | Pop `1`. Neighbor `3` pushed & marked (`0, 2` already visited). |
| **3**    | `[3]`                            | `[1, 1, 1, 1]`             | `2`         | `[0, 1, 2]`    | Pop `2`. All neighbors already visited.                         |
| **4**    | `[]`                             | `[1, 1, 1, 1]`             | `3`         | `[0, 1, 2, 3]` | Pop `3`. Queue empty $\rightarrow$ **Done!**                    |

## Implementations

```java []
// Java

class Solution {
    public ArrayList<Integer> bfs(ArrayList<ArrayList<Integer>> adj) {
        int V = adj.size();
        ArrayList<Integer> ans = new ArrayList<>();
        int[] vis = new int[V];
        Queue<Integer> que = new ArrayDeque<>();

        // 1. Initialize with the starting node
        int start = 0;
        que.offer(start);
        vis[start] = 1;

        // 2. Loop until the queue is empty
        while (!que.isEmpty()) {
            int node = que.poll();
            ans.add(node);

            // 3. Look at all adjacent neighbors
            for (int it : adj.get(node)) {
                if (vis[it] == 0) {
                    que.offer(it);
                    vis[it] = 1; // Mark immediately to prevent duplicates
                }
            }
        }
        return ans;
    }
}

```

```cpp []
// C ++

class Solution {
  public:
    vector<int> bfsOfGraph(vector<vector<int>>& adj) {
        int V = adj.size();
        vector<int> ans;
        vector<int> vis(V, 0);
        queue<int> que;

        // 1. Initialize with the starting node
        int start = 0;
        que.push(start);
        vis[start] = 1;

        // 2. Loop until the queue is empty
        while (!que.empty()) {
            int node = que.front();
            que.pop();
            ans.push_back(node);

            // 3. Look at all adjacent neighbors
            for (int it : adj[node]) {
                if (!vis[it]) {
                    que.push(it);
                    vis[it] = 1; // Mark immediately to prevent duplicates
                }
            }
        }
        return ans;
    }
};

```

## Complexity Analysis

- **Time Complexity**: $\mathcal{O}(V + E)$
    - Every vertex ($V$) is pushed and popped from the queue exactly once.
    - For every vertex, we iterate through all its out-degree neighbors. Summing this up across the entire graph looks at every edge ($E$) exactly once (or twice if undirected).

- **Space Complexity**: $\mathcal{O}(V)$
    - The `vis` array takes $\mathcal{O}(V)$ space.
    - The `queue` can hold up to $V$ vertices in the worst-case scenario (e.g., a star graph).
