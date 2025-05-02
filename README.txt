🛠 Preliminary Setup

### Create Database:
• Open phpMyAdmin
• Create a new database named 2230541.
• Import the provided SQL script (scripts/2230541.sql) to create all necessary tables.

### Configuration Directory:

## In WEB-INF/config/.env you can adjust:
• DB_URL, DB_USER, DB_PASSWORD
• SENDER_EMAIL, APP_PASSWORD (for Gmail SMTP)

### Libraries:
All external JARs are located under WEB-INF/lib/.

🧑‍🤝‍🧑 Default User and Passwords
###USERNAMES				#PASSWORDS	#ROLES
1. kananeloj12@gmail.com 	- 	admin@123	= System admin
2. bokangmahlaka226@gmail.com	-	bokang@123	= Student
3. bill@microsoft.com		-	bill@123	= Employer
4. thabansnr@gmail.com		-	thabang@123	= Employer
5. polokonkolanyane92@gmail.com	-	poloko@123	= Employer

### Troubleshooting
• Missing Jakarta Imports / Java EE Server Errors: If you see errors like jakarta.servlet.* not found or Java EE Server missing, confirm you’re using Apache Tomcat 11.0.2.