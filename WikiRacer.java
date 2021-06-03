/*
 * Author: Yash Agarwal
 * CS 210, Spring 21
 * Purpose: This file defines a class which defines the path to travel from 
 * one Wikipedia page from one to another and prints that path
 * It takes in the start and the end page as arguments from the command
 * line and returns the path of web pages from the intial to the final page
 * 
 * If the input is Emu Stanford_University, then the output will be 
 * [Emu, Food_and_Drug_Administration, Duke_University, Stanford_University]
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WikiRacer {

	/*
	 * Do not edit this main function
	 */
	public static void main(String[] args) {
		List<String> ladder = findWikiLadder(args[0], args[1]);
		System.out.println(ladder);
	}

	/*
	 * This function returns the ladder of web pages from the start
	 * page to the end page. It uses a priority queue backed by a
	 * binary max heap to find the path. It determines the priority depending 
	 * on the number of common pages between the current page and the end page
	 * param- start- the string storing the initial page
	 * end- string storing the final page
	 * return- List<String>- the ladder from the start to end page
	 */
	private static List<String> findWikiLadder(String start, String end) {
		// set to store all the links that have already been visited to ensure
		// that same pages aren't visited multiple times
		Set<String> visitedLinks = new HashSet<String>();
		MaxPQ priorityQueue = new MaxPQ();
		List<String> temp = new ArrayList<String>();
		temp.add(start);
		// ladder containing only the start page
		Ladder init = new Ladder(temp, 1);
		priorityQueue.enqueue(init);
		while(!priorityQueue.isEmpty()) {
			// the highest priority ladder
			List<String> front = priorityQueue.dequeue();
			// links on current page
			Set<String> links = WikiScraper.findWikiLinks(front.get(front.size()-1));
			// end page found
			if(links.contains(end)) {
				front.add(end);
				return front;
			}
			// Parallelization
			links.parallelStream().forEach(link -> {
				WikiScraper.findWikiLinks(link);
				});
			
			for(String link: links) {
				if(!visitedLinks.contains(link)) {
					// creates a new ladder with the existing partial ladder
					// and adds all the links on the current page with the
					// priority based on the number of common pages
					Ladder obj = new Ladder(ladderCopy(front), 
							findPriority(WikiScraper.findWikiLinks(link), WikiScraper.findWikiLinks(end)));
					obj.ladder.add(link);
					priorityQueue.enqueue(obj.ladder, obj.priority);
				}
			}
		}
		return null;
	}
	
	/*
	 * Function to find the priority of the ladder based on the
	 * number of common pages between the current and final page
	 * param- list1- the links on the current page
	 * list2- the links on the final page
	 * return- int - number of common pages
	 */
	private static int findPriority(Set<String> list1, Set<String> list2) {
		int count = 0;
		for(String n: list1) {
			if(list2.contains(n)) {
				count++;
			}
		}
		return count;
	}
	
	/*
	 * Helper function to create a copy of the current partial ladder
	 * param- original- the original copy of the partial ladder
	 * return- List<String>- The copy of the original string
	 */
	private static List<String> ladderCopy(List<String> original){
		List<String> copy = new ArrayList<String>();
		for(String s: original) {
			copy.add(s);
		}
		return copy;
	}

}
