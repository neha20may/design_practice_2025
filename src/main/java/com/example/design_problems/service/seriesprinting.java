package com.example.design_problems.service;

import java.util.concurrent.Semaphore;

class PrintNumberSeries {·

    private int n;
    private Semaphore zeroSem, oddSem, evenSem;

    public PrintNumberSeries(int n) {
    }

    public void PrintZero() {
        for(int i=0;i<n;i++) {
            System.out.println("0 ");
        }
    }

    public void PrintOdd() {
        for(int i=0;i<n;i++){

        }
    }

    public void PrintEven() {
    }

}

public class seriesprinting {
}
