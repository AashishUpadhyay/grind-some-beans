package com.practice.problems;

public class SwapBits {

    public int swapBits(int num) {
        int evenBits = num & 0xAAAAAAAA;
        int oddBits = num & 0x55555555;

        return (evenBits >> 1) | (oddBits << 1);
    }
}
