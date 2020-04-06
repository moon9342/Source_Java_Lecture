package javaIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Exam03_ObjectStreamHashMap {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		Map<String,String> map = 
				new HashMap<String,String>();
		
		
		map.put("1", "홍길동");
		map.put("2", "유관순");
		map.put("3", "신사임당");
		map.put("4", "강감찬");
		
		File file = new File("asset/ObjectStream.txt");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(map);
			
			oos.close();
			fos.close();
			
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			Object tmp = ois.readObject();
			Map<String,String> readMap = null;
			if( tmp instanceof Map<?,?> ) {
				readMap = (Map<String,String>)tmp; 
			}
			
			
			System.out.println(readMap.get("3"));
			ois.close();
			fis.close();
					
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}