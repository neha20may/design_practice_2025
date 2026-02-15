package com.example.design_problems.service.recursion_backtracking;

import java.util.Arrays;

public class permutation {
    void permutation_print(int[] a, int k, int[] buffer) {
        Boolean []isInBuffer= new Boolean[a.length];
        Arrays.fill(isInBuffer, false);
        recurse(a, k, buffer, 0, 0, isInBuffer);

    }

    private void recurse(int[] a, int k, int[] buffer, int bufferIndex, int arrayIndex, Boolean [] isInBuffer) {
        if (bufferIndex == k) {
            System.out.println("Buffer print");
            for (int i = 0; i < bufferIndex; i++) {
                System.out.println(buffer[i]);
            }
            System.out.println("Buffer print end");
        }
        if (arrayIndex >= a.length) {
            return;
        }
        if (bufferIndex >= k) {
            return;
        }
//        for(int i= arrayIndex; i < a.length; i++){
        for (int i = 0; i < a.length; i++) {
            if(!isInBuffer[i]) {
                buffer[bufferIndex] = a[i];
                isInBuffer[i]= true;
                recurse(a, k, buffer, bufferIndex + 1, arrayIndex + 1, isInBuffer);
                isInBuffer[i]=false;
            }else{
                //skip
            }

        }
    }

    public static void main(String[] args) {
        int[] a = new int[]{1, 2, 3};
        int k = 2;
        int[] buffer = new int[k];
        permutation obj = new permutation();
        obj.permutation_print(a, k, buffer);
    }

}
