package com.ichangemycity.appdata;

import java.lang.reflect.Method;

public class StrictMode {

	public final void StrictModeMethod() {
		try {
			Class<?> strictModeClass = Class.forName("android.os.StrictMode");
			Class<?> strictModeThreadPolicyClass = Class
					.forName("android.os.StrictMode$ThreadPolicy");
			Object laxPolicy = strictModeThreadPolicyClass.getField("LAX").get(
					null);
			Method method_setThreadPolicy = strictModeClass.getMethod(
					"setThreadPolicy", strictModeThreadPolicyClass);
			method_setThreadPolicy.invoke(null, laxPolicy);
		} catch (Exception e) {

		}
	}
}