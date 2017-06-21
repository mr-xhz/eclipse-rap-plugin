package com.idohoo.eclipse.plugin.rap.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.idohoo.eclipse.plugin.rap.exception.RapException;
import com.idohoo.eclipse.plugin.rap.http.RapHttpClient;
import com.idohoo.eclipse.plugin.rap.util.ClassUtil;
import com.idohoo.eclipse.plugin.rap.util.JsonUtil;
import com.idohoo.eclipse.plugin.rap.util.StringUtil;
import com.idohoo.eclipse.plugin.rap.vo.ClassFieldVO;
import com.idohoo.eclipse.plugin.rap.vo.ClassMethodVO;
import com.idohoo.eclipse.plugin.rap.vo.ClassVO;
import com.idohoo.eclipse.plugin.rap.vo.rap.RapActionVO;
import com.idohoo.eclipse.plugin.rap.vo.rap.RapModuleVO;
import com.idohoo.eclipse.plugin.rap.vo.rap.RapPageVO;
import com.idohoo.eclipse.plugin.rap.vo.rap.RapParamVO;
import com.idohoo.eclipse.plugin.rap.vo.rap.RapProjectDataVO;
import com.idohoo.eclipse.plugin.rap.vo.rap.RapProjectVO;

public class EditView extends BaseView{
	
	private static EditView instance;
	
	private RapProjectVO project;
	
	private RapModuleVO module;
	
	private RapPageVO page;
	
	private Button saveBtn;
	
	private Text status;
	
	private ClassVO classVO;
	
	private org.eclipse.swt.widgets.List methodList;
	
	private static int randomId = 0;
	
	private EditView(){
		super();
	}
	
	public static EditView getInstance(){
		if(instance == null){
			instance = new EditView();
		}
		return instance;
	}
	
	public EditView init(RapProjectVO project,RapModuleVO module,RapPageVO page){
		this.project = project;
		this.module = module;
		this.page = page;
		return this;
	}

	@Override
	protected void dispose() {
		instance = null;
		
	}

	@Override
	protected void create() {
		
		Shell parent = RapView.getActiveView().getShell();
		
		classVO = ClassUtil.compile(RapView.getActiveView().getEditor());
		
		Group group = new Group(parent,SWT.CENTER);
		group.setText("Class:"+classVO.getClassName());
		group.setBounds(10, 5, 300, 500);
		
		methodList = new org.eclipse.swt.widgets.List(group,SWT.V_SCROLL | SWT.MULTI);
		methodList.setBounds(0, 0,295, 470);
		for(ClassMethodVO classMethodVO : classVO.getMethods()){
			methodList.add(classMethodVO.getName()+"("+classMethodVO.getDescription()+")");
		}
		methodList.addSelectionListener(new SelectionListener() {
			private int selectionCount = 0;
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				saveBtn.setEnabled(methodList.getSelectionCount() != 0);
				if(selectionCount != methodList.getSelectionCount()){
					selectionCount = methodList.getSelectionCount();
					status.append("已选:"+methodList.getSelectionCount()+"个"+System.lineSeparator());
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		Label label = new Label(parent,SWT.LEFT);
		String labelStr = "某项目 >> 某模块 >> 某页面";
		if(project != null && module != null && page != null){
			labelStr = project.getName()+" >> "+module.getName()+" >> "+page.getName();
		}
		label.setText(labelStr);
		label.setBounds(320, 10, 300, 20);
		
		status = new Text(parent,SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY);
		status.setBounds(320,40,300,400);
		
		saveBtn = new Button(parent,SWT.PUSH);
		saveBtn.setText("保存");
		saveBtn.setToolTipText("锁定并且保存");
		saveBtn.setBounds(320,450,300,55);
		saveBtn.setEnabled(false);
		saveBtn.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				EditView.getInstance().save();
			}
			@Override
			public void mouseDown(MouseEvent arg0) {}
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		
		this.controls.add(group);
		this.controls.add(methodList);
		this.controls.add(label);
		this.controls.add(status);
		this.controls.add(saveBtn);
		
	}
	
	private int getRandomId(){
		randomId-=1;
		return randomId;
	}
	
	private String getType(String type){
		if(type.indexOf("POST") != -1){
			return "2";
		}else if(type.indexOf("POST") != -1){
			return "1";
		}else if(type.indexOf("PUT") != -1){
			return "3";
		}else if(type.indexOf("DELETE") != -1){
			return "4";
		}
		return "2";
	}
	
	private String getDateType(ClassFieldVO classFieldVO){
		
		String type = "";
		if(ClassUtil.isJDKType(classFieldVO.getJavaType())){
			switch(classFieldVO.getJavaType()){
			case "int":case "Integer":
			case "float":case "Float":
			case "double":case "Double":
			case "long":case "Long":
			case "BigDecimal":case "BigInteger":
			case "Date":
				type = "number";
				break;
			case "char":case "Byte":case "String":
			case "Char":
				type = "string";
				break;
			case "boolean":case "Boolean":
				type = "boolean";
				break;
			}
		}else{
			type = "object";
		}
		
		if(classFieldVO.isList()){
			type = "array<"+type+">";
		}
		
		return type;
	}
	
	private RapParamVO getParam(ClassFieldVO classFieldVO){
		RapParamVO rapParamVO = new RapParamVO();
		rapParamVO.setId(getRandomId());
		rapParamVO.setDataType(getDateType(classFieldVO));
		rapParamVO.setIdentifier(classFieldVO.getName());
		rapParamVO.setName(classFieldVO.getComment());
		rapParamVO.setRemark(classFieldVO.getComment());
		List<RapParamVO> parameterList = new ArrayList<RapParamVO>();
		rapParamVO.setParameterList(parameterList);
		if(classFieldVO.getFields().size() > 0){
			for(ClassFieldVO subField : classFieldVO.getFields()){
				parameterList.add(getParam(subField));
			}
		}
		return rapParamVO;
	}
	
	private RapActionVO methodToAction(int pageId,ClassMethodVO classMethodVO){
		RapActionVO result = new RapActionVO();
		result.setPageId(pageId);
		result.setDescription(classMethodVO.getDescription());
		result.setId(getRandomId());
		result.setName(StringUtil.isEmpty(classMethodVO.getNickname())?(StringUtil.isEmpty(classMethodVO.getDescription())?classMethodVO.getName():classMethodVO.getDescription()):classMethodVO.getNickname());
		result.setResponseTemplate("");
		String url = classMethodVO.getUrl();
		if(StringUtil.isNotEmpty(classVO.getUrl())){
			url = classVO.getUrl() + url;
		}
		result.setRequestUrl(url);
		result.setRequestType(getType(classMethodVO.getMethod()));
		List<RapParamVO> responseParameterList = new ArrayList<RapParamVO>();
		result.setResponseParameterList(responseParameterList);
		if(classMethodVO.getResult() != null){
			if(ClassUtil.isJDKType(classMethodVO.getResult().getJavaType())){
				responseParameterList.add(getParam(classMethodVO.getResult()));
			}else if(classMethodVO.getResult().getFields().size() > 0){
				for(ClassFieldVO classFieldVO : classMethodVO.getResult().getFields()){
					responseParameterList.add(getParam(classFieldVO));
				}
			}
		}
		List<RapParamVO> requestParameterList = new ArrayList<RapParamVO>();
		result.setRequestParameterList(requestParameterList);
		if(classMethodVO.getParams().size() == 1){
			if(ClassUtil.isJDKType(classMethodVO.getParams().get(0).getJavaType())){
				requestParameterList.add(getParam(classMethodVO.getParams().get(0)));
			}else if(classMethodVO.getParams().get(0).getFields().size() > 0){
				for(ClassFieldVO classFieldVO : classMethodVO.getParams().get(0).getFields()){
					requestParameterList.add(getParam(classFieldVO));
				}
			}
		}else if(classMethodVO.getParams().size() >1){
			for(ClassFieldVO classFieldVO : classMethodVO.getParams()){
				requestParameterList.add(getParam(classFieldVO));
			}
		}
		return result;
	}
	
	private void save(){
		this.status.append("锁定中..."+System.lineSeparator());
		RapHttpClient rapHttpClient = RapHttpClient.getInstance();
		try {
			int projectId = project.getId();
			int moduleId = module.getId();
			int pageId = page.getId();
			RapProjectDataVO rapProjectDataVO = rapHttpClient.lock(projectId);
			this.status.append("锁定完成 projectId:"+projectId+System.lineSeparator());
			
			RapPageVO selectPage = null;
			for(RapModuleVO rapModuleVO : rapProjectDataVO.getModuleList()){
				if(rapModuleVO.getId().intValue() == moduleId){
					for(RapPageVO rapPageVO : rapModuleVO.getPageList()){
						if(rapPageVO.getId().intValue() == pageId){
							selectPage = rapPageVO;
							break;
						}
					}
					break;
				}
			}
			if(selectPage == null){
				this.status.append("找不到pageId:"+pageId+"的页面");
				return;
			}
			List<RapActionVO> liRapActionVO = selectPage.getActionList();
			if(liRapActionVO == null){
				liRapActionVO = new ArrayList<RapActionVO>();
				selectPage.setActionList(liRapActionVO);
			}
			int[] indices = methodList.getSelectionIndices();
			for(int index : indices){
				liRapActionVO.add(methodToAction(pageId,classVO.getMethods().get(index)));
			}
			rapHttpClient.checkIn(projectId, JsonUtil.stringify(rapProjectDataVO));
			this.status.append("保存成功 projectId:"+projectId+System.lineSeparator());
		} catch (RapException e) {
			this.status.append(e.getMessage()+System.lineSeparator());
		}
	}

	@Override
	protected void afterShow() {
		RapView.getActiveView().toScreenCenter(640, 560);
		
	}

}
