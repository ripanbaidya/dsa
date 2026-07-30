# Floyd-Warshall Algorithm

<p align="right">Last updated - 29.07.2026</p>

**Floyd-Warshall** is an **All-Pairs Shortest Path (APSP)** graph algorithm.

Unlike single-source shortest path algorithms (like Dijkstra or Bellman-Ford) that calculate the shortest path from **one starting node** to every other node, Floyd-Warshall computes the shortest paths between **every pair of vertices** $(i, j)$ in a directed or undirected weighted graph in a single execution.

### Key Characteristics

- **Problem Type:** All-Pairs Shortest Path (APSP).
- **Paradigms Used:** **Dynamic Programming** (bottom-up tabular approach).
- **Graph Representation:** Expects an **Adjacency Matrix** `dist[V][V]`.
- **Supports:** Positive edge weights, negative edge weights, and directed/undirected edges.
- **Detects:** Negative weight cycles (if a node's self-distance becomes negative, i.e., `dist[i][i] < 0`).
- **Time Complexity:** $\mathcal{O}(V^3)$ regardless of whether the graph is sparse or dense.
- **Space Complexity:** $\mathcal{O}(V^2)$ to store the distance matrix.

### Why not just run Dijkstra or Bellman-Ford $V$ times?

1. **Running Dijkstra $V$ times:** Takes $O(V \cdot (V + E) \log V)$. For a dense graph where $E \approx V^2$, this becomes $O(V^3 \log V)$, which is slower than Floyd-Warshall's $O(V^3)$. Furthermore, **Dijkstra fails on negative edge weights**, even if there are no negative cycles.
2. **Running Bellman-Ford $V$ times:** Takes $O(V^2 \cdot E)$. For a dense graph ($E \approx V^2$), this explodes to $O(V^4)$, making Floyd-Warshall significantly faster.
3. **Simplicity:** Floyd-Warshall is a matrix-based Dynamic Programming algorithm that runs in 3 nested loops with zero complex data structures (like priority queues).

## Core Concepts

### 1. Negative Weight Edges

A graph edge can have a negative cost (e.g., gas cost vs. cash reward, or gain vs. loss in financial modeling).

- **Dijkstra fails** because it operates greedily: once a node is marked as visited/processed, Dijkstra assumes its shortest distance is finalized. A negative edge encountered later could lower the path cost to an already-visited node, breaking Dijkstra's core greedy choice property.
- **Floyd-Warshall and Bellman-Ford handle negative edges** by continuously relaxing edges across paths without making greedy assumptions about node finalization.

### 2. Negative Weight Cycles

A **negative weight cycle** is a cycle whose total edge sum is strictly less than 0 (e.g., $A \to B$ (+2), $B \to C$ (-5), $C \to A$ (+1); total sum = -2).

- If a path passes through a negative cycle, you can traverse that cycle endlessly to achieve a path weight of $-\infty$.
- **No algorithm can produce a valid shortest path** through a negative cycle. However, Floyd-Warshall can **detect** negative cycles: if after running the algorithm, any self-distance `dist[i][i] < 0`, a negative cycle exists containing node `i`.

## Intuition & Dynamic Programming State Definition

Floyd-Warshall uses **Dynamic Programming** based on the idea of incrementally considering intermediate nodes.

### Intuition

To find the shortest path between any node $i$ and node $j$, ask: _"Does taking a detour through node $k$ produce a shorter path than our current best distance from $i$ to $j$?"_

We build the solution by allowing intermediate nodes one by one:

1. First, compute shortest paths using **no intermediate nodes** (direct edges).
2. Allow node `0` to be an intermediate node.
3. Allow nodes `{0, 1}` to be intermediate nodes...
4. ...and so on, up to allowing nodes `{0, 1, 2, ..., n-1}`.

### DP State Definition

Let $\text{dp}[k][i][j]$ be the shortest distance from node $i$ to node $j$ considering only the subset of nodes $\{0, 1, \dots, k\}$ as potential intermediate nodes.

#### Recurrence Formula

$$\text{dp}[k][i][j] = \min\Big(\underbrace{\text{dp}[k-1][i][j]}_{\text{Don't use node } k \text{ as intermediate}}, \;\; \underbrace{\text{dp}[k-1][i][k] + \text{dp}[k-1][k][j]}_{\text{Use node } k \text{ as intermediate}}\Big)$$

#### Space Optimization

Notice that step $k$ only depends on step $k-1$. We can drop the 3D array down to a 2D array $\text{dist}[i][j]$ updated in-place:

#$$\text{dist}[i][j] = \min(\text{dist}[i][j], \text{dist}[i][k] + \text{dist}[k][j])$$

## Algorithm

The order of nested loops is **critical**: `k` MUST be the **outermost loop**.

```text
Initialize dist[][] matrix with edge weights, 0 for diagonal (dist[i][i]), and INF for missing edges.

For k from 0 to n-1:       // Intermediate node iteration (MUST BE OUTERMOST)
    For i from 0 to n-1:   // Source node iteration
        For j from 0 to n-1: // Destination node iteration
            If dist[i][k] != INF and dist[k][j] != INF:
                dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])

```

> **Interview Red Flag:** Placing `k` as the innermost loop is a common mistake. If `k` is innermost, you compute `dist[i][j]` using paths before `dist[i][k]` and `dist[k][j]` have been fully optimized with prior intermediate nodes.

### Implementation

### Key Edge Cases Handled:

1. **Integer Overflow (`INF + dist`):** If `dist[i][k]` is `INF`, adding any value can cause 32-bit integer overflow into negative numbers, corrupting `Math.min()`. Always check `dist[i][k] != INF && dist[k][j] != INF`.
2. **Negative Cycles:** Checked after running the main algorithm by inspecting `dist[i][i] < 0`.

```java []
class Solution {
    // 10^8 represents unreachable infinity
    private static final int INF = 100000000;

    public void floydWarshall(int[][] dist) {
        int n = dist.length;

        // Step 1: Core Floyd-Warshall Triple Loop
        // Outer loop 'k' picks the intermediate node
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // Edge Case: Avoid integer overflow when adding INF
                    if (dist[i][k] != INF && dist[k][j] != INF) {
                        dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                    }
                }
            }
        }

        // Step 2: Detection of Negative Weight Cycles (Optional / Follow-up)
        for (int i = 0; i < n; i++) {
            if (dist[i][i] < 0) {
                // Node i is part of a negative cycle
                System.out.println("Graph contains a negative weight cycle involving node: " + i);
            }
        }
    }
}

```

## Complexity Analysis

- **Time Complexity:** ${O}(V^3)$,There are 3 nested loops, each running $V$ times. The work done inside the innermost loop is $\mathcal{O}(1)$.

- **Space Complexity:** $\mathcal{O}(1)$, auxiliary dynamic space or $\mathcal{O}(V^2)$ for adjacency matrix update

  If modifying the input matrix in-place, auxiliary space is $\mathcal{O}(1)$.

## Comparison Table

| Algorithm          | Type          | Time Complexity     | Space Complexity | Handles Negative Edges? | Handles Negative Cycles? | Best Used For                                                       |
| ------------------ | ------------- | ------------------- | ---------------- | ----------------------- | ------------------------ | ------------------------------------------------------------------- |
| **Dijkstra**       | Single-Source | $O((V + E) \log V)$ | $O(V)$           | ❌ No                   | ❌ No                    | Shortest path from **one** source on non-negative graphs.           |
| **Bellman-Ford**   | Single-Source | $O(V \cdot E)$      | $O(V)$           | ✅ Yes                  | ✅ Detects them          | Shortest path from **one** source when negative edge weights exist. |
| **Floyd-Warshall** | All-Pairs     | $O(V^3)$            | $O(V^2)$         | ✅ Yes                  | ✅ Detects them          | Shortest path between **every pair** of nodes.                      |

## References

- Algorithm - https://youtu.be/oNI0rf2P9gE?si=qbadW0qUVEG7wru_
