package FooBar;

import java.util.ArrayList;
import java.util.Arrays;


public class DoomsdayFuel {
	static ArrayList<Integer> absorbing = new ArrayList<Integer>();
	static ArrayList<Integer> nonabsorbing = new ArrayList<Integer>();
	static ArrayList<Integer> newOrder = new ArrayList<Integer>();
	static Fraction[][] canon;
	
	
	
	public static class Fraction{
		
		int num;
		int denom;
		
		public Fraction(int num,int denom) {
			this.num = num;
			this.denom = denom;
		}
		public Fraction(Fraction copy) {
			this.num = copy.getNum();
			this.denom = copy.getDenom();
		}
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
		public int getDenom() {
			return denom;
		}
		public void setDenom(int denom) {
			this.denom = denom;
		}
		
		//Math Operations for Fractions
		
		public static Fraction subtract(Fraction a,Fraction b) {
			
			Fraction aCopy = new Fraction(a);
			Fraction bCopy = new Fraction(b);
			Fraction result;
			if(a.getDenom() != b.getDenom()) {
				int commonDom = getLCM(a.getDenom(),b.getDenom());
				aCopy.convert(commonDom);
				bCopy.convert(commonDom);
			}
			
			result = new Fraction(aCopy.getNum()-bCopy.getNum(),aCopy.getDenom());
			return result;
		}
		
		public static Fraction add(Fraction a,Fraction b) {
			
			Fraction aCopy = new Fraction(a);
			Fraction bCopy = new Fraction(b);
			Fraction result;
			if(a.getDenom() != b.getDenom()) {
				int commonDom = getLCM(a.getDenom(),b.getDenom());
				aCopy.convert(commonDom);
				bCopy.convert(commonDom);
			}
			
			result = new Fraction(aCopy.getNum()+bCopy.getNum(),aCopy.getDenom());
			return result;
		}
		
		public static Fraction multiply(Fraction a,Fraction b) {
			
			Fraction result = new Fraction(a.getNum()*b.getNum(),a.getDenom()*b.getDenom());
			result.simplify();
			return result;
		}
		public static Fraction multiply(Fraction a, int x) {
			
			a.setNum(a.getNum()*x);
			return a;
		}
		public static Fraction divide(Fraction a,Fraction b) {
			
			Fraction result = new Fraction(a.getNum()*b.getDenom(),a.getDenom()*b.getNum());
			result.simplify();
			return result;
			
		}
		public static int getLCM(int a, int b) {
			
			return((Math.abs(a*b))/getGCD(a,b));
		}
		public static int getMultLCM(int[] arr, int i) {
			
			if(i == arr.length-2) {
				return getLCM(arr[i],arr[i+1]);
			}
			return getLCM(arr[i],getMultLCM(arr,i+1)); 
		}
		public static int getGCD(int a, int b) {
			
			if(a == 0) {
				return b;
			}
			return getGCD(b % a, a);
		}
		public void convert(int newDom) {
			
			this.num *= (newDom/denom);
			this.denom = newDom;
			
		}
		public void simplify() {
			
			int divisor = getGCD(num,denom);
			num /= divisor;
			denom /= divisor;
			if (denom < 0) {
				denom = Math.abs(denom);
				num *= -1;
			}	
		}
	}	
	
	
	public static void main(String[] args) {

		int[][] test = new int[][]{{1}};
		int[] result = solution(test);
		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i]);
		}
		
	}
	
	public static int[] solution(int[][] m) {

		if(m.length == 1) {return new int[] {1,1};}
		canon = new Fraction[m.length][m[0].length];
		Fraction[][] temp = new Fraction[m.length][m[0].length];
		Fraction[][] temp2 = new Fraction[m.length][m[0].length];
		//Convert to Canonical Form
		
		for (int i = 0; i < m.length; i++) {
			int sum = Arrays.stream(m[i]).sum();
			if (sum == 0 || (sum == 1 && m[i][i] == 1)) {
				absorbing.add(i);
				//Change to represent absorbing state instead of terminal one
				m[i][i] = 1;
				sum = Arrays.stream(m[i]).sum();
			}else {nonabsorbing.add(i);}
			//Convert int[][] to Fraction[][];
			for (int j = 0; j < m[i].length; j++) {
				temp2[i][j] = new Fraction(m[i][j],sum);
			}
		}
		//newOrder has absorbing states first then nonabsorbing
		newOrder.addAll(absorbing);
		newOrder.addAll(nonabsorbing);
		
		//Convert temp Matrix to Canonical form
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < newOrder.size(); j++) {
				temp[i][j] = temp2[i][newOrder.get(j)];
			}
		}
		for (int i = 0; i < canon.length; i++) {
			canon[i] = temp[newOrder.get(i)];
			for (int j = 0; j < canon[i].length; j++) {
				canon[i][j].simplify();
			}
		}
		
		
		//Define A and B submatrixes from the canonical form
		Fraction[][] B = new Fraction[nonabsorbing.size()][nonabsorbing.size()];
		Fraction[][] A = new Fraction[nonabsorbing.size()][absorbing.size()];
		for (int i = 0; i < B.length; i++) {
			B[i] = Arrays.copyOfRange(canon[i + absorbing.size()], absorbing.size(), canon.length);
			A[i] = Arrays.copyOfRange(canon[i+ absorbing.size()], 0, absorbing.size());
		}
		
		Fraction[][] F;
		if(B.length == 1) {
			
			Fraction BRep = new Fraction(B[0][0].getDenom(),B[0][0].getNum());
			BRep.simplify();
			F = new Fraction[][]{{new Fraction(BRep.getDenom(),BRep.getNum())}};
		}else {
			F = (getInverse((subtractMat(getIdentityMat(B.length),B))));
		}
		//Fundamental Matrix = (I-B)^-1	
		
		//Get solution Matrix by multiply the fundamental matix by the A matrix
		Fraction[][] solMat = (multiplyMat(F,A));
		
		if(solMat.length == 1) {
			solMat[0][0].simplify();
			return new int[] {solMat[0][0].getNum(),solMat[0][0].getDenom()};
		}
		
		//Find lowest common multiple of solution fractions and convert to like denominators
		int[] denoms = new int[solMat[0].length];
		for (int i = 0; i < solMat[0].length; i++) {
			solMat[0][i].simplify();
			denoms[i] = solMat[0][i].getDenom();
		}
		int commonDom;
		if(denoms.length > 1) {
			commonDom = Fraction.getMultLCM(denoms,0);
		}else {
			commonDom = denoms[0];
		}
		int[] result = new int[solMat[0].length +1];
		//List Numerators first then denominator
		for (int i = 0; i < result.length-1; i++) {
			solMat[0][i].convert(commonDom);
			result[i] = solMat[0][i].getNum();
		}
		result[result.length-1] = commonDom;
		
		return result;
    }
	
	public static Fraction[][] getIdentityMat(int size) {
		
		Fraction[][] result = new Fraction[size][size];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result.length; j++) {
				if(i == j) {
					result[i][j] = new Fraction(1,1);
				}else {
					result[i][j] = new Fraction(0,1);
				}
			}
		}
		return result;
	}
	
	public static Fraction[][] subtractMat(Fraction[][] x, Fraction[][] y){
		
		for (int i = 0; i < y.length; i++) {
			for (int j = 0; j < y.length; j++) {
				x[i][j] = Fraction.subtract(x[i][j], y[i][j]);
				x[i][j].simplify();
			}
		}
		return x;
	}
	
	public static Fraction getDeterminant(Fraction[][] mat) {
		
		Fraction sum = new Fraction(0,1);
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat.length; j++) {
				mat[i][j].simplify();
			}
		}
		if(mat.length == 1) {
			return mat[0][0];
		}
		if(mat.length == 2) {
			Fraction result = new Fraction(Fraction.subtract(Fraction.multiply(mat[0][0], mat[1][1]), Fraction.multiply(mat[0][1], mat[1][0])));
			result.simplify();
			return result;
		}
		
		for (int i = 0; i < mat[0].length; i++) {
			sum = Fraction.add(sum, Fraction.multiply(Fraction.multiply(mat[0][i],getDeterminant(getSubMat(mat,0,i))), (int)Math.pow(-1, i)));
			sum.simplify();
		}
		return sum;
	}
	
	public static Fraction[][] getSubMat(Fraction[][] mat, int exRow, int exCol){
		
		Fraction[][] sub = new Fraction[mat.length-1][mat[0].length-1];
		
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; i != exRow && j < mat[i].length; j++) {
				if(j != exCol) {
					sub[i<exRow ? i:i-1][j<exCol?j:j-1] = mat[i][j];
				}
			}
		}
		return sub;
	}
	
	public static Fraction[][] getInverse(Fraction[][] mat){
		
		Fraction[][] inverse = new Fraction[mat.length][mat[0].length];
		if(mat.length == 1) {
			return new Fraction[][] {{new Fraction(mat[0][0].getDenom(),mat[0][0].getNum())}};
		}
		for (int i = 0; i < inverse.length; i++) {
			for (int j = 0; j < inverse.length; j++) {
				inverse[i][j] = new Fraction(Fraction.multiply(getDeterminant(getSubMat(mat,i,j)), (int)(Math.pow(-1, i+j))));
				inverse[i][j].simplify();
			}
		}
		
		Fraction det = new Fraction(getDeterminant(mat));
		Fraction detRep = new Fraction(det.getDenom(),det.getNum());
		for (int i = 0; i < inverse.length; i++) {
			for (int j = 0; j <= i; j++) {
				Fraction temp = new Fraction(inverse[i][j]);
				inverse[i][j] = Fraction.multiply(inverse[j][i], detRep);
				inverse[j][i] = Fraction.multiply(temp, detRep);
				inverse[i][j].simplify();
				inverse[j][i].simplify();
			}
		}
		return inverse;
	}
	
	public static Fraction[][] multiplyMat(Fraction[][] a, Fraction[][] b){
		
		Fraction[][] result = new Fraction[a.length][b[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j <b[0].length; j++) {
				result[i][j] = new Fraction(0,1);
				for (int k = 0; k < a[i].length; k++) {
					result[i][j] =  Fraction.add(result[i][j], Fraction.multiply(a[i][k],b[k][j]));           ;
				}
				result[i][j].simplify();
			}
		}
		return result;
	}
	public static void printMat(Fraction[][] mat) {
		System.out.println();
		for (Fraction[] i : mat) {
			for (Fraction j : i) {
				if(j.getNum() == 0 || j.getDenom() == 1) {
					System.out.print(" " + j.getNum()  +"  ");
				}else {
					System.out.printf("%d/%d ",j.getNum(),j.getDenom());
				}
				
			}
			System.out.print("\n");
		}	
	}
}
