package FooBar;

public class NumberMessage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[] sol = solution(new int[] {24,3,10,12,8,4}, 12);
		System.out.println();
		for (int i : sol) {
			System.out.print(i + " ");
		}
		System.out.println();
	}

	public static int[] solution(int[] l, int t) {
		
		int sum = 0;
		int indexStart = 0, indexEnd = 0;
		
		//Add each element of array to sum and record start and end elements of that sum
		for (int i = 0; i < l.length; i++) {
			sum += l[i];
			System.out.println(sum);
			indexEnd = i;
			
			while(sum > t){
				//If sum is now greater than key, remove front elements until it no longer is
				sum -= l[indexStart];
				System.out.println(sum);
				indexStart++;
			}
			//if sum is equal to the key, return the start and end indexes
			if(sum == t) {
				return new int[] {indexStart,indexEnd};
			} 
		}
		//If the entire array is iterated through without the sum being equal to the key, return {-1,-1}
		return new int[] {-1,-1};
    }
}
