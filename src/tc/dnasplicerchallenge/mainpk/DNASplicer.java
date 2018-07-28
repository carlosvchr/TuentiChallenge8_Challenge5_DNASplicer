package tc.dnasplicerchallenge.mainpk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;

public class DNASplicer {

	public static void main(String[] args) {
		// Create conexion giving ip and port
		Connex conn = new Connex("52.49.91.111", 3241);
		conn.open();
		String data;

		System.out.println(conn.receive());
		System.out.println(conn.receive());
		conn.send("SUBMIT");
		System.out.println(conn.receive());
		
		while((data=conn.receive()) != null) {
			System.out.println(data);
			ArrayList<Integer> sol = processData(data);
			Collections.sort(sol);
			String strSol = (sol.get(0)+1)+"";
			for(int i=1; i<sol.size(); i++) {
				strSol += ","+(sol.get(i)+1);
			}
			System.out.println(strSol);
			// Send data in string format (coma separated)
			conn.send(strSol);
			String recvdata = conn.receive();
			System.out.println(recvdata);
		}
		
		conn.close();
		
	}
	
	/** Process the input data and return the solution in an unordered Integer list */
	public static ArrayList<Integer> processData(String data) {
		/** Prepare data */
		ArrayList<String> dataList = new ArrayList<>(Arrays.asList(data.split(" ")));
		Hashtable<String, Integer> original = new Hashtable<>();
		
		for(int i=0; i<dataList.size(); i++) {
			original.put(dataList.get(i), i);
		}
		
		/** Get the parts which other parts can start, so these part have to be the headers of the dna combination */
		ArrayList<DNAPart> starters = getBegMatches(dataList, original);
		
		/** Calculate recursively until find a solution or a null */
		for(int i=0; i<starters.size(); i++) {
			ArrayList<Integer> res = processData(starters.get(i), original);
			if(res != null) return res;
		}
		
		return null;
	}
	
	
	/** Each iteration we take the curren string being analyzed and get all possible
	 * matches to other parts. When it matches, the match is deleted from the list to not being analyzed twice.
	 * Now we take the difference between the current part and the match. That difference will be the next current.
	 * When the difference is 0, then we have found a solution. If there are not any match and the current part is
	 * longer than 0. Then it has not a solution and return null. */
	public static ArrayList<Integer> processData(DNAPart cur, Hashtable<String, Integer> o){
		System.out.println(cur.dnastr());
		// If all part has been processed without residues, we finish
		if(cur.dnastr().length() == 0) return cur.solution();
		
		ArrayList<DNAPart> matches = getMatches(cur, o);
		for(int i=0; i<matches.size(); i++) {
			ArrayList<Integer> res = processData(matches.get(i), o);
			if(res != null) return res;
		}
		
		// Return null when no matches
		return null;
	}
	
	/** Get all parts that fits to our current part */
	public static ArrayList<DNAPart> getMatches(DNAPart cur, Hashtable<String, Integer> o){
		ArrayList<DNAPart> matches = new ArrayList<>();
		for(int i=0; i<cur.curlist().size(); i++) {
			/* E.g. cur=ABA and i=ABACS, then i is removed from the list and 
			 * now cur=CS. sol=sol+i. list=list-i */
			if(cur.dnastr().startsWith(cur.curlist().get(i)) || cur.curlist().get(i).startsWith(cur.dnastr())) {
				ArrayList<String> cpy = new ArrayList<>(cur.curlist());
				cpy.remove(cur.curlist().get(i));
				ArrayList<Integer> sol = new ArrayList<>(cur.solution());
				sol.add(o.get(cur.curlist().get(i)));
				String ncur;
				if(cur.dnastr().length() < cur.curlist().get(i).length()) {
					ncur = cur.curlist().get(i).substring(cur.dnastr().length(), cur.curlist().get(i).length());
				}else {
					ncur = cur.dnastr().substring(cur.curlist().get(i).length(), cur.dnastr().length());
				}		
				matches.add(new DNAPart(ncur, cpy, sol));			
			}
		}
		
		return matches;
	}
	
	/** Get the dna part that can be starters */
	public static ArrayList<DNAPart> getBegMatches(ArrayList<String> l, Hashtable<String, Integer> o) {
		ArrayList<DNAPart> starters = new ArrayList<>();
		/* Compare all elements with each other (but not with itself). */
		for(int i=0; i<l.size(); i++) {
			for(int j=0; j<l.size(); j++) {
				if(i!=j) {
					/* If some is contained inside another one, that one can be 
					 * a header of our dna part, so we take it in consideration*/
					if(l.get(i).startsWith(l.get(j))) {
						ArrayList<String> cpy = new ArrayList<>(l);
						cpy.remove(l.get(j));
						ArrayList<Integer> sol = new ArrayList<>();
						sol.add(o.get(l.get(j)));
						starters.add(new DNAPart(l.get(j), cpy, sol));
					}
				}
			}
		}	
		return starters;
	}

}
