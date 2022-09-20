package com.fc.springboot.config;

import com.google.inject.Singleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;*/

import java.util.HashSet;
import java.util.Set;

/**
 * redid集群
 */

@Configuration
//@ConditionalOnClass({JedisCluster.class})
@EnableConfigurationProperties(RedisProperties.class)
public class JedisClusterConfig {

  /*  @Bean
    @Singleton
    public JedisCluster getJedisCluster() {
        String clusterNodes = "192.168.198.138:16080,192.168.198.138:16081,192.168.198.138:16082";
        String[] serverArray = clusterNodes.split(",");
        Set<HostAndPort> nodes = new HashSet<>();
        for (String ipPort: serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(),Integer.valueOf(ipPortPair[1].trim())));
        }
        return new JedisCluster(nodes, 4000);
    }*/
}
