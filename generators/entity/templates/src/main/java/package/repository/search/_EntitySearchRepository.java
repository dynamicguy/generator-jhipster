package <%=packageName%>.repository.search;

import <%=packageName%>.domain.<%=entityClass%>;

<% if (searchEngine == 'elasticsearch') { %>
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;<% } %>

<% if (searchEngine == 'solr') { %>
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.Query;    
import org.springframework.data.solr.repository.SolrCrudRepository;<% } %>

<% if (databaseType == 'cassandra') { %>
import java.util.UUID;<% } %>

/**
 * Spring Data <% if (searchEngine == 'solr') { %>SOLR<% } else { %>Elasticsearch<% } %> repository for the <%=entityClass%> entity.
 */
public interface <%=entityClass%>SearchRepository extends <% if (searchEngine == 'solr') { %>SolrCrudRepository<% } else { %>ElasticsearchRepository<% }%><<%=entityClass%>, <% if (databaseType=='sql' || databaseType=='mongodb') { %>Long<% } %><% if (databaseType == 'cassandra') { %>UUID<% } %>> {
    
    @Highlight(prefix = "<[[", postfix = "]]>", fields = { "id" })
    @Query(value = "_text_:?0")
    SolrResultPage<<%=entityClass%>> findByAllFields(String term, Pageable pageable);
    
}