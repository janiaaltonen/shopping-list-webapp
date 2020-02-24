package servlet;

import com.google.gson.Gson;
import db.JDBCShoppingListItemDao;
import model.ShoppingListItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/remove")
public class DeleteItemServlet extends HttpServlet {
    JDBCShoppingListItemDao listItems = new JDBCShoppingListItemDao();
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));
        ShoppingListItem deleteItem = listItems.getItem(id);

        if (deleteItem != null) {
            listItems.removeItem(deleteItem);
        }

        String json = new Gson().toJson(deleteItem);
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().println(json);
    }
}
