/**
 * LeetCode 1071. Greatest Common Divisor of Strings
 *
 * Pattern : Math + String (Euclidean Algorithm)
 * Time    : O(m + n)  — dominated by building/comparing the two concatenations
 * Space   : O(m + n)  — the two temporary concatenated strings
 *
 * Key insight:
 *   Strings under concatenation behave like integers under addition.
 *   1) A common divisor string exists  IFF  str1 + str2 == str2 + str1
 *      (Lyndon–Schützenberger theorem: two strings commute under
 *      concatenation only when both are repetitions of one common block).
 *   2) When it exists, the LARGEST divisor has length gcd(m, n) — the
 *      plain integer GCD of the two lengths — so we just return that prefix.
 */
public class GreatestCommonDivisorOfStrings {

    public String gcdOfStrings(String str1, String str2) {
        // ---------- Step 1: Existence check via commutativity ----------
        // If the two strings are built from a common block x, then both
        // orders of concatenation are just x repeated (m+n)/|x| times,
        // hence equal. If they are NOT equal, no common divisor exists.
        if (!(str1 + str2).equals(str2 + str1)) {
            return "";
        }

        // ---------- Step 2: Largest divisor length = gcd of lengths ----------
        int gcdLength = gcd(str1.length(), str2.length());

        // ---------- Step 3: The answer is simply that prefix ----------
        return str1.substring(0, gcdLength);
    }

    /**
     * Euclid's algorithm (~300 BC — the oldest algorithm still in use):
     * any common divisor of (a, b) also divides a % b, so the pair can be
     * shrunk without losing divisors until the remainder hits 0.
     * Runs in O(log(min(a, b))) steps (Lamé's theorem).
     */
    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // ---------------------------------------------------------------
    // Quick self-test (run: java GreatestCommonDivisorOfStrings.java —
    // Java 25 runs single-file sources directly, no compile step).
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        var solution = new GreatestCommonDivisorOfStrings();

        // Example 1: ABCABC = ABC×2, ABC = ABC×1 → "ABC"
        System.out.println(solution.gcdOfStrings("ABCABC", "ABC"));     // ABC

        // Example 2: ABABAB = AB×3, ABAB = AB×2 → "AB"
        System.out.println(solution.gcdOfStrings("ABABAB", "ABAB"));    // AB

        // Example 3: no common block → ""
        System.out.println(solution.gcdOfStrings("LEET", "CODE"));      // (empty)

        // Block longer than one repetition: ABAABA = ABA×2 → "ABA"
        System.out.println(solution.gcdOfStrings("ABAABA", "ABA"));     // ABA

        // Identical strings → the string itself
        System.out.println(solution.gcdOfStrings("AAAA", "AAAA"));      // AAAA
    }
}