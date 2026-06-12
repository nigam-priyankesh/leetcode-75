import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/// LeetCode 739. Daily Temperatures
///
/// - **Pattern**: Monotonic Stack (next greater element)
/// - **Time**: O(n) amortized — every index is pushed once and popped at most once
/// - **Space**: O(n) — the stack holds up to n indices (strictly decreasing temps)
///
/// Key insight: a day stays "unanswered" only while every later day is colder,
/// so the unanswered days always form a strictly DECREASING temperature
/// sequence — keep them on a stack. Each new day pops and answers every
/// colder day it beats, then waits on the stack for its own warmer day.
/// The stack stores INDICES (not temperatures) because the answer is a
/// distance in days, not a temperature.
public class DailyTemperatures {

    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] daysUntilWarmer = new int[n]; // default 0 = "no warmer day ever comes"

        // Indices of days still waiting for a warmer day; their temperatures
        // are strictly decreasing from the bottom of the stack to the top.
        Deque<Integer> pendingDays = new ArrayDeque<>();

        for (int today = 0; today < n; today++) {
            // Today answers every pending day that is strictly colder.
            while (!pendingDays.isEmpty()
                    && temperatures[pendingDays.peek()] < temperatures[today]) {
                int colderDay = pendingDays.pop();
                daysUntilWarmer[colderDay] = today - colderDay;
            }
            pendingDays.push(today); // today now waits for ITS warmer day
        }
        // Days left on the stack never met a warmer day — they keep the default 0.
        return daysUntilWarmer;
    }

    // ---------------------------------------------------------------
    // Quick self-test (run: java DailyTemperatures.java).
    // Java 25 instance main (JEP 512): no public/static/args needed,
    // solution methods callable directly, IO.println for console output.
    // ---------------------------------------------------------------
    void main() {
        // Example 1: the classic mixed week
        IO.println(Arrays.toString(
                dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73})));
        // [1, 1, 4, 2, 1, 1, 0, 0]

        // Example 2: strictly rising — every answer is 1, stack never grows past one
        IO.println(Arrays.toString(dailyTemperatures(new int[]{30, 40, 50, 60})));
        // [1, 1, 1, 0]

        // Example 3
        IO.println(Arrays.toString(dailyTemperatures(new int[]{30, 60, 90})));
        // [1, 1, 0]

        // Strictly falling — nothing is ever answered, stack holds all n (worst-case space)
        IO.println(Arrays.toString(dailyTemperatures(new int[]{90, 80, 70})));
        // [0, 0, 0]

        // All equal — equal is NOT warmer (strict <), so all zeros
        IO.println(Arrays.toString(dailyTemperatures(new int[]{50, 50, 50})));
        // [0, 0, 0]
    }
}
