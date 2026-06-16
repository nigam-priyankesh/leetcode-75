import java.util.ArrayList;
import java.util.List;

/// LeetCode 328. Odd Even Linked List
///
/// - **Pattern**: Two-pointer in-place pointer rewiring (de-interleave two sublists)
/// - **Time**: O(n) — one pass, each node's `next` re-linked once
/// - **Space**: O(1) — only a handful of pointers; no new nodes allocated
///
/// Key insight: don't move the VALUES — re-route the `next` pointers. Walk
/// two cursors in lockstep, one threading the odd-position nodes, one
/// threading the even-position nodes. At the end, splice the whole even
/// chain onto the tail of the odd chain. The trick that makes it O(1) and
/// bug-free: stash the even list's HEAD before the loop, because the odd
/// cursor's `.next` walk destroys the path back to it.
public class OddEvenLinkedList {

    public ListNode oddEvenList(ListNode head) {
        // 0 or 1 nodes: already trivially grouped, nothing to rewire.
        if (head == null || head.next == null) return head;

        ListNode oddTail = head;             // builds the odd-position chain (1st, 3rd, 5th, …)
        ListNode evenTail = head.next;       // builds the even-position chain (2nd, 4th, 6th, …)
        ListNode evenHead = evenTail;        // SAVE: where we reattach the evens after the odds

        // evenTail leads the walk; it hits null first (on odd-length lists) or
        // evenTail.next hits null first (on even-length lists). One guard covers both.
        while (evenTail != null && evenTail.next != null) {
            oddTail.next = evenTail.next;    // odd grabs the node two ahead (skip the even)
            oddTail = oddTail.next;          // advance the odd cursor onto it
            evenTail.next = oddTail.next;    // even grabs the node two ahead (skip the odd)
            evenTail = evenTail.next;        // advance the even cursor onto it
        }

        oddTail.next = evenHead;             // splice the even chain after the last odd node
        return head;
    }

    /// LeetCode's singly-linked list node. Nested + static so it can't collide
    /// with the ListNode of any future linked-list problem in this repo, while
    /// the method signature stays identical to the judge's.
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    // ---------------------------------------------------------------
    // Quick self-test (run: java OddEvenLinkedList.java).
    // Java 25 instance main (JEP 512): no public/static/args needed,
    // solution methods callable directly, IO.println for console output.
    // ---------------------------------------------------------------
    void main() {
        // Example 1: 1->2->3->4->5  → odds {1,3,5} then evens {2,4} → 1 3 5 2 4
        IO.println(toList(oddEvenList(build(1, 2, 3, 4, 5))));        // [1, 3, 5, 2, 4]

        // Example 2: 2->1->3->5->6->4->7 → odds {2,3,6,7} then evens {1,5,4}
        IO.println(toList(oddEvenList(build(2, 1, 3, 5, 6, 4, 7))));  // [2, 3, 6, 7, 1, 5, 4]

        // Even length: trailing even node must still attach cleanly
        IO.println(toList(oddEvenList(build(1, 2, 3, 4))));           // [1, 3, 2, 4]

        // Two nodes: one odd, one even — order unchanged
        IO.println(toList(oddEvenList(build(1, 2))));                 // [1, 2]

        // Single node and empty list: returned as-is
        IO.println(toList(oddEvenList(build(99))));                  // [99]
        IO.println(toList(oddEvenList(build())));                    // []
    }

    /// Build a linked list from values; used only by the self-tests.
    private ListNode build(int... values) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        for (int value : values) {
            tail.next = new ListNode(value);
            tail = tail.next;
        }
        return dummy.next;
    }

    /// Serialize a linked list to a List for readable test output.
    private List<Integer> toList(ListNode head) {
        List<Integer> values = new ArrayList<>();
        for (ListNode node = head; node != null; node = node.next) {
            values.add(node.val);
        }
        return values;
    }
}
