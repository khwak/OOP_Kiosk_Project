import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Search extends JFrame {
    private JPanel panel;              // 전체 panel
    private JTextField searchField;    // 검색창
    private JPanel resultPanel;        // 결과창
    private BookDatabase bookDatabase; // 도서 데이터
    private JFrame previousFrame;      // 이전 화면을 저장
    private int result_num;            // 검색 결과 수
    private int height = 200;          // 결과 유닛 높이

    public Search(String csvFilePath) {
        //, JFrame previousFrame
        //this.previousFrame = previousFrame;
        bookDatabase = new BookDatabase(csvFilePath);  // 도서 csv 파일의 경로를 통해 데이터베이스를 받는다.
        createUI();  // UI 생성
    }

    private void createUI() {
        setTitle("Search");              // 창 제목
        setSize(400, 600);  // 키오스크 화면 크기
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창을 닫을 때 실행 종료
        setLocationRelativeTo(null);     // 화면 중앙에 컴포넌트 배치


        // 전체 panel
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        SearchLabel(); // label : 검색

        MainPanel();   // main panel : 검색창과 결과창을 담을 panel

        BackPanel();   // 뒤로 가기

        add(panel);    // frame에 전체 panel 추가
    }

    // 검색 label
    private void SearchLabel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.GRAY);

        JLabel title = new JLabel("검색", SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(10, 0, 10, 0));
        Font titleFont = new Font("Dialog", Font.BOLD, 25); // 글꼴, 굵은체, 크기 25
        title.setFont(titleFont);
        titlePanel.add(title);

        panel.add(titlePanel, BorderLayout.NORTH);  // 전체 panel의 상단에 추가
    }

    // main panel
    private void MainPanel() {
        JPanel panelMain = new JPanel(new BorderLayout());
        panelMain.setBackground(Color.WHITE);

        // 검색창
        searchField = new JTextField(20);        // 검색창 길이
        searchField.setBorder(new RoundedBorder(10, 4)); // 검색창 디자인

        JButton searchButton = new JButton("검색");  // 검색 버튼
        searchButton.setBorder(new RoundedBorder(0, 2)); // 검색 버튼 디자인
        Font searchFont = new Font("Dialog", Font.PLAIN, 20); // 글꼴, 크기 20
        searchButton.setFont(searchFont);          // 글꼴 적용
        searchButton.setBackground(Color.WHITE);   // 버튼의 배경색
        searchButton.setForeground(Color.BLACK);   // 버튼의 글자색

        searchButton.addActionListener(new ActionListener() {  // 버튼 event
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBooks();  // 검색 버튼을 누르면 search 시작
            }
        });

        JPanel searchPanel = new JPanel(); // 검색창 panel
        searchPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
        searchPanel.setBackground(Color.WHITE);

        ImageIcon originalIcon = new ImageIcon(Search.class.getResource("search_symbol.png"));  // 돋보기 아이콘
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(40, 30, Image.SCALE_SMOOTH); // 원하는 크기로 조절
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel searchLabel = new JLabel(scaledIcon);

        searchPanel.add(searchLabel);  // 돋보기 아이콘
        searchPanel.add(searchField);  // 검색 입력창
        searchPanel.add(searchButton); // 검색 버튼

        panelMain.add(searchPanel, BorderLayout.NORTH);  // main panel에 검색창 추가


        // 결과창
        resultPanel = new JPanel();  // 결과창 panel
        resultPanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        resultPanel.setBackground(Color.WHITE);

        JScrollPane resultAll = new JScrollPane(resultPanel);  // Scroll
        resultAll.setPreferredSize(new Dimension(400, 400));
        resultAll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 스크롤 단위 조절을 위해 고정
        resultAll.getVerticalScrollBar().setUnitIncrement(10);  // 스크롤 시 단위

        panelMain.add(resultAll, BorderLayout.CENTER);  // main panel에 검색창 추가

        panel.add(panelMain, BorderLayout.CENTER);   // 전체 panel에 main panel 추가
    }

    // 뒤로가기
    private void BackPanel() {
        JButton back = new JButton("뒤로 가기");  // 뒤로가기 버튼
        back.setBorder(new EmptyBorder(10, 0, 10, 0));
        Font backFont = new Font("Dialog", Font.BOLD, 20); // 글꼴, 굵은체, 크기 20
        back.setFont(backFont);
        back.setBackground(Color.ORANGE);  // 버튼의 배경색
        back.setForeground(Color.BLACK);   // 버튼의 글자색

        back.addActionListener(new ActionListener() {  // 뒤로가기 event
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });

        panel.add(back, BorderLayout.SOUTH);         // 전체 panel의 하단에 뒤로가기 버튼 추가
    }
    private void goBack() {
        previousFrame.setVisible(true); // 이전 화면을 보이도록 설정
        dispose(); // 현재 화면 닫기
    }

    // 도서 검색
    private void searchBooks() {
        String query = searchField.getText();  // 검색창에 입력한 문자열
        List<Book> results = bookDatabase.searchBooks(query);  // 데이터베이스에서 문자열에 해당하는 내용을 results에 저장

        resultPanel.removeAll(); // 이전 결과를 제거
        result_num = 0;          // 결과 개수 변수

        for (Book book : results) {  // 입력한 문자열과 일치하는 내용을 찾기 위해 for문 순환
            result_num += 1;     // 일치하는 내용을 찾을 때마다 결과 개수 +1

            // 결과 panel
            // 도서 사진, 도서 정보, 예약 버튼
            JPanel bookPanel = new JPanel();
            bookPanel.setBackground(Color.WHITE);
            bookPanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
            bookPanel.setPreferredSize(new Dimension(400, height));

            // 도서 사진
            BookImage coverPanel = new BookImage(book.getCoverImagePath());
            coverPanel.setPreferredSize(new Dimension(100, height-10));
            coverPanel.setBackground(Color.WHITE);

            // 도서 정보
            JPanel infoPanel = new JPanel(new GridLayout(3, 1)); // 3개의 정보를 세로로 표시하기 위한 패널
            infoPanel.setPreferredSize(new Dimension(200, height-10));
            JLabel titleLabel = new JLabel("제목: " + book.getTitle());
            JLabel authorLabel = new JLabel("저자: " + book.getAuthor());
            JLabel reservationLabel = new JLabel("예약: " + (book.isAvailable() ? "가능" : "불가능"));

            infoPanel.add(titleLabel);
            infoPanel.add(authorLabel);
            infoPanel.add(reservationLabel);

            // 예약 panel과 예약 button
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BorderLayout());
            buttonPanel.setPreferredSize(new Dimension(100, height-10));
            buttonPanel.setBorder(new EmptyBorder(0, 0, 0, 30));

            JButton reserveButton = new JButton("예약");
            Font reserveFont = new Font("Dialog", Font.BOLD, 17); // 글꼴, 굵은체, 크기 17
            reserveButton.setFont(reserveFont);
            reserveButton.setBackground(Color.GREEN);   // 버튼의 배경색
            reserveButton.setForeground(Color.BLACK);   // 버튼의 글자색

            reserveButton.setEnabled(book.isAvailable()); // 예약이 가능하면 활성화
            reserveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (book.isAvailable()) {
                        dispose();
                        new Reservation(book, Search.this);
                    } else {
                        JOptionPane.showMessageDialog(
                                Search.this,
                                "예약이 불가능합니다.",
                                "예약 실패",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            });
            buttonPanel.add(reserveButton, BorderLayout.CENTER); // 버튼 panel에 button 추가

            bookPanel.add(coverPanel); // 도서 표지 결과 추가
            bookPanel.add(infoPanel); // 도서 정보 결과 추가
            bookPanel.add(buttonPanel); // 버튼 패널을 오른쪽에 추가

            resultPanel.add(bookPanel); // 결과 panel에 추가
        }
        resultPanel.setPreferredSize(new Dimension(300, result_num*height));
        resultPanel.revalidate(); // UI 업데이트
        resultPanel.repaint();    // UI 업데이트
    }
}
