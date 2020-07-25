package renamer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Renamer {
	private Map<Character, String> transliteRUENG;

	
	public Renamer() {
		setTransliteRUENG();
	}

	@SuppressWarnings("unchecked")
	private void setTransliteRUENG() {
		try(FileInputStream fis = new FileInputStream(new File("transliteRuEngMap.bin"));
				ObjectInputStream ois = new ObjectInputStream(fis);) {
			this.transliteRUENG = (Map<Character, String>) ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	String transliteration(Path path) {
		String s = path.getFileName().toString();
		String extend = "";
		if (!Files.isDirectory(path)) {
			Pattern pattern = Pattern.compile("(.+?)(\\.[^.]*$|$)");
			Matcher matcher = pattern.matcher(s);
			while (matcher.find()) {
				s = matcher.group(1);
				extend = matcher.group(2);
			}
		}
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			Character letter = s.charAt(i);
			if (letter.toString().matches("[\\w()]")) {
				result.append(letter);
				continue;
			}
			if (letter.toString().matches("[\\s-.]")) {
				result.append("_");
				continue;
			}
			result.append(transliteRUENG.get(letter));
		}
		return result.append(extend).toString();
	}

	void findAllFiles(Path path, List<Path> pathList) {
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(path)) {
			for (Path p : dirStream) {
				pathList.add(p);
				if (Files.isDirectory(p)) {
					findAllFiles(p, pathList);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void renameAll(String targetPath) throws IOException {
		Path path = Paths.get(targetPath);
		if (Files.exists(path)) {
			List<Path> pathList = new ArrayList<Path>();
			List<Path> renamedPathList = new ArrayList<Path>();
			findAllFiles(path, pathList);
			for (Path p : pathList) {
				renamedPathList.add(p.resolveSibling(transliteration(p)));
			}
			for (int i = renamedPathList.size() - 1; i >= 0; i--) {
				Files.move(pathList.get(i), renamedPathList.get(i), StandardCopyOption.REPLACE_EXISTING);
			}
			System.out.println("Переименование завершено успешно!");
		} else {
			System.out.println("Path doesn't exist");
		}
	}

	public static void main(String[] args) throws IOException {
		new Renamer().renameAll("C:\\91Н6Ф");

	
		
	}

}
