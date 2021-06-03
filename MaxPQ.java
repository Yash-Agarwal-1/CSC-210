import java.util.List;

/*
 * Author: Yash Agarwal
 * CS 210, Spring 21
 * Purpose: This file defines a binary max heap which is used by
 * WikiRacer to find the highest priority ladder and is backed by
 * Ladder.java which represents the element at each index.
 * The priority queue is implemented using an array and removal
 * takes place based on the highest priority value.
 * 
 */
public class MaxPQ {

	private Ladder[] arr;
	private int size;
	private static final int DEFAULT_ARRAY_SIZE = 10;
	
	public MaxPQ() {
		this.arr = new Ladder[DEFAULT_ARRAY_SIZE];
		this.size = 0;
	}
	
	 /*
     * this function adds a value to the end of the queue and then calls
     * the bubbleUp() function which compares to its parents and swaps it into
     * its respected place
     * param- ladder- the ladder to be added
     * priority- the priority of the patient which is being added
     * return- none
     */
	public void enqueue(List<String> ladder, int priority) {
        Ladder obj = new Ladder(ladder, priority);
        // when the number of patients goes above 9
        // increases the size of the array
        if (size >= arr.length - 1) {
            growArray();
        }
        // first element is added at index 1 to ease with index calculations
        if (size == 0) {
            arr[1] = obj;
            size++;
        } else {
            size++;
            arr[size] = obj;
            bubbleUp(size);
        }
    }
	
	/*
     * similar function but with a different signature for when
     * the Ladder object has been added
     * param- Ladder - an instance of the Ladder class
     * defined in Ladder.java
     * return- none
     */
	public void enqueue(Ladder newLadder) {
        enqueue(newLadder.ladder,newLadder.priority);
    }
	
	/*
     * helper function which creates a duplicate array of double size
     * and assigns it to arr
     * param- none
     * return- none
     */
	private void growArray() {
        Ladder[] copyArr = new Ladder[(size + 1) * 2];
        for (int i = 0; i <= this.size; i++) {
            copyArr[i] = arr[i];
        }
        this.arr = copyArr;
    }
	
	/*
     * this function checks the added ladder with its parent index
     * and if the priority is higher- swaps it with the parent
     * It continues this process until the patient is in its
     * required position
     * param- ind- the index at which the patient has been added in the
     * array
     * return- none
     */
	private void bubbleUp(int ind) {
        // copy to iterate for the same priority case
        int ind_copy = ind;
        // if the priority is lower swaps it by calling swap()
        while (ind > 1 && arr[(ind / 2)].priority < arr[ind].priority) {
            swap(ind / 2, ind);
            // changes ind to the parent node index
            ind = ind / 2;
        }

    }
	
	/*
     * this function swaps two ladders provided their index
     * param- i- index of the first element
     * ind- index of the second element
     * return - none
     */
	private void swap(int i, int ind) {
        Ladder temp = arr[i];
        arr[i] = arr[ind];
        arr[ind] = temp;
    }
	
	/*
     * this function puts the elements in the proper order after dequeue()
     * is called and the first element is removed. It performs this task
     * recursively
     * param- ind- index of the first element in array after the removal
     * takes place
     * return- none
     */
	private void bubbleDown(int ind) {
        int swapIndex = ind;
        int leftChild = ind*2;
        // compares with both the left and the right child to check
        // which index needs to be swapped with
        if (leftChild <= size
                && arr[leftChild].priority > arr[swapIndex].priority)
            swapIndex = leftChild;
        int rightChild = ind*2+1;
        if (rightChild <= size
                && arr[rightChild].priority > arr[swapIndex].priority)
            swapIndex = rightChild;
        // modification to the copy swapIndex means either of the ifs have
        // been executed and therefore swapping needs to take place
        if (ind != swapIndex) {
            swap(ind, swapIndex);
            // calls the function on swapIndex to make it the next parent index
            bubbleDown(swapIndex);
        }
    }
	
	/*
     * function to remove the ladder with the highest priority in the queue
     * that is the patient with the highest priority integer value
     * param- none
     * return List<String>- ladder with the topmost priority
     */
	public List<String> dequeue() {
        // empty queue
        if (size == 0) {
            throw new IllegalArgumentException();
        }
        List<String> ladder = arr[1].ladder;
        arr[1] = arr[size];
        size--;
        bubbleDown(1);
        return ladder;
    }
	
	/*
     * checks if the queue is empty
     * param- none
     * return- boolean- true if the queue is empty
     * and vice versa
     */
	public boolean isEmpty() {
        return size == 0;
    }
	
	/*
     * returns the size of the queue
     * param- none
     * return- int- size of the queue
     */
	public int size() {
        return size;
    }
	
}
