package com.coretree.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TestGetHostName {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ByteOrder byteorder = ByteOrder.BIG_ENDIAN;
			
			// InetAddress bar = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
			// InetAddress bar = InetAddress.getByName("localhost");
			InetAddress bar = InetAddress.getByName("127.0.0.1");

			ByteBuffer b = ByteBuffer.allocate(4);
			byte[] temp = bar.getAddress();
			System.out.println("temp bytes size: " + temp.length);
			b.put(temp);
			b.flip();
			
			if (byteorder == ByteOrder.BIG_ENDIAN)
				b.order(ByteOrder.LITTLE_ENDIAN);
			else
				b.order(ByteOrder.BIG_ENDIAN);
			
			//System.out.println("b.getInt(): " + b.getInt());
			int ip = b.getInt();
			System.out.println("ip: " + ip);
		} catch (UnknownHostException | BufferUnderflowException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
