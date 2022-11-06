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
    // set sqs endpoint from inside container
    public static String queueUrl_app1 = "http://172.17.0.2:4566/000000000000/App1_q";
    public static String queueUrl_app2 = "http://172.17.0.2:4566/000000000000/App2_q";

    public static void main(String[] args) throws IOException {

        AmazonSQS sqs = sqsConfig();

        // create Menu for Enduser
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        userMenu();

        int choise = Integer.parseInt(br.readLine());

        // keep the program running until User choise to exit
        while (choise != 3) {

            if (choise == 1) {
                // send massages to the queue
                System.out.println("Enter the massage to App2:");
                String massageToApp2 = br.readLine();
                sendMassage(sqs, massageToApp2);
                System.out.println("Enter your choise:");
                choise = Integer.parseInt(br.readLine());

            } else if (choise == 2) {
                // receive messages from the queue
                recieveMassage(sqs);
                System.out.println("Enter your choise:");
                choise = Integer.parseInt(br.readLine());
            } else if (choise == 3) {
                break;
            } else {
                // handle wroge entry from User
                System.out.println("you Entered wrong value");
                System.out.println("Enter your choise:");
                choise = Integer.parseInt(br.readLine());
            }
        }

    }

   
    static AmazonSQS sqsConfig() {
        // configure localstack access and sqs connection
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("test", "test");
        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("http://172.17.0.2:4566", "us-east-1"))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
        return sqs;
    }

    static void userMenu() {
        System.out.println("Enter your choise:");
        System.out.println("1: send massage to APP2");
        System.out.println("2: Recieve massages of App1");
        System.out.println("3: Exit");
    }

    static void sendMassage(AmazonSQS sqs, String massageToApp2) {
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl_app2)
                .withMessageBody(massageToApp2)
                .withDelaySeconds(5);
        sqs.sendMessage(send_msg_request);
    }

    static void recieveMassage(AmazonSQS sqs) {
        List<Message> messages = sqs.receiveMessage(queueUrl_app1).getMessages();
        System.out.println("the massages you recieved are:");
        for (Message m : messages) {
            System.out.println(m.getBody());
        }

        // delete messages from the queue
        for (Message m : messages) {
            sqs.deleteMessage(queueUrl_app1, m.getReceiptHandle());
        }
        messages.clear();
    }
    
}
