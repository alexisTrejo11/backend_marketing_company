package at.backend.MarketingCompany.shared;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import graphql.schema.DataFetchingEnvironment;

public class AppContext {
  public static UserId getUserIdFromContext(DataFetchingEnvironment env) {
    String userId = env.getGraphQlContext().get("userId");
    if (userId == null) {
      throw new SecurityException("User not authenticated");
    }

    return UserId.of(userId);
  }
}
