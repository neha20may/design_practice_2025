package com.example.design_problems.service.recursion_backtracking;

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 * int val;
 * TreeNode left;
 * TreeNode right;
 * TreeNode() {}
 * TreeNode(int val) { this.val = val; }
 * TreeNode(int val, TreeNode left, TreeNode right) {
 * this.val = val;
 * this.left = left;
 * this.right = right;
 * }
 * }
 */
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {

    }

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

class Solution {
    public void print(String s) {
        System.out.println(s);
    }

    public int rec(TreeNode node, boolean isparentRobbed) {
        if (node == null) {
            return 0;
        }
        print("start node = " + node.val + " allowed to rob = " + isparentRobbed);
        if (isparentRobbed) {
            //can't rob myself only can go ahead.
            int left = rec(node.left, false);
            int right = rec(node.right, false);
            return left + right;
        } else {
            //parent not robbed- i can rob myself or not
            //case 1: robbing this node
            int left = rec(node.left, true);
            int right = rec(node.right, true);
            int amount1 = node.val + left + right;


            //case 2: not robbing ,self
            left = rec(node.left, false);
            right = rec(node.right, false);
            int amount2 = left + right;
            return Math.max(amount1, amount2);
        }

    }

    public int rob(TreeNode root) {
        int maxamount = 0;
        int includeMaxAmount = rec(root, false);
        return includeMaxAmount;
    }
}

public class house_robber_3 {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(4, new TreeNode(1,
                new TreeNode(2,
                        new TreeNode(3,
                                null,
                                null),
                        null),
                null),
                null);

        Solution s = new Solution();
        System.out.println("ans=" + s.rob(root));

    }
}
