package com.amazonaws.samples;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadingText {

	public static String readFile() {

		
		String filepath = "A:\\AWS\\Java Program\\text4.txt";
		String text = "";
		// This will reference one line at a time
		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(filepath);

			// wrapping FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				text = line + "";
			}

			// close file.
			bufferedReader.close();
		} 
		catch (FileNotFoundException ex) 
		{
			System.out.println("Unable to open file '" + filepath + "'");
		} 
		catch (IOException ex) 
		{
			System.out.println("Error reading file '" + filepath + "'");
		}
		return text;
	}
}
