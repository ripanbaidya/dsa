# [133. Clone Graph](https://leetcode.com/problems/clone-graph/description/)

<p align="right">Last updated - 22.07.2026</p>

## Intuition

Cloning a graph means creating a **deep copy**: a brand-new set of nodes with identical values and edges, but residing at completely different memory addresses.

The main challenge in graph cloning (unlike a tree) is that graphs can contain **cycles** and **shared nodes**. If you blindly traverse and create nodes without tracking where you've been, you will enter an infinite loop or duplicate the same node multiple times.

To handle this:

1. **Map original nodes to cloned nodes:** Keep a hash map (`oldToNew`) that records every original node and its newly created clone.
2. **Reuse clones:** Before creating a new node, check the map. If it's already there, reuse the existing clone instead of recreating it. This handles cycles naturally!

## Algorithm

1. **Base Case:** If the given starting node is `null`, return `null`.
2. **Check Hash Map:** Check if the current original `node` is already in `oldToNew`:
   - If **yes**, return the previously created clone (`oldToNew.get(node)`).

3. **Create & Store Clone:**
   - Create a new `Node` with the same value (`node.val`).
   - Put the mapping `(original node -> cloned node)` into `oldToNew`.

4. **Recursively Clone Neighbors:**
   - Iterate over each neighbor `nei` of the original node.
   - Recursively call `dfs(nei)` to get the cloned version of that neighbor.
   - Add that cloned neighbor to `newNode.neighbors`.

5. **Return Clone:** Return `newNode`.

## Implementation

**Java**

```java []
class Solution {
    public Node cloneGraph(Node node) {
        // Map to keep track of original nodes and their cloned counterparts
        Map<Node, Node> oldToNew = new HashMap<>();
        return dfs(node, oldToNew);
    }

    private Node dfs(Node node, Map<Node, Node> oldToNew) {
        if (node == null) {
            return null;
        }

        // If node was already cloned, return its existing clone (handles cycles)
        if (oldToNew.containsKey(node)) {
            return oldToNew.get(node);
        }

        // Create the copy of the current node and register it in the map
        Node copy = new Node(node.val);
        oldToNew.put(node, copy);

        // Recursively clone all neighbors and attach them to the copy
        for (Node neighbor : node.neighbors) {
            copy.neighbors.add(dfs(neighbor, oldToNew));
        }

        return copy;
    }
}

```

**C++**

```cpp []
/*
// Definition for a Node.
class Node {
public:
    int val;
    vector<Node*> neighbors;
    Node() {
        val = 0;
        neighbors = vector<Node*>();
    }
    Node(int _val) {
        val = _val;
        neighbors = vector<Node*>();
    }
    Node(int _val, vector<Node*> _neighbors) {
        val = _val;
        neighbors = _neighbors;
    }
};
*/

class Solution {
public:
    Node* cloneGraph(Node* node) {
        // Map to keep track of original node pointers to cloned node pointers
        unordered_map<Node*, Node*> oldToNew;
        return dfs(node, oldToNew);
    }

private:
    Node* dfs(Node* node, unordered_map<Node*, Node*>& oldToNew) {
        if (node == nullptr) {
            return nullptr;
        }

        // If node was already cloned, return its existing clone (handles cycles)
        if (oldToNew.find(node) != oldToNew.end()) {
            return oldToNew[node];
        }

        // Create the copy of the current node and register it in the map
        Node* copy = new Node(node->val);
        oldToNew[node] = copy;

        // Recursively clone all neighbors and attach them to the copy
        for (Node* neighbor : node->neighbors) {
            copy->neighbors.push_back(dfs(neighbor, oldToNew));
        }

        return copy;
    }
};

```

---

## Complexity Analysis

- **Time Complexity:** $\mathcal{O}(V + E)$
  - Where $V$ is the number of nodes (vertices) and $E$ is the number of edges.
  - Every node is visited exactly once because we store it in `oldToNew`.
  - Every edge is traversed twice (once from each endpoint in an undirected graph).

- **Space Complexity:** $\mathcal{O}(V)$
  - **Hash Map:** Stores $V$ key-value pairs (mapping each original node to its copy).
  - **Call Stack:** In the worst-case scenario (a long linear graph like $1 - 2 - 3 - 4$), the DFS call stack can grow up to $\mathcal{O}(V)$ deep.
