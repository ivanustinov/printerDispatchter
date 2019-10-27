package dispatcher.web;

import dispatcher.service.Service;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * //TODO add comments.
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 14.10.2019
 */
@WebServlet(urlPatterns = {"/controller"})
public class Controller extends HttpServlet {
    private Service printerDispatcher = Service.getInstanse();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action != null) {
            printerDispatcher.doAction(action, req);
        }
        try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
            writer.append(req.getAttribute("result").toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
