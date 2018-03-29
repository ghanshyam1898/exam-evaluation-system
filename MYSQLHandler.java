import java.sql.*;

public class MYSQLHandler {
   
   public static void main(String[] args) {

   	command_parser cp = new command_parser();
   	cp.evaluate_copies("1");   	

   }

}

class command_parser{

   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/dbtable?autoReconnect=true&useSSL=false";
   static final String USER = "root";
   static final String PASS = "root12";


	public static void parse_create_user(String full_name, String subject, String email, String password, String college){

		   try{
			   Connection conn = null;

			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);


			  PreparedStatement sql = conn.prepareStatement("INSERT INTO professor_registration (full_name, subject, email, password, college) values ( ?, ?, ?, ?, ?);");

		      sql.setString(1, full_name);
		      sql.setString(2, subject);
		      sql.setString(3, email);
		      sql.setString(4, password);
		      sql.setString(5, college);
		      
		      sql.executeUpdate();

		   }catch(SQLException se){
		      se.printStackTrace();

			}catch(Exception e){
      e.printStackTrace();
   }

	}


	public String verify_signin_credentials(String email, String password){

		   String professor_id = "-1";	

		   try{
			   Connection conn = null;
			   Statement stmt = null;
			   

    		   Class.forName("com.mysql.jdbc.Driver");
			   conn = DriverManager.getConnection(DB_URL,USER,PASS);

			   stmt = conn.createStatement();
			   String sql;
			   sql = "SELECT professor_id, email, password FROM  professor_registration";
			   ResultSet rs = stmt.executeQuery(sql);

		      while(rs.next()){
		      		if (email.equals(rs.getString("email")) && password.equals(rs.getString("password"))){
		      			professor_id = new Integer(rs.getString("professor_id")).toString();
		      			return professor_id;
		      		}

		      }

		      rs.close();
		      stmt.close();
		      conn.close();

		      return professor_id;

		   }catch(SQLException se){
		      se.printStackTrace();

			}catch(Exception e){
      e.printStackTrace();
		   }
	  
	  return professor_id;

	}

//Next function in the class

	public static void handle_file_upload(String registration_number, String subject_code, String file_path){

		   try{
			   Connection conn = null;

			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);


			PreparedStatement sql = conn.prepareStatement("INSERT INTO copies_maintainer (registration_number, subject_code, file_path) values ( ?, ?, ?);");

		      sql.setString(1, registration_number);
		      sql.setString(2, subject_code);
		      sql.setString(3, file_path);
		      
		      sql.executeUpdate();

		   }catch(SQLException se){
		      se.printStackTrace();

			}catch(Exception e){
      e.printStackTrace();
   }

	}

	public String evaluate_copies(String professor_id){

			String file_path = "";	

		   try{
			   Connection conn = null;
			   Statement stmt = null;
			   
    		   Class.forName("com.mysql.jdbc.Driver");
			   conn = DriverManager.getConnection(DB_URL,USER,PASS);

			   stmt = conn.createStatement();
			   String sql;

			   sql = "SELECT subject FROM  professor_registration where professor_id=";
			   sql += professor_id;
			   sql += ";";
			   ResultSet prof = stmt.executeQuery(sql);
			   String prof_subject = "";
			   while (prof.next()){
			   		prof_subject = prof.getString("subject");
			   }

			   stmt = conn.createStatement();
			   sql = "SELECT id,subject_code ,checked_status, file_path FROM  copies_maintainer where subject_code=";
			   sql+="\"";
			   sql += prof_subject;
			   sql+="\"";
			   sql+=";";
			   ResultSet copies = stmt.executeQuery(sql);
		      while(copies.next()){
		      		if(copies.getInt("checked_status") == 0){
		      			file_path = copies.getString("file_path");
		      			return file_path;
		      		} 

		      }
		      copies.close();
		      stmt.close();
		      conn.close();

		      return file_path;

		   }catch(SQLException se){
		      se.printStackTrace();

			}catch(Exception e){
      e.printStackTrace();
		   }
	  
	  return file_path;

	}


	public static void assign_marks(String file_path, String marks){

		   try{
			   Connection conn = null;

			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);


			PreparedStatement sql = conn.prepareStatement("update copies_maintainer set marks=?, checked_status=true where file_path=?;");

		      sql.setString(1, marks);
		      sql.setString(2, file_path);
		      
		      sql.executeUpdate();

		   }catch(SQLException se){
		      se.printStackTrace();

			}catch(Exception e){
      e.printStackTrace();
   }

	}

	public String find_results(String registration_number, String subject_code){

			String marks = "";

		   try{
			   Connection conn = null;
			   Statement stmt = null;
			   

    		   Class.forName("com.mysql.jdbc.Driver");
			   conn = DriverManager.getConnection(DB_URL,USER,PASS);

			   stmt = conn.createStatement();
			   String sql;
			   sql = "SELECT marks FROM  copies_maintainer where registration_number=";
			   sql += "\"";
			   sql += registration_number;
			   sql += "\"";
			   sql += "and subject_code = ";
			   sql += "\"";
			   sql += subject_code;
			   sql += "\";";

			   ResultSet rs = stmt.executeQuery(sql);

			   
		      while(rs.next()){

		      		marks = rs.getString("marks");
		      }

		      rs.close();
		      stmt.close();
		      conn.close();

		      return marks;

		   }catch(SQLException se){
		      se.printStackTrace();

			}catch(Exception e){
      e.printStackTrace();
		   }
	  
	  return marks;

	}



}



/*

copies_maintainer

+-----------------------+--------------+------+-----+---------+----------------+
| Field                 | Type         | Null | Key | Default | Extra          |
+-----------------------+--------------+------+-----+---------+----------------+
| id                    | int(11)      | NO   | PRI | NULL    | auto_increment |
| registration_number   | varchar(50)  | YES  |     | NULL    |                |
| subject_code          | varchar(50)  | YES  |     | NULL    |                |
| checked_status        | tinyint(1)   | YES  |     | 0       |                |
| professor_assigned_id | int(11)      | YES  |     | NULL    |                |
| marks                 | decimal(6,4) | YES  |     | NULL    |                |
| file_path             | varchar(200) | YES  |     | NULL    |                |
+-----------------------+--------------+------+-----+---------+----------------+


professor_registration

+--------------+-------------+------+-----+---------+----------------+
| Field        | Type        | Null | Key | Default | Extra          |
+--------------+-------------+------+-----+---------+----------------+
| professor_id | int(11)     | NO   | PRI | NULL    | auto_increment |
| full_name    | varchar(50) | YES  |     | NULL    |                |
| subject      | varchar(50) | YES  |     | NULL    |                |
| email        | varchar(50) | YES  |     | NULL    |                |
| password     | varchar(50) | YES  |     | NULL    |                |
+--------------+-------------+------+-----+---------+----------------+

*/
