import Messages.Messages;
import com.google.protobuf.InvalidProtocolBufferException;

public class TestEnclaveBridge
{
    public static void main(String[] args) {
        EnclaveBridge eb = new EnclaveBridge();
        String res = eb.handleVerification(eb.messageHandlerOBJ);
        try {
            byte[] data = res.getBytes();
            Messages.MessageMsg0 result = Messages.MessageMsg0.parseFrom(data);
            System.out.println(result.getStatus());
        } catch (InvalidProtocolBufferException e) {
            System.out.println(e.getMessage());
        }
    }
}
