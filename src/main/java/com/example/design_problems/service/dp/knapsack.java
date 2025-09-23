package com.example.design_problems.service.dp;

import java.util.Arrays;

public class knapsack {
    public int knapsackTopDown(int[] w, int[] v, int i, int c, int n, int[][] memo) {
        if (i >= n) {
            return 0;
        }
        if (c < 0) {
            return 0;
        }
        if (memo[i][c] != -1) {
            return memo[i][c];
        }
        int includeVal = 0;
        int excludeVal = 0;
        System.out.println("i =" + i + " v[i]=" + v[i] + " cuurent c=" + c + " w[i]=" + w[i]);

        if (w[i] <=
                c) {
            includeVal = v[i] + knapsackTopDown(w, v, i + 1, c - w[i], n, memo);
        }
        excludeVal = knapsackTopDown(w, v, i + 1, c, n, memo);
        System.out.println("i =" + i + " v[i]=" + v[i] + " cuurent c=" + c + " w[i]=" + w[i] + " include val=" + includeVal + " exclude val =" + excludeVal);
        int finalVal = Math.max(includeVal, excludeVal);
        memo[i][c] = finalVal;
        return finalVal;

    }

    public static void main(String[] args) {
        int[] w = {1, 2, 3, 5};
        int[] v = {1, 5, 4, 8};
        int c = 6;
        //pick items so that sum of weights < c and values is maximum
        //top down - pick and not pick ; memo[i][current cap]
        int n = 4;
        int[][] memo = new int[4][c + 1];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < c + 1; j++) {
                memo[i][j] = -1;
            }
        }
        knapsack obj = new knapsack();
        int ans = obj.knapsackTopDown(w, v, 0, c, n, memo);
        System.out.println(ans);
        System.out.println("memo");
        for(int i=0;i< 4; i++) {
            int[] r = memo[i];
            System.out.println();
            Arrays.stream(r).forEach(x -> System.out.print(x + "   "));
            System.out.println();
        }
    }
}
