package at.backend.MarketingCompany.crm.servicePackage.core.domain.exceptions;

public class ServicePackagePersistenceException extends RuntimeException {
  public ServicePackagePersistenceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServicePackagePersistenceException(String message) {
    super(message);
  }
}
