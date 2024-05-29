package com.itwill.project01.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

import com.itwill.project01.controller.ProjectDao;
import com.itwill.project01.model.Project;
import com.itwill.project01.view.ProjectCreateFrame;
import com.itwill.project01.view.ProjectCreateFrame.CreateNotify;
import com.itwill.project01.view.ProjectDetailsFrame;
import com.itwill.project01.view.ProjectDetailsFrame.UpdateNotify;

public class ProjectViewMain implements CreateNotify, UpdateNotify {
	private static final String[] SEARCH_TYPES = { "카테고리", "상호명", "지역", "세부지역" }; // 콤보박스 보기
	private static final String[] COLUMB_TYPES = { "번호", "카테고리", "상호명", "지역", "세부지역", "방문여부" }; // 컬럼 목록 보기

	private JFrame frame;
	private JLabel lblMainText;
	private JPanel ButtonPanel;
	private JButton btnEdit;
	private JButton btnDelete;
	private JPanel panel;
	private JComboBox comboBox;
	private JTextField textField;
	private JLabel lblSearch;
	private JTable table;
	private JScrollPane scrollPane;

	private ProjectDao dao = ProjectDao.getInstance();
	private DefaultTableModel tableModel;
	private JButton btnAllList;
	private JButton btnClose;
	private static String userID;
	private ProjectLoginFrame app;

	/**
	 * Launch the application.
	 */
	public static void show(String userID, ProjectLoginFrame app) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProjectViewMain window = new ProjectViewMain(userID, app);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ProjectViewMain(String userID, ProjectLoginFrame app) {
		this.app = app;
		this.userID = userID;
		initialize();
		initializeTable();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	public void initializeTable() {
		List<Project> project = dao.allRead(userID);
		resetTable(project);
	}

	private void resetTable(List<Project> project) {
		tableModel = new DefaultTableModel(null, COLUMB_TYPES);
		for (Project s : project) {
			String visiteYesNo;
			if (s.getVisite() == 1) {
				visiteYesNo = "YES";
			} else {
				visiteYesNo = "NO";
			}
			Object[] row = { s.getId(), s.getCategori(), s.getStore(), s.getCity(), s.getBorough(), visiteYesNo };
			tableModel.addRow(row);
		}
		table.setModel(tableModel);
	}

	private void initialize() {
		frame = new JFrame();
		int x = 0;
		int y = 0;
		if(app != null) {
			x = app.getX();
			y = app.getY();
		}
		frame.setBounds(x, y ,450, 600);
		
		if (app == null) {
			frame.setLocationRelativeTo(null);
		}
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("맛집 저장소");

		lblMainText = new JLabel("TASTY RESTAURANT GUIDE");
		lblMainText.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		lblMainText.setHorizontalAlignment(SwingConstants.CENTER);
		lblMainText.setBounds(12, 10, 410, 60);
		frame.getContentPane().add(lblMainText);

		ButtonPanel = new JPanel();
		ButtonPanel.setBounds(12, 57, 410, 40);
		frame.getContentPane().add(ButtonPanel);
		ButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton btnCreate = new JButton("신규 추가");
		btnCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ProjectCreateFrame.showProjectCreateFrame(frame, ProjectViewMain.this);
			}
		});
		btnCreate.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		ButtonPanel.add(btnCreate);

		btnEdit = new JButton("수정 및 상세보기");
		btnEdit.addActionListener(e -> showDetailFrame());
		btnEdit.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		ButtonPanel.add(btnEdit);

		btnDelete = new JButton("삭제");
		btnDelete.addActionListener(e -> deleteInfo());
		btnDelete.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		ButtonPanel.add(btnDelete);

		panel = new JPanel();
		panel.setBounds(12, 98, 410, 40);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		comboBox = new JComboBox();
		comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		DefaultComboBoxModel<String> comboBoxMedel = new DefaultComboBoxModel<>(SEARCH_TYPES);
		comboBox.setModel(comboBoxMedel);
		comboBox.setBounds(75, 3, 98, 30);
		panel.add(comboBox);

		textField = new JTextField();
		textField.setBounds(185, 0, 151, 34);
		panel.add(textField);
		textField.setColumns(10);

		lblSearch = new JLabel("검색");
		lblSearch.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		lblSearch.setHorizontalAlignment(SwingConstants.CENTER);
		lblSearch.setBounds(16, 3, 60, 30);
		panel.add(lblSearch);

		JButton btnSearch = new JButton("찾기");
		btnSearch.addActionListener(e -> search());
		btnSearch.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		btnSearch.setBounds(344, 4, 60, 29);
		panel.add(btnSearch);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 148, 410, 360);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		table.setFont(new Font("굴림", Font.BOLD, 12));
		DefaultTableModel tableModel = new DefaultTableModel(null, COLUMB_TYPES);
		table.setModel(tableModel);
//		table.setEnabled(false);
		scrollPane.setViewportView(table);

		btnAllList = new JButton("목록보기");
		btnAllList.addActionListener(e -> initializeTable());
		btnAllList.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		btnAllList.setBounds(51, 524, 117, 29);
		frame.getContentPane().add(btnAllList);

		btnClose = new JButton("로그아웃");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int x = JOptionPane.showConfirmDialog(frame, "정말 로그아웃 하시겠습니까?", "로그아웃 확인", JOptionPane.YES_NO_OPTION);
				System.out.println(x);
				if (x == JOptionPane.YES_OPTION) {
					ProjectLoginFrame.main(null);
					frame.dispose();
				}

			}

		});
		btnClose.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		btnClose.setBounds(264, 524, 117, 29);
		frame.getContentPane().add(btnClose);
	}

	private void deleteInfo() {
		int index = table.getSelectedRow();
		if (index == -1) {
			JOptionPane.showConfirmDialog(frame, "삭제할 행을 선택해주세요.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(frame, "정말 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			Integer id = (Integer) tableModel.getValueAt(index, 0);
			int result = dao.delete(id);
			if (result == 1) {
				initializeTable();
				JOptionPane.showMessageDialog(frame, "삭제가 완료되었습니다.");
			} else {
				JOptionPane.showMessageDialog(frame, "삭제 실패");
			}
		}

	}

	private void showDetailFrame() {
		int index = table.getSelectedRow();
		if (index == -1) {
			JOptionPane.showMessageDialog(frame, "상세보기 및 수정할 행을 먼저 선택하세요.", "확인", JOptionPane.QUESTION_MESSAGE);
			return;
		} else {
			Integer id = (Integer) tableModel.getValueAt(index, 0);
			ProjectDetailsFrame.showProjectDetailsFrame(frame, id, ProjectViewMain.this);
		}
	}

	public void notifyCreateSuccess() {
		initializeTable();
	}

	@Override
	public void notifyUpdateSuccess() {
		initializeTable();
	}

	private void search() {
		int type = comboBox.getSelectedIndex();
		String keyword = textField.getText();
		if (keyword.equals("")) {
			JOptionPane.showMessageDialog(frame, "검색어를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
			textField.requestFocus();
			return;
		}
		List<Project> project = dao.search(type, keyword, userID);
		resetTable(project);
	}

	public static String getUserID() {
		return userID;
	}

}
