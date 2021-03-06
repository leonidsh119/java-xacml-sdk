/**
 * Copyright (C) 2013-2013 Thales Services - ThereSIS - All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.ow2.authzforce.sdk;

import java.util.List;

import org.ow2.authzforce.sdk.core.schema.Responses;
import org.ow2.authzforce.sdk.core.schema.category.ActionCategory;
import org.ow2.authzforce.sdk.core.schema.category.EnvironmentCategory;
import org.ow2.authzforce.sdk.core.schema.category.ResourceCategory;
import org.ow2.authzforce.sdk.core.schema.category.SubjectCategory;
import org.ow2.authzforce.sdk.exceptions.XacmlSdkException;

public interface XacmlSdk {

	/**
	 * This method is used to create the XML request based on the RequestType
	 * 
	 * @return XML Request (String)
	 */
	public abstract String toString();
	
	/**
	 * 
	 * @param subject
	 * @param resources
	 * @param actions
	 * @param environment
	 * @return
	 * @throws XacmlSdkException
	 */
	public abstract Responses getAuthZ(SubjectCategory subject,
			ResourceCategory resources, ActionCategory actions,
			EnvironmentCategory environment) throws XacmlSdkException;
	
	/**
	 * 
	 * @param subject
	 * @param resources
	 * @param actions
	 * @param environment
	 * @return
	 * @throws XacmlSdkException
	 */
	public abstract Responses getAuthZ(List<SubjectCategory> subject,
			ResourceCategory resources, ActionCategory actions,
			EnvironmentCategory environment) throws XacmlSdkException;
	
	public abstract Responses getAuthZ(SubjectCategory subject,
			List<ResourceCategory> resources, ActionCategory actions,
			EnvironmentCategory environment) throws XacmlSdkException;
	
	public abstract Responses getAuthZ(SubjectCategory subject,
			ResourceCategory resources, List<ActionCategory> actions,
			EnvironmentCategory environment) throws XacmlSdkException;
	
	public abstract Responses getAuthZ(SubjectCategory subject,
			ResourceCategory resources, ActionCategory actions,
			List<EnvironmentCategory> environment) throws XacmlSdkException;

}