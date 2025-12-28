package com.example.design_problems.service.binarysearch;

import java.util.Arrays;

class firstOcc {
    public int[] findFirstOcc(int[] a, int target) {
        int start = 0;
        int end = a.length - 1;
        int left_result = -1;
        int right_result = -1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (a[mid] < target) {
                start = mid + 1;
            } else if (a[mid] > target) {
                end = mid - 1;
            } else {
                left_result = mid; //record and move on ; since this is more strict requirement that ane lement has to be present we are in == part; in finding closure element problem we record mid blindly.
                end = mid - 1;
            }
        }
        start=0;
        end= a.length-1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (a[mid] < target) {
                start = mid + 1;
            } else if (a[mid] > target) {
                end = mid - 1;
            } else {
                right_result = mid;
                start = mid + 1;
            }
        }
        int[] result = {left_result, right_result};
        return result;
    }
}

public class BinarySearch {
    public static void main(String[] args) {
        int[] a = {0, 1, 2};
        firstOcc obj = new firstOcc();
        Arrays.stream(obj.findFirstOcc(a, 2))
                .forEach(x->{
                    System.out.println(x);
                });

    }
}
