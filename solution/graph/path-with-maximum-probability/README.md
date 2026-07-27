# [Path with Maximum Probability](https://leetcode.com/problems/path-with-maximum-probability/)

<p align="right">Last updated - 27.07.2026</p>

## Approach: Dijkstra

### Intuition

Standard shortest path problems (like standard Dijkstra's algorithm) aim to find the path with the **minimum total distance/cost** by adding edge weights together.

In this problem:

1. We want to find a path with the **maximum probability** instead of minimum cost.
2. Probabilities along a path are **multiplied** together rather than added.
3. Since edge probabilities are values between $0$ and $1$, multiplying more edges together can only keep the probability the same or make it smaller.

Because of this greedy property (multiplying by a value $\le 1$ never increases the total probability), we can adapt **Dijkstra's Algorithm**:

- Instead of a Min-Heap (to pick the smallest distance), we use a **Max-Heap** (Priority Queue) to always explore the node with the highest probability first.
- The moment we pop the destination node (`endNode`) from our max-heap, we are guaranteed to have found the maximum possible probability to reach it.

### Algorithm

1. **Build Adjacency List:**
   Convert the edge array into an adjacency list representation. Each entry will store the target neighbor and the success probability of traversing that undirected edge.
2. **Initialize Tracking:**
   - Create an array `maxProb` of size $n$ to keep track of the highest probability found to reach each node so far. Initialize all values to $0.0$.
   - Create a **Max-Heap** (Priority Queue) sorted by probability in descending order.

3. **Start Traversal:**
   - Push the `startNode` into the max-heap with an initial probability of $1.0$ (100% chance of starting at the start node).

4. **Process Nodes (Dijkstra):**
   - Extract the node with the highest probability from the heap.
   - If this node is the `endNode`, return its probability immediately.
   - If the extracted probability is smaller than `maxProb[currentNode]`, skip it (it's an outdated entry).
   - Otherwise, iterate through all neighbors:
     - Calculate the new path probability to the neighbor: `currentProb * edgeProbability`.
     - If this new probability is higher than `maxProb[neighborNode]`, update `maxProb[neighborNode]` and push the neighbor into the max-heap.

5. **Fallback:**
   - If the heap becomes empty and we never reached `endNode`, return `0.0` (no path exists).

### Implementations

```java []
class Pair {
    int node;
    double probability;

    Pair(int node, double probability) {
        this.node = node;
        this.probability = probability;
    }
}

class Solution {
    public double maxProbability(int n, int[][] edges, double[] succProb, int startNode, int endNode) {
        // Step 1: Build the adjacency list for the undirected graph
        List<List<Pair>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }

        for (int i = 0; i < edges.length; i++) {
            int u = edges[i][0];
            int v = edges[i][1];
            double prob = succProb[i];

            adj.get(u).add(new Pair(v, prob));
            adj.get(v).add(new Pair(u, prob));
        }

        // Step 2: Distance array to store the maximum probability to reach each node
        double[] maxProb = new double[n];

        // Step 3: Max-Heap to explore paths with highest probability first
        PriorityQueue<Pair> maxHeap = new PriorityQueue<>(
            (a, b) -> Double.compare(b.probability, a.probability)
        );

        // Start at the startNode with probability 1.0
        maxHeap.offer(new Pair(startNode, 1.0));

        // Step 4: Dijkstra-like traversal
        while (!maxHeap.isEmpty()) {
            Pair current = maxHeap.poll();
            int currNode = current.node;
            double currProb = current.probability;

            // Target reached; because of Max-Heap, this is guaranteed to be optimal
            if (currNode == endNode) {
                return currProb;
            }

            // Skip if we already found a strictly better path to currNode
            if (currProb < maxProb[currNode]) {
                continue;
            }

            // Explore all adjacent neighbors
            for (Pair neighbor : adj.get(currNode)) {
                int neighborNode = neighbor.node;
                double edgeProb = neighbor.probability;

                // Relax the edge if a path with higher probability is found
                if (maxProb[neighborNode] < currProb * edgeProb) {
                    maxProb[neighborNode] = currProb * edgeProb;
                    maxHeap.offer(new Pair(neighborNode, maxProb[neighborNode]));
                }
            }
        }

        // If endNode is unreachable
        return 0.0;
    }
}

```

### Complexity Analysis

- Time Complexity: $O(E \log V)$
  - **Graph Construction:** $O(E)$, where $E$ is the total number of edges.
  - **Dijkstra Traversal:** In the worst case, every edge is examined and added to the heap. Pushing into or popping from a priority queue of size $V$ takes $O(\log V)$ time. Therefore, processing all vertices and edges takes $O((V + E) \log V)$ time, which simplifies to **$O(E \log V)$** for connected graph instances.

- Space Complexity: $O(V + E)$
  - **Adjacency List:** Stores $V$ vertices and $2E$ total directed edges across lists, requiring $O(V + E)$ space.
  - **Probability Array (`maxProb`):** Stores maximum probabilities for $V$ nodes, requiring $O(V)$ space.
  - **Priority Queue (`maxHeap`):** Holds up to $E$ elements in the worst case, requiring $O(E)$ space.
  - Overall space required is **$O(V + E)$**.
