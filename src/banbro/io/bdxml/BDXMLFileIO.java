package banbro.io.bdxml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import banbro.io.FileIO;
import banbro.model.bdx.BDX;

public class BDXMLFileIO {

	public static final String EXTENSION = "bdxml";
	public static final FileFilter FILE_FILTER =
			new FileNameExtensionFilter("BDXMLファイル(*." + EXTENSION + ")", EXTENSION);

	public static BDX readBDXML(File file) throws IOException {
		String ex= FileIO.getFileExtension(file).toLowerCase();
		if (ex==null || ex.equals(EXTENSION)==false) {
			throw new IOException(file.getPath() + "\"はBDXMLファイルではありません。");
		}
		BDX bdx = null;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			BDXMLParser bdxParser = new BDXMLParser();
			parser.parse(file, bdxParser);

			bdx = bdxParser.getBdx();
		} catch (ParserConfigurationException | SAXException e) {
			IOException e2 = new IOException(file.getPath() + "\"はBDXMLファイルではないか、互換性のないバージョンです。");
			e2.addSuppressed(e);
			throw e2;
		} catch (IOException e) {
			throw e;
		}
		return bdx;
	}

	public static void writeBDXML(File outputFile, BDX bdx, String software) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(outputFile);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
				BufferedWriter bw = new BufferedWriter(osw);
				) {
			bw.write(new BDXML(bdx, software).toXML());
			bw.flush();
		} catch (IOException e) {
			throw e;
		}
	}

}
