package com.kboutin.core;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kboutin.utils.FileUtils;

public class Test {

	private final static Logger logger = LogManager.getLogger(PicturesManager.class);

	private static Map<byte[], List<Path>> filteredList = new HashMap<>();

	//private static final String FILE_EXT = ".jpg";
	private static final String START_PATH = "/Users/kouikoui/Desktop/BellesPhotos";

	private Test() {

	}

	public static int findShort(String s) {
		return Stream.of(s.split(" ")).mapToInt(w -> w.length()).min().getAsInt();
	}

	private static final class CustomVisitor extends SimpleFileVisitor<Path> {

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

			System.out.println("Visiting " + file.toString());
			if (String.valueOf(file.getFileName()).endsWith(".JPG") || String.valueOf(file.getFileName()).endsWith(".jpg")) {
				try {
					byte[] md5 = FileUtils.calculateMD5(file);
					List<Path> lstPaths = filteredList.containsKey(md5) ? filteredList.get(md5) : new ArrayList<>();
					lstPaths.add(file);
					filteredList.put(md5, lstPaths);
					//filteredList.put(FileUtils.calculateMD5(file), file);
				} catch (NoSuchAlgorithmException e) {
					//logger.log(Level.SEVERE, e.getMessage());
				}
			}
			return FileVisitResult.CONTINUE;
		}
	}

	public static void main(String[] args) throws Exception {

		/*logger.log(Level.INFO, filteredList.toString());
		Files.walkFileTree(Paths.get(START_PATH), new CustomVisitor());
		for (byte[] md5 : filteredList.keySet()) {
			System.out.println("Hash : " + String.valueOf(md5));
			filteredList.get(md5).forEach(p -> System.out.println("\t" + p.toString()));
		}*/

		/*Test t = new Test();
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		list.add("3");

		list.forEach(t::pouet);*/

		/*List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		list.add("3");

		String s = list.stream().reduce("", (a, b) -> a + b );
		System.out.println(s);*/
		//String[] names = new String[] { "Sheldon", "Leonard", "Penny", "Rajesh", "Howard" };
		//int n = 6;  
		//System.out.println(Test.WhoIsNext(names, n));

		System.out.println(Test.findShort("B7ebbik Petit Coeur Je TAime très fort !!"));
	}

	public static <T> int length(Node<T> n) {

		/*while (n.next != null) {
			if (p.test(n.data)) {
				return true;
			}
		}
		return false;*/
		int nbElements = 0;
		for (T t = n.data; n.next != null; n = n.next) {
			nbElements++;
		}
		return nbElements;
	}

	class Node<T> {
		public T data;
		public Node<T> next;

		Node(T data, Node next) {
			this.data = data;
			this.next = next;
		}

		Node(T data) {
			this(data, null);
		}
	}

	public static String WhoIsNext(String[] names, int n)
	{
		int lastDigit = -1;
		String nString = String.valueOf(n);
		if (n > 10) {
			lastDigit = Integer.parseInt(nString.substring(nString.length() - 1));
		} else if (n >=5) {
			lastDigit = n - 5;
		}
		lastDigit = lastDigit != -1 ? Math.max(0, (int) Math.ceil(lastDigit / 2) - 1) : n - 1;
		return names[lastDigit];
	}

	public int count(int i, int j) {
		int count = 0;
		for (int start = i; start <= j; start++) {
			if (!String.valueOf(start).contains("5")) {
				count++;
			}
		}

		return count;
	}

	public void pouet(String s) {
		System.out.println(s);
	}
}
