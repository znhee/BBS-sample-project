package taro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class TaroDao {
	public static Connection getConnection() {
		Connection conn;
		try {
			Context initContext = new InitialContext();
			DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/project");
			conn = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return conn;
	}
	
	public List<Taro> listQues() {
		Connection conn = getConnection();
		String sql = "SELECT * FROM taro ORDER BY qNo;";
		List<Taro> list = new ArrayList<>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Taro taro = new Taro();
				taro.setqNo(rs.getInt(1));
				taro.setContent(rs.getString(2));
				taro.setAns1(rs.getString(3));
				taro.setAns2(rs.getString(4));
				list.add(taro);
			}
			rs.close(); stmt.close(); conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Taro getQuesInfo(int qNo) {
		Connection conn = getConnection();
		String sql = "SELECT * FROM taro WHERE qNo=?;";
		Taro taro = new Taro();
		try {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, qNo);
			
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				taro.setqNo(rs.getInt(1));
				taro.setContent(rs.getString(2));
				taro.setAns1(rs.getString(3));
				taro.setAns2(rs.getString(4));
			}
			rs.close(); pStmt.close(); conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return taro;
	}
}
