import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by Administrator on 2018/8/15.
 */

@SpringBootTest
public class Atest {

    @Test
    public void t() {
        Integer s = null;

        System.out.println(null == s);
        System.out.println((int) s == 0);
        System.out.println(s.equals(0));
    }
}
