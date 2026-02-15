package com.example.design_problems.service.recursion_backtracking;

import java.util.Arrays;

public class coin_change_subset {
    public void printCoinChangeSubset(int[] coin, int[] buffer, int target) {

        recurse(coin, buffer, target, 0, 0);

    }

    private void recurse(int[] coin, int[] buffer, int target, int arrayIndex, int bufferIndex) {
        System.out.println("START array index " + arrayIndex + " buffer index= " + bufferIndex + " target=" + target);
        if (target == 0) {
            //no point traversing further
            printBuffer(buffer, bufferIndex);
            return;
        }
        if (arrayIndex >= coin.length) {
            System.out.println("END array index " + arrayIndex + " buffer index= " + bufferIndex + " target=" + target);
            return;
        }
        if (bufferIndex >= buffer.length) {
            System.out.println("END array index " + arrayIndex + " buffer index= " + bufferIndex + " target=" + target);
            return;
        }
        for (int i = arrayIndex; i < coin.length; i++) {
            if (coin[i] <= target && target != 0) {
                buffer[bufferIndex] = coin[i];
                recurse(coin, buffer, target - coin[i], i, bufferIndex + 1);
//                recurse(coin, buffer, target - coin[i], i + 1, bufferIndex + 1); -- viw it took sometimw forme to get this!
                System.out.println("END array index " + i + " buffer index= " + bufferIndex + " target=" + target);

            }
        }
    }

    private void printBuffer(int[] buffer, int bufferIndex) {
        System.out.println("print buffer");
        for (int i = 0; i < bufferIndex; i++) {
            System.out.print(buffer[i] + ", ");
        }
        System.out.println();
    }


    public static void main(String[] args) {
        int[] coin = new int[]{1, 2, 5};
        int target = 5;
        coin_change_subset obj = new coin_change_subset();
        int[] buffer = new int[5];
        Arrays.fill(buffer, -1);
        obj.printCoinChangeSubset(coin, buffer, target);
    }
}
