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

import java.util.Date;

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
				System.out.print("1.  Search vehicle\n");
				System.out.print("2.  Make reservation\n");
				System.out.print("3.  Rent a vehicle\n");
				System.out.print("4.  Return a vehicle\n");
				System.out.print("5.  Daily total rental\n");
				System.out.print("6.  Daily branch rental\n");
				System.out.print("7.  Daily total return\n");
                System.out.print("8.  Daily branch return\n");
                System.out.print("9.  Edit database\n");
				System.out.print("10.  Show customer\n");
				System.out.print("20.  Show reservation\n");
				System.out.print("30.  Show rent\n");
				System.out.print("40.  Show return\n");
				System.out.print("50.  Show vehicle\n");
				System.out.print("60.  Show vehicle types\n");
				System.out.print("0.  Quit\n>> ");

				choice = Integer.parseInt(in.readLine());
				
				System.out.println(" ");

				switch(choice)
				{
                    case 1:   searchVehicle(); break;
                    case 2:   makeReservation(); break;
                    case 3:   rentVehicle(); break;
                    case 4:   returnVehicle(); break;
                    case 5:   totalRental(); break;
                    case 6:   branchRental(); break;
                    case 7:   totalReturn(); break;
                    case 8:   branchReturn(); break;
                    case 9:   editTable(); break;
                    case 10:  showCustomer(); break;
                    case 20:  showReservation(); break;
                    case 30:  showRent(); break;
                    case 40:  showReturn(); break;
                    case 50:  showVehicle(); break;
                    case 60:  showVehicleType(); break;
					case 0:   quit = true;
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

    private void searchVehicle() {

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
        int         location = 0;
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

            while(location == 0) {
                switch(choice) {
                    case 1: location = 1; break;
                    case 2: location = 2; break;
                    case 3: location = 3; break;
                    default: location = 0; break;
                }
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

                rs = stmt.executeQuery("SELECT MAX(confNo) AS conf FROM reservation");
                while(rs.next()) {
                    confNo = rs.getInt("conf") + 1;
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

    private void rentVehicle() {
        int         confNo = 0;
        int         rid = 0;
        int         confirm = 3;
        int         odometer = 0;
        int         dlicence = 0;
        String      cardName = "";
        Date        expDate = new Date();
        String      cardNo = "";
        String      vt = "";
        String      vlicence = "";
        String      city = "";
        Timestamp   fromDate;
        Timestamp   toDate;
        Statement   stmt;
        ResultSet   rs;
        ResultSet   reservation;
        ResultSet   vehicle;

        try {
            while(confirm == 3) {
                while (true) {
                    stmt = con.createStatement();
                    System.out.println("What is your confirmation number?\n-1 if you haven't made one");
                    confNo = Integer.parseInt(in.readLine());
                    System.out.println(" ");
                    if (confNo == -1) {
                        makeReservation();
                    } else {
                        // find reservation number and correlated data
                        reservation = stmt.executeQuery("SELECT * FROM reservation WHERE confNo =" + confNo);
                        reservation.next();
                        if (reservation != null) {
                            vt = reservation.getString("vtname");
                            dlicence = reservation.getInt("dlicence");
                            fromDate = reservation.getTimestamp("fromDate");
                            toDate = reservation.getTimestamp("toDate");
                            vehicle = stmt.executeQuery("SELECT * FROM vehicle WHERE status LIKE 'available' AND vtname LIKE '" + vt + "'");
                            // get vehicle data
                            // TODO: if case where no vehicle available?
                            // get last vehicle that fits given requirements
                            while (vehicle.next()) {
                                vlicence = vehicle.getString("vlicence");
                                odometer = vehicle.getInt("odometer");
                                city = vehicle.getString("city");
                            }

                            stmt.executeQuery(" UPDATE vehicle SET status = 'rented' WHERE vlicence = '" + vlicence + "'");

                            // create rent ID
                            rs = stmt.executeQuery("SELECT MAX(rid) AS r FROM rent");
                            while(rs.next()) {
                                rid = rs.getInt("r") + 1;
                            }
                            System.out.println("Enter card name: ");
                            cardName = in.readLine();

                            System.out.println("Enter card number: ");
                            cardName = in.readLine();

                            //TODO: take first 4 characters, all strictly numeric
                            System.out.println("Enter card expiry date (MMYY): ");
                            cardName = in.readLine();

                            // print receipt
                            System.out.println("Receipt: ");

                            System.out.println("Confirmation number: ");
                            System.out.printf("%-15s\n", confNo);

                            System.out.println("Vehicle type: ");
                            System.out.printf("%-15s\n", vt);

                            System.out.println("Drivers licence: ");
                            System.out.printf("%-15s\n", dlicence);

                            System.out.println("From: ");
                            System.out.printf("%-25s\n", fromDate);

                            System.out.println("To: ");
                            System.out.printf("%-25s\n", toDate);

                            System.out.println("City: ");
                            System.out.printf("%-15s\n", odometer);

                            System.out.printf("%-15s\n", vlicence);

                            // missing comma error for some reason,
                            // someone please enlighten me.
                            stmt.executeUpdate("INSERT into rent values (" + rid + ", '" + vlicence + "', " + dlicence + ", " + fromDate + ", " + toDate + ", " + odometer + ", '" + cardName + "', '" + cardNo + "', TO_DATE('" + expDate +  "', 'mm/yy'), " + confNo + ")");
                            break;
                        }
                    }
                }
                stmt.close();
            }
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

    private void returnVehicle() {

    }

    private void totalRental() {
        String      vlicence;
        int         vid;
        String      make;
        String      model;
        int         year;
        String      color;
        int         odometer;
        String      status;
        String      vtname;
        int         location;
        String      city;
        int         number;
        Statement   stmt;
        ResultSet   rs;
        int         numCols;
        ResultSetMetaData rsmd;
	   
		try {
			stmt = con.createStatement();

			rs = stmt.executeQuery(
            "SELECT v.vlicence, v.vid, v.make, v.model, v.year, v.color, v.odometer, v.status, v.vtname, v.location, v.city " +
            "FROM vehicle v, rent r " +
            "WHERE v.vlicence = r.vlicence AND TO_CHAR(r.fromDate, 'YYYY-MM-DD') LIKE TO_CHAR(CURRENT_DATE, 'YYYY-MM-DD') " +
            "ORDER BY v.location, v.vtname");

			// get info on ResultSet
			rsmd = rs.getMetaData();

			// get number of columns
			numCols = rsmd.getColumnCount();

            System.out.println(" ");
            System.out.println("Vehicles rented out today:");
			
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

				vlicence = rs.getString("vlicence");
				System.out.printf("%-15s", vlicence);

                vid = rs.getInt("vid");
				System.out.printf("%-15s", vid);
                
                make = rs.getString("make");
                System.out.printf("%-15s", make);

                model = rs.getString("model");
                System.out.printf("%-15s", model);
                
                year = rs.getInt("year");
                System.out.printf("%-15s", year);
                
                color = rs.getString("color");
                System.out.printf("%-15s", color);

                odometer = rs.getInt("odometer");
                System.out.printf("%-15s", odometer);
                
                status = rs.getString("status");
                System.out.printf("%-15s", status);
                
                vtname = rs.getString("vtname");
                System.out.printf("%-15s", vtname);

                location = rs.getInt("location");
                System.out.printf("%-15s", location);

                city = rs.getString("city");
                System.out.printf("%-15s\n", city);
            }
            
            rs = stmt.executeQuery(
            "SELECT v.location, v.vtname, COUNT(v.vid) AS \"NUMBER\" " +
            "FROM vehicle v, rent r " +
            "WHERE v.vlicence = r.vlicence AND TO_CHAR(r.fromDate, 'YYYY-MM-DD') LIKE TO_CHAR(CURRENT_DATE, 'YYYY-MM-DD') " +
            "GROUP BY v.location, v.vtname " +
            "ORDER BY v.location, v.vtname");

			// get info on ResultSet
			rsmd = rs.getMetaData();

			// get number of columns
			numCols = rsmd.getColumnCount();

			System.out.println(" ");
            System.out.println("Vehicle types rented per branch:");
			
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

				location = rs.getInt("location");
                System.out.printf("%-15s", location);

                vtname = rs.getString("vtname");
                System.out.printf("%-15s", vtname);

                number = rs.getInt("number");
                System.out.printf("%-15s\n", number);
            }

            rs = stmt.executeQuery(
            "SELECT COUNT(*) AS \"NUMBER\" " +
            "FROM rent r " +
            "WHERE TO_CHAR(r.fromDate, 'YYYY-MM-DD') LIKE TO_CHAR(CURRENT_DATE, 'YYYY-MM-DD')");

			System.out.println(" ");
            System.out.println("Total number of vehicle rented today:");

            while(rs.next()) {
                number = rs.getInt("number");
                System.out.printf("%-15s\n", number);
            }
	
		// close the statement; 
		// the ResultSet will also be closed
		stmt.close();
		}
		catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}	
    }

    private void branchRental() {
        String      vlicence;
        int         vid;
        String      make;
        String      model;
        int         year;
        String      color;
        int         odometer;
        String      status;
        String      vtname;
        int         location;
        String      city;
        int         number;
        int         branch = 0;
        int         choice;
        Statement   stmt;
        ResultSet   rs;
        int         numCols;
        ResultSetMetaData rsmd;
	   
		try {
            stmt = con.createStatement();
            
            System.out.print("Which branch?\n");
            System.out.print("1. Vancouver \t 2. Richmond \t 3. Burnaby \n");
            choice = Integer.parseInt(in.readLine());
            
            System.out.println(" ");

            while(branch == 0) {
                switch(choice) {
                    case 1: branch = 1; break;
                    case 2: branch = 2; break;
                    case 3: branch = 3; break;
                    default: branch = 0; break;
                }
            }

			rs = stmt.executeQuery(
            "SELECT v.vlicence, v.vid, v.make, v.model, v.year, v.color, v.odometer, v.status, v.vtname, v.location, v.city " +
            "FROM vehicle v, rent r " +
            "WHERE v.vlicence = r.vlicence AND TO_CHAR(r.fromDate, 'YYYY-MM-DD') LIKE TO_CHAR(CURRENT_DATE, 'YYYY-MM-DD') AND v.location = " + branch + " " +
            "ORDER BY v.vtname");

			// get info on ResultSet
			rsmd = rs.getMetaData();

			// get number of columns
			numCols = rsmd.getColumnCount();

            System.out.println(" ");
            System.out.println("Vehicles rented out today:");
			
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

				vlicence = rs.getString("vlicence");
				System.out.printf("%-15s", vlicence);

                vid = rs.getInt("vid");
				System.out.printf("%-15s", vid);
                
                make = rs.getString("make");
                System.out.printf("%-15s", make);

                model = rs.getString("model");
                System.out.printf("%-15s", model);
                
                year = rs.getInt("year");
                System.out.printf("%-15s", year);
                
                color = rs.getString("color");
                System.out.printf("%-15s", color);

                odometer = rs.getInt("odometer");
                System.out.printf("%-15s", odometer);
                
                status = rs.getString("status");
                System.out.printf("%-15s", status);
                
                vtname = rs.getString("vtname");
                System.out.printf("%-15s", vtname);

                location = rs.getInt("location");
                System.out.printf("%-15s", location);

                city = rs.getString("city");
                System.out.printf("%-15s\n", city);
            }
            
            rs = stmt.executeQuery(
            "SELECT v.vtname, COUNT(v.vid) AS \"NUMBER\" " +
            "FROM vehicle v, rent r " +
            "WHERE v.vlicence = r.vlicence AND TO_CHAR(r.fromDate, 'YYYY-MM-DD') LIKE TO_CHAR(CURRENT_DATE, 'YYYY-MM-DD') AND v.location = " + branch + " " +
            "GROUP BY v.vtname " +
            "ORDER BY v.vtname");

			// get info on ResultSet
			rsmd = rs.getMetaData();

			// get number of columns
			numCols = rsmd.getColumnCount();

			System.out.println(" ");
            System.out.println("Vehicle types rented:");
			
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

                vtname = rs.getString("vtname");
                System.out.printf("%-15s", vtname);

                number = rs.getInt("number");
                System.out.printf("%-15s\n", number);
            }

            rs = stmt.executeQuery(
            "SELECT COUNT(*) AS \"NUMBER\" " +
            "FROM vehicle v, rent r " +
            "WHERE v.vlicence = r.vlicence AND TO_CHAR(r.fromDate, 'YYYY-MM-DD') LIKE TO_CHAR(CURRENT_DATE, 'YYYY-MM-DD') AND v.location = " + branch);

			System.out.println(" ");
            System.out.println("Total number of vehicle rented todays:");

            while(rs.next()) {
                number = rs.getInt("number");
                System.out.printf("%-15s\n", number);
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

    private void totalReturn() {
        String      vlicence;
        int         vid;
        String      make;
        String      model;
        int         year;
        String      color;
        int         odometer;
        String      status;
        String      vtname;
        int         location;
        String      city;
        int         number;
        double      revenue = 0.0;
        Statement   stmt;
        ResultSet   rs;
        int         numCols;
        ResultSetMetaData rsmd;
	   
		try {
            stmt = con.createStatement();
            
			rs = stmt.executeQuery(
            "SELECT v.vlicence, v.vid, v.make, v.model, v.year, v.color, v.odometer, v.status, v.vtname, v.location, v.city " +
            "FROM vehicle v, rent r, return t " +
            "WHERE v.vlicence = r.vlicence AND r.rid = t.rid AND TO_CHAR(t.return_date, 'YYYY-MM-DD') LIKE TO_CHAR(CURRENT_DATE, 'YYYY-MM-DD') " +
            "ORDER BY v.location, v.vtname");

			// get info on ResultSet
			rsmd = rs.getMetaData();

			// get number of columns
			numCols = rsmd.getColumnCount();

            System.out.println(" ");
            System.out.println("Vehicles rented out today:");
			
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

				vlicence = rs.getString("vlicence");
				System.out.printf("%-15s", vlicence);

                vid = rs.getInt("vid");
				System.out.printf("%-15s", vid);
                
                make = rs.getString("make");
                System.out.printf("%-15s", make);

                model = rs.getString("model");
                System.out.printf("%-15s", model);
                
                year = rs.getInt("year");
                System.out.printf("%-15s", year);
                
                color = rs.getString("color");
                System.out.printf("%-15s", color);

                odometer = rs.getInt("odometer");
                System.out.printf("%-15s", odometer);
                
                status = rs.getString("status");
                System.out.printf("%-15s", status);
                
                vtname = rs.getString("vtname");
                System.out.printf("%-15s", vtname);

                location = rs.getInt("location");
                System.out.printf("%-15s", location);

                city = rs.getString("city");
                System.out.printf("%-15s\n", city);
            }
            
            rs = stmt.executeQuery(
            "SELECT v.location, v.vtname, COUNT(v.vid) AS \"NUMBER\", SUM(t.value) AS \"REVENUE\" " +
            "FROM vehicle v, rent r, return t " +
            "WHERE v.vlicence = r.vlicence AND r.rid = t.rid AND TO_CHAR(r.fromDate, 'YYYY-MM-DD') LIKE TO_CHAR(CURRENT_DATE, 'YYYY-MM-DD') " +
            "GROUP BY v.location, v.vtname " +
            "ORDER BY v.location, v.vtname");

			// get info on ResultSet
			rsmd = rs.getMetaData();

			// get number of columns
			numCols = rsmd.getColumnCount();

			System.out.println(" ");
            System.out.println("Vehicle types rented per branch:");
			
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

				location = rs.getInt("location");
                System.out.printf("%-15s", location);

                vtname = rs.getString("vtname");
                System.out.printf("%-15s", vtname);

                number = rs.getInt("number");
                System.out.printf("%-15s", number);

                revenue = rs.getDouble("revenue");
                System.out.printf("%-15s\n", revenue);
            }

            rs = stmt.executeQuery(
            "SELECT COUNT(*) AS \"NUMBER\", SUM(value) AS \"REVENUE\"" +
            "FROM return " +
            "WHERE TO_CHAR(return_date, 'YYYY-MM-DD') LIKE TO_CHAR(CURRENT_DATE, 'YYYY-MM-DD')");

			System.out.println(" ");
            System.out.println("Total number of vehicle rented today and total revenue:");

            while(rs.next()) {
                number = rs.getInt("number");
                System.out.printf("%-15s\t", number);

                revenue = rs.getDouble("revenue");
                System.out.printf("$%-15s\n", revenue);
            }
	
		// close the statement; 
		// the ResultSet will also be closed
		stmt.close();
		}
		catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
    }

    private void branchReturn() {
        String      vlicence;
        int         vid;
        String      make;
        String      model;
        int         year;
        String      color;
        int         odometer;
        String      status;
        String      vtname;
        int         location;
        String      city;
        int         number;
        int         branch = 0;
        int         choice;
        double      revenue;
        Statement   stmt;
        ResultSet   rs;
        int         numCols;
        ResultSetMetaData rsmd;
	   
		try {
            stmt = con.createStatement();
            
            System.out.print("Which branch?\n");
            System.out.print("1. Vancouver \t 2. Richmond \t 3. Burnaby \n");
            choice = Integer.parseInt(in.readLine());
            
            System.out.println(" ");

            while(branch == 0) {
                switch(choice) {
                    case 1: branch = 1; break;
                    case 2: branch = 2; break;
                    case 3: branch = 3; break;
                    default: branch = 0; break;
                }
            }

			rs = stmt.executeQuery(
            "SELECT v.vlicence, v.vid, v.make, v.model, v.year, v.color, v.odometer, v.status, v.vtname, v.location, v.city " +
            "FROM vehicle v, rent r, return t " +
            "WHERE v.vlicence = r.vlicence AND r.rid = t.rid AND TO_CHAR(t.return_date, 'YYYY-MM-DD') LIKE TO_CHAR(CURRENT_DATE, 'YYYY-MM-DD') AND v.location = " + branch + " " +
            "ORDER BY v.location, v.vtname");

			// get info on ResultSet
			rsmd = rs.getMetaData();

			// get number of columns
			numCols = rsmd.getColumnCount();

            System.out.println(" ");
            System.out.println("Vehicles rented out today:");
			
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

				vlicence = rs.getString("vlicence");
				System.out.printf("%-15s", vlicence);

                vid = rs.getInt("vid");
				System.out.printf("%-15s", vid);
                
                make = rs.getString("make");
                System.out.printf("%-15s", make);

                model = rs.getString("model");
                System.out.printf("%-15s", model);
                
                year = rs.getInt("year");
                System.out.printf("%-15s", year);
                
                color = rs.getString("color");
                System.out.printf("%-15s", color);

                odometer = rs.getInt("odometer");
                System.out.printf("%-15s", odometer);
                
                status = rs.getString("status");
                System.out.printf("%-15s", status);
                
                vtname = rs.getString("vtname");
                System.out.printf("%-15s", vtname);

                location = rs.getInt("location");
                System.out.printf("%-15s", location);

                city = rs.getString("city");
                System.out.printf("%-15s\n", city);
            }
            
            rs = stmt.executeQuery(
            "SELECT v.location, v.vtname, COUNT(v.vid) AS \"NUMBER\", SUM(t.value) AS \"REVENUE\" " +
            "FROM vehicle v, rent r, return t " +
            "WHERE v.vlicence = r.vlicence AND r.rid = t.rid AND TO_CHAR(r.fromDate, 'YYYY-MM-DD') LIKE TO_CHAR(CURRENT_DATE, 'YYYY-MM-DD') AND v.location = " + branch + " " +
            "GROUP BY v.location, v.vtname " +
            "ORDER BY v.location, v.vtname");

			// get info on ResultSet
			rsmd = rs.getMetaData();

			// get number of columns
			numCols = rsmd.getColumnCount();

			System.out.println(" ");
            System.out.println("Vehicle types rented per branch:");
			
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

				location = rs.getInt("location");
                System.out.printf("%-15s", location);

                vtname = rs.getString("vtname");
                System.out.printf("%-15s", vtname);

                number = rs.getInt("number");
                System.out.printf("%-15s", number);

                revenue = rs.getDouble("revenue");
                System.out.printf("%-15s\n", revenue);
            }

            rs = stmt.executeQuery(
            "SELECT COUNT(*) AS \"NUMBER\", SUM(t.value) AS \"REVENUE\"" +
            "FROM vehicle v, rent r, return t " +
            "WHERE v.vlicence = r.vlicence AND r.rid = t.rid AND TO_CHAR(t.return_date, 'YYYY-MM-DD') LIKE TO_CHAR(CURRENT_DATE, 'YYYY-MM-DD') AND v.location = " + branch);

			System.out.println(" ");
            System.out.println("Total number of vehicle rented today and total revenue:");

            while(rs.next()) {
                number = rs.getInt("number");
                System.out.printf("%-15s\t", number);

                revenue = rs.getDouble("revenue");
                System.out.printf("$%-15s\n", revenue);
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

    private void editTable() {
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

    private void showRent() {
        int         rid;
        String      vlicence;
        int         dlicence;
        Timestamp   fromDate;
        Timestamp   untilDate;
        int         odometer;
        String      cardName;
        String      cardNo;
        Date        expDate;
        int         confNo;
		Statement   stmt;
		ResultSet   rs;
	   
		try {
			stmt = con.createStatement();

			rs = stmt.executeQuery("SELECT * FROM rent");

			// get info on ResultSet
			ResultSetMetaData rsmd = rs.getMetaData();

			// get number of columns
			int numCols = rsmd.getColumnCount();

			System.out.println(" ");
			
			// display column names;
			for (int i = 0; i < numCols; i++) {
                // get column name and print it
                if(rsmd.getColumnName(i+1).equals("FROMDATE") || rsmd.getColumnName(i+1).equals("TODATE") || rsmd.getColumnName(i+1).equals("CARDNO")) {
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

				rid = rs.getInt("rid");
                System.out.printf("%-15s", rid);
                
                vlicence = rs.getString("vlicence");
                System.out.printf("%-15s", vlicence);
                
                dlicence = rs.getInt("dlicence");
                System.out.printf("%-15s", dlicence);
                
                fromDate = rs.getTimestamp("fromDate");
                System.out.printf("%-25s", fromDate);
                
                untilDate = rs.getTimestamp("toDate");
                System.out.printf("%-25s", untilDate);
                
                odometer = rs.getInt("odometer");
                System.out.printf("%-15s", odometer);

                cardName = rs.getString("cardName");
                System.out.printf("%-15s", cardName);

                cardNo = rs.getString("cardNo");
                System.out.printf("%-25s", cardNo);

                expDate = rs.getDate("expDate");
                System.out.printf("%-15s", expDate);

                confNo = rs.getInt("confNo");
                System.out.printf("%-15s\n", confNo);
			}
	
		// close the statement; 
		// the ResultSet will also be closed
		stmt.close();
		}
		catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}	
    }

    private void showReturn() {
        int         rid;
        Timestamp   returnDate;
        int         odometer;
        String      fullTank;
        float       value;
		Statement   stmt;
		ResultSet   rs;
	   
		try {
			stmt = con.createStatement();

			rs = stmt.executeQuery("SELECT * FROM return");

			// get info on ResultSet
			ResultSetMetaData rsmd = rs.getMetaData();

			// get number of columns
			int numCols = rsmd.getColumnCount();

			System.out.println(" ");
			
			// display column names;
			for (int i = 0; i < numCols; i++) {
                // get column name and print it
                if(rsmd.getColumnName(i+1).equals("RETURN_DATE")) {
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

				rid = rs.getInt("rid");
                System.out.printf("%-15s", rid);
                
                returnDate = rs.getTimestamp("return_date");
                System.out.printf("%-25s", returnDate);
                
                odometer = rs.getInt("odometer");
                System.out.printf("%-15s", odometer);
                
                fullTank = rs.getString("fullTank");
                System.out.printf("%-15s", fullTank);

                value = rs.getFloat("value");
                System.out.printf("%-15s\n", value);
			}
	
		// close the statement; 
		// the ResultSet will also be closed
		stmt.close();
		}
		catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}	
    }

    private void showVehicle() {
        String      vlicence;
        int         vid;
        String      make;
        String      model;
        int         year;
        String      color;
        int         odometer;
        String      status;
        String      vtname;
        int         location;
        String      city;
		Statement   stmt;
		ResultSet   rs;
	   
		try {
			stmt = con.createStatement();

			rs = stmt.executeQuery("SELECT * FROM vehicle");

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
               
                
				vlicence = rs.getString("vlicence");
                System.out.printf("%-15s", vlicence);
                
                vid = rs.getInt("vid");
                System.out.printf("%-15s", vid);
                
                make = rs.getString("make");
                System.out.printf("%-15s", make);
                
                model = rs.getString("model");
                System.out.printf("%-15s", model);

                year = rs.getInt("year");
                System.out.printf("%-15s", year);
                
                color = rs.getString("color");
                System.out.printf("%-15s", color);
                
                odometer = rs.getInt("odometer");
                System.out.printf("%-15s", odometer);
                
                status = rs.getString("status");
                System.out.printf("%-15s", status);
                
                vtname = rs.getString("vtname");
                System.out.printf("%-15s", vtname);

                location = rs.getInt("location");
                System.out.printf("%-15s", location);
                
                city = rs.getString("city");
                System.out.printf("%-15s\n", city);
			}
	
		// close the statement; 
		// the ResultSet will also be closed
		stmt.close();
		}
		catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}	
    }

    private void showVehicleType() {
        String      vtname;
        String      feature;
        float       wrate;
        float       drate;
        float       hrate;
        float       wirate;
        float       dirate;
        float       hirate;
        float       krate;
		Statement   stmt;
		ResultSet   rs;
	   
		try {
			stmt = con.createStatement();

			rs = stmt.executeQuery("SELECT * FROM vt");

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
                vtname = rs.getString("vtname");
                System.out.printf("%-15s", vtname);
                
                feature = rs.getString("feature");
                System.out.printf("%-15s", feature);
                
                wrate = rs.getFloat("wrate");
                System.out.printf("%-15s", wrate);
                
                drate = rs.getFloat("drate");
                System.out.printf("%-15s", drate);

                hrate = rs.getFloat("hrate");
                System.out.printf("%-15s", hrate);

                wirate = rs.getFloat("wirate");
                System.out.printf("%-15s", wirate);
                
                dirate = rs.getFloat("dirate");
                System.out.printf("%-15s", dirate);

                hirate = rs.getFloat("hirate");
                System.out.printf("%-15s", hirate);

                krate = rs.getFloat("krate");
                System.out.printf("%-15s\n", krate);
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