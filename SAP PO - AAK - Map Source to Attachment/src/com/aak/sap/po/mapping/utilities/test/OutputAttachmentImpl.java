package com.aak.sap.po.mapping.utilities.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.sap.aii.mapping.api.Attachment;
import com.sap.aii.mapping.api.OutputAttachments;

public class OutputAttachmentImpl implements OutputAttachments {
	private Collection<Attachment> attachments = new ArrayList<Attachment>();

	
	public Attachment create(String contentId, byte[] content) {
		AttachmentImpl attachment = new AttachmentImpl();
		attachment.create(content, contentId, "application/xml");
		return attachment;
	}
	
	
	public Attachment create(String contentId, String contentType, byte[] content) {
		AttachmentImpl attachment = new AttachmentImpl();
		attachment.create(content, contentId, contentType);
		return attachment;
	}
	
	
	public void removeAttachment(String contentID) {
		Iterator<Attachment> iter = this.attachments.iterator();
		while (iter.hasNext()) {
			Attachment currentAttachment = iter.next();
			String cId = currentAttachment.getContentId();
			
			if (contentID.equals(cId)) {
				this.attachments.remove(currentAttachment);
				break;
			}
		}
	}
	
	
	public void setAttachment(Attachment attachment) {
		System.out.println("--> Output attachment set with properties: "
				+ "\n    # Content Id: "   + attachment.getContentId()
				+ "\n    # Content Type: " + attachment.getContentType()
				+ "\n    # Content Size: " + attachment.getContent().length);
		this.attachments.add(attachment);
	} 
		
}
