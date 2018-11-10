package com.wangdm.tutorial.netty.client;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

@Component
public class NettyClient {

	@Value("${netty.server.host}")
	private String host;
	
	@Value("${netty.server.port}")
	private Integer port;
	
	private static final Logger log = LoggerFactory.getLogger(NettyClient.class);
	
	public NettyClient(){
		log.info("Create Netty Client");
	}
	
	@PostConstruct
	public void initialize() {
		log.info("Init Netty Client, host:{}, port:{}", host, port);
		
		try {
			EventLoopGroup workerGroup = new NioEventLoopGroup();
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(workerGroup);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {	
				@Override
				public void initChannel(SocketChannel ch) throws Exception{
					ch.pipeline().addLast(new ClientHandler());
				}
			});
			
			ChannelFuture channelFuture = bootstrap.connect(host, port);
			channelFuture.sync();
			
			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} 
	
	@PreDestroy
	public void destroy() {
		log.info("Destroy Netty Server, host:{}, port:{}", host, port);
	}
}
