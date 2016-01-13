package com.coretree.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.coretree.sql.DBConnection;

public class DBConnectionTest {

	private static final String QUERY = "select username, password, uname, posi from users";
	
	public static void main(String[] args) {
		try(Connection con = DBConnection.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(QUERY)) {	
			
			while(rs.next()){
				String id = rs.getString("username");
				String name = rs.getString("password");
				String email = rs.getString("uname");
				String country = rs.getString("posi");
				System.out.println(id + ", " +name+ ", " +email+ ", " +country);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
