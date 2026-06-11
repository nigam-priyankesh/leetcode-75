# 1071. Greatest Common Divisor of Strings

**Difficulty:** Easy | **Pattern:** Math + String (Euclidean Algorithm) | **LeetCode Link:** [Problem 1071](https://leetcode.com/problems/greatest-common-divisor-of-strings/)

---

## Problem Statement

For two strings `s` and `t`, we say *"`t` divides `s`"* if and only if `s = t + t + t + ... + t` (i.e., `t` concatenated with itself one or more times).

Given two strings `str1` and `str2`, return the **largest string `x`** such that `x` divides **both** `str1` and `str2`. If no such string exists, return `""`.

### Example 1
```
Input:  str1 = "ABCABC", str2 = "ABC"
Output: "ABC"            (ABCABC = ABC×2, ABC = ABC×1)
```

### Example 2
```
Input:  str1 = "ABABAB", str2 = "ABAB"
Output: "AB"             (ABABAB = AB×3, ABAB = AB×2)
```

### Example 3
```
Input:  str1 = "LEET", str2 = "CODE"
Output: ""               (no common divisor string exists)
```

---

## Asked By (Big Companies)

- **Google** — likes it because the elegant solution requires a *proof*, not just code
- **Amazon** — appears in phone screens and OAs
- **Adobe** — frequently reported
- **Microsoft**
- **Apple**
- **IBM**

> 💼 This problem is a favorite because there's a brute-force way, a clean way, and a *beautiful* way. Reaching the beautiful way shows mathematical maturity.

---

## Intuition — How to Think About It

### Step 1: What must a "string divisor" look like?
If `x` divides `str1`, then `str1` is just `x` repeated. Same for `str2`. So **both strings are built from the same repeating block** — they are "multiples" of `x`, exactly like integers.

This analogy is the whole problem: **strings under concatenation behave like integers under addition.**

| Integers | Strings |
|---|---|
| 12 = 4 + 4 + 4 | `"ABABAB"` = `"AB"+"AB"+"AB"` |
| gcd(12, 8) = 4 | gcdOfStrings(`"ABABAB"`, `"ABAB"`) = `"AB"` |

### Step 2: The magic check — `str1 + str2 == str2 + str1`
**Claim:** A common divisor string exists **if and only if** `str1 + str2` equals `str2 + str1`.

**Why (intuition for both directions):**
- **(⇐) If they share a base block `x`:** then `str1 = x×m` and `str2 = x×n`, so both `str1+str2` and `str2+str1` are just `x` repeated `m+n` times — identical. ✅
- **(⇒) If `str1+str2 == str2+str1`:** swapping the order of concatenation changes nothing, which is only possible when both strings are repetitions of one common building block. (This is the **Lyndon–Schützenberger theorem**: two strings *commute* under concatenation ⇔ they are powers of a common string. See the theorem section below.)

### Step 3: If a divisor exists, which one is the largest?
If both strings are repetitions of block `x` with `|x| = L`, then `L` divides `len(str1)` **and** `len(str2)`. The largest such `L` is exactly **`gcd(len(str1), len(str2))`** — the integer GCD!

So the answer is simply: **the first `gcd(len1, len2)` characters of either string.**

### Visual Walkthrough (str1 = "ABABAB", str2 = "ABAB")

```
Check:  str1 + str2 = "ABABAB" + "ABAB" = "ABABABABAB"
        str2 + str1 = "ABAB" + "ABABAB" = "ABABABABAB"   → equal ✅ divisor exists

gcd(6, 4) = 2

Answer: first 2 chars of str1 = "AB" ✅
```

Counter-example (str1 = "LEET", str2 = "CODE"):
```
"LEET" + "CODE" = "LEETCODE"
"CODE" + "LEET" = "CODELEET"   → different ❌ return ""
```

---

## Algorithm (Step by Step)

1. **Commutativity check:** if `(str1 + str2) != (str2 + str1)`, no common divisor exists → return `""`.
2. **Compute integer GCD** of the two lengths using the **Euclidean algorithm**: `gcd(a, b) = gcd(b, a mod b)` until `b == 0`.
3. **Return the prefix** of `str1` of length `gcd(len1, len2)`.

Three lines of logic. That's the entire solution.

---

## Complexity Analysis (Detailed)

Let `m = str1.length()` and `n = str2.length()`.

### Time Complexity: **O(m + n)**
- Building `str1 + str2` and `str2 + str1`: each concatenation copies `m + n` characters → `O(m + n)`.
- Comparing the two concatenations: at most `m + n` character comparisons → `O(m + n)`.
- Euclidean GCD on the lengths: `O(log(min(m, n)))` iterations — by **Lamé's theorem**, the number of steps is at most ~5× the number of digits of the smaller number (worst case: consecutive Fibonacci numbers). Negligible next to the string work.
- Substring extraction: `O(gcd(m, n))` ≤ `O(min(m, n))`.
- **Total: O(m + n) + O(log min(m, n)) = O(m + n).**

### Space Complexity: **O(m + n)**
- The two temporary concatenated strings each hold `m + n` characters → `O(m + n)`.
- The recursive GCD uses `O(log(min(m, n)))` stack frames (or `O(1)` if written iteratively) — dominated by the strings.
- (A character-by-character comparison without building concatenations can reduce space to `O(gcd(m,n))` for just the answer, but the `O(m+n)` version is cleaner and is the expected interview answer.)

---

## Important Theorems & Concepts 📚

### 1. Euclidean Algorithm (~300 BC, Euclid's *Elements* — the oldest algorithm still in use!)
```
gcd(a, b) = gcd(b, a mod b),  with  gcd(a, 0) = a
```
**Why it works:** any number that divides both `a` and `b` also divides `a mod b` (since `a mod b = a − qb`). So the set of common divisors never changes as we shrink the pair — only the numbers get smaller, until one hits 0.

### 2. Lyndon–Schützenberger Theorem (the proof behind the magic check)
> Two strings `s` and `t` satisfy `s + t = t + s` **iff** there exists a string `x` with `s = xᵐ` and `t = xⁿ`.

In plain words: **strings commute under concatenation only when they're powers of the same block.** This is why one equality check replaces all the divisor-hunting work.

### 3. Lamé's Theorem (why GCD is fast)
The Euclidean algorithm on numbers up to `N` takes `O(log N)` steps; the worst case is consecutive Fibonacci numbers. Mentioning this when asked "how fast is gcd?" is a strong signal.

---

## Tips & Tricks to Remember the Pattern 💡

### 🧮 The "strings are numbers" mnemonic
Whenever a problem says *"X repeated k times"*, think: **concatenation = addition, repetition = multiplication.** Then ask, "what would I do if these were integers?" — here, you'd compute a GCD, so do exactly that on the *lengths*.

### 🔑 Pattern recognition triggers
- "string `t` **divides** string `s`" → GCD analogy
- "smallest repeating unit / repeated substring pattern" → same family (see LC 459)
- Two things built from a common building block → check if they **commute** (`a+b == b+a`)

### 🧠 The 3-line template (memorize)
```java
if (!(str1 + str2).equals(str2 + str1)) return "";
return str1.substring(0, gcd(str1.length(), str2.length()));
// gcd(a, b) = b == 0 ? a : gcd(b, a % b)
```

### ⚠️ Common pitfalls interviewers watch for
1. **Brute-forcing prefixes** (trying every prefix length from largest down, checking divisibility) — works in `O((m+n)·min(m,n))` but misses the elegance. Mention it, then improve it.
2. **Checking only one direction** (e.g., only that the prefix divides `str1`) — the commutativity check covers both strings at once.
3. **Forgetting the existence check entirely** and returning `prefix(gcd)` blindly — fails on `"LEET"/"CODE"`.
4. Writing your own GCD wrong: remember the base case is `b == 0 → a`, **not** `b == 1`.

### 🔗 Practice ladder (same family)
| Problem | Twist |
|---|---|
| 1071. GCD of Strings | Commutativity + GCD (this one) |
| 459. Repeated Substring Pattern | Is a string a power of a smaller block? |
| 686. Repeated String Match | How many repeats until containment? |
| 28. Find the Index of First Occurrence | String matching fundamentals (KMP) |

---

## What to Say in the Interview (Script)

1. *"Strings under concatenation behave like integers under addition — 'divides' here means the string is the block repeated, so I expect a GCD-style answer."*
2. *"A common divisor exists iff `str1 + str2 == str2 + str1`. That's the Lyndon–Schützenberger theorem: strings commute only when they're powers of a common block."*
3. *"If it exists, the largest divisor has length exactly `gcd(len1, len2)`, so I return that prefix — using Euclid's algorithm for the integer GCD."*
4. *"Time is O(m+n) for the concatenation check; space O(m+n) for the temporaries. The GCD itself is only O(log min(m,n)) by Lamé's theorem."*

Dropping the theorem name *with* a one-line intuition for it is the kind of detail interviewers remember. 🚀