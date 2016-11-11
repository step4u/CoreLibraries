package com.coretree.core;

import java.nio.ByteOrder;

import com.coretree.socket.RTPRecordServer;

public class RTPRecordService {

	public static void main(String[] args) {
		RTPRecordServer recordserver = null;
		ByteOrder bo = ByteOrder.BIG_ENDIAN;
		String filetype = "wav";
		
		if (args.length == 0) {
			System.out.println("No parameters. RTP Recroder is starting in BIG_ENDIAN mode");
			recordserver = new RTPRecordServer();
		} else {
			for (int i = 0 ; i < args.length ; i++) {
				if (args[i].contains("-")) {
					switch (args[i]) {
						case "-bo":
							try {
								switch (args[i+1].toLowerCase()) {
									case "bigendian":
									case "big":
										System.out.println(String.format("[%s] [%s] RTP Recroder is starting in BIG_ENDIAN mode", args[i], args[i+1]));
										bo = ByteOrder.BIG_ENDIAN;
										break;
									case "littleendian":
									case "little":
										System.out.println(String.format("[%s] [%s] RTP Recroder is starting in LITTLE_ENDIAN mode", args[i], args[i+1]));
										bo = ByteOrder.LITTLE_ENDIAN;
										break;
									default:
										System.out.println(String.format("[%s] [%s] is invalid parameter. RTP Recroder is starting in BIG_ENDIAN mode", args[i], args[i+1]));
										bo = ByteOrder.BIG_ENDIAN;
										break;
								}
							} catch (IndexOutOfBoundsException e) {
								System.out.println("Invalid parameter. RTP Recroder is starting in BIG_ENDIAN mode");
								bo = ByteOrder.BIG_ENDIAN;
							}
							break;
						case "-ft":
							try {
								switch (args[i+1].toLowerCase()) {
									case "wav":
									case "wave":
										filetype = "wav";
										break;
									case "mp3":
										System.out.println(String.format("[%s] [%s] is invalid parameter. This option is not ready yet.", args[i], args[i+1]));
										filetype = "mp3";
										break;
									default:
										System.out.println(String.format("[%s] [%s] is invalid parameter. You can just choose among wav[e], mp3 (Mp3 will be supported soon)", args[i], args[i+1]));
										filetype = "wav";
										break;
								}
							} catch (IndexOutOfBoundsException e) {
								System.out.println("Invalid parameter. The file will be saved as wave.");
								bo = ByteOrder.BIG_ENDIAN;
							}
							break;
						default:
							System.out.println(String.format("[%s] is invalid parameter. RTP Recroder is starting in BIG_ENDIAN mode", args[i]));
							bo = ByteOrder.BIG_ENDIAN;
							filetype = "wav";
							break;
					}
				}
			}
			
			recordserver = new RTPRecordServer(bo, filetype);
		}
	}
}
