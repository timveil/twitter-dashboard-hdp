twitter-dashboard-hdp
=====================


install kafka preview

clone cleanup script

enable 2nd adapter

enable port forwarding for 9092 and 2181

update /etc/hosts to point sandbox.hortonworks.com to ip of 2nd adapter

update storm/logback/cluster.xml with


    <appender name="TWEETS" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${storm.log.dir}/tweets.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
            <fileNamePattern>tweets.log.%i</fileNamePattern>
            <minIndex>1</minIndex>
            <maxIndex>9</maxIndex>
        </rollingPolicy>

        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <maxFileSize>2MB</maxFileSize>
        </triggeringPolicy>

        <encoder>
            <pattern>%d %-8r %m%n</pattern>
        </encoder>
    </appender>

    <logger name="dashboard.storm" additivity="false">
        <level value="debug"/>
        <appender-ref ref="TWEETS"/>
    </logger>

    
Create "tweets" topic
    
    bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic tweets
    
    
    