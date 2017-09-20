package com.justedit.hotcodeoptions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaHotCodeReplaceListener;
import org.eclipse.jdt.debug.core.JDIDebugModel;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements IStartup{

	// The plug-in ID
	public static final String PLUGIN_ID = "HotCodeOptions"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
				
		super.start(context);
		plugin = this;

		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	@Override
	public void earlyStartup() {
		
		System.out.println("Hotcode-Options: Activator.earlyStartup()");
		
		JDIDebugModel.addHotCodeReplaceListener(new IJavaHotCodeReplaceListener() {

			@Override
			public void obsoleteMethods(IJavaDebugTarget target) {
				System.out.println("Activator.start(...).new IJavaHotCodeReplaceListener() {...}.obsoleteMethods()");
			}

			@Override
			public void hotCodeReplaceSucceeded(IJavaDebugTarget target) {
				System.out.println(
						"Activator.start(...).new IJavaHotCodeReplaceListener() {...}.hotCodeReplaceSucceeded()");
			}

			@Override
			public void hotCodeReplaceFailed(IJavaDebugTarget target, DebugException exception) {
				System.out
						.println("Activator.start(...).new IJavaHotCodeReplaceListener() {...}.hotCodeReplaceFailed()");

				try {

					ILaunch launch = target.getLaunch();
					
					ILaunchConfiguration config = launch.getLaunchConfiguration();
					if (config != null && config.exists() && config.getAttribute(HotCodeTab.RESTART_ON_HCR_FAILS, false)) {
						
						System.out.println("Restarting launch");
						launch.terminate();
						
						Display.getDefault().asyncExec(new Runnable() {
							
							@Override
							public void run() {
								try {
									config.launch(launch.getLaunchMode(), null);
								} catch (CoreException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								//DebugUIPlugin.launchInBackground(config, launch.getLaunchMode());
							}
						});
					}

				} catch (CoreException e) {
					e.printStackTrace();
				} 

			}
		});
		
	}

}
