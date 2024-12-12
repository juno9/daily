import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NewServer2 {
    public static Map<String, user> 유저해시맵 = new ConcurrentHashMap<>();

    // 시간 포매터
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 로그 메서드
    private static void log(String message) {
        String currentTime = LocalDateTime.now().format(TIME_FORMATTER);
        System.out.println("[" + currentTime + "] " + message);
    }

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(10005)) {
            log("서버 시작");

            while (true) {
                log("접속 대기 중...");
                Socket sock = server.accept(); // 클라이언트 연결 수신
                log("클라이언트 접속: " + sock.getInetAddress());//접속한 클라이언트의 ip주소

                // 스트림 생성
                InputStreamReader 인풋스트림리더 = new InputStreamReader(sock.getInputStream());
                OutputStreamWriter 아웃풋스트림라이터 = new OutputStreamWriter(sock.getOutputStream());
                BufferedReader 버퍼리더 = new BufferedReader(인풋스트림리더);
                PrintWriter 프린트라이터 = new PrintWriter(아웃풋스트림라이터, true);

                // 채팅 스레드 시작
                new ChatThread2(sock, 버퍼리더, 프린트라이터).start(); // 클라이언트와 연결되어 있는 소켓을 이 서버 내에서는 클라이언트라고 취급하게 됨
            }
        } catch (IOException e) {
            log("오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
