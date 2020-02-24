<%@ page language="java" contentType="text/html; utf-8"
         pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>Shopping List Content</title>
    <link rel="stylesheet" href="/styles/demo.css">

</head>

<body>
    <h1> Shopping list contents</h1>
    <table>
        <thead>
            <tr><th>Product</th></tr>
        </thead>
        <tbody>
            <c:forEach items="${shoppingList}" var="item">
                <tr id="product-${item.id}">
                    <td><c:out value="${item.title}"/></td>
                    <td><button onclick="removeProduct(${ item.getId() })">Remove</button></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <h2> Add new item to shopping list</h2>
        <form action="/addNew" id="add-new-form" method="post">
            <input id="new-item-title" name="title" required type="text" placeholder=" type item here..." autofocus />
            <input type="submit" id="add-new-item" value="Add to list" />
        </form>


    <script>
        async function removeProduct(id) {
            try {
                let response = await fetch('/remove?id=' + id, {method: 'DELETE'});
                let rowId = "product-" + id;
                document.getElementById(rowId).remove()
            } catch (e) {
                console.error(e);
                alert('An error occurred. Please check the consoles of the browser and the backend.');
            }
        }
    </script>
</body>
</html>