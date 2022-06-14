package com.revature.ersmaven;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;

/**
 * Servlet implementation class CreateReimbursementServlet
 */
@WebServlet("/ReimburseServlet")
public class ReimburseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    ReimbursementService reimbursementServ = new ReimbursementService();
    AuthService authServ = new AuthService();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReimburseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		UserService userServ = new UserService();
		response.setContentType("text/html");
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		System.out.println("username in create servlet: "+username);
		User user = null;
		user = userServ.getByUsername(username).get();
		int id=user.getId();
		
		
		Reimbursement createReimbursement = new Reimbursement();
		double amount = Double.parseDouble(request.getParameter("amount"));
		String description = request.getParameter("description");
		String message = null;
		//InputStream inputStream = null;
		//Part filePart = request.getPart("receipt");

		//if (filePart != null) {

			// Prints out some information
			// for debugging
			//System.out.println(filePart.getName());
			//System.out.println(filePart.getSize());
			//System.out.println(filePart.getContentType());

			// Obtains input stream of the upload file
			//inputStream = filePart.getInputStream();
		//}
		//if (authServ.exampleRetrieveCurrentUser().isPresent()) {
		//	user = authServ.exampleRetrieveCurrentUser().get();
		//}
		//System.out.println("username is = "+username);
		createReimbursement.setAmount(amount);
		createReimbursement.setDescription(description);
		//createReimbursement.setReceipt(inputStream);
		createReimbursement.setAuthor(id);
		
		System.out.println("reimbursement object is: "+createReimbursement);
		
		Reimbursement reimbursement = reimbursementServ.create(createReimbursement);
		if (reimbursement != null) {
			message = "Reimbursement created and registered into database!";
		}
		System.out.println(message);
		
		if (username != null && reimbursement !=null) {
			response.sendRedirect("HomeServlet?username="+username);
		} else {
			out.println("An error has occurred while submitting Reimbursement Request. Click <a href='Login.html'>Here</a> to go back to Login Page");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
