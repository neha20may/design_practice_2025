package com.example.design_problems.service.recursion_backtracking.dfs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * """
 * 2139. Minimum Moves to Reach Target Score
 * <p>
 * You are playing a game with integers. You start with the integer 1 and you want to reach the integer target.
 * <p>
 * In one move, you can either:
 * <p>
 * Increment the current integer by one (i.e., x = x + 1).
 * Double the current integer (i.e., x = 2 * x).
 * You can use the increment operation any number of times, however, you can only use the double operation at most maxDoubles times.
 * <p>
 * Given the two integers target and maxDoubles, return the minimum number of moves needed to reach target starting with 1."""
 **/


public class reach_target {
    public int minMoves(int target, int maxDoubles) {
        List<Integer> ansCand = new ArrayList<>();
//        solve(target, maxDoubles, ansCand, 0,1);
//        int res= Collections.min(ansCand);
        int res = solve2(target, maxDoubles);
        return res;
    }

    private int solve2(int target, int maxDoubles) {
        int steps = 0;
        while (target > 1) {
            // System.out.println("target= "+target);
            if (target % 2 == 0 && maxDoubles > 0) {
                target = target / 2;
                maxDoubles = maxDoubles - 1;
                steps = steps + 1;
            } else {
                if (maxDoubles == 0) {
                    steps = steps+ target - 1;
                    target = 0;
                } else {
                    target = target - 1;
                    steps = steps + 1;
                }
            }
        }
        return steps;
    }


    private void solve(int target, int maxDoubles, List<Integer> steps, int step, int curStep) {
//        System.out.println("target "+target+ " count of steps "+step +" cur Step ="+curStep+ " doubles "+maxDoubles);
        if (target - curStep < 0) {
            return;
        }
        if (target - curStep == 0) {
            steps.add(step);
            return;
        }

        /**
         * curStep = curStep +1 ---check if target
         * curStep= curStep * 2 --check if target
         *
         * there are only two candidates at any step
         */
//        curStep = curStep+1;
        if (maxDoubles > 0) {
            solve(target, maxDoubles - 1, steps, step + 1, curStep * 2);
        }
        solve(target, maxDoubles, steps, step + 1, curStep + 1);


    }

    public static void main(String[] args) {

        reach_target obj = new reach_target();
        int target = 19;
        int maxdouble = 2;
        int res = obj.minMoves(target, maxdouble);
        System.out.println(res);
        Collections.reverse(new ArrayList<>());

    }
}
