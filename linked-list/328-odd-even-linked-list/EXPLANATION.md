# 328. Odd Even Linked List

**Difficulty:** Medium | **Pattern:** In-place pointer rewiring (two-pointer de-interleave) | **LeetCode Link:** [Problem 328](https://leetcode.com/problems/odd-even-linked-list/)

---

## Problem Statement

Given the `head` of a singly linked list, group all nodes at **odd positions** together followed by all nodes at **even positions**, and return the reordered list. Positions are **1-indexed** (the 1st node is odd, the 2nd is even, …) — this is about a node's *position*, **not** its value.

The relative order within the odd group and within the even group must be preserved. You must solve it in **O(1) extra space** and **O(n) time**.

### Example 1
```
Input:  1 → 2 → 3 → 4 → 5
Output: 1 → 3 → 5 → 2 → 4
        └ odds ┘  └evens┘
```

### Example 2
```
Input:  2 → 1 → 3 → 5 → 6 → 4 → 7
Output: 2 → 3 → 6 → 7 → 1 → 5 → 4
```

### Constraints
- The number of nodes is in `[0, 10⁴]`
- `-10⁶ <= Node.val <= 10⁶`

---

## Asked By (Big Companies)

- **Amazon** — a frequent linked-list warm-up
- **Microsoft**
- **Meta (Facebook)**
- **Bloomberg**
- **Adobe**
- **Apple**

> 💼 The interviewer is testing pointer discipline: can you rewire `next` references in place without losing the list, allocating nodes, or creating a cycle? The single most common bug — forgetting to save the even head — is exactly what they're watching for.

---

## Intuition — How to Think About It

### Step 1: Position, not value — and rewire, don't copy
"Odd/even" refers to the 1-indexed *position* in the list, so the very first node is always odd. The O(1)-space constraint forbids building new lists or arrays of values: the only legal move is to **re-point the existing `next` references**. We're un-zipping one interleaved chain into two.

### Step 2: Two builders walking in lockstep
The list alternates odd, even, odd, even, … So run two cursors:
- `oddTail` — the growing tail of the odd chain, starts at `head` (position 1).
- `evenTail` — the growing tail of the even chain, starts at `head.next` (position 2).

Each is two hops apart from its next target. `oddTail` should point to the node **two ahead** (skipping the even node between), then `evenTail` does the same. Advance both, repeat. This naturally separates the interleaving into two chains in a single pass.

### Step 3: The one trick that makes it work — save the even head
As soon as you start re-pointing `oddTail.next`, the original link from the last odd node into the even chain is gone. So **before the loop, stash `evenHead = head.next`**. After the walk, the odd chain is complete but ends pointing at nothing useful — splice the saved even head onto `oddTail.next`, and the two chains become one.

### Step 4: Why one loop guard handles both parities
`evenTail` always leads the walk. The condition `evenTail != null && evenTail.next != null` stops correctly in both cases:
- **Odd length** (last node is odd): `evenTail` eventually becomes `null` → first clause stops it.
- **Even length** (last node is even): `evenTail.next` becomes `null` → second clause stops it, leaving `oddTail` correctly on the last odd node, ready for the splice.

### Visual Walkthrough (1 → 2 → 3 → 4 → 5)

```
Start:   odd=1, even=2, evenHead=2          1→2→3→4→5

Iter 1:  odd.next = even.next (3)           1→3 …
         odd = 3
         even.next = odd.next (4)           2→4 …
         even = 4
         odd chain: 1→3   even chain: 2→4

Iter 2:  even(4).next = 5, so continue
         odd.next = even.next (5)           3→5
         odd = 5
         even.next = odd.next (null)        4→null
         even = null
         odd chain: 1→3→5   even chain: 2→4

Loop ends (even == null).
Splice:  odd(5).next = evenHead(2)          1→3→5→2→4  ✅
```

---

## Algorithm (Step by Step)

1. If the list has 0 or 1 nodes, return `head` (nothing to regroup).
2. `oddTail = head`, `evenTail = head.next`, `evenHead = head.next` (save it!).
3. While `evenTail != null && evenTail.next != null`:
   - `oddTail.next = evenTail.next` (odd grabs the node two ahead).
   - `oddTail = oddTail.next`.
   - `evenTail.next = oddTail.next` (even grabs the node two ahead).
   - `evenTail = evenTail.next`.
4. `oddTail.next = evenHead` (attach the even chain after the odds).
5. Return `head`.

**Loop invariant** (state it!): *after each iteration, `oddTail` and `evenTail` are the last nodes of two disjoint chains containing exactly the odd-position and even-position nodes seen so far, in original order.* The final splice joins them; no node is lost or duplicated because every `next` is rewritten exactly once.

---

## Complexity Analysis (Detailed)

### Time Complexity: **O(n)**
- Each iteration advances `evenTail` by two positions and rewrites two `next` pointers in O(1). The loop runs ~n/2 times, touching every node exactly once.
- Total: O(n), a single pass — no second traversal needed.

### Space Complexity: **O(1)**
- Three pointers (`oddTail`, `evenTail`, `evenHead`) plus the implicit head reference. No new nodes, no array, no recursion.
- This is the headline constraint of the problem: the naive "collect odds, collect evens, rebuild" uses O(n) space and is explicitly disallowed.

---

## Important Theorems & Concepts 📚

### 1. In-place pointer manipulation
The core linked-list skill: restructure by editing `next` references, never by moving data. The recurring hazard is **order of assignment** — once you overwrite a pointer you may lose the only path to a node, so save it first (here, `evenHead`). This is the same discipline behind list reversal (206) and the runner technique.

### 2. The "save before you overwrite" rule
A linked list is only reachable through its pointers; the instant you reassign the last reference to a sublist, that sublist is orphaned (and, in a GC language, eventually collected). Stashing `evenHead` before the rewiring loop is the concrete application — recognizing *which* pointer is about to become unreachable is the skill.

### 3. De-interleaving and the two-pointer family
This is a "split one stream into two by alternating index" problem — the same shape as separating a zipped list, partitioning by parity, or the odd/even split step inside some merge routines. The two-cursors-in-lockstep idiom generalizes to any "thread a subsequence through an existing structure" task.

---

## Tips & Tricks to Remember the Pattern 💡

### 🧵 The "two needles" mnemonic
Picture two needles threading through beads on one string. The odd needle picks bead 1, skips one, picks 3, skips one, picks 5… The even needle does the same starting at 2. When you run out of beads, **tie the even thread onto the end of the odd thread**. The "tie at the end" is the splice — and you must have kept hold of where the even thread *started* to tie it on.

### 🔑 Pattern recognition triggers
- "Group / reorder by **position** parity", "de-interleave", "split alternating nodes" → two-cursor in-place rewiring
- "**O(1) extra space**" on a linked-list reordering → you must rewire pointers, not collect values
- You're tempted to build two `ArrayList`s and rebuild → that's the O(n)-space answer the constraint forbids; convert it to pointer surgery

### 🧠 The template (de-interleave by position)
```java
if (head == null || head.next == null) return head;
ListNode odd = head, even = head.next, evenHead = even;  // SAVE evenHead
while (even != null && even.next != null) {
    odd.next  = even.next;  odd  = odd.next;   // odd grabs node 2 ahead
    even.next = odd.next;   even = even.next;  // even grabs node 2 ahead
}
odd.next = evenHead;        // reattach evens after odds
return head;
```
Memory hook: **the four assignments alternate odd, even, odd, even — the same alternation as the list itself.**

### ⚠️ Common pitfalls interviewers watch for
1. **Not saving `evenHead`** — the #1 bug. After rewiring, you have no way to find the start of the even chain to splice it on.
2. **Wrong loop guard** — using only `even != null` (or only `even.next != null`) breaks on one parity. You need **both** clauses; `even` leads, so check it and its `next`.
3. **Creating a cycle** — forgetting that the last even node's `next` must end as `null`. The loop leaves it null naturally *only* because `evenTail` advances onto the node whose `next` is null; verify the tail doesn't loop back.
4. **Grouping by value, not position** — re-read the problem; node *values* are irrelevant, it's the 1-indexed slot.
5. **O(n)-space rebuild** — correct output, but fails the stated constraint; the in-place version is the point.

### 🔗 Practice ladder (linked-list pointer surgery)
| Problem | Twist |
|---|---|
| 206. Reverse Linked List | The foundational rewire (save `next` before flipping) |
| 328. Odd Even Linked List | De-interleave into two chains (this one) |
| 24. Swap Nodes in Pairs | Local rewiring with a dummy head |
| 86. Partition List | Two chains split by a value pivot (same splice trick) |
| 2. Add Two Numbers | Build a new list while walking two |
| 143. Reorder List | Split + reverse + merge (combines three patterns) |

---

## What to Say in the Interview (Script)

1. *"'Odd/even' is about 1-indexed position, not value, and the O(1)-space rule means I rewire `next` pointers in place rather than collecting values into arrays."*
2. *"I run two cursors in lockstep — an odd-chain tail starting at head, an even-chain tail starting at head.next — each grabbing the node two ahead so they de-interleave in one pass."*
3. *"The key move: I save the even head before the loop, because rewiring the odd pointers destroys the path back to it. At the end I splice the even chain onto the odd tail."*
4. *"My loop guard checks both `even` and `even.next` so it terminates correctly whether the list length is odd or even. O(n) time, O(1) space, one pass, no allocation."*

The sentence *"save the even head before you rewire — that one pointer is the whole problem"* is the one interviewers nod at. 🚀
