import java.io.*;
import java.net.Socket;

public class user {
    String 이메일;
 BufferedReader 버퍼리더;

 PrintWriter 프린트라이터;
    String 위치;

    boolean 첫연결여부;


    user(String 이메일,BufferedReader 버퍼리더,PrintWriter 프린트라이터 ) {
        this.이메일 = 이메일;
        this.프린트라이터 = 프린트라이터;
        this.버퍼리더=버퍼리더;
        this.위치=이메일;

    }

    public void set첫연결여부(boolean 첫연결여부) {
        this.첫연결여부 = 첫연결여부;
    }

    public boolean get첫연결여부() {
        return 첫연결여부;
    }

    public void set위치(String 위치) {
        this.위치 = 위치;
    }

    public String get위치() {
        return 위치;
    }

    public BufferedReader get버퍼리더() {
        return 버퍼리더;
    }

    public void set버퍼리더(BufferedReader 버퍼리더) {
        this.버퍼리더 = 버퍼리더;
    }


    public PrintWriter get프린트라이터() {
        return 프린트라이터;
    }

    public void set프린트라이터(PrintWriter 프린트라이터) {
        this.프린트라이터 = 프린트라이터;
    }

    public void set이메일(String 이메일) {
        this.이메일 = 이메일;
    }



    public String get이메일() {
        return 이메일;
    }
}
