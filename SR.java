// java.sql package
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
// for reading from the command line
import java.io.*;

// for the login window
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SR implements ActionListener {
    // command line reader 
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    private Connection con;

    // user is allowed 3 login attempts
    private int loginAttempts = 0;

    // components of the login window
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JFrame mainFrame;

    public SR() {
        mainFrame = new JFrame("User Login");
  
        JLabel usernameLabel = new JLabel("Enter username: ");
        JLabel passwordLabel = new JLabel("Enter password: ");
  
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        passwordField.setEchoChar('*');
  
        JButton loginButton = new JButton("Log In");
  
        JPanel contentPane = new JPanel();
        mainFrame.setContentPane(contentPane);
  
  
        // layout components using the GridBag layout manager
  
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
  
        contentPane.setLayout(gb);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  
        // place the username label 
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(usernameLabel, c);
        contentPane.add(usernameLabel);
  
        // place the text field for the username 
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(usernameField, c);
        contentPane.add(usernameField);
  
        // place password label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(passwordLabel, c);
        contentPane.add(passwordLabel);
  
        // place the password field 
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(passwordField, c);
        contentPane.add(passwordField);
  
        // place the login button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(loginButton, c);
        contentPane.add(loginButton);
  
        // register password field and OK button with action event handler
        passwordField.addActionListener(this);
        loginButton.addActionListener(this);
  
        // anonymous inner class for closing the window
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { 
                System.exit(0); 
            }
        });
  
        // size the window to obtain a best fit for the components
        mainFrame.pack();
  
        // center the frame
        Dimension d = mainFrame.getToolkit().getScreenSize();
        Rectangle r = mainFrame.getBounds();
        mainFrame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
  
        // make the window visible
        mainFrame.setVisible(true);
  
        // place the cursor in the text field for the username
        usernameField.requestFocus();
  
        try {
            // Load the Oracle JDBC driver
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
            System.exit(-1);
        }
    }

    /*
     * connects to Oracle database named ug using user supplied username and password
     */ 
    private boolean connect(String username, String password) {
        String connectURL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu"; 

        try {
	        con = DriverManager.getConnection(connectURL,username,password);

        	System.out.println("\nConnected to Oracle!");
	        return true;
        } catch (SQLException ex) {
	        System.out.println("Message: " + ex.getMessage());
	        return false;
        }
    }

    /*
     * event handler for login window
     */ 
    public void actionPerformed(ActionEvent e) {
        if ( connect(usernameField.getText(), String.valueOf(passwordField.getPassword()))) {
            // if the username and password are valid, 
            // remove the login window and display a text menu 
                mainFrame.dispose();
                showMenu();     
        }
        else {
            loginAttempts++;
            
            if (loginAttempts >= 3) {
                mainFrame.dispose();
                System.exit(-1);
            }
            else {
                // clear the password
                passwordField.setText("");
            }
        }           
    }

    private void showMenu() {
		int choice;
		boolean quit;

		quit = false;
		
		try {
			// disable auto commit mode
			con.setAutoCommit(false);

			while (!quit)
			{
				System.out.print("\n\nPlease choose one of the following: \n");
				System.out.print("1.  Make reservation\n");
				System.out.print("2.  View Available Vehicles\n");
				System.out.print("8.  Show customer\n");
				System.out.print("9.  Show reservation\n");
				System.out.print("0.  Quit\n>> ");

				choice = Integer.parseInt(in.readLine());
				
				System.out.println(" ");

				switch(choice)
				{
                    case 1:  makeReservation(); break;
                    case 2:  viewAvailableVehicles(); break;
                    case 8:  showCustomer(); break;
                    case 9:  showReservation(); break;
					case 0:  quit = true;
				}
			}

			con.close();
			in.close();
			System.out.println("\nGood Bye!\n\n");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("IOException!");

			try {
				con.close();
				System.exit(-1);
			} catch (SQLException ex) {
				System.out.println("Message: " + ex.getMessage());
			}
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
    }

    private void viewAvailableVehicles() {
        String      vt ="";
        String      location ="";
        String      choice;
        String      sqlQuery = "";
        boolean     valid = false;
        Timestamp   start = null;
        Timestamp   end = null;
        ResultSet   rs;


        try {
        System.out.print("Enter vehicle type:\n");

        //TODO: stole this from makeReservation, refactor later 
        choice = new String(in.readLine()).trim();
        //TODO: check if user gave a valid vehicle type
        //  is there a table of just vehicle types? 

        vt = choice;

        System.out.print("Enter location:");

        choice = new String(in.readLine()).trim();
        
        //TODO: check if user gave a valid location
        location = choice; 

        System.out.println(" ");

        System.out.print("Enter availability start timestamp (format: 2001-7-27 09:00:30.75):");

        choice = new String(in.readLine());
        start = parseResponseIntoTimestamp(choice);

        System.out.print("Enter availability end timestamp (format: 2001-7-27 09:00:30.75):");

        choice = new String(in.readLine());
        end = parseResponseIntoTimestamp(choice);

        System.out.print("Selection options: VehicleType = " + vt + 
            ", location = " + location + 
            " start ts: " + start.toString() + 
            "end ts: " + end.toString()); 

        Statement stmt = con.createStatement();
        //if either strartStr or endStr is null, both are invalid
        String startStr = tsToString(start);
        String endStr = tsToString(end);
        
        String whereConditions = "";
        if (vt != null && vt.length() > 0) {
            //TODO also must check vtname is valid
            whereConditions += "WHERE vehicle.vtname = " + vt;
        }

        if (location != null && location.length() > 0) {
            if (location != null && location.length() > 0) {
                whereConditions += " AND ";
            } else {
                whereConditions = "WHERE ";
            }
            whereConditions += "vehicle.location = " + location;
        }

        if (startStr != null && endStr != null) {
            if ((vt != null && vt.length() > 0) || (location != null && location.length() > 0)) {
                whereConditions += " AND ";
            } else {
                whereConditions = "WHERE ";
            }
            //warning, this will most definitely not include rent.confNo when there's no reservation for the rent
            whereConditions += "confNo NOT IN (" +
                "SELECT reservation.confNo FROM rent " + 
                "JOIN reservation ON rent.confNo = reservation.confNo " +
                "WHERE " + startStr + " BETWEEN " + 
                "rent.fromDate" + " AND "  + "rent.ToDate " + 
                "OR " + endStr + " BETWEEN " + 
                "rent.fromDate" + " AND "  + "rent.ToDate " + 
                "WHERE " + startStr + " BETWEEN " + 
                "reservation.fromDate" + " AND "  + "reservation.ToDate " + 
                "OR " + endStr + " BETWEEN " + 
                "reservation.fromDate" + " AND "  + "reservation.ToDate" + 
            ")";
        }
        sqlQuery = "SELECT COUNT(*) AS total FROM reservation JOIN rent " + 
                    "ON reservation.confNo = rent.confNo " + 
                    "JOIN vehicle ON rent.vlicence = vehicle.vlicence " + whereConditions + ";";

        System.out.println(sqlQuery);

        // todo: also make sure vehicles are for rent
        rs = stmt.executeQuery(sqlQuery);

        // get info on ResultSet
        int count = rs.getInt("total");
        
        System.out.print("Number of available vehicles: " + count);

        stmt.close();
        } catch (IOException e) {
            System.out.print("IOException caught\n");

        } catch (SQLException sE) {
            System.out.print("SQLException caught:\n" + sE.getMessage());
        }
    }
    // TODO: move later
    // parses a string in the format '2001-7-27 09:00:30' into a timestamp
    public SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

    private Timestamp parseResponseIntoTimestamp(String str) {
        try {
            //String[] str.split(" ");
            if (str != null) {
                java.util.Date d = dFormat.parse(str);
                return new Timestamp(d.getTime());
            }
            return null;
        } catch (ParseException pE) {
            System.out.print("Parse Exception thrown!\n");
            return null;
        }
    }

    private String tsToString(Timestamp ts) {
        return dFormat.format(ts);
    }
    
    private void makeReservation() {
        String      vt ="";
        String      fromDay;
        String      fromTime;
        String      untilDay;
        String      untilTime;
        int         dlicence;
        int         confNo = 0;
        int         choice;
        int         location;
        String      fromDate;
        String      untilDate;
        boolean     available;      //to check for vehicle availability, not used now
        boolean     cancel;
        boolean     newuser = true;
        Statement   stmt;
        ResultSet   rs;

        try {
            con.setAutoCommit(false);
            stmt = con.createStatement();

            System.out.print("Choose one of the follow type of vehicle:\n");
            System.out.print("1. Economy \t 2. Compact \t 3. Mid-size \t 4. Standard \t 5. Full-size \t 6. SUV \t 7. Truck \n");
            choice = Integer.parseInt(in.readLine());
            
            System.out.println(" ");

            switch(choice) {
                case 1: vt = "Economy"; break;
                case 2: vt = "Compact"; break;
                case 3: vt = "Mid-size"; break;
                case 4: vt = "Standard"; break;
                case 5: vt = "Full-size"; break;
                case 6: vt = "SUV"; break;
                case 7: vt = "Truck"; break;
            }

            System.out.print("Pick up from where?\n");
            System.out.print("1. Vancouver \t 2. Richmond \t 3. Burnaby \n");
            choice = Integer.parseInt(in.readLine());
            
            System.out.println(" ");

            switch(choice) {
                case 1: location = 1; break;
                case 2: location = 2; break;
                case 3: location = 3; break;
            }

            System.out.print("From which day?\n");
            System.out.print("In format of mm/dd/yyyy, ie. 01/01/2020\n");
            fromDay = in.readLine();

            System.out.println(" ");

            System.out.print("From what time?\n");
            System.out.print("In format of hh[24]:mm, ie. 13:30\n");
            fromTime = in.readLine();

            System.out.println(" ");

            System.out.print("To which day?\n");
            System.out.print("In format of mm/dd/yyyy, ie. 01/01/2020\n");
            untilDay = in.readLine();

            System.out.println(" ");

            System.out.print("To what time?\n");
            System.out.print("In format of hh[24]:mm, ie. 13:30\n");
            untilTime = in.readLine();

            System.out.println(" ");

            fromDate = fromDay + " " + fromTime;
            untilDate = untilDay + " " + untilTime;

            System.out.printf("Vehicle: %s\n", vt);
            System.out.printf("From: \t %s\n", fromDate);
            System.out.printf("To: \t %s\n", untilDate);

            System.out.println(" ");

            // Need to check for vehicle availability
            
            System.out.print("Confirm Reservation?\n1. Confirm \t 2. Cancel\n");

            choice = Integer.parseInt(in.readLine());

            System.out.println(" ");

            if(choice == 1) {
                cancel = false;
            } else {
                cancel = true;
            }
                
            if(cancel) {
                System.out.print("Reservation cancelled");
            } else {
                System.out.print("What is your driver licence number?\n");
                dlicence = Integer.parseInt(in.readLine());

                System.out.println(" ");

                rs = stmt.executeQuery("SELECT dlicence FROM customer");

                while(rs.next()) {
                    if(dlicence == rs.getInt("dlicence")) {
                        newuser = false;
                    }
                }
            
                if(newuser) {
                    addNewUser(dlicence);
                }

                rs = stmt.executeQuery("SELECT confNo FROM reservation");
                while(rs.next()) {
                    confNo = rs.getInt("confNo") + 1;
                }

                System.out.print("Reservation complete:\n");
                System.out.printf("Vehicle: %s\n", vt);
                System.out.printf("From: \t %s\n", fromDate);
                System.out.printf("To: \t %s\n", untilDate);
                System.out.printf("Confirmation Number: \t %d \n", confNo);

                System.out.println(" ");

                stmt.executeUpdate("INSERT into reservation values (" + confNo + ", '" + vt + "', " + dlicence + 
                    ", TO_DATE('" + fromDate + "', 'mm/dd/yyyy hh24:mi'), TO_DATE('" + untilDate + "', 'mm/dd/yyyy hh24:mi'))");
            }

            // close the statement; 
            // the ResultSet will also be closed
            stmt.close();
        } catch (IOException e) {
			System.out.println("IOException!");

			try {
				con.close();
				System.exit(-1);
			} catch (SQLException ex) {
				System.out.println("Message: " + ex.getMessage());
			}
		} catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
        }
    }

    private void addNewUser(int licenceNum) {
        int     dlicence = licenceNum;
        int     cellphone = 0;
        String  name = "";
        String  address = "";
        int     confirm = 2;
        Statement stmt;
        
        try {
            while(confirm == 2) {
                System.out.print("What is your cellphone number?\n");
                cellphone = Integer.parseInt(in.readLine());
        
                System.out.println(" ");

                System.out.print("What is your name?\n");
                name = in.readLine();
        
                System.out.println(" ");

                System.out.print("What is your address?\n");
                address = in.readLine();
    
                System.out.println(" ");

                System.out.printf("Licence No: \t %d \n", dlicence);
                System.out.printf("Cellphone No: \t %d \n", cellphone);
                System.out.printf("Name: \t\t %s \n", name);
                System.out.printf("Address: \t %s \n", address);
    
                System.out.println(" ");

                System.out.print("Is all the information correct? \t 1. Yes \t 2. No\n");
    
                confirm = Integer.parseInt(in.readLine());
                
                System.out.println(" ");
            }
    
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT into customer values (" + dlicence + ", " + cellphone + ", '" + name + "', '" + address + "')");
        } catch (IOException e) {
			System.out.println("IOException!");

			try {
				con.close();
				System.exit(-1);
			} catch (SQLException ex) {
				System.out.println("Message: " + ex.getMessage());
			}
		} catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
        }
    }

    private void showCustomer() {
		int         dlicence;
		int         cellphone;
		String      name;
		String      address;
		Statement   stmt;
		ResultSet   rs;
	   
		try {
			stmt = con.createStatement();

			rs = stmt.executeQuery("SELECT * FROM customer");

			// get info on ResultSet
			ResultSetMetaData rsmd = rs.getMetaData();

			// get number of columns
			int numCols = rsmd.getColumnCount();

			System.out.println(" ");
			
			// display column names;
			for (int i = 0; i < numCols; i++) {
				// get column name and print it

				System.out.printf("%-15s", rsmd.getColumnName(i+1));    
			}

			System.out.println(" ");

			while(rs.next()) {
				// for display purposes get everything from Oracle 
				// as a string

				// simplified output formatting; truncation may occur

				dlicence = rs.getInt("dlicence");
				System.out.printf("%-15s", dlicence);

                cellphone = rs.getInt("cellphone");
				System.out.printf("%-15s", cellphone);
                
                name = rs.getString("name");
                System.out.printf("%-15s", name);

                address = rs.getString("address");
				System.out.printf("%-15s\n", address);
			}
	
		// close the statement; 
		// the ResultSet will also be closed
		stmt.close();
		}
		catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}	
    }

    private void showReservation() {
        int         confNo;
        String      vt;
        int         dlicence;
        Timestamp   fromDate;
        Timestamp   untilDate;
		Statement   stmt;
		ResultSet   rs;
	   
		try {
			stmt = con.createStatement();

			rs = stmt.executeQuery("SELECT * FROM reservation");

			// get info on ResultSet
			ResultSetMetaData rsmd = rs.getMetaData();

			// get number of columns
			int numCols = rsmd.getColumnCount();

			System.out.println(" ");
			
			// display column names;
			for (int i = 0; i < numCols; i++) {
                // get column name and print it
                if(rsmd.getColumnName(i+1).equals("FROMDATE") || rsmd.getColumnName(i+1).equals("TODATE")) {
                    System.out.printf("%-25s", rsmd.getColumnName(i+1));
                } else {
                    System.out.printf("%-15s", rsmd.getColumnName(i+1));
                }
			}

			System.out.println(" ");

			while(rs.next()) {
				// for display purposes get everything from Oracle 
				// as a string

				// simplified output formatting; truncation may occur

				confNo = rs.getInt("confNo");
                System.out.printf("%-15s", confNo);
                
                vt = rs.getString("vtname");
                System.out.printf("%-15s", vt);
                
                dlicence = rs.getInt("dlicence");
                System.out.printf("%-15s", dlicence);
                
                fromDate = rs.getTimestamp("fromDate");
                System.out.printf("%-25s", fromDate);
                
                untilDate = rs.getTimestamp("toDate");
				System.out.printf("%-25s\n", untilDate);
			}
	
		// close the statement; 
		// the ResultSet will also be closed
		stmt.close();
		}
		catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}	
    }

    public static void main(String args[]) {
        new SR();
    }
}