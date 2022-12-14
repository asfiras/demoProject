package com.Demo.app;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Hello world SQS App!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("test", "test");

        final AmazonSQS sqs = AmazonSQSClientBuilder.standard()
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://172.17.0.2:4566","us-east-1"))
        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
        .build();
        String queueUrl_app1 = "http://172.17.0.2:4566/000000000000/App1_q";
        String queueUrl_app2 = "http://172.17.0.2:4566/000000000000/App2_q";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter your choise:");
        System.out.println("1: send massage to APP1");
        System.out.println("2: Recieve massages of App2");
        System.out.println("3: Exit");

        int choise = Integer.parseInt(br.readLine());

        while (choise != 3) {

            if (choise == 1) {
                System.out.println("Enter the massage to App1:");
                String massageToApp1 = br.readLine();
                SendMessageRequest send_msg_request = new SendMessageRequest()
                        .withQueueUrl(queueUrl_app1)
                        .withMessageBody(massageToApp1)
                        .withDelaySeconds(5);
                sqs.sendMessage(send_msg_request);
                System.out.println("Enter your choise:");
                choise = Integer.parseInt(br.readLine());

            } else if (choise == 2) {
                // receive messages from the queue
                List<Message> messages = sqs.receiveMessage(queueUrl_app2).getMessages();
                System.out.println("the massages you recieved are:");
                for (Message m : messages) {
                    System.out.println(m.getBody());
                }

                // delete messages from the queue
                for (Message m : messages) {
                    sqs.deleteMessage(queueUrl_app2, m.getReceiptHandle());
                }
                messages.clear();
                System.out.println("Enter your choise:");
                choise = Integer.parseInt(br.readLine());
            } else if (choise == 3) {
                break;
            }
            else{
                System.out.println("you Entered wrong value");
                System.out.println("Enter your choise:"); 
                choise = Integer.parseInt(br.readLine());
            }
        }
    }
}
