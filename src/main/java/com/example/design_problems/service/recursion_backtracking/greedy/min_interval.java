package com.example.design_problems.service.recursion_backtracking.greedy;


import java.util.*;

class interval {
    public int start;
    public int end;
    public int span;

    public interval(int a, int b) {
        start = a;
        end = b;
        span = b - a;
    }

    @Override
    public String toString() {
        return "[  " + start + " , " + end + " ]";
    }
}

public class min_interval {

    public int videoStitchingV3(int[][] clips, int time) {
        Arrays.sort(clips, (a, b) -> {
            if (a[0] == b[0]) {
                return b[1] - a[1];
            }
            return a[0] - b[0];
        });
        int boundary = 0, farthest = 0, ans = 0;

        for (int i = 0; i < clips.length; i++) {
            System.out.println("i=" + i + " { clips[0] = " + clips[i][0] + " ,clips[1] =  " + clips[i][1] + " }" + " boundary = " + boundary);
            ;
            if (clips[i][0] > boundary) {
                System.out.println("!! this clip start  > boundary !!");
                ans++;
                boundary = farthest;
                System.out.println("setting boundary to fartheest " + boundary);
            }

            farthest = Math.max(farthest, clips[i][1]);
            System.out.println("farthest change ? = " + farthest);
        }
        return ans;
    }


    private void solveRecursive(List<interval> intervals, int time, int startIndex, List<
            interval> ans, List<interval> finalAns) {

        if (startIndex >= intervals.size()) {
            return;
        }
        interval mergedInterval = merged(ans);
        if (mergedInterval.end == time && mergedInterval.start == 0) {
            //this is one candidate
            System.out.println("found answer candidate= " + ans);
            if (finalAns.size() < ans.size()) {
                //will this work ?  simply reassignment? nope you are removing elememtn from for loop
                finalAns.clear();
                for (interval x : ans) {
                    finalAns.add(x);
                }
            }
            return;
        }
        /**
         * how to find candidates here?
         * say start with all intervals whose start time - combination or permiyation?
         * combination so always move ahesd +1 of start index
         * basically check all the interals
         */
        List<interval> candidates = new ArrayList<>();
        for (int i = startIndex; i < intervals.size(); i++) {
            //discontiued candidates are also coming
            //only those candidates whose start time <= mrgedInterval end time

            if (intervals.get(i).start <= mergedInterval.end) {
            }

            if (mergedInterval.end == Integer.MIN_VALUE) {
                ans.add(intervals.get(i));
                solveRecursive(intervals, time, startIndex + 1, ans, finalAns);
                ans.remove(intervals.get(i));
            }
        }
        for (interval c : candidates) {
            ans.add(c);
            solveRecursive(intervals, time, startIndex + 1, ans, finalAns);
        }

    }

    private interval merged(List<interval> ans) {
        int start = Integer.MAX_VALUE;
        int end = Integer.MIN_VALUE;
        for (interval c : ans) {
            if (c.start < start) {
                start = c.start;
            }
            if (c.end > end) {
                end = c.end;
            }
        }
        interval m = new interval(start, end);
        return m;
    }

    public int videoStitching(int[][] clips, int time) {
        List<interval> l = new ArrayList();
        for (int[] c : clips) {
            l.add(new interval(c[0], c[1]));
        }
        Collections.sort(l, (a, b) -> {
            return b.span - a.span;
        });
        int ans = 0;
        interval current = new interval(l.get(0).start, l.get(0).end);
        for (interval i : l) {
            if (isOverLap(current, i)) {
                if (contained(current, i)) {
                    continue;
                }
                ans = ans + 1;
                merge(current, i);
            } else {
                //are they at boundary?
                if (isBoundary(current, i)) {
                    ans = ans + 1;
                    merge(current, i);
                } else {
                    break;
                }
            }
        }
        if (allDone(current, time)) {
            return ans + 1;
        } else {
            return 0;
        }

    }

    private boolean contained(interval current, interval i) {
        if (current.end >= i.end && current.start <= i.start) {
            return true;
        }
        return false;
    }

    private Boolean allDone(interval current, int time) {
        if (current.start == 0 && current.end == time) {
            return true;
        }
        return false;
    }

    private boolean isBoundary(interval current, interval i) {
        if (current.end == i.start || current.start == i.end) {
            return true;
        }
        return false;
    }

    private void merge(interval current, interval i) {
        System.out.println("merging current start :" + current.start + " end :" + current.end + " with i start " + i.start + " end: " + i.end);
        int min = Math.min(current.start, i.start);
        int max = Math.max(current.end, i.end);
        current.start = min;
        current.end = max;
        //if merge changes current then ans+1;
        current.span = current.end - current.start;
    }

    private Boolean isOverLap(interval current, interval i) {
        if (current.end > i.start && current.start < i.end) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
//[[0,2],[4,6],[8,10],[1,9],[1,5],[5,9]]
        int[][] clips = {
                {0, 1},
                {6, 8},
                {0, 2},
                {5, 6},
                {0, 4},
                {0, 3},
                {6, 7},
                {1, 3},
                {4, 7},
                {1, 4},
                {2, 5},
                {2, 6},
                {3, 4},
                {4, 5},
                {5, 7},
                {6, 9}
//                {0, 2},
//                {4, 6},
//                {8, 10},
//                {1, 9},
//                {1, 5},
//                {5, 9}
        };
        min_interval obj = new min_interval();
        int ans = obj.videoStitchingV3(clips, 9);
        System.out.println("ans=" + ans);
    }
}

