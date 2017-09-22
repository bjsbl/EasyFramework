package org.easy.framekwork.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.easy.framekwork.Constant;
import org.easy.framekwork.mvc.bean.Result;
import org.easy.framekwork.mvc.bean.View;

public class ViewResolver {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public void resolveView(HttpServletRequest request, HttpServletResponse response, Object actionMethodResult) {
		if (actionMethodResult != null) {
			if (actionMethodResult instanceof View) {
				View view = (View) actionMethodResult;
				if (view.isRedirect()) {
					try {
						response.sendRedirect(request.getContextPath() + view.getPath());
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					String path = "/WEB-INF/jsp/" + view.getPath();
					Map<String, Object> data = view.getData();
					if (data != null && !data.isEmpty()) {
						for (Map.Entry<String, Object> entry : data.entrySet()) {
							request.setAttribute(entry.getKey(), entry.getValue());
						}
					}
					try {
						request.getRequestDispatcher(path).forward(request, response);
					} catch (ServletException | IOException e) {
						e.printStackTrace();
					}
				}
			} else if (actionMethodResult instanceof String) {
				response.setContentType("application/json");
				response.setCharacterEncoding(Constant.UTF_8);
				PrintWriter writer = null;
				try {
					writer = response.getWriter();
					writer.write(actionMethodResult.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				writer.flush();
				writer.close();
			} else {
				Result result = (Result) actionMethodResult;
				response.setContentType("application/json");
				response.setCharacterEncoding(Constant.UTF_8);
				PrintWriter writer = null;
				try {
					writer = response.getWriter();
					writer.write(objectMapper.writeValueAsString(result));
				} catch (IOException e) {
					e.printStackTrace();
				}
				writer.flush();
				writer.close();
			}
		}
	}
}
