# LeetCode 752. Open the Lock

<p align="right">Last updated - 13.07.2026</p>

- https://leetcode.com/problems/open-the-lock/description/

## Approach: BFS

### Intuition

You are dealing with a 4-wheel combination lock where each wheel can turn from `0` to `9` and wrap around (e.g., `9` turns into `0`, and `0` turns into `9`). Your goal is to find the minimum number of dial turns to reach a `target` combination from `"0000"`, avoiding specified `deadends`.

1. **State Space Tree:** Think of each 4-digit combination as a **node** in a graph. From any combination, you can rotate any of the 4 wheels either forward (+1) or backward (-1). This means each node has exactly **8 neighbors**.
2. **Finding the _Shortest_ Path:** Since you want the _minimum_ number of moves, and moving any wheel by one slot counts as exactly $1$ uniform step, this problem maps directly to finding the shortest path in an unweighted graph.
3. **Why Breadth-First Search (BFS)?** BFS radiates outward layer by layer. It checks all combinations reachable in 1 move, then all in 2 moves, and so on. The moment the BFS algorithm hits the `target`, it is mathematically guaranteed to be the absolute shortest path.

### Algorithm

1. **Edge Cases:** - If the starting position `"0000"` or the `target` itself is in the `deadends` list, it is impossible to unlock it. Return `-1` immediately.
   - If the `target` is already `"0000"`, it requires `0` moves. Return `0`.

2. **Setup Safety Nets:**
   - Convert the `deadends` array into a hash set (`deadSet`) for fast $\mathcal{O}(1)$ validity checks.
   - Maintain a `visited` set (`vis`) to keep track of combinations we have already processed to avoid infinite loops.

3. **Initialize the Queue:**
   - Create a queue to hold structural pairs: `(current_combination, current_move_count)`.
   - Seed it with the starting state: `("0000", 0)`. Mark `"0000"` as visited.

4. **BFS Traversal:**
   While the queue is not empty:
   - Dequeue the front element `(currLock, currMoveCnt)`.
   - **Check Destination:** If `currLock.equals(target)`, return `currMoveCnt`.
   - **Generate 8 Neighbors:** Loop through each of the 4 digit positions ($i = 0$ to $3$):
     - **Forward Rotation:** Shift the character at index $i$ up by 1 (handle wrapping `9` $\rightarrow$ `0`).
     - **Backward Rotation:** Shift the character at index $i$ down by 1 (handle wrapping `0` $\rightarrow$ `9`).
     - For both generated strings: If it is **not** a deadend and **not** visited yet, add it to the `visited` set and enqueue it with `currMoveCnt + 1`.

5. **No Solution:** If the queue runs dry and you haven't returned the target, it's trapped. Return `-1`.

### Implementations

**Java**

```java []
class Solution {
    public int openLock(String[] deadends, String target) {
        Set<String> deadSet = new HashSet<>(Arrays.asList(deadends));

        // Edge Cases
        if (target.equals("0000")) return 0;
        if (deadSet.contains("0000") || deadSet.contains(target)) return -1;

        // Queue holds pairs of: {current combination, move count}
        Queue<Pair> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();

        queue.offer(new Pair("0000", 0));
        visited.add("0000");

        while (!queue.isEmpty()) {
            Pair pair = queue.poll();
            String currLock = pair.lock;
            int currMoveCnt = pair.moveCnt;

            if (currLock.equals(target)) {
                return currMoveCnt;
            }

            // Generate all 8 possible next combinations
            for (int i = 0; i < 4; i++) {
                char[] lockArr = currLock.toCharArray();
                char originalChar = lockArr[i];

                // Option A: Rotate wheel forward (+1)
                char forward = (originalChar == '9') ? '0' : (char) (originalChar + 1);
                lockArr[i] = forward;
                String nextForward = new String(lockArr);
                if (!deadSet.contains(nextForward) && !visited.contains(nextForward)) {
                    visited.add(nextForward);
                    queue.offer(new Pair(nextForward, currMoveCnt + 1));
                }

                // Option B: Rotate wheel backward (-1)
                char backward = (originalChar == '0') ? '9' : (char) (originalChar - 1);
                lockArr[i] = backward;
                String nextBackward = new String(lockArr);
                if (!deadSet.contains(nextBackward) && !visited.contains(nextBackward)) {
                    visited.add(nextBackward);
                    queue.offer(new Pair(nextBackward, currMoveCnt + 1));
                }
            }
        }

        return -1;
    }

    private static class Pair {
        String lock;
        int moveCnt;

        public Pair(String lock, int moveCnt) {
            this.lock = lock;
            this.moveCnt = moveCnt;
        }
    }
}

```

**C++**

```cpp []
class Solution {
public:
    int openLock(vector<string>& deadends, string target) {
        unordered_set<string> deadSet(deadends.begin(), deadends.end());

        // Edge Cases
        if (target == "0000") return 0;
        if (deadSet.count("0000") || deadSet.count(target)) return -1;

        // Queue stores a pair of: {current combination, move count}
        queue<pair<string, int>> q;
        unordered_set<string> visited;

        q.push({"0000", 0});
        visited.insert("0000");

        while (!q.empty()) {
            auto [currLock, currMoveCnt] = q.front();
            q.pop();

            if (currLock == target) {
                return currMoveCnt;
            }

            // Generate all 8 possible next combinations
            for (int i = 0; i < 4; i++) {
                char originalChar = currLock[i];

                // Option A: Rotate wheel forward (+1)
                currLock[i] = (originalChar == '9') ? '0' : originalChar + 1;
                if (!deadSet.count(currLock) && !visited.count(currLock)) {
                    visited.insert(currLock);
                    q.push({currLock, currMoveCnt + 1});
                }

                // Option B: Rotate wheel backward (-1)
                currLock[i] = (originalChar == '0') ? '9' : originalChar - 1;
                if (!deadSet.count(currLock) && !visited.count(currLock)) {
                    visited.insert(currLock);
                    q.push({currLock, currMoveCnt + 1});
                }

                // Revert to original state for the next index iteration
                currLock[i] = originalChar;
            }
        }

        return -1;
    }
};

```

### **Complexity Analysis**

Let $D$ be the number of elements in the `deadends` array. The lock has a maximum constant number of digits ($N = 4$) and base numbers ($B = 10$).

- **Time Complexity:** $\mathcal{O}(B^N + D)$ $\rightarrow$ **$\mathcal{O}(1)$ asymptotically**
  The total possible unique states (combinations) on the lock is strictly bounded by $10^4 = 10,000$. In the absolute worst case, our BFS will visit each combination once. For every combination, we perform a fixed $8$ checks (each taking $\mathcal{O}(4)$ time to construct strings). Since $10,000 \times 8 \times 4$ is a fixed number, the execution time is technically bounded by a constant factor. Initializing the hash set takes $\mathcal{O}(D)$ time, leading to a final scale of $\mathcal{O}(10^4 + D)$.
- **Space Complexity:** $\mathcal{O}(B^N + D)$ $\rightarrow$ **$\mathcal{O}(1)$ asymptotically**
  The `visited` set and the BFS `queue` will store at most $10,000$ unique structural combinations. The `deadSet` stores at most $D$ deadend strings. Thus, the total auxiliary memory required caps out comfortably at a small static constant plus the size of the input constraints.
