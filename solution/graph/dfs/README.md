## DFS - Depth First Search

## Intuition

Think of **Depth-First Search (DFS)** like exploring a maze. You pick a path and keep walking forward as deep as possible until you hit a dead end. When you can't go any further, you **backtrack** to the last intersection and try the next available path.

To achieve this "go deep first" behavior, DFS uses:

1. **Recursion (or a Call Stack)**: To remember the path we came from so we can backtrack safely.
2. **A Visited Array**: To make sure we don't visit the same node twice and get trapped in an infinite cycle.

## Approach

1. **Initialize**: Create a `visited` array and a `result` list.
2. **The Recursive Helper**: Create a function that takes the current `node`, the graph, and our tracking structures.
3. **Visit and Record**: The moment you step into a node:

- Mark it as visited (`vis[node] = 1`).
- Add it to your `result` list.

4. **Explore Deeply**: Look at all neighbors of the current node. If a neighbor hasn't been visited yet, _immediately_ jump into it by making a recursive call.

## Dry Run

**Graph Input**: `V = 4`, `adj = [[1, 2], [0, 2, 3], [0, 1, 3], [1, 2]]` (Start node = `0`)

```
   0 --- 1
   |   / |
   |  /  |
   2 --- 3

```

| Step  | Call Stack (Current Node)                               | Visited Array (`0` to `3`) | Result List    | Action / Path Chosen                                                                                   |
| ----- | ------------------------------------------------------- | -------------------------- | -------------- | ------------------------------------------------------------------------------------------------------ |
| **1** | `helper(0)`                                             | `[1, 0, 0, 0]`             | `[0]`          | Visit `0`. First unvisited neighbor is `1`. Call `helper(1)`.                                          |
| **2** | `helper(1)` $\rightarrow$ `helper(0)`                   | `[1, 1, 0, 0]`             | `[0, 1]`       | Visit `1`. Neighbors are `0` (vis), `2` (unvis). Call `helper(2)`.                                     |
| **3** | `helper(2)` $\rightarrow$ `helper(1)` $\rightarrow$ ... | `[1, 1, 1, 0]`             | `[0, 1, 2]`    | Visit `2`. Neighbors are `0` (vis), `1` (vis), `3` (unvis). Call `helper(3)`.                          |
| **4** | `helper(3)` $\rightarrow$ `helper(2)` $\rightarrow$ ... | `[1, 1, 1, 1]`             | `[0, 1, 2, 3]` | Visit `3`. Neighbors `1` and `2` are already visited. **Dead end!**                                    |
| **5** | Backtracking phase...                                   | `[1, 1, 1, 1]`             | `[0, 1, 2, 3]` | `helper(3)` pops off. `helper(2)` has no more neighbors, pops off. All pop off $\rightarrow$ **Done!** |

## Implementations

Your Java solution was already perfectly correct! Here it is along with the C++ equivalent.

### Java

```java []
class Solution {
    public ArrayList<Integer> dfs(ArrayList<ArrayList<Integer>> adj) {
        int V = adj.size();
        int start = 0;

        ArrayList<Integer> ans = new ArrayList<>();
        int[] vis = new int[V];

        // Start the recursive traversal
        helper(start, adj, ans, vis);
        return ans;
    }

    private void helper(int node, ArrayList<ArrayList<Integer>> adj, ArrayList<Integer> ans, int[] vis) {
        // 1. Mark the current node as visited and add to answers
        vis[node] = 1;
        ans.add(node);

        // 2. Recursively visit all unvisited neighbors (going deep)
        for (int it : adj.get(node)) {
            if (vis[it] == 0) {
                helper(it, adj, ans, vis);
            }
        }
    }
}

```

### C++

```cpp []
class Solution {
  private:
    void helper(int node, vector<vector<int>>& adj, vector<int>& ans, vector<int>& vis) {
        // 1. Mark the current node as visited and add to answers
        vis[node] = 1;
        ans.push_back(node);

        // 2. Recursively visit all unvisited neighbors (going deep)
        for (int it : adj[node]) {
            if (!vis[it]) {
                helper(it, adj, ans, vis);
            }
        }
    }

  public:
    vector<int> dfsOfGraph(vector<vector<int>>& adj) {
        int V = adj.size();
        int start = 0;

        vector<int> ans;
        vector<int> vis(V, 0);

        // Start the recursive traversal
        helper(start, adj, ans, vis);
        return ans;
    }
};

```

## Complexity Analysis

- **Time Complexity**: $\mathcal{O}(V + E)$
    - **Vertices ($V$)**: The `helper` function is called exactly once for each node because of the `vis` check.
    - **Edges ($E$)**: Inside the function, the `for` loop looks at the neighbors. Across the entire execution, we traverse every edge exactly once (or twice for undirected graphs).

- **Space Complexity**: $\mathcal{O}(V)$
    - The `vis` array takes $\mathcal{O}(V)$ space.
    - The **recursion call stack** takes $\mathcal{O}(V)$ space in the worst-case scenario (e.g., if the graph is a straight linear chain, the stack depth will equal the number of vertices).
