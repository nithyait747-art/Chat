BYTE XL / NIMBUS VERSION

This is a Maven Dynamic Web Project for Eclipse.
It uses JSP + Servlet + AJAX + JDBC + MySQL.

IMPORTANT:
The Nimbus MySQL host/user/password are environment/account-specific.
They cannot be safely guessed in a ZIP.

1. Import:
Eclipse > File > Import > Maven > Existing Maven Projects
Select this project folder.

2. MySQL:
Run database/chatdb.sql using the MySQL tool available in your ByteXL/Nimbus environment.

3. Nimbus DB details:
Open:
src/main/java/com/chat/util/DBConnection.java

Replace:
YOUR_NIMBUS_MYSQL_HOST
YOUR_NIMBUS_MYSQL_USER
YOUR_NIMBUS_MYSQL_PASSWORD

Keep:
PORT = "3306" unless Nimbus shows another port.
DATABASE = "chatdb" unless you choose another database name.

4. IMPORTANT:
Do not put localhost unless Nimbus specifically provides MySQL on localhost.
JDBC needs the actual MySQL host and port.

5. Run:
Right click project > Run As > Run on Server > Tomcat 9.

6. URL:
http://localhost:8080/SimpleChattingSystem/

7. Test:
Login alice in one browser and bob in another.
Enter the other username and send messages.

The previous GetMessagesServlet syntax issue has also been corrected in this version.
