import cn.telecom.commons.util.AESUtils;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class AESUtilsTest {
    @Test
    public void test01() {
        String content = "64ffd8d3a3035169022260fe" + "_" + new Random().nextDouble();
        String aesKey = "abcd123456123456";
        String encrypt = AESUtils.encrypt(content, aesKey);
        System.err.println(encrypt);
        String decrypt = AESUtils.decrypt(encrypt, aesKey);
        System.err.println(decrypt);
    }
}
