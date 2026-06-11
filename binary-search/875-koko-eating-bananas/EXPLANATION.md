# 875. Koko Eating Bananas

**Difficulty:** Medium | **Pattern:** Binary Search on the Answer | **LeetCode Link:** [Problem 875](https://leetcode.com/problems/koko-eating-bananas/)

---

## Problem Statement

Koko loves bananas. There are `n` piles, the `i`-th pile has `piles[i]` bananas. The guards will return in `h` hours.

Koko picks a fixed **eating speed `k`** (bananas/hour). Each hour she chooses one pile and eats `k` bananas from it. If the pile has fewer than `k` bananas, she finishes the pile and **does not eat more that hour** (she won't switch piles mid-hour).

Return the **minimum integer speed `k`** such that she can eat all the bananas within `h` hours.

### Example 1
```
Input:  piles = [3, 6, 7, 11], h = 8
Output: 4      (hours at speed 4: ⌈3/4⌉+⌈6/4⌉+⌈7/4⌉+⌈11/4⌉ = 1+2+2+3 = 8 ✓)
```

### Example 2
```
Input:  piles = [30, 11, 23, 4, 20], h = 5
Output: 30     (5 piles, 5 hours → every pile must be cleared in one hour → speed = max pile)
```

### Example 3
```
Input:  piles = [30, 11, 23, 4, 20], h = 6
Output: 23     (hours: 2+1+1+1+1 = 6 ✓; at speed 22 it would be 2+1+2+1+1 = 7 ✗)
```

### Constraints
- `1 <= piles.length <= 10⁴`
- `piles.length <= h <= 10⁹`
- `1 <= piles[i] <= 10⁹`

---

## Asked By (Big Companies)

- **Meta (Facebook)** — extremely frequent; this problem originated from a Facebook interview setting
- **Google** — common phone-screen and onsite warm-up for the binary-search-on-answer family
- **Amazon** — appears in OAs and onsites
- **Bloomberg**
- **Apple**
- **Uber / DoorDash** — delivery-capacity variants of the same pattern
- **TikTok**

> 💼 Interviewers use this to test whether you can binary search over something that *isn't an array*. Candidates who only know "binary search = find element in sorted array" get stuck; candidates who say "I'll binary search the **answer space**" stand out immediately.

---

## Intuition — How to Think About It

### Step 1: What are we actually choosing?
We're choosing a **speed** `k`, not an array index. Valid speeds form a range:
- **Minimum possible:** `1` (slowest imaginable)
- **Maximum useful:** `max(piles)` — eating faster than the biggest pile changes nothing, since she stops at a pile's end anyway.

So the candidate answers are the integers `[1, max(piles)]`. That's our "virtual sorted array."

### Step 2: The monotonic predicate — the heart of the pattern
Define: `canFinish(k)` = "at speed `k`, total hours ≤ h".

The crucial observation: **if Koko can finish at speed `k`, she can finish at any speed faster than `k`.** Eating faster never hurts. So the predicate over the speed range looks like:

```
speed:      1    2    3    4    5    6    ...   11
canFinish:  F    F    F    T    T    T    ...    T
                          ↑
                  first TRUE = the answer
```

**One flip, never flips back — that's monotonicity, and monotonicity is the license to binary search.** We're hunting the *first true* (the boundary), which halves the range each step.

### Step 3: Evaluating the predicate
At speed `k`, a pile of `p` bananas takes `⌈p / k⌉` hours (she can't carry leftover appetite to another pile). Sum that over all piles and compare with `h`. That's an `O(n)` check.

### Visual Walkthrough (piles = [3, 6, 7, 11], h = 8)

```
Search space [1 .. 11]

lo=1,  hi=11 → mid=6:  hours = 1+1+2+2 = 6  ≤ 8  feasible → hi=6   (mid might be the answer, keep it)
lo=1,  hi=6  → mid=3:  hours = 1+2+3+4 = 10 > 8  too slow → lo=4   (mid is ruled out)
lo=4,  hi=6  → mid=5:  hours = 1+2+2+3 = 8  ≤ 8  feasible → hi=5
lo=4,  hi=5  → mid=4:  hours = 1+2+2+3 = 8  ≤ 8  feasible → hi=4
lo == hi == 4 → answer = 4 ✅
```

11 candidate speeds checked with only **4** predicate evaluations.

---

## Algorithm (Step by Step)

1. Set `slowestSpeed = 1`, `fastestSpeed = max(piles)`.
2. While `slowestSpeed < fastestSpeed`:
   - `candidateSpeed = slowestSpeed + (fastestSpeed - slowestSpeed) / 2` (overflow-safe midpoint).
   - Compute `hoursNeeded = Σ ⌈pile / candidateSpeed⌉` (accumulate in a **long**).
   - If `hoursNeeded ≤ h` → the candidate works; the answer is this speed **or slower** → `fastestSpeed = candidateSpeed` (keep the candidate — it may be the answer).
   - Else → too slow → `slowestSpeed = candidateSpeed + 1` (the candidate is ruled out).
3. Loop ends when `slowestSpeed == fastestSpeed` — both point at the **first feasible speed**. Return it.

**Loop invariant** (say this in the interview): the answer is always inside `[slowestSpeed, fastestSpeed]`; everything below `slowestSpeed` is proven infeasible, and `fastestSpeed` is feasible or untested-but-bounded.

---

## Complexity Analysis (Detailed)

Let `n = piles.length` and `M = max(piles)`.

### Time Complexity: **O(n log M)**
- The search space `[1, M]` halves every iteration → `⌈log₂ M⌉` iterations. With `M ≤ 10⁹`, that's at most **30 iterations**, always.
- Each iteration evaluates the predicate in `O(n)` (one pass summing ceilings).
- Total: `O(n log M)` = at most `10⁴ × 30 = 3 × 10⁵` operations — instant.
- Compare with checking every speed: `O(n × M)` = `10⁴ × 10⁹ = 10¹³` — impossible. The log turns a billion candidates into thirty.

### Space Complexity: **O(1)**
- Three scalars (`slowestSpeed`, `fastestSpeed`, `hoursNeeded`) regardless of input size. The iterative search uses no stack or auxiliary structures.

### ⚠️ Overflow analysis (interviewers probe this!)
- `hoursNeeded` can reach `10⁴ piles × 10⁹ hours` (at speed 1) `= 10¹³` → **must be a `long`**; an `int` overflows at ~2.1 × 10⁹.
- Naive ceiling `(pile + speed - 1) / speed` can overflow `int` too: `10⁹ + 10⁹ - 1 ≈ 2 × 10⁹ > Integer.MAX_VALUE`. Use `Math.ceilDiv(pile, speed)` (Java 18+, overflow-free) or the safe identity `(pile - 1) / speed + 1`.
- Midpoint `lo + (hi - lo) / 2` instead of `(lo + hi) / 2` — the historic JDK binary-search bug.

---

## Important Theorems & Concepts 📚

### 1. Binary Search on a Monotonic Predicate (the generalized binary search)
Classic binary search finds a *value in a sorted array*. The general form finds the **boundary of any monotone true/false predicate** — this is the discrete cousin of the **bisection method** from numerical analysis (finding where a monotone function crosses a threshold). Whenever you can prove *"if it works for x, it works for all x above (or below)"*, you may binary search — no array required.

### 2. The "first true" (lower bound) template
This loop shape (`while (lo < hi)`, feasible → `hi = mid`, infeasible → `lo = mid + 1`) computes the **lower bound**: the smallest value satisfying the predicate. It cannot infinite-loop because `mid` always rounds *down*, so `lo` strictly increases and `hi` never increases past a feasible point.

### 3. Ceiling division identity
`⌈a / b⌉ = (a + b − 1) / b = (a − 1) / b + 1` in integer math. The second form never overflows; in modern Java just use **`Math.ceilDiv(a, b)`** and mention the overflow reasoning out loud.

---

## Tips & Tricks to Remember the Pattern 💡

### 🎯 The "Goldilocks" mnemonic
The answer space is a row of porridge bowls sorted from too-cold to too-hot, and feasibility flips exactly once. Don't taste every bowl — jump to the middle, learn which *half* is wrong, discard it. You're searching for the **boundary bowl**, not a known value.

### 🔑 Pattern recognition triggers — "binary search the answer" when you see:
- "**minimum** speed / capacity / size / time **such that** [some condition holds]"
- "**maximize** the minimum…" or "**minimize** the maximum…"
- Checking a *given* answer is easy (an `O(n)` simulation), but *finding* the best answer directly seems hard
- The condition is obviously monotone in the quantity being asked for

### 🧠 The template (memorize — it solves a dozen LeetCode problems verbatim)
```java
int lo = minPossibleAnswer, hi = maxPossibleAnswer;
while (lo < hi) {
    int mid = lo + (hi - lo) / 2;
    if (feasible(mid)) hi = mid;      // works → answer is mid or smaller, KEEP mid
    else               lo = mid + 1;  // fails → answer is strictly bigger
}
return lo; // == hi: the first feasible value
```

### ⚠️ Common pitfalls interviewers watch for
1. **`hi = mid - 1` after a feasible mid** — discards a possibly-optimal answer; feasible mids must stay in range (`hi = mid`).
2. **`int` hour counter** — overflows at `10¹³` total hours. Use `long`.
3. **Manual ceiling with doubles** — `(int) Math.ceil((double) pile / speed)` invites precision bugs at `10⁹`; stay in integer math.
4. **Starting `hi` at some arbitrary big number** instead of `max(piles)` — works but wastes iterations and signals weaker reasoning about bounds.
5. **Forgetting *why* binary search is legal** — always state the monotonicity argument before coding; that's the actual interview answer.

### 🔗 Practice ladder (same template, increasing difficulty)
| Problem | Twist |
|---|---|
| 704. Binary Search | The classic array version (foundation) |
| 875. Koko Eating Bananas | Binary search the answer (this one) |
| 1011. Capacity to Ship Packages in D Days | Identical template, capacity instead of speed |
| 1482. Min Days to Make m Bouquets | Feasibility = counting adjacent groups |
| 410. Split Array Largest Sum (Hard) | Minimize the maximum — same skeleton |
| 4. Median of Two Sorted Arrays (Hard) | Binary search on a partition position |

---

## What to Say in the Interview (Script)

1. *"I'm choosing a speed, and feasibility is monotone — if speed k works, every faster speed works. A monotone predicate means I can binary search the answer space [1, max(pile)] instead of an array."*
2. *"Checking one speed is an O(n) simulation: each pile costs ceil(pile/speed) hours; I'll accumulate in a long since it can reach 10¹³."*
3. *"I'll use the first-true template: feasible → hi = mid (keep the candidate), infeasible → lo = mid + 1. The loop invariant is that the answer always stays inside [lo, hi]."*
4. *"Total: O(n log M) time — log of 10⁹ is just 30 checks — and O(1) space. I'll use Math.ceilDiv to avoid the overflow in the naive ceiling formula."*

Naming the invariant and the overflow before being asked is exactly what separates "knows binary search" from "owns binary search." 🚀
