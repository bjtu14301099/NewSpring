package factory;

public interface ApplicationContext {


String getId();


String getApplicationName();

String getDisplayName();

long getStartupDate();

ApplicationContext getParent();

//AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException;

}
