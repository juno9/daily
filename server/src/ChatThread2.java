import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

class ChatThread2 extends Thread {

    private final Socket 소켓;
    private final BufferedReader 버퍼리더;
    private final PrintWriter 프린트라이터;
    private String 이메일;
    private user 유저;

    public ChatThread2(Socket 소켓, BufferedReader 버퍼리더, PrintWriter 프린트라이터) {
        this.소켓 = 소켓;
        this.버퍼리더 = 버퍼리더;
        this.프린트라이터 = 프린트라이터;
    }

    // 로그 메서드: 시간과 메시지를 함께 출력
    private void log(String message) {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("[" + currentTime + "] " + message);
    }

    @Override
    public void run() {
        try {
            // 1. 이메일 읽기
            이메일 = 버퍼리더.readLine();//제일 먼저 클라이언트는 내가 누구요 하는 이메일 정보를 보내도록 만들어 놨다.
            if (이메일 == null || 이메일.isEmpty()) {
                log("유효하지 않은 이메일을 수신했습니다.");
                return;
            }
            log(이메일 + "의 스레드 이메일 수신: " + 이메일);

            // 2. 유저 등록/갱신
            handleUserRegistration();

            // 3. 메시지 처리 루프
            processMessages();

        } catch (Exception e) {
            log(이메일 + "의 스레드 예외 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanupResources();
        }
    }

    private void handleUserRegistration() {
        user 기존유저 = NewServer2.유저해시맵.get(이메일);//메인 쓰레드에 있는 해시에서 이 이메일을 쓰고 있는 기존 유저가 있는지 먼저 확인
        if (기존유저 != null) {
            log(이메일 + "의 스레드 기존 유저를 제거합니다.");
            NewServer2.유저해시맵.remove(이메일);
        }//기존 유저가 있다면 해시맵에서 삭제

        유저 = new user(이메일, 버퍼리더, 프린트라이터);//새로 유저를 만들어 줌
        NewServer2.유저해시맵.put(이메일, 유저);
        log(이메일 + "의 스레드 유저 등록 완료.");
    }

    private void processMessages() throws IOException {
        String 상대방이메일;

        while (true) {
            상대방이메일 = 버퍼리더.readLine();//제일 처음 채팅을 하려고 채팅방에 입장하면 클라이언트는 최초로 생성했던 소켓을 통해 상대방 이메일을 보내야 한다.
            if (상대방이메일 == null || 상대방이메일.equals("/exit")) {
                log(이메일 + "의 스레드 클라이언트가 종료를 요청했습니다.");
                NewServer2.유저해시맵.remove(이메일);
                break;
            }

            log(이메일 + "의 스레드 상대방 이메일: " + 상대방이메일);
            유저.set위치(상대방이메일);

            while (true) {
                String 메시지 = 버퍼리더.readLine();
                if (메시지 == null || 메시지.equals("/quit")) {
                    log(이메일 + "의 스레드 대화 종료 요청 수신.");
                    유저.set위치("");
                    break;
                }
                handleMessage(상대방이메일, 메시지);
            }
        }
    }

    private void handleMessage(String 상대방이메일, String 메시지) {
        user 상대유저 = NewServer2.유저해시맵.get(상대방이메일);
        if (상대유저 != null) {
            log(이메일 + "의 스레드 메시지 전달: " + 메시지);
            상대유저.get프린트라이터().println(이메일);
            상대유저.get프린트라이터().println(메시지);
            상대유저.get프린트라이터().flush();
            log(상대유저.get이메일() + "에게 메시지 전달 완료: " + 메시지);
        } else {
            log(이메일 + "의 스레드 상대 유저가 오프라인 상태입니다.");
        }

        dbInsert(상대방이메일, 메시지);
    }

    private void dbInsert(String 받는유저이메일, String 보내는메시지) {
        String 유저이름 = "juno";
        String 비밀번호 = "skdye1dye!@#";
        String url = "jdbc:mysql://localhost:3306/DAILY?useSSL=false";

        String 날짜변환 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String query = "INSERT INTO DAILY.message (sender_email, receiver_email, time, contents) VALUES (?, ?, ?, ?)";

        try (Connection 연결 = DriverManager.getConnection(url, 유저이름, 비밀번호);
             PreparedStatement pstmt = 연결.prepareStatement(query)) {

            pstmt.setString(1, 이메일);
            pstmt.setString(2, 받는유저이메일);
            pstmt.setString(3, 날짜변환);
            pstmt.setString(4, 보내는메시지);
            pstmt.executeUpdate();
            log(이메일 + " 메시지가 데이터베이스에 저장되었습니다.");

        } catch (SQLException e) {
            log(이메일 + " 데이터베이스 오류: " + e.getMessage());
        }
    }

    private void cleanupResources() {
        try {
            if (버퍼리더 != null) 버퍼리더.close();
            if (프린트라이터 != null) 프린트라이터.close();
            if (소켓 != null && !소켓.isClosed()) 소켓.close();
            log(이메일 + "의 스레드 리소스 정리 완료.");
        } catch (IOException e) {
            log(이메일 + "의 스레드 리소스 정리 중 오류 발생: " + e.getMessage());
        }
    }
}
