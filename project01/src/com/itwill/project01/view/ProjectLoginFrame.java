package com.itwill.project01.view;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import com.itwill.project01.controller.ProjectDao;
import com.itwill.project01.model.ProjectAccount;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ProjectLoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblTitle;
	private JTextField textID;
	private JPasswordField textPassword;
	private JFrame frame;

	private ProjectDao dao = ProjectDao.getInstance();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProjectLoginFrame frame = new ProjectLoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ProjectLoginFrame() {
		setTitle("맛집 저장소 로그인");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(600, 300, 350, 272);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblTitle = new JLabel("맛집 저장소 로그인");
		lblTitle.setFont(new Font("Lucida Grande", Font.BOLD, 18));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(6, 6, 338, 60);
		contentPane.add(lblTitle);

		JLabel lblID = new JLabel("아이디");
		lblID.setHorizontalAlignment(SwingConstants.CENTER);
		lblID.setBounds(59, 78, 70, 30);
		contentPane.add(lblID);

		textID = new JTextField();
		textID.setHorizontalAlignment(SwingConstants.CENTER);
		textID.setBounds(144, 78, 160, 30);
		contentPane.add(textID);
		textID.setColumns(10);

		textPassword = new JPasswordField();
		textPassword.setHorizontalAlignment(SwingConstants.CENTER);
		textPassword.setColumns(10);
		textPassword.setBounds(144, 126, 160, 30);
		contentPane.add(textPassword);

		JLabel lblPassword = new JLabel("비밀번호");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(59, 126, 70, 30);
		contentPane.add(lblPassword);

		JButton btnLogin = new JButton("로그인");
		btnLogin.addActionListener(e -> login());
		btnLogin.setBounds(46, 181, 117, 29);
		contentPane.add(btnLogin);

		JButton btnCreate = new JButton("회원가입");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProjectCreateAccount.showCreateAccount(frame);
			}
		});
		btnCreate.setBounds(187, 181, 117, 29);
		contentPane.add(btnCreate);
	}

	private void login() {
		String userID = textID.getText();
		String password = new String(textPassword.getPassword());

		ProjectAccount account = dao.accountLogin(userID);

		if (account == null) {
			JOptionPane.showMessageDialog(frame, "등록되지 않은 정보입니다.");
		} else {
			if (account.getPassword().equals(password)) {
				JOptionPane.showMessageDialog(frame, "로그인 성공");
				ProjectViewMain.show(userID, ProjectLoginFrame.this);
				dispose();
			} else {
				JOptionPane.showMessageDialog(frame, "비밀번호가 일치하지 않습니다.");
			}
		}

	}

}
