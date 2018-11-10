package com.wangdm.tutorial.netty.server;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Component
public class NettyServer {
	
	@Value("${netty.server.port}")
	private Integer port;
	
	private static final Logger log = LoggerFactory.getLogger(NettyServer.class);
	
	public NettyServer(){
		log.info("Create Netty Server");
	}
	
	@PostConstruct
	public void initialize() {
		log.info("Init Netty Server, port:" + port);
		
		try {
			EventLoopGroup masterGroup = new NioEventLoopGroup();
			EventLoopGroup workerGroup = new NioEventLoopGroup();
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(masterGroup, workerGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception{
					ch.pipeline().addLast(new ServerHandler());
				}
			});
			bootstrap.option(ChannelOption.SO_BACKLOG, 128);
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			ChannelFuture channelFuture = bootstrap.bind(port);
			channelFuture.sync();
			
			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} 
	
	@PreDestroy
	public void destroy() {
		log.info("Destroy Netty Server, port:" + port);
	}

}
