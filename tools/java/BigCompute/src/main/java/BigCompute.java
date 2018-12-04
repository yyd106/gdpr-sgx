import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

public class BigCompute {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("decimal.txt"));
            String num_str = br.readLine();
            int charArryLen = num_str.length();
            if(charArryLen >= 64 && charArryLen < 128) {
                charArryLen = 64;
            } else if(charArryLen > 128){
                charArryLen = 128;
            }
            char res[] = new char[charArryLen];
            Arrays.fill(res,'0');
            BigInteger decimalNum = new BigInteger(num_str);
            BigInteger base = new BigInteger("16");
            BigInteger baseAcc = new BigInteger("0");
            BigInteger cal = new BigInteger("1");
            BigInteger zero = new BigInteger("0");
            BigInteger one = new BigInteger("1");
            BigInteger acc = new BigInteger("0");
            BigInteger tmp;
            while(true) {
                if(decimalNum.equals(zero)) {
                    break;
                }
                while (decimalNum.compareTo(cal) >= 0) {
                    cal = cal.multiply(base);
                    baseAcc = baseAcc.add(one);
                }
                if(decimalNum.compareTo(cal) == 0) {
                    res[baseAcc.intValue()] = '1';
                    break;
                } else {
                    cal = cal.divide(base);
                    baseAcc = baseAcc.subtract(one);
                }
                System.out.println("baseAcc:"+baseAcc);
                while(acc.compareTo(base)<=0) {
                    tmp = cal.multiply(acc);
                    int flag = decimalNum.compareTo(tmp);
                    if(flag > 0) {
                        acc=acc.add(one);
                        continue;
                    }
                    if(flag < 0) {
                        tmp = cal.multiply(acc.subtract(one));
                        decimalNum = decimalNum.subtract(tmp);
                    } else {
                        decimalNum = zero;
                    }
                    int int_tmp = acc.subtract(one).intValue();
                    res[baseAcc.intValue()] = int_tmp > 9 ? (char)(int_tmp-10+'a') : (char)(int_tmp+'0');
                    break;
                }
                System.out.println("dec:"+decimalNum);
                acc = zero;
                cal = one;
                baseAcc = zero;
            }
            StringBuilder result = new StringBuilder();
            result.append(res);
            System.out.println(result.reverse());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
