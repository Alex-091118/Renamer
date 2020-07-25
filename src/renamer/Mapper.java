package renamer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Mapper {

	public static void main(String[] args) throws IOException {
		Map<Character, String> transliteRUENG = new HashMap<Character, String>();
		for (int i = 1040; i < 1104; i++) {
			transliteRUENG.put(Character.valueOf((char) i), "");
					
	}
		transliteRUENG.put(Character.valueOf((char) 1025), "");
		transliteRUENG.put(Character.valueOf((char) 1105), "");
		
		Scanner scanner = new Scanner(System.in);
		for (Character c : transliteRUENG.keySet()) {
			System.out.println("¬вести соответствие дл€ буквы " + c);
			String st = scanner.nextLine();
			if ("0".equals(st)) break;
			transliteRUENG.put(c, st);
		}
		
		FileOutputStream fos = new FileOutputStream(new File("transliteRuEngMap.bin"));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(transliteRUENG);
		System.out.println(transliteRUENG);
		

	}

}
