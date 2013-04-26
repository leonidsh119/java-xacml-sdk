package com.thalesgroup.authzforce.sdk.xacml.utils;

import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributesType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.RequestType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ResponseType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ResultType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.thalesgroup.authzforce.sdk.XacmlSdk;
import com.thalesgroup.authzforce.sdk.core.schema.Action;
import com.thalesgroup.authzforce.sdk.core.schema.Environment;
import com.thalesgroup.authzforce.sdk.core.schema.Request;
import com.thalesgroup.authzforce.sdk.core.schema.Resource;
import com.thalesgroup.authzforce.sdk.core.schema.Response;
import com.thalesgroup.authzforce.sdk.core.schema.Responses;
import com.thalesgroup.authzforce.sdk.core.schema.Subject;
import com.thalesgroup.authzforce.sdk.core.schema.XACMLAttributeId;
import com.thalesgroup.authzforce.sdk.exceptions.XacmlSdkException;
import com.thalesgroup.authzforce.sdk.exceptions.XacmlSdkExceptionCodes;

/**
 * This Library is about XACML and XML Processing tools to make the developers'
 * life easier.
 * 
 * @author Romain FERRARI, romain.ferrari[AT]thalesgroup.com
 * @version 0.5
 * 
 */
public class XacmlSdkImpl implements XacmlSdk {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(XacmlSdkImpl.class);

	private RequestType request;
	private WebResource webResource;
	private Client client;

	private static List<AttributesType> attributes = new ArrayList<AttributesType>();

	/**
	 * Constructor
	 * 
	 * @param serverEndpoint
	 */
	public XacmlSdkImpl(URI serverEndpoint) {
		this.client = Client.create();
		this.webResource = this.client.resource(serverEndpoint);
	}

	private void clearRequest() {
		this.request = new Request();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thalesgroup.authzforce.sdk.xacml.utils.XacmlSdk#toString()
	 */
	@Override
	public String toString() {
		LOGGER.debug("Create XML (marshalling)");
		java.io.StringWriter sw = new StringWriter();
		try {
			Marshaller marsh = JAXBContext.newInstance(RequestType.class)
					.createMarshaller();
			marsh.marshal(new JAXBElement<RequestType>(new QName(
					"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17"),
					RequestType.class, request), sw);
		} catch (JAXBException e) {
			LOGGER.error("", e);
		}
		LOGGER.debug(new String(sw.getBuffer()).replaceAll("\"", "'"));

		return sw.toString();
	}

	private void forgeResource(Resource resource) throws XacmlSdkException {
		AttributesType attr = new AttributesType();

		if (resource != null) {
			LOGGER.debug("Forging Resource...");
			attr.setCategory(XACMLAttributeId.XACML_3_0_RESOURCE_CATEGORY_RESOURCE
					.value());
			attr.getAttribute().add(resource);

			attributes.add(attr);
		} else {
			throw new XacmlSdkException(
					XacmlSdkExceptionCodes.MISSING_RESOURCE.value());
		}
	}

	private void forgeSubject(Subject subject) throws XacmlSdkException {
		AttributesType attr = new AttributesType();

		if (subject != null) {
			LOGGER.debug("Forging Subject...");
			attr.setCategory(XACMLAttributeId.XACML_1_0_SUBJECT_CATEGORY_SUBJECT
					.value());
			attr.getAttribute().add(subject);

			attributes.add(attr);
		} else {
			throw new XacmlSdkException(
					XacmlSdkExceptionCodes.MISSING_SUBJECT.value());
		}
	}

	private void forgeAction(Action action) throws XacmlSdkException {
		AttributesType attr = new AttributesType();

		if (action != null) {
			LOGGER.debug("Forging Environment...");
			attr.setCategory(XACMLAttributeId.XACML_3_0_ACTION_CATEGORY_ACTION
					.value());
			attr.getAttribute().add(action);

			attributes.add(attr);
		} else {
			throw new XacmlSdkException(
					XacmlSdkExceptionCodes.MISSING_ACTION.value());
		}
	}

	private void forgeEnvironment(Environment environment)
			throws XacmlSdkException {
		AttributesType attr = new AttributesType();

		if (environment != null) {
			LOGGER.debug("Forging Environment...");
			attr.setCategory(XACMLAttributeId.XACML_3_0_ENVIRONMENT_CATEGORY_ENVIRONMENT
					.value());
			attr.getAttribute().add(environment);

			attributes.add(attr);
		} else {
			throw new XacmlSdkException(
					XacmlSdkExceptionCodes.MISSING_ENVIRONMENT.value());
		}
	}

	private RequestType createXacmlRequest(Subject subject,
			List<Resource> resources, List<Action> actions,
			Environment environment) {
		RequestType xacmlRequest = new RequestType();

		LOGGER.debug("Assembling XACML...");
		try {
			forgeSubject(subject);
			forgeEnvironment(environment);
			for (Action action : actions) {
				forgeAction(action);
			}
			for (Resource resource : resources) {
				forgeResource(resource);
			}
		} catch (XacmlSdkException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		xacmlRequest.getAttributes().addAll(attributes);
		xacmlRequest.setCombinedDecision(false);
		xacmlRequest.setReturnPolicyIdList(false);

		this.request = xacmlRequest;
		StringWriter writer = new StringWriter();
		try {
			JAXBContext jc = JAXBContext
					.newInstance("oasis.names.tc.xacml._3_0.core.schema.wd_17");
			Marshaller u = jc.createMarshaller();
			u.marshal(xacmlRequest, writer);
		} catch (Exception e) {
			System.out.println(e);
		}

		return request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thalesgroup.authzforce.sdk.xacml.utils.XacmlSdk#getAuthZ(com.thalesgroup
	 * .authzforce.sdk.core.schema.Subject, java.util.List, java.util.List,
	 * com.thalesgroup.authzforce.sdk.core.schema.Environment)
	 */
	@Override
	public Responses getAuthZ(Subject subject, List<Resource> resources,
			List<Action> actions, Environment environment)
			throws XacmlSdkException {
		Responses responses = new Responses();
		/*
		 * FIXME: Loop to handle some kind of xacml v3.0 emulation
		 */
		RequestType myRequest = createXacmlRequest(subject, resources, actions,
				environment);
		ResponseType myResponse = webResource.accept(MediaType.APPLICATION_XML)
				.type(MediaType.APPLICATION_XML)
				.post(ResponseType.class, myRequest);

		for (ResultType result : myResponse.getResult()) {
			Response response = new Response();
			for (AttributesType returnedAttr : result.getAttributes()) {
				for (AttributeType attr : returnedAttr.getAttribute()) {
					if (attr.getAttributeId().equals(XACMLAttributeId.XACML_RESOURCE_RESOURCE_ID.value())) {
						for (AttributeValueType attrValue : attr
								.getAttributeValue()) {
							response.setResourceId(String.valueOf(attrValue.getContent()));
						}
					} else if (attr.getAttributeId().equals(XACMLAttributeId.XACML_ACTION_ACTION_ID.value())) {
						for (AttributeValueType attrValue : attr
								.getAttributeValue()) {
							response.setAction(String.valueOf(attrValue.getContent()));
						}
					} else if (attr.getAttributeId().equals(XACMLAttributeId.XACML_SUBJECT_SUBJECT_ID.value())) {
						for (AttributeValueType attrValue : attr
								.getAttributeValue()) {
							response.setSubject(String.valueOf(attrValue.getContent()));
						}
					}
				}
			}
			response.setDecision(result.getDecision());
			responses.getResponse().add(response);
			this.clearRequest();
		}
		return responses;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thalesgroup.authzforce.sdk.xacml.utils.XacmlSdk#getAuthZ(com.thalesgroup
	 * .authzforce.sdk.core.schema.Subject,
	 * com.thalesgroup.authzforce.sdk.core.schema.Resource,
	 * com.thalesgroup.authzforce.sdk.core.schema.Action,
	 * com.thalesgroup.authzforce.sdk.core.schema.Environment)
	 */
	@Override
	public Response getAuthZ(Subject subject, Resource resource, Action action,
			Environment environment) throws XacmlSdkException {
		List<Resource> tmpResourceList = new ArrayList<Resource>();
		List<Action> tmpActionList = new ArrayList<Action>();
		tmpResourceList.add(resource);
		tmpActionList.add(action);
		return getAuthZ(subject, tmpResourceList, tmpActionList, environment)
				.getResponse().get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thalesgroup.authzforce.sdk.xacml.utils.XacmlSdk#getAuthZ(com.thalesgroup
	 * .authzforce.sdk.core.schema.Subject, java.util.List,
	 * com.thalesgroup.authzforce.sdk.core.schema.Action,
	 * com.thalesgroup.authzforce.sdk.core.schema.Environment)
	 */
	@Override
	public Responses getAuthZ(Subject subject, List<Resource> resource,
			Action action, Environment environment) throws XacmlSdkException {
		List<Action> tmpActionList = new ArrayList<Action>();
		tmpActionList.add(action);
		return getAuthZ(subject, resource, tmpActionList, environment);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thalesgroup.authzforce.sdk.xacml.utils.XacmlSdk#getAuthZ(com.thalesgroup
	 * .authzforce.sdk.core.schema.Subject,
	 * com.thalesgroup.authzforce.sdk.core.schema.Resource, java.util.List,
	 * com.thalesgroup.authzforce.sdk.core.schema.Environment)
	 */
	@Override
	public Responses getAuthZ(Subject subject, Resource resource,
			List<Action> action, Environment environment)
			throws XacmlSdkException {
		List<Resource> tmpResourceList = new ArrayList<Resource>();
		tmpResourceList.add(resource);

		return getAuthZ(subject, tmpResourceList, action, environment);
	}

}