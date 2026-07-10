# 127. Word Ladder

<p align="right">Last updated - 10.07.2026</p>

- https://leetcode.com/problems/word-ladder/description/

## Approach: Breadth-First Search (BFS)

### Intuition

The problem asks for the **shortest transformation sequence** from a `beginWord` to an `endWord` by changing one character at a time, where each intermediate word must exist in a given dictionary (`wordList`).

This can be modeled as an **unweighted graph problem**:

- **Nodes/Vertices:** Every unique word in the dictionary (and the `beginWord`).
- **Edges:** An undirected edge exists between two words if they differ by exactly **one** character.
- **Goal:** Find the shortest path from the start node (`beginWord`) to the destination node (`endWord`).

For finding the shortest path in an unweighted graph, **Breadth-First Search (BFS)** is the optimal choice. BFS explores the graph level-by-level (or step-by-step). The very first time we reach the `endWord`, we are guaranteed to have found the shortest path.

### Key Optimizations:

1. **Finding Neighbors Efficiently:** Instead of comparing the current word with every single word in the dictionary (which takes $O(N \times L)$ time, where $N$ is the number of words and $L$ is the length of a word), we alter each character of the current word from `'a'` to `'z'` ($O(26 \times L)$). If the mutated word exists in our dictionary look-up set, it's a valid neighbor.
2. **Avoiding Cycles (Visited Tracking):** To prevent the algorithm from trapped in an infinite cycle (e.g., `"hit"` $\rightarrow$ `"hot"` $\rightarrow$ `"hit"`), we must track visited words. A highly efficient way to do this without allocating extra memory for a separate `visited` set is to simply **remove the word from the dictionary `HashSet**` as soon as it is enqueued.

### Algorithm

1. Initialize a `HashSet` containing all words from `wordList` for $O(1)$ fast lookups.
2. If `endWord` is not present in the set, a valid transformation sequence is impossible; return `0`.
3. Initialize a `Queue` for BFS. Each entry in the queue will store a pair/structure consisting of the `currentWord` and the current path length `steps`.
4. Push the `beginWord` into the queue with a step count of `1`, and remove it from the set to mark it visited.
5. While the queue is not empty:
   - Pop the front element `(currWord, currSteps)`.
   - If `currWord` is equal to `endWord`, return `currSteps`.
   - Iterate through each character position $i$ of `currWord` from $0$ to $L-1$:
     - Save the original character.
     - Replace it with every character from `'a'` to `'z'`.
     - Convert the modified character array back to a string (`newWord`).
     - If `newWord` exists in the set:
       - Push `(newWord, currSteps + 1)` into the queue.
       - Remove `newWord` from the set to prevent revisiting.

     - Restore the original character before moving to the next position.

6. If the queue becomes empty and the `endWord` was never reached, return `0`.

### Implementations

**Java**

```java
class Solution {
    // Helper class to store word and its distance from the beginWord
    private static class Pair {
        String word;
        int steps;

        Pair(String word, int steps) {
            this.word = word;
            this.steps = steps;
        }
    }

    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        // Convert list to set for O(1) lookups
        HashSet<String> set = new HashSet<>(wordList);

        // If target word is not in the dictionary, no sequence can be formed
        if (!set.contains(endWord)) {
            return 0;
        }

        // Queue for BFS storing Pair(word, current_sequence_length)
        Queue<Pair> queue = new ArrayDeque<>();
        queue.offer(new Pair(beginWord, 1));

        // Mark the start word as visited by removing it from the set
        set.remove(beginWord);

        while (!queue.isEmpty()) {
            Pair curr = queue.poll();
            String currWord = curr.word;
            int currSteps = curr.steps;

            // Since it's BFS, the first time we hit endWord, it's the shortest path
            if (currWord.equals(endWord)) {
                return currSteps;
            }

            char[] charArr = currWord.toCharArray();
            // Try changing each character position
            for (int i = 0; i < charArr.length; i++) {
                char originalChar = charArr[i];

                // Try all 26 possible lowercase English alphabets
                for (char c = 'a'; c <= 'z'; c++) {
                    charArr[i] = c;
                    String newWord = String.valueOf(charArr);

                    // If the mutated word exists in the dictionary
                    if (set.contains(newWord)) {
                        queue.offer(new Pair(newWord, currSteps + 1));
                        // Remove from set to mark it as visited
                        set.remove(newWord);
                    }
                }
                // Restore the original character for the next position's transformations
                charArr[i] = originalChar;
            }
        }

        // If endWord is unreachable
        return 0;
    }
}
```

**C++**

```cpp
class Solution {
public:
    int ladderLength(string beginWord, string endWord, vector<string>& wordList) {
        // Convert vector to unordered_set for O(1) average lookup time
        unordered_set<string> wordSet(wordList.begin(), wordList.end());

        // If the target word isn't in the list, transition is impossible
        if (wordSet.find(endWord) == wordSet.end()) {
            return 0;
        }

        // Queue for BFS storing pair of {current_word, current_steps}
        queue<pair<string, int>> que;
        que.push({beginWord, 1});

        // Mark the beginWord as visited by removing it from the set
        wordSet.erase(beginWord);

        while (!que.empty()) {
            auto [currWord, currSteps] = que.front();
            que.pop();

            // BFS ensures the first match found provides the minimum steps
            if (currWord == endWord) {
                return currSteps;
            }

            // Generate all possible adjacent words (differing by 1 character)
            for (int i = 0; i < currWord.length(); i++) {
                char originalChar = currWord[i];

                for (char c = 'a'; c <= 'z'; c++) {
                    currWord[i] = c;

                    // Check if the modified word exists in the remaining dictionary
                    if (wordSet.find(currWord) != wordSet.end()) {
                        que.push({currWord, currSteps + 1});
                        // Remove from set to prevent infinite cycles
                        wordSet.erase(currWord);
                    }
                }
                // Backtrack to the original character before inspecting the next index
                currWord[i] = originalChar;
            }
        }

        return 0;
    }
};

```

### Dry-run

**The Setup**

- **Inputs:**
  - `beginWord = "hit"`
  - `endWord = "cog"`
  - `wordList = ["hot","dot","dog","lot","log","cog"]`

- **Initialization:**
  - `set = {"hot", "dot", "dog", "lot", "log", "cog"}` (Initialized with `wordList`)
  - `que = []`
  - `set.remove(beginWord)` $\rightarrow$ `"hit"` is not in the set anyway, so `set` remains unchanged.
  - `que.offer(new Pair("hit", 1))` $\rightarrow$ `que = [("hit", 1)]`

**The Dry Run Execution**

#### **Iteration 1**

- **Queue State:** `[("hit", 1)]`
- `que.poll()` extracts: `currWord = "hit"`, `currCnt = 1`.
- **Check:** Does `"hit".equals("cog")`? No.
- **Looping through characters of `"hit"`:**
  - **$i = 0$ (Modify 'h'):** Generates `"ait"`, `"bit"`, ..., `"zit"`. None of these are in `set`.
  - **$i = 1$ (Modify 'i'):** Generates `"hat"`, `"hbt"`, ..., `"hot"`, ..., `"hzt"`.
    - When `newWord = "hot"`: `set.contains("hot")` is **True**!
    - `que.offer(new Pair("hot", 2))` $\rightarrow$ `que = [("hot", 2)]`
    - `set.remove("hot")` $\rightarrow$ **Crucial Step!** `set` is now `{"dot", "dog", "lot", "log", "cog"}`.

  - **$i = 2$ (Modify 't'):** Generates `"hia"`, `"hib"`, ..., `"hiz"`. None are in `set`.

#### **Iteration 2**

- **Queue State:** `[("hot", 2)]`
- `que.poll()` extracts: `currWord = "hot"`, `currCnt = 2`.
- **Check:** Does `"hot".equals("cog")`? No.
- **Looping through characters of `"hot"`:**
  - **$i = 0$ (Modify 'h'):** Generates `"aot"`, `"bot"`, ..., `"dot"`, ..., `"lot"`, ..., `"zot"`.
    - When `newWord = "dot"`: `set.contains("dot")` is **True**!
      - `que.offer(new Pair("dot", 3))`
      - `set.remove("dot")` $\rightarrow$ `set` becomes `{"dog", "lot", "log", "cog"}`.

    - When `newWord = "lot"`: `set.contains("lot")` is **True**!
      - `que.offer(new Pair("lot", 3))`
      - `set.remove("lot")` $\rightarrow$ `set` becomes `{"dog", "log", "cog"}`.

- **$i = 1$ (Modify 'o'):** Generates `"hat"`, `"hbt"`, etc.
  - _Note:_ It generates `"hit"`, but `"hit"` is not in the set. If we had skipped step 1, it might have tried to check `"hot"` again, but `"hot"` was already deleted from the set in Iteration 1!

- **$i = 2$ (Modify 't'):** Generates `"hoa"`, `"hob"`, etc. None are in `set`.

- **End of Iteration 2 Queue State:** `[("dot", 3), ("lot", 3)]`

#### **Iteration 3**

- **Queue State:** `[("dot", 3), ("lot", 3)]`
- `que.poll()` extracts: `currWord = "dot"`, `currCnt = 3`.
- **Check:** Does `"dot".equals("cog")`? No.
- **Looping through characters of `"dot"`:**
  - **$i = 0$ (Modify 'd'):** Generates `"aot"`, `"bot"`, ..., `"hot"` (not in set anymore), `"lot"` (not in set anymore).
  - **$i = 1$ (Modify 'o'):** Generates `"dat"`, `"dbt"`, etc. None in `set`.
  - **$i = 2$ (Modify 't'):** Generates `"doa"`, `"dob"`, ..., `"dog"`, ..., `"doz"`.
    - When `newWord = "dog"`: `set.contains("dog")` is **True**!
      - `que.offer(new Pair("dog", 4))`
      - `set.remove("dog")` $\rightarrow$ `set` becomes `{"log", "cog"}`.

- **End of Iteration 3 Queue State:** `[("lot", 3), ("dog", 4)]`

#### **Iteration 4**

- **Queue State:** `[("lot", 3), ("dog", 4)]`
- `que.poll()` extracts: `currWord = "lot"`, `currCnt = 3`.
- **Check:** Does `"lot".equals("cog")`? No.
- **Looping through characters of `"lot"`:**
  - **$i = 0$ (Modify 'l'):** Generates `"aot"`, `"bot"`, etc.
  - **$i = 1$ (Modify 'o'):** Generates `"lat"`, `"lbt"`, etc.
  - **$i = 2$ (Modify 't'):** Generates `"loa"`, `"lob"`, ..., `"log"`, ..., `"loz"`.
    - When `newWord = "log"`: `set.contains("log")` is **True**!
      - `que.offer(new Pair("log", 4))`
      - `set.remove("log")` $\rightarrow$ `set` becomes `{"cog"}`.

- **End of Iteration 4 Queue State:** `[("dog", 4), ("log", 4)]`

#### **Iteration 5**

- **Queue State:** `[("dog", 4), ("log", 4)]`
- `que.poll()` extracts: `currWord = "dog"`, `currCnt = 4`.
- **Check:** Does `"dog".equals("cog")`? No.
- **Looping through characters of `"dog"`:**
  - **$i = 0$ (Modify 'd'):** Generates `"aog"`, `"bog"`, ..., `"cog"`, ..., `"zog"`.
    - When `newWord = "cog"`: `set.contains("cog")` is **True**!
      - `que.offer(new Pair("cog", 5))`
      - `set.remove("cog")` $\rightarrow$ `set` becomes `{}` (empty).

    - **$i = 1$ and $i = 2$:** No further valid matches found.

- **End of Iteration 5 Queue State:** `[("log", 4), ("cog", 5)]`

#### **Iteration 6**

- **Queue State:** `[("log", 4), ("cog", 5)]`
- `que.poll()` extracts: `currWord = "log"`, `currCnt = 4`.
- **Check:** Does `"log".equals("cog")`? No.
- **Looping through characters:** Since the `set` is empty now, no new elements are added.
- **End of Iteration 6 Queue State:** `[("cog", 5)]`

#### **Iteration 7**

- **Queue State:** `[("cog", 5)]`
- `que.poll()` extracts: `currWord = "cog"`, `currCnt = 5`.
- **Check:** Does `"cog".equals("cog")`? **YES!**

> **Execution stops here:** The condition `if (currWord.equals(endWord))` triggers instantly, executing `return currCnt;`.

#### **Final Output:** `5`

---

### Complexity Analysis

Let $N$ be the number of words in the `wordList` and $L$ be the uniform length of each word.

- **Time Complexity**
  - **Set Construction:** Copying $N$ words into a hash set takes $O(N \times L)$ time because hashing a string takes time proportional to its length.
  - **BFS Processing:** In the worst case, every word in the dictionary is added and removed from the queue exactly once, resulting in $N$ iterations.
  - **Word Mutations:** For each word popped from the queue, we iterate through its length $L$. At each character position, we substitute $26$ possible characters. For each substitution, generating or finding the new string in the hash set costs $O(L)$ time due to string hashing/comparison.
  - **Total Time Complexity:** $$O(N \times L + N \times L \times 26 \times L) = O(N \times L^2)$$

  Given the constraints ($L \le 10, N \le 5000$), $N \times L^2 \approx 5000 \times 100 = 500,000$ operations, which runs near instantly ($0\text{ ms}$ or a few milliseconds).

- **Space Complexity**
  - **Hash Set Storage:** Storing $N$ words of length $L$ requires $O(N \times L)$ space.
  - **Queue Space:** In the worst-case scenario, the BFS queue holds a large fraction of the dictionary words, taking $O(N \times L)$ space.
  - **Total Space Complexity:** $$O(N \times L)$$
