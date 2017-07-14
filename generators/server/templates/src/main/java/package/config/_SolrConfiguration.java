<%#
 Copyright 2013-2017 the original author or authors from the JHipster project.

 This file is part of the JHipster project, see https://jhipster.github.io/
 for more information.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-%>
package <%=packageName%>.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

import java.net.MalformedURLException;

@Configuration
@EnableSolrRepositories(basePackages = "com.skillshill.app.repository.search", multicoreSupport = true, schemaCreationSupport = true)
public class SolrConfiguration implements EnvironmentAware {

    private final Logger log = LoggerFactory.getLogger(SolrConfiguration.class);

    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, ENV_SOLR);
    }

    private static final String ENV_SOLR = "spring.data.solr.";
    private static final String SOLR_HOST = "host";
    private static final String SOLR_ALTERNATE_HOST = "alternate-host";
    private static final String ZK_HOST = "zk-host";

    @Bean
    public SolrClient solrClient() throws MalformedURLException {
        String solrHost = this.propertyResolver.getProperty(SOLR_HOST, "http://localhost:8983/solr");
        String zkHost = this.propertyResolver.getProperty(ZK_HOST, "localhost:9983");
        String alternateHost = this.propertyResolver.getProperty(SOLR_ALTERNATE_HOST, "http://127.0.0.1:8983/solr");
        log.debug("starting solr with params: {}, {} and {}", solrHost, zkHost, alternateHost);

        HttpSolrClient httpSolrClient = new HttpSolrClient(solrHost);
        HttpClientUtil.setMaxConnections(httpSolrClient.getHttpClient(), 500);
        HttpClientUtil.setMaxConnectionsPerHost(httpSolrClient.getHttpClient(), 100);
        httpSolrClient.setAllowCompression(true);
        httpSolrClient.setParser(new XMLResponseParser());
        httpSolrClient.setFollowRedirects(true);
        httpSolrClient.setUseMultiPartPost(true);
        return httpSolrClient;
    }

    @Bean
    public SolrOperations solrTemplate() throws MalformedURLException {
        return new SolrTemplate(solrClient());
    }

}
