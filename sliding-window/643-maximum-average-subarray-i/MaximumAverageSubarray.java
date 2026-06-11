/**
 * LeetCode 643. Maximum Average Subarray I
 *
 * Pattern : Fixed-Size Sliding Window
 * Time    : O(n)  — each element enters the window once and leaves once
 * Space   : O(1)  — only a few scalar variables, no extra data structures
 *
 * Key insight:
 *   Maximizing the AVERAGE of a window of fixed length k is identical to
 *   maximizing its SUM (the divisor k never changes). So we track integer
 *   sums while sliding and perform exactly ONE floating-point division at
 *   the end — faster and free of accumulated rounding error.
 */
public class MaximumAverageSubarray {

    public double findMaxAverage(int[] nums, int k) {
        // ---------- Step 1: Build the first window of size k ----------
        // This is the only place we do k additions; every later window
        // is derived from this one in O(1).
        int windowSum = 0;
        for (int i = 0; i < k; i++) {
            windowSum += nums[i];
        }

        int maxWindowSum = windowSum;

        // ---------- Step 2: Slide the window across the array ----------
        // windowEnd is the index of the element ENTERING the window.
        // The element LEAVING the window is exactly k positions behind it.
        for (int windowEnd = k; windowEnd < nums.length; windowEnd++) {
            int entering = nums[windowEnd];        // new element on the right
            int leaving  = nums[windowEnd - k];    // old element on the left

            windowSum += entering - leaving;       // O(1) slide — the whole trick!

            maxWindowSum = Math.max(maxWindowSum, windowSum);
        }

        // ---------- Step 3: Divide ONCE, with a double cast ----------
        // Casting before dividing avoids the classic integer-division bug
        // (51 / 4 == 12 in int math, but we need 12.75).
        return (double) maxWindowSum / k;
    }

    // ---------------------------------------------------------------
    // Quick self-test (run: java MaximumAverageSubarray.java).
    // Java 25 instance main (JEP 512): no public/static/args needed,
    // and solution methods are callable directly — no `new` required.
    // ---------------------------------------------------------------
    void main() {
        // Example 1: best window is [12, -5, -6, 50] → 51 / 4 = 12.75
        System.out.println(findMaxAverage(new int[]{1, 12, -5, -6, 50, 3}, 4)); // 12.75

        // Example 2: single element array
        System.out.println(findMaxAverage(new int[]{5}, 1));                    // 5.0

        // Edge case: all negatives — max average is the "least negative" window
        System.out.println(findMaxAverage(new int[]{-1, -2, -3, -4}, 2));       // -1.5
    }
}
