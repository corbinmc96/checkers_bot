import lejos.nxt.*;
import lejos.nxt.comm.*;

public class USBListener {
	public static void main(String[] args) {
		NXTConnection connection = USB.waitForConnection();
		System.out.println("connected");
		Button.waitForAnyEvent(1000000000);
	}
}