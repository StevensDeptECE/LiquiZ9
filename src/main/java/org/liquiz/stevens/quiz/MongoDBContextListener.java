/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.liquiz.stevens.quiz;


import javax.annotation.ManagedBean;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.liquiz.stevens.mongodb.converter.LiquiZCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Value;



//@WebListener
public class MongoDBContextListener implements ServletContextListener {

    @Value("${spring.data.mongodb.port}")
    public int port;

    @Value("${spring.data.mongodb.host}")
    public String host;

    @Override
	public void contextDestroyed(ServletContextEvent sce) {
            MongoClient mongo = (MongoClient) sce.getServletContext().getAttribute("MONGO_CLIENT");
            mongo.close();
            System.out.println("MongoClient closed successfully");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
            ServletContext ctx = sce.getServletContext();
            CodecRegistry codecRegistry = CodecRegistries.fromRegistries(CodecRegistries.fromProviders(new LiquiZCodecProvider()), MongoClient.getDefaultCodecRegistry());
            MongoClientOptions options = new MongoClientOptions.Builder().codecRegistry(codecRegistry).build();
            MongoClient mongo = new MongoClient(new ServerAddress("localhost" , 27017), options);
            System.out.println("MongoClient initialized successfully");
            sce.getServletContext().setAttribute("MONGO_CLIENT", mongo);
	}

}