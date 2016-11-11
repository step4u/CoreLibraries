package com.coretree.socket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.coretree.consts.ErrorMessages;
import com.coretree.crypto.Crypto;
import com.coretree.crypto.CryptoAES;
import com.coretree.event.EndOfCallEventArgs;
import com.coretree.event.IEventHandler;
import com.coretree.exceptions.CryptoException;
import com.coretree.media.WaveFormat;
import com.coretree.media.WaveFormatEncoding;
import com.coretree.models.Options;
import com.coretree.models.RTPInfo;
import com.coretree.models.RTPRecordInfo;
import com.coretree.models.ReceivedRTP;
import com.coretree.sql.DBConnection;
import com.coretree.util.BitConverter;
import com.coretree.util.Finalvars;
import com.coretree.util.Util;

public class RTPRecordServer extends Thread implements IEventHandler<EndOfCallEventArgs>
{
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    // private final Lock w = rwl.writeLock();
	
	private DatagramSocket serverSocket;
	private List<RTPRecordInfo> recordIngList;
	
	private Options _option;
	private String _delimiter;
	private String _strformat = "%s/%s";
	private String OS = System.getProperty("os.name");
	// private Thread sockThread = null;
	
	private ByteOrder bo = ByteOrder.BIG_ENDIAN;
	private String filetype = "wav";

	public RTPRecordServer() {
		this(ByteOrder.BIG_ENDIAN, "wav");
	}
	
	public RTPRecordServer(ByteOrder byteorder, String filetype) {
		if (OS.contains("Windows")) {
			_delimiter = "\\";
			_strformat = "%s\\%s";
		} else {
			_delimiter = "/";
			_strformat = "%s/%s";
		}
		
		recordIngList = new ArrayList<RTPRecordInfo>();
		_option = new Options();
		_option.saveDirectory = "./";
		
		this.bo = byteorder;
		this.filetype = filetype;

		InitiateSocket();
	}

	public void InitiateSocket() {
		try {
			// InetSocketAddress address = new InetSocketAddress("127.0.0.1", 21010);
			// serverSocket = new DatagramSocket(address);
			serverSocket = new DatagramSocket(21010);
			
			byte[] receiveData = new byte[424];
			// byte[] receiveData = new byte[1024];
			// byte[] sendData = new byte[1024];

			while (true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				byte[] rcvbytes = receivePacket.getData();

				RTPInfo rtpObj = null;
				
				try
				{
					rtpObj = new RTPInfo(rcvbytes, this.bo);
				} catch (ArrayIndexOutOfBoundsException e){
					System.out.println("ArrayIndexOutOfBoundsException :\r\n" + e.getMessage());
					continue;
				}
				
				// System.out.println("CALLID: " + String.valueOf(rtpObj.StartCallSec) + String.valueOf(rtpObj.StartCallUSec) + ", EXT: " + rtpObj.extension + ", rtpObj.peer_number: " + rtpObj.peer_number + ", rtpObj.codec: " + rtpObj.codec + ", rtpObj.isExtension: " + rtpObj.isExtension + ", rtpObj.size: " + rtpObj.size);
				Util.WriteLog("rtpObj.seq: " + rtpObj.seq + ", CALLID: " + String.valueOf(rtpObj.StartCallSec) + String.valueOf(rtpObj.StartCallUSec) + ", EXT: " + rtpObj.extension + ", rtpObj.peer_number: " + rtpObj.peer_number + ", rtpObj.codec: " + rtpObj.codec + ", rtpObj.isExtension: " + rtpObj.isExtension + ", rtpObj.size: " + rtpObj.size, 2);

				int nDataSize = rtpObj.size - 12;

				if (nDataSize != 80 && nDataSize != 160 && nDataSize != 240 && nDataSize != -12) {
					System.out.println("nDataSize: " + nDataSize + ", rtpObj.codec: " + rtpObj.codec);
					continue;
				}

				this.StackRtp2Instance(rtpObj);
			}
		} catch (IOException e) {
			System.err.println("UDP Port 21010 was occupied.");
			Util.WriteLog(String.format(Finalvars.ErrHeader, 1001, e.getMessage()), 1);
		}		
	}

	private void StackRtp2Instance(RTPInfo rtp) {
		RTPRecordInfo ingInstance = null;

		String _callid = String.valueOf(rtp.StartCallSec) + String.valueOf(rtp.StartCallSec);
		
		r.lock();
		try {
			// ingInstance = recordIngList.stream().filter(x -> x.callid.equals(_callid) && x.ext.equals(rtp.extension.trim())).findFirst().get();
			ingInstance = recordIngList.stream().filter(x -> x.ext.equals(rtp.extension.trim())).findFirst().get();
			ingInstance.Add(rtp);
		} catch (NoSuchElementException | NullPointerException e) {
			WaveFormat wavformat =  null;
			
			switch (rtp.codec) {
				case 0:
					// wavformat = WaveFormat.CreateALawFormat(8000, 1);
					wavformat = WaveFormat.CreateMuLawFormat(8000, 1);
					// wavformat = WaveFormat.CreateCustomFormat(WaveFormatEncoding.MuLaw, 8000, 1, 16000, 1, 16);
					break;
				case 8:
					wavformat = WaveFormat.CreateALawFormat(8000, 1);
					// wavformat = WaveFormat.CreateMuLawFormat(8000, 1);
					// wavformat = WaveFormat.CreateCustomFormat(WaveFormatEncoding.ALaw, 8000, 1, 16000, 1, 16);
					// wavformat = WaveFormat.CreateCustomFormat(WaveFormatEncoding.Pcm, 8000, 1, 8000, 1, 8);
					break;
/*				case 4:
					wavformat = WaveFormat.CreateCustomFormat(WaveFormatEncoding.G723, 8000, 1, 8000, 1, 8);
					break;
				case 18:
					wavformat = WaveFormat.CreateCustomFormat(WaveFormatEncoding.G729, 8000, 1, 8000, 1, 8);
					break;*/
				default:
					wavformat = WaveFormat.CreateMuLawFormat(8000, 1);
					break;
			}
			
			LocalDateTime localdatetime = LocalDateTime.now();

			// DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
			// String _header = localdatetime.format(df);
			String _fileName = String.format("%s_%s_%s", _callid, rtp.extension.trim(), rtp.peer_number.trim());
			
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String _datepath = localdatetime.format(df);
			String _path = String.format(_strformat, _option.saveDirectory, _datepath);
			 
			File _dir = new File(_path);
			if (!_dir.exists()) _dir.mkdir();
			 
			ingInstance = new RTPRecordInfo(wavformat, String.format(_strformat, _option.saveDirectory, _datepath), _fileName, this.bo);
			ingInstance.callid = _callid;
			ingInstance.ext = rtp.extension;
			ingInstance.peer = rtp.peer_number;
			ingInstance.StartDate = localdatetime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			ingInstance.StartHms = localdatetime.format(DateTimeFormatter.ofPattern("HHmmss"));
			ingInstance.savepath = String.format(_strformat, _option.saveDirectory, _datepath);
			ingInstance.filename = _fileName;

			ingInstance.Add(rtp);
			ingInstance.EndOfCallEventHandler.addEventHandler(this);
			
			recordIngList.add(ingInstance);
		} finally {
			r.unlock();
		}
	}

	@Override
	public void eventReceived(Object sender, EndOfCallEventArgs e) {
		RTPRecordInfo item = (RTPRecordInfo)sender;
		
		int size = 0;
		try {
			String filepath = item.savepath + _delimiter + item.filename;
			
			if (Files.exists(Paths.get(filepath + ".encrypted"))) {
				File prevfile = new File(filepath + ".encrypted");
				String key = "Mary has one cat";
				byte[] prevfilebuff = CryptoAES.decrypt(key, prevfile);
				
				File inputfile = new File(filepath);
				FileInputStream inputfilestream = new FileInputStream(inputfile);
	            byte[] inputfilebuff = new byte[(int)inputfile.length()];
	            inputfilestream.read(inputfilebuff, 0, inputfilebuff.length);
	            inputfilestream.close();
	            
	            byte[] newfilebuff = new byte[prevfilebuff.length + inputfilebuff.length - 44];
	            
	            System.arraycopy(prevfilebuff, 0, newfilebuff, 0, prevfilebuff.length);
	            System.arraycopy(inputfilebuff, 0, newfilebuff, prevfilebuff.length, inputfilebuff.length - 44);
	            
	            File outputfile = new File(filepath + ".tmp");
	            FileOutputStream outputstream = new FileOutputStream(outputfile);
	            outputstream.write(newfilebuff);
	            
	        	FileChannel ch = outputstream.getChannel();
	        	long position = ch.position();
	        	ch.position(40);
	        	ch.write(ByteBuffer.wrap(BitConverter.GetBytes(newfilebuff.length), 0, 4));
	        	ch.position(position);
	        	outputstream.close();
	        	
	        	if (Files.exists(Paths.get(filepath + ".tmp"))) {
	        		Files.delete(Paths.get(filepath + ".encrypted"));
	        		Files.delete(Paths.get(filepath));
	        	}
	        	
				String k = "Mary has one cat";
		        File inputFile = new File(filepath + ".tmp");
		        String encryptedfn = item.filename + ".encrypted";
		        File encryptedFile = new File(item.savepath + _delimiter + encryptedfn);
		        
		        CryptoAES.encrypt(k, inputFile, encryptedFile);
		        
		        Files.delete(Paths.get(filepath + ".tmp"));
		        size = (int)Files.size(Paths.get(filepath + ".encrypted"));
		        recordIngList.removeIf(x -> x.ext.equals(item.ext) && x.peer.equals(item.peer));
		        
		        // update database
				StringBuilder sb = new StringBuilder();
				sb.append("update trecord_mgt set");
				sb.append(" file_size=?");
				sb.append(" where callid=?");
				sb.append(" and extension_no=?");

				
				Connection con = null;
				
				try {
					con = DBConnection.getConnection();
					PreparedStatement stmt = con.prepareStatement(sb.toString());
					con.setAutoCommit(false);
					
					stmt.setInt(1, size);
					stmt.setString(2, item.callid);
					stmt.setString(3, item.ext);
					
					stmt.executeUpdate();
					stmt.close();
					
					con.commit();
					con.close();
				} catch (SQLException e1) {
					Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_SQL_EXCEPTION, e1.getMessage()), 1);
					
					try {
						con.rollback();
					} catch (SQLException e2) {
						Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_SQL_EXCEPTION, e2.getMessage()), 1);
					}
				} catch (RuntimeException e2) {
					Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_SQL_EXCEPTION, e2.getMessage()), 1);
					
					try {
						con.rollback();
					} catch (SQLException e1) {
						Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_SQL_EXCEPTION, e2.getMessage()), 1);
					}
				} finally {
					System.out.println(String.format("stream end event update db: sql: %s", sb.toString()));
					System.out.println(String.format("stream end event: callid: %s, ext: %s, peer: %s, filename: %s, filesize: %d", item.callid, item.ext, item.peer, item.filename, size));
				}
			} else {
				String k = "Mary has one cat";
		        File inputFile = new File(filepath);
		        String encryptedfn = item.filename + ".encrypted";
		        File encryptedFile = new File(item.savepath + _delimiter + encryptedfn);
		        // File decryptedFile = new File("d:\\document.wav");

		        // Crypto.setAlgorithm("HmacSHA1");
		        // Crypto.setTransformation("HmacSHA1");
		        // Crypto.encrypt(k, inputFile, encryptedFile);
		        
		        CryptoAES.encrypt(k, inputFile, encryptedFile);
		        // CryptoAES.decrypt(key, encryptedFile, decryptedFile);
		        
		        Files.delete(Paths.get(item.savepath + _delimiter + item.filename));
		        size = (int)Files.size(Paths.get(filepath + ".encrypted"));

		        // insert into database
				StringBuilder sb = new StringBuilder();
				sb.append("insert into trecord_mgt (");
				sb.append("rec_seq");
				sb.append(", callid");
				sb.append(", extension_no");
				sb.append(", emp_no");
				sb.append(", tel_no");
				sb.append(", call_typ_cd");
				sb.append(", rec_start_date");
				sb.append(", rec_start_hms");
				sb.append(", rec_end_date");
				sb.append(", rec_end_hms");
				sb.append(", file_name");
				sb.append(", file_size");
				sb.append(") values (");
				sb.append("GEN_ID(SEQ_REC, 1)");
				sb.append(", ?");
				sb.append(", ?");
				sb.append(", (select emp_no from torganization where extension_no=?)");
				sb.append(", ?");
				sb.append(", (select first 1 call_typ_cd from tcall_stat where callid=?)");
				sb.append(", ?");
				sb.append(", ?");
				sb.append(", replace(cast(current_date as varchar(10)), '-', '')");
				sb.append(", substring(replace(cast(current_time as varchar(13)), ':', '') from 1 for 6)");
				sb.append(", ?");
				sb.append(", ?");
				sb.append(")");
				
				Connection con = null;
				
				try {
					con = DBConnection.getConnection();
					PreparedStatement stmt = con.prepareStatement(sb.toString());
					con.setAutoCommit(false);
					
					stmt.setString(1, item.callid);
					stmt.setString(2, item.ext);
					stmt.setString(3, item.ext);
					stmt.setString(4, item.peer);
					stmt.setString(5, item.callid);
					stmt.setString(6, item.StartDate);
					stmt.setString(7, item.StartHms);
					stmt.setString(8, item.filename);
					stmt.setInt(9, size);
					
					stmt.executeUpdate();
					stmt.close();
					
					con.commit();
					con.close();
					
					recordIngList.removeIf(x -> x.ext.equals(item.ext) && x.peer.equals(item.peer));
				} catch (SQLException e1) {
					Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_SQL_EXCEPTION, e1.getMessage()), 1);
					
					try {
						con.rollback();
					} catch (SQLException e2) {
						Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_SQL_EXCEPTION, e2.getMessage()), 1);
					}
				} catch (RuntimeException e2) {
				//} catch (NullPointerException | UnsupportedOperationException e3) {
					// e2.printStackTrace();
					
					Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_SQL_EXCEPTION, e2.getMessage()), 1);
					
					try {
						con.rollback();
					} catch (SQLException e1) {
						// e1.printStackTrace();
						Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_SQL_EXCEPTION, e2.getMessage()), 1);
					}
					// Util.WriteLog(String.format(Finalvars.ErrHeader, 1002, e2.getMessage()), 1);
				} finally {
					System.out.println(String.format("stream end event insert db: sql: %s", sb.toString()));
					System.out.println(String.format("stream end event: callid: %s, ext: %s, peer: %s, filename: %s", item.callid, item.ext, item.peer, item.filename));
				}
			}
		} catch (IOException ex) {
			Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_FILE_DOES_NOT_EXIST, ex.getMessage()), 1);
        } catch (CryptoException ex) {
        	Util.WriteLog(String.format(Finalvars.ErrHeader, ErrorMessages.ERR_CRYPTO_EXCEPTION, ex.getMessage()), 1);
        } catch (Exception ex) {
        	System.err.println(ex.getMessage());
        }
	}
}
