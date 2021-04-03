package FooBarBraille;

public class BrailleMain {

	static String alphabet = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static String[] brailleChars = {"000000","100000","101000","110000","110100","100100","111000","111100","101100","011000","011100"
			,"100010","101010","110010","110110","100110","111010","111110","101110","011010","011110","100011","101011","011101"
			,"110011","110111","100111"};
	
	
	    public static String solution(String s) {
	    	
	    	char[] sChar = s.toCharArray();
	    	String output = "";
	    	
	    	for (int i = 0; i < sChar.length; i++) {
				output += indexToBraille(alphabet.indexOf(sChar[i]));
			}
			return output;
	    }
	
	    public static String indexToBraille(int index) {
	    	
	    	String brailleOutput = "";
	    	if(index != -1) {
	    		if(index > 26) {
	    			index -= 26;
	    			brailleOutput += "000001";
	    		}
	    		String upAndDown = brailleChars[index];
	    		String sideToSide = "" + upAndDown.charAt(0) + upAndDown.charAt(2) + upAndDown.charAt(4) + upAndDown.charAt(1) + upAndDown.charAt(3) + upAndDown.charAt(5);
	    		brailleOutput += sideToSide;
	    	}
	    	return brailleOutput;
	    }
	
	    
	
	public static void main(String[] args) {

		System.out.println(solution("Braille"));
		System.out.println("000001110000111010100000010100111000111000100010".equals(solution("Braille")));
		
	}

}
