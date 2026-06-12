# 739. Daily Temperatures

**Difficulty:** Medium | **Pattern:** Monotonic Stack (next greater element) | **LeetCode Link:** [Problem 739](https://leetcode.com/problems/daily-temperatures/)

---

## Problem Statement

Given an array `temperatures` of daily temperatures, return an array `answer` where `answer[i]` is **the number of days you have to wait after day `i` to get a warmer temperature**. If no future day is warmer, `answer[i] = 0`.

### Example 1
```
Input:  temperatures = [73, 74, 75, 71, 69, 72, 76, 73]
Output: [1, 1, 4, 2, 1, 1, 0, 0]

Day 2 (75°): the next warmer day is day 6 (76°) → wait 4 days.
Day 6 (76°): nothing after it is warmer → 0.
```

### Example 2
```
Input:  temperatures = [30, 40, 50, 60]
Output: [1, 1, 1, 0]
```

### Constraints
- `1 <= temperatures.length <= 10⁵`
- `30 <= temperatures[i] <= 100`

---

## Asked By (Big Companies)

- **Meta (Facebook)** — among their most-reported stack questions
- **Amazon**
- **Google**
- **Microsoft**
- **Bloomberg**
- **Adobe**
- **Goldman Sachs**

> 💼 This is *the* canonical "next greater element" question. Interviewers use it to see whether you reach for the monotonic stack or settle for the O(n²) double loop — with `n = 10⁵`, the brute force's ~10¹⁰ worst-case comparisons is exactly the trap.

---

## Intuition — How to Think About It

### Step 1: Why brute force hurts
For each day, scan forward until you find a warmer one: O(n²) worst case (a falling-then-flat week makes every scan run to the end). The wasted work: when day `i` scans forward past days `i+1..j-1`, it re-walks territory those days will each re-walk again. We need every comparison to count *once*.

### Step 2: Flip the question — who does TODAY answer?
Instead of each day searching forward for its answer, let each new day **hand out answers backwards**: when today's temperature arrives, it is the "next warmer day" for *every earlier, still-unanswered day that is colder than today*.

### Step 3: The unanswered days are always decreasing
Here's the structural gem. Suppose two days are both still waiting, the earlier one colder than the later one — impossible! The later, warmer day would have answered the earlier one the moment it arrived. So:

> **The unanswered days, in order, always form a strictly decreasing temperature sequence.**

A decreasing sequence where you only add to the end and remove from the end = a **stack**. Today pops every colder top (answering each: `today − thatDay` days of waiting), stops at the first top ≥ today (that one is still waiting — today didn't beat it), then pushes itself.

### Step 4: Store indices, not temperatures
The answer is a *distance in days*, so the stack must remember *which day*, not just how warm. `temperatures[index]` recovers the temperature for comparisons — indices carry strictly more information.

### Visual Walkthrough (temperatures = [73, 74, 75, 71, 69, 72, 76, 73])

```
day 0 (73): stack empty                      → push 0        stack: [0]
day 1 (74): 74 > 73 → pop 0, ans[0]=1-0=1    → push 1        stack: [1]
day 2 (75): 75 > 74 → pop 1, ans[1]=1        → push 2        stack: [2]
day 3 (71): 71 < 75 → nothing to answer      → push 3        stack: [2,3]
day 4 (69): 69 < 71 → nothing to answer      → push 4        stack: [2,3,4]
day 5 (72): 72 > 69 → pop 4, ans[4]=1
            72 > 71 → pop 3, ans[3]=2
            72 < 75 → stop                   → push 5        stack: [2,5]
day 6 (76): 76 > 72 → pop 5, ans[5]=1
            76 > 75 → pop 2, ans[2]=4        → push 6        stack: [6]
day 7 (73): 73 < 76 → nothing to answer      → push 7        stack: [6,7]

Days 6 and 7 never get answered → ans stays 0.
Result: [1, 1, 4, 2, 1, 1, 0, 0] ✅
```

Note the stack's temperatures read decreasing at every snapshot: `[75,71,69]`, `[75,72]`, `[76,73]` — the invariant in action.

---

## Algorithm (Step by Step)

1. Create `answer` array of size n, all zeros (zero = "never answered" comes for free).
2. Create an empty stack of **indices**.
3. For each `today` from `0` to `n − 1`:
   - While the stack is non-empty **and** `temperatures[stack.top] < temperatures[today]`:
     pop `colderDay`, set `answer[colderDay] = today − colderDay`.
   - Push `today`.
4. Return `answer`. Indices still on the stack keep their default 0.

**Loop invariant** (state it!): *the stack holds exactly the not-yet-answered days, in increasing index order and strictly decreasing temperature order.* Each push and pop preserves it, and it's what makes every pop's answer correct: the popped day's next-warmer day is *today* because every day between them was colder (they were popped earlier or never beat the popped day).

---

## Complexity Analysis (Detailed)

### Time Complexity: **O(n)** — amortized analysis
- The inner `while` loop looks like it could make a single iteration O(n) — and one iteration *can* pop many elements. The trick is to count **per element, not per iteration**: every index is pushed exactly once and popped **at most once**, so all pops across the entire run total ≤ n.
- Total work = n pushes + ≤ n pops + n outer iterations = **O(n)**. This "charge the work to the element, not the loop" argument is amortized analysis — say the word.
- For n = 10⁵: ~2 × 10⁵ stack operations vs. up to ~5 × 10⁹ comparisons for the O(n²) double loop.

### Space Complexity: **O(n)** worst case
- A strictly decreasing input (`[100, 99, 98, …]`) answers nothing — all n indices pile up on the stack.
- Best case (strictly increasing): the stack never holds more than one element.
- The output array doesn't count as extra space (it's the required answer).

### Micro-optimization worth *mentioning*
An `int[]` used as a stack (with an integer top pointer) beats `ArrayDeque<Integer>` by avoiding autoboxing — same O(n), smaller constants. Mention it; only write it if asked to optimize.

---

## Important Theorems & Concepts 📚

### 1. The Monotonic Stack principle
A stack that maintains sorted order by evicting violators before each push. The eviction moment is magic: **the new element is the popped element's "next greater" — and the new stack top is the popped element's "previous greater"** — both for free in O(n) total. One structure, two relations.

### 2. Amortized analysis (the banker's argument)
Each element "deposits" one credit when pushed; its eventual pop "spends" it. Total operations ≤ 2n regardless of how lumpy individual iterations look. The same argument powers sliding-window deques (LC 239) and KMP. Naming "amortized O(n)" — and *why* — is a strong senior signal.

### 3. Next Greater Element — a problem family, not a problem
"First warmer day" is "next strictly greater element to the right", dressed in weather. The same skeleton solves: next greater (496/503), stock span (901 — previous greater), histogram rectangles (84), trapping rain water (42). Learn the skeleton once, re-skin it forever.

---

## Tips & Tricks to Remember the Pattern 💡

### 🥞 The "impatient queue" mnemonic
Picture cold days standing in a line, each waiting for a warmer day. When a warm day walks in, it taps every colder person on the shoulder — *"I'm your answer"* — and they leave. Whoever remains is still colder-than-everyone-since... which is why the line always reads coldest-at-the-back, and why only the back of the line ever needs checking: a **stack**.

### 🔑 Pattern recognition triggers
- "**Next** / **previous** **greater** / **smaller** element" in any costume → monotonic stack
- "How long until a bigger X arrives" / "days until", "span of days since" → monotonic stack with **indices**
- You catch yourself writing a nested loop where the inner loop scans forward/backward for the first element beating the current one → replace it with a stack
- Answer for each position depends on **one nearest element in one direction** → this pattern (if it depends on a *range*, think sliding window / deque instead)

### 🧠 The template (four lines that solve a dozen problems)
```java
Deque<Integer> stack = new ArrayDeque<>(); // indices; values strictly decreasing
for (int i = 0; i < n; i++) {
    while (!stack.isEmpty() && arr[stack.peek()] < arr[i]) {
        answer[stack.pop()] = i;           // i is the "next greater" for the popped index
    }
    stack.push(i);
}
```
Dials to remember: `<` vs `<=` (strict vs non-strict "greater"), flip the comparison for "next **smaller**", iterate right-to-left (or capture `stack.peek()` after popping) for "**previous** greater".

### ⚠️ Common pitfalls interviewers watch for
1. **Pushing temperatures instead of indices** — you can't compute "how many days" without the index; indices give you both.
2. **Using `<=` instead of `<`** — an *equal* temperature is not *warmer*; equal days must stay on the stack. (Other family members, like histogram, flip this — know which dial you're setting.)
3. **Calling it O(n²) "because of the nested loop"** — under-claiming your own algorithm is as bad as over-claiming; the amortized argument is part of the solution.
4. **Forgetting the leftover-stack days** — initialize answers to 0 and they handle themselves; clearing the stack afterward is wasted code.
5. **`java.util.Stack`** — it's a synchronized legacy `Vector`; idiomatic Java uses `ArrayDeque` (say this — it's a free Java-fluency point).

### 🔗 Practice ladder (the monotonic stack family)
| Problem | Twist |
|---|---|
| 496. Next Greater Element I | The skeleton, plus a HashMap |
| 739. Daily Temperatures | Next greater as a distance (this one) |
| 901. Online Stock Span | Previous greater-or-equal, streaming |
| 503. Next Greater Element II | Circular array — loop the index twice |
| 84. Largest Rectangle in Histogram (Hard) | Next smaller on BOTH sides |
| 42. Trapping Rain Water (Hard) | Stack version of the water problem |

---

## What to Say in the Interview (Script)

1. *"Brute force re-scans the same cold streaks for every day — O(n²). Instead of each day searching forward, I'll let each arriving day answer the earlier days it beats."*
2. *"The days still waiting are always strictly decreasing in temperature — a warmer earlier day would already have answered a colder one. Add and remove only at the end of that sequence: that's a monotonic stack. I store indices, since the answer is a distance in days."*
3. *"Each new day pops every strictly colder top — popping answers that day as `today minus thatDay` — then pushes itself. Whatever survives to the end keeps answer 0."*
4. *"Time is amortized O(n): each index is pushed once and popped at most once, so total stack operations are bounded by 2n even though a single step can pop many. Space is O(n) for a strictly falling input."*

The sentence *"each element is pushed once and popped at most once — that's the whole O(n) proof"* is the one to land. 🚀