# [Find the City With the Smallest Number of Neighbors at a Threshold Distance](https://leetcode.com/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/description/)

<p align="right">Last updated - 30.07.2026</p>

## Approach: Floyd-Warshall Algorithm

### Intuition

The problem asks us to find a city that can reach the **fewest other cities** within a given maximum distance (`distanceThreshold`). If multiple cities tie for the smallest count, we pick the city with the **largest index**.

To determine how many cities city $A$ can reach within `distanceThreshold`, we first need to know the **shortest path distance** from city $A$ to every other city $B$. Since we need this all-pairs shortest path information for every city in the graph, the **Floyd-Warshall algorithm** is a natural fit:

- It computes the shortest distance between **all pairs of nodes** in $\mathcal{O}(n^3)$ time.
- Given $n \le 100$, an $\mathcal{O}(n^3)$ approach executes well within time limits ($100^3 = 1,000,000$ operations).

Once all-pairs shortest distances are calculated:

1. Count how many cities $j$ satisfy $\text{dist}[i][j] \le \text{distanceThreshold}$ for each city $i$.
2. Track the minimum count seen so far.
3. Update the answer whenever a city has a count **less than or equal to** the minimum (the $\le$ operator naturally handles tie-breaking by choosing the larger index $i$).

---

### 2. Algorithm

1. **Initialize Distance Matrix:**
   - Create an $n \times n$ matrix `dist` initialized with a large value (e.g., $10^9$) to represent infinity, avoiding integer overflow.
   - Set $\text{dist}[i][i] = 0$ for all $0 \le i < n$.
   - Populate initial direct edge weights from `edges` into `dist[u][v]` and `dist[v][u]`.

2. **Run Floyd-Warshall Algorithm:**
   - Loop through intermediate nodes $k$ from $0$ to $n-1$.
   - Loop through source nodes $i$ from $0$ to $n-1$.
   - Loop through destination nodes $j$ from $0$ to $n-1$.
   - Update: $\text{dist}[i][j] = \min(\text{dist}[i][j], \text{dist}[i][k] + \text{dist}[k][j])$.

3. **Find Optimal City:**
   - Initialize `minReachableCount = Integer.MAX_VALUE` and `bestCity = -1`.
   - For each city $i$ from $0$ to $n-1$:
     - Count cities $j$ ($j \neq i$) where $\text{dist}[i][j] \le \text{distanceThreshold}$.
     - If `count <= minReachableCount`, update `minReachableCount = count` and `bestCity = i`.

- Return `bestCity`.

### Implementation

```java []
class Solution {
    public int findTheCity(int n, int[][] edges, int distanceThreshold) {
        int[][] dist = new int[n][n];
        int INF = (int) 1e9; // Large value to prevent integer overflow

        // Step 1: Initialize distance matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else {
                    dist[i][j] = INF;
                }
            }
        }

        // Populate direct edge distances
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int weight = edge[2];
            dist[u][v] = weight;
            dist[v][u] = weight;
        }

        // Step 2: Floyd-Warshall Algorithm (All-Pairs Shortest Paths)
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }

        // Step 3: Find city with smallest number of reachable neighbors
        int minReachableCount = Integer.MAX_VALUE;
        int bestCity = -1;

        for (int i = 0; i < n; i++) {
            int reachableCount = 0;
            for (int j = 0; j < n; j++) {
                if (i != j && dist[i][j] <= distanceThreshold) {
                    reachableCount++;
                }
            }

            // Tie-breaker: <= picks the larger index when counts are equal
            if (reachableCount <= minReachableCount) {
                minReachableCount = reachableCount;
                bestCity = i;
            }
        }

        return bestCity;
    }
}

```

### Complexity Analysis

- **Time Complexity:** $\mathcal{O}(n^3)$
  - **Matrix Initialization:** $\mathcal{O}(n^2)$
  - **Floyd-Warshall Loop:** Three nested loops running $n$ times each, taking $\mathcal{O}(n^3)$ operations.
  - **Counting Neighbors:** Two nested loops running $n$ times each, taking $\mathcal{O}(n^2)$ operations.
  - _Overall Time Complexity:_ $\mathcal{O}(n^3)$. With $n \le 100$, $100^3 = 10^6$ operations, which runs well within the 1-second execution limit.

- **Space Complexity:** $\mathcal{O}(n^2)$
  - Uses an $n \times n$ 2D matrix `dist` to store shortest path distances between all pairs of nodes.
