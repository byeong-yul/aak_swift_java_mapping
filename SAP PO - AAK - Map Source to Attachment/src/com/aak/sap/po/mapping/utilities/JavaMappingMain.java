package com.aak.sap.po.mapping.utilities;


import java.io.InputStream;
import java.io.OutputStream;

import com.sap.aii.mapping.api.AbstractTransformation;
import com.sap.aii.mapping.api.DynamicConfiguration;
import com.sap.aii.mapping.api.InputAttachments;
import com.sap.aii.mapping.api.InputHeader;
import com.sap.aii.mapping.api.InputParameters;
import com.sap.aii.mapping.api.OutputAttachments;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.api.TransformationInput;
import com.sap.aii.mapping.api.TransformationOutput;


/**
 * Class is used for SAP PI 7.1+ Java mappings.
 * Below are shown some useful examples of various use cases often needed: 
 * 
 * 	Example of reading a mapping import parameter:
 *    	String val = (String) getInputParams().getValue("myInputParam");
 *
 * 	Example of reading an interface constant:
 *	  	String val = getInputHeader().getInterface();
 *    	String val = getInputHeader().getMessageId();
 *
 * 	Example of getting a specific value contained in a Dynamic Configuration: 
 *    	DynamicConfigurationKey myKey = DynamicConfigurationKey.create("http://segmenta.com", "transactionID");
 *	  	String val = getDynamicConf().get(myKey));
 *
 * 	Example of adding an entry to Dynamic Configuration:
 *    	DynamicConfiguration conf = getDynamicConf();
 *	  	DynamicConfigurationKey key = DynamicConfigurationKey.create("urn:segmenta.com", "Name");
 *    	conf.put(key, "Value");
 */
public abstract class JavaMappingMain extends AbstractTransformation {

	/*********************************************************************/
	/********************* Instance variables  ***************************/
	/*********************************************************************/
	protected DynamicConfiguration 	dynamicConf;		//container of dynamic configuration
	protected InputParameters      	inputParams;		//container of mapping import parameters
	protected InputHeader		   	inputHeader;		//container of mapping constants (interface, namespace, msg id, etc.)
	protected InputAttachments		inputAttachments;
	protected OutputAttachments		outputAttachments;
	
	
	/*********************************************************************/
	/********************* Class Enums         ***************************/
	/*********************************************************************/
	protected static enum traceLevel {
		info,
		debug,
		warning,
	}


	/*********************************************************************/
	/********************* Instance Getters and Setters ******************/
	/*********************************************************************/
	public InputAttachments getInputAttachments() {
		return inputAttachments;
	}

	public void setInputAttachments(InputAttachments attachments) {
		inputAttachments = attachments;
	}
	
	public OutputAttachments getOutputAttachments() {
		return outputAttachments;
	}
	
	public void setOutputAttachments(OutputAttachments attachments) {
		outputAttachments = attachments;
	}
	
	public DynamicConfiguration getDynamicConf() {
		return dynamicConf;
	}

	public void setDynamicConf(DynamicConfiguration dynamicConf) {
		this.dynamicConf = dynamicConf;
	}

	public InputParameters getInputParams() {
		return inputParams;
	}

	public void setInputParameters(InputParameters inputParams) {
		this.inputParams = inputParams;
	}

	public InputHeader getInputHeader() {
		return inputHeader;
	}

	public void setInputHeader(InputHeader inputHeader) {
		this.inputHeader = inputHeader;
	}


	/*********************************************************************/
	/********************* Instance Methods    ***************************/
	/*********************************************************************/
	/**
	 * (non-Javadoc)
	 * @see com.sap.aii.mapping.api.AbstractTransformation#transform(com.sap.aii.mapping.api.TransformationInput, com.sap.aii.mapping.api.TransformationOutput)
	 * 
	 * This method is called by PI
	 */
	@Override
	public void transform(TransformationInput in, TransformationOutput out) throws StreamTransformationException {
		writeTrace(traceLevel.info, "Mapping initiated...");

		// Set InputAttachments
		setInputAttachments(in.getInputAttachments());
		
		// Set OutputAttachments
		setOutputAttachments(out.getOutputAttachments());
		
		// Set Dynamic Configuration (so its globally available)
		setDynamicConf(in.getDynamicConfiguration());

		// Set Input Parameters (so its globally available)
		setInputParameters(in.getInputParameters());

		// Set Input Header (so its globally available) 
		setInputHeader(in.getInputHeader());

		// Get streams
		InputStream is  = in.getInputPayload().getInputStream();
		OutputStream os = out.getOutputPayload().getOutputStream();

		// Process streams
		processStreams(is, os);
	}


	/**
	 * This method mimics the old StreamTransformation 'execute' method and 
	 * also serves as the primary method used when testing mapping outside
	 * of PI.
	 * 
	 * @param is		InputStream
	 * @param os		OutputStream
	 * @throws StreamTransformationException
	 * 					Used in case of mapping errors during processing of streams
	 */
	public void processStreams(InputStream is, OutputStream os) throws StreamTransformationException {
		writeTrace(traceLevel.debug, "method processStreams entered...");

		try {
			// Perform mapping from source XML to target XML
			map(is, os);
		} catch (Exception e) {
			throw new StreamTransformationException("*processStreams* Error occured. " + e);
		}
	}
	
	
	/**
	 * Perform mapping from source XML to target XML
	 * 
	 * @param is			source data
	 * @param os			target data
	 * @throws Exception	If any type of exception occurs...
	 */
	abstract protected void map(InputStream is, OutputStream o) throws Exception;
	

	/**
	 * Write to trace.
	 * If mapping is executed in SAP PI environment then SAP PI trace is used. 
	 * Otherwise trace is written to System.Out.
	 * 
	 * @param traceLev		trace level to be written to
	 * @param msg			message to be logged
	 */
	protected void writeTrace(traceLevel traceLev, String msg) {
		final String prefix  = "*** ";
		final String postfix = " ***";

		if (null != getTrace()) {
			switch (traceLev) {
			case info:
				getTrace().addInfo(msg);
				break;
			case debug:
				getTrace().addDebugMessage(msg);
				break;
			case warning:
				getTrace().addWarning(msg);
				break;
			}			
		} else {
			switch (traceLev) {
			case info:
				System.out.println("[INFO] " + prefix + msg + postfix);
				break;
			case debug:
				System.out.println("[DEBUG] " + prefix + msg + postfix);
				break;
			case warning:
				System.out.println("[WARNING] " + prefix + msg + postfix);
				break;
			}
		}
	}
}
