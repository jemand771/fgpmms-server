package tools.external;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TotalLineCounter {

	
	public static int lines = 0;
	
	public static void main(String[] args) throws FileNotFoundException{
		
		File[] thisFolder = new File("src").listFiles();
		
		long start = System.currentTimeMillis();
		processFolder(thisFolder);
		@SuppressWarnings("unused")
		long time = System.currentTimeMillis() - start;
		System.out.println(lines);
	}
	
	public static void processFolder(File[] folders) throws FileNotFoundException{
		
		for(int i = 0; i < folders.length; i++){
			
//			System.out.println(folders[i].getPath());
			
			if(folders[i].isDirectory()){
				processFolder(folders[i].listFiles());
			}
			else {
				File file = folders[i];
				Scanner sc = new Scanner(file);
				while(sc.hasNextLine()){
					sc.nextLine();
					lines++;
				}
				sc.close();
			}
		}
	}
}
