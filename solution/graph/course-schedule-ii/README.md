# [210. Course Schedule II](https://leetcode.com/problems/course-schedule-ii/description/)

<p align="right">Last updated - 14.07.2026</p>

## Approach: Kahn's Algorithm (BFS-based Topological Sort)

### Intuition

This problem is a direct implementation of **Topological Sorting**.

- We represent the courses as nodes in a graph.
- If a prerequisite exists such that you must take course $u$ before course $v$ (given as `[v, u]` in the input), we draw a directed edge from $u \rightarrow v$.
- The **InDegree** of a node represents how many prerequisites are remaining for that course.
- A course with an InDegree of `0` has no remaining prerequisites and can be taken immediately.
- By processing courses with `0` InDegree, updating their dependent neighbors, and adding newly freed courses (InDegree = 0) to our processing queue, we naturally construct a valid schedule. If our final list size is less than `numCourses`, we have encountered a cycle, meaning no valid schedule exists.

### Algorithm

1. Create an adjacency list `adj` of size `numCourses` and an `inDegree` array of size `numCourses`.
2. For each prerequisite edge `[course, prereq]`:
   - Add a directed edge from `prereq` to `course` in `adj`.
   - Increment `inDegree[course]` by 1.

3. Initialize a queue `queue` and push all courses that have `inDegree == 0`.
4. Create an array `order` of size `numCourses` and an iterator `it = 0` to record our schedule.
5. While the queue is not empty:
   - Poll a node from the queue.
   - Assign `order[it++] = node`.
   - For each neighbor of this node, decrement its InDegree by 1.
   - If a neighbor's InDegree drops to 0, push it into the queue.

6. If `it == numCourses` (meaning we successfully scheduled all courses), return `order`. Otherwise, return an empty array `[]`.

### Implementation

**Java**

```java
import java.util.*;

class Solution {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        // Step 1: Build the dependency graph and compute InDegrees
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adj.add(new ArrayList<>());
        }

        int[] inDegree = new int[numCourses];
        for (int[] edge : prerequisites) {
            int course = edge[0];
            int prereq = edge[1];
            adj.get(prereq).add(course); // prereq -> course
            inDegree[course]++;          // Increment InDegree of the dependent course
        }

        // Step 2: Add all courses with 0 prerequisites to the queue
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        // Step 3: Process the BFS queue to build the order
        int[] order = new int[numCourses];
        int it = 0;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            order[it++] = node;

            // Reduce InDegree for all adjacent neighbors
            for (int nei : adj.get(node)) {
                inDegree[nei]--;
                // If InDegree becomes 0, add neighbor to queue
                if (inDegree[nei] == 0) {
                    queue.add(nei);
                }
            }
        }

        // Step 4: If all courses are processed, return order; else return empty array
        return it == numCourses ? order : new int[0];
    }
}

```

**C++**

```cpp
#include <vector>
#include <queue>

using namespace std;

class Solution {
public:
    vector<int> findOrder(int numCourses, vector<vector<int>>& prerequisites) {
        // Step 1: Build the dependency graph and compute InDegrees
        vector<vector<int>> adj(numCourses);
        vector<int> inDegree(numCourses, 0);

        for (const auto& edge : prerequisites) {
            int course = edge[0];
            int prereq = edge[1];
            adj[prereq].push_back(course); // prereq -> course
            inDegree[course]++;            // Increment InDegree of the dependent course
        }

        // Step 2: Add all courses with 0 prerequisites to the queue
        queue<int> que;
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                que.push(i);
            }
        }

        // Step 3: Process the BFS queue to build the order
        vector<int> order;
        while (!que.empty()) {
            int node = que.front();
            que.pop();
            order.push_back(node);

            // Reduce InDegree for all adjacent neighbors
            for (int nei : adj[node]) {
                inDegree[nei]--;
                // If InDegree becomes 0, add neighbor to queue
                if (inDegree[nei] == 0) {
                    que.push(nei);
                }
            }
        }

        // Step 4: If all courses are processed, return order; else return empty vector
        if (order.size() == numCourses) {
            return order;
        }
        return {};
    }
};

```

### Dry Run

Let's trace with a sample test case:

- **Input:** `numCourses = 4`, `prerequisites = [[1,0],[2,0],[3,1],[3,2]]`
- **Adjacency List:**
  - `0 -> [1, 2]`
  - `1 -> [3]`
  - `2 -> [3]`
  - `3 -> []`

- **Initial InDegrees:** `[0: 0, 1: 1, 2: 1, 3: 2]`

#### Execution:

1. **Queue Setup:** Initial nodes with `inDegree == 0` is `[0]`. `queue = [0]`, `it = 0`.
2. **Iteration 1:** - Poll `0`. `order[0] = 0` (Increment `it` to 1).
   - Decrement `inDegree` of neighbors `1` and `2`:
     - `inDegree[1]` becomes 0 $\rightarrow$ push `1`.
     - `inDegree[2]` becomes 0 $\rightarrow$ push `2`.
   - `queue = [1, 2]`.

3. **Iteration 2:**
   - Poll `1`. `order[1] = 1` (Increment `it` to 2).
   - Decrement neighbor `3`: `inDegree[3]` becomes 1.
   - `queue = [2]`.

4. **Iteration 3:**
   - Poll `2`. `order[2] = 2` (Increment `it` to 3).
   - Decrement neighbor `3`: `inDegree[3]` becomes 0 $\rightarrow$ push `3`.
   - `queue = [3]`.

5. **Iteration 4:**
   - Poll `3`. `order[3] = 3` (Increment `it` to 4).
   - No neighbors. Queue becomes empty.

_Result:_ `it (4) == numCourses (4)`. Return `order = [0, 1, 2, 3]` (or `[0, 2, 1, 3]`, which is also a valid output!).

### Complexity Analysis

- **Time Complexity:** $\mathcal{O}(V + E)$ where $V = \text{numCourses}$ and $E = \text{prerequisites.length}$. We process each vertex and its incident edges exactly once.
- **Space Complexity:** $\mathcal{O}(V + E)$ for storing the adjacency list representation of the graph, alongside $\mathcal{O}(V)$ space for the tracking structures (`inDegree` array, tracking list, and BFS queue).
