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
import java.util.List;


@WebServlet("/")
public class ShoppingListServlet extends HttpServlet {

    JDBCShoppingListItemDao listItems = new JDBCShoppingListItemDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ShoppingListItem> allItems = listItems.getAllItems();

        req.setAttribute("shoppingList", allItems);
        req.getRequestDispatcher("/WEB-INF/list.jsp").forward(req, resp);
    }
}
