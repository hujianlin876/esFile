package com.esfile.common.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Elasticsearch配置类
 * 配置ES客户端连接、索引设置等
 * 
 * @author esfile
 * @since 1.0.0
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.esfile.repository.elasticsearch")
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    @Value("${spring.data.elasticsearch.username}")
    private String username;

    @Value("${spring.data.elasticsearch.password}")
    private String password;

    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;

    /**
     * 配置Elasticsearch客户端
     */
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        // 解析集群节点地址
        String[] nodes = clusterNodes.split(",");
        HttpHost[] httpHosts = new HttpHost[nodes.length];
        
        for (int i = 0; i < nodes.length; i++) {
            String node = nodes[i].trim();
            String[] hostPort = node.split(":");
            String host = hostPort[0];
            int port = hostPort.length > 1 ? Integer.parseInt(hostPort[1]) : 9200;
            httpHosts[i] = new HttpHost(host, port, "http");
        }

        // 配置认证信息
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, 
            new UsernamePasswordCredentials(username, password));

        // 构建RestClientBuilder
        RestClientBuilder builder = RestClient.builder(httpHosts)
            .setHttpClientConfigCallback((HttpAsyncClientBuilder httpClientBuilder) -> 
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
            .setRequestConfigCallback(requestConfigBuilder -> 
                requestConfigBuilder
                    .setConnectTimeout(5000)
                    .setSocketTimeout(60000)
                    .setConnectionRequestTimeout(0));

        // 构建RestHighLevelClient
        return new RestHighLevelClient(builder);
    }
}
