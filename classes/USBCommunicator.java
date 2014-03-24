import lejos.nxj.comm.*;

public class USBCommunicator {
	public static void main(String[] args) throws NXTCommException {
		NXTComm comm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
		NXTInfo nxtInfo = comm.search(null);
		nxtInfo.open();
		System.out.println("connected");
	}
}