package example;

import java.io.File;

public class Main {
	
	public static void main(String[] args) {
	
		System.out.println("File read Start.");
		
		String fileName = "a1.out";
		
		File file = new File(fileName);
		
		ASMFileRead reader = new ASMFileRead(file);
		
		reader.read();

	}
}

