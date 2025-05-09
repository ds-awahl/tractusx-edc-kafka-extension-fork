/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 * Copyright (c) 2025 Cofinity-X GmbH
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.eclipse.tractusx.edc.extensions.kafka;

import org.eclipse.edc.connector.controlplane.transfer.spi.flow.DataFlowManager;
import org.eclipse.edc.http.spi.EdcHttpClient;
import org.eclipse.edc.iam.oauth2.spi.client.Oauth2Client;
import org.eclipse.edc.runtime.metamodel.annotation.Extension;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.spi.security.Vault;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
import org.eclipse.edc.spi.types.TypeManager;
import org.eclipse.tractusx.edc.extensions.kafka.auth.KafkaOAuthService;
import org.eclipse.tractusx.edc.extensions.kafka.auth.KafkaOAuthServiceImpl;

/**
 * Kafka Broker flow extension
 */
@Extension(value = KafkaBrokerExtension.NAME)
public class KafkaBrokerExtension implements ServiceExtension {

    public static final String NAME = "Kafka stream extension";

    @Inject
    private DataFlowManager dataFlowManager;

    @Inject
    private Vault vault;

    @Inject
    private TypeManager typeManager;

    @Inject
    private EdcHttpClient httpClient;

    @Inject
    private Oauth2Client oauth2Client;

    @Override
    public void initialize(final ServiceExtensionContext context) {
        KafkaOAuthService kafkaOAuthService = new KafkaOAuthServiceImpl(httpClient, typeManager.getMapper());
        dataFlowManager.register(10, new KafkaBrokerDataFlowController(vault, kafkaOAuthService));
    }
}