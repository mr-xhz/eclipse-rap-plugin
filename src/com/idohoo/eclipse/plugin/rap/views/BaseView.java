package com.idohoo.eclipse.plugin.rap.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Control;

public abstract class BaseView {

	private static List<BaseView> views = new ArrayList<BaseView>();
	
	protected List<Control> controls = new ArrayList<Control>();
	
	protected BaseView(){
		views.add(this);
	}

	public void show(){
		for(BaseView baseView : views){
			baseView.hide();
		}
		controls.clear();
		this.create();
		this.afterShow();
	}
	
	public void hide(){
		for(Control control : controls){
			control.dispose();
		}
		controls.clear();
	}
	
	protected abstract void dispose();
	
	protected abstract void create();
	
	protected abstract void afterShow();
	
	private void disposeControl(){
		for(Control control : controls){
			control.dispose();
		}
		this.dispose();
	}
	
	public static void disposeALl(){
		for(BaseView baseView : views){
			baseView.disposeControl();
		}

		views.clear();
	}
}
