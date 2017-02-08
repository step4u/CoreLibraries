package com.coretree.core;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteOrder;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import com.coretree.event.HaveGotUcMessageEventArgs;
import com.coretree.event.IEventHandler;
import com.coretree.models.GroupWareData;
import com.coretree.models.HappyTalkRequest;
import com.coretree.models.HappyTalkResponse;
import com.coretree.socket.UcServer;
import com.coretree.util.Const4pbx;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class HappyTalkService implements IEventHandler<HaveGotUcMessageEventArgs> {
	UcServer uc = null;
	ByteOrder byteorder = ByteOrder.BIG_ENDIAN;
	String pbxip = "127.0.0.1";
	
	HappyTalkService happytalk = null;
	
	public HappyTalkService() {
		this(null);
	}
	
	public HappyTalkService(String[] args) {
		if (args == null) {
			System.out.println("No parameters. HappyTalkService is starting in BIG_ENDIAN mode.");
			uc = new UcServer(pbxip, byteorder);
			uc.HaveGotUcMessageEventHandler.addEventHandler(this);
			return;
		}

		if (args.length == 0) {
			System.out.println("No parameters. HappyTalkService is starting in BIG_ENDIAN mode.");
			uc = new UcServer(pbxip, byteorder);
			uc.HaveGotUcMessageEventHandler.addEventHandler(this);
		} else {
			for (int i = 0 ; i < args.length ; i++) {
				if (args[i].contains("-")) {
					switch (args[i]) {
						case "-bo":
							try {
								switch (args[i+1].toLowerCase()) {
									case "bigendian":
									case "big":
										System.out.println(String.format("[%s] [%s] HappyTalkService is starting in BIG_ENDIAN mode.", args[i], args[i+1]));
										break;
									case "littleendian":
									case "little":
										System.out.println(String.format("[%s] [%s] HappyTalkService is starting in LITTLE_ENDIAN mode.", args[i], args[i+1]));
										byteorder = ByteOrder.LITTLE_ENDIAN;
										break;
									default:
										System.out.println(String.format("[%s] [%s] is invalid parameter. HappyTalkService is starting in BIG_ENDIAN mode.", args[i], args[i+1]));
										byteorder = ByteOrder.BIG_ENDIAN;
										break;
								}
							} catch (IndexOutOfBoundsException e) {
								System.out.println("Invalid parameter. HappyTalkService is starting in BIG_ENDIAN mode.");
								byteorder = ByteOrder.BIG_ENDIAN;
							}
							break;
						case "-pbxip":
							try {
								pbxip = args[i+1].toString();
							} catch (IndexOutOfBoundsException e) {
								System.out.println("Invalid parameter. HappyTalkService has been terminated.");
								return;
							}
							break;
						case "-h":
							System.out.println("ex. [-bo ex] [-pbxip 127.0.0.1]");
							break;
						default:
							System.out.println(String.format("[%s] is invalid parameter. HappyTalkService is starting in BIG_ENDIAN mode.", args[i]));
							byteorder = ByteOrder.BIG_ENDIAN;
							break;
					}
				}
			}
			uc = new UcServer(pbxip, byteorder);
			uc.HaveGotUcMessageEventHandler.addEventHandler(this);
		}
	}

	public static void main(String[] args) {
		new HappyTalkService(args);
	}
	
	@Override
	public void eventReceived(Object sender, HaveGotUcMessageEventArgs e) {
		GroupWareData data = new GroupWareData(e.getItem(), byteorder);
		
		if (data.getCmd() != Const4pbx.UC_SEND_INPUT_DATA_REQ) {
			// System.err.println("\nWrong <<<---------" + data.toString() + "\n");
			return;
		}

		data.setCmd(Const4pbx.UC_SEND_INPUT_DATA_RES);
		data.setStatus(Const4pbx.UC_STATUS_SUCCESS);
		
		try {
			this.uc.Send(data);
			
			String profile_key = "e21359de916c49970fbfd99ba7c93f138f7d2201";
			// String https_url = String.format("https://dev-alimtalk-api.sweettracker.net/v1/%s/sendMessage",  profile_key);
			String https_url = String.format("https://alimtalk-api.sweettracker.net/v1/%s/sendMessage",  profile_key);
						
			Gson gson = new Gson();
			HappyTalkRequest request = new HappyTalkRequest();
			request.msgid = "CT" + String.valueOf(java.time.ZonedDateTime.now().toInstant().toEpochMilli());
			request.message_type = "at";
			request.profile_key = profile_key;
			request.template_code = "ars002";
			request.receiver_num = "82" + data.getInputData();
			request.message = "카카오 상담톡 실시간 1:1 채팅을 시작합니다. 문의글을 채팅 입력란에 작성하시어 상담원과 바로 채팅을 진행하세요.";
			request.reserved_time = "00000000000000";
			// request.sms_message = "카카오알림톡 실패 시 전송됨";
			// request.sms_title = "코아트리카카오";
			// request.sms_kind = "S";
			// request.sender_num = "025080648";
			request.btn_name = "상담신청";
			request.btn_url = "https://api.happytalk.io/api/kakao/chat_open?yid=%40%ED%95%B4%ED%94%BC%ED%86%A1io&site_id=4000000015&category_id=61504&division_id=61505";
			
			List<HappyTalkRequest> happytalklist = new ArrayList<HappyTalkRequest>();
			happytalklist.add(request);
			
			URL url = new URL(https_url);
			HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-type", "Application/json");
			con.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(gson.toJson(happytalklist));
			wr.flush();
			wr.close();
			
			for(HappyTalkRequest req : happytalklist) {
				System.out.println(req.toString());
			}
			
			System.out.println("json Array: " + gson.toJson(happytalklist));

			// print_https_cert(con);
			print_content(con);
    	} catch (IllegalStateException ex) {
			ex.printStackTrace();
		} catch (JsonSyntaxException ex) {
			ex.printStackTrace();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.err.println("\n<<<---------" + data.toString() + "\n");
	}
	
	private void print_https_cert(HttpsURLConnection con) throws Exception {
	    if(con != null) {
    		System.out.println("Response Code : " + con.getResponseCode());
    		System.out.println("Cipher Suite : " + con.getCipherSuite());
    		System.out.println("\n");

    		Certificate[] certs = con.getServerCertificates();
    		for(Certificate cert : certs){
    			System.out.println("Cert Type : " + cert.getType());
    			System.out.println("Cert Hash Code : " + cert.hashCode());
    			System.out.println("Cert Public Key Algorithm : "
                                    + cert.getPublicKey().getAlgorithm());
    			System.out.println("Cert Public Key Format : "
                                    + cert.getPublicKey().getFormat());
    			System.out.println("\n");
    		}
	    }
	}
	
	@SuppressWarnings({ "unchecked" })
	private void print_content(HttpsURLConnection con) throws Exception {
		if(con != null) {
			System.out.println("****** Content of the URL ********");
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String input;

			while ((input = br.readLine()) != null) {
				Gson gson = new Gson();
				// System.out.println("input: " + input);
				ArrayList<HappyTalkResponse> responselist = gson.fromJson(input, new ArrayList<HappyTalkResponse>().getClass());
				
				for(Object res : responselist) {
					System.out.println(gson.toJson(res));
				}
			}
			br.close();
		}
	}
	
}
