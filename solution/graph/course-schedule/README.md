# [207. Course Schedule](https://leetcode.com/problems/course-schedule/description/)

<p align="right">Last updated - 14.07.2026</p>

## Approach: Kahn's Algorithm (BFS-based Cycle Detection)

### Intuition

Think of each course as a node in a directed graph. A prerequisite requirement `[a, b]` means you must take course `b` before course `a`. This creates a directed edge from `b \rightarrow a`.

Using Kahn's Algorithm, we keep track of the **InDegree** (the number of prerequisites) for each course.

1. Any course with an InDegree of `0` has no prerequisites and can be taken immediately.
2. When we "take" a course, we remove it from the graph, meaning it no longer acts as a prerequisite for other courses. We decrement the InDegree of all courses that depended on it.
3. If any of those dependent courses now have an InDegree of `0`, they are ready to be taken, so we add them to our processing queue.
4. If we manage to process all `numCourses`, it means the graph is a Directed Acyclic Graph (DAG) and we can finish all courses. If we get stuck and some courses are left unprocessed, a cycle exists.

> **Note on your code:** Your provided code maps the edge as `edge[0] -> edge[1]`. While reversing all edges still accurately detects the _existence_ of a cycle, mapping it as `edge[1] -> edge[0]` (Prerequisite $\rightarrow$ Course) is the standard, chronologically intuitive way to model dependencies.

### Algorithm

1. Create an adjacency list `adj` and an `inDegree` array of size `numCourses`.
2. For each pair `[course, prereq]` in `prerequisites`:
   - Add an edge from `prereq` to `course` in `adj`.
   - Increment `inDegree[course]` by 1.

3. Initialize a queue and push all courses that have `inDegree == 0`.
4. Initialize a counter `visitedCount = 0` to track how many courses we successfully process.
5. While the queue is not empty:
   - Poll a course from the queue and increment `visitedCount`.
   - For every dependent neighbor of this course, decrement its InDegree by 1.
   - If a neighbor's InDegree drops to 0, push it into the queue.

6. Return `visitedCount == numCourses`.

### Implementation

**Java**

```java
import java.util.*;

class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // Step 1: Build the dependency graph (prereq -> course)
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adj.add(new ArrayList<>());
        }

        int[] inDegree = new int[numCourses];
        for (int[] edge : prerequisites) {
            int course = edge[0];
            int prereq = edge[1];
            adj.get(prereq).add(course); // prereq must be taken before course
            inDegree[course]++;          // course depends on prereq
        }

        // Step 2: Push all courses with 0 prerequisites into the queue
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        // Step 3: Process courses using BFS
        int visitedCount = 0;
        while (!queue.isEmpty()) {
            int current = queue.poll();
            visitedCount++;

            // Reduce InDegree for all courses depending on the current course
            for (int neighbor : adj.get(current)) {
                inDegree[neighbor]--;
                // If all prerequisites for this neighbor are met
                if (inDegree[neighbor] == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // If we visited all courses, no cycle exists
        return visitedCount == numCourses;
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
    bool canFinish(int numCourses, vector<vector<int>>& prerequisites) {
        // Step 1: Build the dependency graph (prereq -> course)
        vector<vector<int>> adj(numCourses);
        vector<int> inDegree(numCourses, 0);

        for (const auto& edge : prerequisites) {
            int course = edge[0];
            int prereq = edge[1];
            adj[prereq].push_back(course); // prereq must be taken before course
            inDegree[course]++;            // course depends on prereq
        }

        // Step 2: Push all courses with 0 prerequisites into the queue
        queue<int> que;
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                que.push(i);
            }
        }

        // Step 3: Process courses using BFS
        int visitedCount = 0;
        while (!que.empty()) {
            int current = que.front();
            que.pop();
            visitedCount++;

            // Reduce InDegree for all courses depending on the current course
            for (int neighbor : adj[current]) {
                inDegree[neighbor]--;
                // If all prerequisites for this neighbor are met
                if (inDegree[neighbor] == 0) {
                    que.push(neighbor);
                }
            }
        }

        // If we visited all courses, no cycle exists
        return visitedCount == numCourses;
    }
};

```

### Dry Run

#### Case 1: Valid Scenario (No Cycle)

_Input:_ `numCourses = 3`, `prerequisites = [[1, 0], [2, 1]]` (To take 1 take 0; To take 2 take 1).

- **Graph:** `0 -> 1 -> 2`
- **Initial InDegree:** `[0: 0, 1: 1, 2: 1]`
- **Queue Initialization:** Node `0` has InDegree 0. `queue = [0]`, `visitedCount = 0`.

- **Iteration 1:** Poll `0`. `visitedCount = 1`. Neighbor is `1`. Decrement `inDegree[1]` to 0. Push `1`. `queue = [1]`.
- **Iteration 2:** Poll `1`. `visitedCount = 2`. Neighbor is `2`. Decrement `inDegree[2]` to 0. Push `2`. `queue = [2]`.
- **Iteration 3:** Poll `2`. `visitedCount = 3`. No neighbors. Queue empty.

_Conclusion:_ `visitedCount (3) == numCourses (3)` $\rightarrow$ Returns **`true`**.

#### Case 2: Invalid Scenario (Cycle Present)

_Input:_ `numCourses = 2`, `prerequisites = [[1, 0], [0, 1]]` (To take 1 take 0; To take 0 take 1).

- **Graph:** `0 <-> 1` (Cycle)
- **Initial InDegree:** `[0: 1, 1: 1]`
- **Queue Initialization:** No node has InDegree 0! `queue = []`, `visitedCount = 0`.

_The loop never runs._

_Conclusion:_ `visitedCount (0) == numCourses (2)` is false $\rightarrow$ Returns **`false`**.

### Complexity Analysis

- **Time Complexity:** $\mathcal{O}(V + E)$ where $V = \text{numCourses}$ and $E = \text{prerequisites.length}$. We look at every node once when clearing the queue and process every dependency edge exactly once.
- **Space Complexity:** $\mathcal{O}(V + E)$ to build and store the adjacency list graph representation, alongside $\mathcal{O}(V)$ space for the `inDegree` array and the BFS queue.
