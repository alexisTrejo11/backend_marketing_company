package at.backend.MarketingCompany.crm.servicePackage.domain.exceptions;

public class ServicePackagePersistenceException extends RuntimeException {
  public ServicePackagePersistenceException(String message, Throwable cause) {
    super(message, cause);
  }
}
