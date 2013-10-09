import java.util.ArrayList;

public class ArraysHelper {
	public ArrayList<Byte> asArrayList(byte[] array) {
		ArrayList<Byte> result = new ArrayList<Byte>();

		for (byte item : array) {
			result.add(item);
		}

		return result;
	}

	public ArrayList<Piece> asArrayList(Piece[] array) {
		ArrayList<Piece> result = new ArrayList<Piece>();

		for (Piece item : array) {
			result.add(item);
		}

		return result;
	}

	
}