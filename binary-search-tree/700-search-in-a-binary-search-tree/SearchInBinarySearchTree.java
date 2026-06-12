import java.util.ArrayList;
import java.util.List;

/// LeetCode 700. Search in a Binary Search Tree
///
/// - **Pattern**: BST-guided descent (binary search shaped like a tree)
/// - **Time**: O(h) — h = tree height: O(log n) balanced, O(n) skewed
/// - **Space**: O(1) — iterative descent, no recursion stack
///
/// Key insight: the BST ordering invariant — everything in the left subtree
/// is smaller, everything in the right subtree is larger — means ONE
/// comparison at each node discards an entire subtree. That is binary
/// search; the "array" is just laid out as a tree.
public class SearchInBinarySearchTree {

    public TreeNode searchBST(TreeNode root, int val) {
        TreeNode current = root;

        // Walk down: each comparison eliminates one whole subtree.
        while (current != null && current.val != val) {
            current = val < current.val
                    ? current.left    // target is smaller → it can only live on the left
                    : current.right;  // target is larger  → it can only live on the right
        }
        return current; // the matching node, or null if we fell off the tree
    }

    // ---------------------------------------------------------------
    // Quick self-test (run: java SearchInBinarySearchTree.java).
    // Java 25 instance main (JEP 512): no public/static/args needed,
    // solution methods callable directly, IO.println for console output.
    // ---------------------------------------------------------------
    void main() {
        //        4
        //       / \
        //      2   7
        //     / \
        //    1   3
        TreeNode root = new TreeNode(4,
                new TreeNode(2, new TreeNode(1), new TreeNode(3)),
                new TreeNode(7));

        // Example 1: val = 2 → return the subtree rooted at 2
        IO.println(preorder(searchBST(root, 2)));  // [2, 1, 3]

        // Example 2: val = 5 → not present → null (empty subtree)
        IO.println(preorder(searchBST(root, 5)));  // []

        // Searching the root's own value returns the whole tree
        IO.println(preorder(searchBST(root, 4)));  // [4, 2, 1, 3, 7]

        // A leaf match returns just that leaf
        IO.println(preorder(searchBST(root, 7)));  // [7]

        // Single-node tree, miss on both sides
        IO.println(preorder(searchBST(new TreeNode(1), 0)));  // []
    }

    /// Preorder serialization of a subtree, used only by the self-tests
    /// to show WHICH subtree the search returned (not just hit/miss).
    private List<Integer> preorder(TreeNode node) {
        List<Integer> values = new ArrayList<>();
        collectPreorder(node, values);
        return values;
    }

    private void collectPreorder(TreeNode node, List<Integer> values) {
        if (node == null) return;
        values.add(node.val);
        collectPreorder(node.left, values);
        collectPreorder(node.right, values);
    }
}

/// LeetCode's standard binary tree node (provided by the judge; included
/// here so the file is self-contained and runnable).
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {}

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}