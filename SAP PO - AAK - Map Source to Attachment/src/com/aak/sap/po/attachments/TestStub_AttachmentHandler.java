package com.aak.sap.po.attachments;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import com.aak.sap.po.mapping.utilities.test.DynamicConfiguration;
import com.aak.sap.po.mapping.utilities.test.OutputAttachmentImpl;
import com.sap.aii.mapping.api.DynamicConfigurationKey;

/*
 * This is the main class for used for testing SAP PI/PO java mappings
 */
public class TestStub_AttachmentHandler {

	private static final String FILE_PATH_SOURCE = "../../../../../input/";
	private static final String FILE_PATH_TARGET = "files/output";
	private static final String FILE_NAME_TARGET = "output.xml";
	
	
	public static void main(String[] args) {
		InputStream is = null;
		OutputStream os = null;
		
		try {
			// Set name of file to process
			String fileName = "AAFCSEMM_ESSESESS_Pain.001.001_0128.xml";
			
			// Get paths to source and target files
			String sourceFile = getPath(FILE_PATH_SOURCE + fileName).toAbsolutePath().toString();
			String targetFile = Paths.get(FILE_PATH_TARGET).toAbsolutePath().toString() + "\\"+ FILE_NAME_TARGET;
			System.out.println("Source file: " + sourceFile);
			System.out.println("Target file: " + targetFile);

			// Prepare in- and outputstreams
			is = new FileInputStream(sourceFile);
			os = new FileOutputStream(targetFile);

			// Create instance of SAP PO mapping
			AttachmentHandler mapping = new AttachmentHandler();
			
			// Only relevant if OutputAttachments are required
			OutputAttachmentImpl oAttachments = new OutputAttachmentImpl();
			mapping.setOutputAttachments(oAttachments);
			
			// Only relevant if using Dynamic Configuration
			DynamicConfiguration dc = new DynamicConfiguration();
			dynamicConf_addTestValues(dc);
			mapping.setDynamicConf(dc);

			// Perform Java Mapping
			mapping.processStreams(is, os);

			// Print content of Dynamic Configuration
			printDynamicConf(dc);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignore) {}
			}
			if (os != null) {
				try {
					os.close();					
				} catch (IOException ignore) {}
			}
		}
	}


	private static void dynamicConf_addTestValues(DynamicConfiguration dc) {
		DynamicConfigurationKey fileDirKey 			= DynamicConfigurationKey.create("http://sap.com/xi/XI/System/File", "Directory");
		DynamicConfigurationKey fileNameKey 		= DynamicConfigurationKey.create("http://sap.com/xi/XI/System/File", "FileName");
		DynamicConfigurationKey fileTypeKey 		= DynamicConfigurationKey.create("http://sap.com/xi/XI/System/File", "FileType");
		DynamicConfigurationKey fileSizeKey 		= DynamicConfigurationKey.create("http://sap.com/xi/XI/System/File", "SourceFileSize");
		DynamicConfigurationKey fileTimestampKey 	= DynamicConfigurationKey.create("http://sap.com/xi/XI/System/File", "SourceFileTimestamp");

		dc.put(fileDirKey, "/Reception");
		dc.put(fileNameKey, "ESSESESS_AAFCSEMM_pain.002.001_1003333302_R1.SNL18055D11561445675773240S.par");
		dc.put(fileTypeKey, "bin");
		dc.put(fileSizeKey, "510");
		dc.put(fileTimestampKey, "20190625T065600Z");
	}


	private static void printDynamicConf(DynamicConfiguration dc) {
		DynamicConfigurationKey dcKey = null;
		String value                  = null;
		Iterator<DynamicConfigurationKey> iter = dc.getKeys();
		int i = 0;
		System.out.println("\nContent of Dynamic Configuration after mapping:");
		while(iter.hasNext()) {
			dcKey = iter.next();
			value = dc.get(dcKey);
			System.out.println("#" + i + "  key:   " + dcKey);
			System.out.println("    value: " + value);
			i++;
		}		
	}

	
	private static Path getPath(String file) throws Exception {
		URL fileUrl = TestStub_AttachmentHandler.class.getResource(file);
		
		if (fileUrl == null) {
			// Resource not found
			throw new Exception("*getPath* The requested resource is not found in project. Check file name: " + file);
		}
		
		Path filePath = Paths.get(fileUrl.toURI());
		return filePath;
	}
	
}
