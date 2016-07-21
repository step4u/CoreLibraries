package com.coretree.core;

import java.nio.ByteOrder;

import com.coretree.socket.CdrServer;

public class CdrService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ByteOrder byteorder = ByteOrder.BIG_ENDIAN;
		String pbxip = "localhost"; 
		CdrServer cdrserver = null;
		
		
		if (args.length == 0) {
			System.out.println("No parameters. CdrServer is starting in BIG_ENDIAN mode");
			// cdrserver = new CdrServer(ByteOrder.BIG_ENDIAN);
			cdrserver = new CdrServer(ByteOrder.BIG_ENDIAN, "localhost");
		} else {
			for (int i = 0 ; i < args.length ; i++) {
				if (args[i].contains("-")) {
					switch (args[i]) {
						case "-bo":
							try {
								switch (args[i+1].toLowerCase()) {
									case "bigendian":
									case "big":
										System.out.println(String.format("[%s] [%s] CdrServer is starting in BIG_ENDIAN mode", args[i], args[i+1]));
										byteorder = ByteOrder.BIG_ENDIAN;
										break;
									case "littleendian":
									case "little":
										System.out.println(String.format("[%s] [%s] CdrServer is starting in LITTLE_ENDIAN mode", args[i], args[i+1]));
										byteorder = ByteOrder.LITTLE_ENDIAN;
										break;
									default:
										System.out.println(String.format("[%s] [%s] is invalid parameter. CdrServer is starting in BIG_ENDIAN mode", args[i], args[i+1]));
										byteorder = ByteOrder.BIG_ENDIAN;
										break;
								}
							} catch (IndexOutOfBoundsException e) {
								System.out.println("Invalid parameter. CdrServer is starting in BIG_ENDIAN mode");
								byteorder = ByteOrder.BIG_ENDIAN;
							}
							break;
						case "-pbxip":
							pbxip = args[i+1].toString();
							break;
						default:
							System.out.println(String.format("[%s] is invalid parameter. CdrServer is starting in BIG_ENDIAN mode", args[i]));
							cdrserver = new CdrServer(ByteOrder.BIG_ENDIAN);
							break;
					}
				}
			}
			cdrserver = new CdrServer(byteorder, pbxip);
		}
	}
}
