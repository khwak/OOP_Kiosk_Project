package LibraryManagerDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfirmTaskDialog extends JDialog {
    private boolean isConfirmed;
    public ConfirmTaskDialog(String task) {
        setTitle("작업 확인");
        setConfirmed(false);

        Color green = new Color(0x00469C76);
        Color orange = new Color(0x00EE7930);

        Font btnFont = new Font("맑은 고딕", Font.BOLD, 18);
        Font labelFont = new Font("맑은 고딕", Font.BOLD, 16);

        JPanel confirmPanel=new JPanel();
        JLabel confirmLabel = new JLabel("도서 "+task+"을(를) 완료하시겠습니까?");
        confirmLabel.setFont(labelFont);
        confirmPanel.add(confirmLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        JButton yesButton = new JButton("완료");
        yesButton.setBackground(green);
        yesButton.setForeground(Color.WHITE);
        yesButton.setFont(btnFont);
        JButton noButton = new JButton("취소");
        noButton.setBackground(orange);
        noButton.setForeground(Color.WHITE);
        noButton.setFont(btnFont);

        yesButton.addActionListener(e -> {
            setConfirmed(true);
            dispose();
        });

        noButton.addActionListener(e-> {dispose();});

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        add(new JPanel(), BorderLayout.NORTH); // 빈 공간 추가 (위쪽 여백)
        add(confirmPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocation(950, 300);
        setSize(300, 150);
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }
}
