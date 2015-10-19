package com.autonomy.abc.selenium.config;

// could remove this to fully separate hosted/on-prem, but left for convenience
// allows shared tests to do a simple "if hosted then x else y"
// instead of excessively complicated polymorphic method calls
public enum ApplicationType {
	ON_PREM("On Premise", "com.autonomy.abc.selenium.config.OPApplication"),
	HOSTED("Hosted", "com.autonomy.abc.selenium.config.HSOApplication");

	private final String name;
	private final String className;

	ApplicationType(final String name, final String className) {
		this.name = name;
		this.className = className;
	}

	public String getName() {
		return name;
	}

	// instead exposed as factory method Application.ofType
	Application makeApplication() {
		try {
			return (Application) Class.forName(className).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new IllegalStateException("Could not create Application object - check that the correct mockui package is included in the POM", e);
		}
	}
}
