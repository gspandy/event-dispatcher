import enums.InnerEnum;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by zhangsx on 2015/3/17.
 */
public class EnumTest {
    @Test
    public void testMyEnum(){
        Assert.assertTrue("BODY".equals(InnerEnum.BODY.toString()));
        Assert.assertTrue("CP".equals(InnerEnum.CP.toString()));
        Assert.assertTrue("FACT".equals(InnerEnum.FACT.toString()));
    }
}
