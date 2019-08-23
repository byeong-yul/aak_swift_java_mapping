package com.aak.sap.po.mapping.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class JavaMappingUtils {
	
	public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		
	    while ((len = is.read(buffer)) != -1) {
	        baos.write(buffer, 0, len);
	    }
		
	    return baos.toByteArray();
	}
	
}
