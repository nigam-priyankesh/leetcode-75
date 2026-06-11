# 643. Maximum Average Subarray I

**Difficulty:** Easy | **Pattern:** Sliding Window (Fixed Size) | **LeetCode Link:** [Problem 643](https://leetcode.com/problems/maximum-average-subarray-i/)

---

## Problem Statement

You are given an integer array `nums` consisting of `n` elements, and an integer `k`.

Find a **contiguous subarray** whose length is **exactly `k`** that has the **maximum average value**, and return this value. Any answer with a calculation error less than `10⁻⁵` will be accepted.

### Example 1
```
Input:  nums = [1, 12, -5, -6, 50, 3], k = 4
Output: 12.75
Explanation: Maximum average is (12 - 5 - 6 + 50) / 4 = 51 / 4 = 12.75
```

### Example 2
```
Input:  nums = [5], k = 1
Output: 5.0
```

---

## Asked By (Big Companies)

This question is frequently asked at:

- **Meta (Facebook)** — very frequent, often as a warm-up before harder sliding window follow-ups
- **Amazon** — appears in online assessments (OA) and phone screens
- **Google** — used as a screening question to test optimization instincts
- **Microsoft**
- **Bloomberg**
- **Apple**
- **Adobe**

> 💼 Interviewers love this problem because the brute force is obvious, and they want to see if you *instantly* recognize the sliding window optimization.

---

## Intuition — How to Think About It

### Step 1: Understand what's being asked
Maximizing the **average** of a subarray of fixed length `k` is the same as maximizing the **sum** of that subarray (since we always divide by the same `k`). So the problem reduces to:

> **Find the maximum sum of any contiguous subarray of length `k`, then divide by `k` once at the end.**

This reframing is the first thing that impresses an interviewer — say it out loud!

### Step 2: Spot the inefficiency in brute force
Brute force computes the sum of every window of size `k` from scratch:

```
Window 1: [1, 12, -5, -6]          → sum = 2
Window 2:    [12, -5, -6, 50]      → sum = 51
Window 3:        [-5, -6, 50, 3]   → sum = 42
```

Notice that **consecutive windows share `k - 1` elements**! Recomputing them is wasted work — that's `O(n × k)` time.

### Step 3: The Sliding Window insight
When the window slides one step to the right:
- **One new element enters** on the right → add it
- **One old element leaves** on the left → subtract it

```
newWindowSum = oldWindowSum + nums[entering] - nums[leaving]
```

Each slide is now `O(1)` instead of `O(k)`. Total: `O(n)`.

### Visual Walkthrough (nums = [1, 12, -5, -6, 50, 3], k = 4)

```
Initial window:  [1, 12, -5, -6] 50  3     windowSum = 2,  maxSum = 2

Slide right →     1 [12, -5, -6, 50] 3     windowSum = 2 - 1 + 50 = 51,  maxSum = 51

Slide right →     1  12 [-5, -6, 50, 3]    windowSum = 51 - 12 + 3 = 42, maxSum = 51

Answer = maxSum / k = 51 / 4 = 12.75 ✅
```

---

## Algorithm (Step by Step)

1. **Build the first window:** sum the first `k` elements. This is your initial `windowSum` and initial `maxWindowSum`.
2. **Slide the window** from index `k` to `n - 1`:
   - Add the element entering the window: `nums[windowEnd]`
   - Subtract the element leaving the window: `nums[windowEnd - k]`
   - Update `maxWindowSum` if the new `windowSum` is larger.
3. **Return** `maxWindowSum / k` as a `double` (divide only once, at the very end).

---

## Complexity Analysis (Detailed)

### Time Complexity: **O(n)**
- Building the first window: `k` additions → `O(k)`
- Sliding the window: `(n - k)` slides, each doing exactly **one addition, one subtraction, one comparison** → `O(n - k)`
- Total: `O(k) + O(n - k) = O(n)` — every element is touched **exactly once** when it enters the window and **exactly once** when it leaves.
- Compare with brute force: `O((n − k + 1) × k)` — worst when `k ≈ n/2`: for `n = 10⁵` that's `(5×10⁴ + 1) × 5×10⁴ ≈ 2.5 × 10⁹` operations (too slow) vs. `10⁵` for sliding window. (Note: `k = n` is *not* the worst case — there's only one window then.)

### Space Complexity: **O(1)**
- We only use three variables: `windowSum`, `maxWindowSum`, and a loop index.
- No extra arrays, no prefix sums needed — constant auxiliary space regardless of input size.

---

## Tips & Tricks to Remember the Pattern 💡

### 🪟 The "Train Window" mnemonic
Imagine looking out of a moving train window: as the train moves, **one tree enters your view and one tree exits**. You never re-scan the whole landscape — you just account for what changed. That's exactly `+ entering - leaving`.

### 🔑 Pattern recognition triggers
Reach for **fixed-size sliding window** whenever you see:
- "subarray / substring of **size exactly k**"
- "**contiguous** elements"
- maximize / minimize / count something over all windows of size `k`

### 🧠 The template (memorize this skeleton — it solves dozens of problems)
```java
// 1. Build first window of size k
// 2. for (int end = k; end < n; end++)
//        windowSum += nums[end] - nums[end - k];  // slide in O(1)
//        update answer
```

### ⚠️ Common pitfalls interviewers watch for
1. **Integer division bug:** `maxSum / k` with two ints truncates! Cast to double: `(double) maxSum / k`.
2. **Dividing inside the loop:** averages introduce floating-point error and waste time — compare **sums**, divide **once** at the end.
3. **Overflow awareness:** mention that `n ≤ 10⁵` and `|nums[i]| ≤ 10⁴`, so max sum ≈ `10⁹` fits in `int`, but using `long`/keeping it in mind shows maturity.

### 📚 Related concept: Prefix Sums
This problem can also be solved with a **prefix sum array** (`sum(i..j) = prefix[j+1] - prefix[i]`), but that costs `O(n)` extra space. Sliding window is a *space-optimized special case* of prefix sums when the window size is fixed. Mentioning this trade-off is a great interview move.

### 🔗 Practice ladder (same pattern, increasing difficulty)
| Problem | Twist |
|---|---|
| 643. Maximum Average Subarray I | Pure fixed window (this one) |
| 1456. Max Vowels in Substring of Given Length | Fixed window + counting |
| 1004. Max Consecutive Ones III | **Variable** window |
| 209. Minimum Size Subarray Sum | Variable window + shrinking |
| 239. Sliding Window Maximum | Fixed window + monotonic deque |

---

## What to Say in the Interview (Script)

1. *"Maximizing the average of a fixed-length window is the same as maximizing its sum, since k is constant."*
2. *"Brute force is O(n·k), but consecutive windows overlap in k−1 elements, so I'll use a sliding window: add the entering element, subtract the leaving one — O(1) per slide."*
3. *"I'll track sums as integers and divide by k only once at the end, cast to double, to avoid floating-point error and the integer-division trap."*
4. *"Total: O(n) time, O(1) space."*

Saying these four sentences before writing a line of code is what separates a good candidate from a great one. 🚀
