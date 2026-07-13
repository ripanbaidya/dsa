# 433. Minimum Genetic Mutation

<p align="right">Last updated - 13.07.2026</p>

- https://leetcode.com/problems/minimum-genetic-mutation/description/

## Approach: BFS

### Intuition

This problem asks for the minimum number of mutations needed to transform a `startGene` into an `endGene`.

1. **Modeling as a Graph:** Think of each 8-character gene string as a **node** in a graph. A valid mutation means changing exactly one character (e.g., from `'A'` to `'T'`). If two gene strings differ by exactly one character, there is an **undirected edge** between them.
2. **The Constraints:** You can only traverse to a node (gene string) if it exists in the given `bank`. The `bank` acts as a map of valid checkpoints.
3. **Why Breadth-First Search (BFS)?** We need to find the **minimum** number of mutations. Because every single mutation counts as exactly $1$ step (an unweighted graph), BFS is the perfect algorithm. BFS explores all 1-step mutations, then all 2-step mutations, and so on. The moment we encounter the `endGene` during our BFS traversal, we are guaranteed to have found the shortest mutation path.

### Algorithm

1. **Edge Cases:** If the `startGene` is already equal to the `endGene`, $0$ mutations are required. Return `0`.
2. **Setup the Bank:** Convert the `bank` array into a hash set (`set`). This allows for $\mathcal{O}(1)$ time complexity lookups to check if a generated mutation is valid.
3. **Initialize the Queue:**
   - Use a queue to store pairs of `(current_gene, current_mutation_count)`.
   - Push the `startGene` with a count of `0` into the queue.

4. **BFS Loop:** While the queue is not empty:
   - Dequeue the front element.
   - **Check Destination:** If this gene string equals `endGene`, return the `current_mutation_count`.
   - **Generate Neighbors (Mutations):** For each of the 8 character positions in the current gene string, try replacing it with one of the 4 valid choices: `'A'`, `'C'`, `'G'`, or `'T'`.
   - **Validate and Prune:** If the newly formed gene string exists in our valid gene bank (`set`):
     - Enqueue it with an incremented mutation count: `(new_gene, current_mutation_count + 1)`.
     - **Prevent Cycles:** Remove the `new_gene` from the `set`. Removing it serves as marking it "visited" so we don't process it again in a loop.

5. **No Solution:** If the queue becomes empty and the `endGene` was never reached, return `-1`.

### Implementations

**Java**

```java
class Solution {
    public int minMutation(String startGene, String endGene, String[] bank) {
        if (startGene.equals(endGene)) return 0;

        // Convert bank to a Set for O(1) lookups
        Set<String> validBank = new HashSet<>(Arrays.asList(bank));

        // If endGene is not in the bank, it's impossible to reach it safely
        if (!validBank.contains(endGene)) return -1;

        char[] choices = {'A', 'C', 'G', 'T'};

        // Queue stores: {current_gene_string, mutation_count}
        Queue<Pair> queue = new ArrayDeque<>();
        queue.offer(new Pair(startGene, 0));
        validBank.remove(startGene); // Mark startGene as visited

        while (!queue.isEmpty()) {
            Pair current = queue.poll();
            String curGene = current.gene;
            int curCount = current.mutationCnt;

            if (curGene.equals(endGene)) {
                return curCount;
            }

            char[] curGeneArr = curGene.toCharArray();
            // Try mutating every single position
            for (int i = 0; i < 8; i++) {
                char originalChar = curGeneArr[i];

                for (char choice : choices) {
                    if (choice == originalChar) continue;

                    curGeneArr[i] = choice;
                    String mutatedGene = String.valueOf(curGeneArr);

                    // If the mutated gene is valid and hasn't been visited yet
                    if (validBank.contains(mutatedGene)) {
                        queue.offer(new Pair(mutatedGene, curCount + 1));
                        validBank.remove(mutatedGene); // Mark as visited
                    }
                }
                // Revert back to original state before moving to the next index
                curGeneArr[i] = originalChar;
            }
        }

        return -1;
    }

    // Custom helper class to keep track of state
    private static class Pair {
        String gene;
        int mutationCnt;

        public Pair(String gene, int mutationCnt) {
            this.gene = gene;
            this.mutationCnt = mutationCnt;
        }
    }
}

```

**C++**

```cpp
class Solution {
public:
    int minMutation(string startGene, string endGene, vector<string>& bank) {
        if (startGene == endGene) return 0;

        // Convert bank to an unordered_set for O(1) lookups
        unordered_set<string> validBank(bank.begin(), bank.end());

        // If endGene is not in the bank, it's unreachable
        if (validBank.find(endGene) == validBank.end()) return -1;

        vector<char> choices = {'A', 'C', 'G', 'T'};

        // Queue stores pair of: {current_gene, mutation_count}
        queue<pair<string, int>> q;
        q.push({startGene, 0});
        validBank.erase(startGene); // Mark startGene as visited

        while (!q.empty()) {
            auto [curGene, curCount] = q.front();
            q.pop();

            if (curGene == endGene) {
                return curCount;
            }

            // Try mutating every single position of the 8-character string
            for (int i = 0; i < 8; i++) {
                char originalChar = curGene[i];

                for (char choice : choices) {
                    if (choice == originalChar) continue;

                    curGene[i] = choice;

                    // If the mutated gene is valid and hasn't been visited yet
                    if (validBank.find(curGene) != validBank.end()) {
                        q.push({curGene, curCount + 1});
                        validBank.erase(curGene); // Mark as visited
                    }
                }
                // Revert back to original state before moving to the next index
                curGene[i] = originalChar;
            }
        }

        return -1;
    }
};

```

### Complexity Analysis

- Time Complexity: $O(N)$
  - **HashSet Initialization:** Copying the `bank` array into a `HashSet` takes $O(N \times L)$ time, where $N$ is the number of genes in the bank and $L$ is the length of the gene string ($L = 8$).
  - **BFS Traversal:** In the worst-case scenario, you look at every gene in the bank exactly once. For each gene, you loop through its 8 characters ($L$) and try 4 choices ($A$) for each character. Since creating a new string and checking it against the `HashSet` takes $O(L)$ time due to hashing, the BFS part takes $O(N \times L^2 \times A)$ time.
  - Because $L = 8$ and $A = 4$ are fixed constants, the entire time complexity simplifies beautifully to **$O(N)$**.

- Space Complexity: $O(N)$
  - **HashSet Storage:** The `HashSet` stores at most $N$ strings of length 8, which takes $O(N \times L)$ space.
  - **Queue Storage:** The BFS `Queue` holds at most $N$ strings at any given time, taking $O(N \times L)$ space.
  - Dropping the constant length factor $L$, the overall space complexity is **$O(N)$**.
