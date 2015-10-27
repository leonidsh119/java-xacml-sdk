package com.thalesgroup.authzforce.sdk.core;

import java.net.URI;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.impl.MetadataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.authzforce.api.jaxrs.EndUserDomain;
import com.thalesgroup.authzforce.api.jaxrs.EndUserDomainSet;

public class Net {

	private static final Logger LOGGER = LoggerFactory.getLogger(Net.class);

	private MetadataMap<String, String> customHeaders;
	private String domainId;
	private URI serverEndpoint;
	private WebClient client;
	private EndUserDomainSet targetedDomain;

	public Net(URI serverEndpoint, String domainId, MultivaluedMap<String, String> headers) {
		this.serverEndpoint = serverEndpoint;
		this.domainId = domainId;
		this.customHeaders = new MetadataMap<String, String>(headers);
		this.client = createClient();
		this.targetedDomain = JAXRSClientFactory.fromClient(this.client, EndUserDomainSet.class);
	}
	
	private WebClient createClient() {
		EndUserDomainSet proxy = JAXRSClientFactory.create(this.serverEndpoint, EndUserDomainSet.class);
		WebClient webClient = WebClient.fromClient(WebClient.client(proxy));
		if (null != this.customHeaders && this.customHeaders.size() > 0) {
			webClient.headers(this.customHeaders);
		}		
		// Request/response logging (for debugging).
		if (LOGGER.isDebugEnabled()) {
			final ClientConfiguration clientConf = WebClient.getConfig(webClient);
			clientConf.getInInterceptors().add(new LoggingInInterceptor());
			clientConf.getOutInterceptors().add(new LoggingOutInterceptor());
		}
		
		return webClient;
	}

	/*
	 * Headers customizers
	 */

	public MultivaluedMap<String, String> getCustomHeaders() {
		return customHeaders;
	}

	public EndUserDomain getMyDomain() {
		return targetedDomain.getEndUserDomain(this.domainId);
	}

	@Override
	public String toString() {

		String networkHandler = "[ endpoint => " + this.serverEndpoint + ", domain => " + this.domainId;
		if (client != null) {
			networkHandler += ", headers => [" + client.getHeaders() + "]]";
		} else {
			networkHandler += "]";
		}
		return networkHandler;
	}

}