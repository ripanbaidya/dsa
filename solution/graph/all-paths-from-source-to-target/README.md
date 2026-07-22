---
Practice Link: https://leetcode.com/problems/all-paths-from-source-to-target/description/
Company Tags: Bloomberg (Most frequent), Microsoft, Amazon, Google, Meta, Apple
---

# 797. All Paths From Source to Target

<p align="right">Last updated - 23.07.2026</p>

## Intuition

Since the input graph is a **Directed Acyclic Graph (DAG)**, there are no cycles to worry about, meaning we will never get stuck in an infinite loop.

To find all paths from node `0` to node `n - 1`, we can use **Depth-First Search (DFS) with Backtracking**:

1. We start at node `0` and construct a path step-by-step.
2. At any given node, we explore all of its outgoing edges (neighbors) recursively.
3. If we reach node `n - 1`, we have found a complete valid path, so we add a copy of our current path to our results.
4. When we finish exploring all paths starting from a neighbor, we **backtrack** by removing the current node from our path list before returning to the previous node.

## Algorithm

1. Initialize a global/result list `ans` to hold all complete paths.
2. Define a recursive helper function `dfs(node, graph, path, ans)`:
   - **Step 1:** Append `node` to `path`.
   - **Step 2 (Base Case):** If `node == n - 1`, add a copy of `path` to `ans`.
   - **Step 3 (Recursive Step):** Otherwise, iterate over each neighbor in `graph[node]` and call `dfs(neighbor, ...)` recursively.
   - **Step 4 (Backtrack):** Remove the last added node from `path` so other branches can use `path` cleanly.

3. Start the search by calling `dfs(0, graph, path, ans)` from the main function and return `ans`.

## Implementation

```java []
// Java
class Solution {
    public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        dfs(0, graph, ans, path);
        return ans;
    }

    private void dfs(int node, int[][] graph, List<List<Integer>> ans, List<Integer> path) {
        path.add(node);

        if (node == graph.length - 1) {
            ans.add(new ArrayList<>(path));
        } else {
            for (int neighbor : graph[node]) {
                dfs(neighbor, graph, ans, path);
            }
        }

        path.remove(path.size() - 1); // backtrack
    }
}

```

```cpp []
// C ++
class Solution {
public:
    vector<vector<int>> allPathsSourceTarget(vector<vector<int>>& graph) {
        vector<vector<int>> ans;
        vector<int> path;
        dfs(0, graph, ans, path);
        return ans;
    }

private:
    void dfs(int node, const vector<vector<int>>& graph, vector<vector<int>>& ans, vector<int>& path) {
        path.push_back(node);

        if (node == graph.size() - 1) {
            ans.push_back(path);
        } else {
            for (int neighbor : graph[node]) {
                dfs(neighbor, graph, ans, path);
            }
        }

        path.pop_back(); // backtrack
    }
};

```

## Complexity Analysis

Let $N$ be the number of nodes in the graph.

- **Time Complexity:** $O(2^N \cdot N)$
  - In the worst-case scenario (e.g., a fully connected DAG where node $i$ connects to all nodes $j > i$), there are up to $2^{N-1} - 1$ paths from node `0` to node `N - 1`.
  - For each path found, copying it into the results takes $O(N)$ time.

- **Space Complexity:** $O(N)$ auxiliary space
  - The maximum depth of the recursion stack and the size of the `path` list at any time is $O(N)$. _(Note: The space to store the output `ans` is $O(2^N \cdot N)$)._
