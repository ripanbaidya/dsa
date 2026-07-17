# [Dijkstra Algorithm](https://www.geeksforgeeks.org/problems/implementing-dijkstra-set-1-adjacency-matrix/1)

<p align="right">Last updated - 17.07.2026</p>

Whether you are navigating a map, routing data packets across the internet, or trying to find the quickest path to complete a video game quest, you are likely relying on **Dijkstra's Algorithm**.

## What is Dijkstra's Algorithm?

Invented by computer scientist Edsger W. Dijkstra in 1956, Dijkstra's algorithm is a **greedy algorithm** used to find the **shortest path from a single source node to all other nodes** in a weighted, directed or undirected graph.

Think of it as a "ripple effect" or a BFS (Breadth-First Search) on steroids. While standard BFS works perfectly for unweighted graphs (where every edge costs $1$), Dijkstra is designed to handle graphs where edges have **different positive weights** (costs, distances, or times).

> ⚠️ **Crucial Rule:** Dijkstra's algorithm **only works when edge weights are non-negative**. If a graph contains negative edge weights, the greedy assumption fails, and you must use alternative algorithms like **Bellman-Ford**.

## Dijkstra Algorithm Using Priority Queue (Min-Heap)

### Intuition

The core idea of Dijkstra's algorithm is a **greedy exploration** of the shortest paths.

Think of it like a ripple effect or a spreading fire. You start at the source node with a distance of `0` and all other nodes at a distance of infinity ($\infty$). You always pick the unvisited node that currently has the **absolute shortest known distance** from the source. Once you "settle" that node, you look at all its neighbors and check if passing through this node gives them a shorter path than what they previously knew.

Using a **Priority Queue (Min-Heap)** allows us to instantly grab the closest unvisited node in $O(\log V)$ time instead of scanning every node in the graph.

### Algorithm

1. **Initialize:** Create a `distance` array/list filled with $\infty$. Set `distance[source] = 0`. Initialize a Min-Heap storing pairs of `(current_distance, node)`. Push `(0, source)` into the heap.
2. **Loop until empty:** While the Min-Heap is not empty:
   - Pop the top element `(dist, u)`.
   - **Skipping Optimization:** If `dist` is greater than the already recorded `distance[u]`, skip it (it's a stale entry).

3. **Relax Edges:** For each neighbor `v` of node `u` with weight `w`:
   - If `distance[u] + w < distance[v]`:
   - Update `distance[v] = distance[u] + w`.
   - Push `(distance[v], v)` into the Min-Heap.

### Implementation

**Java**

```java
class Solution {
	public int[] dijkstra(int V, int[][] edges, int src) {
		// 1. Build the Adjacency List
		// Each element is an array of {neighborNode, edgeWeight}
		List<List<int[]>> adj = new ArrayList<>();
		for (int i = 0; i < V; i++) {
			adj.add(new ArrayList<>());
		}
		
		for (int[] edge : edges) {
			int u = edge[0];
			int v = edge[1];
			int weight = edge[2];
			
			adj.get(u).add(new int[] {v, weight});
			adj.get(v).add(new int[] {u, weight});
		}
		
		// 2. Initialize the Min-Heap Priority Queue tracking {distanceFromSource, node}
		PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
		
		// 3. Initialize tracking array for shortest distances
		int[] dist = new int[V];
		Arrays.fill(dist, (int) 1e9); // Using 1e9 as a safe alternative to infinity
		
		// 4. Base Case Setup
		dist[src] = 0;
		minHeap.offer(new int[] {0, src});
		
		// 5. Process the graph greedily
		while (!minHeap.isEmpty()) {
			int[] current = minHeap.poll();
			int currentDistance = current[0];
			int currentNode = current[1];
			
			// Skip processing if we already found a shorter path to this node
			if (currentDistance > dist[currentNode]) {
				continue;
			}
			
			// Explore all neighboring connections
			for (int[] neighbor : adj.get(currentNode)) {
				int neighborNode = neighbor[0];
				int edgeWeight = neighbor[1];
				
				int tentativeDistance = currentDistance + edgeWeight;
				
				// Relaxation - If a shorter path to the neighbor is discovered
				if (tentativeDistance < dist[neighborNode]) {
					dist[neighborNode] = tentativeDistance;
					minHeap.offer(new int[] {tentativeDistance, neighborNode});
				}
			}
		}
		
		return dist;
	}
}
```

### Complexity Analysis

Let $V$ be the number of vertices (nodes) and $E$ be the number of edges.

- **Time Complexity:** $\mathcal{O}((V + E) \log V)$
  - Each vertex is extracted from the priority queue at most once: $\mathcal{O}(V \log V)$.
  - Each edge is relaxed at most once, which can result in pushing an element into the min-heap: $\mathcal{O}(E \log V)$.
  - In a connected graph where $E \geq V$, this simplifies to $\mathcal{O}(E \log V)$.

- **Space Complexity:** $\mathcal{O}(V + E)$
  - $\mathcal{O}(V)$ for the `distances` array and the Priority Queue (which holds at most $V$ valid unique elements at any given point).
  - $\mathcal{O}(V + E)$ to store the graph adjacency list representation.

## References

- Practice - https://www.geeksforgeeks.org/problems/implementing-dijkstra-set-1-adjacency-matrix/1
- Implementation Using PriorityQueue (MinHeap) - https://youtu.be/V6H1qAeB-l4?si=x9XVs4qGgSk8p1DL
- Detailed Complexity Analysis - https://youtu.be/3dINsjyfooY?si=JzrYLR9JvKLHT0Mx