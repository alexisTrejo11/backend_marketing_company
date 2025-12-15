package at.backend.MarketingCompany.crm.quote.adapter.input.graphql;

import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.QuoteInput;
import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.input.AddQuotesItemsInput;
import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.output.QuoteOutput;
import at.backend.MarketingCompany.crm.quote.adapter.input.graphql.mapper.QuoteResponseMapper;
import at.backend.MarketingCompany.crm.quote.core.application.dto.AddQuoteItemsCommand;
import at.backend.MarketingCompany.crm.quote.core.application.dto.GetQuoteByIdQuery;
import at.backend.MarketingCompany.crm.quote.core.application.input.QuoteCommandService;
import at.backend.MarketingCompany.crm.quote.core.application.input.QuoteQueryService;

import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteItemId;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class QuoteController {
  private final QuoteResponseMapper responseMapper;
  private final QuoteCommandService commandService;
  private final QuoteQueryService queryService;

  @QueryMapping
  public Page<QuoteOutput> getAllQuotes(@Argument PageInput input) {
    var quotes = queryService.getAllQuotes(input.toPageable());
    return quotes.map(responseMapper::toResponse);
  }

  @QueryMapping
  public QuoteOutput getQuoteById(@Argument String id) {
    var getQuoteById = GetQuoteByIdQuery.from(id);
    var quote = queryService.getQuoteById(getQuoteById);
    return responseMapper.toResponse(quote);
  }

  @MutationMapping
  public QuoteOutput createQuote(@Valid @Argument QuoteInput input) {
    var createQuoteCommand = input.toCommand();
    var quote = commandService.createQuote(createQuoteCommand);

    return responseMapper.toResponse(quote);
  }

  @MutationMapping
  public QuoteOutput addQuoteItems(@Valid @Argument AddQuotesItemsInput input) {
    AddQuoteItemsCommand command = input.toCommand();
    var quote = commandService.addQuoteItems(command);

    return responseMapper.toResponse(quote);
  }

  @MutationMapping
  public QuoteOutput deleteQuoteItem(@Valid @Argument String itemId) {
    var quote = commandService.deleteQuoteItem(QuoteItemId.of(itemId));
    return responseMapper.toResponse(quote);
  }

  @MutationMapping
  public QuoteOutput deleteQuote(@Argument String id) {
    var quote = commandService.deleteQuote(QuoteId.of(id));
    return responseMapper.toResponse(quote);
  }
}
