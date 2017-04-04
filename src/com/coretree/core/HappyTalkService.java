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
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
	private UcServer uc = null;
	private ByteOrder byteorder = ByteOrder.BIG_ENDIAN;
	private String pbxip = "127.0.0.1";
	
	// private HappyTalkService happytalk = null;
	private String profile_key = "";
	private String tmp_code = "";

	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();
	private List<HappyTalkRequest> happytalklist = new ArrayList<HappyTalkRequest>();
	
	private final String helpMsg = "ex. [-bo ex] [-pbxip 127.0.0.1] [-pk a1234677888...] [-tc ars123]";
	private final String invalidParameterMsg = "Invalid parameter. HappyTalkService has been terminated.";
	private final String invalidParameterMsg2 = "[%s] [%s] HappyTalkService is starting in BIG_ENDIAN mode.";
	private final String invalidParameterMsg3 = "[%s] is invalid parameter. HappyTalkService is starting in BIG_ENDIAN mode.";
	private final String noParameterMsg = "No parameters. HappyTalkService is starting in BIG_ENDIAN mode.";
	private final String startLittleEndianMsg = "[%s] [%s] HappyTalkService is starting in LITTLE_ENDIAN mode.";
	
	public HappyTalkService() {
		this(null);
	}
	
	public HappyTalkService(String[] args) {
		if (args == null) {
			System.out.println(noParameterMsg);
			uc = new UcServer(pbxip, byteorder);
			uc.HaveGotUcMessageEventHandler.addEventHandler(this);
			return;
		}
		
		if (args.length == 0) {
			if (profile_key.equals("")) {
				System.out.println(helpMsg);
				return;
			}
			
			if (tmp_code.equals("")) {
				System.out.println(helpMsg);
				return;
			}
			
			System.out.println(noParameterMsg);
			uc = new UcServer(pbxip, byteorder);
			uc.HaveGotUcMessageEventHandler.addEventHandler(this);
		} else {
			for (int i = 0 ; i < args.length ; i++) {
				if (args[i].contains("-")) {
					switch (args[i]) {
						case "-bo":
							if (args[i+1].toString().contains("-")) {
								System.out.println(invalidParameterMsg);
								return;
							}
							
							try {
								switch (args[i+1].toLowerCase()) {
									case "bigendian":
									case "big":
										System.out.println(String.format(invalidParameterMsg2, args[i], args[i+1]));
										break;
									case "littleendian":
									case "little":
										System.out.println(String.format(startLittleEndianMsg, args[i], args[i+1]));
										byteorder = ByteOrder.LITTLE_ENDIAN;
										break;
									default:
										System.out.println(String.format(invalidParameterMsg2, args[i], args[i+1]));
										byteorder = ByteOrder.BIG_ENDIAN;
										break;
								}
							} catch (IndexOutOfBoundsException e) {
								System.out.println(invalidParameterMsg);
								byteorder = ByteOrder.BIG_ENDIAN;
							}
							break;
						case "-pbxip":
							if (args[i+1].toString().contains("-")) {
								System.out.println(invalidParameterMsg);
								return;
							}
							
							try {
								pbxip = args[i+1].toString();
							} catch (IndexOutOfBoundsException e) {
								System.out.println(invalidParameterMsg);
								return;
							}
							break;
						case "-pk":
							if (args[i+1].toString().contains("-")) {
								System.out.println(invalidParameterMsg);
								return;
							}
							
							try {
								profile_key = args[i+1].toString();
							} catch (IndexOutOfBoundsException e) {
								System.out.println(invalidParameterMsg);
								return;
							}
							break;
						case "-tc":
							if (args[i+1].toString().contains("-")) {
								System.out.println(invalidParameterMsg);
								return;
							}
							
							try {
								tmp_code = args[i+1].toString();
							} catch (IndexOutOfBoundsException e) {
								System.out.println(invalidParameterMsg);
								return;
							}
							break;
						case "-h":
							System.out.println(helpMsg);
							return;
							// break;
						default:
							System.out.println(String.format(invalidParameterMsg3, args[i]));
							byteorder = ByteOrder.BIG_ENDIAN;
							break;
					}
				}
			}
			
			if (profile_key.equals("")) {
				System.out.println(helpMsg);
				return;
			}
			
			if (tmp_code.equals("")) {
				System.out.println(helpMsg);
				return;
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
			this.stackMessage2Kako(data);
			// this.sendMessage2Kakao();
			
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
	
	@SuppressWarnings("unchecked")
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
					// System.out.println(gson.toJson(res));
					HappyTalkResponse response = gson.fromJson(gson.toJson(res), HappyTalkResponse.class);
					System.out.println(response.toString());
					if (response.result.equals("Y") && response.code.equals("K000")) {
						w.lock();
						try {
							happytalklist.removeIf(x -> x.msgid.equals(response.msgid));
						} finally {
							w.unlock();
						}
					} else {
						HappyTalkRequest request = null;
						try {
							request = happytalklist.stream().filter(x -> x.msgid.equals(response.msgid)).findFirst().get();
							
							if (request.count > 2) {
								w.lock();
								try {
									happytalklist.removeIf(x -> x.msgid.equals(response.msgid));
								} finally {
									w.unlock();
								}
								return;
							}
							
							Thread.sleep(10);
							
							request.msgid = "CT" + String.valueOf(java.time.ZonedDateTime.now().toInstant().toEpochMilli());
							request.count++;
							
//							w.lock();
//							try {
//								happytalklist.add(request);
//							} finally {
//								w.unlock();
//							}
							
							this.sendMessage2Kakao();
						} catch (NoSuchElementException ex) {
							return;
						}
					}
				}
			}
			br.close();
		}
	}
	
	private void stackMessage2Kako(GroupWareData data) throws Exception {
		HappyTalkRequest request = new HappyTalkRequest();
		
		request.msgid = "CT" + String.valueOf(java.time.ZonedDateTime.now().toInstant().toEpochMilli());
		// request.message_type = "at";
		request.profile_key = profile_key;
		request.template_code = tmp_code;
		if (data.getInputData().length() < 1) {
			request.receiver_num = "82" + (data.getCaller().substring(0,1).equals("0") == true ? data.getCaller().substring(1, data.getCaller().length()) : data.getCaller());
		} else {
			request.receiver_num = "82" + (data.getInputData().substring(0,1).equals("0") == true ? data.getInputData().substring(1, data.getInputData().length()) : data.getInputData());
		}
		request.message = "카카오 상담톡 실시간 1:1 채팅을 시작합니다.\n문의글을 채팅 입력란에 작성하시어\n상담원과 바로 채팅을 진행하세요.";
		request.reserved_time = "00000000000000";
		request.sms_message = "카카오알림톡 실패 시 전송됨";
		request.sms_title = "";
		request.sms_kind = "S";
		request.sender_num = "07079973220";
		request.btn_name = "상담신청";
		// request.btn_url = "https://api.happytalk.io/api/kakao/chat_open?yid=%40%ED%95%B4%ED%94%BC%ED%86%A1io&site_id=4000000015&category_id=61504&division_id=61505";
		request.btn_url = "https://api.happytalk.io/api/kakao/chat_open?yid=%40%EC%BD%94%EC%95%84%ED%8A%B8%EB%A6%AC&site_id=3000000064&category_id=64321&division_id=64322";
		
		w.lock();
		try {
			happytalklist.add(request);
		} finally {
			w.unlock();
		}
		
		this.sendMessage2Kakao();
	}
	
	private void sendMessage2Kakao() throws Exception {
		ArrayList<HappyTalkRequest> tmplist = null;
		w.lock();
		try {
			if (happytalklist.size() < 1) return;
			
			tmplist = new ArrayList<HappyTalkRequest>(happytalklist);
		} finally {
			w.unlock();
		}
		

		// String https_url = String.format("https://dev-alimtalk-api.sweettracker.net/v1/%s/sendMessage",  profile_key);
		String https_url = String.format("https://alimtalk-api.sweettracker.net/v1/%s/sendMessage", profile_key);
		
		URL url = new URL(https_url);
		HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "Application/json");
		con.setDoOutput(true);
		
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		Gson gson = new Gson();
		// wr.writeBytes(gson.toJson(tmplist));
		wr.write(gson.toJson(tmplist).getBytes("UTF-8"));
		wr.flush();
		wr.close();
		
		//for(HappyTalkRequest req : happytalklist) {
		// System.out.println(req.toString());
		//}
		
		System.out.println("json Array: " + gson.toJson(happytalklist));

		// print_https_cert(con);
		print_content(con);
	}
	
}
