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
        String p = "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF";
        String a = "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC";
        String b = "28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93";
        String n = "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123";
        String gx = "32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7";
        String gy = "BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0";
        System.out.println("p:"+(new BigInteger(p,16).toString(10)));
        System.out.println("a:"+(new BigInteger(a,16).toString(10)));
        System.out.println("b:"+(new BigInteger(b,16).toString(10)));
        System.out.println("n:"+(new BigInteger(n,16).toString(10)));
        System.out.println("gx:"+(new BigInteger(gx,16).toString(10)));
        System.out.println("gy:"+(new BigInteger(gy,16).toString(10)));
        String s_pri_key = "10184146703365334043556037669205670770691723245811011851142641057245451781251";
        String s_pub_key = "10023173303565839910269731948987173184753266829996803179839040821547573653963694953281102673523187199036244355845660731906844071735357464450704716398511337";
        System.out.println("s_pri_key:"+(new BigInteger(s_pri_key,10).toString(16)));
        System.out.println("s_pub_key:"+(new BigInteger(s_pub_key,10).toString(16)));
        String alice_pri = "74962889921511758806000625803248773172902578715171168883169288003687679096040";
        String alice_pub = "6781714726494924399855043667467569005334935375161245224552159964168663933747995240724520657516922807224546083987924504415649357007348153859626714340433003";
        String bob_pri = "0900000009000000090000000900000009000000090000000900000009000000";
        String bob_pub = "10732698203830920249878074464951965691998910450537314357207017337686155047163490632428015280598342213730916066094450363289692580921429992734132721617521594";
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
