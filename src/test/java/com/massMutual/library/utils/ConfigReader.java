package com.massMutual.library.utils;

import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

public class ConfigReader {

  private static EnvironmentVariables environmentVariables;

  static {
    environmentVariables =
            SystemEnvironmentVariables.createEnvironmentVariables();
  }

  public static String getProperty(String keyword) {
    return EnvironmentSpecificConfiguration
            .from(environmentVariables)
            .getProperty(keyword);
  }
}
