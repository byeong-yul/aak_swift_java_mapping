package com.aak.sap.po.attachments;

import java.util.ArrayList;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.aak.sap.po.mapping.utilities.JavaMappingMain;
import com.aak.sap.po.mapping.utilities.JavaMappingUtils;
import com.sap.aii.mapping.api.Attachment;
import com.sap.aii.mapping.api.DynamicConfigurationKey;
import com.sap.aii.mapping.api.OutputAttachments;

public class AttachmentHandler extends JavaMappingMain {
	/**
	 * (non-Javadoc)
	 * @see com.aak.sap.po.mapping.utilities.JavaMappingMain#map(org.w3c.dom.Document)
	 */
	protected void map(InputStream sourceData, OutputStream targetData) throws Exception {
		super.writeTrace(traceLevel.debug, "method map entered...");

		// Prepare Dynamic Configuration Keys
		DynamicConfigurationKey fileDirKey 			= DynamicConfigurationKey.create("http://sap.com/xi/XI/System/File", "Directory");
		DynamicConfigurationKey fileNameKey 		= DynamicConfigurationKey.create("http://sap.com/xi/XI/System/File", "FileName");
		DynamicConfigurationKey fileTypeKey 		= DynamicConfigurationKey.create("http://sap.com/xi/XI/System/File", "FileType");
		DynamicConfigurationKey fileSizeKey 		= DynamicConfigurationKey.create("http://sap.com/xi/XI/System/File", "SourceFileSize");
		DynamicConfigurationKey fileTimestampKey 	= DynamicConfigurationKey.create("http://sap.com/xi/XI/System/File", "SourceFileTimestamp");
		
		// Build target XML and write to output
		buildTargetXml(targetData, fileDirKey, fileNameKey, fileTypeKey, fileSizeKey, fileTimestampKey);
		
		// Convert input stream to Attachment
		Attachment attachment = createAttachment(sourceData, this.getDynamicConf().get(fileNameKey), "application/octet-stream");

		// Add attachment to output
		addAttachmentToOutput(attachment);
		
		super.writeTrace(traceLevel.debug, "method map finished...");
	}


	private void buildTargetXml(OutputStream targetData,
								DynamicConfigurationKey fileDirKey, 
								DynamicConfigurationKey fileNameKey, 
								DynamicConfigurationKey fileTypeKey,
								DynamicConfigurationKey fileSizeKey,
								DynamicConfigurationKey fileTimestampKey
			) throws Exception {
		try {
			// Build target XML
			XMLOutputFactory xof = XMLOutputFactory.newInstance();
			XMLStreamWriter xmlWriter = xof.createXMLStreamWriter(targetData, "UTF-8");

			xmlWriter.writeStartDocument("UTF-8", "1.0");
			xmlWriter.writeStartElement("ns", "Attachment", "http://aak.com/treasury/swift/common/in1");
			xmlWriter.writeNamespace("ns", "http://aak.com/treasury/swift/common/in1");
			
			xmlWriter.writeStartElement("FileName");
			xmlWriter.writeCharacters(this.getDynamicConf().get(fileNameKey));
			xmlWriter.writeEndElement();
			
			xmlWriter.writeStartElement("Directory");
			xmlWriter.writeCharacters(this.getDynamicConf().get(fileDirKey));
			xmlWriter.writeEndElement();
			
			xmlWriter.writeStartElement("FileType");
			xmlWriter.writeCharacters(this.getDynamicConf().get(fileTypeKey));
			xmlWriter.writeEndElement();
			
			xmlWriter.writeStartElement("FileSize");
			xmlWriter.writeCharacters(this.getDynamicConf().get(fileSizeKey));
			xmlWriter.writeEndElement();
			
			xmlWriter.writeStartElement("FileTimestamp");
			xmlWriter.writeCharacters(this.getDynamicConf().get(fileTimestampKey));
			xmlWriter.writeEndElement();
			
			xmlWriter.writeEndDocument();
		} catch (XMLStreamException e) {
			throw new Exception("*buildTargetXml* Error building target XML and writing to outputstream.\n" + e);
		}
	}
	

	private Attachment createAttachment(InputStream sourceData, String fileName, String contentType) throws Exception {
		try {
			// Get SAP PO Output attachments
			OutputAttachments oAttachments = this.getOutputAttachments();
			
			// Get input bytes
			byte[] attachmentBytes = JavaMappingUtils.getBytesFromInputStream(sourceData);
			
			// Create new attachment
			Attachment newAttachment = oAttachments.create(fileName, contentType, attachmentBytes);

			return newAttachment;
		} catch (IOException e) {
			throw new Exception("*createAttachment* Error creating attachment.\n" + e);
		}
	}
	
	
	private void addAttachmentToOutput(Attachment attachment)  {
		// Get SAP PO Output attachments
		OutputAttachments oAttachments = this.getOutputAttachments();
			
		// Add attachment to output
		ArrayList<Attachment> newAttachments = new ArrayList<Attachment>();
		newAttachments.add(attachment);
		oAttachments.setAttachment(attachment);
	}
}
