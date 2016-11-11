package com.coretree.models;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import com.coretree.event.EndOfCallEventArgs;
import com.coretree.event.Event;
import com.coretree.io.WaveFileWriter;
import com.coretree.media.MixingAudioInputStream;
import com.coretree.media.WaveFormat;
import com.coretree.media.WaveFormatEncoding;
import com.coretree.media.mmsc.ConvertInputStream;
import com.coretree.media.mmsc.DecompressInputStream;
import com.coretree.util.Finalvars;
import com.coretree.util.Util;

public class RTPRecordInfo implements Closeable
{
	public Event<EndOfCallEventArgs> EndOfCallEventHandler = new Event<EndOfCallEventArgs>();
	
	private int dataSize = 0;
	private int previousDataSize = 0;
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();
	
	private Timer timer;
	private Timer endtimer;
	private short endcount = 0;
	public String callid;
	public int seq;
	public String ext;
	public String peer;
	public String StartDate;
	public String StartHms;
	public WaveFormat codec;
	public String savepath;
	public String filename;

	private List<RTPInfo> listIn;
	private List<RTPInfo> listOut;
	private WaveFileWriter writer = null;
	private WaveFormat format = new WaveFormat(8000, 16, 1);
	private AudioFormat targetformat = null;
	private boolean isBigendian = true;

	private final int timerInterval = 1000;
	private final int endtimerInterval = 1500;
	
	private String OS = System.getProperty("os.name");
	
	public int getCodec() {
		int out = -1;
		switch (this.codec.waveFormatTag) {
			case ALaw:
				out = 8;
				break;
			case MuLaw:
				out = 0;
				break;
			default:
				out = 8;
				break;
		}
		
		return out;
	}
	public void setCodec(int codec) {
		switch (codec) {
			case 0:
				this.codec = WaveFormat.CreateMuLawFormat(8000, 1);
				break;
			case 8:
				this.codec = WaveFormat.CreateALawFormat(8000, 1);
				break;
			/*case 4:
				this.codec = WaveFormat.CreateCustomFormat(WaveFormatEncoding.G723, 8000, 1, 8000, 1, 8);
				break;
			case 18:
				this.codec = WaveFormat.CreateCustomFormat(WaveFormatEncoding.G729, 8000, 1, 8000, 1, 8);
				break;*/
			default:
				this.codec = WaveFormat.CreateMuLawFormat(8000, 1);
				break;
		}
	}
	
	public RTPRecordInfo(WaveFormat _codec, String savepath, String filename, ByteOrder byteorder) {
		this.savepath = savepath;
		this.filename = filename;
		if (byteorder == ByteOrder.BIG_ENDIAN)
			isBigendian = true;
		else
			isBigendian = false;
		
		// this.format = WaveFormat.CreateCustomFormat(WaveFormatEncoding.Pcm, 8000, 1, 8000*2, 1, 8*2);
		// this.format = WaveFormat.CreateALawFormat(8000, 1);
		this.format = WaveFormat.CreateMuLawFormat(8000, 1);
		this.targetformat = new AudioFormat(AudioFormat.Encoding.ULAW, 8000, 8, 1, 1, 8000 * 1, isBigendian);
		
		codec = _codec;
		listIn = new ArrayList<RTPInfo>();
		listOut = new ArrayList<RTPInfo>();
		
		try {
			String _strformat = "%s/%s";
			if (OS.contains("Windows")) {
				_strformat = "%s\\%s";
			} else {
				_strformat = "%s/%s";
			}
			
			writer = new WaveFileWriter(String.format(_strformat, savepath, filename), format);
		} catch (IOException e) {
			Util.WriteLog(String.format(Finalvars.ErrHeader, 1003, e.getMessage()), 1);
		}

		this.InitTimer();
	}

	private void InitTimer() {
		Timer_Elapsed timer_elapsed = new Timer_Elapsed();
		timer = new Timer();
		timer.schedule(timer_elapsed, timerInterval, timerInterval);

		Endtimer_Elapsed endtimer_Elapsed = new Endtimer_Elapsed(this);
		endtimer = new Timer();
		endtimer.schedule(endtimer_Elapsed, endtimerInterval, endtimerInterval);
	}

	private int chk = 0;
	private int first = -1;
	public void Add(RTPInfo rtp) {
		if (getCodec() != rtp.codec) {
			setCodec(rtp.codec);
		}
		
		if (chk == 0) {
			byte[] rtpbuff = new byte[rtp.size];
			System.arraycopy(rtp.voice, 0, rtpbuff, 0, rtp.size);
			RTPHeader rtpheader = null;
			
			try {
				rtpheader = new RTPHeader(rtpbuff, 0, rtpbuff.length);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Util.WriteLog("EXT: " + rtp.extension + ", CODEC: " + rtp.codec + ", isExt: " + rtp.isExtension + ", SIZE: " + rtp.size + ", realCodec: " + rtpheader.getPacketType(), 1);
			
			chk++;
			first = rtp.isExtension;
		} else if (chk == 1) {
			if (first != rtp.isExtension) {
				byte[] rtpbuff = new byte[rtp.size];
				System.arraycopy(rtp.voice, 0, rtpbuff, 0, rtp.size);
				RTPHeader rtpheader = null;
				
				try {
					rtpheader = new RTPHeader(rtpbuff, 0, rtpbuff.length);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Util.WriteLog("EXT: " + rtp.extension + ", CODEC: " + rtp.codec + ", isExt: " + rtp.isExtension + ", SIZE: " + rtp.size + ", realCodec: " + rtpheader.getPacketType(), 1);
				
				chk++;
			}
		} else {
			
		}
		
        if (rtp.size == 0) endcount++;

        if (endcount > 1) {
            if (timer != null) timer.cancel();

            if (endtimer != null) endtimer.cancel();

            this.MixRtp("final");
            
            try {
				close();
			} catch (IOException e) {
				Util.WriteLog(String.format(Finalvars.ErrHeader, 1004, e.getMessage()), 1);
			} finally {
				Util.WriteLog("endded EXT: " + rtp.extension, 1);
            	EndOfCallEventHandler.raiseEvent(this, new EndOfCallEventArgs(""));
			}

            return;
        }

//        if (endtimer != null)
//        {
//            endtimer.cancel();
//            endtimer.purge();
//            
//            Endtimer_Elapsed endtimer_Elapsed = new Endtimer_Elapsed();
//            endtimer = new Timer();
//            endtimer.schedule(endtimer_Elapsed, endtimerInterval, endtimerInterval);
//        }

        if (rtp.size == 0) return;

        if (rtp.isExtension == 1) {
        	RTPInfo tmpRtp = new RTPInfo();
        	tmpRtp.seq = rtp.seq;
        	tmpRtp.size = rtp.size;
        	tmpRtp.isExtension = rtp.isExtension;
        	tmpRtp.extension = rtp.extension;
        	tmpRtp.peer_number = rtp.peer_number;
        	tmpRtp.voice = rtp.voice;
        	
        	w.lock();
        	try {
            	listIn.add(tmpRtp);
        	} finally {
        		w.unlock();
        	}
        } else {
        	RTPInfo tmpRtp = new RTPInfo();
        	tmpRtp.seq = rtp.seq;
        	tmpRtp.size = rtp.size;
        	tmpRtp.isExtension = rtp.isExtension;
        	tmpRtp.extension = rtp.extension;
        	tmpRtp.peer_number = rtp.peer_number;
        	tmpRtp.voice = rtp.voice;
        	
        	w.lock();
        	try {
            	listOut.add(tmpRtp);        		
        	} finally {
        		w.unlock();
        	}
        }
    }

	private void MixRtp(String check) {
		if (listIn == null || listOut == null) return;

		List<RTPInfo> linin = new ArrayList<RTPInfo>();
		List<RTPInfo> linout = new ArrayList<RTPInfo>();

		r.lock();
		try {
			linin = listIn.stream().collect(Collectors.toList());
		} finally {
			r.unlock();
		}

		r.lock();
		try {
			linout = listOut.stream().collect(Collectors.toList());			
		} finally {
			r.unlock();
		}

		Collections.sort(linin, new Comparator<RTPInfo>() {
			@Override
			public int compare(RTPInfo o1, RTPInfo o2) {
				if (o1.seq > o2.seq)
					return 1;
				else if (o1.seq < o2.seq)
					return -1;
				else
					return 0;
			}
		});

		Collections.sort(linout, new Comparator<RTPInfo>() {
			@Override
			public int compare(RTPInfo o1, RTPInfo o2) {
				if (o1.seq > o2.seq)
					return 1;
				else if (o1.seq < o2.seq)
					return -1;
				else
					return 0;
			}
		});

		RTPInfo itemIn = null;
		RTPInfo itemOut = null;
		
		try {
			itemIn = linin.stream().findFirst().get();
		} catch (NoSuchElementException | NullPointerException e) {
			Util.WriteLog(String.format(Finalvars.ErrHeader, 1005, e.getMessage()), 1);
			return;
		}
		
		try {
			itemOut = linout.stream().findFirst().get();
		} catch (NoSuchElementException | NullPointerException e) {
			Util.WriteLog(String.format(Finalvars.ErrHeader, 1006, e.getMessage()), 1);
			return;
		}
		
		DelayedMsec delayedMsec = DelayedMsec.same;
//		if (itemIn == null || itemOut == null)
//		{
//			return;
//		}
//		else
//		{
			byte[] mixedbytes = null;
			if ((itemIn.size - headersize) == 80 && (itemOut.size - headersize) == 160) {
				delayedMsec = DelayedMsec.i80o160;

				if (check.equals("final")) {
					for (RTPInfo item : linout) {
						mixedbytes = this.Mixing(linin, linout, item, delayedMsec);
						this.WaveFileWriting(mixedbytes);
					}
				} else {
					for (int i = 0; i < linout.size() * 0.8; i++) {
						mixedbytes = this.Mixing(linin, linout, linout.get(i), delayedMsec);
						this.WaveFileWriting(mixedbytes);
					}
				}
			} else if ((itemIn.size - headersize) == 160 && (itemOut.size - headersize) == 80) {
				delayedMsec = DelayedMsec.i160o80;

				if (check.equals("final")) {
					for (RTPInfo item : linin) {
						mixedbytes = this.Mixing(linin, linout, item, delayedMsec);
						this.WaveFileWriting(mixedbytes);
					}
				} else {
					for (int i = 0; i < linin.size() * 0.8; i++) {
						mixedbytes = this.Mixing(linin, linout, linin.get(i), delayedMsec);
						this.WaveFileWriting(mixedbytes);
					}
				}
			} else {
				delayedMsec = DelayedMsec.same;

				if (check.equals("final")) {
					for (RTPInfo item : linin) {
						mixedbytes = this.Mixing(linin, linout, item, delayedMsec);
						this.WaveFileWriting(mixedbytes);
					}
				} else {
					for (int i = 0; i < linin.size() * 0.8; i++) {
						mixedbytes = this.Mixing(linin, linout, linin.get(i), delayedMsec);
						this.WaveFileWriting(mixedbytes);
					}
				}
			}
//		}
	}

	private int headersize = 12;
	private byte[] Mixing(List<RTPInfo> linin, List<RTPInfo> linout, RTPInfo item, DelayedMsec delayedMsec) {
		byte[] mixedbytes = null;
		
		RTPInfo _item0 = null;
		RTPInfo _item1 = null;

		if (delayedMsec == DelayedMsec.i80o160) {
			int seq = item.seq * 2;
			
			r.lock();
			try {
				_item0 = linin.stream().filter(x -> x.seq == seq).findFirst().get();					
			} catch (NoSuchElementException | NullPointerException e) {
				Util.WriteLog(String.format(Finalvars.ErrHeader, 1007, e.getMessage()), 1);
				
				_item0 = new RTPInfo();
				_item0.voice = new byte[332];
				_item0.seq = seq;
				_item0.size = 92;
				_item0.extension = item.extension;
				_item0.peer_number = item.peer_number;
			} finally {
				r.unlock();
			}
			
			r.lock();
			try {
				_item1 = linin.stream().filter(x -> x.seq == (seq + 1)).findFirst().get();					
			} catch (NoSuchElementException | NullPointerException e) {
				Util.WriteLog(String.format(Finalvars.ErrHeader, 1008, e.getMessage()), 1);
				
				_item1 = new RTPInfo();
				_item1.voice = new byte[332];
				_item1.seq = seq + 1;
				_item1.size = 92;
				_item1.extension = item.extension;
				_item1.peer_number = item.peer_number;
			} finally {
				r.unlock();
			}
			
			final RTPInfo __item0 = _item0;
			final RTPInfo __item1 = _item1;

			// item2 + tmpitem mix with item1 and write
			byte[] tmpbuff = new byte[332];
			System.arraycopy(__item0.voice, 0, tmpbuff, 0, __item0.size);
			System.arraycopy(__item1.voice, headersize, tmpbuff, __item0.size, (__item1.size - headersize));

			RTPInfo _itm = new RTPInfo();
			_itm.voice = tmpbuff;
			_itm.size = (_item0.size + _item1.size - headersize);

			try {
				mixedbytes = this.RealMix(_itm, item);
			} catch (IOException e) {
				// e.printStackTrace();
				Util.WriteLog(String.format(Finalvars.ErrHeader, 1009, e.getMessage()), 1);
			}

			w.lock();
			try {
				listIn.removeIf(x -> (x.seq == __item0.seq));
			} finally {
				w.unlock();
			}
			
			w.lock();
			try {
				listIn.removeIf(x -> x.seq == __item1.seq);				
			} finally {
				w.unlock();
			}

			w.lock();
			try {
				listOut.removeIf(x -> x.seq == item.seq);				
			} finally {
				w.unlock();
			}
		} else if (delayedMsec == DelayedMsec.i160o80) {
			int seq = item.seq * 2;
			
			r.lock();
			try {
				_item0 = linout.stream().filter(x -> x.seq == seq).findFirst().get();					
			} catch (NoSuchElementException | NullPointerException e) {
				Util.WriteLog(String.format(Finalvars.ErrHeader, 1010, e.getMessage()), 1);
				
				_item0 = new RTPInfo();
				_item0.voice = new byte[332];
				_item0.seq = seq;
				_item0.size = 92;
				_item0.extension = item.extension;
				_item0.peer_number = item.peer_number;
			} finally {
				r.unlock();
			}


			r.lock();
			try {
				_item1 = linout.stream().filter(x -> x.seq == seq + 1).findFirst().get();					
			} catch (NoSuchElementException | NullPointerException e) {
				Util.WriteLog(String.format(Finalvars.ErrHeader, 1011, e.getMessage()), 1);
				
				_item1 = new RTPInfo();
				_item1.voice = new byte[332];
				_item1.seq = seq;
				_item1.size = 92;
				_item1.extension = item.extension;
				_item1.peer_number = item.peer_number;
			} finally {
				r.unlock();
			}
			
			final RTPInfo __item0 = _item0;
			final RTPInfo __item1 = _item1;

			// item2 + tmpitem mix with item1 and write
			byte[] tmpbuff = new byte[332];
			System.arraycopy(__item0.voice, 0, tmpbuff, 0, __item0.size);
			System.arraycopy(__item1.voice, headersize, tmpbuff, __item0.size, (__item1.size - headersize));
			RTPInfo _itm = new RTPInfo();
			_itm.voice = tmpbuff;
			_itm.size = (__item0.size + __item1.size - headersize);

			try {
				mixedbytes = this.RealMix(_itm, item);
			} catch (IOException e) {
				// e.printStackTrace();
				Util.WriteLog(String.format(Finalvars.ErrHeader, 1012, e.getMessage()), 1);
			}

			w.lock();
			try {
				listIn.removeIf(x -> x.seq == item.seq);				
			} finally {
				w.unlock();
			}

			w.lock();
			try {
				listOut.removeIf(x -> x.seq == __item0.seq);				
			} finally {
				w.unlock();
			}

			w.lock();
			try {
				listOut.removeIf(x -> x.seq == __item1.seq);				
			} finally {
				w.unlock();
			}
		} else {
			// item > in
			// same
			// item1 mix with item2 and write
			RTPInfo _item = null;
			
			r.lock();
			try {
				_item = linout.stream().filter(x -> x.seq == item.seq).findFirst().get();					
			} catch (NoSuchElementException | NullPointerException e) {
				// Util.WriteLog(String.format(Finalvars.ErrHeader, 1013, e.getMessage()), 1);
				
				_item = new RTPInfo();
				_item.voice = new byte[332];
				_item.seq = item.seq;
				_item.size = item.size;
				_item.extension = item.extension;
				_item.peer_number = item.peer_number;
			} finally {
				r.unlock();
			}
			
			final RTPInfo __item = _item;

			try {
				mixedbytes = this.RealMix(item, __item);
			} catch (IOException e) {
				e.printStackTrace();
			}

			w.lock();
			try {
				listIn.removeIf(x -> x.seq == item.seq);
			} finally {
				w.unlock();
			}
			
			w.lock();
			try {
				listOut.removeIf(x -> x.seq == __item.seq);				
			} finally {
				w.unlock();
			}
		}

		return mixedbytes;
	}

	private byte[] RealMix(RTPInfo item1, RTPInfo item2) throws IOException {
		if (item1 == null || item2 == null) return null;

		if (item1.size == 0 || item2.size == 0) return null;

		byte[] wavSrc1 = new byte[item1.size - headersize];
		byte[] wavSrc2 = new byte[item2.size - headersize];

		System.arraycopy(item1.voice, headersize, wavSrc1, 0, wavSrc1.length);
		System.arraycopy(item2.voice, headersize, wavSrc2, 0, wavSrc2.length);
		
		List<AudioInputStream> audioInputStreamList = new ArrayList<AudioInputStream>();
		
		InputStream inputstream1 = new ByteArrayInputStream(wavSrc1, 0, wavSrc1.length);
		InputStream inputstream2 = new ByteArrayInputStream(wavSrc2, 0, wavSrc2.length);
		
		AudioFormat aformat;
		AudioInputStream audioInputStream1 = null;
		AudioInputStream audioInputStream2 = null;
		
		switch (codec.waveFormatTag) {
			case MuLaw:
				// aformat = new AudioFormat(AudioFormat.Encoding.ULAW, 8000, 8, 1, 1, 8000 * 1, isBigendian);
				audioInputStream1 = new AudioInputStream(inputstream1, targetformat, wavSrc1.length);
				audioInputStream2 = new AudioInputStream(inputstream2, targetformat, wavSrc2.length);
				
				audioInputStreamList.add(audioInputStream1);
				audioInputStreamList.add(audioInputStream2);
				break;
			case ALaw:
				// aformat = new AudioFormat(AudioFormat.Encoding.ALAW, 8000, 8, 1, 1, 8000 * 1, isBigendian);
				ConvertInputStream convertInputStream1 = new ConvertInputStream(inputstream1, true);
				ConvertInputStream convertInputStream2 = new ConvertInputStream(inputstream2, true);
				
				audioInputStream1 = new AudioInputStream(convertInputStream1, targetformat, wavSrc1.length);
				audioInputStream2 = new AudioInputStream(convertInputStream2, targetformat, wavSrc2.length);
				
				audioInputStreamList.add(audioInputStream1);
				audioInputStreamList.add(audioInputStream2);
				break;
/*			case G723:
				aformat = new AudioFormat(AudioFormat.Encoding.ALAW, 8000, 8, 1, 1, 8000 * 1, isBigendian);
				break;
			case G729:
				aformat = new AudioFormat(AudioFormat.Encoding.ALAW, 8000, 8, 1, 1, 8000 * 1, isBigendian);
				break;
			case Pcm:
				aformat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 8, 1, 1, 8000 * 1, isBigendian);
				break;*/
			default:
				// aformat = new AudioFormat(AudioFormat.Encoding.ALAW, 8000, 8, 1, 1, 8000 * 1, isBigendian);
				audioInputStream1 = new AudioInputStream(inputstream1, targetformat, wavSrc1.length);
				audioInputStream2 = new AudioInputStream(inputstream2, targetformat, wavSrc2.length);
				
				audioInputStreamList.add(audioInputStream1);
				audioInputStreamList.add(audioInputStream2);
				break;
		}
		
		// AudioInputStream audioInputStream = new MixingFloatAudioInputStream(aformat, audioInputStreamList);
		AudioInputStream audioInputStream = new MixingAudioInputStream(targetformat, audioInputStreamList);
		
		
		// to PCM
		/*
		AudioFormat aaformat = new AudioFormat(8000, 16, 1, true, false);
		DecompressInputStream audioInputStream0 = new DecompressInputStream(audioInputStream, true);
		
		byte[] mixedbytes = new byte[wavSrc1.length * 2];
		
		try {
			
			audioInputStream0.read(mixedbytes, 0, mixedbytes.length);
		} catch (IOException e) {
			Util.WriteLog(String.format(Finalvars.ErrHeader, 1014, e.getMessage()), 1);
		}
		
		audioInputStream1.close();
		audioInputStream2.close();
		audioInputStream.close();
		audioInputStream0.close();
		*/
		
		// to ALAW or ULAW
		byte[] mixedbytes = new byte[wavSrc1.length];
		
		try
		{
			audioInputStream.read(mixedbytes, 0, mixedbytes.length);
		}
		catch (IOException e)
		{
			Util.WriteLog(String.format(Finalvars.ErrHeader, 1014, e.getMessage()), 1);
		}
		
		audioInputStream1.close();
		audioInputStream2.close();
		audioInputStream.close();
		
		return mixedbytes;
	}
	
    private void WaveFileWriting(byte[] buff) {
        if (buff == null) return;
        if (buff.length == 0) return;

        try {
			this.writer.Write(buff, 0, buff.length);
		} catch (IOException e) {
			Util.WriteLog(String.format(Finalvars.ErrHeader, 1015, e.getMessage()), 1);
		}
        
        try {
			this.writer.flush();
			dataSize += buff.length;
		} catch (IOException e) {
			Util.WriteLog(String.format(Finalvars.ErrHeader, 1016, e.getMessage()), 1);
		}
    }
    
	@Override
	public void close() throws IOException {
		this.writer.close();

		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		
		if (endtimer != null) {
			endtimer.cancel();
			endtimer = null;
		}
		
		listIn.clear();
		listOut.clear();
	}

	enum DelayedMsec {
		i80o160, i160o80, same
	}

	class Timer_Elapsed extends TimerTask {
		@Override
		public void run() {
			MixRtp("");
		}
	}

	class Endtimer_Elapsed extends TimerTask {
		public RTPRecordInfo parent = null;
		public Endtimer_Elapsed(RTPRecordInfo obj) {
			this.parent = obj;
		}
		
		@Override
		public void run() {
			if (previousDataSize < dataSize) {
				previousDataSize = dataSize;
				return;
			}
			
			MixRtp("final");
			
			try {
				close();
				if (endtimer != null) endtimer.cancel();
			} catch (IOException e) {
				Util.WriteLog(String.format(Finalvars.ErrHeader, 1017, e.getMessage()), 1);
			} finally {
				System.out.println("Finished finally");
				EndOfCallEventHandler.raiseEvent(parent, new EndOfCallEventArgs(""));
			}
		}

	}
}
