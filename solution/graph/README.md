## Graph

<p align="right">Last updated - 07.07.2026</p>

## Topics Covered

- [Introduction to graph](#introduction-to-graph)
- [Graph Representation - Adj Matrix & Adj List](#graphs-representation)
- [What is Connected Components ?](#connected-component)
- [Graph Traversal - DFS & BFS](#graph-traversal)

## Introduction to Graph

A **Graph** is a `non-linear` data structure consisting of a finite set of `vertices (or nodes)` and a set of `edges` that connect these vertices.

Mathematically, a graph $G$ is represented as a pair:

$$G = (V, E)$$

Where:

- $V$ is the set of **Vertices** (nodes).
- $E$ is the set of **Edges** (connections).

### Example

Imagine a mini social network where circles are people and lines are friendships:

```text
   (A) -------- (B)
    |          / |
    |         /  |
    |        /   |
   (C) -----/   (D)

```

In this graph:

- $V = \{A, B, C, D\}$
- $E = \{(A,B), (A,C), (B,C), (B,D)\}$

### Essential Graph Terminologies

Commonly used graph terminlogies

- **Vertex (Node):** The individual data points (e.g., `A`, `B`, `C`).
- **Edge:** The link or path between two vertices.
- **Adjacent Nodes (Neighbors):** Two nodes connected directly by an edge. (e.g., `A` and `B` are adjacent).
- **Degree:** The total number of edges connected to a vertex.
- **In-Degree:** (For directed graphs) Number of incoming edges to a node.
- **Out-Degree:** (For directed graphs) Number of outgoing edges from a node.

- **Path:** A sequence of edges that allows you to travel from one vertex to another (e.g., `A -> B -> D`).
- **Cycle:** A path that starts and ends at the same vertex (e.g., `A -> B -> C -> A`).
- **Self-loop:** An edge that connects a vertex to itself.
- **Parallel Edges:** Two or more edges connecting the same pair of vertices.

### Types of Graphs

Interviewers will often tweak the _type_ of graph to change the problem's constraints. Here are the core types you must know:

#### A. Undirected vs. Directed Graphs

- **Undirected Graph:** Edges have no direction. The relationship is mutual. If `A` is connected to `B`, you can travel both ways.
- **Directed Graph (Digraph):** Edges have arrows indicating a one-way street.

```text
Undirected:                     Directed:
   (A) --------- (B)               (A) --------> (B)
                                    ^           /
                                    |          /
                                    |         /
                                   (C) <-----/

```

#### B. Unweighted vs. Weighted Graphs

- **Unweighted Graph:** All edges are equal. Usually counts as a distance of `1`.
- **Weighted Graph:** Edges have values/costs assigned to them (e.g., representing distance, cost, or time). Essential for Shortest Path problems (Dijkstra’s).

```text
Unweighted:                     Weighted:
   (A) --------- (B)               (A) ----(5)----> (B)
    |                               |
    |                              (12)
    |                               |
    |                               v
   (C)                             (C)

```

#### C. Cyclic vs. Acyclic Graphs

- **Cyclic Graph:** Contains at least one cycle.
- **Acyclic Graph:** Has absolutely no cycles.
  - **DAG (Directed Acyclic Graph):** A highly tested interview concept used for scheduling, dependency resolution, and Topological Sort.

```text
Cyclic:                         Acyclic (DAG):
   (1) --------> (2)               (1) --------> (2)
    ^             |                 |             |
    |             |                 |             |
    |             v                 v             v
   (4) <-------- (3)               (4) --------> (3)

 (Cycle: 1->2->3->4->1)           (No cycles possible)

```

#### D. Connected vs. Disconnected Graphs

- **Connected Graph:** You can reach any vertex from any other vertex.
- **Disconnected Graph:** Has isolated vertices or separate "components". (Often tested: "Find the number of provinces/islands").

```text
Disconnected Graph (3 Components):
   (A)-----(B)      (C)-----(D)       (E)

```

### Key Graph Formulas & Properties

Memorize these for FAANG conceptual or optimization questions:

1. **Sum of Degrees (Handshaking Lemma):**
   In an undirected graph, the sum of degrees of all vertices is exactly twice the number of edges.

$$\sum_{v \in V} \text{deg}(v) = 2 \cdot |E|$$

2. **Maximum Edges in an Undirected Graph:**
   If a graph has $V$ vertices and no self-loops/parallel edges, the maximum possible number of edges is:

$$\frac{V \cdot (V - 1)}{2}$$

3. **Maximum Edges in a Directed Graph:**

$$V \cdot (V - 1)$$

4. **Tree Property:**
   A tree is a special type of graph. A connected graph with $V$ vertices is a tree if and only if it has exactly $V - 1$ edges and no cycles.

## Graphs Representation

You can't pass a graph to a function directly; you have to represent it using basic data structures. FAANG interviewers expect you to know the trade-offs of these two primary representations:

### Example Graph

Let's use a 1-indexed, undirected graph with $N = 4$ vertices and $M = 4$ edges.

**Edges:** `(1, 2)`, `(1, 3)`, `(2, 4)`, `(3, 4)`

```text
   (1) --------- (2)
    |             |
    |             |
    |             |
   (3) --------- (4)

```

### 1. Adjacency Matrix

An adjacency matrix is a 2D array of size $(N + 1) \times (N + 1)$ because we are using 1-based indexing **or** $(V \times V)$ if 0-based. `matrix[i][j] = 1` means there is an edge between node $i$ and node $j$.

- **Space Complexity:** $O(V^2)$
- **Time to check if edge exists ($u \to v$):** $O(1)$
- **Best used for:** Dense graphs (where $E$ is close to $V^2$).

#### Implementation

```java []
public class Main {
    public static void main(String[] args) {
        // Hardcoded example values based on our scenario
        int n = 4; // Vertices
        int m = 4; // Edges

        // 1-based indexing means we need a size of (n + 1)
        int[][] adjMatrix = new int[n + 1][n + 1];

        // Given edges
        int[][] edges = { {1, 2}, {1, 3}, {2, 4}, {3, 4} };

        // Populate the matrix
        for (int i = 0; i < m; i++) {
            int u = edges[i][0];
            int v = edges[i][1];

            adjMatrix[u][v] = 1;
            adjMatrix[v][u] = 1; // Omit this line if the graph is Directed
        }

        // Visualizing the matrix row by row (from index 1 to n)
        System.out.println("Adjacency Matrix:");
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                System.out.print(adjMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
```

#### Output Matrix Visualization

```text
Adjacency Matrix:
0 1 1 0
1 0 0 1
1 0 0 1
0 1 1 0
```

### 2. Adjacency List (Most Preferable in Interview)

An adjacency list is an array of lists (or an `ArrayList` of `ArrayList`s in Java). Each index $i$ represents a node, and the list at that index contains all its neighbor nodes.

- **Space Complexity:** $O(V + E)$
- **Time to check if edge exists ($u \to v$):** $O(\text{degree}(u))$
- **Best used for:** Sparse graphs (most real-world interview problems).

#### Implementation

```java
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int n = 4; // Vertices
        int m = 4; // Edges

        // Create an outer list of size (n + 1) for 1-based indexing
        ArrayList<ArrayList<Integer>> adjList = new ArrayList<>();

        // Initialize the internal lists
        for (int i = 0; i <= n; i++) {
            adjList.add(new ArrayList<>());
        }

        // Given edges
        int[][] edges = { {1, 2}, {1, 3}, {2, 4}, {3, 4} };

        // Populate the list
        for (int i = 0; i < m; i++) {
            int u = edges[i][0];
            int v = edges[i][1];

            adjList.get(u).add(v);
            adjList.get(v).add(u); // Omit this line if the graph is Directed
        }

        // Visualizing the list
        System.out.println("Adjacency List:");
        for (int i = 1; i <= n; i++) {
            System.out.print("Node " + i + " is connected to: ");
            System.out.println(adjList.get(i));
        }
    }
}
```

#### Output List Visualization

```text
Node 1 is connected to: [2, 3]
Node 2 is connected to: [1, 4]
Node 3 is connected to: [1, 4]
Node 4 is connected to: [2, 3]
```

### Trade-Offs Analysis

When an interviewer asks, _"Why did you choose an Adjacency List over a Matrix?"_, here is how you answer cleanly using Big-O:

| Operation / Feature                  | Adjacency Matrix             | Adjacency List        | Winner                                 |
| ------------------------------------ | ---------------------------- | --------------------- | -------------------------------------- |
| **Space Complexity**                 | $O(N^2)$                     | $O(N + M)$            | **Adjacency List** (for sparse graphs) |
| **Check if Edge ($u \to v$) Exists** | $O(1)$                       | $O(\text{degree}(u))$ | **Adjacency Matrix**                   |
| **Find all neighbors of node $u$**   | $O(N)$ (must scan whole row) | $O(\text{degree}(u))$ | **Adjacency List**                     |
| **Add a new edge**                   | $O(1)$                       | $O(1)$                | **Tie**                                |

### Which one should you choose in an interview?

1. **Choose Adjacency List 95% of the time.** Most interview problems are **sparse** (where the number of edges $M$ is much smaller than $N^2$). Algorithms like BFS, DFS, and Dijkstra run faster on adjacency lists ($O(N+M)$ vs $O(N^2)$) because you don't waste time checking pairs of nodes that aren't connected.

2. **Choose Adjacency Matrix only if:**
   - $N$ is very small (e.g., $N \le 100$).
   - The graph is **dense** ($M \approx N^2$).
   - You frequently need to check if a specific edge exists between two nodes instantly.
   - You are implementing Floyd-Warshall's All-Pairs Shortest Path algorithm.

## Connected Component

In graph theory, a **Connected Component** (often just called a component) is a maximal subgraph in which **every pair of vertices is reachable from each other via a path**, and it is not connected to any additional vertices in the rest of the graph.

Think of it like a social network: it’s a group of people who are all connected to each other either directly or through mutual friends, but they have absolutely no connections to anyone outside of their group.

### Visualizing Connected Components

Consider an undirected graph with **7 vertices** and **5 edges**:

```text
Component 1:          Component 2:       Component 3:

   (1) --- (2)           (4) --- (5)          (7)
    |                     |
   (3)                   (6)

```

In this graph, there are **3 distinct Connected Components**:

1. **First Component:** Contains nodes `{1, 2, 3}`. You can travel from any of these nodes to another.
2. **Second Component:** Contains nodes `{4, 5, 6}`.
3. **Third Component:** Contains only node `{7}`. A single isolated node with no edges is still considered a valid connected component of size 1.

### Directed Graphs: A Key Distinction

If you are dealing with a **Directed Graph** (where edges have arrows), the concept splits into two types, which FAANG interviewers love to test:

#### 1. Weakly Connected Components

If you change all the directed arrows into undirected lines and the graph becomes a single connected component, it is **weakly connected**. The directions might block you from traveling everywhere, but the underlying structure is joined.

#### 2. Strongly Connected Components (SCC)

A directed graph is **strongly connected** if you can travel from _any_ node to _every other_ node following the direction of the arrows.

```text
Strongly Connected:              Not Strongly Connected:
   (A) <======> (B)                 (A) -------> (B)

```

_(In the right example, you can go from A to B, but you are trapped at B and can never get back to A. This is NOT strongly connected)._

### Why is this important for Coding Interviews?

"Find the number of connected components" is one of the most common core patterns in graph interviews. It is rarely asked directly; instead, it's disguised as real-world scenarios:

- **LeetCode 200 - Number of Islands:** You are given a grid of `'1'`s (land) and `'0'`s (water). You need to find the number of islands. Each island is a connected component of land.
- **Friend Circles / Provinces:** Given a matrix of who is friends with whom, find how many isolated friend groups exist.
- **Network Redundancy:** Finding if a network switch failure will break a system into multiple disconnected pieces.

### How do you find them in code?

You can find the number of connected components easily by running a **DFS (Depth-First Search)** or **BFS (Breadth-First Search)** combined with a `visited` array:

1. Initialize a `count = 0`.
2. Loop through every node from `1` to `N`.
3. If a node has **not** been visited yet, it means you've discovered a _new_ connected component!
4. Increment your `count` and run a DFS/BFS from that node to mark **all** of its reachable neighbors as visited.
5. Repeat until all nodes are visited. Your `count` will equal the total number of connected components.

## Graph Traversal

Graph traversal means visiting every vertex and edge in a graph in a systematic order. Because graphs don't have a single "root" (like trees do) and can contain cycles, we must use a **`visited` array/set** during traversal. This prevents us from getting stuck in infinite loops.

The two foundational traversal algorithms you must know are **Depth-First Search (DFS)** and **Breadth-First Search (BFS)**.

### [1. Depth-First Search (DFS)](/solution/graph/dfs/)

DFS focuses on going **as deep as possible** along each branch before backtracking. Think of it like exploring a maze: you pick a path, walk until you hit a dead end, and then back up to the last intersection to try the next path.

#### How it Works (Theory)

- Uses a **Stack** data structure (implicitly via **Recursion**).
- **Strategy**
  1. Start at a source node and mark it as visited.
  2. Move to an unvisited neighbor.
  3. Repeat process recursively for this neighbor.
  4. When you hit a node with no unvisited neighbors, backtrack to the previous node and look for other unvisited neighbors.

#### Walkthrough Example

Given a graph where Node 1 connects to 2 and 3, and Node 2 connects to 4:

```text
     (1)
    /   \
  (2)   (3)
   |
  (4)

```

- **DFS Traversal Order:** `1 -> 2 -> 4 -> 3` (or `1 -> 3 -> 2 -> 4`)

### Complexity & Use Cases

- **Time Complexity:** $O(V + E)$ — Every node and edge is evaluated.
- **Space Complexity:** $O(V)$ — Due to the recursion stack.
- **Best used for:** \* Pathfinding where any path will do.
  - Detecting cycles in a graph.
  - Topological Sorting (ordering tasks with dependencies).
  - Solving puzzles (like mazes) where you need to check all combinations.

### [2. Breadth-First Search (BFS)](/solution/graph/bfs/)

BFS focuses on going **as wide as possible** before going deeper. It explores the graph layer by layer, visiting all immediate neighbors first, then moving to the neighbors' neighbors. Think of it like a ripple in a pond spreading outward from a central point.

#### How it Works (Theory)

- Uses a **Queue** data structure (First-In, First-Out).
- **Strategy:**
  1. Start at a source node, mark it as visited, and push it into the queue.
  2. Pull the front node out of the queue and look at all its unvisited neighbors.
  3. Mark those neighbors as visited and push them into the queue.
  4. Repeat until the queue is completely empty.

#### Walkthrough Example

Using the same graph from above:

```text
     (1)
    /   \
  (2)   (3)
   |
  (4)

```

- **BFS Traversal Order:** `1 -> 2 -> 3 -> 4`
  _(Notice how it completely finishes the immediate layer `{2, 3}` before stepping down to `{4}`)_.

### Complexity & Use Cases

- **Time Complexity:** $O(V + E)$ — Every node and edge is evaluated.
- **Space Complexity:** $O(V)$ — Storing nodes in the queue.
- **Best used for:**
  - **Finding the shortest path** in an unweighted graph (because it explores layer-by-layer, the first time you reach a target, it's guaranteed to be via the shortest number of steps).
  - Peer-to-peer networks or web crawlers looking for nearby nodes.
  - Finding the "level" or distance of nodes from a starting source.

### Quick ÍComparison

| Feature                | DFS                                           | BFS                                       |
| ---------------------- | --------------------------------------------- | ----------------------------------------- |
| **Data Structure**     | Stack / Recursion                             | Queue (FIFO)                              |
| **Mindset**            | Go deep first, backtrack later.               | Go wide first, layer by layer.            |
| **Primary Superpower** | Cycle detection & connectivity.               | Shortest path (unweighted graphs).        |
| **Memory**             | Efficient for deep graphs; bad for wide ones. | Memory-heavy for highly connected graphs. |
