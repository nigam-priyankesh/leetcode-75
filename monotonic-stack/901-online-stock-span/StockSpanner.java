import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/// LeetCode 901. Online Stock Span
///
/// - **Pattern**: Monotonic Stack with run compression (previous greater element, streaming)
/// - **Time**: O(1) amortized per `next` call — n calls cost O(n) total
/// - **Space**: O(n) worst case — strictly falling prices keep every entry
///
/// Key insight: today's span = today + the spans of every earlier day whose
/// price is ≤ today's. Those days can never matter again — any future day
/// tall enough to see past today sees past them too — so pop each one and
/// ABSORB its span instead of recounting its days. The stack keeps only
/// (price, span) "runs" with strictly decreasing prices: each entry stands
/// for itself plus all the cheaper days it already swallowed.
public class StockSpanner {

    /// A surviving price and the length of the run it has absorbed
    /// (itself + all consecutive earlier days at or below this price).
    private record PriceSpan(int price, int span) {}

    /// Strictly decreasing prices, newest on top. An entry's span counts
    /// the days it dominates, so popped history is never recounted.
    private final Deque<PriceSpan> dominantRuns = new ArrayDeque<>();

    public int next(int price) {
        int span = 1; // today always counts itself

        // Absorb every run today dominates. NON-STRICT: an equal price
        // counts toward the span ("less than or equal to today's price").
        while (!dominantRuns.isEmpty() && dominantRuns.peek().price() <= price) {
            span += dominantRuns.pop().span();
        }

        dominantRuns.push(new PriceSpan(price, span));
        return span;
    }

    // ---------------------------------------------------------------
    // Quick self-test (run: java StockSpanner.java).
    // Java 25 instance main (JEP 512): no public/static/args needed,
    // solution methods callable directly, IO.println for console output.
    // ---------------------------------------------------------------
    void main() {
        // LeetCode example: prices stream in one at a time
        IO.println(Arrays.toString(spans(100, 80, 60, 70, 60, 75, 85)));
        // [1, 1, 1, 2, 1, 4, 6]

        // Strictly rising: every day absorbs all history → span = day number
        IO.println(Arrays.toString(spans(10, 20, 30, 40)));
        // [1, 2, 3, 4]

        // Strictly falling: nobody absorbs anything (worst-case stack growth)
        IO.println(Arrays.toString(spans(40, 30, 20, 10)));
        // [1, 1, 1, 1]

        // Equal prices: ≤ means equals COUNT — spans keep growing
        IO.println(Arrays.toString(spans(50, 50, 50)));
        // [1, 2, 3]

        // Valley then recovery: day at 90 sees over everything since 100
        IO.println(Arrays.toString(spans(100, 40, 70, 50, 90)));
        // [1, 1, 2, 1, 4]
    }

    /// Self-test helper: feed a whole price stream into a fresh spanner
    /// and collect the span reported for each day.
    private int[] spans(int... prices) {
        StockSpanner spanner = new StockSpanner();
        int[] result = new int[prices.length];
        for (int day = 0; day < prices.length; day++) {
            result[day] = spanner.next(prices[day]);
        }
        return result;
    }
}
