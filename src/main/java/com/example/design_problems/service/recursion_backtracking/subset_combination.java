package com.example.design_problems.service.recursion_backtracking;

import java.util.Arrays;

public class subset_combination {
    public void subset(int [] a, int k, int [] buffer, Boolean isAllTillK){
        //buffer is of k size
        Arrays.fill(buffer, -1);
        recursionBacktrack(a, k, buffer, 0, 0, isAllTillK);
//        recursion(a, k, buffer, 0, 0, isAllTillK);
    }

    private void recursion(int[] a, int k, int[] buffer, int bufferIndex, int arrayIndex, Boolean isAllTillK) {
        System.out.println("Call Start buffer index="+bufferIndex+ "arrayIndex="+arrayIndex);
        if(isAllTillK){
            printBufferTill(buffer, bufferIndex);
        }else{
            if(bufferIndex ==k){
                printBufferTill(buffer, bufferIndex);
            }
        }
        if(arrayIndex >= a.length){
            System.out.println("Call End buffer index="+bufferIndex+ "arrayIndex="+arrayIndex);
            return;
        }
        if(bufferIndex>= k){
            System.out.println("Call End buffer index="+bufferIndex+ "arrayIndex="+arrayIndex);
            return;
        }

        for(int i= arrayIndex; i< a.length; i++){
            buffer[bufferIndex]= a[i];
            recursion(a, k, buffer, bufferIndex+1, i+1, isAllTillK);
        }
        System.out.println("Call End buffer index="+bufferIndex+ "arrayIndex="+arrayIndex);
    }



    private void recursionBacktrack(int[] a, int k, int[] buffer, int bufferIndex, int arrayIndex, Boolean isAllTillK) {
        //bcz this is what is used in permutation
        System.out.println("Call Start buffer index= "+bufferIndex+ " arrayIndex= "+arrayIndex);
        if(isAllTillK){
            printBuffer(buffer);
        }else{
            if(bufferIndex ==k){
                printBuffer(buffer);
            }
        }

        if(arrayIndex >= a.length){
            System.out.println("Call End buffer index="+bufferIndex+ "arrayIndex="+arrayIndex);
            return;
        }
        if(bufferIndex>= k){
            System.out.println("Call End buffer index="+bufferIndex+ "arrayIndex="+arrayIndex);
            return;
        }

        for(int i= arrayIndex; i< a.length; i++){
            buffer[bufferIndex]= a[i];
            System.out.println(" after putting array index ="+i+ " element = "+
                    a[i]+" in buffer at buffer index "+bufferIndex
                    +" calling buffer index "+(bufferIndex+1) +" and array index = "+(i+1));
            recursionBacktrack(a, k, buffer, bufferIndex+1, i+1, isAllTillK);
            buffer[bufferIndex]=-1; //mimick removing from path variable or this -1 acts as the bufferindex now
        }
        System.out.println("Call End buffer index= "+bufferIndex+ " array Index="+arrayIndex);
    }

    private void printBuffer(int[] buffer) {
        System.out.print("buffer received- [ ");
        for(int x: buffer){
            if(x==-1){
                continue;
            }
            System.out.print(x+ ", ");
        }
        System.out.println(" ]");
    }

    private void printBufferTill(int[] buffer, int bufferIndex) {
        System.out.print("[ ");
        for(int i=0; i< bufferIndex; i++){
            System.out.print(buffer[i]+", ");
        }
        System.out.println(" ]");
    }

    public static void main(String[] args) {
        int [] a= new int[]{5,6, 7};
        int k=2;
        int [] buffer= new int[k];
        subset_combination obj= new subset_combination();
        obj.subset(a, k, buffer, true);

    }
}
