package com.Ira.IraFinanceAPP;

import java.sql.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FinanceDatabase 
{

		Connection con=null;
		
	
		public FinanceDatabase() 
		{
		
			String url="jdbc:mysql://localhost:3306/irafinance?useUnicode=true&characterEncoding=UTF-8";
			String username="root";
			String password="ashish";
		
				try {
					Class.forName("com.mysql.jdbc.Driver"); 
					con=DriverManager.getConnection(url,username,password);
	    			}
					catch(Exception e)
						{
							System.out.println(e);
						}
	}

		
		
		
/*1------------------------OK----- FOR REGISTER NEW USER------------------------------*/
		   		
		public String create(RegisterUser r2) {
			
			int flag=0,flag1=0;
			
			JSONObject jo = new JSONObject();
			
			System.out.println("1.Register");
		 
			 /*------Checking Mobile or Gst alrady exist-------*/
			String login= "select mobilenumber,gstnumber from registration";
			
			try
			{
				Statement st=con.createStatement();
				ResultSet rs=st.executeQuery(login);
		    
		    		while(rs.next())
		    			if(rs.getString(1).equals(r2.getMobilenumber()))
		    				flag=1;
		    					if(flag==1)
		    					{
		    						jo.put("uname", "null");
		    						jo.put("check", "mexist");
		    						return jo.toString();
		    					}
		    						
		    
		    	Statement st1=con.createStatement();
		    	ResultSet rs1=st1.executeQuery(login);
		    
		    		while(rs1.next())
		    			if(rs1.getString(2).equals(r2.getGstnumber()))
		    				flag=2;
		    					if(flag==2)
		    					{
		    						jo.put("uname", "null");
		    						jo.put("check", "gexist");
		    						return jo.toString();
		    					}
		    						
		    
			} 
			catch(Exception e)
				{	flag=3;
					System.out.println(e);
				
				}
				if(flag==3)
					return "error1";
			
		    	
		    	
               /*-----------Insert Data into Registration Table-------------------*/
		    
		    	
		    String reg="insert into registration(pswd,shopname,"
						+ "address,mobilenumber,emailid,gstnumber,acctlocked,"
						+ "subStartdate,subEnddate)  values(?,?,?,?,?,?,?,?,?)";
			
		    try 
		    {
		    	
		    	PreparedStatement st2=con.prepareStatement(reg);
			
		    	
		    	st2.setString(1, r2.getPassword());
		    	st2.setString(2, r2.getShopname());
		    	st2.setString(3, r2.getAddress());
			
		    	st2.setString(   4,  r2.getMobilenumber());
		    	st2.setString(5, r2.getEmailid());
		    	
		    	st2.setString(6, r2.getGstnumber());
		    	st2.setString(7, r2.getAcctlocked());
		    	
		    	st2.setString(8, r2.getSubstartdate());
		    	st2.setString(9, r2.getSubenddate());
			
		    	st2.executeUpdate();
		
			    flag=4;
			}
			catch(Exception e)
				{
					flag=5;
					System.out.println(e);
				}
		    	if(flag==5)
		    			return "error2";
			
			/*----------Inserting Data into Logincontrol---------*/
		    	
		    	int id=0;
		    	if(flag==4)
		    		{	/*--Getting Subid from registration as username------------*/
		    			 String mo=r2.getMobilenumber();
		    			 
		    		     String regis="select subid from registration where mobilenumber="+mo;
		    		     
		    		     try
		    		     {
		    		    	 Statement st=con.createStatement();
		    		    	 ResultSet rs=st.executeQuery(regis);
		    		    	 
		    		    	 rs.next();
		    		    	 id=rs.getInt(1);
		    		    	 
		    		    	 jo.put("uname", id);
		    		    	 jo.put("check", "success");
		    		     }
		    		     catch(Exception e)
		    		     {   flag=6;
		    		    	 System.out.println(e);
		    		     }
		    		     	if(flag==6)
		    		     		return "error3";
		    		     
		    		     	
		    			String log="insert into logincontrol(username,pswd,acctlocked,forcechgpwd,access,forcelogin)"
							+ "values(?,?,?,?,?,?)";
		    			
		    			try
		    			{
		    				PreparedStatement st1=con.prepareStatement(log);
		    				
		    				st1.setInt(1, id);
		    				st1.setString(2, r2.getPassword());
		    				st1.setString(3, r2.getAcctlocked());
		    				st1.setString(4, "false");
		    				st1.setInt(5, 15);
		    				st1.setString(6, "true");
		    				
		    				st1.executeUpdate();
		    				
		    				flag1=8;
		    			}
		    			catch(Exception e)
		    			{	flag=7;
		    				System.out.println(e);
		    			}
		    				if(flag==7)
		    					return "error4";
		    		}
		    	else 
		    		return "notregister";
			
		    	
		    	
		    	if(flag==4 && flag1==8)
		    		return jo.toString();
		    	System.out.println(id);
		    	return "error5";
		
	
		}
		
		
 
 
 /*2.------------------------Login User--------------------------------------------*/
 
		public String loginUser(String username,String password)
		{
			
			int flag=0,temp=0;
			System.out.println("2.Login");
	 
			JSONObject jo=new JSONObject();
	 
			String log="select *from logincontrol";
	 
				
			String reg="select subid from registration";
			
				try
				{
				Statement st1=con.createStatement();
				ResultSet rs1=st1.executeQuery(reg);
				
					while(rs1.next())
					{
						if(username.equals(rs1.getString(1)))
						{
							temp=1;
						
						}
					}
				
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
				
				
				if(temp==1)
				{
				
					try
						{
							Statement st=con.createStatement();
							ResultSet rs=st.executeQuery(log);
	 	
								while(rs.next())
									{   //flag=1;
	 		
										if(username.equals(rs.getString(1)))
											{     
												flag=2;
											//System.out.println("h"); 
	 		
												if(password.equals(rs.getString(2)))
												
													{
														flag=4;
					
														jo.put("parentuser", rs.getInt(1));
														jo.put("check","success" );
														jo.put("accl",rs.getString(3));
														jo.put("forcep",rs.getString(4));
														jo.put("access", rs.getInt(5));
			 		  
														return jo.toString();
					
													}
	 				
											}
									}
	 	
									if(flag==0)
										{
											jo.put("parentuser", "null");
											jo.put("check", "uincorrect" );
											jo.put("accl",  "null");
											jo.put("forcep",  "null");
											jo.put("access","null");
			  
											return jo.toString();
										}
			 
									if(flag==2)
										{
											jo.put("parentuser", "null");
											jo.put("check", "pincorrect" );
											jo.put("accl",  "null");
											jo.put("forcep",  "null");
											jo.put("access","null");
			  
											return jo.toString();
										}
	 	
						}
						catch(Exception e)
							{
								flag=3;
								System.out.println(e);
							}
	 
							if(flag==3)
								return "error1";
					}
				
				
				else
				{
					System.out.println("else");
					String login="select *from logincontrol";
					
					String s1="'"+username+"'";
					String subu="select subid from subuser where childUserName="+s1;
					
					int id=0;
					try
					{
					Statement st3=con.createStatement();
					ResultSet rs3=st3.executeQuery(subu);
						rs3.next();
						id=rs3.getInt(1);
					
					}
					catch(Exception e)
					{
						System.out.println(e);
					}
					
					int flag1=0;
					
					try
					{
						Statement st2=con.createStatement();
						ResultSet rs2=st2.executeQuery(login);
						
						while(rs2.next())
						{
							if(username.equals(rs2.getString(1)))
							{
										flag1=1;
									if(password.equals(rs2.getString(2)))
										{
											flag1=2;
										
											jo.put("parentuser", id);
											jo.put("check","success" );
											jo.put("accl",rs2.getString(3));
											jo.put("forcep",rs2.getString(4));
											jo.put("access", rs2.getInt(5));
										
											return jo.toString();
											
	
										}

							}
								
						}
						
						if(flag1==0)
						{
							jo.put("parentuser", "null");
							jo.put("check", "uincorrect" );
							jo.put("accl",  "null");
							jo.put("forcep",  "null");
							jo.put("access","null");
  
							return jo.toString();
						}
						
						if(flag1==2)
						{
							jo.put("parentuser", "null");
							jo.put("check", "pincorrect" );
							jo.put("accl",  "null");
							jo.put("forcep",  "null");
							jo.put("access","null");
  
							return jo.toString();
						}
						
						
					}
					catch(Exception e)
					{
						System.out.println(e);
					}
				}
	  
						return "error2";
	 
		}
 
 
 
 /*3.-------------------FORGET USER NAME--------------------------------------------*/
 
		public String forgetUs(String mobilenumber)
			{
			System.out.println("3.Foreget User Name");
				
				int flag=0;
				String mo="select subid,mobilenumber,emailid,acctlocked from registration";
		
				JSONObject jo=new JSONObject();
		
					try
						{
							Statement st=con.createStatement();
							ResultSet rs=st.executeQuery(mo);
	     
								while(rs.next())
									{
										if(rs.getString(2).equals(mobilenumber))
											{
												if(rs.getString(4).equals("false"))
													{
														jo.put("check", "myes");
														jo.put("uname", rs.getString(1));
														jo.put("mnum", rs.getString(2));
														jo.put("email", rs.getString(3));
	    			 
														return jo.toString();
													}
												else
													{
														jo.put("check", "ulock");
														jo.put("uname", "null");
														jo.put("mnum", "null");
														jo.put("email", "null");
	 	    		
														return jo.toString();
													}
											}
	    	
									}
	    
						}
						catch (Exception e) 
							{
								flag=1;
								System.out.println(e);
							}
		
							if(flag==1)
								return "error1";
				try
					{
						jo.put("check", "mno");
						jo.put("uname", "null");
						jo.put("mnum", "null");
						jo.put("email", "null");
	     
	     
					}
					catch(Exception e)
						{
							flag=2;
							System.out.println(e);
						}
	
						if(flag==2)
							return "error2";
		
				return jo.toString();
						
			}
	
 /*4.-----------------------------FORGET PASSWORD-------------------------------------------*/
 
 
 
		public String forgetPd(String username)
			{
				System.out.println("4.Forget Password");
	 
				String un="select subid,mobilenumber,emailid,acctlocked from registration";
		
				JSONObject jo=new JSONObject();
		
					try
						{
							Statement st=con.createStatement();
							ResultSet rs=st.executeQuery(un);
	     
								while(rs.next())
									{
										if(rs.getString(1).equals(username))
											{
												if(rs.getString(4).equals("false"))
													{
														jo.put("ckeck", "uyes");
														jo.put("accl", rs.getString(4));
														jo.put("mnum", rs.getString(2));
														jo.put("email", rs.getString(3));
	    			  
	    			  
														return jo.toString();
													}
	    		 
													jo.put("ckeck", "uyes");
													jo.put("accl", "true");
													jo.put("mnum", "null");
													jo.put("email", "null");
    			  
													return jo.toString();
	    		 
											}
	    	  
									}
						}
						catch(Exception e)
							{
								System.out.println(e);
							}
	 	
			try
				{
						jo.put("ckeck", "uno");
						jo.put("accl", "null");
						jo.put("mnum", "null");
						jo.put("email", "null");
	 	
				}
				catch(Exception e)
						{
							System.out.println(e);
						}
				return jo.toString();
	 
			}
 
 /*5.-------------------------------TEMP PASSWORD-------------------------------*/
		
		
		public String tempPass(String  username,String password)
			{
				System.out.println("5.Temp Password");
			
				int flag=0,temp=0;
				String s="'"+username+"'";
				String rg="update registration set pswd=? where subid=?";
				
				String lc="update logincontrol set pswd=? ,forcechgpwd=? where username="+s;
				
				
				/*-------------------CHECKING USER EXIST OR NOT---------------------------*/
				String main="select subid from registration";
					
					try
					{
					Statement st=con.createStatement();
					ResultSet rs=st.executeQuery(main);
					
						while(rs.next())
						{
							if(username==rs.getString(1))
							{
								temp=1;
							}
						}
						if(temp==0)
							return "unotexist";
					
					}
					catch(Exception e)
					{
						System.out.println(e);
					}
					
					
					
					try
						{
				
				
							//Statement st3=con.createStatement();
							PreparedStatement ps = con.prepareStatement(rg);
							PreparedStatement fcp = con.prepareStatement(lc);
			
							ps.setString(1, password);
							ps.setString(2, username);
			
							fcp.setString(1, password);
							fcp.setString(2, "true");
							//fcp.setString(3, username);
			
							ps.executeUpdate();
							fcp.executeUpdate();
		      
						}
					catch(Exception e)
						{
							flag=1;
							System.out.println(e);
						}
					if(flag==1)
						return "error";
					
						return "success";
		}	
		
		
/*6.---------------------------------RESET PASSWORD---------------------------------*/		
		
 
		public String resetPass(String username,String password)
		{
			System.out.println("6.Reset Password");
			//int flag=0;
				String rege="select subid from registration";
			
					try
						{
							Statement st=con.createStatement();
							ResultSet rs=st.executeQuery(rege);
							
							/*-------CHECKING FOR MAIN USER-----*/ 
								while(rs.next())
									if(rs.getString(1).equals(username))
										{
											int flag1=0;
											String rg="update registration set pswd=? where subid=?";
											String lc="update logincontrol set pswd=? ,forcechgpwd=? where username=?";
		    		
		    		
											try
												{
		    			
		    			
												//Statement st3=con.createStatement();
												PreparedStatement ps = con.prepareStatement(rg);
												PreparedStatement fcp = con.prepareStatement(lc);
		    		
												ps.setString(1, password);
												ps.setString(2, username);
		    		
												fcp.setString(1, password);
												fcp.setString(2, "false");
												fcp.setString(3, username);
		    		
												ps.executeUpdate();
												fcp.executeUpdate();
		    	      
												}
											catch(Exception e)
												{
													//System.out.println("AKJ");
													flag1=1;
													System.out.println(e);
												}
											if(flag1==1)
												return "nreset";   /*--not reset---*/
												return "reset";
		    		
										}
						}

				    	/*--------Main Exception-------*/
							catch(Exception e)
							{  // flag=1;
								System.out.println(e);
							}
							return "error";
					
		  }
		
		
		
/*7.-----------------------SUB USER REGISTRATION--------------------*/
		
		public String createSuser(String user,String username,String pswd,String access,
				                   String substartdate,String subenddate)
			{ 
				System.out.println("7.User Registration");
				int subid=0 ,flag=0;
				
				String control="select  childUserName from subuser";
				String mu="select  *from registration where subid="+user;
				String su="insert into subuser values(?,?,?,?,?,?,?,?)";
				
				String log="insert into logincontrol values(?,?,?,?,?,?,?)";
			
				RegisterUser r=new RegisterUser();
			
				r.setUsername(username);
				r.setPassword(pswd);
			
				r.setSubstartdate(substartdate);
				r.setSubenddate(subenddate);
			
					try
						{
							Statement st3=con.createStatement();
							ResultSet rs1=st3.executeQuery(control);
		    
								while(rs1.next())
									if(rs1.getString(1).equals(r.getUsername()))
										return "subexist";
						}
					catch(Exception e)
						{   flag=1;
							System.out.println(e);
						}
			
					try {
							Statement st=con.createStatement();
							ResultSet rs=st.executeQuery(mu);
							rs.next();
							subid=rs.getInt(1);
							System.out.println(subid);
		     
						}
					catch(Exception e)
					{
						flag=1;
						System.out.println(e);
					}
			
			
					try {
							PreparedStatement st1=con.prepareStatement(su);
							PreparedStatement st2=con.prepareStatement(log);
				
							st1.setInt(1, subid);
							st1.setString(2, r.getUsername());
							st1.setString(3, r.getPassword());
							st1.setString(4, "false");
							st1.setString(5, r.getSubstartdate());
							st1.setString(6, r.getSubenddate());
							st1.setString(7, "true");
							st1.setString(8, access);
		        
							st2.setString(1, r.getUsername());
							st2.setString(2, r.getPassword());
							st2.setString(3, "false");
							st2.setString(4, "true");
							st2.setString(5, access);
							st2.setString(6, "true");
							st2.setInt(7, subid );
							
							st1.executeUpdate(); 
							st2.executeUpdate();
						}
					catch(Exception e)
						{   flag=1;
							System.out.println(e);
						}
					if(flag==1)
						return "error";
							return "success";
			}		
		
		
		
/*8.-------------------------------GET ALL SUB USER NAME BY MAIN USER NAME-----------------------*/
			
		public String allSubUser(String username)
			{
			
			System.out.println("8.All Sub User Name");
				int id=0, flag=0;
				
				JSONObject jo = new JSONObject();
				JSONArray ja = new JSONArray();
				
					try {
							jo.put("subusername", ja);
						}
					catch (JSONException e1) 
						{
					// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				
				
				/*-------Geting subid of Main User from Registration--*/
				
						String main="select subId from registration";
				
						try
							{
					
								Statement st=con.createStatement();
								ResultSet rs=st.executeQuery(main);
			     
								rs.next();
								id=rs.getInt(1);
								//System.out.println("ASSS");
								//System.out.println(id);
			     
							}
						catch(Exception e)
							{
								flag=1;
								//System.out.println("mainusernotexist");
					
								System.out.println(e);
							}
						if(flag==1)
							return jo.toString();
				
				
				
				
				      /*----Fetching all user name from subuser table using main user subid--*/
				
						String sub = "select subId,childUserName from subuser where subId="+username;
							
						try
							{
					
								Statement st1=con.createStatement();
								ResultSet rs1=st1.executeQuery(sub);
			     	
								while(rs1.next())
									{
										//int s=rs1.getInt(1);
											if(username.equals(rs1.getString(1))) 
												{
			    		
			    		
													ja.put(rs1.getString(2));
			        
			            
													//System.out.println(ja);
												}
			    	
									}
			    	

							}
						catch(Exception e)
							{
								flag=2;
					
								System.out.println(e);
							}
						if(flag==2)
							return jo.toString();
							//System.out.println(jo);

							return jo.toString();
			}
		
		
		

/*9.---------------------------------FOR EDIT USER ACCESS-----------------------------------------*/
		
		public String editSuser(String username,String paswd,String access)
		{
			System.out.println("9.Edit User Access");
			int flag=0,temp=0;
			
			String sub="update subuser set childPassword=?, access=? where childUserName=?";
			String login="update logincontrol set pswd=?,access=? where userName=?";
			
				String subuser="select childUserName from subuser ";
			
			
				try
				{
					Statement st=con.createStatement();
					ResultSet rs=st.executeQuery(subuser);
					
					while(rs.next())
					{
						if(rs.getString(1).equals(username))
						{
							temp=1;
							break;
						}
					}
					
					if(temp==0)
						return "uincorrect";
				}
				catch(Exception e)
				{
					System.out.println(e);
					
				}
			
			try
				{
					PreparedStatement ps = con.prepareStatement(sub);
					PreparedStatement ps1 = con.prepareStatement(login);
			
					ps.setString(1, paswd);
					ps.setString(2, access);
					ps.setString(3, username);
			
					ps1.setString(1, paswd);
					ps1.setString(2, access);
					ps1.setString(3, username);
			
					ps.executeUpdate();
					ps1.executeUpdate();
				}
				catch(Exception e)
					{
						flag=1;
						System.out.println(e);
					}
				if(flag==1)
					return "error";
			
				return "success";
		}		
		

		
		
/*10.--------------------------------GET SUB USER ACCESS  FROM MAIN USER-----------------------------------*/
		public String getSubAccess(String username)
		{
			System.out.println("10.Get Sub User Access");
				int flag=0;
				String suser="select childUserName from subuser";
				String slogin="select *from logincontrol";
			 
			 
				JSONObject jo=new JSONObject();
			 
					try
						{
							Statement st=con.createStatement();
							ResultSet rs=st.executeQuery(suser);
		     
							Statement st1=con.createStatement();
							ResultSet rs1=st1.executeQuery(slogin);
							
							while(rs.next())
								if(rs.getString(1).equals(username))
									{    
										flag=1;
										break;
									}
							
							if(flag==1)
								{
									while(rs1.next())
										if(rs1.getString(1).equals(username))
											if(rs1.getString(3).equals("false"))
												{   flag=2;
		    		
													jo.put("check","suexist");
													jo.put("accl", "false");
													jo.put("access", rs1.getString(5));
		    			 
													return jo.toString();
												}
								}
		     	
		     	
						}
					catch(Exception e)
						{   
							System.out.println(e);
						}
			 
			 
					if(flag==0)
						{
							try
							{
							jo.put("check","udoesnotexist");
							jo.put("accl", "null");
							jo.put("access","null");
				 
				
							return jo.toString();
							}
							catch(Exception e)
							{
								System.out.println(e);
							}
						}
					if(flag==1)
						{
							try
							{
							jo.put("check","uexist");
							jo.put("accl", "true");
							jo.put("access","null"); 
				 
			 
							return jo.toString();
							}
							catch(Exception e)
							{
								System.out.println(e);
							}
						}	
					
					return "error";
		
		}	
		
		
		
	/*----------------------------------------HINDI-------------------------------------------*/
		
		public String createH(String name)
		{
			
			String hin="insert into hindi values(?)";
			
			
			try
			{
			PreparedStatement ps = con.prepareStatement(hin);
			
			ps.setString(1, name);
			ps.executeUpdate();
			}
			catch(Exception e)
			{
				System.out.print(e);
			}
			
			return name;
		}
		

		public String showH()
		{
			String s="select data from hindi";
			JSONObject jo=new JSONObject();
			JSONArray ja=new JSONArray();
			try
			{
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery(s);
			
			while(rs.next())
			{
				ja.put(rs.getString(1));
				
			}
			jo.put("name", ja);
			}
			catch(Exception e)
			{
				System.out.println(e);
			}

			return jo.toString();
		}
		
		
		
/*=------------------------------Shrink data-------------------------------------------*/
		
		
		public String getAddItem(String str)
		{
			int flag=0;
			int temp=0;
			int count=0,addnew=0;
			//String str="{\"subid\":[1000],\"itemid\":[AF1],\"itemname\":[APPLE],\"itemprice\":[20],\"measurement\":[per kg],\"itemcategory\":[Fruit],\"gstcategory\":[igst],\"startdate\":[2017-12-16],\"enddate\":[2018-12-16],\"count\":[0],\"version\":[1]}";
			
			
			
			
			
			
			
			/*--------------------selecting last item in item table--------------*/
			
			String key="select max(id) from itemmain";
			
			try
			{
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery(key);
			rs.next();
			temp=rs.getInt(1);
			System.out.println(temp);
			
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			
			
			
			/*------------------------inserting data into itemmain-------------------*/
			
			
			
			
			
			String s="insert into itemmain (subId,itemid,itemname,itemprice,measurement,itemcategory,gstcategory,startdate,enddate,count,version) values(?,?,?,?,?,?,?,?,?,?,?)";
			
			try
			{
				//System.out.println(str);
				JSONObject obj=new JSONObject(str);
				
				
				JSONArray arr = obj.getJSONArray("subid");
				JSONArray arr1 = obj.getJSONArray("itemid");
				JSONArray arr2= obj.getJSONArray("itemname");
				JSONArray arr3 = obj.getJSONArray("itemprice");
				JSONArray arr4 = obj.getJSONArray("measurement");
				JSONArray arr5 = obj.getJSONArray("itemcategory");
				JSONArray arr6 = obj.getJSONArray("gstcategory");
				JSONArray arr7 = obj.getJSONArray("startdate");
				JSONArray arr8 = obj.getJSONArray("enddate");
				JSONArray arr9 = obj.getJSONArray("count");
				JSONArray arr10 = obj.getJSONArray("version");
				
			
			    String itemtable="select itemid,version,enddate from itemmain";
			
			   
				
				
				for(int i=0; i<arr.length() && i<arr1.length() && i<arr2.length() && i<arr3.length() && i<arr4.length() && i<arr5.length() && i<arr6.length() && i<arr7.length() && i<arr8.length() &&i<arr9.length() &&i<arr10.length()  ; i++)
				{
					int iditem=0,itemversion=0,itemenddate=0;
					
					PreparedStatement ps = con.prepareStatement(s);
					
					 	Statement st1=con.createStatement();
						ResultSet rs1=st1.executeQuery(itemtable);
					
					while(rs1.next())
						{
							if(rs1.getString(1).equals(arr1.getString(i)))
							{	
								System.out.println("id= ");
								iditem=1;
							
								if(rs1.getString(2).equals(arr10.getString(i)))
										itemversion=1;
								if(rs1.getString(3).equals(arr8.getString(i)))
											itemenddate=1;
							
							
								if(iditem==1 && itemversion==1 && itemenddate==1)
								{
									System.out.println("id= ver= date=");
									break;
								}
							
								
								if(iditem==1 && itemversion==1 && itemenddate!=1)
								{
									System.out.println("hiiii");
									
								
									String dd=arr8.getString(i);
								    String idd=arr1.getString(i);
								    String ve=arr10.getString(i);
									String dd1="'"+dd+"'";
									String idd1="'"+idd+"'";
									String ver="'"+ve+"'";
									String up="update itemmain set enddate="+dd1 +"where itemid="+idd1 + "and version="+ver;
								
									PreparedStatement ps2 = con.prepareStatement(up);
								
								
									ps2.executeUpdate();
								    
									count=0;
									
									break;
								}
							
								if(iditem==1 && itemversion!=1)
									{
									System.out.println("id= ver!=");
									count=1;
									
									//break;
									}
							
						}
					
						}
					
					
					if(count==1 || iditem==0 )
					{
						ps.setInt(1,arr.getInt(i));
						ps.setString(2, arr1.getString(i));
						ps.setString(3, arr2.getString(i));
						ps.setString(4, arr3.getString(i));
						ps.setString(5, arr4.getString(i));
						ps.setString(6, arr5.getString(i));
						ps.setString(7, arr6.getString(i));
						ps.setString(8, arr7.getString(i));
						ps.setString(9, arr8.getString(i));
						ps.setString(10, arr9.getString(i));
						ps.setString(11, arr10.getString(i));
					
					
						ps.executeUpdate();
					}
				
				}
			}
			catch(Exception e)
			{
				flag=1;	
				System.out.println(e);
			}
		
				
			
			/*-----------------------delete data if failed----------------*/
			if(flag==1)
			{
				String del="delete from itemmain where id>"+temp;
				
				try
				{
				PreparedStatement ps = con.prepareStatement(del);
				
				ps.executeUpdate();
				}
				catch(Exception e)
	 			{
					System.out.println(e);
				}
			}
			
			
			/*------------------Sending Responce---------------*/
			
			JSONObject jo=new JSONObject();
			try
			{
			
			
			if(flag==1)
			{
				jo.put("check", "fail");
				return jo.toString();
			}
			else
			{
				jo.put("check", "success");
			}
			
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			
			return	jo.toString();
			
		}
		
	
		
/*12.--------------------------GET ITEM DETAIL BY SUB ID WHICH ITEM IS ACTIVE----------------------*/		
		
	
		public String getItem(String id)
		{
			int flag=0;
			String dat="select subdate(curdate(),1) from dual";
			String datestring=null;
			try
			{
				Statement st1=con.createStatement();
				ResultSet rs1=st1.executeQuery(dat); 
				rs1.next();
				String dat1=rs1.getString(1);
				datestring="'"+dat1+"'";
				//System.out.println(dat1);
				
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			
			//System.out.println(datestring);
			String itemdetail="select *from itemmain where enddate>="+datestring +"and subid="+id;
			
			try
			{
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery(itemdetail); 
			
			JSONObject jo=new JSONObject();
			JSONArray ja=new JSONArray();
			JSONArray ja1=new JSONArray();
			JSONArray ja2=new JSONArray();
			JSONArray ja3=new JSONArray();
			JSONArray ja4=new JSONArray();
			JSONArray ja5=new JSONArray();
			JSONArray ja6=new JSONArray();
			JSONArray ja7=new JSONArray();
			JSONArray ja8=new JSONArray();
			JSONArray ja9=new JSONArray();
			JSONArray ja10=new JSONArray();
			
			jo.put("subid", ja);
			jo.put("itemid", ja1);
			jo.put("itemname", ja2);
			jo.put("itemprice", ja3);
			jo.put("measurement", ja4);
			jo.put("itemcategory", ja5);
			jo.put("gstcategory", ja6);
			jo.put("startdate", ja7);
			jo.put("enddate", ja8);
			jo.put("count", ja9);
			jo.put("version", ja10);
			while(rs.next())
			{
				
				ja.put(rs.getInt(2));
				ja1.put(rs.getString(3));
				ja2.put(rs.getString(4));
				ja3.put(rs.getString(5));
				ja4.put(rs.getString(6));
				ja5.put(rs.getString(7));
				ja6.put(rs.getString(8));
				ja7.put(rs.getString(9));
				ja8.put(rs.getString(10));
				ja9.put(rs.getString(11));
				ja10.put(rs.getString(12));
				
				//System.out.println(rs.getInt(2)+"  "+rs.getString(10));
				
			}
			return jo.toString();
			}
			catch(Exception e)
			{
				flag=1;
				System.out.println(e);
			}
			if(flag==1)
				return "error";
			return "success";
		}
		
		
		
		
/*13.-------------------LOGin DETAIL BY USER NAME----------------------------------------*/		
		
	
		public String getLoginDetail(String detail)
		{
			int count=0;
			
			String log="select *from logincontrol";
			try
			{
				
				
				JSONObject obj=new JSONObject(detail);
				JSONArray arr = obj.getJSONArray("username");
				
				
				JSONObject jo=new JSONObject();
				
				JSONArray ja=new JSONArray();
				JSONArray ja1=new JSONArray();
				JSONArray ja2=new JSONArray();
				JSONArray ja3=new JSONArray();
				JSONArray ja4=new JSONArray();
				JSONArray ja5=new JSONArray();
				JSONArray ja6=new JSONArray();
				
				jo.put("username", ja);
				jo.put("password", ja1);
				jo.put("accl", ja2);
				jo.put("fchg", ja3);
				jo.put("access", ja4);
				jo.put("forcelogin", ja5);
				jo.put("parentid", ja6);
				
				for(int i=0; i<arr.length(); i++)
				{
					count=0;
					
					Statement st=con.createStatement();
					ResultSet rs=st.executeQuery(log); 
					
					while(rs.next())
					{
						if(arr.getString(i).equals(rs.getString(1)))
						{
							
							ja.put(rs.getString(1));
							ja1.put(rs.getString(2));
							ja2.put(rs.getString(3));
							ja3.put(rs.getString(4));
							ja4.put(rs.getString(5));
							ja5.put(rs.getString(6));
							ja6.put(rs.getString(7));
						   
							count=1;
						
						}
						
						
					}
					if(count==0)
					{
						ja.put("null");
						ja1.put("null");
						ja2.put("null");
						ja3.put("null");
						ja4.put("null");
						ja5.put("null");
						ja6.put("null");
						
					}
					
					
				}
				
				return jo.toString();
				
				
				
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			
			
			return "success";
		}
		
		
		
		
/*14.------------------------DETAILS OF REPORT---------------------------------------------*/		
		
	public String getReportHDR(String date1,String date2)
	{
		
		String dat1="'"+date1+"'";
		String dat2="'"+date2+"'";
		
		String hdr="select *from invoice_hdr where  invoice_dt between "+dat1+" and "+dat2;
		
		JSONObject jo=new JSONObject();
		
		try
		{
		JSONArray ja=new JSONArray();
		JSONArray ja1=new JSONArray();
		JSONArray ja2=new JSONArray();
		JSONArray ja3=new JSONArray();
		JSONArray ja4=new JSONArray();
		JSONArray ja5=new JSONArray();
		JSONArray ja6=new JSONArray();
		JSONArray ja7=new JSONArray();
		JSONArray ja8=new JSONArray();
		JSONArray ja9=new JSONArray();
		JSONArray ja10=new JSONArray();
		JSONArray ja11=new JSONArray();
		JSONArray ja12=new JSONArray();
		JSONArray ja13=new JSONArray();
		JSONArray ja14=new JSONArray();
		
		jo.put("userid", ja);
		jo.put("invoice_id", ja1);
		jo.put("invoice_dt", ja2);
		jo.put("invoice_desc", ja3);
		jo.put("customer_name", ja4);
		jo.put("customer_gst", ja5);
		jo.put("customer_mob", ja6);
		jo.put("paid_flag", ja7);
		jo.put("total_disc_amt", ja8);
		jo.put("paid_via", ja9);
		jo.put("payment_Ref", ja10);
		jo.put("total_inv_amt", ja11);
		jo.put("cgst_amt", ja12);
		jo.put("sgst_amt", ja13);
		jo.put("igst_amt" , ja14);
		
		
		
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery(hdr);
		
		
		while(rs.next())
		{
			System.out.println(rs.getInt(1));
			
			ja.put(rs.getString(1));
			ja1.put(rs.getInt(2));
			ja2.put(rs.getDate(3));
			ja3.put(rs.getString(4));
			ja4.put(rs.getString(5));
			ja5.put(rs.getDate(6));
			ja6.put(rs.getString(7));
			ja7.put(rs.getString(8));
			ja8.put(rs.getDouble(9));
			ja9.put(rs.getString(10));
			ja10.put(rs.getString(11));
			ja11.put(rs.getDouble(12));
			ja12.put(rs.getDouble(13));
			ja13.put(rs.getDouble(14));
			ja14.put(rs.getDouble(15));
			
		}
		
		return jo.toString();
		
		
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	
		
		
		
		return "success";
	}
		
	
	
/*15.--------------------DETAILS OF REPORT By INVOICE_ID---------------------------*/	
	
	public String getInvoiceDetail(String invoiceid)
	{
		
		
         String hdr="select *from invoice_hdr where  invoice_id="+invoiceid;
		
		JSONObject jo=new JSONObject();
		
		try
		{
		JSONArray ja=new JSONArray();
		JSONArray ja1=new JSONArray();
		JSONArray ja2=new JSONArray();
		JSONArray ja3=new JSONArray();
		JSONArray ja4=new JSONArray();
		JSONArray ja5=new JSONArray();
		JSONArray ja6=new JSONArray();
		JSONArray ja7=new JSONArray();
		JSONArray ja8=new JSONArray();
		JSONArray ja9=new JSONArray();
		JSONArray ja10=new JSONArray();
		JSONArray ja11=new JSONArray();
		JSONArray ja12=new JSONArray();
		JSONArray ja13=new JSONArray();
		JSONArray ja14=new JSONArray();
		
		jo.put("userid", ja);
		jo.put("invoice_id", ja1);
		jo.put("invoice_dt", ja2);
		jo.put("invoice_desc", ja3);
		jo.put("customer_name", ja4);
		jo.put("customer_gst", ja5);
		jo.put("customer_mob", ja6);
		jo.put("paid_flag", ja7);
		jo.put("total_disc_amt", ja8);
		jo.put("paid_via", ja9);
		jo.put("payment_Ref", ja10);
		jo.put("total_inv_amt", ja11);
		jo.put("cgst_amt", ja12);
		jo.put("sgst_amt", ja13);
		jo.put("igst_amt" , ja14);
		
		
		
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery(hdr);
		
		
		while(rs.next())
		{
			ja.put(rs.getString(1));
			ja1.put(rs.getInt(2));
			ja2.put(rs.getDate(3));
			ja3.put(rs.getString(4));
			ja4.put(rs.getString(5));
			ja5.put(rs.getDate(6));
			ja6.put(rs.getString(7));
			ja7.put(rs.getString(8));
			ja8.put(rs.getDouble(9));
			ja9.put(rs.getString(10));
			ja10.put(rs.getString(11));
			ja11.put(rs.getDouble(12));
			ja12.put(rs.getDouble(13));
			ja13.put(rs.getDouble(14));
			ja14.put(rs.getDouble(15));
			
		}
		
		return jo.toString();
		
		
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		
		return "success";
	}
	

/*16.-------------------------GETING CUSTOMER DETAIL BY MOBILE NUMBER-------------------------------*/	
	
	public String getCustomerDetails(String mobile)
	{
		
		//String mobilestring="'"+mobile+"'";
		String invoicehdr="select  customer_name,customer_mob,  customer_gst from invoice_hdr where  customer_mob="+mobile;
		
		JSONObject jo=new JSONObject();
		
		
		try
		{
		
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery(invoicehdr);
			if(rs.next()==false)
			{
				jo.put("customermobile", "notexist");
				jo.put("name", "null");
				jo.put("gst", "null");
				
				return jo.toString();
				
			}
				
			rs.next();
			
			jo.put("customermobile", rs.getString(2));
			jo.put("name", rs.getString(1));
			jo.put("gst", rs.getString(3));
			
			return jo.toString();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return "ok";
	}
	
	
	
/*17.-------------------------SYNCH INVOICE------------------------------------------------------*/	
	
	
	public String getInvoiceHDR_Line(String item)
	{
		 String hdr="insert into invoice_hdr(userid) values(?)";
		 
		// System.out.println(item);
		 
		 try
		 {
			 JSONObject obj=new JSONObject(item);
			
			 JSONArray arr = obj.getJSONArray("userid");
			 JSONArray arr1 = obj.getJSONArray("invoice_id");
			 JSONArray arr2 = obj.getJSONArray("invoice_dt");
			 JSONArray arr3 = obj.getJSONArray("invoice_desc");
			 JSONArray arr4 = obj.getJSONArray("customer_name");
			 JSONArray arr5 = obj.getJSONArray("customer_gst");
			 JSONArray arr6 = obj.getJSONArray("customer_mob");
			 JSONArray arr7 = obj.getJSONArray("paid_flag");
			 JSONArray arr8 = obj.getJSONArray("total_disc_amt");
			 JSONArray arr9 = obj.getJSONArray("paid_via");
			 JSONArray arr10 = obj.getJSONArray("payment_Ref");
			 JSONArray arr11 = obj.getJSONArray("total_inv_amt");
			 JSONArray arr12 = obj.getJSONArray("cgst_amt");
			 JSONArray arr13 = obj.getJSONArray("sgst_amt");
			 JSONArray arr14 = obj.getJSONArray("igst_amt");
			 JSONArray arr15 = obj.getJSONArray("invoice_item_seq");
			 JSONArray arr16 = obj.getJSONArray("item_id");
			 JSONArray arr17 = obj.getJSONArray("item_uom");
			 JSONArray arr18 = obj.getJSONArray("item_qty");
			 JSONArray arr19 = obj.getJSONArray("item_rate");
			 JSONArray arr20 = obj.getJSONArray("item_dis_rt");
			 JSONArray arr21 = obj.getJSONArray("total_dis_on_item");
			 
			 
			 for(int i=0; i<arr.length(); i++)
			 {
				
				 System.out.print(arr.getInt(i));
				 //PreparedStatement ps = con.prepareStatement(hdr);
					
				// ps.setInt(1, arr1.getInt(i));
					//ps.executeUpdate();
				 
			 }
			 
			 
			 
		 }
		 catch(Exception e)
		 {
			 System.out.println(e);
		 }
		 
		 
		
		
		
		return item;
	}
	
	
		
}

