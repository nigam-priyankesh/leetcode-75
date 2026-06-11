# 162. Find Peak Element

**Difficulty:** Medium | **Pattern:** Binary Search on an Unsorted Array (slope descent) | **LeetCode Link:** [Problem 162](https://leetcode.com/problems/find-peak-element/)

---

## Problem Statement

A **peak** element is one that is **strictly greater than its neighbors**. Given a 0-indexed array `nums`, return the index of **any** peak. Imagine `nums[-1] = nums[n] = -∞` — the edges fall off into negative infinity. You may assume `nums[i] != nums[i + 1]` for all valid `i` (no equal neighbors).

**You must write an algorithm that runs in `O(log n)` time.**

### Example 1
```
Input:  nums = [1, 2, 3, 1]
Output: 2          (nums[2] = 3 is greater than both 2 and 1)
```

### Example 2
```
Input:  nums = [1, 2, 1, 3, 5, 6, 4]
Output: 5 (or 1)   (6 is a peak; 2 at index 1 is also a peak — ANY peak is accepted)
```

### Constraints
- `1 <= nums.length <= 1000`
- `-2³¹ <= nums[i] <= 2³¹ - 1`
- `nums[i] != nums[i + 1]` for all valid `i`

---

## Asked By (Big Companies)

- **Meta (Facebook)** — one of their most frequently reported binary search questions
- **Google** — a classic; also famous as the opening lecture of MIT 6.006 (peak finding)
- **Amazon**
- **Microsoft**
- **Bloomberg**
- **Apple**
- **Uber**

> 💼 The trap interviewers set: the array is **NOT sorted**, so most candidates think binary search is impossible and write O(n). Realizing binary search never needed "sorted" — only a way to discard half with certainty — is the entire test.

---

## Intuition — How to Think About It

### Step 1: An O(n) answer exists — why is O(log n) even possible?
The required `O(log n)` is a giant hint: the interviewer wants you to discard half the array per step. But binary search on *what*? The array isn't sorted. The key reframing:

> **Binary search doesn't require a sorted array. It requires a rule that tells you, with certainty, which half contains an answer.**

### Step 2: Look at the slope under your feet
Stand at index `mid` and compare with the next element:

- **`nums[mid] < nums[mid + 1]` (ascending slope ⬆):** walk right and the values are climbing. Either they climb forever until the last element — which is a peak, because `nums[n] = -∞` — or they drop somewhere, and the point just before the first drop is a peak. **Either way, a peak certainly exists to the RIGHT of `mid`** (and `mid` itself is ruled out, it's smaller than its right neighbor). → `lo = mid + 1`
- **`nums[mid] > nums[mid + 1]` (descending slope ⬇):** by the mirror argument walking left, a peak certainly exists at `mid` **or to the LEFT** (`mid` is still a candidate — it beats its right neighbor!). → `hi = mid`

No equal neighbors means one of the two cases always holds. **Follow the rising slope — you must eventually stand on a peak.** Like hiking uphill: keep climbing and you will reach *a* summit (not necessarily the tallest — the problem only asks for any peak).

### Step 3: Why this is "first true" binary search in disguise
Define the predicate `P(i)` = "`nums[i] > nums[i + 1]`" (the descending edge), with `P(n−1)` = true (last element vs `-∞`). The first index where `P` flips to true is exactly a peak: it beats its right neighbor by `P(i)`, and it beats its left neighbor because `P(i−1)` is false (meaning `nums[i−1] < nums[i]`). So we're hunting a **boundary** again — same skeleton as Koko Eating Bananas, different predicate.

### Visual Walkthrough (nums = [1, 2, 1, 3, 5, 6, 4])

```
index:   0  1  2  3  4  5  6
value:   1  2  1  3  5  6  4

lo=0, hi=6 → mid=3: nums[3]=3 < nums[4]=5  ⬆ ascending → peak is right → lo=4
lo=4, hi=6 → mid=5: nums[5]=6 > nums[6]=4  ⬇ descending → peak at mid or left → hi=5
lo=4, hi=5 → mid=4: nums[4]=5 < nums[5]=6  ⬆ ascending → lo=5
lo == hi == 5 → return 5   (nums[5]=6 > 5 and > 4 ✅)
```

Seven elements, three comparisons.

---

## Algorithm (Step by Step)

1. `lo = 0`, `hi = n - 1`.
2. While `lo < hi`:
   - `mid = lo + (hi - lo) / 2` (overflow-safe; also guarantees `mid + 1 <= hi`, so the comparison never goes out of bounds).
   - If `nums[mid] > nums[mid + 1]` → descending → `hi = mid` (mid stays a candidate).
   - Else → ascending → `lo = mid + 1` (mid is disqualified).
3. Return `lo` (== `hi`) — the interval shrank to a single index that was never disqualified, and the invariant guarantees it's a peak.

**Loop invariant** (state it!): *the range `[lo, hi]` always contains at least one peak.* Initially true (every finite array with -∞ edges has a peak). Each step preserves it by the slope argument. The loop ends with one index left — a peak.

---

## Complexity Analysis (Detailed)

### Time Complexity: **O(log n)**
- The interval `[lo, hi]` halves every iteration: `n → n/2 → n/4 → … → 1`, which is `⌈log₂ n⌉` iterations.
- Each iteration does O(1) work: one midpoint computation and one comparison.
- For `n = 1000`: **10 comparisons** instead of up to 1000 with a linear scan.
- Linear scan (find first `i` with `nums[i] > nums[i+1]`) is O(n) — correct, but fails the explicit O(log n) requirement.

### Space Complexity: **O(1)**
- Three integers (`lo`, `hi`, `mid`). Iterative — no recursion stack, no auxiliary structures.

---

## Important Theorems & Concepts 📚

### 1. Binary search ≠ sorted arrays — it's about *certain elimination*
The deep lesson of this problem: sortedness is just one way to know which half to discard. Any **invariant you can maintain while halving** ("this interval contains a peak") works. This generalizes to rotated arrays (LC 33/153), unknown-size searches, and answer-space searches (LC 875).

### 2. Existence by the boundary argument (discrete fixed-point flavor)
With `nums[-1] = nums[n] = -∞`, the sequence starts ascending (from -∞) and ends descending (into -∞), so somewhere the direction must flip — that flip is a peak. This is the discrete cousin of: *a continuous function rising then falling attains a maximum* (Extreme Value Theorem intuition). The same "follow the gradient" argument extends to **2D peak finding** in O(n log n) — the famous MIT 6.006 opening lecture.

### 3. Why `mid + 1` is always safe
`mid = lo + (hi - lo) / 2` rounds *down*, so when `lo < hi` we have `mid < hi`, hence `mid + 1 ≤ hi` — the neighbor comparison never reads out of bounds. Knowing *why* there's no `ArrayIndexOutOfBoundsException` (rather than just testing for it) is an interview differentiator.

---

## Tips & Tricks to Remember the Pattern 💡

### ⛰️ The "hike uphill" mnemonic
You're dropped on a mountainside in fog. Rule: **always step uphill.** You can't walk uphill forever (the world ends in cliffs of -∞), so you must reach *a* summit. Binary search is the same hike with teleportation — jump to the middle, check the slope, teleport past the half that can't trap a guaranteed peak.

### 🔑 Pattern recognition triggers
- "Find **any** peak / local maximum / local minimum" → slope-following binary search
- Array not sorted **but** the problem demands O(log n) → hunt for a *halving invariant*, not sortedness
- "`nums[i] != nums[i+1]`" or "-∞ boundaries" in the statement → the slope at any point is well-defined → this exact template

### 🧠 The template (two lines of decision logic)
```java
while (lo < hi) {
    int mid = lo + (hi - lo) / 2;
    if (nums[mid] > nums[mid + 1]) hi = mid;      // descending → peak at mid or left
    else                           lo = mid + 1;  // ascending  → peak strictly right
}
return lo;
```
Note it's the **same "first true" skeleton as LC 875** — only the predicate changed (`canFinish(speed)` → `descendingEdge(i)`). One template, many problems.

### ⚠️ Common pitfalls interviewers watch for
1. **`hi = mid - 1` on the descending branch** — throws away `mid`, which might BE the peak. Descending keeps the candidate: `hi = mid`.
2. **Comparing `nums[mid]` with `nums[mid - 1]`** — `mid - 1` can underflow when `lo == mid == 0`; the `mid + 1` formulation is always in bounds (see concept #3).
3. **Returning the maximum element's index via linear scan** — correct output, wrong complexity; the O(log n) requirement is part of the problem.
4. **Assuming there's exactly one peak** — `[1,2,1,3,5,6,4]` has two; the loop converges to *one* of them, and LeetCode accepts any.
5. **Worrying about equal neighbors** — the constraint forbids them; mention that the algorithm relies on this (with plateaus, O(log n) worst case becomes impossible).

### 🔗 Practice ladder (binary search beyond sorted arrays)
| Problem | Twist |
|---|---|
| 704. Binary Search | Sorted array (foundation) |
| 162. Find Peak Element | Slope invariant, unsorted (this one) |
| 153. Find Minimum in Rotated Sorted Array | Pivot invariant |
| 33. Search in Rotated Sorted Array | One sorted half invariant |
| 852. Peak Index in a Mountain Array | This problem with exactly one peak |
| 1901. Find a Peak Element II (Hard) | The 2D version — column max + slope |

---

## What to Say in the Interview (Script)

1. *"The array isn't sorted, but binary search only needs a rule that discards half with certainty. My rule is the slope at mid."*
2. *"If nums[mid] is less than its right neighbor, I'm on an ascending slope — following it must hit a peak before the -∞ edge, so a peak certainly exists to the right and mid itself is disqualified. The mirror argument handles the descending case, where mid stays a candidate."*
3. *"My loop invariant: [lo, hi] always contains a peak. Descending → hi = mid, ascending → lo = mid + 1; when the interval is one element, that's a peak."*
4. *"mid rounds down, so mid + 1 never exceeds hi — no out-of-bounds. O(log n) time, O(1) space; for n = 1000 that's ten comparisons."*

The sentence *"binary search needs certain elimination, not sortedness"* is the one interviewers quote back in debriefs. 🚀