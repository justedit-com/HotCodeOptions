package com.justedit.hotcodeoptions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class HotCodeTab extends AbstractLaunchConfigurationTab{
	
	static final String RESTART_ON_HCR_FAILS = "restartOnHCRfails";
	
	private Button checkButton;

	@Override
	public void createControl(Composite parent) {
		
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		comp.setLayout(new GridLayout(2, true));
		
		checkButton = super.createCheckButton(comp, "Restart application when hot code replacement fails");
		checkButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		
		System.out.println("setDirty(false)");
		setDirty(false);
		
		
		checkButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("setting dirty state");
				setDirty(true);
				getLaunchConfigurationDialog().updateButtons();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		checkButton.setSelection(false);
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			boolean restartOnHCRfailsActive = configuration.getAttribute(RESTART_ON_HCR_FAILS, false);
			checkButton.setSelection(restartOnHCRfailsActive);
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		boolean selected = checkButton.getSelection();
		configuration.setAttribute(RESTART_ON_HCR_FAILS, selected);
		System.out.println("Setting RESTART_ON_HCR_FAILS to " + selected);
		
	}

	@Override
	public String getName() {
		return "Hot Code";
	}

	@Override
	public Image getImage() {
		return new Image(Display.getCurrent(), HotCodeTab.class.getResourceAsStream("fire.png"));
	}

}
