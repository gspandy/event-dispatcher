class C{
    public C(String s){
        System.out.println(s+"------cccccccccccccc");
    }
}





public class D {

    /**D的构造器**/
    public D(){
        System.out.println("D的构造器");
    }

    /**D静态代码块**/
    static{
        System.out.println("执行静态代码块");
    }

    /**D变量**/

    private C c1=new C("变量--（1）");
    private C c2=new C("变量--（2）");

    /**D非静态代码块**/
    {
        System.out.println("执行非静态代码块");
    }

    /**D的静态变量**/

    private static C c3=new C("静态变量--（3）");
    private static C c4=new C("静态变量--（4）");

    public static void main(String [] args){
        new D();
    }
}