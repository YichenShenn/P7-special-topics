import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

/* 
 * 
 * UsingStacksSuitorsLab
 * 
 * This class is mostly a driver for playing with Strings as palindromes, 
 * both iteratively and recursively.  The UsingStacksSuitorsLab class itself is
 * a runnable object, so it can be passed to a thread in our Queue demo
 * 
 * 
 */

public class UsingStacksSuitorsLab implements Runnable {
	private static int threadCount = 0;
	private String name;

	public UsingStacksSuitorsLab() {
		name = "#" + threadCount++ + "Thread";
	}

	public static void main(String[] args) {
		String s1 = "food"; // not a palindrome
		String s2 = "racecar"; // a palindrome

		System.out.println("String1 is \"" + s1 + "\"");
		System.out.println("String2 is \"" + s2 + "\"");

		System.out.println(s1 + " reversed is: ");
		printReverse(s1);
		System.out.println(s2 + " reversed is: ");
		printReverse(s2);
		
		recPrintReverse(s1);
		System.out.println();
		recPrintReverse(s2);
		System.out.println();

		System.out.println(s1 + " is a palindrome: " + isPalindrome(s1));
		System.out.println(s2 + " is a palindrome: " + isPalindrome(s2));

		System.out.println(s1 + " is a palindrome(recursively): " + isPalindromeRec(s1));
		System.out.println(s2 + " is a palindrome(recursively): " + isPalindromeRec(s2));

		System.out.println("Did we build a Queue of Threads and start them? " + buildThreadQueue());

		int n = 6;
		System.out.println("(Q) For " + n + " suitors, stand in place:" + findPlaceToStandQ(n));

		n = 10;
		System.out.println("(Q) For " + n + " suitors, stand in place:" + findPlaceToStandQ(n));
		
		n = 6;
		System.out.println("(S) For " + n + " suitors, stand in place:" + findPlaceToStandS(n));

		n = 10;
		System.out.println("(S) For " + n + " suitors, stand in place:" + findPlaceToStandS(n));
		
	}

	public static void printReverse(String target) {
		// todo: use a stack
		LLStack foo = new LLStack();
		for(int i = 0; i < target.length(); i++) {
			foo.addToStart(target.charAt(i));
		}
		while(!foo.isEmpty()) {
			System.out.println(foo.deleteHead());
		}
	}

	public static void recPrintReverse(String target) {
		// todo
		if(target.length() > 0) {
			System.out.println(target.charAt(target.length() - 1));
			recPrintReverse(target.substring(0, target.length() - 1));
		}
	}

	public static boolean isPalindrome(String input) {
		// todo: use a stack
		LLStack foo = new LLStack();
		for(int i = 0; i < input.length(); i++) {
			foo.addToStart(input.charAt(i));
		}
		String temp = "";
		while(!foo.isEmpty()) {
			temp += foo.deleteHead();
		}
		return input.equals(temp);
	}

	public static boolean isPalindromeRec(String sentence) {
		// todo
		if(sentence.length() > 1) {
			if(sentence.charAt(0) != sentence.charAt(sentence.length() - 1)) return false;
			return isPalindromeRec(sentence.substring(1, sentence.length() - 1));
		}
		return true;
	}

	public static int findPlaceToStandQ(int numSuitors) { //with queue
		// todo
		LLQueue q = new LLQueue();
		for(int i = 1; i <= numSuitors; i++) {
			q.offer(i);
		}
		while(q.size() > 1) {
			q.offer(q.poll());
			q.offer(q.poll());
			q.poll();
		}
		return (int) q.poll();
	}
	
	public static int findPlaceToStandS(int numSuitors) { //with stacks
		LLStack s1 = new LLStack();
		LLStack s2 = new LLStack();
		for(int i = numSuitors; i > 0; i--) {
			s1.addToStart(i);
		}
		boolean fwd = true;
		while(s1.size() + s2.size() > 1) {
			for(int i = 0; i < 2; i++) {
				if(fwd) s2.addToStart(s1.deleteHead());
				else s1.addToStart(s2.deleteHead());
				if(s1.isEmpty()) fwd = false;
				else if(s2.isEmpty()) fwd = true;
			}
			if(fwd && !s1.isEmpty()) s1.deleteHead();
			else s2.deleteHead();
			if(s1.isEmpty()) fwd = false;
			else if(s2.isEmpty()) fwd = true;
		}
		if(!s1.isEmpty()) return (int) s1.deleteHead();
		return (int) s2.deleteHead();
	}
	
	public static int findPlaceToStandCh(int numSuitors) { //challenge
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for(int i = 1; i <= numSuitors; i++) {
			arr.add(i);
		}
		int in = -1;
		boolean fwd = true;
		while(arr.size() > 1) {
			if(fwd) {
				while(in + 3 < arr.size()) {
					in += 3;
					arr.remove(in);
					in--;
				}
				if(Math.random() < .5) fwd = false;
				if(fwd) in -= arr.size();
				else in += (2 * (arr.size() - in)) - 1;
				// in = (2 * arr.size()) - in - 1; this also works
			} else {
				while(in - 3 > -1) {
					in -= 3;
					arr.remove(in);
				}
				fwd = true;
				in = -in - 1;
			}
		}
		return arr.get(0);
	}

	public static boolean buildThreadQueue() { // returns true upon success
		Queue<Thread> q = new LinkedList<Thread>();

		// when our program starts up, it might create multiple threads to use
		q.add(new Thread(new UsingStacksSuitorsLab()));
		q.add(new Thread(new UsingStacksSuitorsLab()));
		q.add(new Thread(new UsingStacksSuitorsLab()));

		System.out.println("Initial Thread order:");
		System.out.println(q.toString());

		// We need to iterate over our pool of threads and call start() on each one
		// Make a loop that dequeues a thread, calls start on it, and //enqueues it
		// again
		// to do:
		// current = get a thread
		// current.start();
		// put the thread back
		for(Thread t : q) {
			t.start();
		}

		System.out.println("Thread order after start()ing:");
		System.out.println(q.toString());

		return true; // on successful start
	}

	/*
	 * No need to edit anything below here, unless you'd like to change the
	 * behaviour of each thread in the thread pool above
	 */
	
	@Override
	public void run() {
		System.out.println(name + " ran");
		/*
		for (int i = 0; i < 1000; i++) {
			System.out.println(name + ": " + i + "th iteration");
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// do nothing here
			}
		}
		*/
	}
}
