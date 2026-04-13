package com.example.apa_project.model;

public class CompanyRepresentative extends User {

	private String positionTitle;

		public CompanyRepresentative(int id,String name, String email , String password , String positionTitle) {
			super(id,name,email,password);
			this.positionTitle = positionTitle;
		}

		public String getPositionTitle() {
			return positionTitle;
		}

		public void setPositionTitle(String positionTitle) {
			this.positionTitle = positionTitle;
		}

}

