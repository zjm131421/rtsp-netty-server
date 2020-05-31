package com.zhuyun.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.util.concurrent.ArrayBlockingQueue;

public class RtpHandler extends SimpleChannelInboundHandler<DatagramPacket>
{

	public static ArrayBlockingQueue<byte[]> arrayBlockingQueue = new ArrayBlockingQueue<byte[]>(500000);
	
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception
    {
    	ByteBuf content = msg.content();
    	byte[] dst = new byte[content.readableBytes()];
    	content.readBytes(dst);
    	
    	boolean offer = arrayBlockingQueue.offer(dst);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        super.channelActive(ctx);
        System.out.println("rtp handler active " + ctx.channel().id().asShortText());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        super.channelInactive(ctx);
        System.out.println("rtp handler inactive "  + ctx.channel().id().asShortText());
    }
    
    private static final char Hex_Char_Arr[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    public static String byteArrToHex(byte[] btArr) {
        char strArr[] = new char[btArr.length * 2];
        int i = 0;
        for (byte bt : btArr) {
            strArr[i++] = Hex_Char_Arr[bt>>>4 & 0xf];
            strArr[i++] = Hex_Char_Arr[bt & 0xf];
        }
        return new String(strArr);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        System.out.println("exception caught");
        cause.printStackTrace();
        ctx.channel().close();
    }
}