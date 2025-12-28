package com.example.design_problems.service.twodmatrix;

import java.util.Random;

public class diagonalzigzag {
    public void traverse(int[][] mat) {
        Boolean diagUp= false;
        Boolean left= false;
        Boolean down= false;
        Boolean diagDown = false;

        /**
         * 
         */



    }

    public static void main(String[] args) {
        int N = 3;
        int[][] mat = new int[N][N];
        Random rand= new Random();
        for(int i=0;i<N; i++){
            for(int j=0; j<N; j++){
                mat[i][j]= rand.nextInt(1000);
            }
        }
        diagonalzigzag obj= new diagonalzigzag();
        obj.traverse(mat);
    }
}
