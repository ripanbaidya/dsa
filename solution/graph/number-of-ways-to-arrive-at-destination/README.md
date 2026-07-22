---
Practice: https://leetcode.com/problems/number-of-ways-to-arrive-at-destination/description/
Company Tags: Google, Amazon, Microsoft, Bloomberg
---

# [1976. Number of Ways to Arrive at Destination](https://leetcode.com/problems/number-of-ways-to-arrive-at-destination/description/)

<p align="right">Last updated - 22.07.2026</p>

## Overview

We have $n$ intersections in a city, represented as nodes in a fully connected graph with bidirectional roads as edges. Each road has a given travel time. Our goal is to determine the number of distinct ways to travel from intersection `0` to intersection `n - 1` while taking the shortest possible time. The problem guarantees that every intersection is reachable from any other intersection, ensuring that the graph is fully connected. Additionally, there is at most one road between any two intersections, so we do not have to consider duplicate edges.

One important detail is that the number of ways can be large, so the answer must be returned modulo $10^9 + 7$. A common mistake is assuming that all roads have unique travel times, but the problem does not impose this restriction. Multiple roads may contribute to the shortest path calculation, and all must be considered. Since the roads are bidirectional, each one can be traversed in either direction. However, backtracking is unnecessary here, meaning we can ignore paths that visit the same road twice, as they will definitely take more time to reach the destination.

For instance, in the first example of the problem description, the shortest time to travel from intersection `0` to intersection `6` is 7 minutes. There are four distinct paths that achieve this travel time, each taking different routes but resulting in the same minimum duration.

Our approach will be based on two fundamental concepts: **Graph Theory** and **Dijkstra’s Shortest Path Algorithm**.

- **Graph Theory:** Understanding graphs, nodes, edges, and different types of graph representations (adjacency list, adjacency matrix).
- **Dijkstra’s Algorithm:** A fundamental shortest path algorithm that efficiently finds the minimum distance from a source node to all other nodes in a weighted graph.

## Approach 1: Dijkstra's Algorithm

### Intuition

Dijkstra’s algorithm is the best fit for this problem because it efficiently finds the shortest path from a single source node to all other nodes in a graph with non-negative edge weights. The core principle of Dijkstra’s algorithm is that it always expands the currently known shortest path first, ensuring that when we reach a node, we do so in the minimum time possible.

Other approaches, such as Breadth-First Search (BFS), Depth-First Search (DFS), or the Bellman-Ford algorithm, would not be as efficient:

- **BFS** does not work for weighted graphs unless modified with a priority queue, which ultimately turns it into Dijkstra’s algorithm.
- **DFS** would be highly inefficient because it explores all possible paths—many of which are unnecessary since they do not guarantee the shortest travel time. The brute-force approach of checking all paths using DFS would have exponential time complexity and would be infeasible for large inputs.

Dijkstra’s algorithm is a greedy algorithm that uses a min-heap (priority queue) to process nodes in increasing order of their shortest known distance. The algorithm starts from the source node (node `0`), initializes its distance to `0`, and sets the distance for all other nodes to infinity. The priority queue ensures that the node with the shortest known distance is always processed first.

For each node extracted from the priority queue, its neighbors are checked. If traveling through the current node provides a shorter path to a neighboring node, the shortest time to that node is updated, and the neighbor is added to the priority queue for further processing. This continues until all nodes have been processed, at which point the shortest time to each node is known.

The reason Dijkstra’s algorithm works correctly is that once a node is extracted from the priority queue, we are guaranteed that we have found the shortest possible path to that node. Any future attempts to update its distance will fail because any other node that could have led to a shorter path already has a greater cost (otherwise, it would have been extracted first from the heap). Additionally, since all edges have a positive weight, any further paths to that node will only add cost.

#### Modifying Dijkstra's to Count Paths

The standard implementation of Dijkstra’s algorithm only finds the shortest distance to each node. However, this problem also requires us to count how many different ways exist to reach the last node (`n - 1`) using the shortest possible time.

To achieve this, we introduce an additional array, `pathCount`, where `pathCount[i]` keeps track of the number of ways to reach node `i` in the shortest time possible.

1. **Initial State:** `pathCount[0] = 1`, since there is exactly one way to start at node `0`.
2. **New Shortest Path Found:** When we find a new shorter path to a node, we reset its path count to match the number of ways to reach the current node (`pathCount[neighborNode] = pathCount[currNode]`).
3. **Equal Shortest Path Found:** If we encounter another path to a node with the exact same shortest time, we add the current node's path count to the neighbor's path count (`pathCount[neighborNode] = (pathCount[neighborNode] + pathCount[currNode]) % MOD`).

#### Avoiding Integer Overflow Edge Cases

This problem is notorious for edge cases involving integer types. A common mistake is using `INT_MAX` as the initial value, assuming it is large enough to represent infinity. However, for this problem, using `INT_MAX` causes incorrect results or integer overflow.

To understand why, analyze the constraints:

- The number of nodes ($n$) is at most $200$.
- The edge weights (`time[i]`) can be as large as $10^9$.

The worst-case scenario occurs when the shortest path involves traversing $199$ edges in a nearly linear graph. The total shortest path value can reach:

$$199 \times 10^9 = 1.99 \times 10^{11}$$

This is far greater than `INT_MAX` ($\approx 2.1 \times 10^9$). If we initialize distances with `INT_MAX`, adding even a single edge weight ($10^9$) causes an integer overflow.

To avoid this, initialize the `shortestTime` array with `Long.MAX_VALUE` (or $9.2 \times 10^{18}$ / `1e12` equivalents in other languages).

### Algorithm

1. Define $\text{MOD} = 10^9 + 7$ for modular arithmetic.
2. Build an adjacency list `graph` where `graph[i]` stores `{neighbor, travelTime}` pairs.
3. Initialize a min-heap (`minHeap`) for Dijkstra's algorithm storing `{currTime, currNode}`.
4. Create a `shortestTime` array initialized to `Long.MAX_VALUE`.
5. Create a `pathCount` array initialized to `0`.
6. Set `shortestTime[0] = 0` and `pathCount[0] = 1`.
7. Push `{0, 0}` into `minHeap` to start processing.
8. While `minHeap` is not empty:
   - Extract the node `currNode` with the current shortest known time `currTime`.
   - If `currTime > shortestTime[currNode]`, skip outdated distances.
   - Iterate over neighbors of `currNode`:
     - **If a new shortest path is found:**
       - Update `shortestTime[neighborNode]`.
       - Reset `pathCount[neighborNode]` to match `pathCount[currNode]`.
       - Push `{shortestTime[neighborNode], neighborNode}` into `minHeap`.

     - **If an equally short path is found:**
       - Add `pathCount[currNode]` to `pathCount[neighborNode]`, modulo $\text{MOD}$.

9. Return `pathCount[n - 1]`, the number of shortest paths to the last node.

> **Time-saving coding tip:**
> Whenever a problem involves calculating distances or counting paths, it's a good idea to use `long long` (or an equivalent large integer type) and apply the modulo operator when required. This helps prevent integer overflow and ensures accurate results, especially in graph and dynamic programming problems.

### Implementation

```java []
class Solution {

    public int countPaths(int n, int[][] roads) {
        final int MOD = 1_000_000_007;

        // Build adjacency list
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] road : roads) {
            int startNode = road[0], endNode = road[1], travelTime = road[2];
            graph.get(startNode).add(new int[] { endNode, travelTime });
            graph.get(endNode).add(new int[] { startNode, travelTime });
        }

        // Min-Heap (priority queue) for Dijkstra
        PriorityQueue<long[]> minHeap = new PriorityQueue<>(
            Comparator.comparingLong(a -> a[0])
        );

        // Store shortest time to each node
        long[] shortestTime = new long[n];
        Arrays.fill(shortestTime, Long.MAX_VALUE);

        // Number of ways to reach each node in shortest time
        int[] pathCount = new int[n];

        shortestTime[0] = 0; // Distance to source is 0
        pathCount[0] = 1;    // 1 way to reach node 0

        minHeap.offer(new long[] { 0, 0 }); // {time, node}

        while (!minHeap.isEmpty()) {
            long[] top = minHeap.poll();
            long currTime = top[0]; // Current shortest time
            int currNode = (int) top[1];

            // Skip outdated distances
            if (currTime > shortestTime[currNode]) {
                continue;
            }

            for (int[] neighbor : graph.get(currNode)) {
                int neighborNode = neighbor[0], roadTime = neighbor[1];

                // Found a new shortest path → Update shortest time and reset path count
                if (currTime + roadTime < shortestTime[neighborNode]) {
                    shortestTime[neighborNode] = currTime + roadTime;
                    pathCount[neighborNode] = pathCount[currNode];
                    minHeap.offer(
                        new long[] { shortestTime[neighborNode], neighborNode }
                    );
                }
                // Found another way with the same shortest time → Add to path count
                else if (currTime + roadTime == shortestTime[neighborNode]) {
                    pathCount[neighborNode] =
                        (pathCount[neighborNode] + pathCount[currNode]) % MOD;
                }
            }
        }

        return pathCount[n - 1];
    }
}

```

### Complexity Analysis

Let $N$ be the number of nodes in the graph and $E$ be the number of edges in the given road connections.

- **Time Complexity:** $\mathcal{O}(N + E \log E)$
  - Building the adjacency list takes $\mathcal{O}(E)$ time since we iterate over all the edges once.
  - The main part of the algorithm is Dijkstra’s algorithm using a min-heap. In this implementation, a node can be added to the heap multiple times if a shorter path to it is found later.
  - For each edge, we may perform a heap insertion, and the heap can grow up to size $\mathcal{O}(E)$ in the worst case.
  - Each insertion or extraction from the heap takes $\mathcal{O}(\log E)$ time. Thus, the total time spent on heap operations is $\mathcal{O}(E \log E)$.
  - Combining both parts gives $\mathcal{O}(E) + \mathcal{O}(E \log E) = \mathcal{O}(N + E \log E)$.

- **Space Complexity:** $\mathcal{O}(N + E)$
  - The adjacency list stores $2 \cdot E$ directed edge entries across $N$ lists, taking $\mathcal{O}(N + E)$ space.
  - The priority queue stores at most $\mathcal{O}(E)$ elements.
  - The `shortestTime` and `pathCount` arrays each require $\mathcal{O}(N)$ space.
  - Therefore, the overall space complexity is dominated by $\mathcal{O}(N + E)$.
