package indexing;

import java.awt.DisplayMode;
import java.util.ArrayList;
import java.util.HashSet;

/*class Node
{
	boolean leaf;
	int noOfKey;
	float key[]; 
	Node child[];
	float addrRecord[];
	float temp; 
	Node nextLeaf;
	
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
}*/

public class BTreeIndex {
	int noOfRecords;
	//BTree bt = new BTree(noOfRecords);
	
	 public ArrayList<Node> buildUpperLevel(ArrayList<Node> list)
	{
		ArrayList<Node> uList = new ArrayList<Node>();
		int count = list.size() / (noOfRecords+1);
		if(list.size() % (noOfRecords+1) >=1)
			count++;
		int track = 0;
		for(int i=0;i<count-2;i++)
		{
			Node node = new Node(noOfRecords,false);
			for(int j=0;j<noOfRecords;j++)
			{
				//node.key[j] = list.get(j+track).key[list.get(j+track).key.length-1] + 1;
				node.key[j] = list.get(j+track+1).temp;
			}
			for(int j=0;j<noOfRecords+1;j++)
			{			
				//if(j==0)
				if(node.temp == Float.MAX_VALUE)
					node.temp = list.get(j+track).temp;
				node.child[j] = list.get(j+track);
			}
			uList.add(node);
			track += noOfRecords+1;
		}
		int left = list.size()-track;
		//System.out.println(list.size());
		//int check = (noOfRecords+1) % 2;
		if(list.size() % (noOfRecords+1) <Math.ceil(((double) noOfRecords+1)/2) && list.size() % (noOfRecords+1) > 0 && list.size() % (noOfRecords+1) != list.size() ) {
			count = left/2 +left%2  ;
			Node node = new Node(count-1,false);
			for(int j=0;j<count-1;j++)
			{
				//node.key[j] = list.get(j+track).key[list.get(j+track).key.length-1] + 1;	
				node.key[j] = list.get(j+track+1).temp;
			}
			for(int j=0;j<count;j++)
			{		
				//if(j==0)
				if(node.temp == Float.MAX_VALUE)
					node.temp = list.get(j+track).temp;
				node.child[j] = list.get(j+track);
			}
			uList.add(node);
			track += count;
		}
		else
		{

		count = Math.min(noOfRecords,list.size()-1);
		if(list.size()>=noOfRecords)
		{
			Node node = new Node(count,false);
			for(int j=0;j<count;j++)
			{
				//node.key[j] = list.get(j+track).key[list.get(j+track).key.length-1] + 1;
			//	if((j+track+1)<list.size())
				node.key[j] = list.get(j+track+1).temp;
			}
			for(int j=0;j<count+1;j++)
			{		
				//if((j+track)<list.size()) {
				//if(j==0)
				if(node.temp == Float.MAX_VALUE)
					node.temp = list.get(j+track).temp;
				node.child[j] = list.get(j+track);
				//}
			}
			uList.add(node);
			track += count+1;
			}
		}
		count = list.size()-track;
		if(count>0)
		{
			Node node = new Node(count-1,false);
			for(int j=0;j<count-1;j++)
			{
				//node.key[j] = list.get(j+track).key[list.get(j+track).key.length-1] + 1;
				node.key[j] = list.get(j+track+1).temp;
			}
			for(int j=0;j<count;j++)
			{			
				//if(j==0)
				if(node.temp == Float.MAX_VALUE)
					node.temp = list.get(j+track).temp;
				node.child[j] = list.get(j+track);
			}
			uList.add(node);
		}
		
		return uList;
	}

	ArrayList<Node> nodesToDisplay = new ArrayList<Node>();
	int indexToDisplay = 0;
	public void displayIndexNode(Node node)
	{
		if(node.leaf==false) {
			System.out.println();
			for(int j=0;j<node.key.length;j++)
				System.out.print(node.key[j] + " ");
			System.out.println();
			
			for(int j=0;j<node.child.length;j++)
			{
				System.out.print(j + "   ");
				Node n1 = node.child[j];
				if(n1!=null) {
					nodesToDisplay.add(n1);
				for(int k=0;k<n1.key.length;k++)
					System.out.print(n1.key[k] + " ");
				}
				System.out.println();
			}	
		}
	}
    public void displayIndexTree(Node root)
	{
		nodesToDisplay.add(root);
		while(indexToDisplay<nodesToDisplay.size())
		{	
			displayIndexNode(nodesToDisplay.get(indexToDisplay));
			indexToDisplay++;
		}
		System.out.println();
	}
	
    public Node getLeftMostLeafNode(Node root) 
    {
    	Node currentNode = root;
    	while(currentNode.leaf != true) {
    		currentNode = currentNode.child[0];
    	}
    	return currentNode;
    }
    public ArrayList<Float> lookup(Node root,Float key) 
    {
    	ArrayList<Float> allLocations = new ArrayList<Float>();
    	Node currentNode = root;
    	Node tempCurrentNode = null;
    	boolean endOfWork = false;
    	
    	if(root.leaf == true) {
			int temp1 = 0;
			while(true) {
				int i;    				
				for(i=0;i<root.key.length;i++) {
					if(key==root.key[i]) {  
						temp1 = 1;
    					allLocations.add(root.addrRecord[i]);
					}
					else if(key!=root.key[i] && temp1 == 1) {
						endOfWork = true;
						break;
					}
    			}
				//System.out.println(allLocations);
				if(endOfWork == true || root.nextLeaf == null)
					break;
				if(i==root.key.length) 
					root = root.nextLeaf;
			}
			
			return allLocations;
			}
    	
    	while(true) {
    		int temp = 0;
    		for(int i=0;i<currentNode.key.length;i++) { 
    			if(currentNode.key[i]==Float.MAX_VALUE) {
    				if(temp == 0) {
    					tempCurrentNode = currentNode.child[i];	 
    					temp = 1;
    				}
    			}
    			else {
	    			if(i==0 && key<currentNode.key[i]) {
	    					currentNode = currentNode.child[0];
	    					tempCurrentNode = currentNode;
	    				break;
	    			}
	    			else if(i==currentNode.key.length-1 && key>=currentNode.key[i]) {
	    				currentNode = currentNode.child[i+1];
	    				tempCurrentNode = currentNode;
	    				break;
	    			}
	    			if(i!=0) {
		    			if(key>=currentNode.key[i-1] && key<currentNode.key[i]) {
		    					currentNode = currentNode.child[i];
		    					tempCurrentNode = currentNode;
		    				break;
		    			}
	    			}
    			}
    		}
    		currentNode = tempCurrentNode;
    		if(currentNode.leaf == true) {
    			int temp1 = 0;
    			while(true) {
    				int i;    				
    				for(i=0;i<currentNode.key.length;i++) {
    					if(key==currentNode.key[i]) {  
    						temp1 = 1;
        					allLocations.add(currentNode.addrRecord[i]);
    					}
    					else if(key!=currentNode.key[i] && temp1 == 1) {
    						endOfWork = true;
    						break;
    					}
        			}
    				//System.out.println(allLocations);
    				if(endOfWork == true || currentNode.nextLeaf == null)
    					break;
    				if(i==currentNode.key.length) 
    					currentNode = currentNode.nextLeaf;
    				}
    			}
    		
    		if(currentNode.leaf == true && (endOfWork == true  || currentNode.nextLeaf == null))
				break;
    			/*for(int i=0;i<currentNode.key.length;i++) {
    				if(key==currentNode.key[i])
    					return currentNode.addrRecord[i];
    			}*/
    		}
    	return allLocations;
    	}
    
    
	public Node createIndexTree(float keys[], float values[], int no_Of_Records) 
	{
		noOfRecords = no_Of_Records;
		if(keys.length<=noOfRecords) {
			Node leaf = new Node(noOfRecords,true);
			leaf.nextLeaf = null;
			for(int i=0;i<keys.length;i++)
			{
				leaf.key[i] = keys[i];
				leaf.addrRecord[i] = values[i];
			}
			return leaf;
		}
		else {
		ArrayList<Node> leafList = new ArrayList<Node>();
		HashSet<Float> keysTillNow = new HashSet<Float>();
		int count = keys.length / noOfRecords;
		
		if(keys.length % noOfRecords >=1)
			count++;
		
		int track = 0;
		for(int i=0;i<count-2;i++)
		{
			Node leaf = new Node(noOfRecords,true);
			for(int j=0;j<noOfRecords;j++)
			{
				//if(j==0)
				if(leaf.temp == Float.MAX_VALUE && !keysTillNow.contains(keys[j+track]))
					leaf.temp = keys[j+track];
				leaf.key[j] = keys[j+track];
				leaf.addrRecord[j] = values[j+track];
				keysTillNow.add(keys[j+track]);
				//leaf.child[j] = null;
			}
			if(!leafList.isEmpty()) {
				leafList.get(leafList.size()-1).nextLeaf = leaf;
			}
				leafList.add(leaf);
			
			//track = (i+1)*(j+1);
			track += noOfRecords;
		}
		
		int left = keys.length-track;
		//System.out.println("left " + keys.length + " " + track);
		if(keys.length % (noOfRecords) <(noOfRecords+1)/2)
			count = left/2 +left%2  ;		
		else
			count = noOfRecords;	
		
	//	System.out.println(count);
		Node leaf = new Node(count,true);
		for(int j=0;j<count;j++)
		{
			//if(j==0)
			if(leaf.temp == Float.MAX_VALUE && !keysTillNow.contains(keys[j+track]))
				leaf.temp = keys[j+track];
		//	System.out.println("hey " + j + "  " + j+track + "  " + keys[j+track]);
			leaf.key[j] = keys[j+track];
			leaf.addrRecord[j] = values[j+track];
			keysTillNow.add(keys[j+track]);
		//	leaf.child[j] = null;
		}
		if(!leafList.isEmpty()) {
			leafList.get(leafList.size()-1).nextLeaf = leaf;
		}
			leafList.add(leaf);
		
		//track = (i+1)*(j+1);
		track += count;
		
		count = keys.length-track;
		leaf = new Node(count,true);
		for(int j=0;j<count;j++)
		{
			//if(j==0)
			if(leaf.temp == Float.MAX_VALUE && !keysTillNow.contains(keys[j+track]))
				leaf.temp = keys[j+track];
	//		System.out.println("hey " + j + "  " + j+track + "  " + keys[j+track]);
			leaf.key[j] = keys[j+track];
			leaf.addrRecord[j] = values[j+track];
			keysTillNow.add(keys[j+track]);
		//	leaf.child[j] = null;
		}
		
		if(!leafList.isEmpty()) {
			leafList.get(leafList.size()-1).nextLeaf = leaf;
		}
			leafList.add(leaf);
		
		
		ArrayList<Node> uList = buildUpperLevel(leafList);
		
		while(uList.size()>1) {
			uList = buildUpperLevel(uList);
		}
		
		Node root = uList.get(0);
		return root;
		}
	}

}
