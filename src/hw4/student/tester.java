package hw4.student;

import java.io.*;
import java.util.*;

public class tester {

		public static void main(String[] args) throws IOException{
			
			GridGraph grid = new GridGraph();
			
			grid.buildGraph("maze.txt");
			
			
			/**
			for(int i=1; i<17; i++) {
				for(int j=1; j<17; j++) {
					System.out.println("from: " + i);
					System.out.println("to: " + j);
					System.out.println(grid.findShortestPath(i, j, true));
					System.out.println("\n");
				}
				System.out.println("----------------------------");
			}
			*/
			
			System.out.println(grid.findShortestPath(1, 1, false));
			System.out.println(grid.findShortestPath(1, 1, true));
			
			
		}
}
