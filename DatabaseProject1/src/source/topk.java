package source;
import indexing.BTreeIndex;
import indexing.Node;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;


class CSV
{
	float columns[];
}

class OUTPUT
{
	float score;
	CSV csv;
	OUTPUT next;
	
	OUTPUT()
	{
		
	}
	
	OUTPUT(float s,CSV o)
	{
		this.score = s;
		this.csv = o;
		this.next = null;
	}
	float getScore()
	{
		return score;
	}
}

/*class IntComparator implements Comparator<OUTPUT> {

    @Override
    public int compare(OUTPUT o1, OUTPUT o2) {
    	return (o1.score>o2.score ? -1 : (o1.score==o2.score ? 0 : 1));
    }
}*/

public class topk {

	static CSV[] joinOnCondition(CSV[] table1, CSV[] table2, String header1, String header2, String column1, String column2,int no1,int no2) {
		String[] row1 = header1.split(",");
		String[] row2 = header1.split(",");
		ArrayList<CSV> row = new ArrayList<CSV>();
		int columnNo1=-1,columnNo2=-1;
		int tempNo = 0;
		for(int i=0;i<row1.length;i++) {
			if(row1[i].equals(column1) && tempNo==no1) {
				columnNo1 = i; break; }
			if(row1[i].equals(column1)) 
			tempNo++;
		}
		tempNo=0;
		for(int i=0;i<row2.length;i++) {
			if(row2[i].equals(column2)&& tempNo==no2)  {
				columnNo2 = i; break; }
			if(row2[i].equals(column2))
				tempNo++;
		}
		
		for(int i=0;i<table1.length;i++) {
			for(int j=0;j<table2.length;j++) {
				if(table1[i].columns[columnNo1] == table2[j].columns[columnNo2]) {
					CSV obj = new CSV();
					obj.columns = new float[table1[i].columns.length+table2[j].columns.length];
					int k=0;
					float col[] = table1[i].columns;
					for(int l=0;l<col.length;l++) 
						obj.columns[k++] = col[l];
					col = table2[j].columns;
					for(int l=0;l<col.length;l++) 
						obj.columns[k++] = col[l];
					
					row.add(obj);
				//	table[index++] = obj;
				}
			}			
		}
		CSV[] table = new CSV[row.size()];
		for(int i=0;i<row.size();i++) {
			table[i] = row.get(i);
		}
		return table;
	}
	
	static CSV[] cartesianProduct(CSV[] table1, CSV[] table2) {
		CSV[] table = new CSV[table1.length*table2.length];
		int index = 0;
		for(int i=0;i<table1.length;i++) {
			for(int j=0;j<table2.length;j++) {
				CSV obj = new CSV();
				obj.columns = new float[table1[i].columns.length+table2[j].columns.length];
				int k=0;
				float col[] = table1[i].columns;
				for(int l=0;l<col.length;l++) 
					obj.columns[k++] = col[l];
				col = table2[j].columns;
				for(int l=0;l<col.length;l++) 
					obj.columns[k++] = col[l];
				
				table[index++] = obj;
			}			
		}
		return table;
	}
	
	static void displayTable(CSV[] table, String header) {
		String[] row = header.split(",");
		for(int k=0;k<row.length;k++)
			System.out.format("%-8s",row[k]);
		System.out.println();
		for(int i=0;i<table.length;i++) {
			float columns[] = table[i].columns;
			for(int j=0;j<columns.length;j++) {
				 if(j==0) 
					 System.out.format("%-8s", String.valueOf((int) Math.round(columns[j])));
					 //System.out.print((int) Math.round(columns[j]) + "  ");
				 else
					 System.out.format("%-8s", String.valueOf(columns[j]));
					// System.out.print(columns[j] + "  ");
			 }
			System.out.println();
		}
	}
	
	static void displayAllTables(ArrayList<CSV[]> tables,ArrayList<String> fileNames,ArrayList<String> headers) {
		for(int i=0;i<tables.size();i++) {
			System.out.println(fileNames.get(i));
			//System.out.format("%-10s", "Score");
			String[] row = headers.get(i).split(",");
			for(int k=0;k<row.length;k++)
				System.out.format("%-8s",row[k]);
			System.out.println();
			CSV obj[] = tables.get(i);
			for(int k=0;k<obj.length;k++) {
				float columns[] = obj[k].columns; 
				for(int j=0;j<columns.length;j++) {
					 if(j==0) 
						 System.out.format("%-8s", String.valueOf((int) Math.round(columns[j])));
						 //System.out.print((int) Math.round(columns[j]) + "  ");
					 else
						 System.out.format("%-8s", String.valueOf(columns[j]));
						// System.out.print(columns[j] + "  ");
				 }
				System.out.println();
			}
			
		}
	}
	
	static OUTPUT insert(OUTPUT root, OUTPUT node)
	{
		OUTPUT output = root;
		if(root== null)
			return node;
		else if(root.next == null)
		{
			if(root.score>=node.score) {
				root.next = node;
				return root;
			}
			else {
				node.next = root;
				return node;
			}
		}
		else if(node.score>root.score)
		{
			node.next = root;
			return node;
		}
		else {
		while(root.next != null && root.next.score >= node.score)
		{
			root = root.next;
		}
		node.next = root.next;
		root.next = node;
		}
		return output;
	}
	static void displayOutput(OUTPUT output,int K,String header)
	{
		int i = 0;
		System.out.format("%-10s", "Score");
		String[] row = header.split(",");
		for(int k=0;k<row.length;k++)
			System.out.format("%-8s",row[k]);
		System.out.println();
		while(output!=null && i<K)
		{
			System.out.format("%-10s", String.valueOf(output.score));
			//System.out.print(output.score + " : ");
			float columns[] = output.csv.columns;
			 for(int j=0;j<columns.length;j++) {
				 if(j==0) 
					 System.out.format("%-8s", String.valueOf((int) Math.round(columns[j])));
					 //System.out.print((int) Math.round(columns[j]) + "  ");
				 else
					 System.out.format("%-8s", String.valueOf(columns[j]));
					// System.out.print(columns[j] + "  ");
			 }
			 
			 System.out.println();
			 output = output.next;
			 i++;
		}
	}
	
	static int countEntries(OUTPUT output,float threshold)
	{
		int count = 0;
		while(output!=null)
		{
			//System.out.print(output.score + " : ");
			if(output.score>=threshold)
				count++;
			else
				break;
			output = output.next;
		}
		return count;
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader br = null;
		int K = Integer.parseInt(args[0]);
		int N = Integer.parseInt(args[1]);
		ArrayList<CSV> rowObjects = new ArrayList<CSV>();    //contains all rows of csv file
		ArrayList<CSV> rowObjectsJoin = new ArrayList<CSV>();
		boolean simpleOrJoin = false;
		Node bTreeRoots[] = null;
		Node bTreeRootsJoin[] = null;
		String input = null;
		Scanner in = new Scanner(System.in);
		OUTPUT output = null;
		String header = new String();
		// bTreeRoots[0] is primary index - others are secondary indexes
		do
		{
			
			output = null;   //for run2
			System.out.println("topk> ");
			input = in.nextLine();	
			String command[] = input.split(" ");
			float parameters[] = new float[command.length];    //v1..vN values
			try {
				for(int i=1;i<command.length;i++)
				{
					parameters[i-1] = Float.parseFloat(command[i]);
				}
			}
			catch(NumberFormatException nfe) {
				//ignore
			}
			
//init command			
			if(command[0].equals("init")) {
				String currentPath;
				String line;
				ArrayList<String> fileNames = new ArrayList<String>();
				ArrayList<String> joinConditions = new ArrayList<String>();
				if(command.length == 2) {
					bTreeRoots = null;
					rowObjects = new ArrayList<CSV>(); 
						try {
							//br = new BufferedReader(new FileReader("/home/dhiren/Desktop/buffalo/sem2/DBMS/Project1/NBA.csv"));
							currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
							//System.out.println(currentPath);
							br = new BufferedReader(new FileReader(currentPath + "/src/data/" + command[1]));
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						try {
							header = br.readLine();					
							String row[] = header.split(",");
							System.out.println("N = " + (row.length-1));
							while ((line = br.readLine()) != null) {
		
						        // use comma as separator
							row = line.split(",");
							
							CSV obj = new CSV();
							obj.columns = new float[row.length];
							
							for(int i=0;i<row.length;i++)
								obj.columns[i] = Float.parseFloat(row[i]);
							
							rowObjects.add(obj);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					/*	for(int i=0;i<rowObjects.size();i++)
						{
							CSV obj = rowObjects.get(i);
							for(int j=0;j<obj.columns.length;j++)
								System.out.print(obj.columns[j] + " ");
							System.out.println();
						}		
						System.out.println();
						System.out.println();
						System.out.println();	*/						
						bTreeRoots = new Node[rowObjects.get(0).columns.length];  // contains btrees for all attributes
						
						
						int n = rowObjects.size();
						BTreeIndex BTI = new BTreeIndex();
						
						for(int i=0; i<rowObjects.get(0).columns.length;i++) {
							float keys[] = new float[n];
							float values[] = new float[n];
							for(int j=0;j<rowObjects.size();j++)  //rowObjects.size() instead of 20
							{
								CSV csv = rowObjects.get(j);
								keys[j] = csv.columns[i];
								if(i==0)
									values[j] = j;
								else
									values[j] = csv.columns[0];
							}
							if(i!=0) {
								MergeSort ms = new MergeSort();
								ms.mergeSort(keys,values,"Decr");
							}
							else {
								MergeSort ms = new MergeSort();
								ms.mergeSort(keys,values,"Incr");
							}
							bTreeRoots[i] = BTI.createIndexTree(keys,values,30);				
						}
						//BTI.displayIndexTree(bTreeRoots[1]);	
						simpleOrJoin = false;
			}
			else {
				bTreeRootsJoin = null;
				rowObjectsJoin = new ArrayList<CSV>(); 
					//Start -- Process command 
									boolean flag = false;
									for(int i=1 ;i<command.length;i++) {
										if(command[i].equals("on")) {
											flag = true;
											continue;
										}
										if(flag == true && !command[i].equals("[") && !command[i].equals("]") && !command[i].equals(","))
											joinConditions.add(command[i]);
										else if(flag==false)
											fileNames.add(command[i]);
									}
					//End -- Process command  	
					//Start -- Table Reading				
									CSV[][] rows = new CSV[1][];
									ArrayList<CSV[]> tables = new ArrayList<CSV[]>(); 
									ArrayList<CSV> temp = new ArrayList<CSV>(); 
									ArrayList<String> headers = new ArrayList<String>(); 
									//String headers[] = new String[fileNames.size()];
									currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
								//	System.out.println(currentPath);
									
									for(int i=0;i<fileNames.size();i++) {
										try {
											br = new BufferedReader(new FileReader(currentPath + "/src/data/" + fileNames.get(i)));
										} catch (FileNotFoundException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}	
										try {
											//headers[i] = br.readLine();
											headers.add(br.readLine());
											temp = new ArrayList<CSV>(); 
											while ((line = br.readLine()) != null) {
										        // use comma as separator
												String[] row = line.split(",");
												
												CSV obj = new CSV();
												obj.columns = new float[row.length];
												
												for(int j=0;j<row.length;j++)
													obj.columns[j] = Float.parseFloat(row[j]);
												temp.add(obj);
											}
											rows[0] = new CSV[temp.size()];
											for(int j=0;j<temp.size();j++) {
												rows[0][j] = new CSV();
												rows[0][j] = temp.get(j);							
											}
											tables.add(rows[0]);
											
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}	
									}
					//End -- Table Reading				
									//displayAllTables(tables,fileNames,headers);
									//System.out.println();
									//displayTable(joinOnCondition(tables.get(0), tables.get(1), headers.get(0), headers.get(1),"id", "id",0,0),headers.get(0) + "," + headers.get(1));
									//System.out.println();
									//displayTable(cartesianProduct(tables.get(0), tables.get(1)),headers.get(0) + "," + headers.get(1));
									for(int i=0;i<joinConditions.size();i++) {					
										String condition = joinConditions.get(i);
										String tableColumns[]= condition.split("=");	
										String tempTable[] = tableColumns[0].split("\\.");
										String table1 = tempTable[0]; String column1 = tempTable[1];
										tempTable = tableColumns[1].split("\\.");
										String table2 = tempTable[0]; String column2 = tempTable[1];
										//System.out.println("1  " + table1 + "  " + column1 + ", " + table2 + "  " + column2);
										int no1=-1,no2=-1;
										int index1=-1,index2=-1,brea = 0;
										for(int j=0;j<fileNames.size();j++) {
										//	System.out.println(fileNames.get(j));
											String tempt[]=fileNames.get(j).split(" ");
											for(int k=0;k<tempt.length;k++) {
												if(tempt[k].equals(table1 + ".csv")) {
													no1 = k;
													brea = 1;
													index1 = j;
													break;
												}
											}
											
											if(brea == 1)
												break;
										}
									//	System.out.println();
										brea = 0;
										for(int j=0;j<fileNames.size();j++) {
										//	System.out.println(fileNames.get(j));
											String tempt[]=fileNames.get(j).split(" ");
											for(int k=0;k<tempt.length;k++) {
												if(tempt[k].equals(table2 + ".csv")) {
													no2 = k;
													brea = 1;
													index2 = j;
													break;
												}
											}
											
											if(brea == 1)
												break;
										}
									//	System.out.println(index1 + " " + no1);
									//	System.out.println(index2 + " " + no2);
										CSV tempCSV[] = joinOnCondition(tables.get(index1), tables.get(index2), headers.get(index1), headers.get(index2),
												column1, column2,no1,no2);
										CSV[] csv1 = tables.get(index1);CSV[] csv2 = tables.get(index2);
										String header1 = headers.get(index1); String header2 = headers.get(index2);
										String fileName1 =fileNames.get(index1); String fileName2 =fileNames.get(index2); 
										String tempHeader = headers.get(index1)+ "," + headers.get(index2);
										String tempFileName = fileNames.get(index1) + " " + fileNames.get(index2);
										tables.remove(csv1);tables.remove(csv2); 
										headers.remove(header1); headers.remove(header2);
										fileNames.remove(fileName1);fileNames.remove(fileName2);
										tables.add(0,tempCSV); headers.add(0,tempHeader); fileNames.add(0,tempFileName);
										
									} //joincondition list loop
									
									while(tables.size()>1) {
										CSV tempCSV[] = cartesianProduct(tables.get(0), tables.get(1));
										CSV[] csv1 = tables.get(0);CSV[] csv2 = tables.get(1);
										String header1 = headers.get(0); String header2 = headers.get(1);
										String fileName1 =fileNames.get(0); String fileName2 =fileNames.get(1);
										String tempHeader = headers.get(0)+ "," + headers.get(1);
										String tempFileName = fileNames.get(0) + " " + fileNames.get(1);
										tables.remove(csv1);tables.remove(csv2); 
										headers.remove(header1); headers.remove(header2);
										fileNames.remove(fileName1);fileNames.remove(fileName2);
										tables.add(0,tempCSV); headers.add(0,tempHeader); fileNames.add(0,tempFileName);
									}
									
									System.out.println(fileNames.get(0));								
									displayTable(tables.get(0),headers.get(0));
									CSV tempCSV[] = tables.get(0);
									for(int i=0;i<tempCSV.length;i++) 									
										rowObjectsJoin.add(tempCSV[i]);
									header = headers.get(0);									
									String row[] = header.split(",");
									int noOfBTrees = 0;
									for(int i=0;i<row.length;i++) {
										if(!row[i].equals("id")) 
											noOfBTrees++;
									}
									System.out.println("N = " + noOfBTrees);
									bTreeRootsJoin = new Node[noOfBTrees];									
									int n = rowObjectsJoin.size();
									BTreeIndex BTI = new BTreeIndex();
									int k = 0;
									for(int i=0; i<rowObjectsJoin.get(0).columns.length;i++) {
										if(!row[i].equals("id")) {
											float keys[] = new float[n];
											float values[] = new float[n];
											for(int j=0;j<rowObjectsJoin.size();j++)  //rowObjects.size() instead of 20
											{
												CSV csv = rowObjectsJoin.get(j);
												keys[j] = csv.columns[i];
												values[j] = j;												
											}											
											MergeSort ms = new MergeSort();
											ms.mergeSort(keys,values,"Decr");	
										//	System.out.println(row[i] + " " + keys.length + " " + values.length);
											bTreeRootsJoin[k++] = BTI.createIndexTree(keys,values,30);
										}
									}																		
									simpleOrJoin = true;
								}
			}
			
//run1 command			
			else if(command[0].equals("run1")) {	
				if(simpleOrJoin == false) {
						BTreeIndex BTI = new BTreeIndex();
						Node nodes[] = new Node[bTreeRoots.length];
						for(int i=0;i<bTreeRoots.length;i++) {
							nodes[i] = BTI.getLeftMostLeafNode(bTreeRoots[i]);
						}
					/*	for(int i=0;i<nodes.length;i++) {
							for(int j=0;j<nodes[i].key.length;j++) {
								System.out.print(" k " + nodes[i].key[j] + " v " + nodes[i].addrRecord[j]);
							}
							System.out.println();
						}*/
						HashSet<CSV> hs = new HashSet<CSV>();
						Node currentNode = nodes[0];
						while(true) {
							float threshold=0;
							boolean kReached = false;
							for(int i=0;i<currentNode.key.length;i++) {
								
								float temp = 0;
								CSV obj = null;
								threshold = 0;
								for(int j=1;j<nodes.length;j++) {
									threshold += parameters[j-1]*nodes[j].key[i];
								}						
								for(int j=1;j<nodes.length;j++) {
									ArrayList<Float> row = BTI.lookup(bTreeRoots[0],nodes[j].addrRecord[i]);
								//	System.out.println(nodes[j].addrRecord[i] + "  " +row.size());
									//System.out.println(nodes[j].addrRecord[i] + "  " + row +  "  "  + (int) Math.round(row.get(0)));
									//obj = rowObjects.get(nodes[j].addrRecord[i]);
									obj = rowObjects.get((int) Math.round(row.get(0)));
									temp = 0;
									for(int k=1;k<obj.columns.length;k++)
										temp += parameters[k-1]*obj.columns[k];
										
								//	if(nodes[j].addrRecord[i] ==4978.0)
							//		System.out.println(nodes[j].addrRecord[i] + "  " + temp);
									if(!hs.contains(obj)) {// && temp<=threshold) {
										OUTPUT op = new OUTPUT(temp,obj);		
										output = insert(output,op);
										hs.add(obj);							
									}					
								}
								
								if(countEntries(output, threshold)>=K) {
							//		System.out.println("K reached");
									kReached = true;
									break;
								}
							}
							
							if(kReached == true)
								break;
							if(currentNode.nextLeaf!=null) {
								currentNode=currentNode.nextLeaf;
								for(int j=1;j<nodes.length;j++) {
									nodes[j] = nodes[j].nextLeaf;
								}
							}
							else {
								System.out.println("breakdown");
								break;
							}
						}				
						displayOutput(output,K,header);
						//BTI.displayIndexTree(bTreeRoots[1]);
						//System.out.println(BTI.lookup(root,19));
				}
				else {
					BTreeIndex BTI = new BTreeIndex();
					Node nodes[] = new Node[bTreeRootsJoin.length];
					for(int i=0;i<bTreeRootsJoin.length;i++) 
						nodes[i] = BTI.getLeftMostLeafNode(bTreeRootsJoin[i]);
					
					HashSet<CSV> hs = new HashSet<CSV>();				
					String row[] = header.split(",");
					while(true) {
						float threshold=0;
						boolean kReached = false;
						for(int i=0;i<nodes[0].key.length;i++) {
							
							float temp = 0;
							CSV obj = null;
							threshold = 0;
							for(int j=0;j<nodes.length;j++) {
								threshold += parameters[j]*nodes[j].key[i];
							}						
							for(int j=0;j<nodes.length;j++) {
								//ArrayList<Float> row = BTI.lookup(bTreeRoots[0],nodes[j].addrRecord[i]);
							//	System.out.println(nodes[j].addrRecord[i] + "  " +row.size());
								//System.out.println(nodes[j].addrRecord[i] + "  " + row +  "  "  + (int) Math.round(row.get(0)));
								//obj = rowObjects.get(nodes[j].addrRecord[i]);
								obj = rowObjectsJoin.get((int) Math.round(nodes[j].addrRecord[i]));
								temp = 0;
								int l=0;
								for(int k=1;k<obj.columns.length;k++) {
									if(!row[k].equals("id"))
									temp += parameters[l++]*obj.columns[k];
								}
									
							//	if(nodes[j].addrRecord[i] ==4978.0)
						//		System.out.println(nodes[j].addrRecord[i] + "  " + temp);
								if(!hs.contains(obj)) {// && temp<=threshold) {
									OUTPUT op = new OUTPUT(temp,obj);		
									output = insert(output,op);
									hs.add(obj);							
								}					
							}
							
							if(countEntries(output, threshold)>=K) {
						//		System.out.println("K reached");
								kReached = true;
								break;
							}
						}
						
						if(kReached == true)
							break;
						if(nodes[0].nextLeaf!=null) {
							nodes[0]=nodes[0].nextLeaf;
							for(int j=1;j<nodes.length;j++) {
								nodes[j] = nodes[j].nextLeaf;
							}
						}
						else {
							System.out.println("breakdown");
							break;
						}
					}				
					displayOutput(output,K,header);
					
				}
			}
			else if(command[0].equals("run2")) {
				if(simpleOrJoin == false) {
				int j=-1;
				for(int i=0;i<rowObjects.size();i++)  //20 is no. of rows in csv //rowObjects.size() instead of 20
				{
					CSV obj = rowObjects.get(i);
					float temp=0;
					
					for(j=1;j<obj.columns.length;j++)
						temp += parameters[j-1]*obj.columns[j];
					
					OUTPUT op = new OUTPUT(temp,obj);		
						output = insert(output,op);										
				}
				
				displayOutput(output,K,header);
				}
				else {
					int j=-1;
					for(int i=0;i<rowObjectsJoin.size();i++)  //20 is no. of rows in csv //rowObjects.size() instead of 20
					{
						CSV obj = rowObjectsJoin.get(i);
						float temp=0;
						int k = 0;
						String row[] = header.split(",");
						for(j=1;j<obj.columns.length;j++) {
							if(!row[j].equals("id"))
							temp += parameters[k++]*obj.columns[j];
						}
						
						OUTPUT op = new OUTPUT(temp,obj);		
							output = insert(output,op);										
					}
					
					displayOutput(output,K,header);
				}
			}
		} while(!input.equals("Terminate"));
	}

}
