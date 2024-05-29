package com.itwill.project01.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import com.itwill.project01.controller.ProjectDao;

import com.itwill.project01.model.ProjectAccount;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import com.itwill.project01.jdbc.OracleJdbc;

public class ProjectCreateAccount extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textGetId;
	private JPasswordField textPassword;
	private JPasswordField textPasswordCheck;
	private JLabel lblPassword;
	private JButton btnCheckId;
	private JLabel lblCreateId;
	private JLabel lblCreateAccount;
	private JTextField textGetEmail;
	private JLabel lblEmail;
	private JButton btnCreate;
	private JButton btnClose;

	private Component parent;
	private ProjectDao dao = ProjectDao.getInstance();
	private JButton btnCheckEmail;

	/**
	 * Launch the application.
	 */
	public static void showCreateAccount(Component parent) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProjectCreateAccount frame = new ProjectCreateAccount(parent);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private ProjectCreateAccount(Component parent) {
		this.parent = parent;
		initialize();
	}

	/**
	 * Create the frame.
	 */
	public void initialize() {
		setTitle("회원가입");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		int x = 0;
		int y = 0;
		if (parent != null) {
			x = parent.getX();
			y = parent.getY();
		}
		setBounds(x, y, 300, 300);
		if (parent == null) {
			setLocationRelativeTo(null);
		}
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblCreateAccount = new JLabel("회원가입");
		lblCreateAccount.setBounds(5, 5, 290, 20);
		lblCreateAccount.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		lblCreateAccount.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblCreateAccount);

		lblCreateId = new JLabel("아이디");
		lblCreateId.setHorizontalAlignment(SwingConstants.CENTER);
		lblCreateId.setBounds(15, 37, 61, 16);
		contentPane.add(lblCreateId);

		textGetId = new JTextField();
		textGetId.setBounds(93, 32, 130, 26);
		contentPane.add(textGetId);
		textGetId.setColumns(10);

		btnCheckId = new JButton("중복확인");
		btnCheckId.addActionListener(e -> checkID());
		btnCheckId.setBounds(225, 32, 70, 29);
		contentPane.add(btnCheckId);

		lblPassword = new JLabel("비밀번호");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(15, 75, 61, 16);
		contentPane.add(lblPassword);

		textPassword = new JPasswordField();
		textPassword.setColumns(10);
		textPassword.setBounds(93, 70, 130, 26);
		contentPane.add(textPassword);

		textPasswordCheck = new JPasswordField();
		textPasswordCheck.setColumns(10);
		textPasswordCheck.setBounds(93, 111, 130, 26);
		contentPane.add(textPasswordCheck);

		JLabel lblPasswordCheck = new JLabel("비밀번호 확인");
		lblPasswordCheck.setHorizontalAlignment(SwingConstants.CENTER);
		lblPasswordCheck.setBounds(15, 116, 79, 16);
		contentPane.add(lblPasswordCheck);

		lblEmail = new JLabel("이메일");
		lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmail.setBounds(15, 157, 61, 16);
		contentPane.add(lblEmail);

		textGetEmail = new JTextField();
		textGetEmail.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
		textGetEmail.setColumns(10);
		textGetEmail.setBounds(93, 152, 130, 26);
		contentPane.add(textGetEmail);

		btnCreate = new JButton("생성");
		btnCreate.addActionListener(e -> createID());
		btnCreate.setBounds(26, 204, 117, 29);
		contentPane.add(btnCreate);

		btnClose = new JButton("닫기");
		btnClose.addActionListener(e -> dispose());
		btnClose.setBounds(155, 204, 117, 29);
		contentPane.add(btnClose);

		btnCheckEmail = new JButton("중복확인");
		btnCheckEmail.addActionListener(e -> checkEmail());
		btnCheckEmail.setBounds(225, 152, 70, 29);
		contentPane.add(btnCheckEmail);
	}

	private void checkEmail() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String email = textGetEmail.getText();
			String SQL = "SELECT COUNT(*) FROM ACCOUNT WHERE EMAIL = ?";
			conn = DriverManager.getConnection(OracleJdbc.URL, OracleJdbc.USER, OracleJdbc.PASSWORD);
			stmt = conn.prepareStatement(SQL);
			stmt.setString(1, email);
			rs = stmt.executeQuery();
			if (email.equals("")) {
				JOptionPane.showMessageDialog(parent, "이메일을 입력하세요");
				return;
			}
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					JOptionPane.showMessageDialog(parent, "중복된 이메일이 존재합니다.");
				} else {
					JOptionPane.showMessageDialog(parent, "사용 가능한 이메일 입니다.");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dao.closeResources(conn, stmt, rs);
		}
	}

	private void createID() {
		String userID = textGetId.getText();
		String password = new String(textPassword.getPassword());
		String email = textGetEmail.getText();
		String passwordCheck = new String(textPasswordCheck.getPassword());

		// 비밀번호와 비밀번호 확인이 일치하는지 확인
		if (!password.equals(passwordCheck)) {
			JOptionPane.showMessageDialog(parent, "비밀번호가 일치하지 않습니다.");
			return;
		}

		// 아이디 중복 검사
		if (isIdDuplicated(userID)) {
			JOptionPane.showMessageDialog(parent, "이미 사용 중인 아이디입니다.");
			return;
		}

		// 이메일 중복 검사
		if (isEmailDuplicated(email)) {
			JOptionPane.showMessageDialog(parent, "이미 사용 중인 이메일입니다.");
			return;
		}

		// 모든 조건을 만족하면 계정 생성
		ProjectAccount newAccount = new ProjectAccount(userID, password, email);
		dao.insertAccount(newAccount); // 데이터베이스에 삽입

		JOptionPane.showMessageDialog(parent, "계정이 생성되었습니다.");
		dispose(); // 회원가입 창 닫기
	}
	
	

	private boolean isEmailDuplicated(String email) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean duplicated = false;

		try {
			conn = DriverManager.getConnection(OracleJdbc.URL, OracleJdbc.USER, OracleJdbc.PASSWORD);
			String SQL = "SELECT COUNT(*) FROM ACCOUNT WHERE EMAIL = ?";
			stmt = conn.prepareStatement(SQL);
			stmt.setString(1, email);
			rs = stmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				duplicated = (count > 0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dao.closeResources(conn, stmt, rs);
		}

		return duplicated;

	}

	private boolean isIdDuplicated(String id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean duplicated = false;

		try {
			conn = DriverManager.getConnection(OracleJdbc.URL, OracleJdbc.USER, OracleJdbc.PASSWORD);
			String SQL = "SELECT COUNT(*) FROM ACCOUNT WHERE USERID = ?";
			stmt = conn.prepareStatement(SQL);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				duplicated = (count > 0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dao.closeResources(conn, stmt, rs);
		}

		return duplicated;
	}

	private void checkID() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String id = textGetId.getText();
			String SQL = "SELECT COUNT(*) FROM ACCOUNT WHERE USERID = ?";
			conn = DriverManager.getConnection(OracleJdbc.URL, OracleJdbc.USER, OracleJdbc.PASSWORD);
			stmt = conn.prepareStatement(SQL);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			if (id.equals("")) {
				JOptionPane.showMessageDialog(parent, "아이디를 입력하세요");
				return;
			}
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					JOptionPane.showMessageDialog(parent, "중복된 아이디가 존재합니다.");
					return;
				} else {
					JOptionPane.showMessageDialog(parent, "사용 가능한 아이디입니다.");
					return;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dao.closeResources(conn, stmt, rs);
		}
	}

}
