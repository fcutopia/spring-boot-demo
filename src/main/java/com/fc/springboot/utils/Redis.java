package com.fc.springboot.utils;


import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/*import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;*/


public class Redis {

    public static void main(String[] args) {
     /*   JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大连接数
        poolConfig.setMaxTotal(1);
        // 最大空闲数
        poolConfig.setMaxIdle(1);
        // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：
        // Could not get a resource from the pool
        poolConfig.setMaxWaitMillis(1000);
        Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
        nodes.add(new HostAndPort("172.28.203.16", 6379));
        nodes.add(new HostAndPort("172.28.203.16", 6380));
        nodes.add(new HostAndPort("172.28.203.16", 6381));
        nodes.add(new HostAndPort("172.28.203.16", 6389));
        nodes.add(new HostAndPort("172.28.203.16", 6390));
        nodes.add(new HostAndPort("172.28.203.16", 6391));
        JedisCluster cluster = new JedisCluster(nodes, poolConfig);
        String name = cluster.get("name");
        System.out.println(name);
        cluster.set("age", "18");
        System.out.println(cluster.get("age"));
        cluster.close();*/
    }
}
