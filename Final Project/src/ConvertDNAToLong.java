import java.util.ArrayList;
import java.util.Collection;

public class ConvertDNAToLong {

    public static long convertToLong(Collection<Character> dnaSeq) {
        long value = 1l;

        for(char chr : dnaSeq) {
            value = value << 2;
            switch (chr) {
                case 'a':
                case 'A':
                    value = value | (0b00);
                    break;
                case 't':
                case 'T':
                    value = value | (0b11);
                    break;
                case 'c':
                case 'C':
                    value = value | (0b01);
                    break;
                case 'g':
                case 'G':
                    value = value | (0b10);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid character found in given dnaSeq '" + chr + "'");
            }
        }
        return value;
    }

    public static long convertToLong(String dnaSeq) {
        Collection<Character> chars = new ArrayList<>(dnaSeq.length());
        for (char chr : dnaSeq.toCharArray())
            chars.add(chr);

        return convertToLong(chars);
    }

    public static String convertFromLong(long dnaSeq) {
        StringBuilder output = new StringBuilder();

        long value = dnaSeq;

        while (value > 1) {
            byte byt = (byte)(value & (0b11));
            value = value >> 2;
            switch (byt) {
                case 0b00:
                    output.append('a');
                    break;
                case 0b11:
                    output.append('t');
                    break;
                case 0b01:
                    output.append('c');
                    break;
                case 0b10:
                    output.append('g');
                    break;
                default:
                    throw new IllegalArgumentException("Invalid byte sequence found in given dnaSeq '" + byt + "'");
            }
        }
        return output.reverse().toString().toUpperCase();
    }
}
