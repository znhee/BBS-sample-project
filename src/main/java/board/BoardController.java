package board;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import db.BoardDao;
import db.ReplyDao;

/**
 * Servlet implementation class BoardController
 */
@WebServlet({ "/board/list", "/board/search", "/board/write", "/board/update", "/board/detail", "/board/delete",
		"/board/deleteConfirm", "/board/reply" })
/*
 * @MultipartConfig( fileSizeThreshold = 1024 * 1024 * 1, // 1 MB maxFileSize =
 * 1024 * 1024 * 10, // 10 MB maxRequestSize = 1024 * 1024 * 100 // 100 MB )
 */
public class BoardController extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String[] uri = request.getRequestURI().split("/");
		String action = uri[uri.length - 1];
		BoardDao dao = new BoardDao();
		ReplyDao replyDao = new ReplyDao();
		HttpSession session = request.getSession();
		String sessionUid = (String) session.getAttribute("uid");
		session.setAttribute("menu", "board");

		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		String title = null, content = null, files = null, uid = null, today = null;
		int bid = 0, totalBoardNo = 0, totalPages = 0;
		Board board = null;
		List<Board> list = null;
		List<String> pageList = null;
		RequestDispatcher rd = null;

		switch (action) {
		case "list":
			int page = Integer.parseInt(request.getParameter("page"));
			list = dao.listBoard("title", "", page);

			session.setAttribute("currentBoardPage", page);
			totalBoardNo = dao.getBoardCount("title", "");
			totalPages = (int) Math.ceil(totalBoardNo / 10.);
			pageList = new ArrayList<>();
			for (int i = 1; i <= totalPages; i++)
				pageList.add(String.valueOf(i));
			request.setAttribute("pageList", pageList);

			today = LocalDate.now().toString(); // 2022-12-20
			request.setAttribute("today", today);
			request.setAttribute("boardList", list);
			rd = request.getRequestDispatcher("/board/list.jsp");
			rd.forward(request, response);
			break;

		case "search":
			String field = request.getParameter("field");
			String query = request.getParameter("query");
			list = dao.listBoard(field, query, 1);
			
			page = 1;
			session.setAttribute("currentBoardPage", page);
			totalBoardNo = dao.getBoardCount(field, query);
			totalPages = (int) Math.ceil(totalBoardNo / 10.);
			pageList = new ArrayList<>();
			for (int i = 1; i <= totalPages; i++)
				pageList.add(String.valueOf(i));
			request.setAttribute("pageList", pageList);
			
			today = LocalDate.now().toString(); // 2022-12-20
			request.setAttribute("today", today);
			request.setAttribute("boardList", list);
			rd = request.getRequestDispatcher("/board/list.jsp");
			rd.forward(request, response);
			break;
			
		case "detail":
			bid = Integer.parseInt(request.getParameter("bid"));
			uid = request.getParameter("uid");
			String option = request.getParameter("option");
			// 조회수 증가. 단, 본인이 읽거나 댓글 작성후에는 제외.
			if (option == null && (!uid.equals(sessionUid))) {
				dao.increaseViewCount(bid);
			}
			board = dao.getBoardDetail(bid);
			request.setAttribute("board", board);
			List<Reply> replyList = replyDao.getReplies(bid);
			request.setAttribute("replyList", replyList);

			rd = request.getRequestDispatcher("/board/detail.jsp");
			rd.forward(request, response);
			break;

		case "write":
			if (request.getMethod().equals("GET")) {
				response.sendRedirect("/bbs/board/write.jsp");
			} else {
				title = request.getParameter("title");
				content = request.getParameter("content");
				System.out.println("title: " + title);

				files = request.getParameter("files");
				/*
				 * String tmpPath = "c:/Temp/upload"; Part filePart = null; String fileName =
				 * null; List<String> fileList = new ArrayList<>(); for (int i=1; i<=2; i++) {
				 * filePart = request.getPart("file" + i); // name이 file1, file2 if (filePart ==
				 * null) continue; fileName = filePart.getSubmittedFileName();
				 * System.out.println("file" + i + ": " + fileName); if (fileName == null ||
				 * fileName.equals("")) continue; fileList.add(fileName);
				 * 
				 * for (Part part : request.getParts()) { part.write(tmpPath + File.separator +
				 * fileName); } }
				 */

				 board = new Board(sessionUid, title, content, files); 
				 dao.insertBoard(board);
				response.sendRedirect("/bbs/board/list?page=1");
			}
			break;

		case "reply":
			content = request.getParameter("content");
			bid = Integer.parseInt(request.getParameter("bid"));
			uid = request.getParameter("uid"); // 게시글의 uid
			// 게시글의 uid와 댓글을 쓰려고 하는 사람의 uid가 같으면 isMine이 1
			int isMine = (uid.equals(sessionUid)) ? 1 : 0;
			Reply reply = new Reply(content, isMine, sessionUid, bid);
			replyDao.insert(reply);
			dao.increaseReplyCount(bid);
			response.sendRedirect("/bbs/board/detail?bid=" + bid + "&uid=" + uid + "&option=DNI");
			break;

		case "delete":
			bid = Integer.parseInt(request.getParameter("bid"));
			response.sendRedirect("/bbs/board/delete.jsp?bid=" + bid);
			break;

		case "deleteConfirm":
			bid = Integer.parseInt(request.getParameter("bid"));
			dao.deleteBoard(bid);
			response.sendRedirect("/bbs/board/list?page=" + session.getAttribute("currentBoardPage"));
			break;

		case "update":
			if (request.getMethod().equals("GET")) {
				bid = Integer.parseInt(request.getParameter("bid"));
				board = dao.getBoardDetail(bid);
				request.setAttribute("board", board);
				rd = request.getRequestDispatcher("/board/update.jsp");
				rd.forward(request, response);
			} else {
				bid = Integer.parseInt(request.getParameter("bid"));
				uid = request.getParameter("uid");
				title = request.getParameter("title");
				content = request.getParameter("content");
				files = request.getParameter("files");

				board = new Board(bid, title, content, files);
				dao.updateBoard(board);
				response.sendRedirect("/bbs/board/detail?bid=" + bid + "&uid=" + uid + "&option=DNI");
			}
			break;

		default:
			System.out.println(request.getMethod() + " 잘못된 경로");
		}
	}

}