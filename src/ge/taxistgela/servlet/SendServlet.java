package ge.taxistgela.servlet;

import com.google.gson.Gson;
import ge.taxistgela.bean.Order;
import ge.taxistgela.dispatcher.OrderDispatcher;
import ge.taxistgela.model.RemoteManager;
import ge.taxistgela.model.RemoteManagerAPI;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Alex on 6/5/2015.
 */
@WebServlet("/send")
public class SendServlet extends ActionServlet {

    // TODO Replace with real implementations.

    protected void sendToDriver(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("sendToDriver");

        OrderDispatcher orderDispatcher = (OrderDispatcher) getServletContext().getAttribute(OrderDispatcher.class.getName());

        String message = request.getParameter("message");

        System.out.println("sendToDriver: " + message);

        orderDispatcher.addToQueue(new Gson().fromJson(message, Order.class));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected void sendToUser(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("sendToUser");

        RemoteManagerAPI sessionManager = (RemoteManagerAPI) getServletContext().getAttribute(RemoteManagerAPI.class.getName());

        String token = request.getParameter("token");
        String message = request.getParameter("message");

        System.out.println("sendToUser: " + token + " " + message);

        sessionManager.sendMessage(RemoteManager.USER_REMOTE, token, message);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
