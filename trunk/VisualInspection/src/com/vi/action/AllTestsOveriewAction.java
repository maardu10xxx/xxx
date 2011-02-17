package com.vi.action;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleTypes;

import org.apache.commons.dbcp.BasicDataSource;

import com.opensymphony.xwork2.ActionSupport;
import com.vi.data.FailureInformation;
import com.vi.data.FailureReportData;
import com.vi.data.FormData;
import com.vi.data.Tested;

public class AllTestsOveriewAction extends ActionSupport{
	//DataSource
	private BasicDataSource dataSource;
	
	private FormData formData;
	private FailureReportData failureReportData;
	
	//lists for storing the database query results
	//so they can be iterated by Struts2
	private ArrayList<Tested> passedList;
	private ArrayList<Tested> preTreatmentErrorList;
	private ArrayList<FailureInformation> failedList;
	
	private String nrOfPassed;
	private String nrOfFailed;
	
	//needed to call edit page
	private String fid;
	private String workstationNr;
	private String positionNr;
	private String partname;
	private String failureCode;
	private String failureDescription;
	private String operatorID;
	private String confirmation;
	
	private String oldFailureCode;
	
	private boolean retrievedData;

	/**
	 * Action for linking to the all tests overview page
	 * 
	 * @return Result which will be processed by Struts2
	 */
	public String openPoAllTestsOverview() {
		return SUCCESS;
	}
	
	/**
	 * Function to get the data from the database
	 * to populate the lists and set the counts
	 */
	private void retrieveData(){
		passedList=new ArrayList<Tested>();
		preTreatmentErrorList=new ArrayList<Tested>();
		failedList=new ArrayList<FailureInformation>();
	
		Connection conn = null;
		CallableStatement coll = null;
		int nrOfPassedInt=0;
		int nrOfFailedInt=0;
		try {
			//query database for the information
			conn = dataSource.getConnection();
			//calling the stored procedure which returns a cursor to the data
			// PROCEDURE GET_ALL_PASSED(in_po_no IN VARCHAR2,in_item_no IN VARCHAR2,in_as_ IN VARCHAR2,
			//in_workstation_no IN VARCHAR2,v_cursor_passed OUT CURSOR_IF);
			coll = conn.prepareCall("{call PCBVI.PKG_TESTRECORDS.GET_ALL_PASSED(?,?,?,?,?)}");
			//prepare input and output arguments 
			coll.setString(1, formData.getPoNo());			
			coll.setString(2, formData.getItemNr());
			coll.setString(3, formData.getVersionAS());
			coll.setString(4, formData.getWorkstationNr());

			coll.registerOutParameter(5, OracleTypes.CURSOR);
			
			coll.execute();
			//get cursor
			ResultSet rs = (ResultSet) coll.getObject(5);
			//and process data
			while (rs.next()) {
				Tested tested=new Tested();
				
				tested.setFid(rs.getString(1));
				tested.setOperatorID(rs.getString(2));
				tested.setTime(rs.getString(3));
				String confirm=rs.getString(4);
				//FAKE is Y if confirmation is N and vice versa
				if(confirm.equals("Y")){
					tested.setConfirmation("N");
				}
				else if(confirm.equals("N")){
					tested.setConfirmation("Y");
				}		
				
				passedList.add(tested);
				//count all passed POs
				nrOfPassedInt++;
				
			}
			coll.close();
			rs=null;
			//calling the stored procedure which returns a cursor to the data
			//PROCEDURE GET_ALL_FAILED_NO_F_RECORD(in_po_no IN VARCHAR2,in_item_no IN VARCHAR2,
			//in_as_ IN VARCHAR2,in_workstation_no IN VARCHAR2,v_cursor_failed_no_rec OUT CURSOR_IF);
			coll = conn.prepareCall("{call PCBVI.PKG_TESTRECORDS.GET_ALL_FAILED_NO_F_RECORD(?,?,?,?,?)}");
			//prepare input and output arguments 
			coll.setString(1, formData.getPoNo());			
			coll.setString(2, formData.getItemNr());
			coll.setString(3, formData.getVersionAS());
			coll.setString(4, formData.getWorkstationNr());

			coll.registerOutParameter(5, OracleTypes.CURSOR);
			
			coll.execute();
			//get cursor
			rs = (ResultSet) coll.getObject(5);
			//and process data
			while (rs.next()) {
				Tested tested=new Tested();
				
				tested.setFid(rs.getString(1));
				tested.setOperatorID(rs.getString(2));
				tested.setTime(rs.getString(3));
				String confirm=rs.getString(4);
				//FAKE is Y if confirmation is N and vice versa
				if(confirm.equals("Y")){
					tested.setConfirmation("N");
				}
				else if(confirm.equals("N")){
					tested.setConfirmation("Y");
				}		

				preTreatmentErrorList.add(tested);
				
			}
			coll.close();
			rs=null;
			//calling the stored procedure which returns a cursor to the data
			//PROCEDURE GET_ALL_FAILED_HAS_F_RECORD(in_po_no IN VARCHAR2,in_item_no IN VARCHAR2,
			//in_as_ IN VARCHAR2,in_workstation_no IN VARCHAR2,v_cursor_failed_has_rec OUT CURSOR_IF);                        			
			coll = conn.prepareCall("{call PCBVI.PKG_TESTRECORDS.GET_ALL_FAILED_HAS_F_RECORD(?,?,?,?,?)}");
			//prepare input and output arguments 
			coll.setString(1, formData.getPoNo());			
			coll.setString(2, formData.getItemNr());
			coll.setString(3, formData.getVersionAS());
			coll.setString(4, formData.getWorkstationNr());

			coll.registerOutParameter(5, OracleTypes.CURSOR);
			
			coll.execute();
			//get cursor
			rs = (ResultSet) coll.getObject(5);
			//and process data
			while (rs.next()) {			   
				FailureInformation failureInformation=new FailureInformation();
				
				failureInformation.setFid(rs.getString(1));
				failureInformation.setPieceLocation(rs.getString(2));
				failureInformation.setComponentLocation(rs.getString(3));
				failureInformation.setFailureCode(rs.getString(4));
				failureInformation.setFailureDefinition(rs.getString(5));
				failureInformation.setFailureDescription(rs.getString(6));
				failureInformation.setTime(rs.getString(7));
				failureInformation.setOperatorID(rs.getString(8));				
				String confirm=rs.getString(9);
				//FAKE is Y if confirmation is N and vice versa
				if(confirm.equals("Y")){
					failureInformation.setConfirmation("N");
				}
				else if(confirm.equals("N")){
					failureInformation.setConfirmation("Y");
				}		
				failedList.add(failureInformation);
				//count all failed POs
				nrOfFailedInt++;
				
			}
			coll.close();
			rs=null;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (coll != null) {
				try {
					coll.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
		//convert numbers to strings to pass them to Struts2
		nrOfPassed=String.valueOf(nrOfPassedInt);
		nrOfFailed=String.valueOf(nrOfFailedInt);;
		
		retrievedData=true;
	}
	
	/**
	 * Delete a record from the TAB_TESTED database table
	 * 
	 * @return Result which will be processed by Struts2
	 */
	public String deleteTested(){
		
		Connection conn = null;
		CallableStatement coll = null;
		try {
			conn = dataSource.getConnection();
			//calling the stored procedure which deletes a test entry
			//PROCEDURE DELETE_TESTED( in_fid IN VARCHAR2,in_workstation_no IN VARCHAR2); 
			coll = conn.prepareCall("{call PCBVI.PKG_TESTRECORDS.DELETE_TESTED(?,?)}");
			//prepare input and output arguments 
			coll.setString(1, fid);
			coll.setString(2, workstationNr);			
			
			coll.execute();		
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (coll != null) {
				try {
					coll.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 

		return SUCCESS;
	}
	
	/**
	 * Delete a record from the TAB_FAILURE database table
	 * 
	 * @return Result which will be processed by Struts2
	 */
	public String delFailureInformation(){
		
		Connection conn = null;
		CallableStatement coll = null;
		try {
			conn = dataSource.getConnection();
			//calling the stored procedure which deletes a failure entry
			//PROCEDURE DELETE_FAILURE(in_fid IN VARCHAR2,in_workstation_no IN VARCHAR2,                        
            //in_piece_location IN VARCHAR2,in_component_location IN VARCHAR2,
            //in_failure_code IN VARCHAR2);  
			coll = conn.prepareCall("{call PCBVI.PKG_TESTRECORDS.DELETE_FAILURE(?,?,?,?,?)}");
			//prepare input and output arguments 
			coll.setString(1, fid);
			coll.setString(2, workstationNr);
			coll.setString(3, positionNr);
			coll.setString(4, partname);
			coll.setString(5, failureCode);
			
			coll.execute();		
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (coll != null) {
				try {
					coll.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 

		
		return SUCCESS;
	}

	/**
	 * Action for linking to the edit failure page
	 * 
	 * @return Result which will be processed by Struts2
	 */
	public String goToEditFailureInformation() {
		oldFailureCode=failureCode;
		return SUCCESS;
	}
	
	/**
	 * Change a record in the TAB_FAILURE database table
	 * 
	 * @return Result which will be processed by Struts2
	 */
	public String editFailureInformation(){
		
		Connection conn = null;
		CallableStatement coll = null;
		try {
			conn = dataSource.getConnection();
			//calling the stored procedure which edits a failure in TAB_FAILURE
			//PROCEDURE EDIT_FAILURE(in_fid IN VARCHAR2,in_workstation_no IN VARCHAR2,                        
            //in_piece_location IN VARCHAR2,in_component_location IN VARCHAR2,
            //in_old_failure_code IN VARCHAR2,in_new_failure_code IN VARCHAR2,
			//in_failure_description IN VARCHAR2);
			coll = conn.prepareCall("{call PCBVI.PKG_TESTRECORDS.EDIT_FAILURE(?,?,?,?,?,?,?)}");
			//prepare input and output arguments 
			coll.setString(1, fid);
			coll.setString(2, workstationNr);
			coll.setString(3, positionNr);
			coll.setString(4, partname);
			coll.setString(5, oldFailureCode);
			coll.setString(6, failureCode);			
			coll.setString(7, failureDescription);
			
			coll.execute();		
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (coll != null) {
				try {
					coll.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
		
		return SUCCESS;
	}
	
	/**
	 * Skip changing a record and return to AllTestsOverview
	 * 
	 * @return Result which will be processed by Struts2
	 */
	public String cancelEditFailureInformation(){
		
		return SUCCESS;
	}
	
	
	
	public BasicDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(BasicDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public FormData getFormData() {
		return formData;
	}

	public void setFormData(FormData formData) {
		this.formData = formData;
	}

	public ArrayList<Tested> getPassedList() {
		//only retrieve the data once, makes no sense to split query into many queries
		//but have to make sure it's done when the first getter is called
		if(!retrievedData){
			retrieveData();
		}
		return passedList;
	}

	public void setPassedList(ArrayList<Tested> passedList) {
		this.passedList = passedList;
	}

	public ArrayList<Tested> getPreTreatmentErrorList() {
		//only retrieve the data once, makes no sense to split query into many queries
		//but have to make sure it's done when the first getter is called
		if(!retrievedData){
			retrieveData();
		}
		return preTreatmentErrorList;
	}

	public void setPreTreatmentErrorList(ArrayList<Tested> preTreatmentErrorList) {
		this.preTreatmentErrorList = preTreatmentErrorList;
	}

	public ArrayList<FailureInformation> getFailedList() {
		//only retrieve the data once, makes no sense to split query into many queries
		//but have to make sure it's done when the first getter is called
		if(!retrievedData){
			retrieveData();
		}
		return failedList;
	}

	public void setFailedList(ArrayList<FailureInformation> failedList) {
		this.failedList = failedList;
	}

	public String getNrOfPassed() {
		//only retrieve the data once, makes no sense to split query into many queries
		//but have to make sure it's done when the first getter is called
		if(!retrievedData){
			retrieveData();
		}
		return nrOfPassed;
	}

	public void setNrOfPassed(String nrOfPassed) {
		this.nrOfPassed = nrOfPassed;
	}

	public String getNrOfFailed() {
		//only retrieve the data once, makes no sense to split query into many queries
		//but have to make sure it's done when the first getter is called
		if(!retrievedData){
			retrieveData();
		}
		return nrOfFailed;
	}

	public void setNrOfFailed(String nrOfFailed) {
		this.nrOfFailed = nrOfFailed;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getWorkstationNr() {
		return workstationNr;
	}

	public void setWorkstationNr(String workstationNr) {
		this.workstationNr = workstationNr;
	}

	public String getPositionNr() {
		return positionNr;
	}

	public void setPositionNr(String positionNr) {
		this.positionNr = positionNr;
	}

	public String getPartname() {
		return partname;
	}

	public void setPartname(String partname) {
		this.partname = partname;
	}

	public String getFailureCode() {
		return failureCode;
	}

	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}

	public String getFailureDescription() {
		return failureDescription;
	}

	public void setFailureDescription(String failureDescription) {
		this.failureDescription = failureDescription;
	}

	public FailureReportData getFailureReportData() {
		//make sure that failure codes are retrieved from DB
		failureReportData.init(dataSource);
		return failureReportData;
	}

	public void setFailureReportData(FailureReportData failureReportData) {
		this.failureReportData = failureReportData;
	}

	public String getOldFailureCode() {
		return oldFailureCode;
	}

	public void setOldFailureCode(String oldFailureCode) {
		this.oldFailureCode = oldFailureCode;
	}

	public String getOperatorID() {
		return operatorID;
	}

	public void setOperatorID(String operatorID) {
		this.operatorID = operatorID;
	}

	public String getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}
			
	
}
