package org.nbone.demo.mq;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author thinking
 * @version 1.0
 * @since 2019/7/4
 */
public class RabbitMQTest {

    private static String userName = "user_account";
    private static String password = "user_account";
    private static String virtualHost = "account";
    private static String hostName = "10.10.40.120";
    private static int portNumber = 6672;

    private static String hostNameTest = "10.10.88.17";


    @Test
    public void testProducer() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(userName);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        factory.setHost(hostNameTest);
        factory.setPort(portNumber);
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();

        // 声明交换机 (交换机名, 交换机类型, 是否持久化, 是否自动删除, 是否是内部交换机, 交换机属性);
        channel.exchangeDeclare("user_account", BuiltinExchangeType.FANOUT, true, false, false, null);

        // 设置消息属性 发布消息 (交换机名, Routing key, 可靠消息相关属性 后续会介绍, 消息属性, 消息体);
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .build();
        String json = "{\"action\":\"updateinfo\",\"uid\":90000004,\"data\":{\"upic\":\"http://img.com/user/hpic/201708024710466d2a_180x180.jpg\"}}";
        channel.basicPublish("user_account", "user_account", false, basicProperties, json.getBytes());

        System.out.println("----end");

    }

    @Test
    public void testProducerFor() throws IOException, TimeoutException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            testProducer();
            TimeUnit.SECONDS.sleep(2);
        }
    }

    @Test
    public void testConsume() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(userName);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        factory.setHost(hostNameTest);
        factory.setPort(portNumber);
        Connection conn = factory.newConnection();


        //Connection connection = factory.newConnection("log队列的消费者");
        //给channel起个编号
        Channel channel = conn.createChannel(10);

        // 声明队列 (队列名, 是否持久化, 是否排他, 是否自动删除, 队列属性);
        AMQP.Queue.DeclareOk queueDeclareOk = channel.queueDeclare("camera_material_user_account", true, false, false, new HashMap<>());
        // 声明交换机 (交换机名, 交换机类型, 是否持久化, 是否自动删除, 是否是内部交换机, 交换机属性);
        AMQP.Exchange.DeclareOk exchangeDeclare = channel.exchangeDeclare("user_account", BuiltinExchangeType.FANOUT, true, false, false, new HashMap<>());
        // 将队列Binding到交换机上 (队列名, 交换机名, Routing key, 绑定属性);
        channel.queueBind(queueDeclareOk.getQueue(), "user_account", "user_account");

        //返回consumerTag，也可以通过重载方法进行设置consumerTag
        String consumerTag = channel.basicConsume(queueDeclareOk.getQueue(), false, new SimpleConsumer(channel));
        System.out.println(consumerTag);
        System.out.println("------------------");

        try {
            TimeUnit.HOURS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        channel.close();
        conn.close();
    }

    @Test
    public void testRabbitTemplate() throws IOException {
       /* org.springframework.amqp.rabbit.connection.Connection conn =  rabbitTemplate.getConnectionFactory().createConnection();
        Channel channel = conn.createChannel(true);*/

    }

}
