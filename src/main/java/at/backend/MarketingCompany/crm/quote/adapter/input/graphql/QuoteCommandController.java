package at.backend.MarketingCompany.crm.quote.adapter.input.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit;
import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.CreateQuoteInput;
import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.input.AddQuoteItemsInput;
import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.input.MarkQuoteAsAcceptedInput;
import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.input.MarkQuoteAsRejectedInput;
import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.input.MarkQuoteAsSentInput;
import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.input.UpdateQuoteDetailsInput;
import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.input.UpdateQuoteItemInput;
import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.output.QuoteOutput;
import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.mapper.QuoteOutputMapper;
import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.mapper.RemoveQuoteItemInput;
import at.backend.MarketingCompany.crm.quote.core.application.commands.AddQuoteItemsCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.DeleteQuoteCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.MarkQuoteAsAcceptedCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.MarkQuoteAsRejectedCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.MarkQuoteAsSentCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.RemoveQuoteItemCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.UpdateQuoteDetailsCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.UpdateQuoteItemCommand;
import at.backend.MarketingCompany.crm.quote.core.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.core.port.input.QuoteCommandInputPort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class QuoteCommandController {
  private final QuoteCommandInputPort commandPort;
  private final QuoteOutputMapper mapper;

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public QuoteOutput createQuote(@Argument @Valid @NotNull CreateQuoteInput input) {
    log.info("Creating new quote");
    var createQuoteCommand = input.toCommand();
    Quote quote = commandPort.createQuote(createQuoteCommand);
    return mapper.toOutput(quote);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public QuoteOutput addQuoteItems(@Argument @Valid @NotNull AddQuoteItemsInput input) {
    log.info("Adding items to quote: {}", input.quoteId());
    AddQuoteItemsCommand command = input.toCommand();
    Quote quote = commandPort.addQuoteItems(command);
    return mapper.toOutput(quote);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public QuoteOutput updateQuoteItem(@Argument @Valid @NotNull UpdateQuoteItemInput input) {
    log.info("Updating quote item: {}", input.itemId());
    UpdateQuoteItemCommand command = input.toCommand();
    Quote quote = commandPort.updateQuoteItem(command);
    return mapper.toOutput(quote);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public QuoteOutput removeQuoteItem(@Argument @Valid @NotNull RemoveQuoteItemInput input) {
    log.info("Removing quote item: {}", input.itemId());
    RemoveQuoteItemCommand command = input.toCommand();
    Quote quote = commandPort.removeQuoteItem(command);
    return mapper.toOutput(quote);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public QuoteOutput markQuoteAsSent(@Argument @Valid @NotNull MarkQuoteAsSentInput input) {
    log.info("Marking quote as sent: {}", input.quoteId());
    MarkQuoteAsSentCommand command = input.toCommand();
    var quote = commandPort.markQuoteAsSent(command);
    return mapper.toOutput(quote);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public QuoteOutput markQuoteAsAccepted(@Argument @Valid @NotNull MarkQuoteAsAcceptedInput input) {
    log.info("Marking quote as accepted: {}", input.quoteId());
    MarkQuoteAsAcceptedCommand command = input.toCommand();
    var quote = commandPort.markQuoteAsAccepted(command);
    return mapper.toOutput(quote);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public QuoteOutput markQuoteAsRejected(@Argument @Valid @NotNull MarkQuoteAsRejectedInput input) {
    log.info("Marking quote as rejected: {}", input.quoteId());
    MarkQuoteAsRejectedCommand command = input.toCommand();
    Quote quote = commandPort.markQuoteAsRejected(command);
    return mapper.toOutput(quote);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public QuoteOutput updateQuoteDetails(@Argument @Valid @NotNull UpdateQuoteDetailsInput input) {
    log.info("Updating quote details: {}", input.quoteId());
    UpdateQuoteDetailsCommand command = input.toCommand();
    Quote quote = commandPort.updateQuoteDetails(command);
    return mapper.toOutput(quote);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasRole('ADMIN')")
  public Boolean deleteQuote(@Argument String quoteId) {
    log.info("Deleting quote: {}", quoteId);
    var command = new DeleteQuoteCommand(QuoteId.of(quoteId));
    commandPort.deleteQuote(command);
    return true;
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
  public Boolean markExpiredQuotes() {
    log.info("Marking expired quotes");
    commandPort.markExpiredQuotes();
    return true;
  }
}
