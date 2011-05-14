package vafusion.mt4j;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

import org.mt4j.MTApplication;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputData.MTFingerInputEvt;
import org.mt4j.input.inputSources.AbstractInputSource;

public class LinuxHIDInputSource extends AbstractInputSource {

	private boolean success;
	private final static int struct_size = 24;
	private Socket sock;
	private Touch previous = null;
	private Touch current = null;
	
	public LinuxHIDInputSource(MTApplication mtApp) {
		super(mtApp);
		success = false;
		
		//start the daemon here...
		
		//daemon should be started
		
		int port = 9999;		
		if((sock = openSocket(port)) == null)
			return;
		
		success = true;
		
	}
	
	public boolean isSuccessfullySetup() {
		
		return success;
		
	}
	
	@Override
	public void pre() {
		
		if(success) {
			MTHIDEvent evt;
			while(true) {
				
				evt = pollEvent();
				switch(evt.type) {
//				case 0x00: //EV_SYN
//					if(evt.code == 2) //SYN_MT_REPORT
//						;
//					else
//						System.err.println("Unrecognized event from HID device");
//					break;
				case 0x01: //EV_KEY
					if(evt.code == 0x14a) { //BTN_TOUCH
						// A touch event has been recorded						
						if(evt.value == 0) { //release
//							MTHIDEvent idEvt = pollEvent(), xEvt = pollEvent(), yEvt = pollEvent(); // the next three events 
//																									// should be the id and (x, y) coordinates 
//																									// of the touch
							this.enqueueInputEvent(new MTFingerInputEvt(this, current.x, current.y, MTFingerInputEvt.INPUT_ENDED, current.cursor));
							current = previous = null;
							continue;
						}
					} else
						System.err.println("Unrecognized event from HID device");
					break;
				case 0x03: //EV_ABS
					
					if(evt.code == 0x00) { //ABS_X
						if(previous == null) //we're getting data about the last touch, but we don't have it's id so we can't track it
							continue;
						
						previous.x = evt.value;
						evt = pollEvent();
						previous.y = evt.value;
						this.enqueueInputEvent(new MTFingerInputEvt(this, previous.x, previous.y, MTFingerInputEvt.INPUT_UPDATED, previous.cursor));
						continue;
					} else if(evt.code == 0x39) { //ABS_MT_TRACKING_ID
						int id;
						if(current == null) {
							current = new Touch(evt.value, (evt = pollEvent()).value, (evt = pollEvent()).value);
							if((evt = pollEvent()).type != 0x00 && evt.code == 2) //EV_SYN and SYN_MT_REPORT, this is a synch point
								System.err.println("Incorrect event sequence!!!");
								
							this.enqueueInputEvent(new MTFingerInputEvt(this, current.x, current.y, MTFingerInputEvt.INPUT_DETECTED, current.cursor));
							
							evt = pollEvent(); // this should be the btn_touch touch down event
						} else {
							
							if(current.id == (id = evt.value)) {
								
								current.x = (evt = pollEvent()).value;
								current.y = (evt = pollEvent()).value;
								this.enqueueInputEvent(new MTFingerInputEvt(this, current.x, current.y, MTFingerInputEvt.INPUT_UPDATED, current.cursor));
								
							} else if(previous.id == (id = evt.value)) {
								
								previous.x = (evt = pollEvent()).value;
								previous.y = (evt = pollEvent()).value;
								this.enqueueInputEvent(new MTFingerInputEvt(this, previous.x, previous.y, MTFingerInputEvt.INPUT_UPDATED, previous.cursor));
								
							} else {
								
								previous = current;
								current = new Touch(id, (evt = pollEvent()).value, (evt = pollEvent()).value);
								this.enqueueInputEvent(new MTFingerInputEvt(this, current.x, current.y, MTFingerInputEvt.INPUT_DETECTED, current.cursor));
								
							}
							
							if((evt = pollEvent()).type != 0x00 && evt.code == 2) //EV_SYN and SYN_MT_REPORT, this is a synch point
								System.err.println("Incorrect event sequence!!!");
						}
						
						
					}
					
					break;
				default:
					System.err.println("Unrecognized event from HID device");
				}
				
			}
			
		}
		
	}
	

	private Socket openSocket(int port) {
		
		try {
			return new Socket(new InetSocketAddress("localhost", port).getAddress(), port);
		} catch (IOException e) {
			System.err.println("Error: Unable to open socket to HID daemon.");
		}
		
		return null;
		
	}
	
	private MTHIDEvent pollEvent() {
		
		try {
			InputStream is = sock.getInputStream();
			byte[] stream = new byte[struct_size];
			int readCount = is.read(stream, 0, struct_size);
			if(readCount < struct_size)
				return null;
			
			return new MTHIDEvent(stream);
		} catch (IOException e) {
			
			System.err.println("Failed to poll for event.");
			
		}
		
		return null;
		
	}
	

}

class MTHIDEvent {
	
	double time;
	int type;
	int code;
	int value;
	
	MTHIDEvent(byte[] stream) {
		
		time = read(stream, 0, 8) + read(stream, 8, 8) / 1000000;
		type = read(stream, 16, 2);
		code = read(stream, 18, 2);
		value = read(stream, 20, 4);
		
	}
	
	private int read(byte[] stream, int offset, int count) {
		
		byte[] b = Arrays.copyOfRange(stream, offset, offset + count);
		BigInteger i = new BigInteger(b);
		return i.intValue();
		
	}
	
}

class Touch {
	
	int id, x, y;
	InputCursor cursor;
	
	Touch(int id, int x, int y) {
		
		this.id = id;
		this.x = x;
		this.y = y;
		cursor = new InputCursor();
		
	}
	
}
