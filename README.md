# LeetCode 75

This repository contains my solutions to the **LeetCode 75** study plan — a curated list of 75 essential problems covering the most important patterns and techniques for coding interviews.

## What you'll find here

- ✅ Solutions organized by topic, added one by one as I work through the plan
- 📝 **Detailed explanations** for each solution — the intuition, the approach, and why it works
- 💡 **Tips and tricks** on recognizing and applying common patterns
- ⏱️ Time and space complexity analysis for every solution

## Solved so far (4/75)

| # | Problem | Pattern | Solution | Explanation |
|---|---|---|---|---|
| 162 | Find Peak Element | Binary Search | [Java](binary-search/162-find-peak-element/FindPeakElement.java) | [Notes](binary-search/162-find-peak-element/EXPLANATION.md) |
| 643 | Maximum Average Subarray I | Sliding Window | [Java](sliding-window/643-maximum-average-subarray-i/MaximumAverageSubarray.java) | [Notes](sliding-window/643-maximum-average-subarray-i/EXPLANATION.md) |
| 875 | Koko Eating Bananas | Binary Search | [Java](binary-search/875-koko-eating-bananas/KokoEatingBananas.java) | [Notes](binary-search/875-koko-eating-bananas/EXPLANATION.md) |
| 1071 | Greatest Common Divisor of Strings | Array / String | [Java](array-string/1071-greatest-common-divisor-of-strings/GreatestCommonDivisorOfStrings.java) | [Notes](array-string/1071-greatest-common-divisor-of-strings/EXPLANATION.md) |

## Repository conventions

Every problem lives in `<topic>/<number>-<kebab-case-title>/`, where the topic folder matches the problem's LeetCode 75 category and roman numerals in titles become lowercase letters (e.g. `...-subarray-i`). Each folder contains:

- `EXPLANATION.md` — problem statement, intuition, step-by-step algorithm, complexity analysis, companies asking it, tips & tricks, and an interview script
- `<ClassName>.java` — a self-contained Java 25 solution (named class + instance `void main()` self-test), runnable directly with `java <ClassName>.java`

## Patterns covered

The LeetCode 75 plan spans the core patterns that show up again and again in interviews:

| Pattern | Examples |
|---|---|
| Array / String | Two pointers, prefix sums, in-place manipulation |
| Two Pointers | Fast/slow pointers, converging pointers |
| Sliding Window | Fixed and variable window sizes |
| Hash Map / Set | Frequency counting, lookups, deduplication |
| Stack | Monotonic stacks, matching pairs |
| Queue | BFS, sliding window maximum |
| Linked List | Reversal, cycle detection, merging |
| Binary Tree (DFS & BFS) | Traversals, path problems, level-order |
| Binary Search Tree | In-order properties, search/insert/delete |
| Graphs (DFS & BFS) | Connected components, shortest paths |
| Heap / Priority Queue | Top-K problems, scheduling |
| Binary Search | Search space reduction, boundaries |
| Backtracking | Combinations, permutations, constraint search |
| Dynamic Programming (1D & multi-D) | Memoization, tabulation, state transitions |
| Bit Manipulation | XOR tricks, bit counting |
| Intervals | Merging, overlap detection |
| Monotonic Stack | Next greater element, histogram problems |
| Trie | Prefix matching, word search |

## Goal

Build a strong, pattern-based intuition for problem solving — not just memorizing solutions, but understanding *when* and *why* each technique applies.

Happy coding! 🚀