class Solution {
    // DFS

    public ArrayList<Integer> topoSort(int V, int[][] edges) {
        // Build the Adjacency List
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
        }

        boolean[] vis = new boolean[V];
        Stack<Integer> stk = new Stack<>();

        // Invoke DFS for all unvisited components
        for (int i = 0; i < V; i++) {
            if (!vis[i]) {
                dfs(i, adj, stk, vis);
            }
        }

        // Extract elements from stack to get topological order
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


/*
class Solution {
    public ArrayList<Integer> topoSort(int V, int[][] edges) {
        // Convert edge list into an adjacency list and compute InDegrees
        List<List<Integer>> adj = new ArrayList<>();
        int[] inDegree = new int[V];

        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            inDegree[edge[1]]++; // Increment inward degree for destination node
        }

        // Push all nodes with InDegree 0 into the queue
        Queue<Integer> que = new ArrayDeque<>();
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                que.offer(i);
            }
        }

        // Process the queue
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
*/