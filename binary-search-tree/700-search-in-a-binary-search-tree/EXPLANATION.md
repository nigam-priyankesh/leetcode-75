# 700. Search in a Binary Search Tree

**Difficulty:** Easy | **Pattern:** BST-guided descent (binary search shaped like a tree) | **LeetCode Link:** [Problem 700](https://leetcode.com/problems/search-in-a-binary-search-tree/)

---

## Problem Statement

You are given the `root` of a **binary search tree (BST)** and an integer `val`. Find the node whose value equals `val` and return **the subtree rooted at that node**. If no such node exists, return `null`.

### Example 1
```
Input:  root = [4,2,7,1,3], val = 2
Output: [2,1,3]

        4                    2
       / \                  / \
      2   7    → return    1   3
     / \
    1   3
```

### Example 2
```
Input:  root = [4,2,7,1,3], val = 5
Output: []          (5 is not in the tree → null)
```

### Constraints
- `1 <= number of nodes <= 5000`
- `1 <= Node.val <= 10⁷`
- `root` is a **valid BST**
- `1 <= val <= 10⁷`

---

## Asked By (Big Companies)

- **Amazon** — frequent warm-up before BST insert/delete follow-ups
- **Microsoft**
- **Google**
- **Meta (Facebook)**
- **Apple**
- **Bloomberg**

> 💼 This is an *opener*. Interviewers use it to check you actually exploit the BST invariant (one comparison per level) instead of traversing the whole tree like a plain binary tree. The real evaluation is the follow-up: insert (701), delete (450), or validate (98).

---

## Intuition — How to Think About It

### Step 1: What makes a BST special?
A binary search tree carries one global promise at **every** node:

> **Everything in the left subtree is smaller than the node; everything in the right subtree is larger.**

Not just the immediate children — *entire subtrees*. That promise is the whole problem.

### Step 2: One comparison kills a whole subtree
Stand at any node and compare `val` with `node.val`:

- **`val == node.val`** → found it, return this node.
- **`val < node.val`** → the target is smaller than this node, and *everything* in the right subtree is larger than this node. The right subtree **cannot** contain the target. Go left.
- **`val > node.val`** → mirror argument. Go right.

You never branch both ways, never backtrack. The search is a single root-to-leaf walk.

### Step 3: This *is* binary search
Compare with classic binary search on a sorted array: compare with the middle, discard half, repeat. A BST is the same idea with the "middle" elements wired together as nodes — the root plays the role of `mid`, its subtrees are the two halves. In a balanced BST each step discards about half the remaining nodes, giving the same O(log n).

### Visual Walkthrough (root = [4,2,7,1,3], val = 3)

```
        4         val=3 < 4  → go LEFT  (subtree under 7 eliminated unseen)
       / \
      2   7       val=3 > 2  → go RIGHT (node 1 eliminated unseen)
     / \
    1   3         val=3 == 3 → FOUND → return node 3
```

Five nodes, three comparisons, two whole subtrees never visited.

---

## Algorithm (Step by Step)

1. Start with `current = root`.
2. While `current != null` and `current.val != val`:
   - If `val < current.val` → `current = current.left`.
   - Else → `current = current.right`.
3. Return `current` — either the matching node or `null` (we walked off the tree, so the value can't exist anywhere).

**Loop invariant** (state it!): *if `val` exists in the tree at all, it lives in the subtree rooted at `current`.* Initially true (the whole tree). Each step preserves it by the BST ordering promise. The loop ends when `current` is the match or `null` — and `null` plus the invariant proves the value is absent.

---

## Complexity Analysis (Detailed)

### Time Complexity: **O(h)** where h = tree height
- The walk visits one node per level, never revisits, never branches: at most `h + 1` comparisons.
- **Balanced BST**: `h ≈ log₂ n` → **O(log n)**. For n = 5000 that's ~13 comparisons.
- **Degenerate (skewed) BST** — every node has one child, e.g. built from sorted input: `h = n − 1` → **O(n)** worst case. Always say "O(h), which is O(log n) only if balanced" — quoting O(log n) unconditionally is a known trap.
- This h-vs-log-n gap is exactly why self-balancing trees (AVL, Red-Black — the engine behind Java's `TreeMap`) exist: they cap h at O(log n).

### Space Complexity: **O(1)** (iterative)
- One pointer (`current`). No stack, no recursion.
- The recursive version is the same time but **O(h) space** for the call stack — on a skewed 5000-node tree that's 5000 frames. Mentioning why you chose iterative is an easy point.

---

## Important Theorems & Concepts 📚

### 1. The BST invariant (ordering property)
For every node `x`: `max(left subtree) < x.val < min(right subtree)`. Every BST algorithm — search, insert, delete, floor/ceiling, range queries — is this one invariant applied repeatedly. Search is its purest form.

### 2. BST search ≡ binary search on the in-order sequence
The in-order traversal of a BST is sorted. Searching the BST visits exactly the nodes binary search would probe in that sorted sequence (root = first mid, and so on). Same elimination logic, different data layout — this connects directly to LC 704/162/875: *binary search is certain elimination, not arrays*.

### 3. Returning a node returns its whole subtree
The problem says "return the subtree" but the code returns one `TreeNode` — there's nothing to copy. A node *is* its subtree, because it holds the references to everything below it. Recognizing this (instead of cloning the subtree) shows you think in references.

---

## Tips & Tricks to Remember the Pattern 💡

### 🚪 The "turnstile" mnemonic
Each BST node is a turnstile with a number on it: **smaller goes left, bigger goes right, equal stops**. You can't take a wrong turn, and you never walk back through a turnstile. The whole algorithm is that one sentence.

### 🔑 Pattern recognition triggers
- "Search / insert / find floor / find closest **in a BST**" → guided descent, O(h), never traverse both children
- The moment a tree problem says **BST** (not just "binary tree") → ask yourself how the ordering invariant prunes work; if your solution visits both subtrees of any node, you're ignoring the B-S in BST
- "Return the subtree" → return the node reference; no copying

### 🧠 The template (one loop, one ternary)
```java
TreeNode current = root;
while (current != null && current.val != val) {
    current = val < current.val ? current.left : current.right;
}
return current;
```
The recursive one-liner is also worth having ready (elegant, but O(h) stack):
```java
if (root == null || root.val == val) return root;
return val < root.val ? searchBST(root.left, val) : searchBST(root.right, val);
```

### ⚠️ Common pitfalls interviewers watch for
1. **Traversing like a plain binary tree** (checking both children, BFS/DFS over everything) — correct output, O(n) always, and it tells the interviewer you missed the entire point of a BST.
2. **Claiming O(log n) unconditionally** — it's O(h); log n needs a balanced tree. Volunteer the skewed-tree caveat before they ask.
3. **Forgetting the `current != null` check** — searching for an absent value must end cleanly at `null`, not throw a `NullPointerException` off a leaf.
4. **Deep-copying the found subtree** — the node reference *is* the subtree.
5. **Recursion without mentioning stack cost** — fine to write recursively, but say "O(h) stack; iterative makes it O(1)."

### 🔗 Practice ladder (the BST core toolkit)
| Problem | Twist |
|---|---|
| 700. Search in a BST | The pure invariant (this one) |
| 701. Insert into a BST | Same descent — insert where the search falls off |
| 450. Delete Node in a BST | Descent + the three-case removal (in LeetCode 75) |
| 98. Validate Binary Search Tree | Check the invariant holds (min/max bounds) |
| 235. Lowest Common Ancestor of a BST | Descent until the two values split |
| 230. Kth Smallest Element in a BST | In-order traversal of a BST is sorted |

---

## What to Say in the Interview (Script)

1. *"A BST promises that the entire left subtree is smaller and the entire right subtree is larger — so one comparison at each node eliminates a whole subtree. This is binary search with the array laid out as a tree."*
2. *"I'll descend iteratively: smaller goes left, bigger goes right, equal stops. My invariant: if the value exists, it's in the subtree under my current pointer — so reaching null proves it's absent."*
3. *"Time is O(h): about log n comparisons if the tree is balanced, but O(n) if it's skewed — that's exactly why self-balancing trees like the Red-Black tree inside Java's TreeMap exist."*
4. *"I return the node itself — a node already references its whole subtree, so there's nothing to copy. Iterative keeps space at O(1) versus O(h) recursion stack."*

The sentence *"a BST is binary search with the array laid out as a tree"* is the one that sticks. 🚀