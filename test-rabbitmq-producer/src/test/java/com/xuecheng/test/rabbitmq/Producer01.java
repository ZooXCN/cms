package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq的入门程序  生成者 producer
 */
public class Producer01 {

    //定义队列名称
    private static final String QUEUE = "hello_Rabbitmq";

    public static void main(String[] args)  {
        //通过连接工厂创建和mq的连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");//mq的服务器IP地址，本地地址
        factory.setPort(5672);//消费者通信端口
        factory.setUsername("guest");//登录用户名
        factory.setPassword("guest");//登录密码
        //设置虚拟机,一个mq服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
        factory.setVirtualHost("/");
        //建立新的连接
        Connection connection = null;
        Channel channel = null;
        try {
           connection = factory.newConnection();
           //创建会话通道channel
            channel = connection.createChannel();
            //声明队列  如果队列在mq中没有，将创建创建
            //String queue 队列名称,
            // boolean durable 是否持久化,mq重启后队列依然存在
            // boolean exclusive,是否独占连接，队列只允许在该连接中访问，如果连接关闭队列将会自动删除，设置为true可用于临时队列的创建
            // boolean autoDelete,自动删除，队列不使用的时候，将会自动删除
            // Map<String, Object> arguments 设置队列中的扩展参数
            channel.queueDeclare(QUEUE,true,false,true,null);
            //发送消息
            //exchange 交换机，如果不指定将使用mq的默认交换机
            //routingKey 路由key 交换机根据路由key将消息转发给指定的队列中，如果使用默认的交换机，routingKey需要设置为队列的名称
            //props 消息的属性
            //body  消息的内容
            //定义一个消息内容
            String message = "hello world";
            channel.basicPublish("",QUEUE,null,message.getBytes());
            System.out.println("发送了一条消息："+message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
