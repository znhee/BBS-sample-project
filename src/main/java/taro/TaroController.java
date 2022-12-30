package taro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class TaroController
 */
@WebServlet({"/taro/main", "/taro/test", "/taro/result"})
public class TaroController extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] uri = request.getRequestURI().split("/");
		String action = uri[uri.length - 1];
		request.setCharacterEncoding("UTF-8");	
		HttpSession session = request.getSession();
		session.setAttribute("menu", "taro");
		TaroDao dao = new TaroDao();
		int s1 = 0, s2 = 0, s3 = 0, s4 = 0;
		String result = null;
		
		RequestDispatcher rd = null;
		int qNo = 0;
		
		Taro taro = null;
		int endPage = 12;
		response.setContentType("text/html; charset=utf-8");
		switch(action) {
		case "main":
			rd = request.getRequestDispatcher("/taro/main.jsp");
			rd.forward(request, response);	
			break;
		case "test":
			if (request.getMethod().equals("GET")) {
				session.setAttribute("s1", 0);
				session.setAttribute("s2", 0);
				session.setAttribute("s3", 0);
				session.setAttribute("s4", 0);
				
				rd = request.getRequestDispatcher("/taro/test.jsp");
				rd.forward(request, response);	
				return;
			} else {
				String op = request.getParameter("op");
				List<Taro> list = dao.listQues();
				String qNo_ = request.getParameter("qNo");
				qNo = Integer.parseInt(qNo_);
				taro = dao.getQuesInfo(qNo);

				System.out.println(op);
				
				s1 = (Integer) session.getAttribute("s1");
				s2 = (Integer) session.getAttribute("s2");
				s3 = (Integer) session.getAttribute("s3");
				s4 = (Integer) session.getAttribute("s4");
				
				switch(qNo) {
				case 1:
				case 2:
				case 3:
					s1 += (op.equals("ans1")) ? 2: -2; break;
				case 4:
				case 5:
				case 6:
					s2 += (op.equals("ans1")) ? 2: -2; break;
				case 7:
				case 8:
				case 9:
					s3 += (op.equals("ans1")) ? 2: -2; break;
				case 10:
				case 11:
				case 12:
					s4 += (op.equals("ans1")) ? 2: -2; break;
				default:
					System.out.println("잘못된 입력");
				}
				request.setAttribute("endPage", endPage);
				request.setAttribute("list", list);
				request.setAttribute("qNo", qNo);
				request.setAttribute("taro", taro);
				
				session.setAttribute("s1", s1);
				session.setAttribute("s2", s2);
				session.setAttribute("s3", s3);
				session.setAttribute("s4", s4);
				
				rd = request.getRequestDispatcher("/taro/test.jsp?qNo=" + qNo);
				rd.forward(request, response);
				
				System.out.println();
				System.out.println(qNo + ", " + op);
				break;
			}
		case "result":
			
			String s1_ = (String) session.getAttribute("s1");
			String s2_ = (String) session.getAttribute("s2");
			String s3_ = (String) session.getAttribute("s3");
			String s4_ = (String) session.getAttribute("s4");
			
			s1 = Integer.parseInt(s1_);
			s2 = Integer.parseInt(s2_);
			s3 = Integer.parseInt(s3_);
			s4 = Integer.parseInt(s4_);
			
			System.out.println(s1 + ", " + s2 + ", " + s3 + ", " + s4);
			result = (s1 >= 0) ? "E" : "I";
			result += (s2 >= 0) ? "S" : "N";
			result += (s3 >= 0) ? "F" : "T";
			result += (s4 >= 0) ? "P" : "J";
			
			request.setAttribute("result", result);
			rd = request.getRequestDispatcher("/ncpl/taro/result.jsp");
			rd.forward(request, response);
			
			break;
		default:
			System.out.println("잘못된 입력");
		}
	}
}



