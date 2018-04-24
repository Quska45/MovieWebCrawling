package createDatabase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class DBManager {
      //공통적으로 사용될 객체와 필수정보들을 인스턴스 변수로 선언했다.
      private static Connection conn;
      
      private final static String DRIVER = "oracle.jdbc.driver.OracleDriver"; //오라클 드라이브를 로드 하는데 사용된다.
      private final static String URL = "jdbc:oracle:thin:@127.0.0.1:1521:XE"; //디비와 연결에 사용된다.
      private final static String USER= "java";//디비와 연결에 사용된다.
      private final static String PASSWORD = "1234";//디비와 연결에 사용된다.
                  
      private DBManager() {
            // TODO Auto-generated constructor stub
            //다른 클래스에서 객체가 생성되지 못하도록 막아준다. 싱글톤패턴은 이렇게 해서 사용하는 것이다.
            //객체가 생성되지 않기 때문에 메소드(getConnection)를 통해 DBManager클래스에 접근하여 작업을 수행하게 된다.
      }
      
      //인스턴스를 생성 또는 기존 인스턴스를 반환하는 메서드
      //싱글톤에서는 객체생성이 불가하기 때문에 메소드를 이용해서 필요한 작업들을 수행한다.
      public static Connection getConnection(){     
              conn=null;
            if(conn == null){
                  //디비와 연결이 되어 있지 않은 상태인 것을 의미한다. 연결을 위해 if문 안으로 들어온다.
                  try { //디비와 관련된 작업을 할 떄는 꼭 try문이 필요하다는 것을 명심하자.
                        Class.forName(DRIVER); //드라이버를 로드 한다.
                        conn = DriverManager.getConnection(URL, USER, PASSWORD); //디비와 자바를 연결 시켜줬다.                        
                  } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        System.out.println("DB연결에 문제가 있습니다.");
                  }
            }
            //나를 호출한 녀석에서 conn을 보내는 것이다.
            //conn은 null로 초기화 되어 있지만 if문에서 이뤄지는 작업들을 통해 값을 가지게 된다.
            return conn;
      }
      
      
      //insert delete update 메소드에 사용하는 close메소드
      public static void close(Connection conn, PreparedStatement pstmt){
            try {
                  if(conn != null){ //conn객체가 디비와 연결되어 있다는 것이다. 연결을 끊고 종료시키기 위해서 if문 안으로 들어온다.
                        try {
                              conn.close();
                        } catch (Exception e) {
                              // TODO: handle exception
                              e.printStackTrace();
                        }
                  }
                  
                  if(pstmt != null){
                        try {
                              pstmt.close();
                        } catch (Exception e) {
                              // TODO: handle exception
                              e.printStackTrace();
                        }
                  }                 
                  
            } catch (Exception e) {
                  e.printStackTrace();
                  System.out.println("DB연결에 문제가 있습니다.");
            }
      }
      
      //search, select의 close에 사용되는 메소드
            public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs){
                  try {
                        if(conn != null){
                              try {
                                    conn.close();
                              } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                              }
                        }
                        
                        if(pstmt != null){
                              try {
                                    pstmt.close();
                              } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                              }
                        }
                        
                        if(rs != null){ //닫힌 것이 아니다. 뭔가가 있다는 것.
                              //rs객체는 search와 select메소드에서만 사용하는 것이다. 따라서 오버로딩을 통해 rs를 제외한 것들만 사용하는 메소드를 만들어준다.
                              try {
                                    rs.close();
                              } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                              }
                        }
                        
                  } catch (Exception e) {
                        e.printStackTrace();
                  }
            }
}