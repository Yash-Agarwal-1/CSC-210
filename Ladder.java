/*
 * Author: Yash Agarwal
 * CS 210, Spring 21
 * This file defines the helper class for MaxPQ and acts as the unit
 * for storing the ladders to reach the end page from the start page
 * It uses an arraylist to store the ladders and has a priority field
 * to decide which ladder is removed first
 */
import java.util.ArrayList;
import java.util.List;

public class Ladder {
	
	public List<String> ladder;
	public int priority;
	
	/*
	 * constructor to initialize a ladder and add
	 * the start page when initialized
	 */
	public Ladder(String start, int priority) {
		this.ladder = new ArrayList<String>();
		this.ladder.add(start);
		this.priority = priority;
	}
	
	/*
	 * constructor to initialize a ladder with
	 * a provided set of pages
	 */
	public Ladder(List<String> ladder, int priority) {
		this.ladder = ladder;
		this.priority = priority;
	}
	
	public String toString() {
		return ladder.toString() + " " + priority;
	}


}
