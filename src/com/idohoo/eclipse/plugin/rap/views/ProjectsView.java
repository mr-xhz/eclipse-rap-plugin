package com.idohoo.eclipse.plugin.rap.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.idohoo.eclipse.plugin.rap.exception.RapException;
import com.idohoo.eclipse.plugin.rap.http.RapHttpClient;
import com.idohoo.eclipse.plugin.rap.util.StringUtil;
import com.idohoo.eclipse.plugin.rap.vo.rap.RapModuleVO;
import com.idohoo.eclipse.plugin.rap.vo.rap.RapPageVO;
import com.idohoo.eclipse.plugin.rap.vo.rap.RapProjectVO;
import com.idohoo.eclipse.plugin.rap.vo.rap.RapWorkspaceVO;

public class ProjectsView extends BaseView {
	private static ProjectsView instance;
	
	private Label status;
	
	private Combo projects;
	
	private Combo modules;
	
	private Combo pages;
	
	private List<RapProjectVO> rapProjects = new ArrayList<RapProjectVO>();
	
	private List<RapModuleVO> rapModules = new ArrayList<RapModuleVO>();
	
	private Button retryBtn;
	
	private Button nextBtn;
	
	private ProjectsView(){
		super();
	}
	
	@Override
	protected void create(){
		Shell parent = RapView.getActiveView().getShell();
		
		projects = new Combo(parent,SWT.READ_ONLY);
		projects.setBounds(10,10,200,30);
		projects.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ProjectsView.getInstance().setStatus("SELECTED_PROJECT","");
				ProjectsView.getInstance().initModules();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		modules = new Combo(parent,SWT.READ_ONLY);
		modules.setBounds(220,10,200,30);
		modules.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ProjectsView.getInstance().setStatus("SELECTED_MODULE","");
				ProjectsView.getInstance().initPages();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		pages = new Combo(parent,SWT.READ_ONLY);
		pages.setBounds(430,10,200,30);
		pages.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ProjectsView.getInstance().setStatus("SELECTED_PAGE","");
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		status = new Label(parent,SWT.CENTER);
		status.setBounds(10,45,640,20);
		status.setText("加载中...");
		
		nextBtn = new Button(parent,SWT.PUSH);
		nextBtn.setToolTipText("下一步");
		nextBtn.setBounds(420, 80,100,40);
		nextBtn.setText("下一步");
		nextBtn.setEnabled(false);
		nextBtn.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				ProjectsView.getInstance().next();
			}
			@Override
			public void mouseDown(MouseEvent arg0) {}
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		
		retryBtn = new Button(parent,SWT.PUSH);
		retryBtn.setToolTipText("重新来过");
		retryBtn.setBounds(530, 80,100,40);
		retryBtn.setText("重试");
		retryBtn.setEnabled(false);
		retryBtn.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent arg0) {
				ProjectsView.getInstance().initProject();
			}
			@Override
			public void mouseDown(MouseEvent arg0) {}
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
		});
		
		/////////////////////////////////////////////////////////////////////////////////
		this.controls.add(status);
		this.controls.add(projects);
		this.controls.add(modules);
		this.controls.add(pages);
		this.controls.add(retryBtn);
		this.controls.add(nextBtn);
		////////////////////////////////////////////////////////////////////////////////////
		this.initProject();
	}
	
	private void setStatus(String status,String errMsg){
		switch(status){
		case "LOADING":
			this.status.setText(StringUtil.isEmpty(errMsg)?"加载中...":errMsg);
			this.retryBtn.setEnabled(false);
			this.nextBtn.setEnabled(false);
			break;
		case "SUCCESS":
			this.status.setText(StringUtil.isEmpty(errMsg)?"请选择一个项目":errMsg);
			this.retryBtn.setEnabled(true);
			this.nextBtn.setEnabled(false);
			break;
		case "NONE":
			this.status.setText(StringUtil.isEmpty(errMsg)?"找不到":errMsg);
			this.retryBtn.setEnabled(true);
			this.nextBtn.setEnabled(false);
			break;
		case "ERROR":
			this.status.setText(errMsg);
			this.retryBtn.setEnabled(true);
			this.nextBtn.setEnabled(false);
			break;
		case "SELECTED_PROJECT":
			int index = this.projects.getSelectionIndex();
			if(index < 1){
				this.status.setText("请选择一个项目");
				this.retryBtn.setEnabled(true);
				this.nextBtn.setEnabled(false);
			}
			break;
		case "SELECTED_MODULE":
			int moduleIndex = this.modules.getSelectionIndex();
			if(moduleIndex < 1){
				this.status.setText("请选择一个模块");
				this.retryBtn.setEnabled(true);
				this.nextBtn.setEnabled(false);
			}
			break;
		case "SELECTED_PAGE":
			int pageIndex = this.pages.getSelectionIndex();
			this.retryBtn.setEnabled(true);
			if(pageIndex < 1){
				this.status.setText("请选择一个页面");
				this.nextBtn.setEnabled(false);
			}else{
				this.nextBtn.setEnabled(true);
			}
			break;
		default:
			break;
		}
		this.status.getParent().layout();
	} 
	
	private int getProjectId(){
		int index = this.projects.getSelectionIndex() - 1;
		if(index >= 0){
			RapProjectVO rapProjectVO = this.rapProjects.get(index);
			return rapProjectVO.getId();
		}
		return 0;
	}
	
	private void next(){
		int index = this.projects.getSelectionIndex() - 1;
		RapProjectVO project = this.rapProjects.get(index);
		index = this.modules.getSelectionIndex() - 1;
		RapModuleVO module = this.rapModules.get(index);
		index = this.pages.getSelectionIndex() - 1;
		RapPageVO page = module.getPageList().get(index);
		EditView.getInstance().init(project, module, page).show();
	}
	
	private void initProject(){

		this.projects.removeAll();
		this.projects.add("--请选择一个项目--");
		this.projects.select(0);
		initModules();
		if(this.rapProjects != null){
			this.rapProjects.clear();
		}
		setStatus("LOADING","");
		RapHttpClient rapHttpClient = RapHttpClient.getInstance();
		try {
			rapProjects = rapHttpClient.listProject();
			if(rapProjects == null || rapProjects.size() == 0){
				setStatus("NONE","请先新建一个项目");
				return;
			}
			for(RapProjectVO rapProjectVO : rapProjects){
				this.projects.add(rapProjectVO.getName());
			}
			setStatus("SUCCESS","请选择一个项目");
		} catch (RapException e) {
			setStatus("ERROR",e.getMessage());
		}
	}
	
	private void initModules(){
		this.modules.removeAll();
		this.modules.add("--请选择一个模块--");
		this.modules.select(0);
		if(rapModules != null){
			rapModules.clear();
		}
		int projectId = this.getProjectId();
		RapHttpClient rapHttpClient = RapHttpClient.getInstance();
		if(projectId != 0){
			try {
				RapWorkspaceVO rapWorkspaceVO = rapHttpClient.loadWorkspae(projectId);
				rapModules = rapWorkspaceVO.getProjectData().getModuleList();
				if(rapModules == null || rapModules.size() == 0){
					setStatus("NONE","请先新建一个模块");
					return;
				}
				for(RapModuleVO rapModuleVO : rapModules){
					this.modules.add(rapModuleVO.getName());
				}
				setStatus("SUCCESS","请选择一个模块");
			} catch (RapException e) {
				setStatus("ERROR",e.getMessage());
			}
		}
		initPages();
	}
	
	private void initPages(){
		this.pages.removeAll();
		this.pages.add("--请选择一个页面--");
		this.pages.select(0);
		int moduleIndex = this.modules.getSelectionIndex() - 1;
		if(moduleIndex >= 0){
			List<RapPageVO> liRapPageVO = this.rapModules.get(moduleIndex).getPageList();
			if(liRapPageVO == null || liRapPageVO.size() == 0){
				setStatus("NONE","请先新建一个页面");
				return;
			}
			for(RapPageVO rapPageVO : liRapPageVO){
				this.pages.add(rapPageVO.getName());
			}
			setStatus("SUCCESS","请选择一个页面");
		}
	}
	
	@Override
	protected void afterShow(){
		RapView.getActiveView().toScreenCenter(640, 170);
	}
	
	public static ProjectsView getInstance(){
		if(instance == null){
			instance = new ProjectsView();
		}
		return instance;
	}
	
	@Override
	protected void dispose(){
		instance = null;
	}
}
