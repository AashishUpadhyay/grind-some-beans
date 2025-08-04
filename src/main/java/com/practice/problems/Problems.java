/*

Inputs:
Sweets = [3,6,7,11], hours = 8
Output: 4 sweets per hour

1 + 2 + 2 + 3

Condition: Cannot switch
Goal is to finish all sweets in the time allocated. 
Return the speed (sweets per hour.)


Approach is baiary search

Edge cases: When rooms are more than 1 but hour is only one. Room count is more than than the no of hours.


*/

package com.practice.problems;

public class Problems {
    public Problems() {

    }

    public static void main(String[] args) {
        Problems problems = new Problems();
        int[] sweets = new int[] { 3, 6, 7, 11 };
        System.out.println("Output = " + problems.minInteger(sweets, 8));
    }

    public int minInteger(int[] sweets, int hours) {
        // Binary Search
        // Max - 11
        // Min - 3
        // Calculate time taken

        int minSpeed = 1000000000;
        int maxSpeed = 0;

        for (Integer sw : sweets) {
            maxSpeed = Math.max(maxSpeed, sw);
        }

        minSpeed = 1;

        int midSpeed = -1;

        // max = 11 min = 3 mid = 7. canFinish = True
        // max = 6 min = 3 mid = 4. canFinish = True
        // max = 3 min = 3. midSpeed = 3 canFinish = True

        int retVal = maxSpeed + 1;

        while (minSpeed <= maxSpeed) {
            midSpeed = minSpeed + (maxSpeed - minSpeed) / 2;
            if (canFinish(sweets, midSpeed, hours)) {
                maxSpeed = midSpeed - 1;
                retVal = Math.min(retVal, midSpeed);
            } else {
                minSpeed = midSpeed + 1;
            }
        }
        return retVal;
    }

    private boolean canFinish(int[] sweets, int speed, int hours) {
        int totalTimetaken = 0;
        for (int sweet : sweets) {
            if (sweet > speed) {
                totalTimetaken += (sweet / speed) + ((sweet % speed) > 0 ? 1 : 0);
            } else {
                totalTimetaken += 1;
            }
        }
        return totalTimetaken <= hours;
    }
}