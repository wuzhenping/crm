import com.msy.plus.Application;
import com.msy.plus.core.jasypt.MyEncryptablePropertyDetector;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyDetector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author MoShuying
 * @date 2018/05/27
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PasswordEncryptor {

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private MyEncryptablePropertyDetector encryptablePropertyDetector;

  @Test
  public void encode() throws Exception {
    final String admin = this.passwordEncoder.encode("root");
    final String user = this.passwordEncoder.encode("888888");
    System.err.println("admin password = " + admin);
    System.err.println("user password = " + user);
//    System.out.println(encryptablePropertyDetector.unwrapEncryptedValue("MyEnc({cSs3wYoZ0BTijYqdYVj9xg==})"));
//    System.out.println(encryptablePropertyDetector.unwrapEncryptedValue("cSs3wYoZ0BTijYqdYVj9xg=="));
//    System.out.println(encryptablePropertyDetector.unwrapEncryptedValue("$2a$10$M819VQByftpCO1LGbDPcn.BTDJVSkbx0lbjc6mMn15xPQL2OQZkDK"));
  }

//  MyEnc({cSs3wYoZ0BTijYqdYVj9xg==})
}