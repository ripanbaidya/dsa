// Using DFS

class Solution {
    // Function to detect cycle in a directed graph.
    public boolean isCyclic(int V, int[][] edges) {
        // Step 1: Build the adjacency list from the given edges
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
        }

        // Track overall visited nodes
        boolean[] vis = new boolean[V];
        // Track nodes in the current DFS recursion stack
        boolean[] pathVis = new boolean[V];

        // Step 2: Traverse all components of the graph
        for (int i = 0; i < V; i++) {
            if (!vis[i]) {
                if (dfs(i, vis, pathVis, adj))
                    return true; // Cycle detected
            }
        }

        return false; // No cycle found
    }

    private boolean dfs(int node, boolean[] vis, boolean[] pathVis,
        List<List<Integer>> adj) {
        // Mark the current node as visited and active in the current path
        vis[node] = true;
        pathVis[node] = true;

        // Step 3: Traverse all adjacent neighbors
        for (int nei : adj.get(node)) {
            // If the neighbor is not visited, recursively visit it
            if (!vis[nei]) {
                if (dfs(nei, vis, pathVis, adj))
                    return true;
            }
            // If the neighbor is visited AND it is in the current path, a cycle exists
            else if (vis[nei] && pathVis[nei]) {
                return true;
            }
        }

        // Step 4: Backtrack - remove the node from the current path tracking
        pathVis[node] = false;
        return false;
    }
}

/*
// Using BFS/ Khan's Algorithm

public class Solution {
 
    public boolean isCyclic(int V, int[][] edges) {
        // Step 1: Build adjacency list and compute in-degrees
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        int[] inDegree = new int[V];
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            inDegree[edge[1]]++;
        }

        // Step 2: Push all nodes with in-degree 0 into a queue
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        // Step 3: Process the queue and keep a count of visited nodes
        int visitedCount = 0;
        while (!queue.isEmpty()) {
            int node = queue.poll();
            visitedCount++;

            // Reduce in-degree for all adjacent neighbors
            for (int nei : adj.get(node)) {
                inDegree[nei]--;
                // If in-degree becomes 0, add it to the queue
                if (inDegree[nei] == 0) {
                    queue.add(nei);
                }
            }
        }

        // If visitedCount equals V, then topological sort is complete (No Cycle)
        // If visitedCount != V, a cycle exists.
        return visitedCount != V;
    }
}

*/
