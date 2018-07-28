package tc.dnasplicerchallenge.mainpk;

import java.util.ArrayList;

public class DNAPart {

	private ArrayList<Integer> sol;
	private ArrayList<String> curList;
	private String part;
	
	public DNAPart(String part, ArrayList<String> curList, ArrayList<Integer> sol) {
		this.part = part;
		this.curList = curList;
		this.sol = sol;
	}
	
	/** Solutions calculated at this point */
	public ArrayList<Integer> solution(){
		return sol;
	}
	
	/** Current list ar this point */
	public ArrayList<String> curlist(){
		return this.curList;
	}
	
	/** Curren string being analyzed */
	public String dnastr() {
		return part;
	}
	
}
