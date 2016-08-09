package indexing;

public class Node {
	public boolean leaf;
	public int noOfKey;
	public float key[]; 
	public Node child[];
	public float addrRecord[];
	public float temp; 
	public Node nextLeaf;
	
    Node(int noOfKey,boolean leaf)
    {
    	this.temp = Float.MAX_VALUE;
    	this.leaf = leaf;
    	this.noOfKey = noOfKey;
    	key = new float[noOfKey]; 
    	if(this.leaf == false)
    		child = new Node[noOfKey+1];
    	else 
    		addrRecord = new float[noOfKey];
    }	
}
