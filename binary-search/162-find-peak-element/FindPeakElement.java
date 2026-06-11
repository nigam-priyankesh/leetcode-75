/// LeetCode 162. Find Peak Element
///
/// - **Pattern**: Binary Search on an Unsorted Array (slope descent)
/// - **Time**: O(log n) — the interval halves every comparison
/// - **Space**: O(1) — three scalars, iterative, no recursion stack
///
/// Key insight: binary search doesn't need a sorted array — it needs a rule
/// that discards half with CERTAINTY. The rule here is the slope at `mid`:
/// on an ascending slope a peak must exist to the right (values can't climb
/// past the `-∞` edge), on a descending slope a peak exists at `mid` or to
/// the left. Loop invariant: `[lo, hi]` always contains at least one peak.
public class FindPeakElement {

    public int findPeakElement(int[] nums) {
        int lo = 0;
        int hi = nums.length - 1;

        while (lo < hi) {
            // Rounds DOWN, so mid < hi and mid + 1 is always in bounds.
            int mid = lo + (hi - lo) / 2;

            if (nums[mid] > nums[mid + 1]) {
                hi = mid;      // descending slope ⬇ — mid beats its right neighbor, KEEP it as a candidate
            } else {
                lo = mid + 1;  // ascending slope ⬆ — mid is disqualified, a peak lies strictly right
            }
        }
        return lo; // == hi: the one index never disqualified — a peak
    }

    // ---------------------------------------------------------------
    // Quick self-test (run: java FindPeakElement.java).
    // Java 25 instance main (JEP 512): no public/static/args needed,
    // solution methods callable directly, IO.println for console output.
    // ---------------------------------------------------------------
    void main() {
        // Example 1: single clear peak
        IO.println(findPeakElement(new int[]{1, 2, 3, 1}));           // 2

        // Example 2: two peaks (indices 1 and 5) — converges to 5
        IO.println(findPeakElement(new int[]{1, 2, 1, 3, 5, 6, 4}));  // 5

        // Single element: trivially a peak (-∞ on both sides)
        IO.println(findPeakElement(new int[]{1}));                    // 0

        // Strictly ascending: peak is the last element
        IO.println(findPeakElement(new int[]{1, 2, 3, 4, 5}));        // 4

        // Strictly descending: peak is the first element
        IO.println(findPeakElement(new int[]{5, 4, 3, 2, 1}));        // 0
    }
}