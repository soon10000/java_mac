package com.itwill.project01.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.itwill.project01.controller.ProjectDao;
import com.itwill.project01.model.Project;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.awt.FlowLayout;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JEditorPane;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.*;

import static com.itwill.project01.jdbc.OracleJdbc.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

public class ProjectCreateFrame extends JFrame {

	public interface CreateNotify {
		void notifyCreateSuccess();
	}

	private static final String CATEGORI_NAME[] = { "한식", "중식", "일식", "양식", "퓨전", "고기", "회", "카페", "기타" };

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textStore;
	private JLabel lblSaveName;
	private JTextField textAddress;
	private JTextField textMemo;
	private JPanel panel;
	private JLabel lblCategori;
	private JComboBox comboBoxCategori;
	private JComboBox comboBoxCity;
	private JLabel lblCity;
	private JComboBox comboBoxBorough;
	private JLabel lblBorough;
	private JLabel lblAddress;
	private JLabel lblMemo;
	private JPanel panel_1;
	private JRadioButton rdbtnVisiteYes;
	private JRadioButton rdbtnVisiteNo;
	private JLabel lblVisite;
	private JLabel lblMainTitle;

	private CreateNotify app;
	private ProjectDao dao = ProjectDao.getInstance();
	private Component parent;
	private int storeId;

	/**
	 * Launch the application.
	 */
	public static void showProjectCreateFrame(Component parent, CreateNotify app) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProjectCreateFrame frame = new ProjectCreateFrame(parent, app);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private ProjectCreateFrame(Component parent, CreateNotify app) {
		this.parent = parent;
		this.app = app;
		initialize();
	};

	/**
	 * Create the frame.
	 */
	public void initialize() {
		setTitle("맛집 추가");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		int x = 0;
		int y = 0;
		if (parent != null) {
			x = parent.getX();
			y = parent.getY();
		}
		setBounds(x, y, 401, 425);
		if (parent == null) {
			setLocationRelativeTo(null);
		}

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblMainTitle = new JLabel("맛집 등록");
		lblMainTitle.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lblMainTitle.setBounds(12, 10, 109, 15);
		contentPane.add(lblMainTitle);

		panel = new JPanel();
		panel.setBounds(12, 35, 361, 50);
		contentPane.add(panel);
		panel.setLayout(null);

		lblSaveName = new JLabel("맛집 이름");
		lblSaveName.setBounds(32, 8, 70, 35);
		lblSaveName.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		panel.add(lblSaveName);

		textStore = new JTextField();
		textStore.setBounds(115, 12, 216, 33);
		panel.add(textStore);
		textStore.setColumns(10);

		lblCategori = new JLabel("카테고리");
		lblCategori.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		lblCategori.setBounds(51, 95, 70, 35);
		contentPane.add(lblCategori);

		comboBoxCategori = new JComboBox();
		comboBoxCategori.setBounds(128, 102, 155, 26);
		DefaultComboBoxModel<String> comboBoxCategoriModel = new DefaultComboBoxModel<String>(CATEGORI_NAME);
		comboBoxCategori.setModel(comboBoxCategoriModel);
		contentPane.add(comboBoxCategori);

		comboBoxCity = new JComboBox();
		comboBoxCity.setBounds(128, 140, 155, 26);
		List<String> citis = dao.getCities();
		for (String s : citis) {
			comboBoxCity.addItem(s);
		}
		contentPane.add(comboBoxCity);

		lblCity = new JLabel("지역");
		lblCity.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		lblCity.setBounds(51, 133, 70, 35);
		contentPane.add(lblCity);

		comboBoxBorough = new JComboBox();
		comboBoxCity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedCity = (String) comboBoxCity.getSelectedItem();
				if (selectedCity != null) {
					List<String> boroughs = dao.getBoroughs(selectedCity);

					comboBoxBorough.removeAllItems();
					for (String s : boroughs) {
						comboBoxBorough.addItem(s);
					}
				}
			}
		});
		comboBoxBorough.setBounds(128, 183, 155, 26);
		contentPane.add(comboBoxBorough);

		lblBorough = new JLabel("구");
		lblBorough.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		lblBorough.setBounds(51, 176, 70, 35);
		contentPane.add(lblBorough);

		textAddress = new JTextField();
		textAddress.setColumns(10);
		textAddress.setBounds(95, 223, 278, 33);
		contentPane.add(textAddress);

		lblAddress = new JLabel("세부주소");
		lblAddress.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		lblAddress.setBounds(23, 221, 70, 35);
		contentPane.add(lblAddress);

		lblMemo = new JLabel("메모");
		lblMemo.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		lblMemo.setBounds(23, 266, 70, 35);
		contentPane.add(lblMemo);

		textMemo = new JTextField();
		textMemo.setColumns(10);
		textMemo.setBounds(95, 268, 278, 33);
		contentPane.add(textMemo);

		panel_1 = new JPanel();
		panel_1.setBounds(22, 311, 351, 30);
		contentPane.add(panel_1);

		lblVisite = new JLabel("방문 여부");
		lblVisite.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		panel_1.add(lblVisite);

		rdbtnVisiteYes = new JRadioButton("예");
		rdbtnVisiteYes.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		panel_1.add(rdbtnVisiteYes);

		rdbtnVisiteNo = new JRadioButton("아니오");
		rdbtnVisiteNo.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		panel_1.add(rdbtnVisiteNo);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbtnVisiteYes);
		buttonGroup.add(rdbtnVisiteNo);
		rdbtnVisiteNo.setSelected(true);

		btnSave = new JButton("저장하기");
		btnSave.addActionListener(e -> getSave());
		btnSave.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		btnSave.setBounds(78, 351, 100, 30);
		contentPane.add(btnSave);

		btnCancel = new JButton("취소");
		btnCancel.addActionListener(e -> dispose());
		btnCancel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		btnCancel.setBounds(205, 351, 100, 30);
		contentPane.add(btnCancel);
	}

	private void getSave() {
		try {
			String store = textStore.getText();
			String categori = (String) comboBoxCategori.getSelectedItem();
			String city = comboBoxCity.getSelectedItem().toString();
			String borough = comboBoxBorough.getSelectedItem().toString();
			String address = textAddress.getText();
			String memo = textMemo.getText();
			String userID = ProjectViewMain.getUserID();
			int visite = rdbtnVisiteYes.isSelected() ? 1 : 0;
			if (store.equals("") || categori.equals("") || city.equals("") || borough.equals("")) {
				JOptionPane.showMessageDialog(ProjectCreateFrame.this, "상호명, 카테고리, 지역, 구, 주소값은 필수입니다.", "경고",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			Project project = new Project(0, store, categori, city, borough, address, memo, visite, userID);
			int result = dao.create(project);

			if (result == 1) {
				JOptionPane.showMessageDialog(ProjectCreateFrame.this, "저장 성공", "성공", JOptionPane.PLAIN_MESSAGE);
				dispose();
			} else {
				JOptionPane.showMessageDialog(ProjectCreateFrame.this, "저장 실패", "실패", JOptionPane.ERROR_MESSAGE);
				dispose();
			}

			app.notifyCreateSuccess();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(ProjectCreateFrame.this, "모든 값이 입력되었는지 확인하세요.");
			e.printStackTrace();

		}

	}

	static final String SQL_SELECT_CITY = "SELECT CITY FROM ADDRESS";

	private JButton btnSave;
	private JButton btnCancel;
}
