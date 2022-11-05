package com.Demo.app;

import org.junit.Assert;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     * @throws InterruptedException
     */
    @Test
    public void mainTest()
    {
         //configure localstack access
         BasicAWSCredentials awsCreds = new BasicAWSCredentials("test", "test");
         String output="";
         final AmazonSQS sqs = AmazonSQSClientBuilder.standard()
         .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://172.17.0.2:4566","us-east-1"))
         .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
         .build();
        SendMessageRequest send_msg_request = new SendMessageRequest()
        .withQueueUrl("http://172.17.0.2:4566/000000000000/App2_q")
        .withMessageBody("TeSt")
        .withDelaySeconds(5);
        try {
        TimeUnit.SECONDS.sleep(7);
    } catch (InterruptedException e) {

        e.printStackTrace();
    }
        sqs.sendMessage(send_msg_request);
        List<Message> messages = sqs.receiveMessage("http://172.17.0.2:4566/000000000000/App2_q").getMessages();
                System.out.println("the massages you recieved are:");
                for (Message m : messages) {
                    output=m.getBody();
                    sqs.deleteMessage("http://172.17.0.2:4566/000000000000/App2_q", m.getReceiptHandle());
                }
                Assert.assertEquals(output, "TeSt");
    }
}
