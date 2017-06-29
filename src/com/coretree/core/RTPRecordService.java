package com.coretree.core;

import java.nio.ByteOrder;

import com.coretree.socket.RTPRecordServer;

public class RTPRecordService {

	public static void main(String[] args) {
		RTPRecordServer recordserver = null;
		ByteOrder bo = ByteOrder.BIG_ENDIAN;
		String filetype = "wav";
		boolean encryptEnabled = false;
		int [] chkOptions = {0,0,0};
		
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
								
								chkOptions[0] = 1;
							} catch (IndexOutOfBoundsException e) {
								chkOptions[0] = 0;
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
								
								chkOptions[1] = 1;
							} catch (IndexOutOfBoundsException e) {
								chkOptions[1] = 0;
							}
							
							break;
						case "-enc":
							/* Encrypt option yes or no */
							try {
								switch (args[i+1].toLowerCase()) {
									case "n":
									case "no":
										encryptEnabled = false;
										break;
									case "y":
									case "yes":
										encryptEnabled = true;
										break;
									default:
										System.out.println(String.format("[%s] [%s] is invalid parameter. The audio file will save as normal (not encrypted).", args[i], args[i+1]));
										encryptEnabled = false;
										break;
								}
								
								chkOptions[2] = 1;
							} catch (IndexOutOfBoundsException e) {
								chkOptions[2] = 0;
							}
							break;
						default:
							System.out.println(String.format("[%s] is invalid parameter. RTP Recroder will be terminated.", args[i]));
							return;
					}
				}
			}
			
			int chkoptionscount = 0;
			for (int i=0; i<chkOptions.length; i++) {
				if (chkOptions[i] == 0) {
					break;
				}
			}
			recordserver = new RTPRecordServer(bo, filetype);
		}
	}
}
