package com.aak.sap.po.mapping.utilities.test;

import java.util.Base64;

import com.sap.aii.mapping.api.Attachment;

public class AttachmentImpl implements Attachment {
	byte[] content			= null;
	String contentId		= null;
	String contentType		= null; 
	
	
	public Attachment create(byte[] content, String contentId, String contentType) {
		this.content = content;
		this.contentId = contentId;
		this.contentType = contentType;	
		return this;
	}
	
	
	public String getBase64EncodedContent() {
		String base64 = Base64.getEncoder().encodeToString(this.content);
		return base64;
	}
	

	public byte[] getContent() {
		return this.content;
	}

	
	public String getContentId() {
		return this.contentId;
	}

	
	public String getContentType() {
		return this.contentType;
	}

}
