package hw4.student;

import java.util.*;
import java.io.*;

/**
 * Class to represent a Weighted Grid Graph. Methods are 
 * provided to build a graph from a file and to find 
 * shortest paths.
 * 
 */

public class GridGraph implements GridGraphInterface {
	
private static int[][] graph;	
private static int graphSize;
	
	

  /**
   * Default constructor 
   */
  public GridGraph( ) {
	  	graph= new int[0][0];
	  	graphSize=0;
  }
  
  /**
   * Builds a grid graph from a specified file. It is assumed
   * that the input file is formatted correctly.
   * 
   * @param filename
   */
  public void buildGraph( String filename ) throws FileNotFoundException {
	  
	  //open scanner
	  Scanner input = new Scanner(new File(filename));
	  
	  //read the first input value in the file and assign it to the size variable
	  //create the matrix size accordingly and assign 0 to every value
	  int size = Integer.parseInt(input.next());
	  size = (int)Math.pow(size,2);
	  graphSize=size+1;
	  
	  int[][] matrix = new int[graphSize][graphSize];
	  for(int i=0; i<(size); i++) {
		  for(int j=0; j<(size); j++) {
			  matrix[i][j]=0;
		  }
	  }
	  
	  //read every input in the file and assign the weight of each line to the appropriate position 
	  while(input.hasNext()) {
		  int currentVertex = Integer.parseInt(input.next());
		  int nextVertex = Integer.parseInt(input.next());
		  int edgeWeight = Integer.parseInt(input.next());
		  
		  matrix[currentVertex][nextVertex]=edgeWeight;
		  
		  System.out.println();
	  }
	  
	  //save the matrix as the global variable, 'graph' and close the input
	  graph=matrix;
	  displayGraph(matrix);
	  input.close();
  }
  
  
  /**
   * Takes an input of a two dimensional array and displays it to the output
   * 
   * @param matrix is the two dimensional array that represents the maze
   * @return A series of strings, representing the matrix
   */
  public static void displayGraph(int[][] matrix) {
	  //print the adjacency matrix to console
	  for(int i=1; i<graphSize; i++) {
		  for(int j=1; j<graphSize; j++) {
			  System.out.print("\t[" +matrix[i][j] +"]");
		  }
		System.out.print("\n");
	  }
	  
  }
  
 
  

  /**
   * Finds the shortest path between a source vertex and a target vertex
   * using Dijkstra's algorithm. 
   * @param s Source vertex (one based index)
   * @param t Target vertex (one based index) 
   * @param weighted Whether edge weights should be used or not.
   * @return A String encoding the shortest path. Vertices are
   *         separated by whitespace.  
   */
  
  public String findShortestPath(int s, int t, boolean weighted) {
	  //if the source is the target, return the empty string
	  if(s==t) {
		  return "";
	  }
	  
	  //create a two dimensional arrays that keeps track of every vertex. 
	  //each vertex is assigned a space by it's index 
	  //each index contains an array of size 2, holding the predecessor at index 0 and the minimum distance at index 1
	  List<Integer> visited = new ArrayList<>();
	  int[][] vertex = new int[graphSize][2];
	  
	  //initialize each inside array to have a predecessor as 0, and the minimum distance of an arbitrarily large number
	  for(int i=1; i<graphSize; i++) {
		  vertex[i][0]=0;
		  vertex[i][1]=9999;
	  }
	 
	  //the starting vertex is assigned itself as a predecessor as itself and given the minimum distance of 0
	  vertex[s][0]=s;
	  vertex[s][1]=0;
	  
	  //Execute Dijkstra's algorithm 
	  for(int i=1; i<graphSize; i++) {
		  //set current vertex to the vertex prioritized by the pseudo priority queue
		  int currentVertex = priority(vertex, visited);
		 
		  //add the current vertex to the list of visited vertices
		  visited.add(currentVertex);
		  
		  //traverse through each vertex in the array and change the minimum distance appropriately
		  for(int j=1; j<graphSize; j++) {
			  int edgeLength = graph[currentVertex][j];
			  if(edgeLength!=0) {
				  //if weighted is false, then the distance between each vertex is 1, otherwise, the distance is equal to the edge length
				  if(weighted==false) {
					  if(vertex[j][1]> 1 + getDist(vertex[currentVertex])) {
						  vertex[j][1]= 1 + getDist(vertex[currentVertex]);
						  vertex[j][0]=currentVertex;
					  }
				  }else if(weighted==true) {
					  if(vertex[j][1]>edgeLength + getDist(vertex[currentVertex])) {
						  vertex[j][1]=edgeLength + getDist(vertex[currentVertex]);
						  vertex[j][0]=currentVertex;
					  }
				  }			  
			  }
		  }
		  
		  //if the current vertex has the minimum distance value of our arbitrarily large initialization distance, the vertex cannot be reached by our source vertex
		  if(vertex[currentVertex][1]==9999) {
			  return ""; 
		  }
		  
		  //if the current vertex is the target vertex, return the path required to get to it
		  if(currentVertex==t) {
			  return getPath(s, currentVertex, vertex);
		 }
	  }
	  //the code should not reach here
	  return "error";
  }
  
  /**
   * Returns the minimum distance currently stored in the vertex's array
   * @param vertex is the size 2 array containing the predecessor and the minimum distance of a vertex
   */
  public static int getDist(int[] vertex) {
	  return vertex[1];
  }
  /**
   * Returns the predecessor vertex currently stored in the vertex's array
   * @param vertex is the size 2 array containing the predecessor and the minimum distance of a vertex
   */
  public static int getPred(int[] vertex) {
	  return vertex[0];
  }
  
  /**
   * Returns the vertex with the highest priority
   * @param vertices is the two dimensional array, holding the predecessor and minimum distance of each vertex
   * @param visited is the list of all vertices that have already been visited
   */
  public static int priority(int[][] vertices, List<Integer> visited) {
	  //initialize the minimum distance and the corresponding vertex to arbitrary numbers
	  int lowestValue=99999;
	  int lowestVertex=0;
	  //traverse the graph to find the vertex with the lowest minimum distance AND is not in the list of visited vertexes
	  //assign the distance and the index of that array to the apropriate local variable values
	  for(int i=1; i<graphSize; i++) {
		  if(vertices[i][1]<lowestValue && !visited.contains(i)) {
			  if(visited.contains(i)==false) {
				  lowestValue=vertices[i][1];
				  lowestVertex=i;
			  }
		  }  
	  }
	  //return the index of the vertex with the lowest minimum distance
	  return lowestVertex;
  }
  
  /**
   * Returns the minimum distance path from the starting vertex to the target vertex
   * @param origin is the starting vertex
   * @param is the target vertex
   * @param vertices it the list of all vertices and their corresponding predecessors and minimum distances
   */
  public static String getPath(int origin, int vertex, int[][] vertices) {
	  //if the vertex reaches the origin, return that value
	  if(vertex==origin){
		  return "" + vertex;
	  //otherwise, recurse to the predecessor vertex and return vertex from the origin to the target vertex
	  }else if(vertex!=origin) {
		  return getPath(origin, vertices[vertex][0], vertices) +" "+ vertex;
	  }
	  //the code should not get here
	  return"error"; 
  }
}
