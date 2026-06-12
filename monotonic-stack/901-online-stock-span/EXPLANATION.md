# 901. Online Stock Span

**Difficulty:** Medium | **Pattern:** Monotonic Stack with run compression (previous greater, streaming) | **LeetCode Link:** [Problem 901](https://leetcode.com/problems/online-stock-span/)

---

## Problem Statement

Design a class `StockSpanner` that receives daily stock prices **one at a time** (streaming / online) and, for each new price, returns its **span**: the number of consecutive days ending today where the price was **less than or equal to** today's price.

Implement:
- `StockSpanner()` — initializes the object
- `int next(int price)` — records today's price and returns today's span

### Example
```
Input:  next(100), next(80), next(60), next(70), next(60), next(75), next(85)
Output:    1          1         1         2         1         4         6

Day 6 (75): 75 ≥ 60, 70, 60, 75-itself? — counts 75, 60, 70, 60 → span 4 (stops at 80 > 75)
Day 7 (85): counts 85, 75, 60, 70, 60, 80 → span 6 (stops at 100 > 85)
```

### Constraints
- `1 <= price <= 10⁵`
- At most `10⁴` calls to `next`

---

## Asked By (Big Companies)

- **Amazon** — frequent in online assessments and phone screens
- **Bloomberg** — a stock-themed classic, naturally
- **Goldman Sachs**
- **Google**
- **Microsoft**
- **Adobe**

> 💼 The streaming wrapper is the point: you can't see future prices and you shouldn't re-scan old ones. Interviewers watch whether your `next` re-walks history (O(n) per call) or absorbs it (amortized O(1)). It's also a classic *design*-flavored question — state lives in the object across calls.

---

## Intuition — How to Think About It

### Step 1: What does brute force waste?
Keep all prices in a list; on each `next`, walk backwards counting days with price ≤ today. Worst case (rising prices: each day sees over all history) every call re-walks everything → O(n) per call, O(n²) for the stream. The waste: when today's 75 walks back over 60, 70, 60, it recounts days that *yesterday's walk already counted*. Counted information is being thrown away.

### Step 2: Absorb, don't recount
When today's price dominates an earlier day (≥ its price), that earlier day can **never matter again**: any future day tall enough to see past *today* is taller than today, hence taller than that earlier day too — it would see past it anyway. So the dominated day is permanently hidden behind today. Don't keep it; **pop it and take its span**:

> **span(today) = 1 + Σ span(every popped day)**

Each popped day's span already counts the days *it* swallowed earlier, so the sum reconstructs the full count without revisiting a single old price. That's the run-compression trick: a stack entry isn't one day, it's a **run** — a survivor plus everything it absorbed.

### Step 3: What survives is strictly decreasing
After popping everything ≤ today, what remains on the stack is strictly greater than today, top to bottom older and higher. The stack is monotonically **decreasing** — exactly like Daily Temperatures (739), with two dials flipped: we look **left** (previous greater) instead of right, and the comparison is **non-strict** (equal prices count toward the span, so they get popped).

### Visual Walkthrough (prices = 100, 80, 60, 70, 60, 75, 85)

Stack entries are `price(span)`, top on the right:

```
next(100): nothing to absorb            → push 100(1)   stack: 100(1)                 → 1
next(80):  80 < 100                     → push 80(1)    stack: 100(1) 80(1)           → 1
next(60):  60 < 80                      → push 60(1)    stack: 100(1) 80(1) 60(1)     → 1
next(70):  70 ≥ 60 → pop 60(1), span=2
           70 < 80 → stop               → push 70(2)    stack: 100(1) 80(1) 70(2)     → 2
next(60):  60 < 70                      → push 60(1)    stack: 100(1) 80(1) 70(2) 60(1) → 1
next(75):  75 ≥ 60 → pop 60(1), span=2
           75 ≥ 70 → pop 70(2), span=4
           75 < 80 → stop               → push 75(4)    stack: 100(1) 80(1) 75(4)     → 4
next(85):  85 ≥ 75 → pop 75(4), span=5
           85 ≥ 80 → pop 80(1), span=6
           85 < 100 → stop              → push 85(6)    stack: 100(1) 85(6)           → 6

Output: [1, 1, 1, 2, 1, 4, 6] ✅
```

Watch `next(75)`: it never touches the two 60s and the 70 individually — it absorbs `70(2)` whole, inheriting the day 70 already swallowed. History is counted exactly once, ever.

---

## Algorithm (Step by Step)

1. Maintain a stack of `(price, span)` pairs with strictly decreasing prices (a Java `record` is the idiomatic pair here).
2. On `next(price)`:
   - Start `span = 1` (today counts itself).
   - While the stack is non-empty **and** `top.price <= price`: pop, add the popped `span` to today's.
   - Push `(price, span)` and return `span`.

**Invariant** (state it!): *each stack entry's span equals the number of consecutive days it dominates (itself + everything it absorbed), and stack prices strictly decrease from bottom to top.* Popping therefore transfers ownership of a whole run in O(1), and the absorbed days are never visited again.

---

## Complexity Analysis (Detailed)

### Time Complexity: **O(1) amortized per call** — O(n) for n calls
- A single `next` can pop many entries (e.g., a new all-time high after a long decline pops everything). But every day is **pushed exactly once and popped at most once** across the entire stream — total stack operations ≤ 2n for n calls.
- Per-call amortized cost: O(1). Same banker's argument as 739: each push deposits the credit its future pop spends.
- For 10⁴ calls: ~2 × 10⁴ operations vs. up to ~5 × 10⁷ for the rescan-history approach — and the gap widens with stream length.

### Space Complexity: **O(n)** worst case
- Strictly falling prices (`100, 99, 98, …`) absorb nothing — all n entries survive on the stack.
- Strictly rising prices keep the stack at a single entry (each day absorbs everything).
- Note what we *don't* store: the raw price history. The stack alone — compressed runs — is sufficient state. That's the memory win of run compression in a genuinely long stream.

---

## Important Theorems & Concepts 📚

### 1. Run compression (weighted stack entries)
The upgrade over a plain monotonic stack: entries carry a **weight** (the run length absorbed so far), so popping transfers a whole block of history in O(1). The same trick powers histogram problems (84) where popped bars carry widths, and is conceptually the path-compression idea from Union-Find: flatten what you've already traversed so it's never traversed again.

### 2. Online vs offline algorithms
An **online** algorithm must answer after each input without seeing the future (this problem); an **offline** one sees the whole input first (739). The monotonic stack is one of the few structures that's naturally online — it only ever reasons about the past. Notice 739 ↔ 901 are mirror twins: next-greater looking right with strict `<`, previous-greater looking left with non-strict `<=`.

### 3. Span = distance to previous strictly-greater element
`span(i) = i − j` where `j` is the nearest earlier index with `price[j] > price[i]` (or span = i + 1 if none). The "sum of popped spans" and "distance to previous greater" formulations are the same number — recognizing such reformulations is what lets you map a story problem onto a known skeleton.

---

## Tips & Tricks to Remember the Pattern 💡

### 👑 The "corporate ladder" mnemonic
Each new hire absorbs the whole team of everyone they outrank — and the absorbed teams' teams, transitively. When a bigger shot arrives later, they take over your *entire org* in one handshake instead of meeting every employee. The stack is the chain of command: strictly decreasing rank, each entry owning everything beneath it.

### 🔑 Pattern recognition triggers
- "**Span**", "consecutive days/elements ending here with values ≤ current" → previous-greater monotonic stack with run compression
- **Streaming** ("design a class", "calls arrive one at a time") + per-element nearest-bigger question → this exact shape; the stack is the object's state
- You're about to store full history and re-scan it per query → ask what part of history is *permanently shadowed* and compress it away

### 🧠 The template (the 739 skeleton, two dials flipped)
```java
private record PriceSpan(int price, int span) {}
private final Deque<PriceSpan> stack = new ArrayDeque<>();

public int next(int price) {
    int span = 1;
    while (!stack.isEmpty() && stack.peek().price() <= price) {
        span += stack.pop().span();   // absorb the whole run, O(1)
    }
    stack.push(new PriceSpan(price, span));
    return span;
}
```
Dials vs 739: direction (previous, not next) and `<=` (equal prices count). The skeleton — pop violators, act on each pop, push self — is identical.

### ⚠️ Common pitfalls interviewers watch for
1. **Using strict `<` instead of `<=`** — the span counts days "less than **or equal to** today"; equal prices must be popped and absorbed. (739 is the opposite — know which dial each problem sets.)
2. **Storing only prices and recounting on pop** — without the span weight you re-walk history and you're back to O(n) per call. The pair is the whole trick.
3. **Storing indices and computing `i − j`** — also correct (span = distance to previous strictly greater)! But then *don't* sum spans; mixing the two formulations double-counts.
4. **Forgetting today counts itself** — initialize `span = 1`, not 0.
5. **Claiming O(1) worst-case per call** — a single call can pop O(n); the right claim is **amortized** O(1). Precision here is a senior signal.

### 🔗 Practice ladder (the monotonic stack family)
| Problem | Twist |
|---|---|
| 496. Next Greater Element I | The skeleton, plus a HashMap |
| 739. Daily Temperatures | Next greater as a distance (offline twin) |
| 901. Online Stock Span | Previous greater, streaming, run compression (this one) |
| 503. Next Greater Element II | Circular array — loop the index twice |
| 84. Largest Rectangle in Histogram (Hard) | Run compression with widths, both directions |
| 42. Trapping Rain Water (Hard) | Stack version of the water problem |

---

## What to Say in the Interview (Script)

1. *"Rescanning history on every call is O(n) per call. The key observation: once today's price dominates an earlier day, that day is permanently shadowed — any future day that sees past today is taller than today, so it sees past that day too."*
2. *"So I keep a stack of (price, span) runs with strictly decreasing prices — each entry owns itself plus everything it absorbed. A new price pops every run it dominates, sums their spans in O(1) per pop, and pushes itself as a new run."*
3. *"The comparison is non-strict — equal prices count toward the span, so they get absorbed too."*
4. *"Each day is pushed once and popped at most once, so n calls cost O(n) total — amortized O(1) per call, not worst-case O(1); a new all-time high can pop a lot. Space is O(n) for a falling market, and I never store raw history — the compressed runs are sufficient state."*

The sentence *"popped history is absorbed, not recounted — that's why the stream costs O(n) total"* is the one to land. 🚀
