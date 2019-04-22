package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Cunsumer01 {
    //定义队列名称
    private static final String QUEUE = "hello_Rabbitmq";
    public static void main(String[] args) {
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
            try {
                connection = factory.newConnection();
                //创建会话通道channel
                Channel channel = connection.createChannel();
                //声明队列  如果队列在mq中没有，将创建创建

                //实现消费方法
                DefaultConsumer defaultConsumer = new DefaultConsumer(channel){

                    /**
                     *
                     * @param consumerTag  消费者标签，标识消费者
                     * @param envelope  信封 通过envelope可以获取getExchange()
                     * @param properties 消息属性
                     * @param body 消息内容
                     * @throws IOException
                     */
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                        //获取交换机
                        String exchange = envelope.getExchange();
                        //获取消息的id，mq在channel中用来标识的id，用于确认某个id的消息被接收到
                        long deliveryTag = envelope.getDeliveryTag();
                        //获取消息内容
                        String message = new String(body,"utf-8");
                        System.out.println("接受到一条消息："+message);

                    }
                };
                //queue, autoAck, callback
                //queue 队列名称 发送方和消费方必须是同一个队列
                //autoAck 是否自动回复 当消费者接收到消息后告知mq此消息已被接收 true自动回复 false需要编写程序执行回复
                //callback 消费方法 当消费者接收到消息执行的方法
                channel.basicConsume(QUEUE,true,defaultConsumer);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

