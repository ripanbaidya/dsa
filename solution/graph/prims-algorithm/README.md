# [Prims Algorithm](https://www.geeksforgeeks.org/problems/minimum-spanning-tree/1)

<p align="right">Last updated - 30.07.2026</p>

**Prim's Algorithm** is a greedy algorithm used to find the **Minimum Spanning Tree (MST)** of a connected, weighted, undirected graph.

A **Spanning Tree** is a subgraph that includes all $V$ vertices of the original graph connected using exactly $V - 1$ edges without forming any cycles. A **Minimum Spanning Tree** is the spanning tree where the **sum of edge weights is minimized**.

![](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQIAAsmYmZBVrFhoDoCOW6B18sG35IeMW33UoxbEgmRbRGD2sYdQExZeQc&s=10)

### Why Do We Need It?

Whenever you need to **connect a set of locations at the absolute minimum total cost**, you are solving for an MST.

#### Real-World Applications:

1. **Telecommunications & Fiber Optics:** Laying cables to connect multiple cities using the minimum total length of cable.
2. **Electrical Power Grids:** Connecting sub-stations to a power grid while minimizing wiring cost.
3. **Road / Railway Network Design:** Building roads between towns such that every town is reachable from any other town with minimal paving budget.
4. **Cluster Analysis in ML:** Used in single-linkage hierarchical clustering to group similar data points.

## Intuition Behind Prim's Algorithm

Think of Prim’s algorithm as growing an **expanding oil slick** or a **growing tree**:

1. **Start with a single node:** You place your seed at any starting node (e.g., node `0`). This node forms your initial "visited" set.
2. **Look at outgoing boundaries:** Look at all edges leading from the visited set to any unvisited nodes.
3. **Pick the cheapest option:** Out of all available boundary edges, greedily pick the one with the smallest weight.
4. **Grow the tree:** Include the new destination node into your visited set.
5. **Repeat:** Continue pulling in the cheapest available unvisited neighbor until all nodes are connected.

> **Why a Min-Heap (Priority Queue)?**
> Instead of scanning every edge over and over to find the minimum boundary weight, a **Min-Heap (Priority Queue)** allows us to extract the lightest available edge in **$O(\log V)$ time**.

## Algorithm

1. Create a boolean array `vis` of size V to track connected nodes.
2. Create a Min-Heap Priority Queue `pq` storing pairs `{weight, node}`.
3. Push `{0, 0}` into `pq` (start at node 0 with cost 0).
4. Initialize `totalMstWeight = 0`.
5. WHILE `pq` is not empty:
   - Extract `{wt, u}` with the minimum weight from `pq`.
   - IF `vis[u]` is true: - Skip it (this node was already connected via a cheaper edge).
   - Mark `vis[u] = true` and add `wt` to `totalMstWeight`.
   - FOR each neighbor `{v, weight}` of `u`:
     - IF `vis[v]` is false:
       - Push `{weight, v}` into `pq`.

6. Return `totalMstWeight`.

## Key Takeaways

- **Tree Property:** Prim's always maintains a single growing connected tree at every step (unlike Kruskal's algorithm, which builds a forest of disconnected components and merges them).
- **Lazy vs Eager:** The implementation we wrote uses **Lazy Prim's**—it pushes candidate edges into the Priority Queue as they are discovered and skips obsolete edges when popped using the `if (vis[u]) continue;` check.

## Implementation

```java []
class Solution {

    public int spanningTree(int V, int[][] edges) {
        // Build the Adjacency List - {neighborNode, weight}
        List<List<int[]>> adj = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], wt = edge[2];
            adj.get(u).add(new int[]{v, wt});
            adj.get(v).add(new int[]{u, wt});
        }

        // Min-Heap Priority Queue, Stores elements as {weight, node}
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

        boolean[] vis = new boolean[V];

        // Start from vertex 0 with weight 0 -> {weight, node}
        pq.offer(new int[]{0, 0});

        int totalMstWeight = 0;

        // Extract smallest edge weight and process
        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int wt = top[0]; // top[0] is weight
            int u = top[1];  // top[1] is node

            // Skip if node is already included in MST
            if (vis[u]) {
                continue;
            }

            // Mark as visited ONLY when popped from PQ
            vis[u] = true;
            totalMstWeight += wt;

            // Add valid neighbors
            for (int[] nei : adj.get(u)) {
                int neiNode = nei[0];
                int neiNodeWt = nei[1];

                if (!vis[neiNode]) {
                    // Offer as {weight, node}
                    pq.offer(new int[]{neiNodeWt, neiNode});
                }
            }
        }

        return totalMstWeight;
    }
}

```

## Complexity Analysis

The time and space complexity of Prim's Algorithm depends directly on the underlying **data structures** used to implement the graph and the priority queue/min-heap.

Here is the detailed complexity breakdown for the standard implementations.

#### **Time Complexity: $\mathcal{O}(E \log V)$**

1. **Building the Adjacency List:**
   Iterating over all $E$ edges takes **$\mathcal{O}(V + E)$** time.
2. **Extracting Min Vertex (`pq.poll()`):**
   - Each vertex is extracted from the Priority Queue at most once (or at most $E$ times in lazy deletion).
   - A single extraction takes $\mathcal{O}(\log V)$ time.
   - Total extraction cost: **$\mathcal{O}(V \log V)$** (or $\mathcal{O}(E \log V)$ in lazy Prim's).

3. **Inserting/Updating Neighbor Edges (`pq.offer()`):**
   - Every edge in the graph is considered at most twice (once from each endpoint).
   - Inserting an edge into a binary min-heap takes $\mathcal{O}(\log V)$ time.
   - Total insertion cost across all edges: **$\mathcal{O}(E \log V)$**.

Combining these steps yields a total time complexity of **$\mathcal{O}((V + E) \log V)$**, which simplifies to **$\mathcal{O}(E \log V)$** for connected graphs (since $E \ge V - 1$).

#### **Space Complexity: $\mathcal{O}(V + E)$**

- **Adjacency List:** Stores $V$ vertices and $2E$ total directed references $\rightarrow \mathcal{O}(V + E)$.
- **Visited Array (`boolean[] vis`):** Stores $V$ booleans $\rightarrow \mathcal{O}(V)$.
- **Priority Queue:** In the worst case (lazy deletion), holds up to $E$ elements $\rightarrow \mathcal{O}(E)$.

Total auxiliary space required is **$\mathcal{O}(V + E)$**.

## Reference

- 