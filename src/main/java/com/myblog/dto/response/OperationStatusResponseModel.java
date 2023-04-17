package com.myblog.dto.response;

public class OperationStatusResponseModel {

	private String operationName;
	private String operationStatus;

	public OperationStatusResponseModel() {

	}

	public OperationStatusResponseModel(String operationName, String operationStatus) {
		this.operationName = operationName;
		this.operationStatus = operationStatus;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getOperationStatus() {
		return operationStatus;
	}

	public void setOperationStatus(String operationStatus) {
		this.operationStatus = operationStatus;
	}

}
