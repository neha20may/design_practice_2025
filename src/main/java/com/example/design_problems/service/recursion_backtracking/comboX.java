package com.example.design_problems.service.recursion_backtracking;

import java.util.Arrays;

public class comboX {
    public static void combo(int[] num, int index, int buffer_index, int[] buffer, int bufferSize, int N) {
        String spaceAccToDepth = "-";
        System.out.println(spaceAccToDepth + " index= " + index + " buffer index=" + buffer_index + " start");
        if (buffer_index == bufferSize) {
            System.out.print(spaceAccToDepth + " combo: ");
            Arrays.stream(buffer).forEach(x -> System.out.print(" " + x));
            System.out.println(spaceAccToDepth + " index = " + index + " buffer index=" + buffer_index + " end");
            return;
        }
        if (index >= N) {
            System.out.println(spaceAccToDepth + " end of nums reached");
            System.out.println(spaceAccToDepth + " index= " + index + " buffer index=" + buffer_index + " end");
            return;
            //btw the loop below <N so even if we dont put this condition we are safe; function returns safely
        }

        for (int i = index; i < N; i++) {
            buffer[buffer_index] = num[i];
            combo(num, i + 1, buffer_index + 1, buffer, bufferSize, N);
        }
        System.out.println(spaceAccToDepth + " index= " + index + " buffer index=" + buffer_index + " end");

    }
    public static void combo2(int[] num, int index, int buffer_index, int[] buffer, int bufferSize, int N){
        String spaceAccToDepth = "-";
        System.out.println(spaceAccToDepth + " index= " + index + " buffer index=" + buffer_index + " start");
        if (buffer_index == bufferSize) {
            System.out.print(spaceAccToDepth + " combo: ");
            Arrays.stream(buffer).forEach(x -> System.out.print(" " + x));
            System.out.println();
            System.out.println(spaceAccToDepth + " index = " + index + " buffer index=" + buffer_index + " end");
            return;
        }
        if (index >= N) {
            System.out.println(spaceAccToDepth + " end of nums reached");
            System.out.println(spaceAccToDepth + " index= " + index + " buffer index=" + buffer_index + " end");
            return;
            //btw the loop below <N so even if we dont put this condition we are safe; function returns safely
        }
        combo2(num, index+1, buffer_index, buffer, bufferSize, N);
        buffer[buffer_index]= num[index];
        combo2(num, index+1, buffer_index+1, buffer, bufferSize, N);
    }

    public static void main(String[] args) {
        int[] num = {1, 2, 3, 4};
        int buffersize = 2;
        int N = num.length;
        int[] buffer = new int[buffersize];
        combo2(num, 0, 0, buffer, buffersize, N);
    }
}
