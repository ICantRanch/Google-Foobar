package FooBar;

import java.util.ArrayList;
import java.util.Arrays;

public class Ialreadydidthat {
	
	static ArrayList<String> previous = new ArrayList<String>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.printf("Length of Cycle:%d\n\n",solution("210022", 3));
		for (String s : previous) {
			System.out.println(s);
		}
		
	}

	public static int solution(String n, int base) {
		
		int repeat = checkPrev(n);
		//Check if current String has already appeared
		
		if(repeat == -1) {
			previous.add(n);
			//Sort String to split into ascending and descending
			char[] c = n.toCharArray();
			Arrays.sort(c);
			String temp = new String(c);
			StringBuilder sb = new StringBuilder(temp);
			sb.reverse();
			int descend = Integer.parseInt(sb.toString(),base);
			int ascend = Integer.parseInt(temp,base);

			//Subtract and if less than original length, add 0's to front
			String result = Integer.toString(descend-ascend, base);
			while(result.length() < n.length()) {
				result = "0" + result;
			}
			return solution(result,base);
		}

		return previous.size() - repeat;        
    }
	
	public static int checkPrev(String n) {
		
		//Check to see if the String has occurred in the previous Array
		for (int i = 0;i < previous.size();i++) {
			if(previous.get(i).equals(n)) {return i;}
		}
		
		return -1;
		
	}
}
