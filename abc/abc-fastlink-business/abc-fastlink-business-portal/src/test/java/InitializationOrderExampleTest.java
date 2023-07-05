/**
 * A
 *
 * @Description A 详细介绍
 * @Author Trivis
 * @Date 2023/6/12 16:59
 * @Version 1.0
 */
public class InitializationOrderExampleTest {
    private static String staticField = "静态属性";
    private String field = "属性";

    static {
        System.out.println(staticField);
        System.out.println("静态代码块");
    }

    {
        System.out.println(field);
        System.out.println("非静态代码块");
    }

    public InitializationOrderExampleTest() {
        System.out.println("构造函数");
    }

    public static void main(String[] args) {
        new InitializationOrderExampleTest();
    }
}
