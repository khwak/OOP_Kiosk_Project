package LibraryManagerDisplay;

import CSVController.BookCSVController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class SelectTaskDialog extends JDialog {
    Vector<Vector<String>> bookList;
    DefaultTableModel model;
    int row;
    public SelectTaskDialog(Vector<Vector<String>> bookList, BookInfo book, DefaultTableModel model, int row) {
        Color green = new Color(0x00469C76);
        Color orange = new Color(0x00EE7930);

        Font btnFont = new Font("맑은 고딕", Font.BOLD, 18);
        Font labelFont = new Font("맑은 고딕", Font.BOLD, 16);

        this.bookList = bookList;
        this.model = model;
        this.row = row;

        setTitle("삭제/수정 선택");

        JPanel confirmPanel = new JPanel();
        JLabel confirmLabel = new JLabel("원하시는 업무를 선택해주세요.");
        confirmLabel.setFont(labelFont);
        confirmPanel.add(confirmLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        JButton updateButton = new JButton("수정");
        updateButton.setBackground(green);
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(btnFont);
        JButton deleteButton = new JButton("삭제");
        deleteButton.setBackground(orange);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(btnFont);

        // 수정 버튼 -> dialog 띄움
        updateButton.addActionListener(e -> {
            dispose();
            UpdateBookDialog updateBookDialog = new UpdateBookDialog(bookList, book);
            updateBookDialog.setVisible(true);
        });

        // 삭제 버튼 -> dialog 띄움
        deleteButton.addActionListener(new DeleteListener(book));

        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(new JPanel(), BorderLayout.NORTH); // 빈 공간 추가 (위쪽 여백)
        add(confirmPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocation(950, 300);
        setSize(300, 150);
    }

    public class DeleteListener implements ActionListener { // 삭제 버튼 리스너
        BookInfo book;

        public DeleteListener(BookInfo book) {
            this.book = book;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // 확인 다이얼로그 띄우기
            ConfirmTaskDialog confirmTaskDialog = new ConfirmTaskDialog("삭제");
            confirmTaskDialog.setVisible(true);

            // 대기 스레드 실행
            DeleteListener.WaitingThread waitingThread = new DeleteListener.WaitingThread(confirmTaskDialog);
            waitingThread.start();
        }

        private class WaitingThread extends Thread {
            private ConfirmTaskDialog confirmTaskDialog;

            public WaitingThread(ConfirmTaskDialog confirmTaskDialog) {
                this.confirmTaskDialog = confirmTaskDialog;
            } // 대기 스레드 생성자

            @Override
            public void run() { // 스레드 실행
                // dialog가 닫힐 때까지 기다림
                while (confirmTaskDialog.isVisible()) {
                    try {
                        Thread.sleep(100); // 다이얼로그가 닫힐 때까지 대기
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                // dialog에서 완료 버튼 눌렀을 때만 수행
                if (confirmTaskDialog.isConfirmed()) {
                    BookCSVController bookCsvController = new BookCSVController(); // CSV 컨트롤러 생성
                    bookCsvController.deleteBook(bookList, book, model, row); // 책 삭제

                    dispose();

                    // 완료 다이얼로그 띄우기
                    JOptionPane optionPane = new JOptionPane("도서 삭제가 완료되었습니다.", JOptionPane.INFORMATION_MESSAGE);
                    JDialog dialog = optionPane.createDialog("도서 삭제 완료");
                    dialog.setLocation(950, 300);
                    dialog.setVisible(true);
                }
            }
        }
    }
}
