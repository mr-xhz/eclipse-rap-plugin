package com.idohoo.eclipse.plugin.rap.vo.rap;

import java.util.Date;
import java.util.List;

public class TestVO {

	private String name;

	private Integer INT;
	
	private Long LONG;
	
	private Boolean BOOLEAN;
	
	private TestVO testVO;
	
	private List<TestVO> listInt;
	
	private Date DATE;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getINT() {
		return INT;
	}

	public void setINT(Integer iNT) {
		INT = iNT;
	}

	public Long getLONG() {
		return LONG;
	}

	public void setLONG(Long lONG) {
		LONG = lONG;
	}

	public Boolean getBOOLEAN() {
		return BOOLEAN;
	}

	public void setBOOLEAN(Boolean bOOLEAN) {
		BOOLEAN = bOOLEAN;
	}
	
	

	public TestVO getTestVO() {
		return testVO;
	}

	public void setTestVO(TestVO testVO) {
		this.testVO = testVO;
	}
	
	

	public List<TestVO> getListInt() {
		return listInt;
	}

	public void setListInt(List<TestVO> listInt) {
		this.listInt = listInt;
	}
	
	

	public Date getDATE() {
		return DATE;
	}

	public void setDATE(Date dATE) {
		DATE = dATE;
	}

	@Override
	public String toString() {
		return "TestVO [name=" + name + ", INT=" + INT + ", LONG=" + LONG + ", BOOLEAN=" + BOOLEAN + ", testVO="
				+ testVO + ", listInt=" + listInt + ", DATE=" + DATE + "]";
	}

	
	
	
	
	
}
