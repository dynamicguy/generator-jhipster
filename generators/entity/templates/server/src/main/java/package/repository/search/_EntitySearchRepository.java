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
package <%=packageName%>.repository.search;

import <%=packageName%>.domain.<%=entityClass%>;
<%_ if (searchEngine === 'elasticsearch') { _%>

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;<% if (databaseType === 'cassandra') { %>

import java.util.UUID;<% } %>

/**
 * Spring Data Elasticsearch repository for the <%=entityClass%> entity.
 */
public interface <%=entityClass%>SearchRepository extends ElasticsearchRepository<<%=entityClass%>, <% if (databaseType === 'sql' || databaseType === 'mongodb') { %>Long<% } %><% if (databaseType === 'cassandra') { %>UUID<% } %>> {
}
<%_ } if (searchEngine === 'solr') { _%>

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * Spring Data SOLR repository for the <%=entityClass%> entity.
 */
public interface <%=entityClass%>SearchRepository extends SolrCrudRepository<<%=entityClass%>, <% if (databaseType === 'sql' || databaseType === 'mongodb') { %>Long<% } %><% if (databaseType === 'cassandra') { %>UUID<% } %>> {

    @Highlight(prefix = "<[[", postfix = "]]>", fields = { <% for (idx in fields) {%>"<%=fields[idx].fieldName%>", <%} %> })
    @Query(value = "_text_:?0")
    SolrResultPage<<%=entityClass%>> findByAllFields(String term, Pageable pageable);

}
<%_ } _%>
