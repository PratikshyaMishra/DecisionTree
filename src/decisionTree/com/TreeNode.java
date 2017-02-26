package decisionTree.com;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * 
 * @author Pratikshya Mishra
 * the class provides a tree which has the fields
 * Children for the child nodes, branchTaken to label the branch as A. C, G or T
 * InformationGain to have the Information gain of that node
 *
 */
public class TreeNode {
	private TreeNode parent;
	private char branchTaken;
	private ArrayList<TreeNode> children;
	private int nodeLabel =-1;
	private double InformationGain;
	private double entropy;
	private boolean isLeaf = false;
	private HashMap<Integer, Character> positionBpUsed;
	public TreeNode getParent() {
		return parent;
	}
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}
	public char getBranchTaken() {
		return branchTaken;
	}
	public void setBranchTaken(char branchTaken) {
		this.branchTaken = branchTaken;
	}
	public ArrayList<TreeNode> getChildren() {
		return children;
	}
	public int getNodeLabel() {
		return nodeLabel;
	}
	public void setNodeLabel(int nodeLabel) {
		this.nodeLabel = nodeLabel;
	}
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	public boolean getLeaf(){
		return isLeaf;
	}
	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}
	public double getEntropy() {
		return entropy;
	}
	/*
	 * The ctor creates an ArrayList for the node children
	 */
	public TreeNode() {
		super();
		children = new ArrayList<TreeNode>();
		positionBpUsed = new HashMap<Integer, Character>();
	}
	public double getInformationGain() {
		return InformationGain;
	}
	public void setInformationGain(double informationGain) {
		InformationGain = informationGain;
	}
	public void addChild(TreeNode child)
	{
		children.add(child);
	}
	public void addPositionBpUsed(TreeNode child,int position, char bp)
	{
		if(parent!=null){
			child.positionBpUsed.putAll(parent.getPositionBpUsed());
			child.positionBpUsed.put(position, bp);
		}
		else
			child.positionBpUsed = new HashMap<Integer, Character>();
	}
	public HashMap<Integer, Character> getPositionBpUsed() {
		return positionBpUsed;
	}
	/**
	 * Exports the decision tree rooted from this node to a TreeML file.
	 * 
	 * @param filename
	 *            The name of the target file
	 */
	public void writeTreeXML(String filename) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(filename));
			writer.println("<?xml version=\"1.0\" ?>");
			writer.println("<tree>");
			writer.println("<declarations>");
			writer.println("<attributeDecl name=\"name\" type=\"String\" />");
			writer.println("</declarations>");
			writeTreeML(writer);
			writer.println("</tree>");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void writeTreeML(PrintWriter writer) {
		if (nodeLabel != -1){
			writer.println("<branch>");
			System.out.println("<branch>");
			writer.print("<attribute name=\"position\" value=\"");
			System.out.print("<attribute name=\"position\" value=\"");
			if (parent == null){
				writer.print(nodeLabel);
				System.out.print(nodeLabel);
			}
			else {
				for (int i = 0; i < parent.children.size(); i++) {
					if (this == parent.children.get(i)) {
						writer.print(parent.nodeLabel+ "  Nucleotide  " + branchTaken);
						System.out.print(nodeLabel+ "  Nucleotide  " + branchTaken);
					}
				}
			}
			writer.println("\" />");
			System.out.println("\" />");
		}
		else{
			writer.println("<leaf>");
			System.out.println("<leaf>");
		}
		if (nodeLabel != -1) {
			for (int i = 0; i < children.size(); i++) {
				if (children.get(i) != null)
					children.get(i).writeTreeML(writer);
			}
			writer.println("</branch>");
			System.out.println("</branch>");
		} else {
			writer.println("<attribute name=\"Nucleotide\" value=\""
					+ branchTaken + "\" />");
			System.out.println("<attribute name=\"Nucleotide\" value=\""
					+ branchTaken + "\" />");
			writer.println("</leaf>");
			System.out.println("</leaf>");
		}
	}
}
