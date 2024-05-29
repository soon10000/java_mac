package com.itwill.project01.controller;

import static com.itwill.project01.jdbc.OracleJdbc.PASSWORD;
import static com.itwill.project01.jdbc.OracleJdbc.URL;
import static com.itwill.project01.jdbc.OracleJdbc.USER;
import static com.itwill.project01.model.Project.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

import com.itwill.project01.model.Project;
import com.itwill.project01.model.ProjectAccount;
import com.itwill.project01.view.ProjectCreateAccount;

import oracle.jdbc.driver.AbstractShardingPreparedStatement;
import oracle.jdbc.driver.OracleDriver;

public class ProjectDao {

	private static ProjectDao instance = null;

	public static ProjectDao getInstance() { // 싱글톤 객체 생성
		if (instance == null) {
			instance = new ProjectDao();
		}
		return instance;
	}

	private ProjectDao() {
		try {
			DriverManager.registerDriver(new OracleDriver());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static final String SQL_SELECT_CITY = "SELECT DISTINCT CITY FROM ADDRESS";

	public List<String> getCities() { // 첫번째 콤보박스 목록
		List<String> cities = new ArrayList<>();

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_SELECT_CITY);
			rs = stmt.executeQuery();

			while (rs.next()) {
				String city = rs.getString("CITY");
				cities.add(city);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt, rs);
		}

		return cities;
	}

	public void closeResources(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeResources(Connection conn, Statement stmt) {
		try {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static final String SQL_SELECT_CITYTWO = "SELECT BOROUGH FROM ADDRESS WHERE CITY = ? ORDER BY BOROUGH"; // 두번째
																													// 콤보박스
																													// 목록

	public List<String> getBoroughs(String city) { // 두번째 콤보박스 목록
		List<String> boroughs = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_SELECT_CITYTWO);
			stmt.setString(1, city);
			rs = stmt.executeQuery();

			while (rs.next()) {
				String borough = rs.getString("BOROUGH");
				boroughs.add(borough);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt, rs);
		}

		return boroughs;
	}

	private static final String SQL_INSERT = "INSERT INTO INFO (STORE, CATEGORI, CITY, BOROUGH, ADDRESS, MEMO, VISITE, USERID) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";

	public int create(Project project) { // 맛집 저장 메서
		int result = 0;
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_INSERT);
			stmt.setString(1, project.getStore());
			stmt.setString(2, project.getCategori());
			stmt.setString(3, project.getCity());
			stmt.setString(4, project.getBorough());
			stmt.setString(5, project.getAddress());
			stmt.setString(6, project.getMemo());
			stmt.setInt(7, project.getVisite());
			stmt.setString(8, project.getUserID());

			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt);
		}
		return result;

	}

	private static final String SQL_SELECT_ALL = "SELECT * FROM INFO WHERE USERID = ? ORDER BY USERID";

	public List<Project> allRead(String userID) {
		List<Project> result = new ArrayList<Project>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_SELECT_ALL);
			stmt.setString(1, userID);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Project project = makeProjectFromResultSet(rs);
				result.add(project);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt, rs);
		}
		return result;
	}

	private Project makeProjectFromResultSet(ResultSet rs) {
		try {
			int id = rs.getInt(COL_ID);
			String store = rs.getString(COL_STORE);
			String categori = rs.getString(COL_CATEGORI);
			String city = rs.getString(COL_CITY);
			String borough = rs.getString(COL_BOROUGH);
			String address = rs.getString(COL_ADDRESS);
			String memo = rs.getString(COL_MEMO);
			int visite = rs.getInt(COL_VISITE);
			String userID = rs.getString(COL_USERID);

			Project project = new Project(id, store, categori, city, borough, address, memo, visite, userID);
			return project;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static final String SQL_SELECT_BY_STORE = "SELECT * FROM INFO WHERE ID = ?";

	public Project details(int storeId) { // 맛집 수정 메서
		Project project = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_SELECT_BY_STORE);
			stmt.setInt(1, storeId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				project = makeProjectFromResultSet(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt, rs);
		}
		return project;

	}

	private static final String SQL_UPDATE = "UPDATE INFO SET STORE = ?, CATEGORI = ?, CITY = ?, BOROUGH = ?, ADDRESS = ?, MEMO = ?, VISITE = ? WHERE ID = ?";

	public int update(Project project) {
		int result = 0;

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_UPDATE);
			stmt.setString(1, project.getStore());
			stmt.setString(2, project.getCategori());
			stmt.setString(3, project.getCity());
			stmt.setString(4, project.getBorough());
			stmt.setString(5, project.getAddress());
			stmt.setString(6, project.getMemo());
			stmt.setInt(7, project.getVisite());
			stmt.setInt(8, project.getId());

			result = stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt);
		}
		return result;

	}

	private static final String SQL_DELETE = "DELETE FROM INFO WHERE ID = ?";

	public int delete(int id) {
		int result = 0;
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_DELETE);
			stmt.setInt(1, id);
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt);
		}
		return result;
	}

	// 콤보박스 카테고리에 따른 검색어 입력 문장
	private static final String SQL_SEARCH_CATE = "SELECT * FROM INFO WHERE lower (CATEGORI) like ? AND USERID = ? order by ID desc";
	private static final String SQL_SEARCH_STORE = "SELECT * FROM INFO WHERE lower (STORE) like ? AND USERID = ? order by ID desc";
	private static final String SQL_SEARCH_CITY = "SELECT * FROM INFO WHERE lower (CITY) like ? AND USERID = ? order by ID desc";
	private static final String SQL_SEARCH_BOROUGH = "SELECT * FROM INFO WHERE lower (BOROUGH) like ? AND USERID = ? order by ID desc";

	public List<Project> search(int type, String keyword, String userID) {
		List<Project> result = new ArrayList<Project>();

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String searchKeyword = "%" + keyword.toLowerCase() + "%";

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			switch (type) {
			case 0:
				stmt = conn.prepareStatement(SQL_SEARCH_CATE);
				stmt.setString(1, searchKeyword);
				stmt.setString(2, userID);
				break;
			case 1:
				stmt = conn.prepareStatement(SQL_SEARCH_STORE);
				stmt.setString(1, searchKeyword);
				stmt.setString(2, userID);
				break;
			case 2:
				stmt = conn.prepareStatement(SQL_SEARCH_CITY);
				stmt.setString(1, searchKeyword);
				stmt.setString(2, userID);
				break;
			case 3:
				stmt = conn.prepareStatement(SQL_SEARCH_BOROUGH);
				stmt.setString(1, searchKeyword);
				stmt.setString(2, userID);
				break;
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				Project project = makeProjectFromResultSet(rs);
				result.add(project);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt, rs);
		}
		return result;

	}

	public void insertAccount(ProjectAccount account) {
		String SQL = "INSERT INTO ACCOUNT (USERID, PASSWORD, EMAIL) VALUES (?, ?, ?)";
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL);
			stmt.setString(1, account.getUserID());
			stmt.setString(2, account.getPassword());
			stmt.setString(3, account.getEmail());

			int rowsInserted = stmt.executeUpdate();

			if (rowsInserted > 0) {
				System.out.println("새로운 계정이 성공적으로 추가되었습니다.");
			} else {
				System.out.println("계정 추가에 실패했습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt);
		}

	}

	public ProjectAccount accountLogin(String userID) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ProjectAccount account = null;
		String SQL = "SELECT * FROM ACCOUNT WHERE USERID = ?";
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL);
			stmt.setString(1, userID);
			rs = stmt.executeQuery();

			if (rs.next()) {
				String dbuserID = rs.getString("USERID");
				String password = rs.getString("PASSWORD");
				String email = rs.getString("EMAIL");

				account = new ProjectAccount(dbuserID, password, email);
			}
		} catch (Exception e) {
            e.printStackTrace();

		} finally {
			closeResources(conn, stmt, rs);
		}

		return account;
	}

}
