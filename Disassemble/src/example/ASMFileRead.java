package example;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class ASMFileRead {
	
	private File file;
	
	public ASMFileRead(File file) {
		this.file = file;
	}
	
	public void read() {
		try {
			@SuppressWarnings("resource")
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
			byte[] buf = new byte[4096];
			int len = in.read(buf);
			
			for(int i = 0; i < len; i ++) {
			
				if(i % 16 == 0) {
					System.out.print(String.format("%07X : ", i));
				}
				
				System.out.print(String.format("%02X ", buf[i]));
				
				if(i % 16 == 15) {
					System.out.print("\n");
				}
			}
			
			System.out.println("\n**********************************************************\n\n");
			ASMAnalyze analyze = new ASMAnalyze(buf);
			analyze.analyze();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
