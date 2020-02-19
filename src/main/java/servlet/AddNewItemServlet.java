package servlet;

import db.JDBCShoppingListItemDao;
import model.ShoppingListItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/addNew")
public class AddNewItemServlet extends HttpServlet {

    JDBCShoppingListItemDao listItems = new JDBCShoppingListItemDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        ShoppingListItem newItem = new ShoppingListItem(listItems.getDefaultId(), title);

        boolean successful = listItems.addItem(newItem);
        
        if (successful) {
            resp.sendRedirect("/");
        } // impl. needed for case when addition was unsuccessful e.g. when shopping list already contains item with same title
    }
}
