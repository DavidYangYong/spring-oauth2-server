package com.monkeyk.sos.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.monkeyk.sos.domain.acl.Message;
import com.monkeyk.sos.service.MessageService;

@SuppressWarnings("serial")
public class MessageServlet extends HttpServlet {
	@Autowired
	private MessageService messageService;

	private MessageService getMessageService() {
		if (messageService == null) {
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
			messageService = (MessageService) ctx.getBean("messageService");
		}
		return messageService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			process(request, response);
		}
		catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			process(request, response);
		}
		catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

	public void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String action = request.getParameter("action");
		if ("view".equals(action)) {
			this.view(request, response);
		}
		else if ("list".equals(action)) {
			this.list(request, response);
		}
		else if ("save".equals(action)) {
			this.save(request, response);
		}
		else if ("update".equals(action)) {
			this.update(request, response);
		}
		else if ("remove".equals(action)) {
			this.remove(request, response);
		}
		else if ("create".equals(action)) {
			this.create(request, response);
		}
		else if ("edit".equals(action)) {
			this.edit(request, response);
		}
		else {
			System.out.println("Unkown Action: " + action);
		}
	}

	public void view(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(request.getParameter("id"));
		Message message = getMessageService().get(id);
		request.setAttribute("message", message);
		request.getRequestDispatcher("/messageView.jsp").forward(request, response);
	}

	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Message> list = getMessageService().getAll();
		request.setAttribute("list", list);
		request.getRequestDispatcher("/messageList.jsp").forward(request, response);
	}

	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String message = request.getParameter("message");
		String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		getMessageService().save(message, userName);
		response.sendRedirect("message.do?action=list");
	}

	public void update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(request.getParameter("id"));
		String message = request.getParameter("message");
		getMessageService().update(id, message);
		response.sendRedirect("message.do?action=list");
	}

	public void remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(request.getParameter("id"));
		getMessageService().removeById(id);
		response.sendRedirect("message.do?action=list");
	}

	public void create(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getRequestDispatcher("/messageEdit.jsp").forward(request, response);
	}

	public void edit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(request.getParameter("id"));
		Message message = getMessageService().get(id);
		request.setAttribute("message", message);
		request.getRequestDispatcher("/messageEdit.jsp").forward(request, response);
	}
}
