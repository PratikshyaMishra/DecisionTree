package decisionTree.com;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;


public class DecisionTree {
	private TreeNode root;
	private double totalEntropy;
	private Queue<TreeNode> treeQueue = new LinkedList<TreeNode>();
	//private int treeNodecount = 0;
	ArrayList<ArrayList<Character>> listofBP = new ArrayList<ArrayList<Character>>();
	public DecisionTree(String fileName) {
		super();
		root = new TreeNode();
		ReadFile readObj = new ReadFile(fileName);
		ArrayList<String> list9Mer = readObj.get9MerData();
		/*
		 * Extract the base pairs from the authentic 9 mers and make an ArrayList of characters 
		 * make an ArrayList of the character ArrayList to store all the authentic 9 mers 
		 */
		for(String get9Mer:list9Mer ){
			ArrayList<Character> getBP = new ArrayList<Character>();
			for(Character bpVal:get9Mer.toCharArray()){
				getBP.add(bpVal);
			}
			listofBP.add(getBP);
		}
		System.out.println();
	}
	/**
	 * The method finds the ovrerall entropy 
	 */
	public void calculateTotalEntropy()
	{
		//Since there are 4 Base Pairs A,C,G,T`
		totalEntropy = Math.log(4)/Math.log(2);
		System.out.println("Total Entropy:"+totalEntropy);
	}
	/**
	 * the method calculates the entropy of
	 * @param bptoPositionTable is the table that contains the 
	 * count of A,G,C.T at position 0-9
	 */
	public HashMap<Integer, Double> calculateEntropy(HashMap<Integer, Character> positionBPselected,HashMap<Integer,
			HashMap<Character, Integer>> BPPositionTable){
		HashMap<Integer, Double> positionEntropy = new HashMap<Integer, Double>();
		double dataSize =0;
		if(BPPositionTable.size()>0){
			Collection<Integer> getOuter = BPPositionTable.get(0).values();
				for(Integer getCount:getOuter){
					if(getCount!=-1)
						dataSize += getCount;
			}
		}
		/*
		 *The block calculates the Shannon entropy H(p) = -sum(p(x)log2p(x))
		 */
		for(Entry<Integer, HashMap<Character,Integer>> bpPositionCount:BPPositionTable.entrySet()){
			double count=0;
			double entropy=0;
			HashMap<Character,Integer> bpCount = bpPositionCount.getValue();
			for(Entry<Character,Integer> getCount:bpCount.entrySet()){
				count = getCount.getValue();
				double probability = (double)count/dataSize;/*Find the probability*/
				entropy += -((probability)* (Math.log(probability)/Math.log(2)));
			}
			positionEntropy.put(bpPositionCount.getKey(), entropy);/* save the entropy for the position*/
		}
		return positionEntropy;
	}
	public HashMap<Integer,HashMap<Character,Integer>> checkPositionUsedInTree(HashMap<Integer,Character> positionBPSelected){
		ArrayList<ArrayList<Character>> updateListBP = new ArrayList<ArrayList<Character>>();
		//a list that keeps the nucleotide as it appears in input. HashMap<position,nucleotide>
		ArrayList<HashMap<Integer,Character>> bpto9MerPosition = new ArrayList<HashMap<Integer,Character>>();
		//count of BP (A,C,G,T) in that position HashMap<Position,HashMap<Nucleotide,count>>();
		HashMap<Integer,HashMap<Character,Integer>> bPtoPositionTable =
				new HashMap<Integer,HashMap<Character,Integer>>();
		//what all nucleotide are present in a position HashMap<Position, ArrayList<Nucleotide>>();
		HashMap<Integer, ArrayList<Character>> bpListMapforPosition =
				new HashMap<Integer, ArrayList<Character>>();
		ArrayList<Character> bpListforPosition;
		HashMap<Character, Integer> bpCountforPosition;
		if(positionBPSelected.size()!=0 ){
			for(ArrayList<Character> bpList:listofBP){
				int count=0;
				for(Entry<Integer,Character> bpPosEntry:positionBPSelected.entrySet()){
					if(bpList.get(bpPosEntry.getKey())== bpPosEntry.getValue()){
						count++;
					}
				}
				if(count == positionBPSelected.size()){
					updateListBP.add(bpList);
				}
			}
		}
		else
			updateListBP = listofBP;
		for(ArrayList<Character> authentic9mer:updateListBP){
			HashMap<Integer, Character> inner= new HashMap<Integer, Character>();
			int i=0;
			for(Character c:authentic9mer){
				inner.put(i, c);
				i++;
			}
			bpto9MerPosition.add(inner);
		}
		/*
		 * the following block creates the count of the bp (A,C, G ,T) for a 
		 * position in the Authentic 9mer
		 */
		for(HashMap<Integer,Character> bptoPositionMap:bpto9MerPosition){
			for(int i=0;i<bptoPositionMap.size();i++){
				if(bpListMapforPosition.containsKey(i)){
					bpListforPosition = bpListMapforPosition.get(i);
					bpListforPosition.add(bptoPositionMap.get(i));
				}
				else{
					bpListforPosition = new ArrayList<Character>();
					bpListforPosition.add(bptoPositionMap.get(i));		
				}
				bpListMapforPosition.put(i, bpListforPosition);
			}
		}
		for(Entry<Integer,ArrayList<Character>> bpListMapforPositionEntry : bpListMapforPosition.entrySet()){
			for(Character bpinList:bpListMapforPositionEntry.getValue()){
				if(!(bpListMapforPositionEntry.getKey()==3 || bpListMapforPositionEntry.getKey()==4)){//excluding position for G and T
					if(bPtoPositionTable.containsKey(bpListMapforPositionEntry.getKey())){  /*check if position is already present in the hashmap*/
						bpCountforPosition = bPtoPositionTable.get(bpListMapforPositionEntry.getKey());
						if(bpCountforPosition.containsKey(bpinList)){ /*check if bp is already present in the hashmap*/
							int count = bpCountforPosition.get(bpinList);
							bpCountforPosition.put(bpinList,(count+1));
						}
						else{
							bpCountforPosition.put(bpinList, 1); /*set the count of the bp t 1*/
						}
						bPtoPositionTable.put(bpListMapforPositionEntry.getKey(), bpCountforPosition);/*update the position value with the new hashmap*/
					}
					else{
						bpCountforPosition = new HashMap<Character, Integer>();/*create a new hashmap*/
						bpCountforPosition.put(bpinList, 1);/*set the count of the bp t 1*/
						bPtoPositionTable.put(bpListMapforPositionEntry.getKey(), bpCountforPosition);/*create the position value with the new hashmap*/
					}
			}
			}
				
		}
		return bPtoPositionTable;
	}
	/**
	 * The method builds the decision Tree
	 */
	public TreeNode buildDecisionTree(){
		//TreeNode root = new TreeNode();
		root.setParent(null);
		root.addPositionBpUsed(root,0, ' ');
		calculateTotalEntropy();
		setAttributeAfterSplit(root,new HashMap<Integer, Character>());
		System.out.println("The root node has the 9'mer Position: "+root.getNodeLabel());
		treeQueue.add(root);
		expandTree(treeQueue);
		return root;
	}
	public void expandTree(Queue<TreeNode> treeQueue){	
		char[] bp = {'A','C','G','T'};	
		while(!treeQueue.isEmpty()){	
			TreeNode parent = treeQueue.remove();
			System.out.println("The parent is: " + parent.getNodeLabel());
			for(int i=0;i<bp.length;i++){
				TreeNode childBp = new TreeNode();
				System.out.println("Child for Bp: " + bp[i]);
				childBp.setParent(parent);
				childBp.setBranchTaken(bp[i]);
				childBp.addPositionBpUsed(childBp,parent.getNodeLabel(), bp[i]);
				//positionBpUsed.put(parent.getNodeLabel(),bp[i]);
				setAttributeAfterSplit(childBp,childBp.getPositionBpUsed());
				parent.addChild(childBp);
				if(!childBp.getLeaf()){
					treeQueue.add(childBp);
				}
			}
			expandTree(treeQueue);
		}
	}
	/**
	 * The method calculates the position that can be the attribute after
	 * applying the entropy and information gain formula based on the nucleotide and parent position
	 */
	public void setAttributeAfterSplit(TreeNode node,HashMap<Integer, Character> positionBpUsed){
		HashMap<Integer,HashMap<Character,Integer>> bP9mertoPositionTable =
				checkPositionUsedInTree(positionBpUsed);
		HashMap<Integer, Double> positionEntropy = calculateEntropy(positionBpUsed,bP9mertoPositionTable);
		double maxGain = Double.MIN_VALUE;
		boolean flagMaxGainChanged =false;
		double informationGain = 0; 
		int position = -1;
		double entropy=0;
		for(Entry<Integer,Double> getEntropy:positionEntropy.entrySet()){
			//if(getEntropy.getValue()!=0){
				informationGain = getTotalEntropy() - getEntropy.getValue();
				if(maxGain < informationGain && !positionBpUsed.containsKey(getEntropy.getKey())){
					maxGain = informationGain;
					position = getEntropy.getKey();
					entropy = getEntropy.getValue();
					flagMaxGainChanged = true;
				}
			//}
		}
		if(flagMaxGainChanged){
			node.setInformationGain(maxGain);
			node.setNodeLabel(position);
			node.setEntropy(entropy);
			bP9mertoPositionTable.get(position);
			flagMaxGainChanged = false;
			System.out.println("The Position:"+ (position)+"  Entropy:"+entropy+"  Information Gain"+maxGain);
		}
		else{
			node.setLeaf(true);
			System.out.println("for the branch:"+node.getBranchTaken()+" the child is a leaf");
		}
	}
	/**
	 * getter method to return total Authentic Entropy
	 * @return total entropy
	 */
	public double getTotalEntropy() {
		return totalEntropy;
	}
	public void score9mer(){
		
	}
/*	public ArrayList<Integer> classify(List<TreeNode> testInstances){
		ArrayList<Integer> predictions = new ArrayList<Integer>();
		for (Instance t : testInstances) {
			// System.out.println("instance" + t.uniqueId);
			int predictedCategory = root.classify(t);
			predictions.add(predictedCategory);
		}
		return predictions;
	}*/
	/**
	 * the main method
	 * @param args is the list pf arguments the main can take
	 */
	public static void main(String[]args) {
		DecisionTree treeAuthentic = new DecisionTree("AuthenticSpliceSite.txt");
		treeAuthentic.root = treeAuthentic.buildDecisionTree();
		treeAuthentic.root.writeTreeXML("tree_authentic_fulltree.xml");
		DecisionTree treeCryptic= new DecisionTree("CrypticSpliceSite.txt");
		treeCryptic.root = treeCryptic.buildDecisionTree();
		treeCryptic.root.writeTreeXML("tree_cryptic_fulltree.xml");
		System.out.println();
	}
}
