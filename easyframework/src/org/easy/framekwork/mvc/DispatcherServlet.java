package org.easy.framekwork.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easy.framekwork.Constant;
import org.easy.framekwork.core.InstanceFactory;
import org.easy.framekwork.log.Logger;
import org.easy.framekwork.mvc.bean.ActionMapping;
import org.easy.framekwork.util.StringUtil;

@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = -2907270713619274397L;
	private Logger logger = Logger.getLogger(DispatcherServlet.class);
	private ActionInvoker handlerInvoker = InstanceFactory.getHandlerInvoker();

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding(Constant.UTF_8);
		String method = request.getMethod();
		String servletPath = request.getServletPath();
		String pathInfo = StringUtil.defaultIfEmpty(request.getPathInfo(), "");
		String path = servletPath + pathInfo;
		if (path.equals("/index.html")) {
			super.service(request, response);
			return;
		}
		if (path.equals("/")) {
			response.sendRedirect(request.getContextPath() + "/index.html");
			return;
		}
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		logger.info(String.format("[Easy] {%s}:{%s}", method, path));
		ActionMapping mapping = ActionHandler.getActionMapping(method, path);
		if (mapping == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not ....");
			return;
		}
		Context.init(request, response);
		try {
			handlerInvoker.invokeHandler(request, response, mapping);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		} finally {
			Context.destroy();
		}
	}

}
