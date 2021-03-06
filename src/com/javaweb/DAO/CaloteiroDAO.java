package com.javaweb.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import com.javaweb.bean.Caloteiro;
import com.javaweb.jdbc.ConnectionFactory;
import com.mysql.jdbc.PreparedStatement;

public class CaloteiroDAO {
	
	/*Singleton Pattern*/
	private static CaloteiroDAO instance;
	private static Connection connection;
	private CaloteiroDAO(){
		connection = ConnectionFactory.getInstance().getConnection();
	}
	public static CaloteiroDAO getInstance(){
		if(instance == null) { instance = new CaloteiroDAO();} 
		else{
			try {
				if(connection.isClosed()) { connection = ConnectionFactory.getInstance().getConnection();}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	public boolean add(Caloteiro caloteiro){
		try {
			PreparedStatement stmt = (PreparedStatement) connection.prepareStatement("Insert into Caloteiro (nome, email, devendo, dataDivida) values (?, ?, ?, ?)");
			stmt.setString(1, caloteiro.getNome());
			stmt.setString(2, caloteiro.getEmail());
			stmt.setInt(3, caloteiro.getDevendo());
			stmt.setDate(4, new Date(caloteiro.getData().getTimeInMillis()));
			stmt.execute();
			stmt.close();
			connection.close();
			return true;
		} catch (Exception e) {
			System.out.println("Erro ao gravar objeto do tipo Caloteiro: " + e.getMessage());
		} return false;
	}
	
	public ArrayList<Caloteiro> getList(){
		ArrayList<Caloteiro> caloteiros = new ArrayList<>();
		
		try {
			PreparedStatement stmt = (PreparedStatement) connection.prepareStatement("Select * from Caloteiro");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Caloteiro caloteiro = new Caloteiro();
				caloteiro.setNome(rs.getString("nome"));
				caloteiro.setEmail(rs.getString("email"));
				caloteiro.setId(rs.getInt("id"));
				caloteiro.setDevendo(rs.getInt("devendo"));
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(rs.getDate("dataDivida"));
				caloteiro.setData(calendar);
				
				caloteiros.add(caloteiro);
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			System.out.println("Erro ao tentar listar caloteiros: " + e.getMessage());
			e.printStackTrace();
		}
		
		return caloteiros;
	}
	
	public Caloteiro getCaloteiro(int id){
		Caloteiro caloteiro = new Caloteiro();
		try {
			PreparedStatement stmt = (PreparedStatement) connection.prepareStatement("Select * from Caloteiro where id=?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();	
			if(rs.isBeforeFirst()){
				rs.next();
				caloteiro.setNome(rs.getString("nome"));
				caloteiro.setEmail(rs.getString("email"));
				caloteiro.setId(rs.getInt("id"));
				caloteiro.setDevendo(rs.getInt("devendo"));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(rs.getDate("dataDivida"));
				caloteiro.setData(calendar);
				return caloteiro;
			}
			return null;
		} catch (Exception e) {
			System.out.println("Erro ao tentar pegar o objeto Caloteiro de id " + id);
			e.printStackTrace();
		}
		return null;
	}
	
	public void remove(int id){
		try {
			PreparedStatement stmt = (PreparedStatement) connection.prepareStatement("Delete from Caloteiro where id=?;");
			stmt.setInt(1, id);
			stmt.execute();
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Erro ao tentar remover caloteiro: ");
			e.printStackTrace();
		}
	}
	
	public boolean update(int id, Caloteiro caloteiro){
		try {
			PreparedStatement stmt = (PreparedStatement) connection.prepareStatement("Update Caloteiro set "+
					"nome=?, email=?, devendo=?, dataDivida=? where id=?;");
			stmt.setString(1, caloteiro.getNome());
			stmt.setString(2, caloteiro.getEmail());
			stmt.setInt(3, caloteiro.getDevendo());
			stmt.setDate(4, new Date(caloteiro.getData().getTimeInMillis()));
			stmt.setInt(5, id);
			
			stmt.execute();
			stmt.close();
			connection.close();
			return true;
		} catch (SQLException e) {
			System.out.println("Erro ao tentar atualizar caloteiro: ");
			e.printStackTrace();
		} return false;
	}
}
