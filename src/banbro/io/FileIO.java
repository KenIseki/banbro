package banbro.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileIO {

	public static String getFileExtension(File file) {
		String name = file.getName();
		return name.substring(name.lastIndexOf(".")+1);
	}
	
	public static boolean findFile(File dir, File file) {
		if (dir.isDirectory()==false) {
			throw new IllegalArgumentException();
		}
		for (File f : dir.listFiles()) {
			if (file.equals(f)) {
				return true;
			}
		}
		return false;
	}

	public static List<File> getSupportedFileList(List<File> files, List<String> exs) {
		return getSupportedFileList(files.toArray(new File[files.size()]), exs);
	}

	public static List<File> getSupportedFileList(File[] files, List<String> exs) {
		List<File> list = new ArrayList<File>();
		for (File file : files) {
			if (file.isFile()) {
				for (String ex : exs) {
					if (isSupportedFile(file, ex)) {
						list.add(file);
						break;
					}
				}
			}
		}
		return list;
	}

	public static boolean isSupportedFile(File file, String ex) {
		if (file==null) {
			return false;
		}
		return file.getName().toLowerCase().endsWith("." + ex.toLowerCase());
	}

}
