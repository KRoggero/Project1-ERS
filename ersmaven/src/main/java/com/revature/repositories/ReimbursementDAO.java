package com.revature.repositories;

import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.UserService;
import com.revature.util.ConnectionFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ReimbursementDAO {
	AuthService authServ = new AuthService();

	/**
	 * Should retrieve a Reimbursement from the DB with the corresponding id or an empty optional if there is no match.
	 */
	public Optional<Reimbursement> getById(int id) {
		Reimbursement reimbursement = new Reimbursement();
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from reimbursement_request where id" + id);
			if (rs.next()) {
				reimbursement.setId(rs.getInt("id"));
				reimbursement.setAmount(rs.getDouble("amount"));
				reimbursement.setAuthor(rs.getInt("author"));
				reimbursement.setResolver(rs.getInt("resolver"));
				if (rs.getString("status").equalsIgnoreCase("pending")) {
					reimbursement.setStatus(Status.PENDING);
				} else if (rs.getString("status").equalsIgnoreCase("denied")) {
					reimbursement.setStatus(Status.DENIED);
				} else if (rs.getString("status").equalsIgnoreCase("approved")) {
					reimbursement.setStatus(Status.APPROVED);
				}
				reimbursement.setCreationDate(rs.getDate("creation_date"));
				reimbursement.setResolutionDate(rs.getDate("resolution_date"));
				reimbursement.setReceipt((InputStream) rs.getObject("receipt_image"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Optional.of(reimbursement);
	}

	/**
	 * Should retrieve a List of Reimbursements from the DB with the corresponding Status or an empty List if there are no matches.
	 */
	public List<Reimbursement> getByStatus(Status status) {
		List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from reimbursement_request where status=" + status);
			while (rs.next()) {
				Reimbursement reimbursement = new Reimbursement();
				reimbursement.setId(rs.getInt("id"));
				reimbursement.setAmount(rs.getDouble("amount"));
				reimbursement.setAuthor(rs.getInt("author"));
				reimbursement.setResolver(rs.getInt("resolver"));
				if (rs.getString("status").equalsIgnoreCase("pending")) {
					reimbursement.setStatus(Status.PENDING);
				} else if (rs.getString("status").equalsIgnoreCase("denied")) {
					reimbursement.setStatus(Status.DENIED);
				} else if (rs.getString("status").equalsIgnoreCase("approved")) {
					reimbursement.setStatus(Status.APPROVED);
				}
				reimbursement.setCreationDate(rs.getDate("creation_date"));
				reimbursement.setResolutionDate(rs.getDate("resolution_date"));
				reimbursement.setReceipt((InputStream) rs.getObject("receipt_image"));
				reimbursementList.add(reimbursement);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reimbursementList;
	}

	/**
	 * <ul>
	 *     <li>Should Update an existing Reimbursement record in the DB with the provided information.</li>
	 *     <li>Should throw an exception if the update is unsuccessful.</li>
	 *     <li>Should return a Reimbursement object with updated information.</li>
	 * </ul>
	 */
	public List<Reimbursement> getAllReimbursements() {
		List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();

		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from reimbursement_request");
			while (rs.next()) {
				Reimbursement reimbursement = new Reimbursement();
				reimbursement.setId(rs.getInt("id"));
				reimbursement.setAmount(rs.getDouble("amount"));
				reimbursement.setAuthor(rs.getInt("author"));
				reimbursement.setResolver(rs.getInt("resolver"));
				if (rs.getString("status").equalsIgnoreCase("pending")) {
					reimbursement.setStatus(Status.PENDING);
				} else if (rs.getString("status").equalsIgnoreCase("denied")) {
					reimbursement.setStatus(Status.DENIED);
				} else if (rs.getString("status").equalsIgnoreCase("approved")) {
					reimbursement.setStatus(Status.APPROVED);
				}
				reimbursement.setCreationDate(rs.getDate("creation_date"));
				reimbursement.setResolutionDate(rs.getDate("resolution_date"));
				reimbursement.setReceipt((InputStream) rs.getObject("receipt_image"));
				reimbursementList.add(reimbursement);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return reimbursementList;
	}

	public Reimbursement update(Reimbursement unprocessedReimbursement) {
		Reimbursement reimbursement = new Reimbursement();
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			String updateQ = "UPDATE reimbursement_request SET resolver = ?, status = ?, resolution_date = CURRENT_TIMESTAMP  WHERE id=?";
			PreparedStatement pstmt = conn.prepareStatement(updateQ);
			pstmt.setInt(1, unprocessedReimbursement.getResolver());
			pstmt.setString(2, unprocessedReimbursement.getStatus().toString());
			pstmt.setInt(3, unprocessedReimbursement.getId());
			pstmt.execute();

			ResultSet rs = conn.createStatement()
					.executeQuery("select * from reimbursement_request where id=" +unprocessedReimbursement.getId());
			if (rs.next()) {
				reimbursement.setId(rs.getInt("id"));
				reimbursement.setAmount(rs.getDouble("amount"));
				reimbursement.setAuthor(rs.getInt("author"));
				reimbursement.setResolver(rs.getInt("resolver"));
				reimbursement.setDescription(rs.getString("description"));
				reimbursement.setCreationDate(rs.getDate("creation_date"));
				reimbursement.setResolutionDate(rs.getDate("resolution_date"));
				if (rs.getString("status").equalsIgnoreCase("pending"))
					reimbursement.setStatus(Status.PENDING);
				if (rs.getString("status").equalsIgnoreCase("approved"))
					reimbursement.setStatus(Status.APPROVED);
				if (rs.getString("status").equalsIgnoreCase("denied"))
					reimbursement.setStatus(Status.DENIED);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reimbursement;
	}

	public List<Reimbursement> getReimbursementByAuthor(int id) {
		List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from reimbursement_request where author=" + id);
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				for (int i=1;i<=rsmd.getColumnCount();i++) {
					System.out.println(rsmd.getColumnName(i)+" : "+ rs.getString(i));
				}

				Reimbursement reimbursement = new Reimbursement();
				reimbursement.setId(rs.getInt("id"));
				reimbursement.setAmount(rs.getDouble("amount"));
				reimbursement.setAuthor(rs.getInt("author"));
				reimbursement.setResolver(rs.getInt("resolver"));
				if (rs.getString("status").equalsIgnoreCase("pending")) {
					reimbursement.setStatus(Status.PENDING);
				} else if (rs.getString("status").equalsIgnoreCase("denied")) {
					reimbursement.setStatus(Status.DENIED);
				} else if (rs.getString("status").equalsIgnoreCase("approved")) {
					reimbursement.setStatus(Status.APPROVED);
				}
				reimbursement.setCreationDate(rs.getDate("creation_date"));
				reimbursement.setResolutionDate(rs.getDate("resolution_date"));
				reimbursement.setReceipt((InputStream) rs.getObject("receipt_image"));
				reimbursementList.add(reimbursement);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reimbursementList;
	}

	public Reimbursement create(Reimbursement reimbursement) {
		User user = null;
		UserService userServ = new UserService();

		user = userServ.getByUserId(reimbursement.getAuthor()).get();
		System.out.println("User ID in Reimbursement Servlet is id=" + user.getId());
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			String insertQuery = "insert into reimbursement_request (amount,author,description,receipt_image,status) values (?,?,?,?, 'pending')";
			PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
			pstmt.setDouble(1, reimbursement.getAmount());
			pstmt.setInt(2, user.getId());
			pstmt.setString(3, reimbursement.getDescription());
			pstmt.setObject(4, reimbursement.getReceipt());
			pstmt.execute();
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				Reimbursement reimburse = new Reimbursement();
				reimburse.setId(rs.getInt("id"));
				reimburse.setAmount(rs.getDouble("amount"));
				reimburse.setAuthor(rs.getInt("author"));
				reimburse.setResolver(rs.getInt("resolver"));
				if (rs.getString("status").equalsIgnoreCase("pending")) {
					reimburse.setStatus(Status.PENDING);
				} else if (rs.getString("status").equalsIgnoreCase("denied")) {
					reimburse.setStatus(Status.DENIED);
				} else if (rs.getString("status").equalsIgnoreCase("approved")) {
					reimburse.setStatus(Status.APPROVED);
				}
				reimburse.setCreationDate(rs.getDate("creation_date"));
				reimburse.setResolutionDate(rs.getDate("resolution_date"));
				reimburse.setReceipt((InputStream) rs.getBlob("receipt_image"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return reimbursement;
	}
}
