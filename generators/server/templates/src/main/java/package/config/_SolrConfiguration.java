package <%=packageName%>.config;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@AutoConfigureAfter(value = { JacksonConfiguration.class })
@EnableSolrRepositories(basePackages = "<%=packageName%>.repository.search", multicoreSupport = true, schemaCreationSupport = true)
public class SolrConfiguration implements EnvironmentAware {

    private static final Logger log = LoggerFactory.getLogger(SolrConfiguration.class);

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
    public HttpSolrClient solrClient() {
        String solrHost = this.propertyResolver.getProperty(SOLR_HOST, "http://localhost:8983/solr");
        String zkHost = this.propertyResolver.getProperty(ZK_HOST, "localhost:9983");
        String alternateHost = this.propertyResolver.getProperty(SOLR_ALTERNATE_HOST, "http://localhost:8983/solr");
        log.debug("starting solr with params: {}, {} and {}", solrHost, zkHost, alternateHost);
        
        HttpSolrClient httpSolrClient = new HttpSolrClient(solrHost);
        httpSolrClient.setAllowCompression(true);
        
        httpSolrClient.setDefaultMaxConnectionsPerHost(10);
        httpSolrClient.setFollowRedirects(true);
        httpSolrClient.setUseMultiPartPost(true);
        return httpSolrClient;
    }

    @Bean
    public SolrOperations solrTemplate() {
        return new SolrTemplate(solrClient());
    }

}
