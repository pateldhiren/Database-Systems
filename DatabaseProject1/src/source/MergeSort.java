package source;

//This program implements the merge sort algorithm for
//arrays of integers.

import java.util.*;

public class MergeSort {
 /*public static void main(String[] args) {
     int[] keys = {14, 32, 67,67, 76, 23, 41, 58, 85};
     int[] values = {0,1,2,3,4,5,6,7,8};
     System.out.println("before: " + Arrays.toString(keys));
     System.out.println("before: " + Arrays.toString(values));
     mergeSort(keys,values);
     System.out.println("after:  " + Arrays.toString(keys));
     System.out.println("after: " + Arrays.toString(values));
 }*/

 // Places the elements of the given array into sorted order
 // using the merge sort algorithm.
 // post: array is in sorted (nondecreasing) order
 public void mergeSort(float[] keys,float[] values,String str) {
     if (keys.length > 1) {
         // split array into two halves
         float[] leftKeys = leftHalf(keys);
         float[] leftValues = leftHalf(values);
         float[] rightKeys = rightHalf(keys);
         float[] rightValues = rightHalf(values);
         
         // recursively sort the two halves
         mergeSort(leftKeys,leftValues,str);
         mergeSort(rightKeys,rightValues,str);
         
         // merge the sorted halves into a sorted whole
         if(str.equals("Decr"))
        	 mergeDecr(keys, leftKeys, rightKeys, values, leftValues,rightValues);
         else if(str.equals("Incr"))
        	 mergeIncr(keys, leftKeys, rightKeys, values, leftValues,rightValues);
     }
 }
 
 // Returns the first half of the given array.
 public float[] leftHalf(float[] array) {
     int size1 = array.length / 2;
     float[] left = new float[size1];
     for (int i = 0; i < size1; i++) {
         left[i] = array[i];
     }
     return left;
 }
 
 // Returns the second half of the given array.
 public float[] rightHalf(float[] array) {
     int size1 = array.length / 2;
     int size2 = array.length - size1;
     float[] right = new float[size2];
     for (int i = 0; i < size2; i++) {
         right[i] = array[i + size1];
     }
     return right;
 }
 
 // Merges the given left and right arrays into the given 
 // result array.  Second, working version.
 // pre : result is empty; left/right are sorted
 // post: result contains result of merging sorted lists;
	 public void mergeDecr(float[] keys, float[] leftKeys, float[] rightKeys, float[] values, float[] leftValues, float [] rightValues) {
		int i1 = 0;   // index into left array
		int i2 = 0;   // index into right array
		
		for (int i = 0; i < keys.length; i++) {
			if (i2 >= rightKeys.length || (i1 < leftKeys.length && leftKeys[i1] >= rightKeys[i2])) {
				keys[i] = leftKeys[i1];    // take from left
				values[i] = leftValues[i1];
				i1++;
			} else {
				keys[i] = rightKeys[i2];   // take from right
				values[i] = rightValues[i2];
				i2++;
			}
		}
	}
	 
	 public void mergeIncr(float[] keys, float[] leftKeys, float[] rightKeys, float[] values, float[] leftValues, float [] rightValues) {
			int i1 = 0;   // index into left array
			int i2 = 0;   // index into right array
			
			for (int i = 0; i < keys.length; i++) {
				if (i2 >= rightKeys.length || (i1 < leftKeys.length && leftKeys[i1] <= rightKeys[i2])) {
					keys[i] = leftKeys[i1];    // take from left
					values[i] = leftValues[i1];
					i1++;
				} else {
					keys[i] = rightKeys[i2];   // take from right
					values[i] = rightValues[i2];
					i2++;
				}
			}
		}
}