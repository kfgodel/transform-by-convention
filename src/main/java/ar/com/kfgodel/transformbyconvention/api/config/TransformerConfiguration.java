package ar.com.kfgodel.transformbyconvention.api.config;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.nary.api.Nary;

/**
 * this type represents the configuration needed by the tranformer to setup
 * Created by kfgodel on 20/03/16.
 */
public interface TransformerConfiguration {
  /**
   * @return The dependency injector needed by converters to fetch dependencies
   */
  DependencyInjector getInjector();

  /**
   * @return The name of packages to look for transformer classes
   */
  Nary<String> getTransformerPackages();
}
