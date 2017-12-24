package jacobi.test.exception;

public class TestException extends Exception {
   public static TestException build(int errorIndex){
       String message = String.format("value in %d doesn't equals",errorIndex);
       return new TestException(message);
   }

   public TestException(String message){
       super(message);
   }
}
