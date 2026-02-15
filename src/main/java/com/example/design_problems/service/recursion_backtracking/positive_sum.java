package com.example.design_problems.service.recursion_backtracking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class positive_sum {
    // public List<Long> maximumEvenSplit(long finalSum) {
    //     List<Long> candidates =  new ArrayList<>();
    //     for(long i=2; i< finalSum; i= i+2){
    //         candidates.add(i);
    //     }
    //     //max you want to go so start with min candidates?
    //     long target =0;
    //     List<Long> ans= new ArrayList();
    //     for(Long c: candidates){
    //         ans.add(c);
    //         target= target+c;
    //         if(target < finalSum){
    //             continue;
    //         }else{
    //             break;
    //         }
    //     }
    //     System.out.println("candiates:"+candidates);
    //     System.out.println("target:"+target);
    //     System.out.println("ans:"+ans);
    //     if(target == finalSum){
    //         return ans;
    //     }else{
    //         return new ArrayList<>();
    //     }
    // }
    public List<Long> maximumEvenSplit2(long finalSum) {
        HashSet<Long> cand = new HashSet<>();
        List<Long> ans = new ArrayList();
        if(finalSum %2!=0){
            return ans;
        }
        recurse2(finalSum, cand, ans);
        return ans;
    }

    public void recurse2(long target, HashSet<Long> cand, List<Long> ans) {
        // System.out.println("taget ="+target +" cand so far "+ cand+ " ans so far "+ans);
        Boolean isFound = false;
        for (long i = 2; i <= target; i = i + 2) {
            if (!cand.contains(i)) {
                // System.out.println("i= "+i);
                cand.add(i);
                ans.add(i);
//                recurse2(target - i, cand, ans);
                target = target-i;
                isFound = true;
            } else {
                continue;
            }
        }
        if(isFound && target!=0){
            long last = ans.get(ans.size()-1);
            last = last + target;
            ans.remove(ans.size()-1);
            ans.add(last);
        }
        //nothing was the candidate then this taget is the element that should be added if target is even;
        if (!isFound && target % 2 == 0) {
            //add to the last ement in the ans
            long last = ans.get(ans.size()-1);
            last = last + target;
            ans.remove(ans.size()-1);
            ans.add(last);
        }
    }
    public List<Long> maximumEvenSplit(long finalSum) {
        List<Long> candidates = new ArrayList<>();
        for (long i = 2; i <= finalSum; i = i + 2) {
            candidates.add(i);
        }
        List<Long> buffer = new ArrayList();
        List<List<Long>> ansCandidates = new ArrayList();
        System.out.println(candidates);
        recurse(candidates, 0, 0, buffer, finalSum, ansCandidates);
        if (ansCandidates.size() == 0) {
            return new ArrayList();
        } else {
            return ansCandidates.get(0);
        }
    }

    public void recurse(List<Long> candidates, int ai, int bi, List<Long> buffer, long target,
                        List<List<Long>> ansCandidates) {
// System.out.println("ai " + ai + " bi " + bi );
        if (ai > candidates.size()) {
            return;
        }
         System.out.println("ai " + ai + " bi " + bi + " target " + target);
        // printBuffer(buffer, bi);
        if (target < 0) {
            return;
        }
        if (target == 0) {
            //this buffer is the ans
            // System.out.print("buffer: ");
            // printBuffer(buffer, bi);
            addBuffer(ansCandidates, buffer, bi);
            return;
        }
        //subset problem or coin change problem
        for (int i = ai; i < candidates.size(); i++) {
            buffer.add(bi, candidates.get(i));
            recurse(candidates, i + 1, bi + 1, buffer, target - candidates.get(i), ansCandidates);
        }

    }

    private void addBuffer(List<List<Long>> candidates, List<Long> buffer, int bi) {
        List<Long> ans = new ArrayList();
        for (int i = 0; i < bi; i++) {
            ans.add(buffer.get(i));
        }
        candidates.add(ans);
    }

    private void printBuffer(List<Long> buffer, int bi) {
        System.out.print("[");
        for (int i = 0; i < bi; i++) {
            System.out.print(buffer.get(i) + ", ");
        }
        System.out.println("]");
    }

    public static void main(String[] args) {
        positive_sum obj = new positive_sum();
        List<Long> ans = obj.maximumEvenSplit2(28);
        System.out.println(ans);
    }

}
