package at.backend.MarketingCompany.crm.quote.infrastructure.adapter.input.web;

import at.backend.MarketingCompany.crm.quote.application.dto.AddQuoteItemsCommand;
import at.backend.MarketingCompany.crm.quote.application.dto.GetQuoteByIdQuery;
import at.backend.MarketingCompany.crm.quote.application.input.QuoteQueryPort;
import at.backend.MarketingCompany.crm.quote.application.input.QuoteServicePort;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteItemId;
import at.backend.MarketingCompany.crm.quote.infrastructure.adapter.input.web.dto.QuoteInput;
import at.backend.MarketingCompany.crm.quote.infrastructure.adapter.input.web.dto.QuoteItemInput;
import at.backend.MarketingCompany.crm.quote.infrastructure.adapter.input.web.dto.QuoteOutput;
import at.backend.MarketingCompany.common.utils.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GraphQLQuoteController {
  private final QuoteResponseMapper responseMapper;
  private final QuoteServicePort commandPortHandler;
  private final QuoteQueryPort queryPortHandler;

  @QueryMapping
  public Page<QuoteOutput> getAllQuotes(@Argument PageInput input) {
    Pageable pageable = PageRequest.of(input.page(), input.size());
    var quotes = queryPortHandler.handle(pageable);
    return quotes.map(responseMapper::toResponse);
  }

  @QueryMapping
  public QuoteOutput getQuoteById(@Argument String id) {
    var getQuoteById = GetQuoteByIdQuery.from(id);
    var quote = queryPortHandler.handle(getQuoteById);
    return responseMapper.toResponse(quote);
  }

  @MutationMapping
  public QuoteOutput createQuote(@Valid @Argument QuoteInput input) {
    var createQuoteCommand = input.toCommand();
    var quote = commandPortHandler.handle(createQuoteCommand);

    return responseMapper.toResponse(quote);
  }

  @MutationMapping
  public QuoteOutput addQuoteItems(@Valid @Argument String id,
      @Argument @NotNull @Size(min = 1) List<QuoteItemInput> input) {

    var itemsCommands = input.stream()
        .map(QuoteItemInput::toCommand)
        .toList();
    var command = AddQuoteItemsCommand.from(id, itemsCommands);

    var quote = commandPortHandler.handle(command);

    return responseMapper.toResponse(quote);
  }

  @MutationMapping
  public QuoteOutput deleteQuoteItem(@Valid @Argument String itemId) {
    var quote = commandPortHandler.handle(QuoteItemId.of(itemId));
    return responseMapper.toResponse(quote);
  }

  @MutationMapping
  public QuoteOutput deleteQuote(@Argument String id) {
    var quote = commandPortHandler.handle(QuoteId.of(id));
    return responseMapper.toResponse(quote);
  }
}
