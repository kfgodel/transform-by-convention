package ar.com.kfgodel.transformbyconvention.impl.config;

import ar.com.kfgodel.convention.api.Convention;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.transformbyconvention.api.config.TransformerConfiguration;

/**
 * This type implements the configuration of a transformer with default convention config
 * Created by kfgodel on 20/03/16.
 */
public class TransformerConfigurationByConvention implements TransformerConfiguration {

  private DependencyInjector injector;

  public static TransformerConfigurationByConvention create(DependencyInjector injector) {
    TransformerConfigurationByConvention configuration = new TransformerConfigurationByConvention();
    configuration.injector = injector;
    return configuration;
  }

  @Override
  public DependencyInjector getInjector() {
    return injector;
  }

  @Override
  public Nary<String> getTransformerPackages() {
    return Nary.create(Convention.create().getConverterPackageNames());
  }
}
