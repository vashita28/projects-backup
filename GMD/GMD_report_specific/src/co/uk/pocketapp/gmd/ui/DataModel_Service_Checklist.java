package co.uk.pocketapp.gmd.ui;

public class DataModel_Service_Checklist {
	
	public String report_Title;
	public String per_Completed;
	public String type_of_completion;
	
	public DataModel_Service_Checklist(String reportTitle, String percentage_Completed, String typeOfCompletion){
		this.report_Title=reportTitle;
		this.per_Completed=percentage_Completed;
		this.type_of_completion=typeOfCompletion;
	}

	public String getReport_Title() {
		return report_Title;
	}

	public void setReport_Title(String report_Title) {
		this.report_Title = report_Title;
	}

	public String getPer_Completed() {
		return per_Completed;
	}

	public void setPer_Completed(String per_Completed) {
		this.per_Completed = per_Completed;
	}

	public String getType_of_completion() {
		return type_of_completion;
	}

	public void setType_of_completion(String type_of_completion) {
		this.type_of_completion = type_of_completion;
	}

}
