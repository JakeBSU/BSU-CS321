import java.nio.ByteBuffer;

public class TreeObject {
	private Long key;
	private int frequency;

	public TreeObject(long key) {
		this.key = key;
		this.frequency = 1;
	}

	public Long getKey(){
		return this.key;
	}

	public int getFrequency(){
		return this.frequency;
	}

	public void increaseFrequency() {
		frequency++;
	}

	public TreeObject(ByteBuffer b) {
		key = b.getLong();
		frequency = b.getInt();
	}

	public String getKeyString()
	{
		return ConvertDNAToLong.convertFromLong(key);
	}

	@Override
	public String toString() {
		return "Frequency: " + frequency + ", Sequence: " + ConvertDNAToLong.convertFromLong(key);
	}

}
