# LeetCode 841. Keys and Rooms

<p align="right">Last updated - 14.07.2026</p>

- https://leetcode.com/problems/keys-and-rooms/description/

## Approach: DFS

### Intuition

Imagine you are standing in front of $N$ locked rooms, numbered from `0` to `n - 1`. Room `0` is already unlocked, and inside every room, there are distinct keys left on the table that open other rooms. Your objective is to determine if it is possible to visit every single room.

1. **Modeling as a Graph:** This problem maps perfectly onto a **Directed Graph**.
   - Each room represents a **node**.
   - A key inside room `A` that opens room `B` represents a **directed edge** from `A` to `B`.

2. **The Goal:** Starting at node `0`, can we traverse the graph such that we visit every single node? In graph terminology, this is a classic **reachability problem**.
3. **Graph Traversal Strategy:** Both Depth-First Search (DFS) and Breadth-First Search (BFS) work excellently here. DFS allows us to pick a key, walk into that room, pick up its keys, and immediately venture as deep as possible into subsequent rooms before backtracking. If our tracking system shows we visited all $N$ nodes by the end of the traversal, the answer is `true`.

### Algorithm

1. **Track Visited Rooms:** Maintain a tracking mechanism (like a `visited` boolean array or hash set) of size $N$ to log which rooms have been entered. This prevents infinite loops if keys form a cyclic chain (e.g., Room 1 contains the key to Room 2, and Room 2 contains the key to Room 1).
2. **Start DFS from Room 0:** - Mark Room `0` as visited.
   - Look at all the keys inside Room `0`.

3. **Recursive Deep Dive (DFS Function):** For each key found in the current room:
   - If the room corresponding to that key has **not** been visited yet:
     - Mark that room as visited.
     - Recursively invoke the DFS function for this new room to explore its keys.

4. **Final Check:** After the traversal finishes, loop through your tracking mechanism. If there is even one room that remains unvisited, return `false`. If all rooms are visited, return `true`.

### Implementations

**Java**

```java []
class Solution {
    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
        int n = rooms.size();
        boolean[] visited = new boolean[n];

        // Start our depth-first exploration from Room 0
        dfs(0, rooms, visited);

        // If any room remains unvisited, return false
        for (boolean isVisited : visited) {
            if (!isVisited) {
                return false;
            }
        }

        return true;
    }

    private void dfs(int currentRoom, List<List<Integer>> rooms, boolean[] visited) {
        // Mark the current room as visited
        visited[currentRoom] = true;

        // Grab all keys available in the current room
        for (int key : rooms.get(currentRoom)) {
            // If we haven't visited the room that this key unlocks, go visit it
            if (!visited[key]) {
                dfs(key, rooms, visited);
            }
        }
    }
}

```

**C++**

```cpp []
class Solution {
public:
    bool canVisitAllRooms(vector<vector<int>>& rooms) {
        int n = rooms.size();
        vector<bool> visited(n, false);

        // Start our depth-first exploration from Room 0
        dfs(0, rooms, visited);

        // If any room remains unvisited, return false
        for (bool isVisited : visited) {
            if (!isVisited) {
                return false;
            }
        }

        return true;
    }

private:
    void dfs(int currentRoom, const vector<vector<int>>& rooms, vector<bool>& visited) {
        // Mark the current room as visited
        visited[currentRoom] = true;

        // Grab all keys available in the current room
        for (int key : rooms[currentRoom]) {
            // If we haven't visited the room that this key unlocks, go visit it
            if (!visited[key]) {
                dfs(key, rooms, visited);
            }
        }
    }
};

```

### Complexity Analysis

Let $V$ be the total number of rooms (Vertices) and $E$ be the total number of keys across all rooms combined (Edges).

- **Time Complexity:** $\mathcal{O}(V + E)$
  The algorithm will visit each room at most once due to the safety check (`!visited[key]`). When inside a room, we iterate through all of its available keys. Thus, every vertex and every edge in the graph representation is processed exactly once, yielding a linear runtime.
- **Space Complexity:** $\mathcal{O}(V)$
  The memory consumption is driven by two factors:
  1. The tracking structure (`visited` array), which requires size $V$.
  2. The implicit execution call stack during recursion. In the worst-case scenario (e.g., Room 0 opens Room 1, which opens Room 2, etc., in a long linear chain), the stack depth will reach $V$. Thus, the total auxiliary space scales linearly with the number of rooms.
