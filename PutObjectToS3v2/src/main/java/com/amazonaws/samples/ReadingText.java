package com.amazonaws.samples;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadingText {

	public static String readFile() {

		
		String filepath = "D:\\Files On Outlook\\OneDrive - Infosys Limited\\Files\\Other Files\\big-size-json.json";
		String text = "";
		// This will reference one line at a time
		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(filepath);

			// wrapping FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				text = text + line + "";
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
