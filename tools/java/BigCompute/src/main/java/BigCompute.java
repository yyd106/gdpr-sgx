import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

public class BigCompute {
    public static void switch_16_to_10(String num_str) {
        System.out.println("string is:"+num_str);
        char str16[] = num_str.toCharArray();
        BigInteger res = new BigInteger("1");
        BigInteger base = new BigInteger("16");
        int len = str16.length;
        for (int i = 0; i < len; i++) {
            char ctmp = str16[i];
            int itmp = 0;
            if(ctmp > 'A') {
                ctmp = (char)(ctmp - 'A' + 'a');
            }
            if (ctmp >= 'a') {
                itmp = ctmp - 'a' + 10;
            } else if (ctmp > '0') {
                itmp = ctmp - '0';
            } else {
                continue;
            }
            int a = len - i - 1;
            BigInteger btmp = new BigInteger("1");
            btmp = btmp.multiply(new BigInteger(String.valueOf(itmp))).multiply(base.pow(a));
            res = res.add(btmp);
        }
        System.out.println(res);
    }
    public static void switch_10_to_16(String num_str) {
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
        BigInteger acc = new BigInteger("1");
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
//                System.out.println("baseAcc:"+baseAcc);
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
//                System.out.println("dec:"+decimalNum);
            acc = one;
            cal = one;
            baseAcc = zero;
        }
        StringBuilder result = new StringBuilder();
        result.append(res);
        System.out.println(result.reverse());
    }
    public static void main(String[] args) {
        String q = "FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF";
        String n = "FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551";
        String a = "FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC";
        String b = "5AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B";
        String gx = "6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296";
        String gy = "4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5";
        System.out.println("q:"+(new BigInteger(q,16).toString(10)));
        System.out.println("a:"+(new BigInteger(a,16).toString(10)));
        System.out.println("b:"+(new BigInteger(b,16).toString(10)));
        System.out.println("n:"+(new BigInteger(n,16).toString(10)));
        System.out.println("gx:"+(new BigInteger(gx,16).toString(10)));
        System.out.println("gy:"+(new BigInteger(gy,16).toString(10)));
        String s_pri_key = "10184146703365334043556037669205670770691723245811011851142641057245451781251";
        String s_pub_key = "10023173303565839910269731948987173184753266829996803179839040821547573653963694953281102673523187199036244355845660731906844071735357464450704716398511337";
        System.out.println("s_pri_key:"+(new BigInteger(s_pri_key,10).toString(16)));
        System.out.println("s_pub_key:"+(new BigInteger(s_pub_key,10).toString(16)));
        String alice_pri = "9697996002915456426079138905704146350415508181892068769187904798530602906730";
        String alice_pub = "96404885132015907233391137191226920779192677339986580813214648812585031688924";
        String bob_pri = "7D5A0975FC2C3057EEF67530417AFFE7FB8055C126DC5C6CE94A4B44F330B5D9";
        String bob_pub = "47123147844591440397545415867766025557083699857589459376159036146123604596388";
        System.out.println("alice pri:"+(new BigInteger(alice_pri,10).toString(16)));
        System.out.println("alice pub:"+(new BigInteger(alice_pub,10).toString(16)));
        System.out.println("bob   pri:"+(new BigInteger(bob_pri,16).toString(10)));
        System.out.println("bob   pub:"+(new BigInteger(bob_pub,10).toString(16)));

        /*
        try {
            String fileName = "decimal.txt";
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String num_str = br.readLine();
            if (args.length == 0) {
                System.out.println("use default method");
                //switch_16_to_10(fileName);
                //switch_10_to_16(num_str);
                //String tmp = new BigInteger(num_str,16).toString(10);
                System.out.println("num str:"+num_str);
                String tmp = new BigInteger(num_str,10).toString(16);
                System.out.println(tmp);
            } else if (args[0].compareTo("de") == 0) {
                switch_16_to_10(num_str);
            } else if (args[0].compareTo("he") == 0) {
                switch_10_to_16(fileName);
            } else {
                System.out.println("Please indicate switch type(de/he)");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        */
    }
}
