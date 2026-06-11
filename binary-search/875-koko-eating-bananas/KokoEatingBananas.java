/// LeetCode 875. Koko Eating Bananas
///
/// - **Pattern**: Binary Search on the Answer (search the solution space, not an array)
/// - **Time**: O(n log M) — M = max pile; the speed range halves each step (≤ 30 checks for 10⁹)
/// - **Space**: O(1) — three scalars, no auxiliary structures
///
/// Key insight: feasibility is *monotonic* in the eating speed — if Koko can
/// finish at speed `k`, she can finish at every faster speed. One flip from
/// false to true, never back. A monotone predicate is the license to binary
/// search: hunt the FIRST feasible speed in `[1, max(piles)]`.
public class KokoEatingBananas {

    public int minEatingSpeed(int[] piles, int h) {
        // Answer space bounds: speed 1 is the slowest imaginable; anything
        // beyond the largest pile is wasted, since a pile still costs at
        // least one full hour no matter how fast she eats.
        int slowestSpeed = 1;
        int fastestSpeed = 0;
        for (int pile : piles) {
            fastestSpeed = Math.max(fastestSpeed, pile);
        }

        // "First true" template. Invariant: the answer always lies inside
        // [slowestSpeed, fastestSpeed] — everything below slowestSpeed is
        // proven infeasible, fastestSpeed is feasible (or not yet ruled out).
        while (slowestSpeed < fastestSpeed) {
            int candidateSpeed = slowestSpeed + (fastestSpeed - slowestSpeed) / 2; // overflow-safe midpoint

            if (canFinishInTime(piles, candidateSpeed, h)) {
                fastestSpeed = candidateSpeed;     // feasible — answer is this speed or slower; KEEP the candidate
            } else {
                slowestSpeed = candidateSpeed + 1; // infeasible — answer is strictly faster
            }
        }
        return slowestSpeed; // == fastestSpeed: the minimal feasible speed
    }

    /// The monotonic predicate: can Koko clear every pile within the hour
    /// limit at this speed? A pile of `p` bananas costs `⌈p / speed⌉` hours —
    /// she stops at a pile's end even if she has appetite left that hour.
    private boolean canFinishInTime(int[] piles, int speed, int hourLimit) {
        long hoursNeeded = 0; // worst case 10⁴ piles × 10⁹ hours = 10¹³ — overflows int, needs long
        for (int pile : piles) {
            // Math.ceilDiv (Java 18+) is overflow-free; the classic
            // (pile + speed - 1) / speed can exceed Integer.MAX_VALUE here.
            hoursNeeded += Math.ceilDiv(pile, speed);
        }
        return hoursNeeded <= hourLimit;
    }

    // ---------------------------------------------------------------
    // Quick self-test (run: java KokoEatingBananas.java).
    // Java 25 instance main (JEP 512): no public/static/args needed,
    // solution methods callable directly, IO.println for console output.
    // ---------------------------------------------------------------
    void main() {
        // Example 1: speed 4 → 1+2+2+3 = 8 hours, exactly h
        IO.println(minEatingSpeed(new int[]{3, 6, 7, 11}, 8));        // 4

        // Example 2: hours == piles → every pile in one hour → speed = max pile
        IO.println(minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 5));  // 30

        // Example 3: one spare hour lets her drop from 30 to 23
        IO.println(minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 6));  // 23

        // Overflow guard: one huge pile, barely too few hours for speed 1
        IO.println(minEatingSpeed(new int[]{312_884_470}, 312_884_469)); // 2

        // Plenty of time: h far exceeds total bananas → minimum speed 1
        IO.println(minEatingSpeed(new int[]{1, 1, 1}, 1_000_000_000));   // 1
    }
}