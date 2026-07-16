# [743. Network Delay Time](https://leetcode.com/problems/network-delay-time/description/)

<p align="right">Last updated - 16.07.2026</p>

## Approach: Dijkstra

### Intuition

Imagine you are standing at a source node $k$ and you send a broadcast signal (like a ripple in water) that travels along directed pathways. Each pathway takes a specific amount of time to cross.

We want to find out:

1. **How long does it take for the signal to reach every single node in the network?**
2. **Is it even possible to reach everyone?**

If we find the shortest (minimum) time it takes to reach each individual node from our starting point $k$, the overall time it takes for **all** nodes to receive the signal will simply be the **maximum** of these individual shortest times. If there is any node we can't reach at all, we return `-1`.

Because all the travel times (edges) are non-negative, this is a textbook **Single-Source Shortest Path (SSSP)** problem. The most efficient algorithm to solve this is **Dijkstra's Algorithm**.

### Algorithm

Dijkstra's algorithm works greedily. It always expands the reach of the node that is currently closest to the starting point.

1. **Represent the Graph:** Convert the input edge list `times` into an **Adjacency List** so we can quickly look up neighbors and travel times for any node.
2. **Track Minimum Times:** Maintain a `shortestTime` array (or vector) of size $n + 1$ (since nodes are 1-indexed). Initialize all distances to "infinity" (a very large number) and the start node $k$ to `0`.
3. **Use a Min-Heap (Priority Queue):** Store pairs of `{travel_time_to_reach_node, node_id}`. The Min-Heap ensures we always process the node with the absolute smallest signal arrival time next.
4. **Relax the Edges:**
   - Pop the node with the minimum arrival time from the heap. Let's call this node `u` with current arrival time `time`.
   - If this popped `time` is greater than the already recorded `shortestTime[u]`, skip it (it's a stale path).
   - For each neighbor `v` of `u` connected by weight `w`:
     - Calculate the potential arrival time at `v`: $\text{new\_time} = \text{time} + w$.
     - If $\text{new\_time}$ is strictly less than the currently recorded `shortestTime[v]`, we update `shortestTime[v]` and push `{new_time, v}` to our heap.

5. **Get the Result:** After processing, find the maximum value in our `shortestTime` array (ignoring index `0`). If any node's shortest time remains "infinity", it means the signal never reached it—return `-1`. Otherwise, return that maximum value.

### Implementations

**Java**

```java []
import java.util.*;

class Solution {
    public int networkDelayTime(int[][] times, int n, int k) {
        // Step 1: Build the Adjacency List (1-indexed)
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : times) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            adj.get(u).add(new int[]{v, w});
        }

        // Step 2: Initialize the shortest times tracker
        int[] shortestTime = new int[n + 1];
        Arrays.fill(shortestTime, (int) 1e9);
        shortestTime[k] = 0;

        // Step 3: Priority Queue (Min-Heap) sorted by travel time
        // Each element is: {time_taken_to_reach, node}
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        pq.offer(new int[]{0, k});

        // Step 4: Process the graph
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int currentTime = current[0];
            int u = current[1];

            // If we found a shorter path to u already, skip processing this older path
            if (currentTime > shortestTime[u]) {
                continue;
            }

            // Traverse all neighbors of u
            for (int[] neighbor : adj.get(u)) {
                int v = neighbor[0];
                int weight = neighbor[1];

                // If traveling to 'v' via 'u' is faster, update and push to queue
                if (currentTime + weight < shortestTime[v]) {
                    shortestTime[v] = currentTime + weight;
                    pq.offer(new int[]{shortestTime[v], v});
                }
            }
        }

        // Step 5: Find the maximum time to reach any node
        int maxDelay = 0;
        for (int i = 1; i <= n; i++) {
            if (shortestTime[i] == (int) 1e9) {
                return -1; // At least one node is unreachable
            }
            maxDelay = Math.max(maxDelay, shortestTime[i]);
        }

        return maxDelay;
    }
}
```

**C++**

```cpp []
#include <bits/stdc++.h>

using namespace std;

class Solution {
public:
    int networkDelayTime(vector<vector<int>>& times, int n, int k) {
        // Step 1: Build the Adjacency List
        // adj[u] will hold pairs of {neighbor, weight}
        vector<vector<pair<int, int>>> adj(n + 1);
        for (const auto& edge : times) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            adj[u].push_back({v, w});
        }

        // Step 2: Initialize shortest times tracker
        vector<int> shortestTime(n + 1, INT_MAX);
        shortestTime[k] = 0;

        // Step 3: Min-Heap to process nodes.
        // Stores elements as: {time_taken_to_reach, node}
        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
        pq.push({0, k});

        // Step 4: Process the graph
        while (!pq.empty()) {
            auto [currentTime, u] = pq.top();
            pq.pop();

            // Skip processing if a better path to u has already been finalized
            if (currentTime > shortestTime[u]) {
                continue;
            }

            // Traverse neighbors
            for (const auto& neighbor : adj[u]) {
                int v = neighbor.first;
                int weight = neighbor.second;

                // Relaxation condition
                if (currentTime + weight < shortestTime[v]) {
                    shortestTime[v] = currentTime + weight;
                    pq.push({shortestTime[v], v});
                }
            }
        }

        // Step 5: Extract the result
        int maxDelay = 0;
        for (int i = 1; i <= n; ++i) {
            if (shortestTime[i] == INT_MAX) {
                return -1; // Node is unreachable
            }
            maxDelay = max(maxDelay, shortestTime[i]);
        }

        return maxDelay;
    }
};

```

### Complexity Analysis

Let $V$ represent the number of vertices (nodes) $n$, and $E$ represent the number of directed edges in `times`.

- Time Complexity: $O(E \log V)$
  - **Graph Building:** Building the adjacency list takes $O(E)$ time.
  - **Heap Operations:** In the worst-case scenario, we might insert every edge relaxation into our Min-Heap. The maximum size of the heap is $O(E)$.
  - Inserting and extracting from a binary heap of size $E$ takes $O(\log E)$ time. Because $E \le V^2$, we can simplify $O(\log E)$ to $O(\log V^2) = O(2 \log V) = O(\log V)$.
  - Thus, performing heap operations across all edges results in an overall time complexity of **$O(E \log V)$**.

- Space Complexity: $O(V + E)$
  - **Adjacency List:** Requires storing all $V$ nodes and $E$ edges, taking $O(V + E)$ space.
  - **Distances Array:** Storing the `shortestTime` array takes $O(V)$ space.
  - **Priority Queue:** In the worst-case, the heap can store up to $O(E)$ path options.
  - Combining these, the overall auxiliary space complexity is **$O(V + E)$**.
