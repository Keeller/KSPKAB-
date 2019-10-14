package com.company;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

    	HashMap<String,HashMap<String,String>> request=new HashMap<>();
    	try {
		ObjectInputStream oi=new ObjectInputStream(System.in);
		request=(HashMap<String, HashMap<String, String>>) oi.readObject();
		oi.close();
		String seq=request.get("GET").get("seq");
		String del=request.get("GET").get("d");
		Sequence s=new Sequence(seq,del);
		PrintStream ps = new PrintStream(System.out, true, "UTF-8");
		ps.println(s.getResult());
		}
		catch (Exception ex){
			ex.printStackTrace();
		}


    }
}
