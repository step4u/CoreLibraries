package com.coretree.core;

import java.nio.ByteOrder;

import com.coretree.socket.RTPRecordServer;

public class RTPRecordService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RTPRecordServer recordserver = null;
		
		if (args.length == 0) {
			System.out.println("No parameters. RTP Recroder is starting in BIG_ENDIAN mode");
			recordserver = new RTPRecordServer(ByteOrder.BIG_ENDIAN);
		} else {
			for (int i = 0 ; i < args.length ; i++) {
				// System.out.println(args[i]);
				if (args[i].contains("-")) {
					switch (args[i]) {
						case "-bo":
							try {
								switch (args[i+1].toLowerCase()) {
									case "bigendian":
									case "big":
										System.out.println(String.format("[%s] [%s] RTP Recroder is starting in BIG_ENDIAN mode", args[i], args[i+1]));
										recordserver = new RTPRecordServer(ByteOrder.BIG_ENDIAN);
										break;
									case "littleendian":
									case "little":
										System.out.println(String.format("[%s] [%s] RTP Recroder is starting in LITTLE_ENDIAN mode", args[i], args[i+1]));
										recordserver = new RTPRecordServer(ByteOrder.LITTLE_ENDIAN);
										break;
									default:
										System.out.println(String.format("[%s] [%s] is invalid parameter. RTP Recroder is starting in BIG_ENDIAN mode", args[i], args[i+1]));
										recordserver = new RTPRecordServer(ByteOrder.BIG_ENDIAN);
										break;
								}
							} catch (IndexOutOfBoundsException e) {
								System.out.println("Invalid parameter. RTP Recroder is starting in BIG_ENDIAN mode");
								recordserver = new RTPRecordServer(ByteOrder.BIG_ENDIAN);
							}
							break;
						default:
							System.out.println(String.format("[%s] is invalid parameter. RTP Recroder is starting in BIG_ENDIAN mode", args[i]));
							recordserver = new RTPRecordServer(ByteOrder.BIG_ENDIAN);
							break;
					}
				}
			}
		}
	}
}
